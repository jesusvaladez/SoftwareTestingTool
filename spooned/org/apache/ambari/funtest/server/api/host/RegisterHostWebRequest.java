package org.apache.ambari.funtest.server.api.host;
public class RegisterHostWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String hostName = null;

    private static java.lang.String pathFormat = "/agent/v1/register/%s";

    public RegisterHostWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String hostName) {
        super(params);
        this.hostName = hostName;
    }

    public java.lang.String getHostName() {
        return this.hostName;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "POST";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return java.lang.String.format(org.apache.ambari.funtest.server.api.host.RegisterHostWebRequest.pathFormat, hostName);
    }
}