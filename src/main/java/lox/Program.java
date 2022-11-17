package lox;

import java.util.List;

public class Program {

    List<Declaration> declarations;

    public Program(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    @Override
    public String toString() {
        return "Program{" +
                "declarations=" + declarations +
                '}';
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }
}
