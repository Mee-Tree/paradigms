package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ParsingException;
import expression.exceptions.WrongConstException;

public class ByteOperation implements Operation<Byte> {

    public Byte parse(final String a) throws ParsingException {
        try {
            return (byte) Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Byte max(final Byte a, final Byte b) {
        return a > b ? a : b;
    }

    public Byte min(final Byte a, final Byte b) {
        return a < b ? a : b;
    }

    public Byte count(final Byte a) {
        return (byte) Integer.bitCount(a & 0xff);
    }

    public Byte add(final Byte a, final Byte b) {
        return (byte) (a + b);
    }

    public Byte subtract(final Byte a, final Byte b) {
        return (byte) (a - b);
    }

    public Byte multiply(final Byte a, final Byte b) {
        return (byte) (a * b);
    }

    private void checkDivide(final Byte a, final Byte b) throws DivisionByZeroException {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
    }

    public Byte divide(final Byte a, final Byte b) {
        checkDivide(a, b);
        return (byte) (a / b);
    }

    public Byte negate(final Byte a) {
        return (byte) (-a);
    }
}

