package generic.expression;

import exceptions.OverflowException;
import generic.operations.Operation;

public class Max<T extends Number> extends AbstractBinaryOperator<T> {
    public Max(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation, 0);
    }

    protected T calculate(T a, T b) throws OverflowException {
        return operation.max(a, b);
    }

    public String toMiniString() {
        return toMiniString(" max ");
    }

    public String toString() {
        return toString(" max ");
    }
}

