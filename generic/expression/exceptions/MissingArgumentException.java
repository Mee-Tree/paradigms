package expression.exceptions;
import expression.parser.Token;

public class MissingArgumentException extends ParsingException {
    public MissingArgumentException(final String message) {
        super("An argument is missing." + message);
    }
}
