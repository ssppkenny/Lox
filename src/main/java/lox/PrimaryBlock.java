package lox;

import java.util.List;

public interface PrimaryBlock {

    class ArgumentsGroup implements PrimaryBlock {
        private final List<Expression> expressions;

        public ArgumentsGroup(List<Expression> expressions) {
            this.expressions = expressions;
        }

        @Override
        public String toString() {
            return "ArgumentsGroup{" +
                    "expressions=" + expressions +
                    '}';
        }
    }

    class DotIdentifier implements PrimaryBlock {
        private final String identifier;

        public DotIdentifier(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return "DotIdentifier{" +
                    "identifier='" + identifier + '\'' +
                    '}';
        }
    }

}
