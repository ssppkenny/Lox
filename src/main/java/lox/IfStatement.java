package lox;

import java.util.Map;

public class IfStatement implements Statement {

    private Expression expression;
    private Statement statement;
    private Else elsePart;

    public IfStatement(Expression expression, Statement statement, Else elsePart) {
        this.expression = expression;
        this.statement = statement;
        this.elsePart = elsePart;
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "expression=" + expression +
                ", statement=" + statement +
                ", elsePart=" + elsePart +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        Boolean b = Utils.isTruthy(expression.eval(env));
        if (b) {
            return statement.eval(env);
        } else {
            return elsePart.eval(env);
        }
    }
}
