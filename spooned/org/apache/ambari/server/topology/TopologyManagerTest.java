package org.apache.ambari.server.topology;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.powermock.api.easymock.PowerMock.mockStatic;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.topology.TopologyManager.class, org.apache.ambari.server.topology.AmbariContext.class })
public class TopologyManagerTest {
    private static final java.lang.String CLUSTER_NAME = "test-cluster";

    private static final long CLUSTER_ID = 1;

    private static final java.lang.String BLUEPRINT_NAME = "test-bp";

    private static final java.lang.String STACK_NAME = "test-stack";

    private static final java.lang.String STACK_VERSION = "test-stack-version";

    private static final java.lang.String SAMPLE_QUICKLINKS_PROFILE_1 = "{\"filters\":[{\"visible\":true}],\"services\":[]}";

    private static final java.lang.String SAMPLE_QUICKLINKS_PROFILE_2 = "{\"filters\":[],\"services\":[{\"name\":\"HDFS\",\"components\":[],\"filters\":[{\"visible\":true}]}]}";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.TopologyManager topologyManager = new org.apache.ambari.server.topology.TopologyManager();

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.TopologyManager topologyManagerReplay = new org.apache.ambari.server.topology.TopologyManager();

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.internal.ProvisionClusterRequest request;

    private final org.apache.ambari.server.topology.PersistedTopologyRequest persistedTopologyRequest = new org.apache.ambari.server.topology.PersistedTopologyRequest(1, request);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.LogicalRequestFactory logicalRequestFactory;

    @org.easymock.Mock(type = org.easymock.MockType.DEFAULT)
    private org.apache.ambari.server.topology.LogicalRequest logicalRequest;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.ConfigurationRequest configurationRequest;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.ConfigurationRequest configurationRequest2;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.ConfigurationRequest configurationRequest3;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.RequestStatusResponse requestStatusResponse;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private java.util.concurrent.ExecutorService executor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.PersistedState persistedState;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.HostGroup group1;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.HostGroup group2;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.Credential credential;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.controller.spi.ClusterController clusterController;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.orm.dao.SettingDAO settingDAO;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory configureClusterTaskFactory;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.tasks.ConfigureClusterTask configureClusterTask;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private java.util.concurrent.Future mockFuture;

    @org.easymock.Mock
    private org.apache.ambari.server.topology.validators.TopologyValidatorService topologyValidatorService;

