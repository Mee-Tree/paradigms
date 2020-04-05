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

const pair = (a, b) => {
  return {
      arity: a,
      op:    b
  };
};

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
    "avg5":     pair(5, avg5),

    "iff":      pair(3, iff),
    "med3":     pair(3, med3),

    "+":        pair(2, add),
    "-":        pair(2, subtract),
    "*":        pair(2, multiply),
    "/":        pair(2, divide),

    "sin":      pair(1, sin),
    "cos":      pair(1, cos),
    "negate":   pair(1, negate),
    "abs":      pair(1, abs),
    "cube":     pair(1, cube),
    "cuberoot": pair(1, cuberoot)
};

const parse = str => {
    let tokens = str.split(" ").filter(s => s.length > 0);
    let stack = [];

    for (const token of tokens) {
        if (token in VARIABLES) {
            stack.push(variable(token));
            continue;
        }

        if (token in CONSTANTS) {
            stack.push(CONSTANTS[token]);
            continue;
        }

        if (token in OPERATIONS) {
            let arity = OPERATIONS[token].arity;
            let op = OPERATIONS[token].op;

            stack.push(op(...stack.splice(-arity)));
            continue;
        }

        stack.push(cnst(parseInt(token)));
    }

    return stack.pop();
};
