package expression;

import expression.exceptions.EvaluatingException;
import expression.operations.Operation;

import java.util.Objects;

public abstract class AbstractBinaryOperator<T extends Number> implements CommonExpression<T> {

    protected final CommonExpression<T> first;
    protected final CommonExpression<T> second;
    protected final Operation<T> operation;
    protected final int priority;

    protected AbstractBinaryOperator(CommonExpression<T> first, CommonExpression<T> second,
                                     Operation<T> operation, int priority) {
        this.first = first;
        this.second = second;
        this.operation = operation;
        this.priority = priority;
    }

    protected abstract T calculate(T a, T b) throws EvaluatingException;

    public T evaluate(T var) throws EvaluatingException {
        return calculate(first.evaluate(var), second.evaluate(var));
    }

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return calculate(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperator<?> that = (AbstractBinaryOperator<?>) o;
        return priority == that.priority &&
                Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return 701 * first.hashCode() + 31 * second.hashCode() + getClass().hashCode();
    }

    public String toString(String operation) {
        return "(" + first.toString() + operation + second.toString() + ")";
    }

    public String toMiniString(String operation) {
        return wrap(first, checkPriority(first))
                + operation
                + wrap(second, checkPriority(second) || checkImportant(second));
    }

    private String wrap(CommonExpression<T> expression, boolean brackets) {
        if (brackets) {
            return "(" + expression.toMiniString() + ")";
        }
        return expression.toMiniString();
    }

    private boolean checkPriority(CommonExpression<T> expression) {
        return priority > expression.getPriority();
    }

    private boolean checkImportant(CommonExpression<T> expression) {
        return priority == expression.getPriority() && (this.isImportant() || expression.isImportant());
    }
}
