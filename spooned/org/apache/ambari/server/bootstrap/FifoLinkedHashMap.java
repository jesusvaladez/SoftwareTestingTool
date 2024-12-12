package org.apache.ambari.server.bootstrap;
@java.lang.SuppressWarnings("serial")
public class FifoLinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> {
    public static final int MAX_ENTRIES = 100;

    @java.lang.Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > org.apache.ambari.server.bootstrap.FifoLinkedHashMap.MAX_ENTRIES;
    }
}