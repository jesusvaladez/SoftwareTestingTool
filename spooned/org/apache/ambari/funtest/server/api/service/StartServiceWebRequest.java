package org.apache.ambari.funtest.server.api.service;
public class StartServiceWebRequest extends org.apache.ambari.funtest.server.api.service.SetServiceStateWebRequest {
    public StartServiceWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, java.lang.String serviceName) {
        super(params, clusterName, serviceName, org.apache.ambari.server.state.State.STARTED, "Start service");
    }
}