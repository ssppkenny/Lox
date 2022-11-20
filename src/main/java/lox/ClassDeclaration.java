package lox;

import java.util.ArrayList;
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
        List<Declaration> declarations = new ArrayList<>();
        Function init = functions.stream().filter(function -> function.getIdentifier().equals("init")).findFirst().orElse(null);
        Parameters params = init == null ? new Parameters(new ArrayList<>()) : init.getParameters();
        declarations.add(new ClassBodyExpressionStatement(identifier, functions));
        Function constructor = new Function(identifier, params, new BlockStatement(declarations));
        env.put("function:" + identifier, constructor);
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
