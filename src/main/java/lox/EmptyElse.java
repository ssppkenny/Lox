package lox;

import java.util.Map;

public class EmptyElse implements Else {
    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
