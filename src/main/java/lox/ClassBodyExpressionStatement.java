package lox;

import java.util.List;
import java.util.Map;

public class ClassBodyExpressionStatement extends ExpressionStatement {

    private List<Function> functions;

    private String className;

    private ClassObject superclass;

    public ClassBodyExpressionStatement(String className, List<Function> functions, ClassObject superclass) {
        super(null);
        this.functions = functions;
        this.className = className;
        this.superclass = superclass;

    }

    private ClassBodyExpressionStatement(Expression expression) {
        super(expression);
    }

    @Override
    public Object eval(Map<String, Object> env) {
        ClassObject classObject =  new ClassObject(className, functions, superclass, env);
        return classObject;

    }
}
