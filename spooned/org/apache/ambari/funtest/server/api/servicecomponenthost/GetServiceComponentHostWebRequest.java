package org.apache.ambari.funtest.server.api.servicecomponenthost;
public class GetServiceComponentHostWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String hostName;

    private java.lang.String componentName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/hosts/%s/host_components/%s";

    public GetServiceComponentHostWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String hostName, java.lang.String componentName) {
        super(params);
        this.clusterName = clusterName;
        this.hostName = hostName;
        this.componentName = componentName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getHostName() {
        return this.hostName;
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
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponenthost.GetServiceComponentHostWebRequest.pathFormat, clusterName, hostName, componentName);
    }
}