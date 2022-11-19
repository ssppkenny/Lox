package lox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parameters {

    List<Identifier> identifiers;

    public Parameters(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "identifiers=" + identifiers +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        List<Object> values = new ArrayList<>();
        for (Identifier identifier : identifiers) {
            values.add(identifier.eval(env));
        }
        return values;
    }

    List<String> getParameterNames() {
        return identifiers.stream().map(Identifier::getName).collect(Collectors.toList());
    }
}
