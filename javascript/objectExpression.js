"use strict";

function ParsingException(name, Message) {
    const result = function(arg, extra = "") {
        this.name = name;
        this.message = Message(arg) + extra;
    };
    result.prototype = Object.create(Error.prototype);
    return result;
}

const MissingOperationException = ParsingException(
    "MissingOperationException",
    () =>
        ``
);

const MissingParenthesisException = ParsingException(
    "MissingParenthesisException",
    () =>
        ``
);

const MissingArgumentException = ParsingException(
    "MissingArgumentException",
    op =>
        `One or several arguments are missing for operation '${op}'.\n`
);

const RedundantArgumentException = ParsingException(
    "RedundantArgumentException",
    op =>
        `There are too many arguments for operation '${op}'.\n`
);

const IllegalSymbolException = ParsingException(
    "IllegalSymbolException",
    symbol =>
        `Illegal symbol found: '${symbol}'.\n`
);

const VARS = ['x', 'y', 'z'];

function Variable(name) {
    this.getName = function() {
        return name;
    }
}

Variable.prototype = {
    evaluate: function(...args) {
        return args[VARS.indexOf(this.getName())];
    },
    diff: function(name) {
        return name === this.name ? ONE : ZERO;
    },
    toString: function() {
        return this.getName();
    }
};
Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;

function Const(number) {
    this.getNumber = function() {
        return number;
    }
}

Const.prototype = {
    evaluate: function() {
        return this.getNumber();
    },
    diff: function() {
        return ZERO;
    },
    toString: function() {
        return this.getNumber().toString();
    }
};
Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;

function Operation(symbol, ...operands) {
    this.getSymbol = function() {
        return symbol;
    };
    this.getOperands = function() {
        return operands;
    }
}

Operation.prototype = {
    evaluate: function(...args) {
        return this.calculate(
            ...this.getOperands().map(op => op.evaluate(...args))
        );
    },
    toString: function() {
        return this.getOperands().concat(this.getSymbol()).join(' ');
    },
    prefix: function() {
        return '(' +
            this.getSymbol() + ' ' + this.getOperands().map(op => op.prefix()).join(' ') +
            ')'
    },
    postfix: function() {
        return '(' +
            this.getOperands().map(op => op.postfix()).join(' ')  + ' '  + this.getSymbol() +
            ')'
    }
};

function createOperation(symbol, calculate, diff) {
    const result = function (...operands) {
        Operation.call(this, symbol, ...operands);
    };
    result.prototype = Object.create(Operation.prototype);
    result.prototype.calculate = calculate;
    result.prototype.diff = diff;
    return result;
}

const Negate = createOperation(
    'negate',
    a => -a,
    function(name) {
        let [first] = this.getOperands();
        return new Negate(first.diff(name))
    }
);

const Add = createOperation(
    '+',
    (a, b) => a + b,
    function(name) {
        let [first, second] = this.getOperands();
        return new Add(first.diff(name), second.diff(name))
    }
);

const Subtract = createOperation(
    '-',
    (a, b) => a - b,
    function(name) {
        let [first, second] = this.getOperands();
        return new Subtract(first.diff(name),second.diff(name));
    }
);

const Multiply = createOperation(
    '*',
    (a, b) => a * b,
    function(name) {
        let [first, second] = this.getOperands();
        return new Add(
            new Multiply(first.diff(name), second),
            new Multiply(first, second.diff(name))
        );
    }
);

const Divide = createOperation(
    '/',
    (a, b) => a / b,
    function(name) {
        let [first, second] = this.getOperands();
        return new Divide(
            new Subtract(
                new Multiply(first.diff(name), second),
                new Multiply(first, second.diff(name))
            ),
            new Multiply(second, second)
        );
    }
);

const Sum = createOperation(
    'sum',
    (...ops) => ops.reduce((sum, cur) => sum + cur, 0),
    function(name) {
        return new Sum(...this.getOperands().map(op => op.diff(name)));
    }
);

const Avg = createOperation(
    'sum',
    (...ops) => Sum.prototype.calculate(...ops) / ops.length,
    function(name) {
        return new Avg(...this.getOperands().map(op => op.diff(name)));
    }
);

const Sumsq = createOperation(
    'sumsq',
    (...ops) => Sum.prototype.calculate(...ops.map(op => op * op)),
    function(name) {
        return new Multiply(
            TWO,
            new Sum(...this.getOperands().map(
                op => new Multiply(op, op.diff(name))
            ))
        );
    }
);

const Length = createOperation(
    'length',
    (...ops) => Math.sqrt(Sumsq.prototype.calculate(...ops)),
    function(name) {
        if (this.getOperands().length === 0) {
            return ZERO;
        }
        return new Divide(
            new Sumsq(...this.getOperands()).diff(name),
            new Multiply(TWO, this)
        );
    }
);

