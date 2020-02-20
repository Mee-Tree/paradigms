package expression.exceptions;

import expression.parser.ParserTest;

public class IllegalSymbolException extends ParsingException {
    public IllegalSymbolException(final String message) {
        super("Illegal symbol:\t\t" + message);
    }
}

