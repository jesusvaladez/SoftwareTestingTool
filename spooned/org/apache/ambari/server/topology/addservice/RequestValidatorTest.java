package org.apache.ambari.server.topology.addservice;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class RequestValidatorTest extends org.easymock.EasyMockSupport {
    private static final java.util.Map<java.lang.String, ?> FAKE_DESCRIPTOR = com.google.common.collect.ImmutableMap.of("kerberos", "descriptor");

    private static final java.lang.String FAKE_DESCRIPTOR_REFERENCE = "ref";

    private final org.apache.ambari.server.topology.addservice.AddServiceRequest request = createNiceMock(org.apache.ambari.server.topology.addservice.AddServiceRequest.class);

    private final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);

    private final org.apache.ambari.server.controller.AmbariManagementController controller = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);

    private final org.apache.ambari.server.state.ConfigHelper configHelper = createMock(org.apache.ambari.server.state.ConfigHelper.class);

    private final org.apache.ambari.server.topology.StackFactory stackFactory = createNiceMock(org.apache.ambari.server.topology.StackFactory.class);

    private final org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = createNiceMock(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);

    private final org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory = createStrictMock(org.apache.ambari.server.topology.SecurityConfigurationFactory.class);

    private final org.apache.ambari.server.topology.addservice.RequestValidator validator = new org.apache.ambari.server.topology.addservice.RequestValidator(request, cluster, controller, configHelper, stackFactory, kerberosDescriptorFactory, securityConfigurationFactory);

    @org.junit.Before
    public void setUp() {
        try {
            validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL);
            EasyMock.expect(cluster.getClusterName()).andReturn("TEST").anyTimes();
            EasyMock.expect(cluster.getServices()).andStubReturn(com.google.common.collect.ImmutableMap.of());
            EasyMock.expect(cluster.getSecurityType()).andStubReturn(org.apache.ambari.server.state.SecurityType.NONE);
            EasyMock.expect(configHelper.calculateExistingConfigs(cluster)).andStubReturn(org.apache.ambari.server.topology.Configuration.newEmpty().asPair());
            EasyMock.expect(request.getServices()).andStubReturn(com.google.common.collect.ImmutableSet.of());
            EasyMock.expect(request.getComponents()).andStubReturn(com.google.common.collect.ImmutableSet.of());
            EasyMock.expect(request.getSecurity()).andStubReturn(java.util.Optional.empty());
            EasyMock.expect(request.getValidationType()).andStubReturn(org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.DEFAULT);
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    @org.junit.After
    public void tearDown() {
        resetAll();
    }

    @org.junit.Test
    public void cannotConstructInvalidRequestInfo() {
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        org.apache.ambari.server.controller.internal.Stack stack = simpleMockStack();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = org.apache.ambari.server.topology.addservice.RequestValidatorTest.someNewServices();
        org.apache.ambari.server.topology.Configuration config = org.apache.ambari.server.topology.Configuration.newEmpty();
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(stack));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        validator.setState(validator.getState().with(config));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.withNewServices(newServices));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        validator.setState(validator.getState().with(stack));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(config));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
        validator.setState(validator.getState().withNewServices(newServices));
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(null, null));
    }

    @org.junit.Test
    public void canConstructValidRequestInfo() {
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.withNewServices(org.apache.ambari.server.topology.addservice.RequestValidatorTest.someNewServices()).with(simpleMockStack()).with(org.apache.ambari.server.topology.Configuration.newEmpty()));
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        replayAll();
        org.apache.ambari.server.topology.addservice.AddServiceInfo addServiceInfo = validator.createValidServiceInfo(actionManager, requestFactory);
        org.junit.Assert.assertNotNull(addServiceInfo);
        org.junit.Assert.assertSame(request, addServiceInfo.getRequest());
        org.junit.Assert.assertEquals(cluster.getClusterName(), addServiceInfo.clusterName());
        org.junit.Assert.assertSame(validator.getState().getConfig(), addServiceInfo.getConfig());
        org.junit.Assert.assertSame(validator.getState().getStack(), addServiceInfo.getStack());
        org.junit.Assert.assertEquals(validator.getState().getNewServices(), addServiceInfo.newServices());
    }

    @org.junit.Test
    public void cannotConstructTwice() {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        replayAll();
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.withNewServices(org.apache.ambari.server.topology.addservice.RequestValidatorTest.someNewServices()).with(simpleMockStack()).with(org.apache.ambari.server.topology.Configuration.newEmpty()));
        validator.createValidServiceInfo(actionManager, requestFactory);
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, () -> validator.createValidServiceInfo(actionManager, requestFactory));
    }

    @org.junit.Test
    public void reportsUnknownStackFromRequest() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId requestStackId = new org.apache.ambari.server.state.StackId("HDP", "123");
        EasyMock.expect(request.getStackId()).andReturn(java.util.Optional.of(requestStackId)).anyTimes();
        EasyMock.expect(stackFactory.createStack(requestStackId.getStackName(), requestStackId.getStackVersion(), controller)).andThrow(new org.apache.ambari.server.AmbariException("Stack not found"));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateStack);
        org.junit.Assert.assertTrue(e.getMessage().contains(requestStackId.toString()));
        org.junit.Assert.assertNull(validator.getState().getStack());
    }

    @org.junit.Test
    public void reportsUnknownStackFromCluster() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId clusterStackId = new org.apache.ambari.server.state.StackId("CLUSTER", "555");
        EasyMock.expect(request.getStackId()).andReturn(java.util.Optional.empty()).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(clusterStackId);
        EasyMock.expect(stackFactory.createStack(clusterStackId.getStackName(), clusterStackId.getStackVersion(), controller)).andThrow(new org.apache.ambari.server.AmbariException("Stack not found"));
        replayAll();
        java.lang.IllegalStateException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalStateException.class, validator::validateStack);
        org.junit.Assert.assertTrue(e.getMessage().contains(clusterStackId.toString()));
        org.junit.Assert.assertNull(validator.getState().getStack());
    }

    @org.junit.Test
    public void useClusterStackIfAbsentInRequest() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId clusterStackId = new org.apache.ambari.server.state.StackId("CLUSTER", "123");
        org.apache.ambari.server.controller.internal.Stack expectedStack = createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);
        EasyMock.expect(request.getStackId()).andReturn(java.util.Optional.empty()).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(clusterStackId);
        EasyMock.expect(stackFactory.createStack(clusterStackId.getStackName(), clusterStackId.getStackVersion(), controller)).andReturn(expectedStack);
        replayAll();
        validator.validateStack();
        org.junit.Assert.assertSame(expectedStack, validator.getState().getStack());
    }

    @org.junit.Test
    public void acceptsKnownServices() {
        java.lang.String newService = "KAFKA";
        requestServices(false, newService);
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        validator.validateServicesAndComponents();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> expectedNewServices = com.google.common.collect.ImmutableMap.of(newService, com.google.common.collect.ImmutableMap.of());
        org.junit.Assert.assertEquals(expectedNewServices, validator.getState().getNewServices());
    }

    @org.junit.Test
    public void acceptsKnownComponents() {
        requestComponents("KAFKA_BROKER");
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        validator.validateServicesAndComponents();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> expectedNewServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401.ambari.apache.org")));
        org.junit.Assert.assertEquals(expectedNewServices, validator.getState().getNewServices());
    }

    @org.junit.Test
    public void handlesMultipleComponentInstances() {
        EasyMock.expect(request.getComponents()).andReturn(java.util.stream.Stream.of("c7401", "c7402").map(hostname -> org.apache.ambari.server.topology.addservice.Component.of("KAFKA_BROKER", hostname)).collect(java.util.stream.Collectors.toSet()));
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        validator.validateServicesAndComponents();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> expectedNewServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401", "c7402")));
        org.junit.Assert.assertEquals(expectedNewServices, validator.getState().getNewServices());
    }

    @org.junit.Test
    public void rejectsMultipleOccurrencesOfSameHostForSameComponent() {
        java.util.Set<java.lang.String> duplicateHosts = com.google.common.collect.ImmutableSet.of("c7402", "c7403");
        java.util.Set<java.lang.String> uniqueHosts = com.google.common.collect.ImmutableSet.of("c7401", "c7404");
        EasyMock.expect(request.getComponents()).andReturn(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Component.of("KAFKA_BROKER", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, "c7401", "c7402", "c7403"), org.apache.ambari.server.topology.addservice.Component.of("KAFKA_BROKER", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY, "c7402", "c7403", "c7404")));
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertTrue(e.getMessage().contains("hosts appear multiple"));
        duplicateHosts.forEach(host -> org.junit.Assert.assertTrue(e.getMessage().contains(host)));
        uniqueHosts.forEach(host -> org.junit.Assert.assertFalse(e.getMessage().contains(host)));
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    @org.junit.Test
    public void rejectsUnknownService() {
        java.lang.String serviceName = "UNKNOWN_SERVICE";
        requestServices(false, serviceName);
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertTrue(e.getMessage().contains(serviceName));
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    @org.junit.Test
    public void rejectsUnknownComponent() {
        java.lang.String componentName = "UNKNOWN_COMPONENT";
        requestComponents(componentName);
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertTrue(e.getMessage().contains(componentName));
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    @org.junit.Test
    public void rejectsExistingServiceForService() {
        java.lang.String serviceName = "KAFKA";
        requestServices(false, serviceName);
        clusterAlreadyHasServices(serviceName);
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertTrue(e.getMessage().contains(serviceName));
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    private void clusterAlreadyHasServices(java.lang.String serviceName) {
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.of(serviceName, createNiceMock(org.apache.ambari.server.state.Service.class))).anyTimes();
    }

    @org.junit.Test
    public void rejectsExistingServiceForComponent() {
        java.lang.String serviceName = "KAFKA";
        java.lang.String componentName = "KAFKA_BROKER";
        clusterAlreadyHasServices(serviceName);
        requestComponents(componentName);
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertTrue(e.getMessage().contains(serviceName));
        org.junit.Assert.assertTrue(e.getMessage().contains(componentName));
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    @org.junit.Test
    public void rejectsEmptyServiceAndComponentList() {
        replayAll();
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateServicesAndComponents);
        org.junit.Assert.assertNull(validator.getState().getNewServices());
    }

    @org.junit.Test
    public void acceptsKnownHosts() {
        java.util.Set<java.lang.String> requestHosts = com.google.common.collect.ImmutableSet.of("c7401.ambari.apache.org", "c7402.ambari.apache.org");
        java.util.Set<java.lang.String> otherHosts = com.google.common.collect.ImmutableSet.of("c7403.ambari.apache.org", "c7404.ambari.apache.org");
        java.util.Set<java.lang.String> clusterHosts = com.google.common.collect.Sets.union(requestHosts, otherHosts);
        EasyMock.expect(cluster.getHostNames()).andReturn(clusterHosts).anyTimes();
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.withNewServices(com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", requestHosts))));
        replayAll();
        validator.validateHosts();
    }

    @org.junit.Test
    public void rejectsUnknownHosts() {
        java.util.Set<java.lang.String> clusterHosts = com.google.common.collect.ImmutableSet.of("c7401.ambari.apache.org", "c7402.ambari.apache.org");
        java.util.Set<java.lang.String> otherHosts = com.google.common.collect.ImmutableSet.of("c7403.ambari.apache.org", "c7404.ambari.apache.org");
        java.util.Set<java.lang.String> requestHosts = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Sets.union(clusterHosts, otherHosts));
        EasyMock.expect(cluster.getHostNames()).andReturn(clusterHosts).anyTimes();
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.withNewServices(com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", requestHosts))));
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateHosts);
        org.junit.Assert.assertTrue(e.getMessage(), e.getMessage().contains("host"));
    }

    @org.junit.Test
    public void acceptsAbsentSecurityWhenClusterHasKerberos() {
        secureCluster();
        replayAll();
        validator.validateSecurity();
    }

    @org.junit.Test
    public void acceptsAbsentSecurityWhenClusterHasNone() {
        replayAll();
        validator.validateSecurity();
    }

    @org.junit.Test
    public void acceptsMatchingKerberosSecurity() {
        secureCluster();
        requestSpecifiesSecurity();
        replayAll();
        validator.validateSecurity();
    }

    @org.junit.Test
    public void acceptsMatchingNoneSecurity() {
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(org.apache.ambari.server.topology.SecurityConfiguration.NONE)).anyTimes();
        replayAll();
        validator.validateSecurity();
    }

    @org.junit.Test
    public void rejectsNoneSecurityWhenClusterHasKerberos() {
        testBothValidationTypes(validator::validateSecurity, "KERBEROS", () -> {
            secureCluster();
            EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(org.apache.ambari.server.topology.SecurityConfiguration.NONE)).anyTimes();
        });
    }

    @org.junit.Test
    public void rejectsKerberosSecurityWhenClusterHasNone() {
        testBothValidationTypes(validator::validateSecurity, "KERBEROS", this::requestSpecifiesSecurity);
    }

    @org.junit.Test
    public void rejectsKerberosDescriptorForNoSecurity() {
        org.apache.ambari.server.topology.SecurityConfiguration requestSecurity = org.apache.ambari.server.topology.SecurityConfiguration.forTest(org.apache.ambari.server.state.SecurityType.NONE, null, com.google.common.collect.ImmutableMap.of("kerberos", "descriptor"));
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(requestSecurity)).anyTimes();
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateSecurity);
        org.junit.Assert.assertTrue(e.getMessage().contains("Kerberos descriptor"));
    }

    @org.junit.Test
    public void rejectsKerberosDescriptorReferenceForNoSecurity() {
        org.apache.ambari.server.topology.SecurityConfiguration requestSecurity = org.apache.ambari.server.topology.SecurityConfiguration.forTest(org.apache.ambari.server.state.SecurityType.NONE, "ref", null);
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(requestSecurity)).anyTimes();
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateSecurity);
        org.junit.Assert.assertTrue(e.getMessage().contains("Kerberos descriptor reference"));
    }

    @org.junit.Test
    public void rejectsRequestWithBothKerberosDescriptorAndReference() {
        secureCluster();
        org.apache.ambari.server.topology.SecurityConfiguration invalidConfig = org.apache.ambari.server.topology.SecurityConfiguration.forTest(org.apache.ambari.server.state.SecurityType.KERBEROS, "ref", com.google.common.collect.ImmutableMap.of());
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(invalidConfig)).anyTimes();
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateSecurity);
        org.junit.Assert.assertTrue(e.getMessage().contains("Kerberos descriptor and reference"));
    }

    @org.junit.Test
    public void loadsKerberosDescriptorByReference() {
        java.lang.String newService = "KAFKA";
        secureCluster();
        requestServices(true, newService);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = requestHasKerberosDescriptorFor(true, newService);
        replayAll();
        validator.validateSecurity();
        org.junit.Assert.assertEquals(kerberosDescriptor, validator.getState().getKerberosDescriptor());
        verifyAll();
    }

    @org.junit.Test
    public void reportsDanglingKerberosDescriptorReference() {
        java.lang.String newService = "KAFKA";
        secureCluster();
        requestServices(true, newService);
        org.apache.ambari.server.topology.SecurityConfiguration requestSecurity = org.apache.ambari.server.topology.SecurityConfiguration.withReference(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE);
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(requestSecurity)).anyTimes();
        EasyMock.expect(securityConfigurationFactory.loadSecurityConfigurationByReference(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE)).andThrow(new java.lang.IllegalArgumentException("No security configuration found for the reference: " + org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE));
        replayAll();
        org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateSecurity);
        verifyAll();
    }

    @org.junit.Test
    public void acceptsDescriptorWithOnlyNewServices() {
        java.lang.String newService = "KAFKA";
        secureCluster();
        requestServices(true, newService);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = requestHasKerberosDescriptorFor(false, newService);
        replayAll();
        validator.validateSecurity();
        org.junit.Assert.assertEquals(kerberosDescriptor, validator.getState().getKerberosDescriptor());
    }

    @org.junit.Test
    public void acceptsDescriptorWithAdditionalServices() {
        java.lang.String newService = "KAFKA";
        java.lang.String otherService = "ZOOKEEPER";
        secureCluster();
        requestServices(true, newService);
        requestHasKerberosDescriptorFor(false, newService, otherService);
        replayAll();
        java.lang.IllegalArgumentException e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validator::validateSecurity);
        org.junit.Assert.assertTrue(e.getMessage().contains("only for new services"));
    }

    @org.junit.Test
    public void acceptsDescriptorWithoutServices() {
        secureCluster();
        requestServices(true, "KAFKA");
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = requestHasKerberosDescriptorFor(false);
        replayAll();
        validator.validateSecurity();
        org.junit.Assert.assertEquals(kerberosDescriptor, validator.getState().getKerberosDescriptor());
    }

    @org.junit.Test
    public void combinesRequestConfigWithClusterAndStack() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.topology.Configuration requestConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        requestConfig.setProperty("kafka-broker", "zookeeper.connect", "zookeeper.connect:request");
        requestConfig.setProperty("kafka-env", "custom_property", "custom_property:request");
        EasyMock.expect(request.getConfiguration()).andReturn(requestConfig.copy()).anyTimes();
        EasyMock.expect(request.getRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY).anyTimes();
        org.apache.ambari.server.topology.Configuration clusterConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        clusterConfig.setProperty("zookeeper-env", "zk_user", "zk_user:cluster_level");
        EasyMock.expect(configHelper.calculateExistingConfigs(cluster)).andReturn(clusterConfig.asPair()).anyTimes();
        org.apache.ambari.server.controller.internal.Stack stack = simpleMockStack();
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        stackConfig.setProperty("zookeeper-env", "zk_user", "zk_user:stack_default");
        stackConfig.setProperty("zookeeper-env", "zk_log_dir", "zk_log_dir:stack_default");
        stackConfig.setProperty("kafka-broker", "zookeeper.connect", "zookeeper.connect:stack_default");
        EasyMock.expect(stack.getDefaultConfig()).andReturn(stackConfig).anyTimes();
        replayAll();
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(stack));
        validator.validateConfiguration();
        org.apache.ambari.server.topology.Configuration config = validator.getState().getConfig();
        org.apache.ambari.server.topology.addservice.RequestValidatorTest.verifyConfigOverrides(requestConfig, clusterConfig, stackConfig, config);
    }

    @org.junit.Test
    public void rejectsKerberosEnvChange() {
        testBothValidationTypes(validator::validateConfiguration, () -> {
            org.apache.ambari.server.topology.Configuration requestConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
            requestConfig.setProperty("kerberos-env", "some-property", "some-value");
            EasyMock.expect(request.getConfiguration()).andReturn(requestConfig.copy()).anyTimes();
            validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        });
    }

    @org.junit.Test
    public void rejectsKrb5ConfChange() {
        testBothValidationTypes(validator::validateConfiguration, () -> {
            org.apache.ambari.server.topology.Configuration requestConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
            requestConfig.setProperty("krb5-conf", "some-property", "some-value");
            EasyMock.expect(request.getConfiguration()).andReturn(requestConfig.copy()).anyTimes();
            validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()));
        });
    }

    private void testBothValidationTypes(java.lang.Runnable validation, java.lang.Runnable testCaseSetup) {
        testBothValidationTypes(validation, null, testCaseSetup);
    }

    private void testBothValidationTypes(java.lang.Runnable validation, java.lang.String expectedMessage, java.lang.Runnable testCaseSetup) {
        testCaseSetup.run();
        replayAll();
        java.lang.Exception e = org.apache.ambari.server.utils.Assertions.assertThrows(java.lang.IllegalArgumentException.class, validation);
        if (expectedMessage != null) {
            org.junit.Assert.assertTrue(e.getMessage().contains(expectedMessage));
        }
        resetAll();
        setUp();
        permissiveValidation();
        testCaseSetup.run();
        replayAll();
        validation.run();
    }

    private static void verifyConfigOverrides(org.apache.ambari.server.topology.Configuration requestConfig, org.apache.ambari.server.topology.Configuration clusterConfig, org.apache.ambari.server.topology.Configuration stackConfig, org.apache.ambari.server.topology.Configuration actualConfig) {
        requestConfig.getProperties().forEach((type, properties) -> properties.forEach((propertyName, propertyValue) -> org.junit.Assert.assertEquals((type + "/") + propertyName, propertyValue, actualConfig.getPropertyValue(type, propertyName))));
        clusterConfig.getProperties().forEach((type, properties) -> properties.forEach((propertyName, propertyValue) -> {
            if (!requestConfig.isPropertySet(type, propertyName)) {
                org.junit.Assert.assertEquals((type + "/") + propertyName, propertyValue, actualConfig.getPropertyValue(type, propertyName));
            }
        }));
        stackConfig.getProperties().forEach((type, properties) -> properties.forEach((propertyName, propertyValue) -> {
            if ((!requestConfig.isPropertySet(type, propertyName)) && (!clusterConfig.isPropertySet(type, propertyName))) {
                org.junit.Assert.assertEquals((type + "/") + propertyName, propertyValue, actualConfig.getPropertyValue(type, propertyName));
            }
        }));
    }

    private org.apache.ambari.server.controller.internal.Stack simpleMockStack() {
        org.apache.ambari.server.controller.internal.Stack stack = createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);
        java.util.Set<java.lang.String> stackServices = com.google.common.collect.ImmutableSet.of("KAFKA", "ZOOKEEPER");
        EasyMock.expect(stack.getServices()).andStubReturn(stackServices);
        EasyMock.expect(stack.getServiceForComponent("KAFKA_BROKER")).andStubReturn("KAFKA");
        EasyMock.expect(stack.getServiceForComponent("ZOOKEEPER_SERVER")).andStubReturn("ZOOKEEPER");
        EasyMock.expect(stack.getServiceForComponent("ZOOKEEPER_CLIENT")).andStubReturn("ZOOKEEPER");
        EasyMock.expect(stack.getValidDefaultConfig()).andStubReturn(org.apache.ambari.server.topology.Configuration.newEmpty());
        return stack;
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> someNewServices() {
        return com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401.ambari.apache.org")));
    }

    private void permissiveValidation() {
        EasyMock.expect(request.getValidationType()).andReturn(org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.PERMISSIVE).anyTimes();
    }

    private void requestServices(boolean validated, java.lang.String... services) {
        EasyMock.expect(request.getServices()).andReturn(java.util.Arrays.stream(services).map(org.apache.ambari.server.topology.addservice.Service::of).collect(java.util.stream.Collectors.toSet())).anyTimes();
        if (validated) {
            validatedServices(services);
        }
    }

    private void validatedServices(java.lang.String... services) {
        validator.setState(org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL.with(simpleMockStack()).withNewServices(java.util.Arrays.stream(services).collect(java.util.stream.Collectors.toMap(java.util.function.Function.identity(), __ -> com.google.common.collect.ImmutableMap.of()))));
    }

    private void requestComponents(java.lang.String... components) {
        EasyMock.expect(request.getComponents()).andReturn(java.util.Arrays.stream(components).map(componentName -> org.apache.ambari.server.topology.addservice.Component.of(componentName, "c7401.ambari.apache.org")).collect(java.util.stream.Collectors.toSet()));
    }

    private void secureCluster() {
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).anyTimes();
    }

    private void requestSpecifiesSecurity() {
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(org.apache.ambari.server.topology.SecurityConfiguration.KERBEROS)).anyTimes();
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor requestHasKerberosDescriptorFor(boolean byReference, java.lang.String... services) {
        org.apache.ambari.server.topology.SecurityConfiguration requestSecurity = (byReference) ? org.apache.ambari.server.topology.SecurityConfiguration.withReference(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE) : org.apache.ambari.server.topology.SecurityConfiguration.withDescriptor(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR);
        EasyMock.expect(request.getSecurity()).andReturn(java.util.Optional.of(requestSecurity)).anyTimes();
        if (byReference) {
            EasyMock.expect(securityConfigurationFactory.loadSecurityConfigurationByReference(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE)).andReturn(org.apache.ambari.server.topology.SecurityConfiguration.forTest(org.apache.ambari.server.state.SecurityType.KERBEROS, org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR_REFERENCE, org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR));
        }
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = org.apache.ambari.server.topology.addservice.RequestValidatorTest.kerberosDescriptorForServices(services);
        EasyMock.expect(kerberosDescriptorFactory.createInstance(org.apache.ambari.server.topology.addservice.RequestValidatorTest.FAKE_DESCRIPTOR)).andReturn(kerberosDescriptor).anyTimes();
        return kerberosDescriptor;
    }

    private static org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptorForServices(java.lang.String... newServices) {
        return new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_SERVICES, java.util.Arrays.stream(newServices).map(each -> com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_NAME, each)).collect(java.util.stream.Collectors.toList())));
    }
}