    private final org.apache.ambari.server.topology.Configuration stackConfig = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());

    private final org.apache.ambari.server.topology.Configuration bpConfiguration = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), stackConfig);

    private final org.apache.ambari.server.topology.Configuration topoConfiguration = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), bpConfiguration);

    private final org.apache.ambari.server.topology.Configuration bpGroup1Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), bpConfiguration);

    private final org.apache.ambari.server.topology.Configuration bpGroup2Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), bpConfiguration);

    private final org.apache.ambari.server.topology.Configuration topoGroup1Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), bpGroup1Config);

    private final org.apache.ambari.server.topology.Configuration topoGroup2Config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>(), bpGroup2Config);

    private org.apache.ambari.server.topology.HostGroupInfo group1Info = new org.apache.ambari.server.topology.HostGroupInfo("group1");

    private org.apache.ambari.server.topology.HostGroupInfo group2Info = new org.apache.ambari.server.topology.HostGroupInfo("group2");

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupInfoMap = new java.util.HashMap<>();

    private java.util.Collection<org.apache.ambari.server.topology.Component> group1Components = java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("component1"), new org.apache.ambari.server.topology.Component("component2"), new org.apache.ambari.server.topology.Component("component3"));

    private java.util.Collection<org.apache.ambari.server.topology.Component> group2Components = java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("component3"), new org.apache.ambari.server.topology.Component("component4"));

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> group1ServiceComponents = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> group2ServiceComponents = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();

    private java.lang.String predicate = "Hosts/host_name=foo";

    private java.util.List<org.apache.ambari.server.topology.TopologyValidator> topologyValidators = new java.util.ArrayList<>();

    private org.easymock.Capture<org.apache.ambari.server.topology.ClusterTopology> clusterTopologyCapture;

    private org.easymock.Capture<java.util.Map<java.lang.String, java.lang.Object>> configRequestPropertiesCapture;

    private org.easymock.Capture<java.util.Map<java.lang.String, java.lang.Object>> configRequestPropertiesCapture2;

    private org.easymock.Capture<java.util.Map<java.lang.String, java.lang.Object>> configRequestPropertiesCapture3;

    private org.easymock.Capture<org.apache.ambari.server.controller.ClusterRequest> updateClusterConfigRequestCapture;

    private org.easymock.Capture<java.lang.Runnable> updateConfigTaskCapture;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        clusterTopologyCapture = EasyMock.newCapture();
        configRequestPropertiesCapture = EasyMock.newCapture();
        configRequestPropertiesCapture2 = EasyMock.newCapture();
        configRequestPropertiesCapture3 = EasyMock.newCapture();
        updateClusterConfigRequestCapture = EasyMock.newCapture();
        updateConfigTaskCapture = EasyMock.newCapture();
        topoConfiguration.setProperty("service1-site", "s1-prop", "s1-prop-value");
        topoConfiguration.setProperty("service2-site", "s2-prop", "s2-prop-value");
        topoConfiguration.setProperty("cluster-env", "g-prop", "g-prop-value");
        group1Info.addHost("host1");
        group1Info.setConfiguration(topoGroup1Config);
        group2Info.setRequestedCount(2);
        group2Info.setPredicate(predicate);
        group2Info.setConfiguration(topoGroup2Config);
        groupInfoMap.put("group1", group1Info);
        groupInfoMap.put("group2", group2Info);
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> groupMap = new java.util.HashMap<>();
        groupMap.put("group1", group1);
        groupMap.put("group2", group2);
        serviceComponents.put("service1", java.util.Arrays.asList("component1", "component3"));
        serviceComponents.put("service2", java.util.Arrays.asList("component2", "component4"));
        group1ServiceComponents.put("service1", java.util.Arrays.asList("component1", "component3"));
        group1ServiceComponents.put("service2", java.util.Collections.singleton("component2"));
        group2ServiceComponents.put("service2", java.util.Arrays.asList("component3", "component4"));
        EasyMock.expect(securityConfiguration.getType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).anyTimes();
        EasyMock.expect(credential.getType()).andReturn(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group1")).andReturn(group1).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group2")).andReturn(group2).anyTimes();
        EasyMock.expect(blueprint.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component3")).anyTimes();
        EasyMock.expect(blueprint.getComponents("service2")).andReturn(java.util.Arrays.asList("component2", "component4")).anyTimes();
        EasyMock.expect(blueprint.getConfiguration()).andReturn(bpConfiguration).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(groupMap).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Collections.singleton(group1)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Collections.singleton(group1)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component3")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component4")).andReturn(java.util.Collections.singleton(group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForService("service1")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForService("service2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getName()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.BLUEPRINT_NAME).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("service1", "service2")).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(blueprint.getRepositorySettings()).andReturn(new java.util.ArrayList<>()).anyTimes();
        EasyMock.expect(stack.getAllConfigurationTypes("service1")).andReturn(java.util.Arrays.asList("service1-site", "service1-env")).anyTimes();
        EasyMock.expect(stack.getAllConfigurationTypes("service2")).andReturn(java.util.Arrays.asList("service2-site", "service2-env")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component1")).andReturn(null).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component2")).andReturn(null).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component3")).andReturn(null).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component4")).andReturn(null).anyTimes();
        EasyMock.expect(stack.getCardinality("component1")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        EasyMock.expect(stack.getCardinality("component2")).andReturn(new org.apache.ambari.server.topology.Cardinality("1")).anyTimes();
        EasyMock.expect(stack.getCardinality("component3")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        EasyMock.expect(stack.getCardinality("component4")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+")).anyTimes();
        EasyMock.expect(stack.getComponents()).andReturn(serviceComponents).anyTimes();
        EasyMock.expect(stack.getComponents("service1")).andReturn(serviceComponents.get("service1")).anyTimes();
        EasyMock.expect(stack.getComponents("service2")).andReturn(serviceComponents.get("service2")).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("service1-site")).andReturn("service1").anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("service2-site")).andReturn("service2").anyTimes();
        EasyMock.expect(stack.getConfiguration()).andReturn(stackConfig).anyTimes();
        EasyMock.expect(stack.getName()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.STACK_VERSION).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes("service1")).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes("service2")).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(request.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(request.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(request.getClusterName()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME).anyTimes();
        EasyMock.expect(request.getDescription()).andReturn("Provision Cluster Test").anyTimes();
        EasyMock.expect(request.getConfiguration()).andReturn(topoConfiguration).anyTimes();
        EasyMock.expect(request.getHostGroupInfo()).andReturn(groupInfoMap).anyTimes();
        EasyMock.expect(request.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY).anyTimes();
        EasyMock.expect(request.getSecurityConfiguration()).andReturn(null).anyTimes();
        EasyMock.expect(group1.getBlueprintName()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.BLUEPRINT_NAME).anyTimes();
        EasyMock.expect(group1.getCardinality()).andReturn("test cardinality").anyTimes();
        EasyMock.expect(group1.containsMasterComponent()).andReturn(true).anyTimes();
        EasyMock.expect(group1.getComponents()).andReturn(group1Components).anyTimes();
        EasyMock.expect(group1.getComponents("service1")).andReturn(group1ServiceComponents.get("service1")).anyTimes();
        EasyMock.expect(group1.getComponents("service2")).andReturn(group1ServiceComponents.get("service1")).anyTimes();
        EasyMock.expect(group1.getConfiguration()).andReturn(topoGroup1Config).anyTimes();
        EasyMock.expect(group1.getName()).andReturn("group1").anyTimes();
        EasyMock.expect(group1.getServices()).andReturn(java.util.Arrays.asList("service1", "service2")).anyTimes();
        EasyMock.expect(group1.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(group2.getBlueprintName()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.BLUEPRINT_NAME).anyTimes();
        EasyMock.expect(group2.getCardinality()).andReturn("test cardinality").anyTimes();
        EasyMock.expect(group2.containsMasterComponent()).andReturn(false).anyTimes();
        EasyMock.expect(group2.getComponents()).andReturn(group2Components).anyTimes();
        EasyMock.expect(group2.getComponents("service1")).andReturn(group2ServiceComponents.get("service1")).anyTimes();
        EasyMock.expect(group2.getComponents("service2")).andReturn(group2ServiceComponents.get("service2")).anyTimes();
        EasyMock.expect(group2.getConfiguration()).andReturn(topoGroup2Config).anyTimes();
        EasyMock.expect(group2.getName()).andReturn("group2").anyTimes();
        EasyMock.expect(group2.getServices()).andReturn(java.util.Arrays.asList("service1", "service2")).anyTimes();
        EasyMock.expect(group2.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(logicalRequestFactory.createRequest(EasyMock.eq(1L), ((org.apache.ambari.server.topology.TopologyRequest) (EasyMock.anyObject())), EasyMock.capture(clusterTopologyCapture))).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(logicalRequest.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(logicalRequest.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(logicalRequest.getReservedHosts()).andReturn(java.util.Collections.singleton("host1")).anyTimes();
        EasyMock.expect(logicalRequest.getRequestStatus()).andReturn(requestStatusResponse).anyTimes();
        EasyMock.expect(ambariContext.getPersistedTopologyState()).andReturn(persistedState).anyTimes();
        ambariContext.createAmbariResources(EasyMock.isA(org.apache.ambari.server.topology.ClusterTopology.class), EasyMock.eq(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME), ((org.apache.ambari.server.state.SecurityType) (EasyMock.isNull())), ((java.lang.String) (EasyMock.isNull())), EasyMock.anyLong());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(ambariContext.getNextRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(ambariContext.isClusterKerberosEnabled(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(false).anyTimes();
        EasyMock.expect(ambariContext.getClusterId(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME)).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(ambariContext.getClusterName(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME).anyTimes();
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME)).andReturn(cluster).anyTimes();
        EasyMock.expect(ambariManagementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(ambariContext.createConfigurationRequests(EasyMock.capture(configRequestPropertiesCapture))).andReturn(java.util.Collections.singletonList(configurationRequest)).anyTimes();
        EasyMock.expect(ambariContext.createConfigurationRequests(EasyMock.capture(configRequestPropertiesCapture2))).andReturn(java.util.Collections.singletonList(configurationRequest2)).anyTimes();
        EasyMock.expect(ambariContext.createConfigurationRequests(EasyMock.capture(configRequestPropertiesCapture3))).andReturn(java.util.Collections.singletonList(configurationRequest3)).anyTimes();
        ambariContext.setConfigurationOnCluster(EasyMock.capture(updateClusterConfigRequestCapture));
        EasyMock.expectLastCall().anyTimes();
        ambariContext.persistInstallStateForUI(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME, org.apache.ambari.server.topology.TopologyManagerTest.STACK_NAME, org.apache.ambari.server.topology.TopologyManagerTest.STACK_VERSION);
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(clusterController.ensureResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class))).andReturn(resourceProvider);
        EasyMock.expect(resourceProvider.createResources(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class))).andReturn(new org.apache.ambari.server.controller.internal.RequestStatusImpl(null));
        EasyMock.expect(configureClusterTaskFactory.createConfigureClusterTask(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(configureClusterTask);
        EasyMock.expect(configureClusterTask.getTimeout()).andReturn(1000L);
        EasyMock.expect(configureClusterTask.getRepeatDelay()).andReturn(50L);
        EasyMock.expect(executor.submit(EasyMock.anyObject(org.apache.ambari.server.topology.AsyncCallableService.class))).andReturn(mockFuture).anyTimes();
        EasyMock.expect(persistedState.persistTopologyRequest(request)).andReturn(persistedTopologyRequest).anyTimes();
        persistedState.persistLogicalRequest(logicalRequest, 1);
        EasyMock.expectLastCall().anyTimes();
        java.lang.Class clazz = org.apache.ambari.server.topology.TopologyManager.class;
        java.lang.reflect.Field f = clazz.getDeclaredField("executor");
        f.setAccessible(true);
        f.set(topologyManager, executor);
        org.easymock.EasyMockSupport.injectMocks(topologyManager);
        java.lang.reflect.Field f2 = clazz.getDeclaredField("executor");
        f2.setAccessible(true);
        f2.set(topologyManagerReplay, executor);
        org.easymock.EasyMockSupport.injectMocks(topologyManagerReplay);
        clazz = org.apache.ambari.server.topology.AmbariContext.class;
        f = clazz.getDeclaredField("clusterController");
        f.setAccessible(true);
        f.set(ambariContext, clusterController);
        org.easymock.EasyMockSupport.injectMocks(ambariContext);
        java.lang.reflect.Field controllerField = org.apache.ambari.server.topology.AmbariContext.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(controllerField, controllerField.getModifiers() & (~java.lang.reflect.Modifier.FINAL));
        controllerField.set(null, ambariManagementController);
    }

    @org.junit.After
    public void tearDown() {
        org.powermock.api.easymock.PowerMock.verify(java.lang.System.class);
        EasyMock.verify(blueprint, stack, request, group1, group2, ambariContext, logicalRequestFactory, logicalRequest, configurationRequest, configurationRequest2, configurationRequest3, requestStatusResponse, executor, persistedState, clusterTopologyMock, mockFuture, settingDAO, eventPublisher, securityConfiguration, credential, ambariManagementController, clusters, cluster);
        org.powermock.api.easymock.PowerMock.reset(java.lang.System.class);
        EasyMock.reset(blueprint, stack, request, group1, group2, ambariContext, logicalRequestFactory, logicalRequest, configurationRequest, configurationRequest2, configurationRequest3, requestStatusResponse, executor, persistedState, clusterTopologyMock, mockFuture, settingDAO, eventPublisher, securityConfiguration, credential, ambariManagementController, clusters, cluster);
    }

    @org.junit.Test
    public void testProvisionCluster() throws java.lang.Exception {
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testBlueprintProvisioningStateEvent() throws java.lang.Exception {
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        eventPublisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.ClusterProvisionStartedEvent.class));
        EasyMock.expectLastCall().once();
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testAddKerberosClientAtTopologyInit() throws java.lang.Exception {
        java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> allRequests = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.topology.LogicalRequest> requestList = new java.util.ArrayList<>();
        requestList.add(logicalRequest);
        EasyMock.expect(logicalRequest.hasPendingHostRequests()).andReturn(false).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(false).anyTimes();
        allRequests.put(clusterTopologyMock, requestList);
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(clusterTopologyMock.isClusterKerberosEnabled()).andReturn(true);
        EasyMock.expect(clusterTopologyMock.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(allRequests).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(ambariContext.isTopologyResolved(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(true).anyTimes();
        EasyMock.expect(group1.addComponent("KERBEROS_CLIENT")).andReturn(true).anyTimes();
        EasyMock.expect(group2.addComponent("KERBEROS_CLIENT")).andReturn(true).anyTimes();
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testDoNotAddKerberosClientAtTopologyInit_KdcTypeNone() throws java.lang.Exception {
        java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> allRequests = java.util.Collections.singletonMap(clusterTopologyMock, java.util.Collections.singletonList(logicalRequest));
        EasyMock.expect(logicalRequest.hasPendingHostRequests()).andReturn(false).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(false).anyTimes();
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(clusterTopologyMock.isClusterKerberosEnabled()).andReturn(true);
        EasyMock.expect(clusterTopologyMock.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(allRequests).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(ambariContext.isTopologyResolved(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.getSecurity()).andReturn(securityConfiguration).anyTimes();
        EasyMock.expect(securityConfiguration.getDescriptor()).andReturn(java.util.Optional.empty());
        EasyMock.expect(request.getCredentialsMap()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.topology.TopologyManager.KDC_ADMIN_CREDENTIAL, credential));
        EasyMock.expect(credential.getAlias()).andReturn("").anyTimes();
        EasyMock.expect(credential.getKey()).andReturn("").anyTimes();
        EasyMock.expect(credential.getPrincipal()).andReturn("").anyTimes();
        bpConfiguration.setProperty(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "none");
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testDoNotAddKerberosClientAtTopologyInit_ManageIdentity() throws java.lang.Exception {
        java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> allRequests = java.util.Collections.singletonMap(clusterTopologyMock, java.util.Collections.singletonList(logicalRequest));
        EasyMock.expect(logicalRequest.hasPendingHostRequests()).andReturn(false).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(false).anyTimes();
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(clusterTopologyMock.isClusterKerberosEnabled()).andReturn(true);
        EasyMock.expect(clusterTopologyMock.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(clusterTopologyMock.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(allRequests).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(ambariContext.isTopologyResolved(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.getSecurity()).andReturn(securityConfiguration).anyTimes();
        EasyMock.expect(securityConfiguration.getDescriptor()).andReturn(java.util.Optional.empty());
        EasyMock.expect(request.getCredentialsMap()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.topology.TopologyManager.KDC_ADMIN_CREDENTIAL, credential));
        EasyMock.expect(credential.getAlias()).andReturn("").anyTimes();
        EasyMock.expect(credential.getKey()).andReturn("").anyTimes();
        EasyMock.expect(credential.getPrincipal()).andReturn("").anyTimes();
        bpConfiguration.setProperty(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.MANAGE_IDENTITIES, "false");
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testBlueprintRequestCompletion() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.ShortTaskStatus t1 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t1);
        org.apache.ambari.server.controller.ShortTaskStatus t2 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t2);
        org.apache.ambari.server.controller.ShortTaskStatus t3 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t3);
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(tasks).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(true).anyTimes();
        EasyMock.expect(logicalRequest.isSuccessful()).andReturn(true).anyTimes();
        eventPublisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.ClusterProvisionedEvent.class));
        EasyMock.expectLastCall().once();
        replayAll();
        topologyManager.provisionCluster(request);
        requestFinished();
        org.junit.Assert.assertTrue(topologyManager.isClusterProvisionWithBlueprintFinished(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID));
    }

    @org.junit.Test
    public void testBlueprintRequestCompletion__Failure() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.ShortTaskStatus t1 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        tasks.add(t1);
        org.apache.ambari.server.controller.ShortTaskStatus t2 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t2);
        org.apache.ambari.server.controller.ShortTaskStatus t3 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t3);
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(tasks).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(true).anyTimes();
        EasyMock.expect(logicalRequest.isSuccessful()).andReturn(false).anyTimes();
        replayAll();
        topologyManager.provisionCluster(request);
        requestFinished();
        org.junit.Assert.assertTrue(topologyManager.isClusterProvisionWithBlueprintFinished(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID));
    }

    @org.junit.Test
    public void testBlueprintRequestCompletion__InProgress() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.ShortTaskStatus t1 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        tasks.add(t1);
        org.apache.ambari.server.controller.ShortTaskStatus t2 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t2);
        org.apache.ambari.server.controller.ShortTaskStatus t3 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t3);
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(tasks).anyTimes();
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(false).anyTimes();
        replayAll();
        topologyManager.provisionCluster(request);
        requestFinished();
        org.junit.Assert.assertFalse(topologyManager.isClusterProvisionWithBlueprintFinished(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID));
    }

    @org.junit.Test
    public void testBlueprintRequestCompletion__NoRequest() throws java.lang.Exception {
        org.apache.ambari.server.topology.TopologyManager tm = new org.apache.ambari.server.topology.TopologyManager();
        tm.onRequestFinished(new org.apache.ambari.server.events.RequestFinishedEvent(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID, 1));
        org.junit.Assert.assertFalse(tm.isClusterProvisionWithBlueprintTracked(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID));
        replayAll();
    }

    @org.junit.Test
    public void testBlueprintRequestCompletion__Replay() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.ShortTaskStatus t1 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t1);
        org.apache.ambari.server.controller.ShortTaskStatus t2 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t2);
        org.apache.ambari.server.controller.ShortTaskStatus t3 = new org.apache.ambari.server.controller.ShortTaskStatus();
        t3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        tasks.add(t3);
        java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> allRequests = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.topology.LogicalRequest> logicalRequests = new java.util.ArrayList<>();
        logicalRequests.add(logicalRequest);
        org.apache.ambari.server.topology.ClusterTopology clusterTopologyMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);
        EasyMock.expect(clusterTopologyMock.getClusterId()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID).anyTimes();
        EasyMock.expect(ambariContext.isTopologyResolved(org.easymock.EasyMock.anyLong())).andReturn(true).anyTimes();
        allRequests.put(clusterTopologyMock, logicalRequests);
        EasyMock.expect(persistedState.getAllRequests()).andReturn(allRequests).anyTimes();
        EasyMock.expect(persistedState.getProvisionRequest(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID)).andReturn(logicalRequest).anyTimes();
        EasyMock.expect(logicalRequest.hasPendingHostRequests()).andReturn(true).anyTimes();
        EasyMock.expect(logicalRequest.getCompletedHostRequests()).andReturn(java.util.Collections.EMPTY_LIST).anyTimes();
        EasyMock.expect(logicalRequest.isFinished()).andReturn(true).anyTimes();
        EasyMock.expect(requestStatusResponse.getTasks()).andReturn(tasks).anyTimes();
        replayAll();
        org.easymock.EasyMock.replay(clusterTopologyMock);
        topologyManagerReplay.getRequest(1L);
        org.junit.Assert.assertTrue(topologyManagerReplay.isClusterProvisionWithBlueprintFinished(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID));
    }

    private void requestFinished() {
        topologyManager.onRequestFinished(new org.apache.ambari.server.events.RequestFinishedEvent(org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_ID, 1));
    }

    private void replayAll() {
        EasyMock.replay(blueprint, stack, request, group1, group2, ambariContext, logicalRequestFactory, configurationRequest, configurationRequest2, configurationRequest3, executor, persistedState, clusterTopologyMock, securityConfigurationFactory, credentialStoreService, clusterController, resourceProvider, mockFuture, requestStatusResponse, logicalRequest, settingDAO, configureClusterTaskFactory, configureClusterTask, eventPublisher, securityConfiguration, credential, ambariManagementController, clusters, cluster);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testScaleHosts__alreadyExistingHost() throws org.apache.ambari.server.topology.InvalidTopologyTemplateException, org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.AmbariException, org.apache.ambari.server.stack.NoSuchStackException {
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.TreeMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, "host1");
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, "group1");
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.topology.TopologyManagerTest.CLUSTER_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, org.apache.ambari.server.topology.TopologyManagerTest.BLUEPRINT_NAME);
        propertySet.add(properties);
        org.apache.ambari.server.topology.BlueprintFactory bpfMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.topology.BlueprintFactory.class);
        org.easymock.EasyMock.expect(bpfMock.getBlueprint(org.apache.ambari.server.topology.TopologyManagerTest.BLUEPRINT_NAME)).andReturn(blueprint).anyTimes();
        org.apache.ambari.server.controller.internal.ScaleClusterRequest.init(bpfMock);
        EasyMock.replay(bpfMock);
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        replayAll();
        topologyManager.provisionCluster(request);
        topologyManager.scaleHosts(new org.apache.ambari.server.controller.internal.ScaleClusterRequest(propertySet));
        org.junit.Assert.fail("InvalidTopologyException should have been thrown");
    }

    @org.junit.Test
    public void testProvisionCluster_QuickLinkProfileIsSavedTheFirstTime() throws java.lang.Exception {
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(request.getQuickLinksProfileJson()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.SAMPLE_QUICKLINKS_PROFILE_1).anyTimes();
        EasyMock.expect(settingDAO.findByName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE)).andReturn(null);
        final long timeStamp = java.lang.System.currentTimeMillis();
        mockStatic(java.lang.System.class);
        EasyMock.expect(java.lang.System.currentTimeMillis()).andReturn(timeStamp);
        org.powermock.api.easymock.PowerMock.replay(java.lang.System.class);
        final org.apache.ambari.server.orm.entities.SettingEntity quickLinksProfile = createQuickLinksSettingEntity(org.apache.ambari.server.topology.TopologyManagerTest.SAMPLE_QUICKLINKS_PROFILE_1, timeStamp);
        settingDAO.create(EasyMock.eq(quickLinksProfile));
        replayAll();
        topologyManager.provisionCluster(request);
    }

    @org.junit.Test
    public void testProvisionCluster_ExistingQuickLinkProfileIsOverwritten() throws java.lang.Exception {
        EasyMock.expect(persistedState.getAllRequests()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(request.getQuickLinksProfileJson()).andReturn(org.apache.ambari.server.topology.TopologyManagerTest.SAMPLE_QUICKLINKS_PROFILE_2).anyTimes();
        final long timeStamp1 = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.orm.entities.SettingEntity originalProfile = createQuickLinksSettingEntity(org.apache.ambari.server.topology.TopologyManagerTest.SAMPLE_QUICKLINKS_PROFILE_1, timeStamp1);
        EasyMock.expect(settingDAO.findByName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE)).andReturn(originalProfile);
        mockStatic(java.lang.System.class);
        final long timeStamp2 = timeStamp1 + 100;
        EasyMock.expect(java.lang.System.currentTimeMillis()).andReturn(timeStamp2);
        org.powermock.api.easymock.PowerMock.replay(java.lang.System.class);
        final org.apache.ambari.server.orm.entities.SettingEntity newProfile = createQuickLinksSettingEntity(org.apache.ambari.server.topology.TopologyManagerTest.SAMPLE_QUICKLINKS_PROFILE_2, timeStamp2);
        EasyMock.expect(settingDAO.merge(newProfile)).andReturn(newProfile);
        replayAll();
        topologyManager.provisionCluster(request);
    }

    private org.apache.ambari.server.orm.entities.SettingEntity createQuickLinksSettingEntity(java.lang.String content, long timeStamp) {
        org.apache.ambari.server.orm.entities.SettingEntity settingEntity = new org.apache.ambari.server.orm.entities.SettingEntity();
        settingEntity.setName(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_NAME_QUICKLINKS_PROFILE);
        settingEntity.setSettingType(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.SETTING_TYPE_AMBARI_SERVER);
        settingEntity.setContent(content);
        settingEntity.setUpdatedBy(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
        settingEntity.setUpdateTimestamp(timeStamp);
        return settingEntity;
    }
}