package expression.exceptions;

public class BaseOneLogarithmException extends EvaluatingException {
    public BaseOneLogarithmException() {
        super("Logarithm to base one cannot be calculated.");
    }
}
