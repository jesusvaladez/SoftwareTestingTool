package org.apache.ambari.server.metadata;
import javax.persistence.EntityManager;
public class AgentAlertDefinitionsTest {
    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void before() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testLoadingAgentHostAlerts() {
        org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions ambariServiceAlertDefinitions = m_injector.getInstance(org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.class);
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions = ambariServiceAlertDefinitions.getAgentDefinitions();
        junit.framework.Assert.assertEquals(3, definitions.size());
        for (org.apache.ambari.server.state.alert.AlertDefinition definition : definitions) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name(), definition.getComponentName());
            junit.framework.Assert.assertEquals("AMBARI", definition.getServiceName());
        }
    }

    @org.junit.Test
    public void testLoadingServertAlerts() {
        org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions ambariServiceAlertDefinitions = m_injector.getInstance(org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.class);
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions = ambariServiceAlertDefinitions.getServerDefinitions();
        junit.framework.Assert.assertEquals(4, definitions.size());
        for (org.apache.ambari.server.state.alert.AlertDefinition definition : definitions) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), definition.getComponentName());
            junit.framework.Assert.assertEquals("AMBARI", definition.getServiceName());
        }
    }
}