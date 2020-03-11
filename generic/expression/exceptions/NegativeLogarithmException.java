package expression.exceptions;

public class NegativeLogarithmException extends EvaluatingException {
    public NegativeLogarithmException() {
        super("Logarithm of a negative value.");
    }
}
