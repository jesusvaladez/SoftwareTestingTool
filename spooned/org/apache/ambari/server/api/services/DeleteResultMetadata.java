package org.apache.ambari.server.api.services;
import org.apache.commons.lang.Validate;
public class DeleteResultMetadata implements org.apache.ambari.server.api.services.ResultMetadata {
    private final java.util.Set<java.lang.String> deletedKeys;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.api.services.ResultStatus> excptions;

    public DeleteResultMetadata() {
        this.deletedKeys = new java.util.HashSet<>();
        this.excptions = new java.util.HashMap<>();
    }

    public void addDeletedKey(java.lang.String key) {
        org.apache.commons.lang.Validate.notNull(key);
        deletedKeys.add(key);
    }

    public void addDeletedKeys(java.util.Collection<java.lang.String> keys) {
        org.apache.commons.lang.Validate.notNull(keys);
        deletedKeys.addAll(keys);
    }

    public void addException(java.lang.String key, java.lang.Exception ex) {
        org.apache.commons.lang.Validate.notNull(key);
        org.apache.commons.lang.Validate.notNull(ex);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = getResultStatusForException(ex);
        excptions.put(key, resultStatus);
    }

    public void addExceptions(java.util.Map<java.lang.String, java.lang.Exception> exceptionKeyMap) {
        if (exceptionKeyMap == null) {
            return;
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.Exception> exceptionEntry : exceptionKeyMap.entrySet()) {
            org.apache.ambari.server.api.services.ResultStatus resultStatus = getResultStatusForException(exceptionEntry.getValue());
            excptions.put(exceptionEntry.getKey(), resultStatus);
        }
    }

    public java.util.Set<java.lang.String> getDeletedKeys() {
        return java.util.Collections.unmodifiableSet(deletedKeys);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.api.services.ResultStatus> getExcptions() {
        return java.util.Collections.unmodifiableMap(excptions);
    }

    private org.apache.ambari.server.api.services.ResultStatus getResultStatusForException(java.lang.Exception ex) {
        org.apache.commons.lang.Validate.notNull(ex);
        if (ex.getClass() == org.apache.ambari.server.security.authorization.AuthorizationException.class) {
            return new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN, ex);
        } else if (ex.getClass() == org.apache.ambari.server.controller.spi.SystemException.class) {
            return new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, ex);
        } else if (ex instanceof org.apache.ambari.server.ObjectNotFoundException) {
            return new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, ex);
        } else if (ex.getClass() == org.apache.ambari.server.controller.spi.UnsupportedPropertyException.class) {
            return new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, ex);
        } else {
            return new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, ex);
        }
    }
}