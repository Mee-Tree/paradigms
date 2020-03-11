package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

import java.util.Objects;

public abstract class AbstractUnaryOperator<T extends Number> implements CommonExpression<T> {
    protected final CommonExpression<T> expression;
    protected final Operation<T> operation;

    protected AbstractUnaryOperator(CommonExpression<T> expression, Operation<T> operation) {
        this.expression = expression;
        this.operation = operation;
    }

    protected abstract T calculate(T a) throws EvaluatingException;

    public T evaluate(T var) throws EvaluatingException {
        return calculate(expression.evaluate(var));
    }

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return calculate(expression.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnaryOperator<T> that = (AbstractUnaryOperator<T>) o;
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
