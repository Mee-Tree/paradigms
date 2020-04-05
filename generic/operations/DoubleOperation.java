package generic.operations;

import exceptions.ParsingException;
import exceptions.WrongConstException;

public class DoubleOperation implements Operation<Double> {

    public Double parse(final String a) throws ParsingException {
        try {
            return Double.parseDouble(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Double max(final Double a, final Double b) {
        return Double.max(a, b);
    }

    public Double min(final Double a, final Double b) {
        return Double.min(a, b);
    }

    public Double count(final Double a) {
        return (double) Long.bitCount(Double.doubleToLongBits(a));
    }

    public Double add(final Double a, final Double b) {
        return a + b;
    }

    public Double subtract(final Double a, final Double b) {
        return a - b;
    }

    public Double multiply(final Double a, final Double b) {
        return a * b;
    }

    public Double divide(final Double a, final Double b) {
        return a / b;
    }

    public Double negate(final Double a) {
        return -a;
    }
}
