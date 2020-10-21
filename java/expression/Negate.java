package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Negate<T extends Number> extends AbstractUnaryOperator<T> {

    public Negate(CommonExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    protected T calculate(T a) throws OverflowException {
        return operation.negate(a);
    }

    public String toString() {
        return toString("-");
    }

    public boolean isImportant() {
        return true;
    }
}
