package org.apache.ambari.funtest.server.api.host;
public class AddHostWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName = null;

    private java.lang.String hostName = null;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/hosts/%s";

    public AddHostWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String hostName) {
        super(params);
        this.clusterName = clusterName;
        this.hostName = hostName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getHostName() {
        return this.hostName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.host.AddHostWebRequest.pathFormat, clusterName, hostName);
    }
}