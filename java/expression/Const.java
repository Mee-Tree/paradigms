package expression;

import java.util.Objects;

public class Const<T> implements CommonExpression<T> {

    private final T number;

    public Const(T number) {
        this.number = number;
    }

    public T evaluate(T var) {
        return number;
    }

    public T evaluate(T x, T y, T z) {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<?> aConst = (Const<?>) o;
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
