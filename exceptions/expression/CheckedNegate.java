package expression;

import expression.exceptions.OverflowException;

public class CheckedNegate extends AbstractUnaryOperator {
    public CheckedNegate(CommonExpression expression) {
        super(expression);
    }

    protected int calculate(int a) throws OverflowException {
        check(a);
        return -a;
    }

    protected static void check(int a) throws OverflowException {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    public String toString() {
        return toString("-");
    }

    public boolean isImportant() {
        return true;
    }
}
