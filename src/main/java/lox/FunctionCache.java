package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FunctionCache {

    private static Map<CacheKey, Object> cache = new HashMap<>();

    public static void put(String fName, List<Object> argumentValues, Object value) {
        CacheKey key = new CacheKey(fName, argumentValues);
        cache.put(key, value);
    }

    public static Object get(String fName, List<Object> argumentValues) {
        CacheKey key = new CacheKey(fName, argumentValues);
        return cache.get(key);
    }


    private static class CacheKey {

        CacheKey(String fname, List<Object> argumentValues) {
            this.fname = fname;
            this.argumentValues = argumentValues;
        }

        private String fname;

        private List<Object> argumentValues;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(fname, cacheKey.fname) && Objects.equals(argumentValues, cacheKey.argumentValues);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fname, argumentValues);
        }

        public String getFname() {
            return fname;
        }

        public List<Object> getArgumentValues() {
            return argumentValues;
        }
    }


}
