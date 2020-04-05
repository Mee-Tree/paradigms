package exceptions;

public class NegativePowerException extends EvaluatingException {
    public NegativePowerException() {
        super("The power is less than zero.");
    }
}
