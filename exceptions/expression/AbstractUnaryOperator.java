package expression;

import expression.exceptions.EvaluatingException;

import java.util.Objects;

public abstract class AbstractUnaryOperator implements CommonExpression {
    protected final CommonExpression expression;

    protected AbstractUnaryOperator(CommonExpression expression) {
        this.expression = expression;
    }

    protected abstract int calculate(int a) throws EvaluatingException;

    public int evaluate(int var) throws EvaluatingException {
        return calculate(expression.evaluate(var));
    }

    public int evaluate(int x, int y, int z) throws EvaluatingException {
        return calculate(expression.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnaryOperator that = (AbstractUnaryOperator) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return 31 * expression.hashCode() + getClass().hashCode();
    }

    public String toString(String operation) {
        return "(" + operation + expression.toString() + ")";
    }
}
