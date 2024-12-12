package org.apache.ambari.funtest.server.api.cluster;
public class SetUserPrivilegeWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private final java.lang.String clusterName;

    private final java.lang.String userName;

    private final java.lang.String principalType;

    private final org.apache.ambari.funtest.server.AmbariUserRole userRole;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/privileges";

    public SetUserPrivilegeWebRequest(org.apache.ambari.funtest.server.ConnectionParams serverParams, java.lang.String clusterName, java.lang.String userName, org.apache.ambari.funtest.server.AmbariUserRole userRole, java.lang.String principalType) {
        super(serverParams);
        this.clusterName = clusterName;
        this.userName = userName;
        this.principalType = principalType;
        this.userRole = userRole;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.SetUserPrivilegeWebRequest.pathFormat, clusterName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject;
        com.google.gson.JsonObject jsonPrivilegeInfo = new com.google.gson.JsonObject();
        jsonPrivilegeInfo.addProperty("permission_name", userRole.toString());
        jsonPrivilegeInfo.addProperty("principal_name", userName);
        jsonPrivilegeInfo.addProperty("principal_type", principalType);
        jsonObject = org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("PrivilegeInfo", jsonPrivilegeInfo);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}