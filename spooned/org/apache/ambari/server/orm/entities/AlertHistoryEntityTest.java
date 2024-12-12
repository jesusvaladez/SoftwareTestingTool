package org.apache.ambari.server.orm.entities;
public class AlertHistoryEntityTest {
    @org.junit.Test
    public void testHashCodeAndEquals() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition1 = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition1.setDefinitionId(1L);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition2 = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition2.setDefinitionId(2L);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        org.junit.Assert.assertEquals(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(history1, history2));
        history1.setAlertDefinition(definition1);
        history2.setAlertDefinition(definition2);
        org.junit.Assert.assertNotSame(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(history1, history2));
        history2.setAlertDefinition(definition1);
        org.junit.Assert.assertEquals(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(history1, history2));
        history1.setAlertLabel("label");
        history1.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history1.setAlertText("text");
        history1.setAlertTimestamp(1L);
        history1.setClusterId(1L);
        history1.setComponentName("component");
        history1.setServiceName("service");
        history1.setHostName("host");
        history2.setAlertLabel("label");
        history2.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history2.setAlertText("text");
        history2.setAlertTimestamp(1L);
        history2.setClusterId(1L);
        history2.setComponentName("component");
        history2.setServiceName("service");
        history2.setHostName("host");
        org.junit.Assert.assertEquals(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(history1, history2));
        history2.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        org.junit.Assert.assertNotSame(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(history1, history2));
        history2.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history1.setAlertId(1L);
        history2.setAlertId(1L);
        org.junit.Assert.assertEquals(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(history1, history2));
        history2.setAlertId(2L);
        org.junit.Assert.assertNotSame(history1.hashCode(), history2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(history1, history2));
    }
}