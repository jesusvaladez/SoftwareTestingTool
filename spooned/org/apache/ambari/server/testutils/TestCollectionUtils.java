package org.apache.ambari.server.testutils;
public class TestCollectionUtils {
    @java.lang.SuppressWarnings("unchecked")
    public static <K, V> java.util.Map<K, V> map(K firstKey, V firstValue, java.lang.Object... others) {
        java.util.Map<K, V> map = new java.util.HashMap<>();
        map.put(firstKey, firstValue);
        java.util.Iterator iterator = java.util.Arrays.asList(others).iterator();
        while (iterator.hasNext()) {
            map.put(((K) (iterator.next())), ((V) (iterator.next())));
        } 
        return map;
    }
}