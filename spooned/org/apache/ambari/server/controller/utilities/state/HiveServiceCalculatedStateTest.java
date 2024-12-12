package org.apache.ambari.server.controller.utilities.state;
public class HiveServiceCalculatedStateTest extends org.apache.ambari.server.controller.utilities.state.GeneralServiceCalculatedStateTest {
    @java.lang.Override
    protected java.lang.String getServiceName() {
        return "HIVE";
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState getServiceCalculatedStateObject() {
        return new org.apache.ambari.server.controller.utilities.state.HiveServiceCalculatedState();
    }

    @java.lang.Override
    protected void createComponentsAndHosts() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponent masterComponent = service.addServiceComponent("HIVE_METASTORE");
        org.apache.ambari.server.state.ServiceComponent secondMasterComponent = service.addServiceComponent("HIVE_SERVER");
        org.apache.ambari.server.state.ServiceComponent thirdMasterComponent = service.addServiceComponent("WEBHCAT_SERVER");
        org.apache.ambari.server.state.ServiceComponent fourMasterComponent = service.addServiceComponent("MYSQL_SERVER");
        org.apache.ambari.server.state.ServiceComponent clientComponent = service.addServiceComponent("HIVE_CLIENT");
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
            sch = thirdMasterComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
            sch = fourMasterComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
        }
    }

    @java.lang.Override
    public void testServiceState_STARTED() throws java.lang.Exception {
        updateServiceState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, state);
        service.getServiceComponent("HIVE_METASTORE").getServiceComponentHost(hosts[0]).setState(org.apache.ambari.server.state.State.INSTALLED);
        state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, state);
        service.getServiceComponent("HIVE_METASTORE").getServiceComponentHost(hosts[1]).setState(org.apache.ambari.server.state.State.INSTALLED);
        state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, state);
    }

    @java.lang.Override
    public void testServiceState_STOPPED() throws java.lang.Exception {
        updateServiceState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, state);
    }
}