package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class RootServiceHostComponentResponse {
    private final java.lang.String serviceName;

    private final java.lang.String hostName;

    private final java.lang.String componentName;

    private final java.lang.String componentState;

    private final java.lang.String componentVersion;

    private final java.util.Map<java.lang.String, java.lang.String> properties;

    public RootServiceHostComponentResponse(java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName, java.lang.String componentState, java.lang.String componentVersion, java.util.Map<java.lang.String, java.lang.String> properties) {
        this.serviceName = serviceName;
        this.hostName = hostName;
        this.componentName = componentName;
        this.componentState = componentState;
        this.componentVersion = componentVersion;
        this.properties = properties;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME)
    public java.lang.String getHostName() {
        return hostName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME)
    public java.lang.String getComponentName() {
        return componentName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_STATE)
    public java.lang.String getComponentState() {
        return componentState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_VERSION)
    public java.lang.String getComponentVersion() {
        return componentVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.PROPERTIES)
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.RootServiceHostComponentResponse other = ((org.apache.ambari.server.controller.RootServiceHostComponentResponse) (o));
        return ((((java.util.Objects.equals(serviceName, other.serviceName) && java.util.Objects.equals(hostName, other.hostName)) && java.util.Objects.equals(componentName, other.componentName)) && java.util.Objects.equals(componentState, other.componentState)) && java.util.Objects.equals(componentVersion, other.componentVersion)) && java.util.Objects.equals(properties, other.properties);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(serviceName, hostName, componentName, componentVersion, componentState);
    }
}