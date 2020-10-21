package expression.exceptions;

public class OverflowException extends EvaluatingException {
    public OverflowException() {
        super("Arithmetic operation resulted in an overflow.");
    }
}
