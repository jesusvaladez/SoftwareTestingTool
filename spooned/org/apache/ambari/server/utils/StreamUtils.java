package org.apache.ambari.server.utils;
public class StreamUtils {
    public static <T> java.util.stream.Stream<T> instancesOf(java.util.stream.Stream<?> stream, java.lang.Class<? extends T> clazz) {
        return stream.filter(clazz::isInstance).map(clazz::cast);
    }
}