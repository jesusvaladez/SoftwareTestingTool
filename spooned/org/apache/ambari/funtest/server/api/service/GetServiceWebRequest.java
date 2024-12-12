package org.apache.ambari.funtest.server.api.service;
public class GetServiceWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/services/%s";

    public GetServiceWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName) {
        super(params);
        this.clusterName = clusterName;
        this.serviceName = serviceName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getHostName() {
        return this.serviceName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "GET";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.service.GetServiceWebRequest.pathFormat, clusterName, serviceName);
    }
}