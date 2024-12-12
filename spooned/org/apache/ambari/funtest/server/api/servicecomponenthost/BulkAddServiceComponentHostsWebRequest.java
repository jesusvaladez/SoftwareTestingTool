package org.apache.ambari.funtest.server.api.servicecomponenthost;
public class BulkAddServiceComponentHostsWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.util.List<java.lang.String> hostNames;

    private java.util.List<java.lang.String> componentNames;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/hosts";

    public BulkAddServiceComponentHostsWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.util.List<java.lang.String> hostNames, java.util.List<java.lang.String> componentNames) {
        super(params);
        this.clusterName = clusterName;
        this.hostNames = new java.util.ArrayList<>(hostNames);
        this.componentNames = new java.util.ArrayList<>(componentNames);
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.util.List<java.lang.String> getHostNames() {
        return java.util.Collections.unmodifiableList(this.hostNames);
    }

    public java.util.List<java.lang.String> getComponentNames() {
        return java.util.Collections.unmodifiableList(this.componentNames);
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.servicecomponenthost.BulkAddServiceComponentHostsWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        com.google.gson.JsonArray hostRoles = new com.google.gson.JsonArray();
        jsonObject.add("RequestInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("query", java.lang.String.format("Hosts/host_name.in(%s)", org.apache.ambari.funtest.server.api.servicecomponenthost.BulkAddServiceComponentHostsWebRequest.toCsv(hostNames))));
        for (java.lang.String componentName : componentNames) {
            hostRoles.add(org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("HostRoles", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("component_name", componentName)));
        }
        jsonObject.add("Body", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("host_components", hostRoles));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }

    private static java.lang.String toCsv(java.util.List<java.lang.String> list) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String item : list) {
            sb.append(java.lang.String.format("%s,", item));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}