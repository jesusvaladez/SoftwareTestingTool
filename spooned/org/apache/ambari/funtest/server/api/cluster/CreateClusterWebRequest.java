package org.apache.ambari.funtest.server.api.cluster;
public class CreateClusterWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String clusterVersion;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s";

    public CreateClusterWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String clusterVersion) {
        super(params);
        this.clusterName = clusterName;
        this.clusterVersion = clusterVersion;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getClusterVersion() {
        return this.clusterVersion;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.CreateClusterWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonClustersObj = new com.google.gson.JsonObject();
        jsonClustersObj.add("Clusters", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("version", getClusterVersion()));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonClustersObj);
    }
}