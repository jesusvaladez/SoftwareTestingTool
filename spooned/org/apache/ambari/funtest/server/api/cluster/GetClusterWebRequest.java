package org.apache.ambari.funtest.server.api.cluster;
public class GetClusterWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s";

    public GetClusterWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName) {
        super(params);
        this.clusterName = clusterName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "GET";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.GetClusterWebRequest.pathFormat, clusterName);
    }
}