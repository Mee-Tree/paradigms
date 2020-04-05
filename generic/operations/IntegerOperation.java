package generic.operations;

import exceptions.*;

public class IntegerOperation implements Operation<Integer> {
	boolean CHECKED;

	public IntegerOperation(boolean check) {
		this.CHECKED = check;
	}

    public Integer parse(final String a) throws ParsingException {
        try {
            return Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Integer max(final Integer a, final Integer b) {
        return Integer.max(a, b);
    }

    public Integer min(final Integer a, final Integer b) {
        return Integer.min(a, b);
    }

    public Integer count(final Integer a) {
        return Integer.bitCount(a);
    }

    private void checkAdd(final Integer a, final Integer b) throws OverflowException {
        if (a > 0 && b > Integer.MAX_VALUE - a) {
            throw new OverflowException();
        }
        if (a < 0 && b < Integer.MIN_VALUE - a) {
            throw new OverflowException();
        }
    }

    public Integer add(final Integer a, final Integer b) throws EvaluatingException {
        if (CHECKED) {
        	checkAdd(a, b);
        }
        return a + b;
    }

    private void checkSubtract(final Integer a, final Integer b) throws OverflowException {
        if (b < 0 && a > b + Integer.MAX_VALUE) {
            throw new OverflowException();
        }
        if (b > 0 && a < b + Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    public Integer subtract(final Integer a, final Integer b) throws EvaluatingException {
        if (CHECKED) {
        	checkSubtract(a, b);
        }
        return a - b;
    }

    private void checkMultiply(final Integer a, final Integer b) throws OverflowException {
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

    public Integer multiply(final Integer a, final Integer b) throws EvaluatingException {
        if (CHECKED) {
        	checkMultiply(a, b);
        }
        return a * b;
    }

    private void checkDivide(final Integer a, final Integer b) throws DivisionByZeroException, OverflowException {
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException();
        }
    }

    public Integer divide(final Integer a, final Integer b) throws EvaluatingException {
    	if (b == 0) {
            throw new DivisionByZeroException();
        }
        if (CHECKED) {
        	checkDivide(a, b);
        }
        return a / b;
    }

    private void checkNegate(final Integer a) throws OverflowException {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    public Integer negate(final Integer a) throws EvaluatingException {
        if (CHECKED) {
        	checkNegate(a);
        }
        return -a;
    }


    private void checkPow(final Integer a, final Integer b) throws NegativePowerException, UndefinedExpressionException {
        if (b < 0) {
            throw new NegativePowerException();
        }
        if (a == 0 && b == 0) {
            throw new UndefinedExpressionException("Zero to the power of zero.");
        }
    }

    public Integer pow(Integer a, Integer b) throws EvaluatingException {
        checkPow(a, b);
        Integer res = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                if (CHECKED) {
                	checkMultiply(res, a);
                }
                res *= a;
            }
            b /= 2;
            if (b > 0) {
            	if (CHECKED) {
                	checkMultiply(a, a);
            	}
                a *= a;
            }
        }
        return res;
    }

    private void checkLog(final Integer a, final Integer b) throws NegativeLogarithmException, BaseOneLogarithmException {
        if (a <= 0 || b <= 0) {
            throw new NegativeLogarithmException();
        }
        if (b == 1) {
            throw new BaseOneLogarithmException();
        }
    }

    public Integer log(Integer a, Integer b) throws EvaluatingException {
        Integer res = 0;
        while (a >= b) {
            a /= b;
            res++;
        }
        return res;
    }
}
