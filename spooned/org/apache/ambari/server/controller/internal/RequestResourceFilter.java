package org.apache.ambari.server.controller.internal;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class RequestResourceFilter implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String serviceName;

    private java.lang.String componentName;

    private final java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();

    public RequestResourceFilter() {
    }

    public RequestResourceFilter(java.lang.String serviceName, java.lang.String componentName, java.util.List<java.lang.String> hostNames) {
        this.serviceName = serviceName;
        this.componentName = componentName;
        if (hostNames != null) {
            this.hostNames.addAll(hostNames);
        }
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("component_name")
    public java.lang.String getComponentName() {
        return componentName;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("hosts")
    public java.util.List<java.lang.String> getHostNames() {
        return hostNames;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((("RequestResourceFilter{" + "serviceName='") + serviceName) + '\'') + ", componentName='") + componentName) + '\'') + ", hostNames=") + hostNames) + '}';
    }
}