package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class RootServiceResponse {
    private final java.lang.String serviceName;

    public RootServiceResponse(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.RootServiceResponse other = ((org.apache.ambari.server.controller.RootServiceResponse) (o));
        return java.util.Objects.equals(serviceName, other.serviceName);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(serviceName);
    }
}