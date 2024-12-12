package org.apache.ambari.server.metadata;
@com.google.inject.Singleton
public class AmbariServiceAlertDefinitions {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.class);

    private java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> m_agentDefinitions = null;

    private java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> m_serverDefinitions = null;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_factory;

    public java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getAgentDefinitions() {
        if (null != m_agentDefinitions) {
            return m_agentDefinitions;
        }
        m_agentDefinitions = getDefinitions(org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT);
        return m_agentDefinitions;
    }

    public java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getServerDefinitions() {
        if (null != m_serverDefinitions) {
            return m_serverDefinitions;
        }
        m_serverDefinitions = getDefinitions(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER);
        return m_serverDefinitions;
    }

    private java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getDefinitions(org.apache.ambari.server.controller.RootComponent component) {
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions = new java.util.ArrayList<>();
        java.io.InputStream inputStream = java.lang.ClassLoader.getSystemResourceAsStream("alerts.json");
        java.io.InputStreamReader reader = new java.io.InputStreamReader(inputStream);
        try {
            java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> allDefinitions = m_factory.getAlertDefinitions(reader, org.apache.ambari.server.controller.RootService.AMBARI.name());
            java.lang.String componentName = component.name();
            for (org.apache.ambari.server.state.alert.AlertDefinition definition : allDefinitions) {
                if (componentName.equals(definition.getComponentName())) {
                    definitions.add(definition);
                }
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions.LOG.error("Unable to load the Ambari alerts JSON file", exception);
        }
        return definitions;
    }
}