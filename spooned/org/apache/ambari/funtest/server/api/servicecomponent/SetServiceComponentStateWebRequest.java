package org.apache.ambari.funtest.server.api.servicecomponent;
public class SetServiceComponentStateWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private org.apache.ambari.server.state.State componentState;

    private java.lang.String requestContext;

    private boolean recoveryEnabled;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/services/%s/components/%s";

    public SetServiceComponentStateWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State componentState, boolean recoveryEnabled, java.lang.String requestContext) {
        super(params);
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.componentState = componentState;
        this.recoveryEnabled = recoveryEnabled;
        this.requestContext = requestContext;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getServiceName() {
        return this.serviceName;
    }

    public org.apache.ambari.server.state.State getComponentState() {
        return this.componentState;
    }

    public boolean isRecoveryEnabled() {
        return this.recoveryEnabled;
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
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponent.SetServiceComponentStateWebRequest.pathFormat, clusterName, serviceName, componentName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add("RequestInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("context", requestContext));
        com.google.gson.JsonObject jsonScInfoObj = new com.google.gson.JsonObject();
        jsonScInfoObj.addProperty("state", java.lang.String.valueOf(componentState));
        jsonScInfoObj.addProperty("recovery_enabled", java.lang.String.valueOf(recoveryEnabled));
        jsonObject.add("Body", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("ServiceComponentInfo", jsonScInfoObj));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}