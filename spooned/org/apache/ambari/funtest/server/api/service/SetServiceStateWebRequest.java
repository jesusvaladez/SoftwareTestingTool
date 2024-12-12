package org.apache.ambari.funtest.server.api.service;
public class SetServiceStateWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private org.apache.ambari.server.state.State serviceState;

    private java.lang.String requestContext;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/services/%s";

    public SetServiceStateWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.State serviceState, java.lang.String requestContext) {
        super(params);
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.serviceState = serviceState;
        this.requestContext = requestContext;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getHostName() {
        return this.serviceName;
    }

    public org.apache.ambari.server.state.State getServiceState() {
        return this.serviceState;
    }

    public java.lang.String getRequestContext() {
        return this.requestContext;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "PUT";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.service.SetServiceStateWebRequest.pathFormat, clusterName, serviceName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add("RequestInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("context", requestContext));
        jsonObject.add("Body", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("ServiceInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("state", serviceState.toString())));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}