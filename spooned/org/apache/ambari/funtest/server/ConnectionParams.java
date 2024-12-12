package org.apache.ambari.funtest.server;
public class ConnectionParams {
    private java.lang.String serverName;

    private int serverApiPort;

    private int serverAgentPort;

    private java.lang.String userName;

    private java.lang.String password;

    public java.lang.String getServerName() {
        return serverName;
    }

    public void setServerName(java.lang.String serverName) {
        this.serverName = serverName;
    }

    public int getServerApiPort() {
        return serverApiPort;
    }

    public void setServerApiPort(int serverApiPort) {
        this.serverApiPort = serverApiPort;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public int getServerAgentPort() {
        return serverAgentPort;
    }

    public void setServerAgentPort(int serverAgentPort) {
        this.serverAgentPort = serverAgentPort;
    }
}