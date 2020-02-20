package expression.parser;

import expression.*;
import expression.exceptions.*;

import java.util.Map;

public class ExpressionParser extends BaseParser {
    private final int MIN_PRIORITY = 1;
    private final int MAX_PRIORITY = 4;
    private Token current;

    private static final Map<Token, Integer> PRIORITIES = Map.of(
            Token.ADD,     1,
            Token.MINUS,   1,
            Token.MUL,     2,
            Token.DIV,     2,
            Token.LOG,     3,
            Token.POW,     3
    );

    public CommonExpression parse(final String expression) throws ParsingException {
        super.source = new StringSource(expression);
        nextChar();
        current = Token.BEGIN;
        CommonExpression result = parseExpression(MIN_PRIORITY);
        if (current != Token.END) {
            throw new WrongBracketsException(getMessage(getPos()));
        }
        return result;
    }

    private void checkOperator() throws MissingOperatorException {
        if (current == Token.VARIABLE || current == Token.CONST || current == Token.CLOSE) {
            throw new MissingOperatorException(getMessage(getPos() - 1));
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
            throw new IllegalSymbolException(getMessage(getPos()));
        }
    }

    private Const parseNumber(String sign) throws ConstOverflowException {
        StringBuilder number = new StringBuilder(sign);
        int pos = getPos() - sign.length();
        while (Character.isDigit(ch)) {
            number.append(ch);
            nextChar();
        }
        try {
            return new Const(Integer.parseInt(number.toString()));
        } catch (NumberFormatException e) {
            throw new ConstOverflowException(getMessage(pos, getPos() + 1));
        }
    }

    private Variable parseVariable() {
        String var = String.valueOf(ch);
        nextChar();
        return new Variable(var);
    }

    private boolean checkPriority(int priority) {
        return PRIORITIES.getOrDefault(current, MAX_PRIORITY) == priority;
    }

    private CommonExpression parseExpression(int priority) throws ParsingException {
        if (priority == MAX_PRIORITY) {
            return getMaxPriorityOperation();
        }
        CommonExpression result = parseExpression(priority + 1);
        while (checkPriority(priority)) {
            result = getOperation(current, result, parseExpression(priority + 1));
        }
        return result;
    }

    private CommonExpression getMaxPriorityOperation() throws ParsingException {
        CommonExpression result;
        getNextToken();

        if (current == Token.CONST) {
            result = parseNumber("");
        } else if (current == Token.VARIABLE) {
            result = parseVariable();
        } else if (current == Token.OPEN) {
            int pos = getPos() - 1;
            result = parseExpression(MIN_PRIORITY);
            if (current != Token.CLOSE) {
                throw new WrongBracketsException(getMessage(pos));
            }
        } else if (current == Token.MINUS) {
            if (!Character.isDigit(ch)) {
                return new CheckedNegate(getMaxPriorityOperation());
            }
            result = parseNumber("-");
        } else {
            throw new MissingArgumentException(getMessage(getPos() - 1), current == Token.END);
        }
        getNextToken();
        return result;
    }

    private CommonExpression getOperation(Token token, CommonExpression result, CommonExpression expression) {
        switch (token) {
            case ADD:
                return new CheckedAdd(result, expression);
            case MINUS:
                return new CheckedSubtract(result, expression);
            case MUL:
                return new CheckedMultiply(result, expression);
            case DIV:
                return new CheckedDivide(result, expression);
            case POW:
                return new CheckedPow(result, expression);
            case LOG:
                return new CheckedLog(result, expression);
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
