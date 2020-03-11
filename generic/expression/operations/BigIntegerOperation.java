package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ParsingException;
import expression.exceptions.WrongConstException;

import java.math.BigInteger;

public class BigIntegerOperation implements Operation<BigInteger> {

    public BigInteger parse(final String a) throws ParsingException {
        try {
            return new BigInteger(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public BigInteger max(final BigInteger a, final BigInteger b) {
        return a.max(b);
    }

    public BigInteger min(final BigInteger a, final BigInteger b) {
        return a.min(b);
    }

    public BigInteger count(final BigInteger a) {
        return BigInteger.valueOf(a.bitCount());
    }

    public BigInteger add(final BigInteger a, final BigInteger b) {
        return a.add(b);
    }

    public BigInteger subtract(final BigInteger a, final BigInteger b) {
        return a.subtract(b);
    }

    public BigInteger multiply(final BigInteger a, final BigInteger b) {
        return a.multiply(b);
    }

    private void checkDivide(final BigInteger a, final BigInteger b) throws DivisionByZeroException  {
        if (b.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException();
        }
    }

    public BigInteger divide(final BigInteger a, final BigInteger b) {
        checkDivide(a, b);
        return a.divide(b);
    }

    public BigInteger negate(final BigInteger a) {
        return a.negate();
    }
}
