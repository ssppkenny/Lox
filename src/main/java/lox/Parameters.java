package lox;

import java.util.List;

public class Parameters {

    List<Identifier> identifiers;

    public Parameters(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "identifiers=" + identifiers +
                '}';
    }
}
