(defn lengths [f xs]
  (apply f (mapv count xs)))

(defn vectorOfNums? [v]
  (and (vector? v) (every? number? v)))

(defn matrixOfNums? [m]
  (and (vector? m) (every? vectorOfNums? m)))

(defn createVectorOperation [f]
  (fn [& xs]
    {:pre ((every? vectorOfNums? xs)
           (lengths == xs))
     :post ((vectorOfNums? %))}
    (apply mapv f xs)))

(defn createMatrixOperation [f]
  (fn [& xs]
    {:pre  ((every? matrixOfNums? xs)
            (lengths == xs))
     :post ((matrixOfNums? %))}
    (apply mapv f xs)))

(defn createShapelessOperation [f]
  (fn [& xs]
    {:pre  ((or
              (every? number? xs)
              (and (every? vector? xs) (lengths == xs))))
     :post ((some-fn vector? number? %))}
    (if (number? (first xs))
      (apply f xs)
      (apply mapv (createShapelessOperation f) xs))))

(def v+ (createVectorOperation +))
(def v- (createVectorOperation -))
(def v* (createVectorOperation *))

(defn v*s [v & xs]
  {:pre ((vectorOfNums? v)
         (every? number? xs))
   :post ((vectorOfNums? %))}
  (mapv (apply partial * xs) v))

(defn scalar [& xs]
  {:pre ((every? vectorOfNums? xs))
   :post (number? %)}
  (reduce + (apply v* xs)))

(def m+ (createMatrixOperation v+))
(def m- (createMatrixOperation v-))
(def m* (createMatrixOperation v*))

(defn m*s [m & xs]
  {:pre ((matrixOfNums? m)
         (every? number? xs))
   :post ((matrixOfNums? %))}
  (mapv #(apply v*s % xs) m))

(defn transpose [m]
  {:pre ((matrixOfNums? m))
   :post ((matrixOfNums? %))}
  (apply mapv vector m))

(defn m*v [m & xs]
  {:pre ((matrixOfNums? m))
   :post ((vectorOfNums? %))}
  (mapv (apply partial scalar xs) m))

(defn vect [& xs]
  {:pre ((every? vectorOfNums? xs)
         (lengths (partial == 3) xs))
   :post ((vectorOfNums? %))}
  (reduce (fn [v u]
    (let [[x, y, z] u
         m [[0 z (- y)]
            [(- z) 0 x]
            [y (- x) 0]]]
     (m*v m v))) xs))

(defn m*m [& xs]
  (reduce (fn [m1 m2]
    (let [m2t (transpose m2)]
      (transpose (mapv (partial m*v m1) m2t)))) xs))

(def s+ (createShapelessOperation +))
(def s- (createShapelessOperation -))
(def s* (createShapelessOperation *))
