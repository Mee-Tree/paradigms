package expression;

import expression.exceptions.BaseOneLogarithmException;
import expression.exceptions.NegativeLogarithmException;
import expression.exceptions.OverflowException;

public class CheckedLog extends AbstractBinaryOperator {
    public CheckedLog(CommonExpression first, CommonExpression second) {
        super(first, second, 3);
    }

    protected int calculate(int a, int b) throws OverflowException {
        check(a, b);
        int res = 0;
        while (a >= b) {
            a /= b;
            res++;
        }
        return res;
    }

    protected static void check(int a, int b) throws NegativeLogarithmException, BaseOneLogarithmException {
        if (a <= 0 || b <= 0) {
            throw new NegativeLogarithmException();
        }
        if (b == 1) {
            throw new BaseOneLogarithmException();
        }
    }

    public String toMiniString() {
        return toMiniString(" // ");
    }

    public String toString() {
        return toString(" // ");
    }
}
