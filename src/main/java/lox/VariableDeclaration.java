package lox;

import java.util.Map;

public class VariableDeclaration implements Declaration {

    private OptionalVariableDeclaration optionalVariableDeclaration;

    private String identifier;

    public VariableDeclaration(String identifier, OptionalVariableDeclaration optVarDecl) {
        this.optionalVariableDeclaration = optVarDecl;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "optionalVariableDeclaration=" + optionalVariableDeclaration +
                ", identifier='" + identifier + '\'' +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        Object value = optionalVariableDeclaration.eval(env);
        env.put(identifier, value);
        return value;
    }
}
