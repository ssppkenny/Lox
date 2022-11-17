package lox;

import java.util.Map;

public class EmptyExpression implements OptExpression {

    public String toString() {
        return "";
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
