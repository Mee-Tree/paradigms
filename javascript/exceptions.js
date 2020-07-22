"use strict";

function ParsingException(message) {
    this.message = message;
}
ParsingException.prototype = Object.create(Error.prototype);

function createException(name, message) {
    const result = function(ind, ...rest) {
        ParsingException.call(this, `${message(...rest)} (pos. ${ind})`);
    };
    result.prototype = Object.create(ParsingException.prototype);
    result.prototype.name = name;
    result.prototype.constructor = result;
    return result;
}

const MissingOperationException = createException(
    "MissingOperationException",
    () =>
        `The operation is missing`
);

const MissingParenthesisException = createException(
    "MissingParenthesisException",
    () =>
        `The right parenthesis is missing`
);

const MissingArgumentException = createException(
    "MissingArgumentException",
    op =>
        `One or several arguments are missing for operation '${op}'`
);

const RedundantArgumentException = createException(
    "RedundantArgumentException",
    op =>
        `There are too many arguments for operation '${op}'`
);

const IllegalSequenceException = createException(
    "IllegalSequenceException",
    symbol =>
        `Illegal sequence found: '${symbol}'`
);