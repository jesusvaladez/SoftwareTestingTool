package org.apache.ambari.server.configuration;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION;
import static org.easymock.EasyMock.expect;
public class AmbariServerConfigurationProviderTest extends org.easymock.EasyMockSupport {
    private static final org.apache.ambari.server.configuration.AmbariServerConfigurationCategory TEST_CONFIGURATION = org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION;

    @org.junit.Test
    public void testGetAndLoadDataForVariousEvents() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.configuration.AmbariServerConfiguration emptyTestConfiguration = createMock(org.apache.ambari.server.configuration.AmbariServerConfiguration.class);
        org.apache.ambari.server.configuration.AmbariServerConfiguration filledTestConfiguration1 = createMock(org.apache.ambari.server.configuration.AmbariServerConfiguration.class);
        org.apache.ambari.server.configuration.AmbariServerConfiguration filledTestConfiguration2 = createMock(org.apache.ambari.server.configuration.AmbariServerConfiguration.class);
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        com.google.inject.persist.jpa.AmbariJpaPersistService persistService = injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        org.apache.ambari.server.configuration.AmbariServerConfigurationProvider provider = createMockBuilder(org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.class).addMockedMethod("loadInstance", java.util.Collection.class).withConstructor(org.apache.ambari.server.configuration.AmbariServerConfigurationProviderTest.TEST_CONFIGURATION, publisher, persistService).createMock();
        EasyMock.expect(provider.loadInstance(java.util.Collections.emptyList())).andReturn(emptyTestConfiguration).once();
        EasyMock.expect(provider.loadInstance(null)).andReturn(filledTestConfiguration1).once();
        EasyMock.expect(provider.loadInstance(null)).andReturn(filledTestConfiguration2).once();
        replayAll();
        injector.injectMembers(provider);
        org.apache.ambari.server.configuration.AmbariServerConfiguration configuration = provider.get();
        org.junit.Assert.assertSame(emptyTestConfiguration, configuration);
        provider.ambariConfigurationChanged(new org.apache.ambari.server.events.AmbariConfigurationChangedEvent(org.apache.ambari.server.configuration.AmbariServerConfigurationProviderTest.TEST_CONFIGURATION.getCategoryName()));
        org.apache.ambari.server.configuration.AmbariServerConfiguration configuration2 = provider.get();
        org.junit.Assert.assertSame(configuration, configuration2);
        provider.jpaInitialized(new org.apache.ambari.server.events.JpaInitializedEvent());
        org.apache.ambari.server.configuration.AmbariServerConfiguration configuration3 = provider.get();
        org.junit.Assert.assertSame(filledTestConfiguration1, configuration3);
        provider.ambariConfigurationChanged(new org.apache.ambari.server.events.AmbariConfigurationChangedEvent(org.apache.ambari.server.configuration.AmbariServerConfigurationProviderTest.TEST_CONFIGURATION.getCategoryName()));
        org.apache.ambari.server.configuration.AmbariServerConfiguration configuration4 = provider.get();
        org.junit.Assert.assertNotSame(configuration3, configuration4);
        verifyAll();
    }

    @org.junit.Test
    public void testToProperties() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        com.google.inject.persist.jpa.AmbariJpaPersistService persistService = injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class);
        org.apache.ambari.server.configuration.AmbariServerConfigurationProvider provider = createMockBuilder(org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.class).withConstructor(org.apache.ambari.server.configuration.AmbariServerConfigurationProviderTest.TEST_CONFIGURATION, publisher, persistService).createMock();
        replayAll();
        java.util.Map actualProperties;
        actualProperties = provider.toProperties(null);
        org.junit.Assert.assertNotNull(actualProperties);
        org.junit.Assert.assertEquals(java.util.Collections.emptyMap(), actualProperties);
        actualProperties = provider.toProperties(java.util.Collections.emptyList());
        org.junit.Assert.assertNotNull(actualProperties);
        org.junit.Assert.assertEquals(java.util.Collections.emptyMap(), actualProperties);
        java.util.Map<java.lang.String, java.lang.String> expectedProperties = new java.util.HashMap<>();
        expectedProperties.put("one", "1");
        expectedProperties.put("two", "2");
        expectedProperties.put("three", "3");
        actualProperties = provider.toProperties(createAmbariConfigurationEntities(expectedProperties));
        org.junit.Assert.assertNotNull(actualProperties);
        org.junit.Assert.assertNotSame(expectedProperties, actualProperties);
        org.junit.Assert.assertEquals(expectedProperties, actualProperties);
        verifyAll();
    }

    private java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> createAmbariConfigurationEntities(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
            org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
            entity.setCategoryName("some-category");
            entity.setPropertyName(entry.getKey());
            entity.setPropertyValue(entry.getValue());
            entities.add(entity);
        }
        return entities;
    }

    private com.google.inject.Injector getInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                com.google.inject.persist.jpa.AmbariJpaPersistService persistService = createMockBuilder(com.google.inject.persist.jpa.AmbariJpaPersistService.class).withConstructor("test", java.util.Collections.emptyMap()).createMock();
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(com.google.inject.persist.jpa.AmbariJpaPersistService.class).toInstance(persistService);
                bind(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            }
        });
    }
}