package expression;

import expression.exceptions.OverflowException;

public class CheckedMultiply extends AbstractBinaryOperator {
    public CheckedMultiply(CommonExpression first, CommonExpression second) {
        super(first, second, 2);
    }

    protected int calculate(int a, int b) throws OverflowException {
        check(a, b);
        return a * b;
    }

    protected static void check(int a, int b) throws OverflowException {
        if (b < 0 && a < Integer.MAX_VALUE / b) {
            throw new OverflowException();
        }
        if (b < 0 && a > 0 && b < Integer.MIN_VALUE / a) {
            throw new OverflowException();
        }
        if (b > 0 && (a < Integer.MIN_VALUE / b || a > Integer.MAX_VALUE / b)) {
            throw new OverflowException();
        }
    }

    public String toMiniString() {
        return toMiniString(" * ");
    }

    public String toString() {
        return toString(" * ");
    }
}

