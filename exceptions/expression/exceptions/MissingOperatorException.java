package expression.exceptions;

public class MissingOperatorException extends ParsingException {
    public MissingOperatorException(String message) {
        super("An operator is missing between:\t" + message);
    }
}
