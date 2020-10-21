package expression.exceptions;

public class MissingArgumentException extends ParsingException {
    public MissingArgumentException(final String message) {
        super("An argument is missing." + message);
    }
}
