package lox;

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
}
