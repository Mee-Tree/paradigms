package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ParsingException;
import expression.exceptions.WrongConstException;

public class ShortOperation implements Operation<Short> {

    public Short parse(final String a) throws ParsingException {
        try {
            return (short) Integer.parseInt(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Short max(final Short a, final Short b) {
        return a > b ? a : b;
    }

    public Short min(final Short a, final Short b) {
        return a < b ? a : b;
    }

    public Short count(final Short a) {
        return (short) Integer.bitCount(a & 0xffff);
    }

    public Short add(final Short a, final Short b) {
        return (short) (a + b);
    }

    public Short subtract(final Short a, final Short b) {
        return (short) (a - b);
    }

    public Short multiply(final Short a, final Short b) {
        return (short) (a * b);
    }

    private void checkDivide(final Short a, final Short b) throws DivisionByZeroException {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
    }

    public Short divide(final Short a, final Short b) {
        checkDivide(a, b);
        return (short) (a / b);
    }

    public Short negate(final Short a) {
        return (short) (-a);
    }
}

