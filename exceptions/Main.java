import expression.*;
import expression.parser.*;
import expression.exceptions.*;

public class Main {
    public static void main(String[] args) throws ParsingException {
        Parser parser = new ExpressionParser();
        TripleExpression expression = parser.parse("1000000*x*x*x*x*x/(x-1)");
        System.out.println("x\tf");
        for (int x = 0; x <= 10; ++x) {
            System.out.print(x + "\t");
            try {
                int res = expression.evaluate(x, 0 , 0);
                System.out.println(res);
            } catch (OverflowException e) {
                System.out.println("overflow");
            } catch (DivisionByZeroException e) {
                System.out.println("division by zero");
            }
        }
    }
}
