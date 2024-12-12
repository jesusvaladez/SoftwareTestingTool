package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class ComponentsResponse {
    @org.codehaus.jackson.annotate.JsonProperty("clusterName")
    private java.lang.String clusterName;

    @org.codehaus.jackson.annotate.JsonProperty("stackName")
    private java.lang.String stackName;

    @org.codehaus.jackson.annotate.JsonProperty("stackVersion")
    private java.lang.String stackVersion;

    @org.codehaus.jackson.annotate.JsonProperty("components")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> components;

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getComponents() {
        return components;
    }

    public void setComponents(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> components) {
        this.components = components;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("ComponentsResponse [clusterName=" + clusterName) + ", stackName=") + stackName) + ", stackVersion=") + stackVersion) + ", components=") + components) + "]";
    }
}