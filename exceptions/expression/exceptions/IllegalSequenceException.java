package expression.exceptions;

public class IllegalSequenceException extends ParsingException {
    public IllegalSequenceException(final String message) {
        super("There is an unexpected sequence. " + message);
    }
}
