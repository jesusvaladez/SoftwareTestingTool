package org.apache.ambari.funtest.server.api.cluster;
public class DeleteClusterWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private final java.lang.String clusterName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s";

    public DeleteClusterWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName) {
        super(params);
        this.clusterName = clusterName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "DELETE";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.DeleteClusterWebRequest.pathFormat, clusterName);
    }
}