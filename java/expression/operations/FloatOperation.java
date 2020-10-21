package expression.operations;

import expression.exceptions.ParsingException;
import expression.exceptions.WrongConstException;

public class FloatOperation implements Operation<Float> {

    public Float parse(final String a) throws ParsingException {
        try {
            return Float.parseFloat(a);
        } catch (NumberFormatException e) {
            throw new WrongConstException("Const is not suitable for this mode.");
        }
    }

    public Float max(final Float a, final Float b) {
        return Float.max(a, b);
    }

    public Float min(final Float a, final Float b) {
        return Float.min(a, b);
    }

    public Float count(final Float a) {
        return (float) Integer.bitCount(Float.floatToIntBits(a));
    }

    public Float add(final Float a, final Float b) {
        return a + b;
    }

    public Float subtract(final Float a, final Float b) {
        return a - b;
    }

    public Float multiply(final Float a, final Float b) {
        return a * b;
    }

    public Float divide(final Float a, final Float b) {
        return a / b;
    }

    public Float negate(final Float a) {
        return -a;
    }
}
