package lox;

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
}
