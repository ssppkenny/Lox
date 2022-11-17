package lox;

import java.util.Map;

public class WhileStatement implements Statement {

    private Expression expression;
    private Statement statement;

    public WhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "WhileStatement{" +
                "expression=" + expression +
                ", statement=" + statement +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        while (Utils.isTruthy(expression.eval(env))) {
            statement.eval(env);
        }
        return null;
    }
}
