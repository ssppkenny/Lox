package lox;

import java.util.Map;

public interface Unary {

    public Object eval(Map<String, Object> env);

}
