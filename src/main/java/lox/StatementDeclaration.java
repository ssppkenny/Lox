package lox;

public class StatementDeclaration implements Declaration {
    private final Object declaration;

    public StatementDeclaration(Object declaration) {
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "StatementDeclaration{" +
                "declaration=" + declaration +
                '}';
    }
}
