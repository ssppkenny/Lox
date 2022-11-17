package lox;

import java.util.Map;

public class EmptyForInit implements FirstForInit {
    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }
}
