package lox;

import java.util.List;
import java.util.Map;

public class Call {

    private Primary primary;
    private List<PrimaryBlock> primaryBlock;

    public Call(Primary primary, List<PrimaryBlock> primaryBlock) {
        this.primary = primary;
        this.primaryBlock = primaryBlock;
    }

    @Override
    public String toString() {
        return "Call{" +
                "primary=" + primary +
                ", primaryBlock=" + primaryBlock +
                '}';
    }

    public Object eval(Map<String, Object> env) {
        return primary.eval(env);
    }
}
