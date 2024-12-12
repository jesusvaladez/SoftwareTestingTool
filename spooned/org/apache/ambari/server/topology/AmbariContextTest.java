package org.apache.ambari.server.topology;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class AmbariContextTest {
    private static final java.lang.String BP_NAME = "testBP";

    private static final java.lang.String CLUSTER_NAME = "testCluster";

    private static final long CLUSTER_ID = 1L;

    private static final java.lang.String STACK_NAME = "testStack";

    private static final java.lang.String STACK_VERSION = "testVersion";

    private static final java.lang.String HOST_GROUP_1 = "group1";

    private static final java.lang.String HOST_GROUP_2 = "group2";

    private static final java.lang.String HOST1 = "host1";

    private static final java.lang.String HOST2 = "host2";

    private static final org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId(org.apache.ambari.server.topology.AmbariContextTest.STACK_NAME, org.apache.ambari.server.topology.AmbariContextTest.STACK_VERSION);

    private static final org.apache.ambari.server.topology.AmbariContext context = new org.apache.ambari.server.topology.AmbariContext();

    private static final org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);

    private static final org.apache.ambari.server.controller.spi.ClusterController clusterController = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.ClusterController.class);

    private static final org.apache.ambari.server.controller.internal.HostResourceProvider hostResourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.internal.HostResourceProvider.class);

    private static final org.apache.ambari.server.controller.internal.ServiceResourceProvider serviceResourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.internal.ServiceResourceProvider.class);

    private static final org.apache.ambari.server.controller.internal.ComponentResourceProvider componentResourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.internal.ComponentResourceProvider.class);

    private static final org.apache.ambari.server.controller.internal.HostComponentResourceProvider hostComponentResourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.class);

    private static final org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider configGroupResourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.class);

    private static final org.apache.ambari.server.topology.ClusterTopology topology = EasyMock.createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);

    private static final org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);

    private static final org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);

    private static final org.apache.ambari.server.topology.HostGroupInfo group1Info = EasyMock.createNiceMock(org.apache.ambari.server.topology.HostGroupInfo.class);

    private static final org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);

    private static final org.apache.ambari.server.state.configgroup.ConfigGroup configGroup1 = EasyMock.createMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);

    private static final org.apache.ambari.server.state.configgroup.ConfigGroup configGroup2 = EasyMock.createMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);

    private static final org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);

    private static final org.apache.ambari.server.state.Host host2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);

    private static final org.apache.ambari.server.state.ConfigFactory configFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigFactory.class);

    private static final org.apache.ambari.server.state.Service mockService1 = EasyMock.createStrictMock(org.apache.ambari.server.state.Service.class);

    private static final com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> mockConfigHelperProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);

    private static final org.apache.ambari.server.state.ConfigHelper mockConfigHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);

    private static final java.util.Collection<java.lang.String> blueprintServices = new java.util.HashSet<>();

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = new java.util.HashMap<>();

    private org.apache.ambari.server.topology.Configuration bpConfiguration = null;

    private org.apache.ambari.server.topology.Configuration group1Configuration = null;

    private static final java.util.Collection<java.lang.String> group1Hosts = java.util.Arrays.asList(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.HOST2);

    private org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest>> configGroupRequestCapture = org.easymock.EasyMock.newCapture();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        java.lang.Class<org.apache.ambari.server.topology.AmbariContext> clazz = org.apache.ambari.server.topology.AmbariContext.class;
        java.lang.reflect.Field f = clazz.getDeclaredField("controller");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.controller);
        f = clazz.getDeclaredField("clusterController");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.clusterController);
        f = clazz.getDeclaredField("hostResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider);
        f = clazz.getDeclaredField("serviceResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider);
        f = clazz.getDeclaredField("componentResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider);
        f = clazz.getDeclaredField("hostComponentResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.topology.AmbariContextTest.hostComponentResourceProvider);
        f = clazz.getDeclaredField("configHelper");
        f.setAccessible(true);
        f.set(org.apache.ambari.server.topology.AmbariContextTest.context, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelperProvider);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> bpProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> bpType1Props = new java.util.HashMap<>();
        bpProperties.put("type1", bpType1Props);
        bpType1Props.put("prop1", "val1");
        bpType1Props.put("prop2", "val2");
        bpConfiguration = new org.apache.ambari.server.topology.Configuration(bpProperties, null);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> group1Properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> type1Props = new java.util.HashMap<>();
        group1Properties.put("type1", type1Props);
        type1Props.put("prop1", "val1.2");
        type1Props.put("prop3", "val3");
        group1Configuration = new org.apache.ambari.server.topology.Configuration(group1Properties, null, bpConfiguration);
        java.util.Map<java.lang.String, java.lang.String> group1ResolvedProperties = new java.util.HashMap<>(bpType1Props);
        group1ResolvedProperties.putAll(type1Props);
        java.util.Map<java.lang.String, java.lang.String> configTypeServiceMapping = new java.util.HashMap<>();
        configTypeServiceMapping.put("type1", "service1");
        org.apache.ambari.server.topology.AmbariContextTest.configGroups.put(1L, org.apache.ambari.server.topology.AmbariContextTest.configGroup1);
        org.apache.ambari.server.topology.AmbariContextTest.configGroups.put(2L, org.apache.ambari.server.topology.AmbariContextTest.configGroup2);
        org.apache.ambari.server.state.Config type1Group1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(type1Group1.getType()).andReturn("type1").anyTimes();
        EasyMock.expect(type1Group1.getTag()).andReturn("group1").anyTimes();
        EasyMock.expect(type1Group1.getProperties()).andReturn(group1ResolvedProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configFactory.createReadOnly(org.easymock.EasyMock.eq("type1"), org.easymock.EasyMock.eq("group1"), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(type1Group1).anyTimes();
        EasyMock.replay(type1Group1);
        org.apache.ambari.server.state.Config type1Service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(type1Service1.getType()).andReturn("type1").anyTimes();
        EasyMock.expect(type1Service1.getTag()).andReturn("service1").anyTimes();
        EasyMock.expect(type1Service1.getProperties()).andReturn(type1Props).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configFactory.createReadOnly(org.easymock.EasyMock.eq("type1"), org.easymock.EasyMock.eq("service1"), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(type1Service1).anyTimes();
        EasyMock.replay(type1Service1);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(repositoryVersion.getVersion()).andReturn("1.1.1.1").atLeastOnce();
        EasyMock.expect(repositoryVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).atLeastOnce();
        EasyMock.expect(repositoryVersionDAO.findByStack(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class))).andReturn(java.util.Collections.singletonList(repositoryVersion)).atLeastOnce();
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder = EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.HostLevelParamsHolder.class);
        EasyMock.replay(repositoryVersionDAO, repositoryVersion, hostLevelParamsHolder);
        org.apache.ambari.server.topology.AmbariContextTest.context.configFactory = org.apache.ambari.server.topology.AmbariContextTest.configFactory;
        org.apache.ambari.server.topology.AmbariContextTest.context.repositoryVersionDAO = repositoryVersionDAO;
        org.apache.ambari.server.topology.AmbariContextTest.context.hostLevelParamsHolder = hostLevelParamsHolder;
        org.apache.ambari.server.topology.AmbariContextTest.blueprintServices.add("service1");
        org.apache.ambari.server.topology.AmbariContextTest.blueprintServices.add("service2");
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.topology.getClusterId()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.topology.getBlueprint()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.topology.getHostGroupInfo()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1, org.apache.ambari.server.topology.AmbariContextTest.group1Info)).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getName()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.BP_NAME).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getStack()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.stack).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getServices()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.blueprintServices).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getComponents("service1")).andReturn(java.util.Arrays.asList("s1Component1", "s1Component2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getComponents("service2")).andReturn(java.util.Collections.singleton("s2Component1")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getConfiguration()).andReturn(bpConfiguration).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.blueprint.getCredentialStoreEnabled("service1")).andReturn("true").anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.stack.getName()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.STACK_NAME).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.stack.getVersion()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.STACK_VERSION).anyTimes();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configTypeServiceMapping.entrySet()) {
            EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.stack.getServicesForConfigType(entry.getKey())).andReturn(java.util.Collections.singletonList(entry.getValue())).anyTimes();
        }
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.controller.getClusters()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.clusters).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.controller.getConfigHelper()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.configHelper).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusters.getCluster(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.cluster).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusters.getClusterById(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.cluster).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusters.getHost(org.apache.ambari.server.topology.AmbariContextTest.HOST1)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.host1).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusters.getHost(org.apache.ambari.server.topology.AmbariContextTest.HOST2)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.host2).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.host1, org.apache.ambari.server.topology.AmbariContextTest.HOST2, org.apache.ambari.server.topology.AmbariContextTest.host2);
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusters.getHostsForCluster(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME)).andReturn(clusterHosts).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getClusterId()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getClusterName()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.host1.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.host2.getHostId()).andReturn(2L).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.group1Info.getConfiguration()).andReturn(group1Configuration).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.group1Info.getHostNames()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.group1Hosts).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroup1.getName()).andReturn(java.lang.String.format("%s:%s", org.apache.ambari.server.topology.AmbariContextTest.BP_NAME, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1)).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroup2.getName()).andReturn(java.lang.String.format("%s:%s", org.apache.ambari.server.topology.AmbariContextTest.BP_NAME, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_2)).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelperProvider.get()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelper).anyTimes();
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        EasyMock.verify(org.apache.ambari.server.topology.AmbariContextTest.controller, org.apache.ambari.server.topology.AmbariContextTest.clusterController, org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.hostComponentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.blueprint, org.apache.ambari.server.topology.AmbariContextTest.stack, org.apache.ambari.server.topology.AmbariContextTest.clusters, org.apache.ambari.server.topology.AmbariContextTest.cluster, org.apache.ambari.server.topology.AmbariContextTest.group1Info, org.apache.ambari.server.topology.AmbariContextTest.configHelper, org.apache.ambari.server.topology.AmbariContextTest.configGroup1, org.apache.ambari.server.topology.AmbariContextTest.configGroup2, org.apache.ambari.server.topology.AmbariContextTest.host1, org.apache.ambari.server.topology.AmbariContextTest.host2, org.apache.ambari.server.topology.AmbariContextTest.configFactory, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelperProvider, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelper);
        EasyMock.reset(org.apache.ambari.server.topology.AmbariContextTest.controller, org.apache.ambari.server.topology.AmbariContextTest.clusterController, org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.hostComponentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.blueprint, org.apache.ambari.server.topology.AmbariContextTest.stack, org.apache.ambari.server.topology.AmbariContextTest.clusters, org.apache.ambari.server.topology.AmbariContextTest.cluster, org.apache.ambari.server.topology.AmbariContextTest.group1Info, org.apache.ambari.server.topology.AmbariContextTest.configHelper, org.apache.ambari.server.topology.AmbariContextTest.configGroup1, org.apache.ambari.server.topology.AmbariContextTest.configGroup2, org.apache.ambari.server.topology.AmbariContextTest.host1, org.apache.ambari.server.topology.AmbariContextTest.host2, org.apache.ambari.server.topology.AmbariContextTest.configFactory, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelperProvider, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelper);
    }

    private void replayAll() {
        EasyMock.replay(org.apache.ambari.server.topology.AmbariContextTest.controller, org.apache.ambari.server.topology.AmbariContextTest.clusterController, org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.hostComponentResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.blueprint, org.apache.ambari.server.topology.AmbariContextTest.stack, org.apache.ambari.server.topology.AmbariContextTest.clusters, org.apache.ambari.server.topology.AmbariContextTest.cluster, org.apache.ambari.server.topology.AmbariContextTest.group1Info, org.apache.ambari.server.topology.AmbariContextTest.configHelper, org.apache.ambari.server.topology.AmbariContextTest.configGroup1, org.apache.ambari.server.topology.AmbariContextTest.configGroup2, org.apache.ambari.server.topology.AmbariContextTest.host1, org.apache.ambari.server.topology.AmbariContextTest.host2, org.apache.ambari.server.topology.AmbariContextTest.configFactory, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelperProvider, org.apache.ambari.server.topology.AmbariContextTest.mockConfigHelper);
    }

    @org.junit.Test
    public void testCreateAmbariResources() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.controller.ClusterRequest> clusterRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.topology.AmbariContextTest.controller.createCluster(EasyMock.capture(clusterRequestCapture));
        EasyMock.expectLastCall().once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getServices()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.clusterServices).anyTimes();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ServiceRequest>> serviceRequestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest>> serviceComponentRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider.createServices(EasyMock.capture(serviceRequestCapture));
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider.createComponents(EasyMock.capture(serviceComponentRequestCapture));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Request> serviceInstallRequestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Request> serviceStartRequestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Predicate> installPredicateCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Predicate> startPredicateCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider.updateResources(EasyMock.capture(serviceInstallRequestCapture), EasyMock.capture(installPredicateCapture))).andReturn(null).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider.updateResources(EasyMock.capture(serviceStartRequestCapture), EasyMock.capture(startPredicateCapture))).andReturn(null).once();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, null, null);
        org.apache.ambari.server.controller.ClusterRequest clusterRequest = clusterRequestCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, clusterRequest.getClusterName());
        org.junit.Assert.assertEquals(java.lang.String.format("%s-%s", org.apache.ambari.server.topology.AmbariContextTest.STACK_NAME, org.apache.ambari.server.topology.AmbariContextTest.STACK_VERSION), clusterRequest.getStackVersion());
        java.util.Collection<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = serviceRequestCapture.getValue();
        org.junit.Assert.assertEquals(2, serviceRequests.size());
        java.util.Collection<java.lang.String> servicesFound = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceRequest serviceRequest : serviceRequests) {
            servicesFound.add(serviceRequest.getServiceName());
            org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, serviceRequest.getClusterName());
        }
        org.junit.Assert.assertTrue((servicesFound.size() == 2) && servicesFound.containsAll(java.util.Arrays.asList("service1", "service2")));
        java.util.Collection<org.apache.ambari.server.controller.ServiceComponentRequest> serviceComponentRequests = serviceComponentRequestCapture.getValue();
        org.junit.Assert.assertEquals(3, serviceComponentRequests.size());
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> foundServiceComponents = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.ServiceComponentRequest componentRequest : serviceComponentRequests) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, componentRequest.getClusterName());
            java.lang.String serviceName = componentRequest.getServiceName();
            java.util.Collection<java.lang.String> serviceComponents = foundServiceComponents.get(serviceName);
            if (serviceComponents == null) {
                serviceComponents = new java.util.HashSet<>();
                foundServiceComponents.put(serviceName, serviceComponents);
            }
            serviceComponents.add(componentRequest.getComponentName());
        }
        org.junit.Assert.assertEquals(2, foundServiceComponents.size());
        java.util.Collection<java.lang.String> service1Components = foundServiceComponents.get("service1");
        org.junit.Assert.assertEquals(2, service1Components.size());
        org.junit.Assert.assertTrue(service1Components.containsAll(java.util.Arrays.asList("s1Component1", "s1Component2")));
        java.util.Collection<java.lang.String> service2Components = foundServiceComponents.get("service2");
        org.junit.Assert.assertEquals(1, service2Components.size());
        org.junit.Assert.assertTrue(service2Components.contains("s2Component1"));
        org.apache.ambari.server.controller.spi.Request installRequest = serviceInstallRequestCapture.getValue();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> installPropertiesSet = installRequest.getProperties();
        org.junit.Assert.assertEquals(1, installPropertiesSet.size());
        java.util.Map<java.lang.String, java.lang.Object> installProperties = installPropertiesSet.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, installProperties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals("INSTALLED", installProperties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME), installPredicateCapture.getValue());
        org.apache.ambari.server.controller.spi.Request startRequest = serviceStartRequestCapture.getValue();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> startPropertiesSet = startRequest.getProperties();
        org.junit.Assert.assertEquals(1, startPropertiesSet.size());
        java.util.Map<java.lang.String, java.lang.Object> startProperties = startPropertiesSet.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, startProperties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals("STARTED", startProperties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME), installPredicateCapture.getValue());
    }

    @org.junit.Test
    public void testCreateAmbariHostResources() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getServices()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.clusterServices).anyTimes();
        org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider.createHosts(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getService("service1")).andReturn(org.apache.ambari.server.topology.AmbariContextTest.mockService1).times(2);
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getService("service2")).andReturn(org.apache.ambari.server.topology.AmbariContextTest.mockService1).once();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest>> requestsCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.topology.AmbariContextTest.controller.createHostComponents(EasyMock.capture(requestsCapture), EasyMock.eq(true));
        EasyMock.expectLastCall().once();
        replayAll();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> componentsMap = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> components = new java.util.ArrayList<>();
        components.add("component1");
        components.add("component2");
        componentsMap.put("service1", components);
        components = new java.util.ArrayList<>();
        components.add("component3");
        componentsMap.put("service2", components);
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariHostResources(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID, "host1", componentsMap);
        org.junit.Assert.assertEquals(requestsCapture.getValue().size(), 3);
    }

    @org.junit.Test
    public void testCreateAmbariHostResourcesWithMissingService() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getServices()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.clusterServices).anyTimes();
        org.apache.ambari.server.topology.AmbariContextTest.hostResourceProvider.createHosts(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getService("service1")).andReturn(org.apache.ambari.server.topology.AmbariContextTest.mockService1).times(2);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest>> requestsCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.topology.AmbariContextTest.controller.createHostComponents(EasyMock.capture(requestsCapture), EasyMock.eq(true));
        EasyMock.expectLastCall().once();
        replayAll();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> componentsMap = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> components = new java.util.ArrayList<>();
        components.add("component1");
        components.add("component2");
        componentsMap.put("service1", components);
        components = new java.util.ArrayList<>();
        components.add("component3");
        componentsMap.put("service2", components);
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariHostResources(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID, "host1", componentsMap);
        org.junit.Assert.assertEquals(requestsCapture.getValue().size(), 2);
    }

    @org.junit.Test
    public void testRegisterHostWithConfigGroup_createNewConfigGroup() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getConfigGroups()).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider.createResources(EasyMock.capture(configGroupRequestCapture))).andReturn(null).once();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.registerHostWithConfigGroup(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1);
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> configGroupRequests = configGroupRequestCapture.getValue();
        org.junit.Assert.assertEquals(1, configGroupRequests.size());
        org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest = configGroupRequests.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, configGroupRequest.getClusterName());
        org.junit.Assert.assertEquals("testBP:group1", configGroupRequest.getGroupName());
        org.junit.Assert.assertEquals("service1", configGroupRequest.getTag());
        org.junit.Assert.assertEquals("Host Group Configuration", configGroupRequest.getDescription());
        java.util.Collection<java.lang.String> requestHosts = configGroupRequest.getHosts();
        requestHosts.retainAll(org.apache.ambari.server.topology.AmbariContextTest.group1Hosts);
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.group1Hosts.size(), requestHosts.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> requestConfig = configGroupRequest.getConfigs();
        org.junit.Assert.assertEquals(1, requestConfig.size());
        org.apache.ambari.server.state.Config type1Config = requestConfig.get("type1");
        org.junit.Assert.assertEquals("type1", type1Config.getType());
        org.junit.Assert.assertEquals("group1", type1Config.getTag());
        java.util.Map<java.lang.String, java.lang.String> requestProps = type1Config.getProperties();
        org.junit.Assert.assertEquals(3, requestProps.size());
        org.junit.Assert.assertEquals("val1.2", requestProps.get("prop1"));
        org.junit.Assert.assertEquals("val2", requestProps.get("prop2"));
        org.junit.Assert.assertEquals("val3", requestProps.get("prop3"));
    }

    @org.junit.Test
    public void testRegisterHostWithConfigGroup_createNewConfigGroupWithPendingHosts() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getConfigGroups()).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup)).andReturn(org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroupResourceProvider.createResources(EasyMock.capture(configGroupRequestCapture))).andReturn(null).once();
        EasyMock.reset(org.apache.ambari.server.topology.AmbariContextTest.group1Info);
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.group1Info.getConfiguration()).andReturn(group1Configuration).anyTimes();
        java.util.Collection<java.lang.String> groupHosts = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.HOST2, "pending_host");
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.group1Info.getHostNames()).andReturn(groupHosts).anyTimes();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.registerHostWithConfigGroup(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1);
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> configGroupRequests = configGroupRequestCapture.getValue();
        org.junit.Assert.assertEquals(1, configGroupRequests.size());
        org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest = configGroupRequests.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, configGroupRequest.getClusterName());
        org.junit.Assert.assertEquals("testBP:group1", configGroupRequest.getGroupName());
        org.junit.Assert.assertEquals("service1", configGroupRequest.getTag());
        org.junit.Assert.assertEquals("Host Group Configuration", configGroupRequest.getDescription());
        java.util.Collection<java.lang.String> requestHosts = configGroupRequest.getHosts();
        org.junit.Assert.assertEquals(2, requestHosts.size());
        org.junit.Assert.assertTrue(requestHosts.contains(org.apache.ambari.server.topology.AmbariContextTest.HOST1));
        org.junit.Assert.assertTrue(requestHosts.contains(org.apache.ambari.server.topology.AmbariContextTest.HOST2));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> requestConfig = configGroupRequest.getConfigs();
        org.junit.Assert.assertEquals(1, requestConfig.size());
        org.apache.ambari.server.state.Config type1Config = requestConfig.get("type1");
        org.junit.Assert.assertEquals("type1", type1Config.getType());
        org.junit.Assert.assertEquals("group1", type1Config.getTag());
        java.util.Map<java.lang.String, java.lang.String> requestProps = type1Config.getProperties();
        org.junit.Assert.assertEquals(3, requestProps.size());
        org.junit.Assert.assertEquals("val1.2", requestProps.get("prop1"));
        org.junit.Assert.assertEquals("val2", requestProps.get("prop2"));
        org.junit.Assert.assertEquals("val3", requestProps.get("prop3"));
    }

    @org.junit.Test
    public void testRegisterHostWithConfigGroup_registerWithExistingConfigGroup() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getConfigGroups()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.configGroups).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroup1.getHosts()).andReturn(java.util.Collections.singletonMap(2L, org.apache.ambari.server.topology.AmbariContextTest.host2)).once();
        org.apache.ambari.server.topology.AmbariContextTest.configGroup1.addHost(org.apache.ambari.server.topology.AmbariContextTest.host1);
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.registerHostWithConfigGroup(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1);
    }

    @org.junit.Test
    public void testRegisterHostWithConfigGroup_registerWithExistingConfigGroup_hostAlreadyRegistered() throws java.lang.Exception {
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getConfigGroups()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.configGroups).once();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.configGroup1.getHosts()).andReturn(java.util.Collections.singletonMap(1L, org.apache.ambari.server.topology.AmbariContextTest.host1)).once();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.registerHostWithConfigGroup(org.apache.ambari.server.topology.AmbariContextTest.HOST1, org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.HOST_GROUP_1);
    }

    @org.junit.Test
    public void testWaitForTopologyResolvedStateWithEmptyUpdatedSet() throws java.lang.Exception {
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.waitForConfigurationResolution(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, java.util.Collections.emptySet());
    }

    @org.junit.Test
    public void testWaitForTopologyResolvedStateWithRequiredUpdatedSet() throws java.lang.Exception {
        final java.lang.String topologyResolvedState = "TOPOLOGY_RESOLVED";
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig.setTag(topologyResolvedState);
        org.apache.ambari.server.state.DesiredConfig testCoreSiteDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testCoreSiteDesiredConfig.setTag(topologyResolvedState);
        org.apache.ambari.server.state.DesiredConfig testClusterEnvDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testClusterEnvDesiredConfig.setTag(topologyResolvedState);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> testDesiredConfigs = new java.util.HashMap<>();
        testDesiredConfigs.put("hdfs-site", testHdfsDesiredConfig);
        testDesiredConfigs.put("core-site", testCoreSiteDesiredConfig);
        testDesiredConfigs.put("cluster-env", testClusterEnvDesiredConfig);
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getDesiredConfigs()).andReturn(testDesiredConfigs).atLeastOnce();
        replayAll();
        java.util.Set<java.lang.String> testUpdatedConfigTypes = new java.util.HashSet<>();
        testUpdatedConfigTypes.add("hdfs-site");
        testUpdatedConfigTypes.add("core-site");
        testUpdatedConfigTypes.add("cluster-env");
        org.apache.ambari.server.topology.AmbariContextTest.context.waitForConfigurationResolution(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, testUpdatedConfigTypes);
    }

    @org.junit.Test
    public void testIsTopologyResolved_True() throws java.lang.Exception {
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig1 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig1.setTag(org.apache.ambari.server.topology.TopologyManager.INITIAL_CONFIG_TAG);
        testHdfsDesiredConfig1.setVersion(1L);
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig2 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig2.setTag(org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG);
        testHdfsDesiredConfig2.setVersion(2L);
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig3 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig3.setTag("ver123");
        testHdfsDesiredConfig3.setVersion(3L);
        org.apache.ambari.server.state.DesiredConfig testCoreSiteDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testCoreSiteDesiredConfig.setTag("ver123");
        testCoreSiteDesiredConfig.setVersion(1L);
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> testDesiredConfigs = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>>builder().put("hdfs-site", com.google.common.collect.ImmutableSet.of(testHdfsDesiredConfig2, testHdfsDesiredConfig3, testHdfsDesiredConfig1)).put("core-site", com.google.common.collect.ImmutableSet.of(testCoreSiteDesiredConfig)).build();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getAllDesiredConfigVersions()).andReturn(testDesiredConfigs).atLeastOnce();
        replayAll();
        boolean topologyResolved = org.apache.ambari.server.topology.AmbariContextTest.context.isTopologyResolved(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID);
        org.junit.Assert.assertTrue(topologyResolved);
    }

    @org.junit.Test
    public void testIsTopologyResolved_WrongOrder_False() throws java.lang.Exception {
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig1 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig1.setTag(org.apache.ambari.server.topology.TopologyManager.INITIAL_CONFIG_TAG);
        testHdfsDesiredConfig1.setVersion(2L);
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig2 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig2.setTag(org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG);
        testHdfsDesiredConfig2.setVersion(1L);
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig3 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig3.setTag("ver123");
        testHdfsDesiredConfig3.setVersion(3L);
        org.apache.ambari.server.state.DesiredConfig testCoreSiteDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testCoreSiteDesiredConfig.setTag("ver123");
        testCoreSiteDesiredConfig.setVersion(1L);
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> testDesiredConfigs = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>>builder().put("hdfs-site", com.google.common.collect.ImmutableSet.of(testHdfsDesiredConfig2, testHdfsDesiredConfig3, testHdfsDesiredConfig1)).put("core-site", com.google.common.collect.ImmutableSet.of(testCoreSiteDesiredConfig)).build();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getAllDesiredConfigVersions()).andReturn(testDesiredConfigs).atLeastOnce();
        replayAll();
        boolean topologyResolved = org.apache.ambari.server.topology.AmbariContextTest.context.isTopologyResolved(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID);
        org.junit.Assert.assertFalse(topologyResolved);
    }

    @org.junit.Test
    public void testIsTopologyResolved_False() throws java.lang.Exception {
        org.apache.ambari.server.state.DesiredConfig testHdfsDesiredConfig1 = new org.apache.ambari.server.state.DesiredConfig();
        testHdfsDesiredConfig1.setTag("ver1222");
        testHdfsDesiredConfig1.setVersion(1L);
        org.apache.ambari.server.state.DesiredConfig testCoreSiteDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testCoreSiteDesiredConfig.setTag("ver123");
        testCoreSiteDesiredConfig.setVersion(1L);
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> testDesiredConfigs = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>>builder().put("hdfs-site", com.google.common.collect.ImmutableSet.of(testHdfsDesiredConfig1)).put("core-site", com.google.common.collect.ImmutableSet.of(testCoreSiteDesiredConfig)).build();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getAllDesiredConfigVersions()).andReturn(testDesiredConfigs).atLeastOnce();
        replayAll();
        boolean topologyResolved = org.apache.ambari.server.topology.AmbariContextTest.context.isTopologyResolved(org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_ID);
        org.junit.Assert.assertFalse(topologyResolved);
    }

    @org.junit.Test
    public void testCreateAmbariResourcesNoVersions() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider vdfResourceProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.class);
        java.lang.Class<org.apache.ambari.server.topology.AmbariContext> clazz = org.apache.ambari.server.topology.AmbariContext.class;
        java.lang.reflect.Field f = clazz.getDeclaredField("versionDefinitionResourceProvider");
        f.setAccessible(true);
        f.set(null, vdfResourceProvider);
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(resource.getPropertyValue(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID)).andReturn(1L).atLeastOnce();
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.RequestStatus.class);
        EasyMock.expect(requestStatus.getAssociatedResources()).andReturn(java.util.Collections.singleton(resource)).atLeastOnce();
        EasyMock.expect(vdfResourceProvider.createResources(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class))).andReturn(requestStatus);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(repositoryVersion.getVersion()).andReturn("1.1.1.1").atLeastOnce();
        EasyMock.expect(repositoryVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).atLeastOnce();
        EasyMock.expect(repositoryVersionDAO.findByStack(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class))).andReturn(java.util.Collections.<org.apache.ambari.server.orm.entities.RepositoryVersionEntity>emptyList()).atLeastOnce();
        EasyMock.expect(repositoryVersionDAO.findByPK(org.easymock.EasyMock.anyLong())).andReturn(repositoryVersion);
        EasyMock.replay(repositoryVersionDAO, repositoryVersion, resource, requestStatus, vdfResourceProvider);
        org.apache.ambari.server.topology.AmbariContextTest.context.repositoryVersionDAO = repositoryVersionDAO;
        expectAmbariResourceCreation();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, null, null);
    }

    private void expectAmbariResourceCreation() {
        try {
            org.apache.ambari.server.topology.AmbariContextTest.controller.createCluster(EasyMock.capture(org.easymock.Capture.newInstance()));
            EasyMock.expectLastCall().once();
            EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.cluster.getServices()).andReturn(org.apache.ambari.server.topology.AmbariContextTest.clusterServices).anyTimes();
            org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider.createServices(EasyMock.capture(org.easymock.Capture.newInstance()));
            EasyMock.expectLastCall().once();
            org.apache.ambari.server.topology.AmbariContextTest.componentResourceProvider.createComponents(EasyMock.capture(org.easymock.Capture.newInstance()));
            EasyMock.expectLastCall().once();
            EasyMock.expect(org.apache.ambari.server.topology.AmbariContextTest.serviceResourceProvider.updateResources(EasyMock.capture(org.easymock.Capture.newInstance()), EasyMock.capture(org.easymock.Capture.newInstance()))).andReturn(null).atLeastOnce();
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    @org.junit.Test
    public void testCreateAmbariResourcesManyVersions() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion1.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(repositoryVersion1.getVersion()).andReturn("1.1.1.1").atLeastOnce();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion2.getId()).andReturn(2L).atLeastOnce();
        EasyMock.expect(repositoryVersion2.getVersion()).andReturn("1.1.2.2").atLeastOnce();
        EasyMock.expect(repositoryVersionDAO.findByStack(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class))).andReturn(java.util.Arrays.asList(repositoryVersion1, repositoryVersion2)).atLeastOnce();
        EasyMock.replay(repositoryVersionDAO, repositoryVersion1, repositoryVersion2);
        org.apache.ambari.server.topology.AmbariContextTest.context.repositoryVersionDAO = repositoryVersionDAO;
        replayAll();
        try {
            org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, null, null);
            org.junit.Assert.fail("Expected failure when several versions are found");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("Several repositories were found for testStack-testVersion:  1.1.1.1, 1.1.2.2.  Specify the version with 'repository_version'", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateAmbariResourcesBadVersion() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(repositoryVersionDAO.findByStackAndVersion(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyString())).andReturn(null).atLeastOnce();
        EasyMock.replay(repositoryVersionDAO);
        org.apache.ambari.server.topology.AmbariContextTest.context.repositoryVersionDAO = repositoryVersionDAO;
        replayAll();
        try {
            org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, "xyz", null);
            org.junit.Assert.fail("Expected failure when a bad version is provided");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("Could not identify repository version with stack testStack-testVersion and version xyz for installing services. Specify a valid version with 'repository_version'", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateAmbariResourcesVersionOK() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repositoryInClusterCreationRequest(1L, "3.0.1.2-345", org.apache.ambari.server.topology.AmbariContextTest.STACK_ID);
        expectAmbariResourceCreation();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, repoVersion.getVersion(), null);
    }

    @org.junit.Test
    public void testCreateAmbariResourcesVersionByIdOK() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repositoryInClusterCreationRequest(1L, "3.0.1.2-345", org.apache.ambari.server.topology.AmbariContextTest.STACK_ID);
        expectAmbariResourceCreation();
        replayAll();
        org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, null, repoVersion.getId());
    }

    @org.junit.Test
    public void testCreateAmbariResourcesVersionMismatch() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repositoryInClusterCreationRequest(1L, "3.0.1.2-345", new org.apache.ambari.server.state.StackId("HDP", "3.0"));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, () -> org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, repoVersion.getVersion(), null));
        org.junit.Assert.assertTrue(e.getMessage(), e.getMessage().contains("should match"));
    }

    @org.junit.Test
    public void testCreateAmbariResourcesVersionMismatchById() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repositoryInClusterCreationRequest(1L, "3.0.1.2-345", new org.apache.ambari.server.state.StackId("HDP", "3.0"));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, () -> org.apache.ambari.server.topology.AmbariContextTest.context.createAmbariResources(org.apache.ambari.server.topology.AmbariContextTest.topology, org.apache.ambari.server.topology.AmbariContextTest.CLUSTER_NAME, null, null, repoVersion.getId()));
        org.junit.Assert.assertTrue(e.getMessage(), e.getMessage().contains("should match"));
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryInClusterCreationRequest(java.lang.Long id, java.lang.String version, org.apache.ambari.server.state.StackId stackId) {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repoEntity.getStackId()).andStubReturn(stackId);
        EasyMock.expect(repoEntity.getId()).andStubReturn(id);
        EasyMock.expect(repoEntity.getType()).andStubReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        EasyMock.expect(repoEntity.getVersion()).andStubReturn(version);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(repositoryVersionDAO.findByPK(1L)).andStubReturn(repoEntity);
        EasyMock.expect(repositoryVersionDAO.findByStackAndVersion(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyString())).andStubReturn(repoEntity);
        EasyMock.replay(repositoryVersionDAO, repoEntity);
        org.apache.ambari.server.topology.AmbariContextTest.context.repositoryVersionDAO = repositoryVersionDAO;
        return repoEntity;
    }
}