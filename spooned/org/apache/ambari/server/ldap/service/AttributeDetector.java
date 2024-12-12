package org.apache.ambari.server.ldap.service;
public interface AttributeDetector<T> {
    void collect(T entry);

    java.util.Map<java.lang.String, java.lang.String> detect();
}