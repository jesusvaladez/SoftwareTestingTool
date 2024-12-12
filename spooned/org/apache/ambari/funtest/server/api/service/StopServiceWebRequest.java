package org.apache.ambari.funtest.server.api.service;
public class StopServiceWebRequest extends org.apache.ambari.funtest.server.api.service.SetServiceStateWebRequest {
    public StopServiceWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName) {
        super(params, clusterName, serviceName, org.apache.ambari.server.state.State.INSTALLED, "Stop service");
    }
}