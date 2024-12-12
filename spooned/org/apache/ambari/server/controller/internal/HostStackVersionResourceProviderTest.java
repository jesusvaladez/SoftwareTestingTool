package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class HostStackVersionResourceProviderTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAOMock;

    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAOMock;

    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.controller.RequestStatusResponse response;

    private org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory;

    private org.apache.ambari.server.controller.spi.ResourceProvider csvResourceProvider;

    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    private org.easymock.Capture<org.apache.ambari.server.actionmanager.Request> requestCapture;

    private org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> executeActionRequestCapture;

    private org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntityMock;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion;

    private org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion;

    private java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystemsEn = new java.util.ArrayList<>();

    {
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("HDP-UTILS-1.1.0.20");
        repoDefinitionEntity1.setBaseUrl("http://s3.amazonaws.com/dev.hortonworks.com/HDP/centos5/2.x/updates/2.2.0.0");
        repoDefinitionEntity1.setRepoName("HDP-UTILS");
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity2.setRepoID("HDP-2.2");
        repoDefinitionEntity2.setBaseUrl("http://s3.amazonaws.com/dev.hortonworks.com/HDP/centos5/2.x/updates/2.2.0.0");
        repoDefinitionEntity2.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity2);
        operatingSystemsEn.add(repoOsEntity);
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        repositoryVersionDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        hostVersionDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(module).with(new org.apache.ambari.server.controller.internal.HostStackVersionResourceProviderTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        csvResourceProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.class);
        hostVersionEntityMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        actionManager = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        repoVersion = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersion.addRepoOsEntities(operatingSystemsEn);
        org.apache.ambari.server.orm.entities.StackEntity stack = new org.apache.ambari.server.orm.entities.StackEntity();
        stack.setStackName("HDP");
        stack.setStackVersion("2.0.1");
        repoVersion.setStack(stack);
        repoVersion.setVersion("2.2");
        repoVersion.setId(1L);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        java.lang.String hostname = "host1";
        java.lang.String clustername = "Cluster100";
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().begin().property(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID).equals(hostname).and().property(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID).equals(clustername).end().and().begin().property(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STATE_PROPERTY_ID).equals("INSTALLING").or().property(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STATE_PROPERTY_ID).equals("INSTALL_FAILED").or().property(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STATE_PROPERTY_ID).equals("OUT_OF_SYNC").end().toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.emptySet(), null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        EasyMock.expect(hostVersionDAOMock.findByClusterAndHost(clustername, hostname)).andReturn(java.util.Collections.singletonList(hostVersionEntityMock));
        EasyMock.expect(hostVersionEntityMock.getRepositoryVersion()).andReturn(repoVersion).anyTimes();
        EasyMock.expect(repositoryVersionDAOMock.findByStackAndVersion(EasyMock.isA(org.apache.ambari.server.state.StackId.class), EasyMock.isA(java.lang.String.class))).andReturn(null).anyTimes();
        EasyMock.expect(hostVersionEntityMock.getState()).andReturn(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING).anyTimes();
        EasyMock.replay(hostVersionDAOMock, hostVersionEntityMock, repositoryVersionDAOMock);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        EasyMock.verify(hostVersionDAOMock, hostVersionEntityMock, repositoryVersionDAOMock);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.1");
        final org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock("host1", org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(host1.getOsFamily()).andReturn("redhat6").anyTimes();
        EasyMock.replay(host1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostsForCluster = java.util.Collections.singletonMap(host1.getHostName(), host1);
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = java.util.Collections.singletonList(sch);
        final org.apache.ambari.server.state.ServiceOsSpecific.Package hivePackage = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
        hivePackage.setName("hive");
        final org.apache.ambari.server.state.ServiceOsSpecific.Package mysqlPackage = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
        mysqlPackage.setName("mysql");
        mysqlPackage.setSkipUpgrade(java.lang.Boolean.TRUE);
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = java.util.Arrays.asList(hivePackage, mysqlPackage);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostConfigTags = new java.util.HashMap<>();
        EasyMock.expect(configHelper.getEffectiveDesiredTags(EasyMock.anyObject(org.apache.ambari.server.state.cluster.ClusterImpl.class), EasyMock.anyObject(java.lang.String.class))).andReturn(hostConfigTags);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getJdkResourceUrl()).andReturn("/JdkResourceUrl").anyTimes();
        EasyMock.expect(managementController.getPackagesForServiceHost(EasyMock.anyObject(org.apache.ambari.server.state.ServiceInfo.class), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(java.lang.String.class))).andReturn(packages).anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(csvResourceProvider).anyTimes();
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster);
        EasyMock.expect(clusters.getHost(EasyMock.anyObject(java.lang.String.class))).andReturn(host1);
        EasyMock.expect(cluster.getHosts()).andReturn(hostsForCluster.values()).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId);
        EasyMock.expect(cluster.getServiceComponentHosts(EasyMock.anyObject(java.lang.String.class))).andReturn(schs).anyTimes();
        EasyMock.expect(sch.getServiceName()).andReturn("HIVE").anyTimes();
        EasyMock.expect(repositoryVersionDAOMock.findByStackAndVersion(EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyObject(java.lang.String.class))).andReturn(repoVersion);
        EasyMock.expect(hostVersionDAOMock.findByClusterStackVersionAndHost(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(hostVersionEntityMock);
        EasyMock.expect(hostVersionEntityMock.getState()).andReturn(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED).anyTimes();
        EasyMock.expect(actionManager.getRequestTasks(EasyMock.anyLong())).andReturn(java.util.Collections.emptyList()).anyTimes();
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        EasyMock.replay(managementController, response, clusters, resourceProviderFactory, csvResourceProvider, cluster, repositoryVersionDAOMock, configHelper, sch, actionManager, hostVersionEntityMock, hostVersionDAOMock);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        injector.injectMembers(provider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, "2.2.0.1-885");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, "2.0.1");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, "host1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response, clusters);
    }

    @org.junit.Test
    public void testCreateResources_on_host_not_belonging_To_any_cluster() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.1");
        final org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock("host1", org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(host1.getOsFamily()).andReturn("redhat6").anyTimes();
        EasyMock.replay(host1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostsForCluster = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        final org.apache.ambari.server.state.ServiceOsSpecific.Package hivePackage = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
        hivePackage.setName("hive");
        final org.apache.ambari.server.state.ServiceOsSpecific.Package mysqlPackage = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
        mysqlPackage.setName("mysql");
        mysqlPackage.setSkipUpgrade(java.lang.Boolean.TRUE);
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = java.util.Arrays.asList(hivePackage, mysqlPackage);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostConfigTags = new java.util.HashMap<>();
        EasyMock.expect(configHelper.getEffectiveDesiredTags(EasyMock.anyObject(org.apache.ambari.server.state.cluster.ClusterImpl.class), EasyMock.anyObject(java.lang.String.class))).andReturn(hostConfigTags);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getJdkResourceUrl()).andReturn("/JdkResourceUrl").anyTimes();
        EasyMock.expect(managementController.getPackagesForServiceHost(EasyMock.anyObject(org.apache.ambari.server.state.ServiceInfo.class), EasyMock.anyObject(java.util.Map.class), EasyMock.anyObject(java.lang.String.class))).andReturn(packages).anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(csvResourceProvider).anyTimes();
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster);
        EasyMock.expect(clusters.getHost(EasyMock.anyObject(java.lang.String.class))).andReturn(host1);
        EasyMock.expect(cluster.getHosts()).andReturn(hostsForCluster.values()).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId);
        EasyMock.expect(repositoryVersionDAOMock.findByStackAndVersion(EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyObject(java.lang.String.class))).andReturn(repoVersion);
        EasyMock.expect(actionManager.getRequestTasks(EasyMock.anyLong())).andReturn(java.util.Collections.emptyList()).anyTimes();
        requestCapture = EasyMock.newCapture();
        executeActionRequestCapture = EasyMock.newCapture();
        actionManager.sendActions(EasyMock.capture(requestCapture), EasyMock.capture(executeActionRequestCapture));
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        EasyMock.replay(managementController, response, clusters, resourceProviderFactory, csvResourceProvider, cluster, repositoryVersionDAOMock, configHelper, sch, actionManager, hostVersionEntityMock, hostVersionDAOMock);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        injector.injectMembers(provider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, "2.2.0.1-885");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, "2.0.1");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, "host1");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> components = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> hiveMetastoreComponent = new java.util.HashMap<>();
        hiveMetastoreComponent.put("name", "HIVE_METASTORE");
        components.add(hiveMetastoreComponent);
        java.util.Map<java.lang.String, java.lang.String> hiveServerstoreComponent = new java.util.HashMap<>();
        hiveServerstoreComponent.put("name", "HIVE_SERVER");
        components.add(hiveServerstoreComponent);
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_COMPONENT_NAMES_PROPERTY_ID, components);
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_FORCE_INSTALL_ON_NON_MEMBER_HOST_PROPERTY_ID, "true");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response, clusters);
        junit.framework.Assert.assertEquals(requestCapture.getValue().getStages().size(), 2);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>(requestCapture.getValue().getStages());
        org.apache.ambari.server.actionmanager.Stage installStage = stages.get(0);
        org.apache.ambari.server.actionmanager.Stage selectStackStage = stages.get(1);
        java.lang.String hostName = "host1";
        junit.framework.Assert.assertEquals(1, installStage.getExecutionCommands().size());
        junit.framework.Assert.assertEquals(hostName, installStage.getExecutionCommands().keySet().iterator().next());
        junit.framework.Assert.assertEquals(1, installStage.getExecutionCommands().get(hostName).size());
        junit.framework.Assert.assertEquals(true, installStage.getExecutionCommands().get(hostName).get(0).getExecutionCommand().isOverrideConfigs());
        junit.framework.Assert.assertEquals(1, selectStackStage.getExecutionCommands().size());
        junit.framework.Assert.assertEquals(hostName, selectStackStage.getExecutionCommands().keySet().iterator().next());
        junit.framework.Assert.assertEquals(1, selectStackStage.getExecutionCommands().get(hostName).size());
        junit.framework.Assert.assertEquals(false, selectStackStage.getExecutionCommands().get(hostName).get(0).getExecutionCommand().isOverrideConfigs());
    }

    @org.junit.Test
    public void testCreateResources_in_out_of_sync_state() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.1");
        final org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock("host1", org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(host1.getOsFamily()).andReturn("redhat6").anyTimes();
        EasyMock.replay(host1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostsForCluster = java.util.Collections.singletonMap(host1.getHostName(), host1);
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = java.util.Collections.singletonList(sch);
        org.apache.ambari.server.state.ServiceOsSpecific.Package hivePackage = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
        hivePackage.setName("hive");
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = java.util.Collections.singletonList(hivePackage);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostConfigTags = new java.util.HashMap<>();
        EasyMock.expect(configHelper.getEffectiveDesiredTags(EasyMock.anyObject(org.apache.ambari.server.state.cluster.ClusterImpl.class), EasyMock.anyObject(java.lang.String.class))).andReturn(hostConfigTags);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getJdkResourceUrl()).andReturn("/JdkResourceUrl").anyTimes();
        EasyMock.expect(managementController.getPackagesForServiceHost(EasyMock.anyObject(org.apache.ambari.server.state.ServiceInfo.class), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(java.lang.String.class))).andReturn(packages).anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(csvResourceProvider).anyTimes();
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster);
        EasyMock.expect(clusters.getHost(EasyMock.anyObject(java.lang.String.class))).andReturn(host1);
        EasyMock.expect(cluster.getHosts()).andReturn(hostsForCluster.values()).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId);
        EasyMock.expect(cluster.getServiceComponentHosts(EasyMock.anyObject(java.lang.String.class))).andReturn(schs).anyTimes();
        EasyMock.expect(sch.getServiceName()).andReturn("HIVE").anyTimes();
        EasyMock.expect(repositoryVersionDAOMock.findByStackAndVersion(EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyObject(java.lang.String.class))).andReturn(repoVersion);
        EasyMock.expect(hostVersionDAOMock.findByClusterStackVersionAndHost(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(hostVersionEntityMock);
        EasyMock.expect(hostVersionEntityMock.getState()).andReturn(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC).anyTimes();
        EasyMock.expect(actionManager.getRequestTasks(EasyMock.anyLong())).andReturn(java.util.Collections.emptyList()).anyTimes();
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        EasyMock.replay(managementController, response, clusters, resourceProviderFactory, csvResourceProvider, cluster, repositoryVersionDAOMock, configHelper, sch, actionManager, hostVersionEntityMock, hostVersionDAOMock);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        injector.injectMembers(provider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, "2.2.0.1-885");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, "HDP");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, "2.0.1");
        properties.put(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, "host1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response, clusters);
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class).toInstance(repositoryVersionDAOMock);
            bind(org.apache.ambari.server.orm.dao.HostVersionDAO.class).toInstance(hostVersionDAOMock);
            bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(configHelper);
        }
    }
}