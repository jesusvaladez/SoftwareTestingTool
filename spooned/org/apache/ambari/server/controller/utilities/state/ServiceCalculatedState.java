package org.apache.ambari.server.controller.utilities.state;
public interface ServiceCalculatedState {
    org.apache.ambari.server.state.State getState(java.lang.String clusterName, java.lang.String serviceName);
}