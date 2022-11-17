package lox;

import java.util.Map;

public interface OptionalVariableDeclaration extends Declaration {
    public Object eval(Map<String, Object> env);
}
