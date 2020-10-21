package expression.parser;

import expression.exceptions.IllegalSequenceException;

public abstract class BaseParser<T> implements Parser<T> {
    protected ExpressionSource source;
    protected char ch;

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String value) {
        if (!source.startsWith(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); ++i) {
            test(value.charAt(i));
        }
        return true;
    }

    protected void expect(final char c) throws IllegalSequenceException {
        if (ch != c) {
            throw new IllegalSequenceException("Expected '" + c + "', found '" + ch + "'.\t" + getMessage(getPos(), 1));
        }
        nextChar();
    }

    protected void expect(final String value) throws IllegalSequenceException {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected int getPos() {
        return source.getPos();
    }

    protected String getMessage(int pos, int len) {
    	return "\n" + source.getExpression()
    			+ "\n" + " ".repeat(Math.max(0, pos))
    			+ "^".repeat(len);  
    }
}
