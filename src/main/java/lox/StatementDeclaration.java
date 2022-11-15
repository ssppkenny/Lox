package lox;

public class StatementDeclaration implements Declaration {
    private final Object declaration;

    public StatementDeclaration(Object declaration) {
        this.declaration = declaration;
    }
}
