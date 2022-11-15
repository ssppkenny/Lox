package lox;

public class ExpressionVariableDeclaration implements OptionalVariableDeclaration {

    private final Expression expression;

    public ExpressionVariableDeclaration(Expression expression) {
        this.expression = expression;
    }

}
