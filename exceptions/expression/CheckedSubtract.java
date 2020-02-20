package expression;

import expression.exceptions.OverflowException;

public class CheckedSubtract extends AbstractBinaryOperator {
    public CheckedSubtract(CommonExpression first, CommonExpression second) {
        super(first, second, 1);
    }

    protected int calculate(int a, int b) throws OverflowException {
        check(a, b);
        return a - b;
    }

    protected static void check(int a, int b) throws OverflowException {
        if (b < 0 && a > b + Integer.MAX_VALUE) {
            throw new OverflowException();
        }
        if (b > 0 && a < b + Integer.MIN_VALUE) {
            throw new OverflowException();
        }
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
