package expression.exceptions;

public class ConstOverflowException extends ParsingException {
    public ConstOverflowException(String message) {
        super("Const is too big for Integer type:\t\t" + message);
    }
}
