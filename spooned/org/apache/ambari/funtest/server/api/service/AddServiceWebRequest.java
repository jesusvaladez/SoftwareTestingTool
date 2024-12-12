package org.apache.ambari.funtest.server.api.service;
public class AddServiceWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/services";

    public AddServiceWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName) {
        super(params);
        this.clusterName = clusterName;
        this.serviceName = serviceName;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public java.lang.String getServiceName() {
        return this.serviceName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.service.AddServiceWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonServiceInfoObj;
        jsonServiceInfoObj = org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("ServiceInfo", org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("service_name", serviceName));
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonServiceInfoObj);
    }
}