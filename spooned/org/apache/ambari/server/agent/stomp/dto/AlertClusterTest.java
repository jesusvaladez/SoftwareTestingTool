package org.apache.ambari.server.agent.stomp.dto;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
public class AlertClusterTest {
    private static final int DEFAULT_INTERVAL = 1;

    private static final int CHANGED_INTERVAL = 999;

    private org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster;

    @org.junit.Test
    public void testAddingNewAlertDefWithoutChangingExisting() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition existing1 = anAlertDefinition(1L);
        org.apache.ambari.server.state.alert.AlertDefinition existing2 = anAlertDefinition(2L);
        alertCluster = newAlertCluster(existing1, existing2);
        org.apache.ambari.server.state.alert.AlertDefinition newDef = anAlertDefinition(3L);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster result = update(alertCluster, newDef);
        assertHasAlerts(result, existing1, existing2, newDef);
    }

    @org.junit.Test
    public void testChangingContentOfExistingAlertDef() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition existing1 = anAlertDefinition(1L);
        org.apache.ambari.server.state.alert.AlertDefinition existing2 = anAlertDefinition(2L);
        alertCluster = newAlertCluster(existing1, existing2);
        org.apache.ambari.server.state.alert.AlertDefinition changed = anAlertDefinition(2, org.apache.ambari.server.agent.stomp.dto.AlertClusterTest.CHANGED_INTERVAL);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster result = update(alertCluster, changed);
        assertHasAlerts(result, existing1, changed);
    }

    @org.junit.Test
    public void testAddingNewAlertDefAndChangingExisting() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition existing1 = anAlertDefinition(1L);
        org.apache.ambari.server.state.alert.AlertDefinition existing2 = anAlertDefinition(2L);
        alertCluster = newAlertCluster(existing1, existing2);
        org.apache.ambari.server.state.alert.AlertDefinition newDef = anAlertDefinition(3L);
        org.apache.ambari.server.state.alert.AlertDefinition changed = anAlertDefinition(2, 999);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster result = update(alertCluster, newDef, changed);
        assertHasAlerts(result, existing1, changed, newDef);
    }

    @org.junit.Test
    public void testNoChange() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition existing = anAlertDefinition(1L);
        alertCluster = newAlertCluster(existing);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster result = update(alertCluster, existing);
        org.junit.Assert.assertThat(result, org.hamcrest.core.Is.is(org.hamcrest.CoreMatchers.nullValue()));
    }

    private void assertHasAlerts(org.apache.ambari.server.agent.stomp.dto.AlertCluster result, org.apache.ambari.server.state.alert.AlertDefinition... expectedItems) {
        org.junit.Assert.assertNotNull(result);
        org.junit.Assert.assertThat(result.getAlertDefinitions(), IsCollectionWithSize.hasSize(expectedItems.length));
        for (org.apache.ambari.server.state.alert.AlertDefinition expected : expectedItems) {
            org.junit.Assert.assertTrue((result.getAlertDefinitions() + " should have contained: ") + expected, result.getAlertDefinitions().stream().anyMatch(each -> each.deeplyEquals(expected)));
        }
    }

    private org.apache.ambari.server.state.alert.AlertDefinition anAlertDefinition(long id) {
        return anAlertDefinition(id, org.apache.ambari.server.agent.stomp.dto.AlertClusterTest.DEFAULT_INTERVAL);
    }

    private org.apache.ambari.server.state.alert.AlertDefinition anAlertDefinition(long id, int interval) {
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition.setDefinitionId(id);
        alertDefinition.setName(java.lang.Long.toString(id));
        alertDefinition.setInterval(interval);
        return alertDefinition;
    }

    private org.apache.ambari.server.agent.stomp.dto.AlertCluster newAlertCluster(org.apache.ambari.server.state.alert.AlertDefinition... existingDefinitions) {
        return new org.apache.ambari.server.agent.stomp.dto.AlertCluster(asMap(existingDefinitions), "host");
    }

    private java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> asMap(org.apache.ambari.server.state.alert.AlertDefinition... alertDefinition) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alerts = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.alert.AlertDefinition each : alertDefinition) {
            alerts.put(each.getDefinitionId(), each);
        }
        return alerts;
    }

    private org.apache.ambari.server.agent.stomp.dto.AlertCluster update(org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster, org.apache.ambari.server.state.alert.AlertDefinition... alertDefinitions) {
        return alertCluster.handleUpdate(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, newAlertCluster(alertDefinitions));
    }
}