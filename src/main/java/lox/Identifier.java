package lox;

import java.util.Map;

public class Identifier {

    private final String identifier;

    public Identifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "identifier='" + identifier + '\'' +
                '}';
    }

    public String getName() {
        return identifier;
    }

    public Object eval(Map<String, Object> env) {
        return env.get(identifier);
    }
}
