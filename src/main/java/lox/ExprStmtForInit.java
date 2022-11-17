package lox;

import java.util.Map;

public class ExprStmtForInit implements FirstForInit {

    private ExpressionStatement expressionStatement;

    public ExprStmtForInit(ExpressionStatement expressionStatement) {
        this.expressionStatement = expressionStatement;
    }

    @Override
    public String toString() {
        return "ExprStmtForInit{" +
                "expressionStatement=" + expressionStatement +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return expressionStatement.eval(env);
    }
}
