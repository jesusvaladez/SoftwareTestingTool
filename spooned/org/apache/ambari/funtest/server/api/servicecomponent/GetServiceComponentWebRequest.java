package org.apache.ambari.funtest.server.api.servicecomponent;
public class GetServiceComponentWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/services/%s/components/%s";

    public GetServiceComponentWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName) {
        super(params);
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getServiceName() {
        return this.serviceName;
    }

    public java.lang.String getComponentName() {
        return this.componentName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "GET";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponent.GetServiceComponentWebRequest.pathFormat, clusterName, serviceName, componentName);
    }
}