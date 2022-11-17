package lox;

import java.util.Map;

public class PrintStatement implements Statement {

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "PrintStatement{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        System.out.println(expression.eval(env));
        return null;
    }
}
