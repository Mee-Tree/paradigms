package expression;

import expression.exceptions.NegativePowerException;
import expression.exceptions.OverflowException;
import expression.exceptions.UndefinedExpressionException;

public class CheckedPow extends AbstractBinaryOperator {
    public CheckedPow(CommonExpression first, CommonExpression second) {
        super(first, second, 3);
    }

    protected int calculate(int a, int b) throws OverflowException, NegativePowerException, UndefinedExpressionException {
        check(a, b);
        int res = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                CheckedMultiply.check(res, a);
                res *= a;
            }
            b /= 2;
            if (b > 0) {
                CheckedMultiply.check(a, a);
                a *= a;
            }
        }
        return res;
    }

    protected static void check(int a, int b) throws NegativePowerException, UndefinedExpressionException {
        if (b < 0) {
            throw new NegativePowerException();
        }
        if (a == 0 && b == 0) {
            throw new UndefinedExpressionException("Zero to the power of zero.");
        }
    }

    public String toMiniString() {
        return toMiniString(" ** ");
    }

    public String toString() {
        return toString(" ** ");
    }
}
