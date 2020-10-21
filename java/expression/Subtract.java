package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Subtract<T extends Number> extends AbstractBinaryOperator<T> {

    public static final String SYMBOL = " - ";

    public Subtract(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation,1);
    }

    protected T calculate(T a, T b) throws OverflowException {
        return operation.subtract(a, b);
    }

    public String toMiniString() {
        return toMiniString(SYMBOL);
    }

    public String toString() {
        return toString(SYMBOL);
    }

    public boolean isImportant() {
        return true;
    }
}
