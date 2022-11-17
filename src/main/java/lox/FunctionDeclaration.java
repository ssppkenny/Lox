package lox;

import java.util.Map;

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

    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
