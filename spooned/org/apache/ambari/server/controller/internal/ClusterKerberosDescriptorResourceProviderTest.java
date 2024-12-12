package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
@java.lang.SuppressWarnings("unchecked")
public class ClusterKerberosDescriptorResourceProviderTest extends org.easymock.EasyMockSupport {
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    private static final java.util.Map<java.lang.String, java.lang.Object> STACK_MAP;

    private static final java.util.Map<java.lang.String, java.lang.Object> USER_MAP;

    private static final java.util.Map<java.lang.String, java.lang.Object> COMPOSITE_MAP;

    static {
        java.util.TreeMap<java.lang.String, java.lang.Object> stackProperties = new java.util.TreeMap<>();
        stackProperties.put("realm", "EXAMPLE.COM");
        stackProperties.put("some.property", "Hello World");
        java.util.Collection<java.lang.String> authToLocalRules = new java.util.ArrayList<>();
        authToLocalRules.add("global.name.rules");
        java.util.TreeMap<java.lang.String, java.lang.Object> stackServices = new java.util.TreeMap<>();
        stackServices.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE.get("name"))), org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE);
        java.util.TreeMap<java.lang.String, java.lang.Object> stackClusterConfProperties = new java.util.TreeMap<>();
        stackClusterConfProperties.put("property1", "red");
        java.util.TreeMap<java.lang.String, java.lang.Object> stackClusterConf = new java.util.TreeMap<>();
        stackClusterConf.put("cluster-conf", stackClusterConfProperties);
        java.util.TreeMap<java.lang.String, java.lang.Object> stackConfigurations = new java.util.TreeMap<>();
        stackConfigurations.put("cluster-conf", stackClusterConf);
        java.util.TreeMap<java.lang.String, java.lang.Object> stackSharedIdentityKeytabOwner = new java.util.TreeMap<>();
        stackSharedIdentityKeytabOwner.put("name", "root");
        stackSharedIdentityKeytabOwner.put("access", "rw");
        java.util.TreeMap<java.lang.String, java.lang.Object> sharedIdentityKeytabGroup = new java.util.TreeMap<>();
        sharedIdentityKeytabGroup.put("name", "hadoop");
        sharedIdentityKeytabGroup.put("access", "r");
        java.util.TreeMap<java.lang.String, java.lang.Object> stackSharedIdentityKeytab = new java.util.TreeMap<>();
        stackSharedIdentityKeytab.put("file", "/etc/security/keytabs/subject.service.keytab");
        stackSharedIdentityKeytab.put("owner", stackSharedIdentityKeytabOwner);
        stackSharedIdentityKeytab.put("group", sharedIdentityKeytabGroup);
        stackSharedIdentityKeytab.put("configuration", "service-site/service2.component.keytab.file");
        java.util.TreeMap<java.lang.String, java.lang.Object> stackSharedIdentity = new java.util.TreeMap<>();
        stackSharedIdentity.put("name", "shared");
        stackSharedIdentity.put("principal", new java.util.TreeMap<>(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE));
        stackSharedIdentity.put("keytab", stackSharedIdentityKeytab);
        java.util.TreeMap<java.lang.String, java.lang.Object> stackIdentities = new java.util.TreeMap<>();
        stackIdentities.put("shared", stackSharedIdentity);
        STACK_MAP = new java.util.TreeMap<>();
        STACK_MAP.put("properties", stackProperties);
        STACK_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.AUTH_TO_LOCAL_PROPERTY.getDescriptorPluralName(), authToLocalRules);
        STACK_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.SERVICE.getDescriptorPluralName(), stackServices.values());
        STACK_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.CONFIGURATION.getDescriptorPluralName(), stackConfigurations.values());
        STACK_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.IDENTITY.getDescriptorPluralName(), stackIdentities.values());
        java.util.TreeMap<java.lang.String, java.lang.Object> userProperties = new java.util.TreeMap<>();
        userProperties.put("realm", "HWX.COM");
        userProperties.put("some.property", "Hello World");
        java.util.TreeMap<java.lang.String, java.lang.Object> userClusterConfProperties = new java.util.TreeMap<>();
        userClusterConfProperties.put("property1", "blue");
        userClusterConfProperties.put("property2", "orange");
        java.util.TreeMap<java.lang.String, java.lang.Object> userClusterConf = new java.util.TreeMap<>();
        userClusterConf.put("cluster-conf", userClusterConfProperties);
        java.util.TreeMap<java.lang.String, java.lang.Object> userConfigurations = new java.util.TreeMap<>();
        userConfigurations.put("cluster-conf", userClusterConf);
        java.util.TreeMap<java.lang.String, java.lang.Object> userSharedIdentityKeytabOwner = new java.util.TreeMap<>();
        userSharedIdentityKeytabOwner.put("name", "root");
        userSharedIdentityKeytabOwner.put("access", "rw");
        java.util.TreeMap<java.lang.String, java.lang.Object> userSharedIdentityKeytabGroup = new java.util.TreeMap<>();
        userSharedIdentityKeytabGroup.put("name", "hadoop");
        userSharedIdentityKeytabGroup.put("access", "r");
        java.util.TreeMap<java.lang.String, java.lang.Object> userSharedIdentityKeytab = new java.util.TreeMap<>();
        userSharedIdentityKeytab.put("file", "/etc/security/keytabs/subject.service.keytab");
        userSharedIdentityKeytab.put("owner", userSharedIdentityKeytabOwner);
        userSharedIdentityKeytab.put("group", userSharedIdentityKeytabGroup);
        userSharedIdentityKeytab.put("configuration", "service-site/service2.component.keytab.file");
        java.util.TreeMap<java.lang.String, java.lang.Object> userSharedIdentity = new java.util.TreeMap<>();
        userSharedIdentity.put("name", "shared");
        userSharedIdentity.put("principal", new java.util.TreeMap<>(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE));
        userSharedIdentity.put("keytab", userSharedIdentityKeytab);
        java.util.TreeMap<java.lang.String, java.lang.Object> userIdentities = new java.util.TreeMap<>();
        userIdentities.put("shared", userSharedIdentity);
        USER_MAP = new java.util.TreeMap<>();
        USER_MAP.put("properties", userProperties);
        USER_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.CONFIGURATION.getDescriptorPluralName(), userConfigurations.values());
        USER_MAP.put(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.IDENTITY.getDescriptorPluralName(), userIdentities.values());
        COMPOSITE_MAP = new java.util.TreeMap<>();
        COMPOSITE_MAP.putAll(STACK_MAP);
        COMPOSITE_MAP.putAll(USER_MAP);
    }

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.security.SecurePasswordHelper.class).toInstance(new org.apache.ambari.server.security.SecurePasswordHelper());
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
                bind(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class).toInstance(new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory());
            }
        });
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider(managementController);
        injector.injectMembers(resourceProvider);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        verifyAll();
    }

    @org.junit.Test
    public void testGetResourcesAsAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesAsClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsClusterOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesAsServiceAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsServiceOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesAsClusterUser() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).atLeastOnce();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).atLeastOnce();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(3, results.size());
        verifyAll();
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsClusterOperator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsServiceOperator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsClusterUser() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testGetResourcesWithPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).atLeastOnce();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = injector.getInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor stackKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.USER_MAP);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor compositeKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP);
        compositeKerberosDescriptor.update(userKerberosDescriptor);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = createMock(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.STACK), EasyMock.eq(cluster), EasyMock.eq(false), EasyMock.anyObject(java.util.Collection.class), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(stackKerberosDescriptor).atLeastOnce();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.USER), EasyMock.eq(cluster), EasyMock.eq(false), EasyMock.anyObject(java.util.Collection.class), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(userKerberosDescriptor).atLeastOnce();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE), EasyMock.eq(cluster), EasyMock.eq(false), EasyMock.anyObject(java.util.Collection.class), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(compositeKerberosDescriptor).atLeastOnce();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(managementController.getKerberosHelper()).andReturn(kerberosHelper).atLeastOnce();
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).atLeastOnce();
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(java.util.Collections.emptyMap()).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate typePredicate;
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results;
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("STACK").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        testResults("STACK", org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP, results);
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("USER").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        testResults("USER", org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.USER_MAP, results);
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("COMPOSITE").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        testResults("COMPOSITE", org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.COMPOSITE_MAP, results);
        verifyAll();
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsClusterOperator() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsServiceOperator() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAndDirectivesAsClusterUser() throws java.lang.Exception {
        testGetResourcesWithPredicateAndDirectives(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testGetResourcesWithPredicateAndDirectives(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).atLeastOnce();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = injector.getInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor stackKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.USER_MAP);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor compositeKerberosDescriptor = kerberosDescriptorFactory.createInstance(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP);
        compositeKerberosDescriptor.update(userKerberosDescriptor);
        org.easymock.Capture<? extends java.util.Collection<java.lang.String>> captureAdditionalServices = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = createMock(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.STACK), EasyMock.eq(cluster), EasyMock.eq(true), EasyMock.capture(captureAdditionalServices), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(stackKerberosDescriptor).atLeastOnce();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.USER), EasyMock.eq(cluster), EasyMock.eq(true), EasyMock.capture(captureAdditionalServices), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(userKerberosDescriptor).atLeastOnce();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(EasyMock.eq(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE), EasyMock.eq(cluster), EasyMock.eq(true), EasyMock.capture(captureAdditionalServices), EasyMock.eq(false), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(compositeKerberosDescriptor).atLeastOnce();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(managementController.getKerberosHelper()).andReturn(kerberosHelper).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_EVALUATE_WHEN_CLAUSE, "true");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_ADDITIONAL_SERVICES, "HIVE, TEZ,PIG");
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).atLeastOnce();
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate typePredicate;
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results;
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("STACK").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            junit.framework.Assert.assertEquals("c1", result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID));
            junit.framework.Assert.assertEquals("STACK", result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID));
            java.util.Map partial1 = result.getPropertiesMap().get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID);
            java.util.Map partial2 = result.getPropertiesMap().get("KerberosDescriptor/kerberos_descriptor/properties");
            partial1.put("properties", partial2);
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP), org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(partial1));
        }
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("USER").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            junit.framework.Assert.assertEquals("c1", result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID));
            junit.framework.Assert.assertEquals("USER", result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID));
            java.util.Map partial1 = result.getPropertiesMap().get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID);
            java.util.Map partial2 = result.getPropertiesMap().get("KerberosDescriptor/kerberos_descriptor/properties");
            partial1.put("properties", partial2);
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.USER_MAP), org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(partial1));
        }
        typePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("COMPOSITE").toPredicate();
        results = provider.getResources(request, new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, typePredicate));
        junit.framework.Assert.assertEquals(1, results.size());
        testResults("COMPOSITE", org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.COMPOSITE_MAP, results);
        verifyAll();
        java.util.List<? extends java.util.Collection<java.lang.String>> capturedValues = captureAdditionalServices.getValues();
        junit.framework.Assert.assertEquals(3, capturedValues.size());
        for (java.util.Collection<java.lang.String> capturedValue : capturedValues) {
            junit.framework.Assert.assertEquals(3, capturedValue.size());
            junit.framework.Assert.assertTrue(capturedValue.contains("HIVE"));
            junit.framework.Assert.assertTrue(capturedValue.contains("PIG"));
            junit.framework.Assert.assertTrue(capturedValue.contains("TEZ"));
        }
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetResourcesWithInvalidKerberosDescriptorTypeAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithInvalidKerberosDescriptorType(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetResourcesWithInvalidKerberosDescriptorTypeAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithInvalidKerberosDescriptorType(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetResourcesWithInvalidKerberosDescriptorTypeAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithInvalidKerberosDescriptorType(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResourcesWithInvalidKerberosDescriptorType(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackVersion = createMock(org.apache.ambari.server.state.StackId.class);
        EasyMock.expect(stackVersion.getStackName()).andReturn("stackName").atLeastOnce();
        EasyMock.expect(stackVersion.getStackVersion()).andReturn("stackVersion").atLeastOnce();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).atLeastOnce();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackVersion).atLeastOnce();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.toMap()).andReturn(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.STACK_MAP).atLeastOnce();
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(metaInfo.getKerberosDescriptor("stackName", "stackVersion", false)).andReturn(kerberosDescriptor).atLeastOnce();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(metaInfo).atLeastOnce();
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("BOGUS").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        try {
            provider.getResources(request, predicate);
            junit.framework.Assert.fail("Expected NoSuchResourceException not thrown");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsClusterOperator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsServiceOperator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsClusterUser() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testGetResourcesWithoutPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        junit.framework.Assert.assertTrue(results.isEmpty());
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider(managementController);
        injector.injectMembers(resourceProvider);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        provider.createResources(request);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResourcesAsServiceAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider(managementController);
        injector.injectMembers(resourceProvider);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID).equals("alias1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        verifyAll();
    }

    private void testResults(java.lang.String type, java.util.Map<java.lang.String, java.lang.Object> expectedData, java.util.Set<org.apache.ambari.server.controller.spi.Resource> results) {
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            junit.framework.Assert.assertEquals("c1", result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID));
            junit.framework.Assert.assertEquals(type, result.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID));
            java.util.Map partial1 = result.getPropertiesMap().get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID);
            java.util.Map partial2 = result.getPropertiesMap().get("KerberosDescriptor/kerberos_descriptor/properties");
            partial1.put("properties", partial2);
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(expectedData), org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProviderTest.GSON.toJson(partial1));
        }
    }
}