package lox;

import java.util.Map;

public interface Declaration {
    public Object eval(Map<String, Object> env);
}
