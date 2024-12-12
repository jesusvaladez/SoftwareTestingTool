package org.apache.ambari.server.controller.internal;
import javax.annotation.concurrent.NotThreadSafe;
import org.apache.commons.lang.Validate;
@javax.annotation.concurrent.NotThreadSafe
public class DeleteStatusMetaData implements org.apache.ambari.server.controller.spi.RequestStatusMetaData {
    private java.util.Set<java.lang.String> deletedKeys;

    private java.util.Map<java.lang.String, java.lang.Exception> exceptionMap;

    public DeleteStatusMetaData() {
        this.deletedKeys = new java.util.HashSet<>();
        this.exceptionMap = new java.util.HashMap<>();
    }

    public void addDeletedKey(java.lang.String key) {
        org.apache.commons.lang.Validate.notEmpty(key, "Key should not be empty");
        deletedKeys.add(key);
    }

    public java.util.Set<java.lang.String> getDeletedKeys() {
        return java.util.Collections.unmodifiableSet(deletedKeys);
    }

    public void addException(java.lang.String key, java.lang.Exception exception) {
        org.apache.commons.lang.Validate.notEmpty(key, "Key should not be empty");
        org.apache.commons.lang.Validate.notNull(exception, "Exception cannot be null");
        exceptionMap.put(key, exception);
    }

    public java.util.Map<java.lang.String, java.lang.Exception> getExceptionForKeys() {
        return java.util.Collections.unmodifiableMap(exceptionMap);
    }
}