package lox;

import java.util.List;

public class BlockStatement implements Statement {

    private final List<Declaration> declarations;

    public BlockStatement(List<Declaration> declarations) {
        this.declarations = declarations;
    }
}
