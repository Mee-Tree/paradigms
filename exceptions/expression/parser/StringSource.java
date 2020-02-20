package expression.parser;

public class StringSource implements ExpressionSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    public boolean hasNext() {
        return pos < data.length();
    }

    public char next() {
        return data.charAt(pos++);
    }

    public boolean startsWith(String pref) {
        return data.startsWith(pref, getPos());
    }

    public int getPos() {
        return pos - 1;
    }

    public String getMessage(int begin, int end) {
        return data.substring(0, begin) + "|--->" +
                data .substring(begin, end) +
                "<---|" + data.substring(end);
    }


}
