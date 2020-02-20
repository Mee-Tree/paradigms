package expression;

import expression.exceptions.IllegalSymbolException;

import java.util.Objects;

public class Variable implements CommonExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public int evaluate(int var) {
        return var;
    }

    public int evaluate(int x, int y, int z) {
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
        Variable variable = (Variable) o;
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
