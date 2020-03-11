package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Multiply<T extends Number> extends AbstractBinaryOperator<T> {
    public Multiply(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation, 2);
    }

    protected T calculate(T a, T b) throws OverflowException {
        return operation.multiply(a, b);
    }

    public String toMiniString() {
        return toMiniString(" * ");
    }

    public String toString() {
        return toString(" * ");
    }
}

