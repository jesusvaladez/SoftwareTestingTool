package org.apache.ambari.server.audit.request.eventcreator;
public interface RequestAuditEventCreator {
    java.util.Set<org.apache.ambari.server.api.services.Request.Type> getRequestTypes();

    java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getResourceTypes();

    java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> getResultStatuses();

    org.apache.ambari.server.audit.event.AuditEvent createAuditEvent(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result);
}