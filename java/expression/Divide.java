package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.operations.Operation;

public class Divide<T extends Number> extends AbstractBinaryOperator<T> {

    public static final String SYMBOL = " / ";

    public Divide(CommonExpression<T> first, CommonExpression<T> second, Operation<T> operation) {
        super(first, second, operation,2);
    }

    protected T calculate(T a, T b) throws DivisionByZeroException, OverflowException {
        return operation.divide(a, b);
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
