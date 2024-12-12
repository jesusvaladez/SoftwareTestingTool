package org.apache.ambari.server.audit.request;
public interface RequestAuditLogger {
    void log(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result);
}