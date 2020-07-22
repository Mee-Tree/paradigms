"use strict";

const VARS = ['x', 'y', 'z'];

function Variable(name) {
    this.name = name;
}

Variable.prototype = {
    evaluate: function(...args) {
        return args[VARS.indexOf(this.name)];
    },
    diff: function(name) {
        return name === this.name ? ONE : ZERO;
        // return new Const(+(name === this.name));
    },
    simplify: function() {
        return this;
    },
    toString: function() {
        return this.name;
    }
};

function Const(number) {
    this.number = number;
}

Const.prototype = {
    evaluate: function() {
        return this.number;
    },
    diff: function() {
        return ZERO;
    },
    simplify: function() {
        return this;
    },
    toString: function() {
        return this.number.toString();
    }
};

function Operation(symbol, ...operands) {
    this.symbol = symbol;
    this.operands = operands;
}

Operation.prototype = {
    evaluate: function(...args) {
        return this.calculate(
            ...this.operands.map(op => op.evaluate(...args))
        );
    },
    simplify: function () {
        const ops = this.operands.map(operand => operand.simplify());
        if (ops.every(isConst)) {
            return new Const(this.calculate(
                ...ops.map(op => op.number)
            ));
        }
        return this.simplifyImpl(ops);
    },
    toString: function() {
        return this.operands.concat(this.symbol).join(' ');
    }
};

const isConst = arg =>
    arg instanceof Const;

const equals = (first, second) =>
    isConst(first) && isConst(second) &&
        first.number === second.number;

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
        const [first] = this.operands;
        return new Negate(first.diff(name))
    },
    (simples) => new Negate(...simples)
);

const Sinh = createOperation(
    'sinh',
    Math.sinh,
    function(name) {
        const [first] = this.operands;
        return new Multiply(first.diff(name), new Cosh(first))
    },
    (simples) => new Sinh(...simples)
);

const Cosh = createOperation(
    'cosh',
    Math.cosh,
    function(name) {
        const [first] = this.operands;
        return new Multiply(first.diff(name), new Sinh(first))
    },
    (simples) => new Cosh(...simples)
);

const Add = createOperation(
    '+',
    (a, b) => a + b,
    function(name) {
        const [first, second] = this.operands;
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
        const [first, second] = this.operands;
        return new Subtract(first.diff(name), second.diff(name));
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
        const [first, second] = this.operands;
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
        const [first, second] = this.operands;
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
        const [first, second] = this.operands;
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
        const [first, second] = this.operands;
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
        const [a, b, c, x] = this.operands;
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
