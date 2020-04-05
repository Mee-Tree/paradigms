package generic.expression;

import exceptions.OverflowException;
import generic.operations.Operation;

public class Subtract<T extends Number> extends AbstractBinaryOperator<T> {
    public Subtract(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation,1);
    }

    protected T calculate(T a, T b) throws OverflowException {
        return operation.subtract(a, b);
    }

    public String toMiniString() {
        return toMiniString(" - ");
    }

    public String toString() {
        return toString(" - ");
    }

    public boolean isImportant() {
        return true;
    }
}
