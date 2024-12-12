package org.apache.ambari.funtest.server.api.cluster;
public class CreateConfigurationWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String configType;

    private java.lang.String configTag;

    private java.util.Map<java.lang.String, java.lang.String> properties;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/configurations";

    public CreateConfigurationWebRequest(org.apache.ambari.funtest.server.ConnectionParams serverParams, org.apache.ambari.funtest.server.ClusterConfigParams configParams) {
        super(serverParams);
        this.clusterName = configParams.getClusterName();
        this.configType = configParams.getConfigType();
        this.configTag = configParams.getConfigTag();
        this.properties = new java.util.HashMap<>(configParams.getProperties());
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
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.CreateConfigurationWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonPropertiesObj = new com.google.gson.JsonObject();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
            jsonPropertiesObj.addProperty(property.getKey(), property.getValue());
        }
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty("type", configType);
        jsonObject.addProperty("tag", configTag);
        jsonObject.add("properties", jsonPropertiesObj);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}