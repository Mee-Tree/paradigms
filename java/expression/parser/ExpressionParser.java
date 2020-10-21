package expression.parser;

import expression.exceptions.*;
import expression.*;
import expression.operations.Operation;

import java.util.Map;

public class ExpressionParser<T extends Number> extends BaseParser<T> {
    private static final int MIN_PRIORITY = 0;
    private static final int MAX_PRIORITY = 4;
    private final Operation<T> operation;
    private Token current;

    private static final Map<Token, Integer> PRIORITIES = Map.of(
            Token.MIN,     0,
            Token.MAX,     0,
            Token.ADD,     1,
            Token.MINUS,   1,
            Token.MUL,     2,
            Token.DIV,     2,
            Token.LOG,     3,
            Token.POW,     3
    );

    public ExpressionParser(Operation<T> operation) {
        this.operation = operation;
    }

    public CommonExpression<T> parse(final String expression) throws ParsingException {
        super.source = new StringSource(expression);
        nextChar();
        current = Token.BEGIN;
        CommonExpression<T> result = parseExpression(MIN_PRIORITY);
        if (current != Token.END) {
            throw new WrongBracketsException(getMessage(getPos(), 1));
        }
        return result;
    }

    private void checkOperator() throws MissingOperatorException {
        if (current == Token.VARIABLE || current == Token.CONST || current == Token.CLOSE) {
            throw new MissingOperatorException(getMessage(getPos() - 1, 1));
        }
    }

    private void getNextToken() throws ParsingException {
        skipWhitespace();
        if (test('\0')) {
            current = Token.END;
        } else if (test("**")) {
            current = Token.POW;
        } else if (test("//")) {
            current = Token.LOG;
        } else if (test('*')) {
            current = Token.MUL;
        } else if (test('/')) {
            current = Token.DIV;
        } else if (test("max")) {
            current = Token.MAX;
        } else if (test("min")) {
            current = Token.MIN;
        } else if (test("count")) {
            current = Token.COUNT;
        } else if (test('+')) {
            current = Token.ADD;
        } else if (test('-')) {
            current = Token.MINUS;
        } else if (test(')')) {
            current = Token.CLOSE;
        } else if (test('(')) {
            checkOperator();
            current = Token.OPEN;
        } else if (between('x', 'z')) {
            checkOperator();
            current = Token.VARIABLE;
        } else if (Character.isDigit(ch)) {
            checkOperator();
            current = Token.CONST;
        } else {
            throw new IllegalSymbolException(getMessage(getPos(), 1));
        }
    }

    private Const<T> parseNumber(String sign) throws WrongConstException {
        StringBuilder number = new StringBuilder(sign);
        int pos = getPos() - sign.length();
        while (Character.isDigit(ch)) {
            number.append(ch);
            nextChar();
        }
        try {
            return new Const<>(operation.parse(number.toString()));
        } catch (ParsingException e) {
            throw new WrongConstException(getMessage(pos, number.length()));
        }
    }

    private Variable<T> parseVariable() {
        String var = String.valueOf(ch);
        nextChar();
        return new Variable<>(var);
    }

    private boolean checkPriority(int priority) {
        return PRIORITIES.getOrDefault(current, MAX_PRIORITY) == priority;
    }

    private CommonExpression<T> parseExpression(int priority) throws ParsingException {
        if (priority == MAX_PRIORITY) {
            return getMaxPriorityOperation();
        }
        CommonExpression<T> result = parseExpression(priority + 1);
        while (checkPriority(priority)) {
            result = getOperation(current, result, parseExpression(priority + 1));
        }
        return result;
    }

    private CommonExpression<T> getMaxPriorityOperation() throws ParsingException {
        CommonExpression<T> result;
        getNextToken();

        if (current == Token.CONST) {
            result = parseNumber("");
        } else if (current == Token.VARIABLE) {
            result = parseVariable();
        } else if (current == Token.COUNT) {
            return new Count<>(getMaxPriorityOperation(), operation);
        } else if (current == Token.OPEN) {
            int pos = getPos() - 1;
            result = parseExpression(MIN_PRIORITY);
            if (current != Token.CLOSE) {
                throw new WrongBracketsException(getMessage(pos, 1));
            }
        } else if (current == Token.MINUS) {
            if (!Character.isDigit(ch)) {
                return new Negate<>(getMaxPriorityOperation(), operation);
            }
            result = parseNumber("-");
        } else {
            throw new MissingArgumentException(getMessage(getPos() - 1, 1));
        }
        getNextToken();
        return result;
    }

    private CommonExpression<T> getOperation(Token token, CommonExpression<T> result, CommonExpression<T> expression) {
        switch (token) {
            case ADD:
                return new Add<>(result, expression, operation);
            case MINUS:
                return new Subtract<>(result, expression, operation);
            case MUL:
                return new Multiply<>(result, expression, operation);
            case DIV:
                return new Divide<>(result, expression, operation);
            case MAX:
                return new Max<>(result, expression, operation);
            case MIN:
                return new Min<>(result, expression, operation);
            default:
                return result;
        }
    }

    private void skipWhitespace() {
        //noinspection StatementWithEmptyBody
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
            // skip
        }
    }
}
