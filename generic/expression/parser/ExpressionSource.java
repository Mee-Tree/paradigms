package expression.parser;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    int getPos();
    boolean startsWith(String value);
    String getExpression();
}
