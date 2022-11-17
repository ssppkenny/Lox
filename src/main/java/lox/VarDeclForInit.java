package lox;

import java.util.Map;

public class VarDeclForInit implements FirstForInit {

    private VariableDeclaration variableDeclaration;

    public VarDeclForInit(VariableDeclaration variableDeclaration) {
        this.variableDeclaration = variableDeclaration;
    }

    @Override
    public String toString() {
        return "VarDeclForInit{" +
                "variableDeclaration=" + variableDeclaration +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return variableDeclaration.eval(env);
    }
}
