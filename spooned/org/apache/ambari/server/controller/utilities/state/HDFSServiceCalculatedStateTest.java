package org.apache.ambari.server.controller.utilities.state;
public class HDFSServiceCalculatedStateTest extends org.apache.ambari.server.controller.utilities.state.GeneralServiceCalculatedStateTest {
    @java.lang.Override
    protected java.lang.String getServiceName() {
        return "HDFS";
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState getServiceCalculatedStateObject() {
        return new org.apache.ambari.server.controller.utilities.state.HDFSServiceCalculatedState();
    }

    @java.lang.Override
    protected void createComponentsAndHosts() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponent masterComponent = service.addServiceComponent("NAMENODE");
        org.apache.ambari.server.state.ServiceComponent masterComponent1 = service.addServiceComponent("SECONDARY_NAMENODE");
        org.apache.ambari.server.state.ServiceComponent clientComponent = service.addServiceComponent("HDFS_CLIENT");
        for (java.lang.String hostName : hosts) {
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6.3");
            host.setHostAttributes(hostAttributes);
            host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
            clusters.mapHostToCluster(hostName, clusterName);
            org.apache.ambari.server.state.ServiceComponentHost sch = masterComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
            org.apache.ambari.server.state.ServiceComponentHost sch1 = masterComponent1.addServiceComponentHost(hostName);
            sch1.setVersion("2.1.1.0");
            sch1.setState(org.apache.ambari.server.state.State.STARTED);
            sch = clientComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
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

    @org.junit.Test
    public void testServiceState_STOPPED_WITH_TWO_NS() throws java.lang.Exception {
        simulateNNFederation();
        org.apache.ambari.server.state.ServiceComponent nnComponent = service.getServiceComponent("NAMENODE");
        updateServiceState(org.apache.ambari.server.state.State.STARTED);
        nnComponent.getServiceComponentHost("h3").setState(org.apache.ambari.server.state.State.INSTALLED);
        nnComponent.getServiceComponentHost("h4").setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, state);
    }

    @org.junit.Test
    public void testServiceState_STARTED_WITH_TWO_NS() throws java.lang.Exception {
        simulateNNFederation();
        org.apache.ambari.server.state.ServiceComponent nnComponent = service.getServiceComponent("NAMENODE");
        updateServiceState(org.apache.ambari.server.state.State.STARTED);
        nnComponent.getServiceComponentHost("h1").setState(org.apache.ambari.server.state.State.INSTALLED);
        nnComponent.getServiceComponentHost("h4").setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.State state = serviceCalculatedState.getState(clusterName, getServiceName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, state);
    }

    private void simulateNNFederation() throws org.apache.ambari.server.AmbariException {
        java.util.HashMap<java.lang.String, java.lang.String> hdfsSiteProperties = new java.util.HashMap<>();
        hdfsSiteProperties.put("dfs.internal.nameservices", "ns1,ns2");
        hdfsSiteProperties.put("dfs.ha.namenodes.ns1", "nn1,nn2");
        hdfsSiteProperties.put("dfs.ha.namenodes.ns2", "nn3,nn4");
        hdfsSiteProperties.put("dfs.namenode.http-address.ns1.nn1", "h1:1234");
        hdfsSiteProperties.put("dfs.namenode.http-address.ns1.nn2", "h2:1234");
        hdfsSiteProperties.put("dfs.namenode.http-address.ns2.nn3", "h3:1234");
        hdfsSiteProperties.put("dfs.namenode.http-address.ns2.nn4", "h4:1234");
        org.apache.ambari.server.state.ConfigFactory configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, "hdfs-site", "version1", hdfsSiteProperties, new java.util.HashMap<>());
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config));
        org.apache.ambari.server.state.ServiceComponent nnComponent = service.getServiceComponent("NAMENODE");
        org.apache.ambari.server.state.ServiceComponent clientComponent = service.getServiceComponent("HDFS_CLIENT");
        org.apache.ambari.server.state.ServiceComponent jnComponent = service.addServiceComponent("JOURNALNODE");
        java.util.List<java.lang.String> newHosts = new java.util.ArrayList<>();
        newHosts.add("h3");
        newHosts.add("h4");
        for (java.lang.String hostName : newHosts) {
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6.3");
            host.setHostAttributes(hostAttributes);
            host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
            clusters.mapHostToCluster(hostName, clusterName);
            org.apache.ambari.server.state.ServiceComponentHost sch = nnComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.STARTED);
            sch = jnComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            sch = clientComponent.addServiceComponentHost(hostName);
            sch.setVersion("2.1.1.0");
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        }
    }
}