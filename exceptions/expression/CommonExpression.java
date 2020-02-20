package expression;

public interface CommonExpression extends Expression, TripleExpression {
    default int getPriority() {
        return 4;
    }
    default boolean isImportant() {
        return false;
    }
}
