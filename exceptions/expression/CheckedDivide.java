package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends AbstractBinaryOperator {
    public CheckedDivide(CommonExpression first, CommonExpression second) {
        super(first, second, 2);
    }

    protected int calculate(int a, int b) throws DivisionByZeroException, OverflowException {
        check(a, b);
        return a / b;
    }

    protected static void check(int a, int b) throws DivisionByZeroException, OverflowException {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException();
        }
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
