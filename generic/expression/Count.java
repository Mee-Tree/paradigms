package generic.expression;

import generic.operations.Operation;

public class Count<T extends Number> extends AbstractUnaryOperator<T> {
    public Count(CommonExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    protected T calculate(T a) {
        return operation.count(a);
    }

    public String toString() {
        return toString("count ");
    }

    public boolean isImportant() {
        return true;
    }
}
