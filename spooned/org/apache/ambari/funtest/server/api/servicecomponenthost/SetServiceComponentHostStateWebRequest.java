package org.apache.ambari.funtest.server.api.servicecomponenthost;
public class SetServiceComponentHostStateWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String hostName;

    private java.lang.String componentName;

    private org.apache.ambari.server.state.State componentState;

    private java.lang.String requestContext;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/hosts/%s/host_components/%s";

    public SetServiceComponentHostStateWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String hostName, java.lang.String componentName, org.apache.ambari.server.state.State componentState, java.lang.String requestContext) {
        super(params);
        this.clusterName = clusterName;
        this.hostName = hostName;
        this.componentName = componentName;
        this.componentState = componentState;
        this.requestContext = requestContext;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getHostName() {
        return this.hostName;
    }

    public org.apache.ambari.server.state.State getComponentState() {
        return this.componentState;
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
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponenthost.SetServiceComponentHostStateWebRequest.pathFormat, clusterName, hostName, componentName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        java.lang.String content;
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add("RequestInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("context", requestContext));
        jsonObject.add("Body", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("HostRoles", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("state", componentState.toString())));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        content = gson.toJson(jsonObject);
        return content;
    }
}