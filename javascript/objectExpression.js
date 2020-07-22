"use strict";

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
        return name === this.getName() ? ONE : ZERO;
        // return new Const(+(name === this.getName()));
    },
    simplify: function() {
        return this;
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
    simplify: function() {
        return this;
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
    simplify: function () {
        const ops = this.getOperands().map(operand => operand.simplify());
        if (ops.every(isConst)) {
            return new Const(this.calculate(
                ...ops.map(op => op.getNumber())
            ));
        }
        return this.simplifyImpl(ops);
    },
    toString: function() {
        return this.getOperands().concat(this.getSymbol()).join(' ');
    },
    prefix: function() {
        return '(' + this.getSymbol() + ' ' +
            this.getOperands().map(op => op.prefix()).join(' ') + ')'
    },
    postfix: function() {
        return '(' + this.getOperands().map(op => op.postfix()).join(' ') + ' ' +
            this.getSymbol() + ')'
    }
};

const isConst = arg =>
    arg instanceof Const;

const equals = (first, second) =>
    isConst(first) && isConst(second) &&
    first.getNumber() === second.getNumber();

function createOperation(symbol, calculate, diff, simplify) {
    const result = function(...operands) {
        Operation.call(this, symbol, ...operands);
    };
    result.prototype = Object.create(Operation.prototype);
    result.prototype.simplifyImpl = simplify;
    result.prototype.calculate = calculate;
    result.arity = calculate.length;
    result.prototype.diff = diff;
    return result;
}

const Negate = createOperation(
    'negate',
    a => -a,
    function(name) {
        const [first] = this.getOperands();
        return new Negate(first.diff(name))
    },
    (simples) => new Negate(...simples)
);

const Sinh = createOperation(
    'sinh',
    Math.sinh,
    function(name) {
        const [first] = this.getOperands();
        return new Multiply(first.diff(name), new Cosh(first))
    },
    (simples) => new Sinh(...simples)
);

const Cosh = createOperation(
    'cosh',
    Math.cosh,
    function(name) {
        const [first] = this.getOperands();
        return new Multiply(first.diff(name), new Sinh(first))
    },
    (simples) => new Cosh(...simples)
);

const Add = createOperation(
    '+',
    (a, b) => a + b,
    function(name) {
        const [first, second] = this.getOperands();
        return new Add(first.diff(name), second.diff(name))
    },
    (simples) => {
        const [first, second] = simples;
        return equals(first, ZERO) ?
            second : equals(second, ZERO) ?
                first : new Add(...simples);
    }
);

const Subtract = createOperation(
    '-',
    (a, b) => a - b,
    function(name) {
        const [first, second] = this.getOperands();
        return new Subtract(first.diff(name),second.diff(name));
    },
    (simples) => {
        const [first, second] = simples;
        return equals(second, ZERO) ?
            first : equals(first, ZERO) ?
                new Negate(second) : new Subtract(...simples);
    }
);

const Multiply = createOperation(
    '*',
    (a, b) => a * b,
    function(name) {
        const [first, second] = this.getOperands();
        return new Add(
            new Multiply(first.diff(name), second),
            new Multiply(first, second.diff(name))
        );
    },
    (simples) => {
        const [first, second] = simples;
        if (equals(first, ZERO) || equals(second, ZERO)) {
            return ZERO;
        }

        return equals(first, ONE) ?
            second : equals(second, ONE) ?
                first : new Multiply(...simples);
    }
);

const Divide = createOperation(
    '/',
    (a, b) => a / b,
    function(name) {
        const [first, second] = this.getOperands();
        return new Divide(
            new Subtract(
                new Multiply(first.diff(name), second),
                new Multiply(first, second.diff(name))
            ),
            new Multiply(second, second)
        );
    },
    (simples) => {
        const [first, second] = simples;
        return equals(first, ZERO) ?
            ZERO : equals(second, ONE) ?
                first : new Divide(...simples);
    }
);

const Power = createOperation(
    'pow',
    Math.pow,
    function(name) {
        const [first, second] = this.getOperands();
        const exp = new Multiply(
            new Log(E, first),
            second
        );

        return new Multiply(
            exp.diff(name),
            new Power(E, exp)
        );
    },
    (simples) => new Power(...simples) // TODO
);

const Log = createOperation(
    'log',
    (a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)),
    function(name) {
        const [first, second] = this.getOperands();
        if (equals(first, E)) {
            return new Divide(second.diff(name), second);
        }
        return new Divide(new Log(E, second), new Log(E, first)).diff(name);
    },
    (simples) => new Log(...simples) // TODO
);

const Gauss = createOperation(
    'gauss',
    (a, b, c, x) => a * Math.exp((b - x) * (x - b) / (2 * c * c)),
    function(name) {
        const [a, b, c, x] = this.getOperands();
        const exp = new Divide(
            new Multiply(
                new Subtract(b, x),
                new Subtract(x, b)
            ),
            new Multiply(TWO, new Multiply(c, c))
        );

        return new Add(
            new Multiply(this, exp.diff(name)),
            new Multiply(a.diff(name), new Gauss(ONE, b, c, x))
        );
    },
    (simples) => new Gauss(...simples) // TODO
);

