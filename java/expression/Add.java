package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Add<T extends Number> extends AbstractBinaryOperator<T> {

    public Add(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation, 1);
    }

    protected T calculate(T a, T b) throws OverflowException {
        return operation.add(a, b);
    }

    public String toString() {
        return toString(" + ");
    }
}