const Sumexp = createOperation(
    'sumexp',
    (...ops) => Sum.prototype.calculate(...ops.map(Math.exp)),
    function(name) {
        return new Sum(...this.getOperands().map(
            op => new Multiply(op.diff(name), new Sumexp(op))
        ));
    }
);

const Softmax = createOperation(
    'softmax',
    (...ops) => Math.exp(ops[0]) / Sumexp.prototype.calculate(...ops),
    function(name) {
        let ops = this.getOperands();
        return new Divide(
            new Sumexp(ops[0]),
            new Sumexp(...ops)
        ).diff(name);
    }
);

const VARIABLES = {
    'x':    new Variable('x'),
    'y':    new Variable('y'),
    'z':    new Variable('z')
};

const OPERATIONS = {
    'avg':      [Avg, Infinity],
    'sum':      [Sum, Infinity],
    'sumsq':    [Sumsq, Infinity],
    'length':   [Length, Infinity],
    'sumexp':   [Sumexp, Infinity],
    'softmax':  [Softmax, Infinity],

    '+':        [Add, 2],
    '-':        [Subtract, 2],
    '*':        [Multiply, 2],
    '/':        [Divide, 2],

    'negate':   [Negate, 1],
};

const TWO = new Const(2);
const ONE = new Const(1);
const ZERO = new Const(0);

function BaseParser(expression) {
    this.expression = expression;
    this.index = 0;
    this.ch = '';
    this.nextChar = function() {
        this.ch = this.index < this.expression.length ?
            this.expression[this.index++] : '\0';
    }
}

function ExpressionParser(expression, parse) {
    BaseParser.call(this, expression);
    this.parse = parse;
    this.curToken = '';
}
ExpressionParser.prototype = {
    extraInfo: function() {
        return `${this.expression}
        ${' '.repeat(Math.max(this.index - 1, 0))}^`;
    },
    nextToken: function() {
        this.skipWhitespace();
        if (this.ch === '(' || this.ch === ')') {
            this.curToken = this.ch;
            this.nextChar();
        } else {
            this.curToken = '';
            while (this.ch !== '(' && this.ch !== ')' && this.ch.trim() !== '' && this.ch !== '\0') {
                // println(this.ch);
                this.curToken += this.ch;
                this.nextChar();
            }
        }
        // println(this.curToken);
        this.skipWhitespace();
    },
    skipWhitespace: function() {
        while (this.ch.trim() === '') {
            this.nextChar();
        }
    }
};


function isNumber(arg) {
    let ind = 0;
    if (arg[0] === '-') {
        ind++;
    }
    if (arg.length === ind) {
        return false;
    }
    while (ind < arg.length && '0' <= arg[ind] && arg[ind] <= '9') {
        ind++;
    }
    return ind === arg.length;
}

const parsePrefix = str => {
    let parser = new ExpressionParser(str, function() {
        if (this.curToken === '(') {
            this.nextToken();
            if (this.curToken in OPERATIONS) {
                let args = [];
                let op = this.curToken;
                this.nextToken();
                while (this.curToken !== ')' && this.index < this.expression.length) {
                    args.push(this.parse());
                    this.nextToken();
                }
                if (this.curToken !== ')') {
                    throw new MissingParenthesisException();
                }
                if (isFinite(OPERATIONS[op][1]) && OPERATIONS[op][1] > args.length) {
                    throw new MissingArgumentException(op);
                } else if (OPERATIONS[op][1] < args.length) {
                    throw new RedundantArgumentException(op);
                }
                return new OPERATIONS[op][0](...args);
            } else {
                throw new MissingOperationException();
            }
        } else if (this.curToken in VARIABLES) {
            return VARIABLES[this.curToken];
        } else if (isNumber(this.curToken)) {
            return new Const(parseInt(this.curToken));
        } else {
            throw new IllegalSymbolException(this.curToken, this.extraInfo());
        }
    });
    parser.nextToken();
    let result = parser.parse();
    // parser.nextToken();
    // println('~~~' + parser.ch + '~~~');
    if (parser.index < parser.expression.length) {
        throw new IllegalSymbolException(parser.ch);
    }
    return result;
};

const parse = str => {
    let tokens = str.split(' ').filter(s => s.length > 0);
    let stack = [];

    for (const token of tokens) {
        if (token in VARIABLES) {
            stack.push(VARIABLES[token]);
        } else if (token in OPERATIONS) {
            let op = OPERATIONS[token];
            stack.push(new op[0](...stack.splice(-op[1])));
        } else {
            stack.push(new Const(parseInt(token)));
        }
    }

    return stack.pop();
};