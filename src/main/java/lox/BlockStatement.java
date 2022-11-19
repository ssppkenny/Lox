package lox;

import java.util.List;
import java.util.Map;

public class BlockStatement implements Statement {

    private final List<Declaration> declarations;

    public BlockStatement(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    @Override
    public String toString() {
        return "BlockStatement{" +
                "declarations=" + declarations +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {
        Object retVal = null;
        for (Declaration declaration : declarations) {
            retVal = declaration.eval(env);
        }
        return retVal;
    }


}
