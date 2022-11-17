package lox;

import java.util.Map;

public class EmptyVariableDeclaration implements OptionalVariableDeclaration {
    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
