package lox;

public interface Primary {

    class True implements Primary {
        public java.lang.String toString() {
            return "true";
        }
    }

    class False implements Primary {
        public java.lang.String toString() {
            return "false";
        }

    }

    class Nil implements Primary {
        public java.lang.String toString() {
            return "null";
        }

    }

    class This implements Primary {
        public java.lang.String toString() {
            return "this";
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
    }

    class Identifier implements Primary {
        private final java.lang.String identifier;

        public Identifier(java.lang.String identifier) {
            this.identifier = identifier;
        }

        @Override
        public java.lang.String toString() {
            return "Identifier{" +
                    "identifier='" + identifier + '\'' +
                    '}';
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
    }

}
