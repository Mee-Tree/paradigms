package generic.operations;

import exceptions.DivisionByZeroException;
import exceptions.ParsingException;
import exceptions.WrongConstException;

public class LongOperation implements Operation<Long> {

    public Long parse(final String a) throws ParsingException {
        try {
            return Long.parseLong(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Long max(final Long a, final Long b) {
        return Long.max(a, b);
    }

    public Long min(final Long a, final Long b) {
        return Long.min(a, b);
    }

    public Long count(final Long a) {
        return (long) Long.bitCount(a);
    }

    public Long add(final Long a, final Long b) {
        return a + b;
    }

    public Long subtract(final Long a, final Long b) {
        return a - b;
    }

    public Long multiply(final Long a, final Long b) {
        return a * b;
    }

    private void checkDivide(final Long a, final Long b) throws DivisionByZeroException {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
    }

    public Long divide(final Long a, final Long b) {
        checkDivide(a, b);
        return a / b;
    }

    public Long negate(final Long a) {
        return -a;
    }
}

