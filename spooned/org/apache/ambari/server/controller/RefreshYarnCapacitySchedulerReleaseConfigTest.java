package org.apache.ambari.server.controller;
import org.springframework.security.core.context.SecurityContextHolder;
@java.lang.SuppressWarnings("serial")
public class RefreshYarnCapacitySchedulerReleaseConfigTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.controller.AmbariManagementController controller;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    private java.lang.String stackName;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testRMRequiresRestart() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        createClusterFixture("HDP-2.0.7");
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.controller.ClusterRequest cr = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), "c1", cluster.getDesiredStackVersion().getStackVersion(), null);
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest("c1", "capacity-scheduler", "version2", new java.util.HashMap<>(), null)));
        controller.updateClusters(java.util.Collections.singleton(cr), null);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest("c1", null, null, null, null);
        r.setStaleConfig("true");
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = controller.getHostComponents(java.util.Collections.singleton(r));
        junit.framework.Assert.assertEquals(1, resps.size());
        junit.framework.Assert.assertEquals(true, configHelper.isStaleConfigs(clusters.getCluster("c1").getService("YARN").getServiceComponent("RESOURCEMANAGER").getServiceComponentHost("c6401"), null));
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testAllRequiresRestart() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        createClusterFixture("HDP-2.0.7");
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.controller.ClusterRequest cr = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), "c1", cluster.getDesiredStackVersion().getStackVersion(), null);
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest("c1", "core-site", "version2", new java.util.HashMap<>(), null)));
        controller.updateClusters(java.util.Collections.singleton(cr), null);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest("c1", null, null, null, null);
        r.setStaleConfig("true");
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = controller.getHostComponents(java.util.Collections.singleton(r));
        junit.framework.Assert.assertEquals(4, resps.size());
    }

    @org.junit.Test
    public void testConfigInComponent() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceRequest("HDP", "2.0.6", "YARN");
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responsesWithParams = controller.getStackServices(java.util.Collections.singleton(requestWithParams));
        junit.framework.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceResponse responseWithParams : responsesWithParams) {
            junit.framework.Assert.assertEquals(responseWithParams.getServiceName(), "YARN");
            junit.framework.Assert.assertTrue(responseWithParams.getConfigTypes().containsKey("capacity-scheduler"));
        }
    }

    @org.junit.Test
    public void testConfigInComponentOverwrited() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceRequest("HDP", "2.0.7", "YARN");
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responsesWithParams = controller.getStackServices(java.util.Collections.singleton(requestWithParams));
        junit.framework.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceResponse responseWithParams : responsesWithParams) {
            junit.framework.Assert.assertEquals(responseWithParams.getServiceName(), "YARN");
            junit.framework.Assert.assertTrue(responseWithParams.getConfigTypes().containsKey("capacity-scheduler"));
        }
    }

    private void createClusterFixture(java.lang.String stackName) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        this.stackName = stackName;
        createCluster("c1", stackName);
        addHost("c6401", "c1");
        addHost("c6402", "c1");
        clusters.updateHostMappings(clusters.getHost("c6401"));
        clusters.updateHostMappings(clusters.getHost("c6402"));
        clusters.getCluster("c1");
        createService("c1", "YARN", null);
        createServiceComponent("c1", "YARN", "RESOURCEMANAGER", org.apache.ambari.server.state.State.INIT);
        createServiceComponent("c1", "YARN", "NODEMANAGER", org.apache.ambari.server.state.State.INIT);
        createServiceComponent("c1", "YARN", "YARN_CLIENT", org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost("c1", "YARN", "RESOURCEMANAGER", "c6401", null);
        createServiceComponentHost("c1", "YARN", "NODEMANAGER", "c6401", null);
        createServiceComponentHost("c1", "YARN", "NODEMANAGER", "c6402", null);
        createServiceComponentHost("c1", "YARN", "YARN_CLIENT", "c6402", null);
    }

    private void addHost(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        clusters.addHost(hostname);
        setOsFamily(clusters.getHost(hostname), "redhat", "6.3");
        clusters.getHost(hostname).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        if (null != clusterName) {
            clusters.mapHostToCluster(hostname, clusterName);
        }
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    private void createCluster(java.lang.String clusterName, java.lang.String stackName) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, stackName, null);
        controller.createCluster(r);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"), "2.0.7-1234");
        org.apache.ambari.server.controller.ServiceRequest r1 = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, repositoryVersion.getId(), dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(controller, injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class), requests);
    }

    private void createServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, serviceName, componentName, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(controller, requests);
    }

    private void createServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        controller.createHostComponents(requests);
        org.apache.ambari.server.state.Service service = clusters.getCluster(clusterName).getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent rm = service.getServiceComponent(componentName);
        org.apache.ambari.server.state.ServiceComponentHost rmc1 = rm.getServiceComponentHost(hostname);
    }
}