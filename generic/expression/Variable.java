package expression;

import expression.exceptions.IllegalSymbolException;

import java.util.Objects;

public class Variable<T> implements CommonExpression<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public T evaluate(T var) {
        return var;
    }

    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        throw new IllegalArgumentException("Illegal variable name.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable<T> variable = (Variable<T>) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return name;
    }
}
