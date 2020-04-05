package generic.expression;

import exceptions.DivisionByZeroException;
import exceptions.OverflowException;
import generic.operations.Operation;

public class Divide<T extends Number> extends AbstractBinaryOperator<T> {
    public Divide(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation,2);
    }

    protected T calculate(T a, T b) throws DivisionByZeroException, OverflowException {
        return operation.divide(a, b);
    }

    public String toMiniString() {
        return toMiniString(" / ");
    }

    public String toString() {
        return toString(" / ");
    }

    public boolean isImportant() {
        return true;
    }
}
