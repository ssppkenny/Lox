package lox;

import java.util.Map;

public class FunctionDeclaration implements Declaration {

    private final Function function;

    public FunctionDeclaration(Function function) {
        this.function = function;
    }


    @Override
    public String toString() {
        return "FunctionDeclaration{" +
                "function=" + function +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        env.put("function:" + function.getIdentifier(), function);
        return null;
    }
}
