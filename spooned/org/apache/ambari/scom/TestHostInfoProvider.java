package org.apache.ambari.scom;
public class TestHostInfoProvider implements org.apache.ambari.scom.HostInfoProvider {
    private java.lang.String clusterName;

    private java.lang.String componentName;

    private java.lang.String hostId;

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public java.lang.String getHostId() {
        return hostId;
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        this.clusterName = clusterName;
        this.componentName = componentName;
        return "host1";
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException {
        this.hostId = id;
        return "host1";
    }

    @java.lang.Override
    public java.lang.String getHostAddress(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException {
        return "127.0.0.1";
    }
}