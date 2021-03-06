package expression;

import expression.operations.Operation;

public class Min<T extends Number> extends AbstractBinaryOperator<T> {

    public Min(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation, 0);
    }

    protected T calculate(T a, T b) {
        return operation.min(a, b);
    }

    public String toString() {
        return toString(" min ");
    }
}

