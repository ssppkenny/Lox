package lox;

import java.util.HashMap;
import java.util.Map;

public class DeclaredClasses {

    private static Map<String,ClassObject> classes = new HashMap<>();

    public static ClassObject findClass(String name) {
        return classes.get(name);
    }

    public static void addClass(String name, ClassObject classObject) {
        classes.put(name, classObject);
    }

}
