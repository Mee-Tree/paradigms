package expression;

public interface CommonExpression<T> extends Expression<T>, TripleExpression<T> {
    default int getPriority() {
        return 4;
    }
    default boolean isImportant() {
        return false;
    }
}
