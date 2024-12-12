package org.apache.ambari.server.controller.utilities.state;
public class HBaseServiceCalculatedStateTest extends org.apache.ambari.server.controller.utilities.state.GeneralServiceCalculatedStateTest {
    @java.lang.Override
    protected java.lang.String getServiceName() {
        return "HBASE";
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState getServiceCalculatedStateObject() {
        return new org.apache.ambari.server.controller.utilities.state.HBaseServiceCalculatedState();
    }

    @java.lang.Override
    protected void createComponentsAndHosts() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponent masterComponent = service.addServiceComponent("HBASE_MASTER");
        org.apache.ambari.server.state.ServiceComponent secondMasterComponent = service.addServiceComponent("HBASE_REGIONSERVER");
        org.apache.ambari.server.state.ServiceComponent clientComponent = service.addServiceComponent("HBASE_CLIENT");
        for (java.lang.String hostName : hosts) {
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6.3");
            host.setHostAttributes(hostAttributes);
            host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
            clusters.mapHostToCluster(hostName, clusterName);
            org.apache.ambari.server.state.ServiceComponentHost sch = clientComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            sch = masterComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
            sch = secondMasterComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
        }
    }

    @java.lang.Override
    public void testServiceState_STARTED() throws java.lang.Exception {
        updateServiceState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, state);
    }

    @java.lang.Override
    public void testServiceState_STOPPED() throws java.lang.Exception {
        updateServiceState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, state);
    }
}