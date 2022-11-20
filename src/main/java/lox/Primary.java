package lox;

import java.util.Map;

public interface Primary {

    Object eval(Map<java.lang.String, Object> env);

    class True implements Primary {
        public java.lang.String toString() {
            return "true";
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return Boolean.TRUE;
        }
    }

    class False implements Primary {
        public java.lang.String toString() {
            return "false";
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return Boolean.FALSE;
        }
    }

    class Nil implements Primary {
        public java.lang.String toString() {
            return "null";
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return null;
        }
    }

    class This implements Primary {
        public java.lang.String toString() {
            return "this";
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return null;
        }
    }

    class Number implements Primary {
        private Double d;

        public Number(Double d) {
            this.d = d;
        }

        @Override
        public java.lang.String toString() {
            return "Number{" +
                    "d=" + d +
                    '}';
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return d;
        }
    }


    class String implements Primary {
        private java.lang.String s;

        public String(java.lang.String s) {
            this.s = s;
        }

        @Override
        public java.lang.String toString() {
            return "String{" +
                    "s='" + s + '\'' +
                    '}';
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return s;
        }
    }

    class Identifier implements Primary {
        private final java.lang.String identifier;

        public Identifier(java.lang.String identifier) {
            this.identifier = identifier;
        }

        @Override
        public java.lang.String toString() {
            return identifier;
        }

        public java.lang.String getIdentifier() {
            return identifier;
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return env.get(identifier);
        }
    }

    class ExpressionPrimary implements Primary {
        private final Expression expression;

        public ExpressionPrimary(Expression expression) {
            this.expression = expression;
        }

        @Override
        public java.lang.String toString() {
            return "ExpressionPrimary{" +
                    "expression=" + expression +
                    '}';
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return expression.eval(env);
        }
    }

    class SuperIdentifier implements Primary {
        private java.lang.String identifier;

        public SuperIdentifier(java.lang.String identifier) {
            this.identifier = identifier;
        }

        @Override
        public java.lang.String toString() {
            return "SuperIdentifier{" +
                    "identifier='" + identifier + '\'' +
                    '}';
        }

        @Override
        public Object eval(Map<java.lang.String, Object> env) {
            return identifier;
        }
    }

}
