package lox;

public class IfStatement implements Statement {

    private Expression expression;
    private Statement statement;
    private Else elsePart;

    public IfStatement(Expression expression, Statement statement, Else elsePart) {
        this.expression = expression;
        this.statement = statement;
        this.elsePart = elsePart;
    }

}
