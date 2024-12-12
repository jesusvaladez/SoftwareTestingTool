package org.apache.ambari.funtest.server.api.cluster;
public class AddDesiredConfigurationWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String configType;

    private java.lang.String configTag;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s";

    public AddDesiredConfigurationWebRequest(org.apache.ambari.funtest.server.ConnectionParams serverParams, org.apache.ambari.funtest.server.ClusterConfigParams configParams) {
        super(serverParams);
        this.clusterName = configParams.getClusterName();
        this.configType = configParams.getConfigType();
        this.configTag = configParams.getConfigTag();
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getConfigType() {
        return this.configType;
    }

    public java.lang.String getConfigTag() {
        return this.configTag;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "PUT";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.AddDesiredConfigurationWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject;
        com.google.gson.JsonObject jsonDesiredConfigs = new com.google.gson.JsonObject();
        jsonDesiredConfigs.addProperty("type", configType);
        jsonDesiredConfigs.addProperty("tag", configTag);
        jsonObject = org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("Clusters", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("desired_configs", jsonDesiredConfigs));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}