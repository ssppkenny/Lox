package lox;

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

}
