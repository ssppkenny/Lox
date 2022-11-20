package lox;

import java.util.List;
import java.util.Map;

public class CallEqualsExpression implements Assignment {

    private Expression expression;

    private Call call;

    public CallEqualsExpression(Call call, Expression expression) {
        this.expression = expression;
        this.call = call;
    }

    @Override
    public String toString() {
        return "CallEqualsExpression{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Object eval(Map<String, Object> env) {

        Object o = env.get(call.getPrimary().toString());
        if (o instanceof ClassObject) {
            ClassObject co = (ClassObject) o;
            List<PrimaryBlock> primaryBlock = call.getPrimaryBlock();
            PrimaryBlock primaryBlock1 = primaryBlock.get(0);
            String propName = primaryBlock1.toString();
            co.setProperty(propName, expression.eval(env));
        }

        return null;
    }
}
