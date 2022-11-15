package lox;

import java.util.List;

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
}
