package org.apache.ambari.funtest.server;
public class ClusterConfigParams {
    private java.lang.String clusterName;

    private java.lang.String configTag;

    private java.lang.String configType;

    private java.util.Map<java.lang.String, java.lang.String> properties;

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        this.properties = properties;
    }

    public java.lang.String getConfigType() {
        return configType;
    }

    public void setConfigType(java.lang.String configType) {
        this.configType = configType;
    }

    public java.lang.String getConfigTag() {
        return configTag;
    }

    public void setConfigTag(java.lang.String configTag) {
        this.configTag = configTag;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }
}