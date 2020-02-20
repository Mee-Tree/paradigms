package expression;

import expression.exceptions.OverflowException;

public class CheckedAdd extends AbstractBinaryOperator {
    public CheckedAdd(CommonExpression first, CommonExpression second) {
        super(first, second, 1);
    }

    protected int calculate(int a, int b) throws OverflowException {
        check(a, b);
        return a + b;
    }

    protected static void check(int a, int b) throws OverflowException {
        if (a > 0 && b > Integer.MAX_VALUE - a) {
            throw new OverflowException();
        }
        if (a < 0 && b < Integer.MIN_VALUE - a) {
            throw new OverflowException();
        }
    }

    public String toMiniString() {
        return toMiniString(" + ");
    }

    public String toString() {
        return toString(" + ");
    }
}
