package lox;

import java.util.Optional;

public class EmptyParentDeclaration implements OptionalParentDeclaration {
    @Override
    public Optional<ClassObject> getDeclaredClass() {
        return Optional.empty();
    }
}
