package org.apache.ambari.server.controller.utilities.state;
public abstract class GeneralServiceCalculatedStateTest {
    protected final java.lang.String[] hosts = new java.lang.String[]{ "h1", "h2" };

    protected final java.lang.String clusterName = "c1";

    protected org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState serviceCalculatedState;

    protected com.google.inject.Injector injector;

    protected org.apache.ambari.server.state.Cluster cluster;

    protected org.apache.ambari.server.state.Service service;

    @com.google.inject.Inject
    protected org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        final org.apache.ambari.server.state.StackId stack211 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        final java.lang.String version = "2.1.1-1234";
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new com.google.inject.Module() {
            @java.lang.Override
            public void configure(com.google.inject.Binder binder) {
            }
        }));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(stack211, version);
        clusters.addCluster(clusterName, stack211);
        cluster = clusters.getCluster(clusterName);
        service = cluster.addService(getServiceName(), repositoryVersion);
        createComponentsAndHosts();
        org.apache.ambari.server.topology.TopologyManager topologyManager = injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(topologyManager);
        org.apache.ambari.server.actionmanager.ActionManager.setTopologyManager(topologyManager);
        serviceCalculatedState = getServiceCalculatedStateObject();
    }

    protected abstract java.lang.String getServiceName();

    protected abstract org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState getServiceCalculatedStateObject();

    protected abstract void createComponentsAndHosts() throws java.lang.Exception;

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    protected void updateServiceState(org.apache.ambari.server.state.State newState) throws java.lang.Exception {
        org.apache.ambari.server.state.Service service = cluster.getService(getServiceName());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponentMap = service.getServiceComponents();
        for (org.apache.ambari.server.state.ServiceComponent serviceComponent : serviceComponentMap.values()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostMap = serviceComponent.getServiceComponentHosts();
            for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHostMap.values()) {
                if (serviceComponentHost.getServiceComponentName().contains("_CLIENT")) {
                    serviceComponentHost.setState(org.apache.ambari.server.state.State.INSTALLED);
                } else {
                    switch (newState) {
                        case STARTED :
                            serviceComponentHost.setState(org.apache.ambari.server.state.State.STARTED);
                            break;
                        default :
                            serviceComponentHost.setState(org.apache.ambari.server.state.State.INSTALLED);
                            break;
                    }
                }
            }
        }
    }

    @org.junit.Test
    public abstract void testServiceState_STARTED() throws java.lang.Exception;

    @org.junit.Test
    public abstract void testServiceState_STOPPED() throws java.lang.Exception;
}