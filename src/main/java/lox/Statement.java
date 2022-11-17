package lox;

import java.util.Map;

public interface Statement extends Declaration {
    public Object eval(Map<String, Object> env);
}
