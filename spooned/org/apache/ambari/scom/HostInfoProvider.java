package org.apache.ambari.scom;
public interface HostInfoProvider {
    public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException;

    public java.lang.String getHostName(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException;

    public java.lang.String getHostAddress(java.lang.String id) throws org.apache.ambari.server.controller.spi.SystemException;
}