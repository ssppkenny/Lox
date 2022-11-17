package lox;

import java.util.Map;

public class ForStatement implements Statement {

    private FirstForInit firstForInit;

    private OptExpression optExpression1;

    private OptExpression optExpression2;

    private Statement statement;

    public ForStatement(FirstForInit firstForInit, OptExpression optExpression1, OptExpression optExpression2, Statement statement) {
        this.firstForInit = firstForInit;
        this.optExpression1 = optExpression1;
        this.optExpression2 = optExpression2;
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "ForStatement{" +
                "firstForInit=" + firstForInit +
                ", optExpression1=" + optExpression1 +
                ", optExpression2=" + optExpression2 +
                ", statement=" + statement +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        firstForInit.eval(env);
        while (Utils.isTruthy(optExpression1.eval(env))) {
            loop(env);
            optExpression2.eval(env);
        }
        return null;
    }

    private Object loop(Map<String, Object> env) {
        return statement.eval(env);
    }
}
