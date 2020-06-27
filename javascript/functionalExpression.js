"use strict";

const VARS = ['x', 'y', 'z'];

const variable = name => {
    const ind = VARS.indexOf(name);
    return (...args) => args[ind];
};

const cnst = value => () => value;

const operation = action => (...operands) => (...args) =>
    action(...operands.map(x => x(...args)));

const cube = operation(a => a * a * a);
const cuberoot = operation(Math.cbrt);

const sin = operation(Math.sin);
const cos = operation(Math.cos);

const abs = operation(Math.abs);
const negate = operation(a => -a);

const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);

const iff = operation((a, b, c) => a >= 0 ? b : c);

const med3 = operation((a, b, c) =>
    [a, b, c].sort((x, y) => x - y)[1]
);

const avg5 = operation((...arr) =>
    arr.reduce((a, b) => a + b) / arr.length
);

const VARIABLES = {
    "x":    variable("x"),
    "y":    variable("y"),
    "z":    variable("z")
};

const one = cnst(1);
const two = cnst(2);
const pi = cnst(Math.PI);
const e = cnst(Math.E);

const CONSTANTS = {
    "one":  one,
    "two":  two,
    "pi":   pi,
    "e":    e
};

const OPERATIONS = {
    "avg5":     [5, avg5],

    "iff":      [3, iff],
    "med3":     [3, med3],

    "+":        [2, add],
    "-":        [2, subtract],
    "*":        [2, multiply],
    "/":        [2, divide],

    "sin":      [1, sin],
    "cos":      [1, cos],
    "negate":   [1, negate],
    "abs":      [1, abs],
    "cube":     [1, cube],
    "cuberoot": [1, cuberoot]
};

const parse = str => {
    return str.split(' ').filter(s => s.length > 0)
        .reduce((stack, token) => {
            if (token in VARIABLES) {
                stack.push(VARIABLES[token]);
            } else if (token in CONSTANTS) {
                stack.push(CONSTANTS[token]);
            } else if (token in OPERATIONS) {
                const [arity, op] = OPERATIONS[token];
                stack.push(op(...stack.splice(-arity)));
            } else {
                stack.push(cnst(parseInt(token)));
            }
            return stack;
        }, []).pop();
};
