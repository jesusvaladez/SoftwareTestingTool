package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class HostComponentProcessResponse {
    private java.lang.String cluster;

    private java.lang.String host;

    private java.lang.String component;

    private java.util.Map<java.lang.String, java.lang.String> map;

    public HostComponentProcessResponse(java.lang.String clusterName, java.lang.String hostName, java.lang.String componentName, java.util.Map<java.lang.String, java.lang.String> processValueMap) {
        cluster = clusterName;
        host = hostName;
        component = componentName;
        map = processValueMap;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getCluster() {
        return cluster;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME_PROPERTY_ID)
    public java.lang.String getHost() {
        return host;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME_PROPERTY_ID)
    public java.lang.String getComponent() {
        return component;
    }

    public java.util.Map<java.lang.String, java.lang.String> getValueMap() {
        return map;
    }
}