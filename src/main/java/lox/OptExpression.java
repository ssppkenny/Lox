package lox;

import java.util.Map;

public interface OptExpression {

    public Object eval(Map<String, Object> env);

}
