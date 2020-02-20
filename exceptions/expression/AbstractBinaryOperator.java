package expression;

import expression.exceptions.EvaluatingException;
import expression.exceptions.OverflowException;

import java.util.Objects;

public abstract class AbstractBinaryOperator implements CommonExpression {
    protected final CommonExpression first;
    protected final CommonExpression second;
    protected final int priority;

    protected AbstractBinaryOperator(CommonExpression first, CommonExpression second, int priority) {
        this.first = first;
        this.second = second;
        this.priority = priority;
    }

    protected abstract int calculate(int a, int b) throws EvaluatingException;

    public int evaluate(int var) throws EvaluatingException {
        return calculate(first.evaluate(var), second.evaluate(var));
    }

    public int evaluate(int x, int y, int z) throws EvaluatingException {
        return calculate(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperator that = (AbstractBinaryOperator) o;
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

    public String wrap(CommonExpression expression, boolean brackets) {
        if (brackets) {
            return "(" + expression.toMiniString() + ")";
        }
        return expression.toMiniString();
    }

    private boolean checkPriority(CommonExpression expression) {
        return priority > expression.getPriority();
    }

    private boolean checkImportant(CommonExpression expression) {
        return priority == expression.getPriority() && (this.isImportant() || expression.isImportant());
    }

    public String toMiniString(String operation) {
        return wrap(first, checkPriority(first))
                + operation
                + wrap(second, checkPriority(second) || checkImportant(second));
    }
}
