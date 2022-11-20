package lox;

import java.util.List;
import java.util.Map;

public class ClassBodyExpressionStatement extends ExpressionStatement {

    private List<Function> functions;

    private String className;

    public ClassBodyExpressionStatement(String className, List<Function> functions) {
        super(null);
        this.functions = functions;
        this.className = className;

    }

    private ClassBodyExpressionStatement(Expression expression) {
        super(expression);
    }

    @Override
    public Object eval(Map<String, Object> env) {
        return new ClassObject(className, functions, env);
    }
}
