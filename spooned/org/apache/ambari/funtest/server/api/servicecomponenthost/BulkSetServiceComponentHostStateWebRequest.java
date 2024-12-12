package org.apache.ambari.funtest.server.api.servicecomponenthost;
public class BulkSetServiceComponentHostStateWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private org.apache.ambari.server.state.State currentState;

    private org.apache.ambari.server.state.State desiredState;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/host_components?HostRoles/state=%s";

    public BulkSetServiceComponentHostStateWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, org.apache.ambari.server.state.State currentState, org.apache.ambari.server.state.State desiredState) {
        super(params);
        this.clusterName = clusterName;
        this.currentState = currentState;
        this.desiredState = desiredState;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "PUT";
    }

    public org.apache.ambari.server.state.State getCurrentState() {
        return this.currentState;
    }

    public org.apache.ambari.server.state.State getDesiredState() {
        return this.desiredState;
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponenthost.BulkSetServiceComponentHostStateWebRequest.pathFormat, clusterName, this.currentState);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add("HostRoles", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("state", desiredState.toString()));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}