(defn constant [value] (constantly value))
(defn variable [name] #(% name))

(defn operation [calc]
  (fn [& args]
    (fn [vars]
      (apply calc (mapv #(% vars) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation #(/ %1 (double %2))))
(def negate (operation -))
(def min (operation clojure.core/min))
(def max (operation clojure.core/max))

(def FUNCTIONAL
  {'+       add,
   '-       subtract,
   '*       multiply,
   '/       divide,
   'negate  negate,
   'min     min,
   'max     max})

(defn parse-functional [expr]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    :else (apply
            (FUNCTIONAL (first expr))
            (mapv parse-functional (rest expr)))))

(defn parseFunction [expr]
  (parse-functional (read-string expr)))

; ################################################################################### ;
(defprotocol IExpression
  (evaluate [_ vars])
  (diff [_ var])
  (toString [_ &])
  (toStringInfix [_]))

(declare ZERO)
(deftype JConstant [value]
  IExpression
  (evaluate [_ _] value)
  (diff [_ _] ZERO)
  (toString [_] (format "%.1f" (double value)))
  (toStringInfix [this] (.toString this)))

(defn Constant [value] (JConstant. value))

(defonce ZERO (JConstant. 0))
(defonce ONE (JConstant. 1))
(defonce E (JConstant. Math/E))

(deftype JVariable [name]
  IExpression
  (evaluate [_ vars] (vars name))
  (diff [_ var] (if (= name var) ONE ZERO))
  (toString [_] (str name))
  (toStringInfix [this] (.toString this)))

(defn Variable [name] (JVariable. name))

(deftype JAbstractOperation [calc diff' sym args]
  IExpression
  (evaluate [_ vars] (apply calc (mapv #(evaluate % vars) args)))
  (diff [_ var] (diff' var))
  (toString [_]
    (str "(" (clojure.string/join " " (cons sym (mapv toString args))) ")"))
  (toStringInfix [_]
    (let [infix (mapv toStringInfix args)]
      (if (= (count infix) 1)
        (str sym "(" (first infix) ")")
        (str "(" (first infix) " " sym " " (second infix) ")")))))

(defn Add [& args] (JAbstractOperation. + (fn [var] (apply Add (mapv #(diff % var) args))) '+ args))
(defn Subtract [& args] (JAbstractOperation. - (fn [var] (apply Subtract (mapv #(diff % var) args))) '- args))

(defn Multiply [& args]
  (letfn [(diff' [var]
            (reduce #(Add
                       (Multiply %1 (diff %2 var))
                       (Multiply (diff %1 var) %2)) args))]
    (JAbstractOperation. * diff' '* args)))

(def div #(/ %1 (double %2)))
(defn Divide [& args]
  (letfn [(diff' [var]
            (reduce #(Divide
                       (Subtract
                         (Multiply (diff %1 var) %2)
                         (Multiply %1 (diff %2 var)))
                       (Multiply %2 %2)) args))]
    (JAbstractOperation. div diff' '/ args)))

(def log #(div (Math/log (Math/abs %2)) (Math/log (Math/abs %1))))
(defn Lg [ff ss]
  (letfn [(diff' [var]
            (if (= ff E)
              (Divide (diff ss var) ss)
              (diff (Divide
                      (Lg E ss) (Lg E ff)) var)))]
    (JAbstractOperation. log diff' 'lg [ff ss])))

(defn Pw [ff ss]
  (letfn [(diff' [var]
            (Multiply
              (Pw ff ss)
              (diff (Multiply ss (Lg E ff)) var)))]
    (JAbstractOperation. #(Math/pow %1 %2) diff' 'pw [ff ss])))

(defn b-double [op]
  #(Double/longBitsToDouble
     (op (Double/doubleToLongBits %1) (Double/doubleToLongBits %2))))

(defn Or [& args] (JAbstractOperation. (b-double bit-or) #() '| args))
(defn And [& args] (JAbstractOperation. (b-double bit-and) #() '& args))
(defn Xor [& args] (JAbstractOperation. (b-double bit-xor) #() (symbol "^") args))

(defn Negate [arg]
  (JAbstractOperation. -  #(Negate (diff arg %)) 'negate [arg]))

(defn toString [obj] (toString obj))

(defonce OPERATIONS
         {'+             Add,
          '-             Subtract,
          '*             Multiply,
          '/             Divide,
          'pw            Pw,
          'lg            Lg,
          'negate        Negate,
          '|             Or,
          '&             And,
          (symbol "^")   Xor})

(defn parse [expr]
  (cond
    (number? expr) (Constant expr)
    (symbol? expr) (Variable (str expr))
    :else (apply
            (OPERATIONS (first expr))
            (mapv parse (rest expr)))))

(defn parseObject [expr]
  (parse (read-string expr)))

; ################################################################################### ;
(load-file "essentials.clj")

(def *variable (+map (comp Variable str) (+char "xyz")))
(def *number (+map (comp Constant read-string)
                   (+str (+seqf concat
                                (+seqf cons (+opt (+char "-")) (+plus *digit))
                                (+seqf cons (+opt (+char ".")) (+star *digit))))))

(defn *operator [ops]
  (+map (comp symbol str) (apply +or (map +string ops))))

(declare min-priority)
(def unary
  (delay (+seqn 0
        *ws (+or
              (+seqn 1 (+char "(") min-priority (+char ")"))
              (+map #(Negate (second %)) (+seq (*operator ["negate"]) unary))
              *number
              *variable)
        *ws)))

(def left-assoc
  (fn [a] (reduce #(apply (OPERATIONS (first %2)) [%1 (second %2)])
                  (first a) (partition 2 (rest a)))))

(def PRIORITIES
  [["^"] ["|"] ["&"] ["+" "-"] ["*" "/"]])

(defn abstract [index]
  (let [ops (nth PRIORITIES index)
        index (inc index)
        next (if (= index (count PRIORITIES))
               unary
               (abstract index))]
    (+map left-assoc (+seqf cons next
                            (+map (partial apply concat)
                                  (+star (+seq (*operator ops) next)))))))

(def min-priority (abstract 0))

(defn parseObjectInfix [expr]
  ((+parser min-priority) expr))
