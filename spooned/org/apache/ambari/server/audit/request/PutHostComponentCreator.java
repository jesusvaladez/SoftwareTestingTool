package org.apache.ambari.server.audit.request;
public class PutHostComponentCreator extends org.apache.ambari.server.audit.request.AbstractBaseCreator {
    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.Request.Type> getRequestTypes() {
        return java.util.Collections.singleton(org.apache.ambari.server.api.services.Request.Type.PUT);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getResourceTypes() {
        return java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> getResultStatuses() {
        return null;
    }
}