package lox;

public class FunctionDeclaration implements Declaration {

    private final Object declaration;

    public FunctionDeclaration(Object declaration) {
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "FunctionDeclaration{" +
                "declaration=" + declaration +
                '}';
    }
}