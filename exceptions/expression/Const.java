package expression;

import java.util.Objects;

public class Const implements CommonExpression {
    private final Number number;

    public Const(Number number) {
        this.number = number;
    }

    public int evaluate(int var) {
        return number.intValue();
    }

    public int evaluate(int x, int y, int z) {
        return number.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const aConst = (Const) o;
        return Objects.equals(number, aConst.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    public String toString() {
        return number.toString();
    }
}
