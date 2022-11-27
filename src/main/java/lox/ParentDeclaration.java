package lox;

import java.util.Optional;

public class ParentDeclaration implements OptionalParentDeclaration {
    private String identifier;

    public ParentDeclaration(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ParentDeclaration{" +
                "identifier='" + identifier + '\'' +
                '}';
    }

    @Override
    public Optional<ClassObject> getDeclaredClass() {
        return Optional.ofNullable(DeclaredClasses.findClass(identifier));
    }
}
