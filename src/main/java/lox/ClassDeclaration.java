package lox;

import java.util.List;

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
    public String toString() {
        return "ClassDeclaration{" +
                "identifier='" + identifier + '\'' +
                ", optionalParentDeclaration=" + optionalParentDeclaration +
                ", functions=" + functions +
                '}';
    }
}
