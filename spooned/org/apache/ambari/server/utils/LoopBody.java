package org.apache.ambari.server.utils;
public interface LoopBody<T, R> {
    R run(T t);
}