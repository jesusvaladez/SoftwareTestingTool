package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class CredentialResourceProviderTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmpFolder.create();
        final java.io.File masterKeyFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
        junit.framework.Assert.assertTrue(new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey").initializeMasterKeyFile(masterKeyFile, "secret"));
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION.getKey(), tmpFolder.getRoot().getAbsolutePath());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION.getKey(), tmpFolder.getRoot().getAbsolutePath());
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.security.SecurePasswordHelper.class).toInstance(new org.apache.ambari.server.security.SecurePasswordHelper());
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        tmpFolder.delete();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        junit.framework.Assert.assertNotNull(lastEvent);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Credential, lastEvent.getResourceType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        junit.framework.Assert.assertEquals(request, lastEvent.getRequest());
        junit.framework.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testCreateResources_FailMissingAlias() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = getCredentialTestProperties("c1", null, "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        try {
            provider.createResources(request);
            junit.framework.Assert.fail("Expected exception due to missing alias");
        } catch (java.lang.IllegalArgumentException e) {
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testCreateResources_FailMissingPrincipal() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = getCredentialTestProperties("c1", "alias1", null, "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        try {
            provider.createResources(request);
            junit.framework.Assert.fail("Expected exception due to missing alias");
        } catch (java.lang.IllegalArgumentException e) {
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testCreateResources_NotInitialized() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.security.SecurePasswordHelper.class).toInstance(new org.apache.ambari.server.security.SecurePasswordHelper());
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        try {
            provider.createResources(request);
            junit.framework.Assert.fail("Expected IllegalArgumentException thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Credentials cannot be stored in Ambari's persistent secure credential " + ("store since secure persistent storage has not yet be configured.  Use ambari-server " + "setup-security to enable this feature."), e.getLocalizedMessage());
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testGetResourcesAsAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesAsClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesAsServiceAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        provider.createResources(request);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(3, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            java.lang.Object alias = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID);
            java.lang.Object type = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
            if ("alias1".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY.name().toLowerCase(), type);
            } else if ("alias2".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED.name().toLowerCase(), type);
            } else if ("alias3".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY.name().toLowerCase(), type);
            } else {
                junit.framework.Assert.fail("Unexpected alias in list: " + alias);
            }
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesWithPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResourcesWithPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        provider.createResources(request);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID).equals("alias1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            java.lang.Object alias = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID);
            java.lang.Object type = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
            if ("alias1".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY.name().toLowerCase(), type);
            } else {
                junit.framework.Assert.fail("Unexpected alias in list: " + alias);
            }
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateNoResultsAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateNoResults(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithPredicateNoResultsAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateNoResults(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesWithPredicateNoResultsAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithPredicateNoResults(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResourcesWithPredicateNoResults(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        provider.createResources(request);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID).equals("alias4").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        try {
            provider.getResources(request, predicate);
            junit.framework.Assert.fail("Expected NoSuchResourceException not thrown");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResourcesWithoutPredicateAsClusterAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResourcesWithoutPredicateAsServiceAdministrator() throws java.lang.Exception {
        testGetResourcesWithoutPredicate(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResourcesWithoutPredicate(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        EasyMock.expect(request.getPropertyIds()).andReturn(null).once();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, null);
        junit.framework.Assert.assertTrue(results.isEmpty());
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        properties.addAll(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        EasyMock.expect(request.getProperties()).andReturn(properties).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", null, "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID).equals("alias1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            java.lang.Object alias = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID);
            java.lang.Object type = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
            if ("alias1".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY.name().toLowerCase(), type);
            } else {
                junit.framework.Assert.fail("Unexpected alias in list: " + alias);
            }
        }
        provider.updateResources(request, predicate);
        results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            java.lang.Object alias = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID);
            java.lang.Object type = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
            if ("alias1".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED.name().toLowerCase(), type);
            } else {
                junit.framework.Assert.fail("Unexpected alias in list: " + alias);
            }
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testUpdateResourcesResourceNotFoundAsAdministrator() throws java.lang.Exception {
        testUpdateResourcesResourceNotFound(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testUpdateResourcesResourceNotFoundAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResourcesResourceNotFound(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesResourceNotFoundAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResourcesResourceNotFound(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testUpdateResourcesResourceNotFound(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        properties.addAll(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        EasyMock.expect(request.getProperties()).andReturn(properties).once();
        EasyMock.expect(request.getProperties()).andReturn(getCredentialTestProperties("c1", null, "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID).equals("alias4").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        try {
            provider.updateResources(request, predicate);
            junit.framework.Assert.fail("Expected NoSuchResourceException thrown");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
        }
        EasyMock.verify(request, factory, managementController);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsServiceAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.internal.CredentialResourceProvider credentialResourceProvider = new org.apache.ambari.server.controller.internal.CredentialResourceProvider(managementController);
        injector.injectMembers(credentialResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        properties.addAll(getCredentialTestProperties("c1", "alias1", "username1", "password1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias2", "username2", "password2", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        properties.addAll(getCredentialTestProperties("c1", "alias3", "username3", "password3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        EasyMock.expect(request.getProperties()).andReturn(properties).once();
        EasyMock.expect(request.getPropertyIds()).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getCredentialResourceProvider(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class))).andReturn(credentialResourceProvider);
        EasyMock.replay(request, factory, managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Credential, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID).equals("alias1").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, results.size());
        for (org.apache.ambari.server.controller.spi.Resource result : results) {
            java.lang.Object alias = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID);
            java.lang.Object type = result.getPropertyValue(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
            if ("alias1".equals(alias)) {
                junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY.name().toLowerCase(), type);
            } else {
                junit.framework.Assert.fail("Unexpected alias in list: " + alias);
            }
        }
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        try {
            provider.getResources(request, predicate);
            junit.framework.Assert.fail("Expected NoSuchResourceException thrown");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
        }
        EasyMock.verify(request, factory, managementController);
    }

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getCredentialTestProperties(java.lang.String clusterName, java.lang.String alias, java.lang.String principal, java.lang.String password, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) {
        java.util.Map<java.lang.String, java.lang.Object> mapProperties = new java.util.HashMap<>();
        if (clusterName != null) {
            mapProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID, clusterName);
        }
        if (alias != null) {
            mapProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID, alias);
        }
        if (password != null) {
            mapProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID, password);
        }
        if (principal != null) {
            mapProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID, principal);
        }
        if (credentialStoreType != null) {
            mapProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID, credentialStoreType.name().toLowerCase());
        }
        return java.util.Collections.singleton(mapProperties);
    }
}