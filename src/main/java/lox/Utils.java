package lox;

public class Utils {
    public static boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    public static Object show(Object o) {
        if (o instanceof Double) {
            Double d = (Double) o;
            if (d.intValue() == d) {
                return d.intValue();
            } else {
                return d;
            }
        } else {
            return o;
        }
    }

    public static Number show(Double d) {
        if (d.intValue() == d) {
            return d.intValue();
        } else {
            return d;
        }
    }

}
