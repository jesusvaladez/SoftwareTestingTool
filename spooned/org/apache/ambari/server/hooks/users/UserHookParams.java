package org.apache.ambari.server.hooks.users;
public enum UserHookParams {

    SCRIPT("hook-script"),
    PAYLOAD("cmd-payload"),
    CLUSTER_ID("cluster-id"),
    CLUSTER_NAME("cluster-name"),
    CMD_TIME_FRAME("cmd-timeframe"),
    CMD_INPUT_FILE("cmd-input-file"),
    CLUSTER_SECURITY_TYPE("cluster-security-type"),
    CMD_HDFS_PRINCIPAL("cmd-hdfs-principal"),
    CMD_HDFS_KEYTAB("cmd-hdfs-keytab"),
    CMD_HDFS_USER("cmd-hdfs-user");
    private java.lang.String param;

    UserHookParams(java.lang.String param) {
        this.param = param;
    }

    public java.lang.String param() {
        return param;
    }
}