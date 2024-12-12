package org.apache.ambari.funtest.server.api.user;
public class DeleteUserWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private final java.lang.String userName;

    private static java.lang.String pathFormat = "/api/v1/users/%s";

    public DeleteUserWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String userName) {
        super(params);
        this.userName = userName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "DELETE";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.user.DeleteUserWebRequest.pathFormat, userName);
    }
}