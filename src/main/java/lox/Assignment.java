package lox;

import java.util.Map;

public interface Assignment {

    public Object eval(Map<String, Object> env);

}
