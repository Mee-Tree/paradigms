package expression.exceptions;

public class UndefinedExpressionException extends EvaluatingException {
    public UndefinedExpressionException(String message) {
        super(message);
    }
}
