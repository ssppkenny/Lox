package lox;

import java.util.List;
import java.util.Map;

public class ClassDeclaration implements Declaration {

    private String identifier;

    private OptionalParentDeclaration optionalParentDeclaration;

    private List<Function> functions;

    public ClassDeclaration(String identifier, OptionalParentDeclaration optionalParentDeclaration, List<Function> functions) {
        this.identifier = identifier;
        this.optionalParentDeclaration = optionalParentDeclaration;
        this.functions = functions;

    }

    @Override
    public Object eval(Map<String, Object> env) {
        return null;
    }

    @Override
    public String toString() {
        return "ClassDeclaration{" +
                "identifier='" + identifier + '\'' +
                ", optionalParentDeclaration=" + optionalParentDeclaration +
                ", functions=" + functions +
                '}';
    }
}
