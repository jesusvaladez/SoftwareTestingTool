package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class RootServiceComponentResponse {
    private final java.lang.String serviceName;

    private final java.lang.String componentName;

    private final java.util.Map<java.lang.String, java.lang.String> properties;

    private final java.lang.String componentVersion;

    private final long serverClock = java.lang.System.currentTimeMillis() / 1000L;

    public RootServiceComponentResponse(java.lang.String serviceName, java.lang.String componentName, java.lang.String componentVersion, java.util.Map<java.lang.String, java.lang.String> properties) {
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.componentVersion = componentVersion;
        this.properties = properties;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME)
    public java.lang.String getComponentName() {
        return componentName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.PROPERTIES)
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION)
    public java.lang.String getComponentVersion() {
        return componentVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK)
    public long getServerClock() {
        return serverClock;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.RootServiceComponentResponse other = ((org.apache.ambari.server.controller.RootServiceComponentResponse) (o));
        return ((java.util.Objects.equals(serviceName, other.serviceName) && java.util.Objects.equals(componentName, other.componentName)) && java.util.Objects.equals(componentVersion, other.componentVersion)) && java.util.Objects.equals(properties, other.properties);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(serviceName, componentName, componentVersion);
    }
}