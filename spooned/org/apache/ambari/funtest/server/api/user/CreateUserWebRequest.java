package org.apache.ambari.funtest.server.api.user;
public class CreateUserWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private final java.lang.String userName;

    private final java.lang.String password;

    private final boolean isActive;

    private final boolean isAdmin;

    private static java.lang.String pathFormat = "/api/v1/users/%s";

    public enum ActiveUser {

        FALSE,
        TRUE;}

    public enum AdminUser {

        FALSE,
        TRUE;}

    public CreateUserWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String userName, java.lang.String password, org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.ActiveUser activeUser, org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.AdminUser adminUser) {
        super(params);
        this.userName = userName;
        this.password = password;
        this.isActive = activeUser == org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.ActiveUser.TRUE;
        this.isAdmin = adminUser == org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.AdminUser.TRUE;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.pathFormat, userName);
    }

    @java.lang.Override
    protected java.lang.String getRequestData() {
        com.google.gson.JsonObject jsonObject;
        com.google.gson.JsonObject jsonUserInfo = new com.google.gson.JsonObject();
        jsonUserInfo.addProperty("active", isActive);
        jsonUserInfo.addProperty("admin", isAdmin);
        jsonUserInfo.addProperty("password", password);
        jsonObject = org.apache.ambari.funtest.server.AmbariHttpWebRequest.createJsonObject("Users", jsonUserInfo);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(jsonObject);
    }
}