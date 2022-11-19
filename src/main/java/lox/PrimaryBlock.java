package lox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface PrimaryBlock {

    public Object eval(Map<String, Object> env);

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

        @Override
        public Object eval(Map<String, Object> env) {
            List<Object> arguments = expressions.stream().map(e -> e.eval(env)).collect(Collectors.toList());
            return arguments;
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

        @Override
        public Object eval(Map<String, Object> env) {
            return null;
        }
    }

}
