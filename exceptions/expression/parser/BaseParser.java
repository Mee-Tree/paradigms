package expression.parser;

import expression.exceptions.IllegalSequenceException;

public abstract class BaseParser implements Parser {
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
            throw new IllegalSequenceException("Expected '" + c + "', found '" + ch + "'.\t" + getMessage(getPos()));
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

    protected String getMessage(int pos) {
        pos = Math.max(pos, 0);
        return getMessage(pos, pos + 1);
    }

    protected String getMessage(int begin, int end) {
        return source.getMessage(begin, end);
    }
}
