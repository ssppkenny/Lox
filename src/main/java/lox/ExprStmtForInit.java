package lox;

public class ExprStmtForInit implements FirstForInit {

    private ExpressionStatement expressionStatement;

    public ExprStmtForInit(ExpressionStatement expressionStatement) {
        this.expressionStatement = expressionStatement;
    }

}
