package lox;

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
}
