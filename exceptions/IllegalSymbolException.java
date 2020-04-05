package exceptions;

public class IllegalSymbolException extends ParsingException {
    public IllegalSymbolException(final String message) {
        super("Illegal symbol." + message);
    }
}