const Sum = createOperation(
    'sum',
    (...ops) => ops.reduce((sum, cur) => sum + cur, 0),
    function(name) {
        return new Sum(...this.getOperands().map(op => op.diff(name)));
    },
    (simples) => new Sum(...simples)
);

const Avg = createOperation(
    'avg',
    (...ops) => Sum.prototype.calculate(...ops) / ops.length,
    function(name) {
        return new Avg(...this.getOperands().map(op => op.diff(name)));
    },
    (simples) => new Avg(...simples)
);

const TWO = new Const(2);
const ONE = new Const(1);
const ZERO = new Const(0);
const E = new Const(Math.E);

const VARIABLES = {
    'x':    new Variable('x'),
    'y':    new Variable('y'),
    'z':    new Variable('z')
};

const OPERATIONS = {
    'avg':      Avg,
    'sum':      Sum,

    'gauss':    Gauss,

    '+':        Add,
    '-':        Subtract,
    '*':        Multiply,
    '/':        Divide,
    'pow':      Power,
    'log':      Log,

    'negate':   Negate,
    'sinh':     Sinh,
    'cosh':     Cosh
};

const parse = str => {
    return str.split(' ').filter(s => s.length > 0)
        .reduce((stack, token) => {
            if (token in VARIABLES) {
                stack.push(VARIABLES[token]);
            } else if (token in OPERATIONS) {
                const op = OPERATIONS[token];
                stack.push(new op(...stack.splice(-op.arity)));
            } else {
                stack.push(new Const(parseInt(token)));
            }
            return stack;
        }, []).pop();
};

/* ################################################################# */

include('./exceptions.js');

function BaseParser(expression) {
    this.expression = expression;
    this.index = 0;
    this.ch = '';
}
BaseParser.prototype.nextChar = function() {
    this.ch = this.index < this.expression.length ?
        this.expression[this.index++] : '\0';
};

function ExpressionParser(expression, mode) {
    BaseParser.call(this, expression);
    this.mode = mode;
    this.curToken = '';
    this.nextToken();
}

ExpressionParser.prototype = Object.create(BaseParser.prototype);
ExpressionParser.prototype.nextToken = function() {
    this.skipWhitespace();
    if (this.ch === '(' || this.ch === ')') {
        this.curToken = this.ch;
        this.nextChar();
    } else {
        this.curToken = '';
        while (this.ch !== '(' && this.ch !== ')' && this.ch.trim() !== '' && this.ch !== '\0') {
            this.curToken += this.ch;
            this.nextChar();
        }
    }
    this.skipWhitespace();
};
ExpressionParser.prototype.skipWhitespace = function() {
    while (this.ch.trim() === '') {
        this.nextChar();
    }
};

ExpressionParser.prototype.parseArgs = function() {
    const args = [];
    while (this.curToken !== ')' && this.curToken !== '' && !(this.curToken in OPERATIONS)) {
        args.push(this.parse());
        this.nextToken();
    }
    return args;
};
ExpressionParser.prototype.parse = function() {
    if (this.curToken === '(') {
        this.nextToken();
        let args = [];

        if (this.mode === "postfix") {
            args = this.parseArgs();
        }

        if (!(this.curToken in OPERATIONS)) {
            throw new MissingOperationException(this.index);
        }
        const op = OPERATIONS[this.curToken];
        const symbol = this.curToken;
        this.nextToken();

        if (this.mode === "prefix") {
            args = this.parseArgs();
        }

        if (this.curToken !== ')') {
            throw new MissingParenthesisException(this.index);
        } else if (op.arity > args.length) {
            throw new MissingArgumentException(this.index, symbol);
        } else if (op.arity !== 0 && op.arity < args.length) {
            throw new RedundantArgumentException(this.index, symbol);
        }
        return new op(...args);
    } else if (this.curToken in VARIABLES) {
        return VARIABLES[this.curToken];
    } else if (isNumber(this.curToken)) {
        return new Const(parseInt(this.curToken));
    } else {
        throw new IllegalSequenceException(this.index, this.curToken);
    }
};

function isNumber(arg) {
    let ind = (arg[0] === '-' ? 1 : 0);
    if (arg.length === ind) {
        return false;
    }
    for (; ind < arg.length; ++ind) {
        if (arg[ind] < '0' || arg[ind] > '9') {
            return false;
        }
    }
    return true;
}

const parseMode = (str, mode) => {
    const parser = new ExpressionParser(str, mode);
    const result = parser.parse();
    parser.nextToken();
    if (parser.curToken !== '') {
        throw new IllegalSequenceException(parser.index, parser.curToken);
    }
    return result;
};

const parsePrefix = str => parseMode(str, "prefix");
const parsePostfix = str => parseMode(str, "postfix");
