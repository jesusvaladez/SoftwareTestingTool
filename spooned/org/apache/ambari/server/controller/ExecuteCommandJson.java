package org.apache.ambari.server.controller;
public class ExecuteCommandJson {
    private java.lang.String clusterHostJson;

    private java.lang.String commandParamsJson;

    private java.lang.String hostParamsJson;

    ExecuteCommandJson(java.lang.String clusterHosts, java.lang.String commandParams, java.lang.String hostParams) {
        clusterHostJson = clusterHosts;
        commandParamsJson = commandParams;
        hostParamsJson = hostParams;
    }

    public java.lang.String getHostParamsForStage() {
        return hostParamsJson;
    }

    public java.lang.String getCommandParamsForStage() {
        return commandParamsJson;
    }

    public java.lang.String getClusterHostInfo() {
        return clusterHostJson;
    }

    public void setClusterHostInfo(java.lang.String clusterHostJson) {
        this.clusterHostJson = clusterHostJson;
    }
}