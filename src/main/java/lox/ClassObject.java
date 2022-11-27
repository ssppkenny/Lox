package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassObject {

    private String className;

    private List<Function> functions;

    private Map<String, Object> env;

    private ClassObject superclass;

    public ClassObject(String className, List<Function> functions, ClassObject superclass, Map<String, Object> env) {
        this.className = className;
        this.functions = functions;
        this.env = new HashMap<>();
        this.env.putAll(env);
        this.superclass = superclass;
    }

    public Object callFunction(String functionName, Map<String, Object> fArgs) {
        Function function = findFunction(functionName);
        String methodName = className + "_" + functionName;
        Function function1 = new Function(methodName, function.getParameters(), function.getBlock());
        fArgs.put("function:" + methodName, function1);
        fArgs.put("this", this);
        return function1.eval(env, fArgs);
    }

    public Function getFunction(String functionName) {
        Function function = findFunction(functionName);
        return function;
    }

    private Function findFunction(String functionName) {
        Function function = functions.stream().filter(f -> f.getIdentifier().equals(functionName)).findFirst().orElse(null);
        if (function == null) {
            return superclass.getFunction(functionName);
        } else {
            return function;
        }
    }


    public void setProperty(String name, Object value) {
        env.put(name, value);
    }

    public Object getProperty(String name) {
        return env.get(name);
    }

    public String getClassName() {
        return className;
    }
}
