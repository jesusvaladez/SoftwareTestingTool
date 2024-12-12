package org.apache.ambari.server.alerts;
public class AlertHelperTest {
    @org.junit.Test
    public void testThresholdCalculations() {
        org.apache.ambari.server.state.alert.AlertHelper alertHelper = new org.apache.ambari.server.state.alert.AlertHelper();
        org.junit.Assert.assertEquals(1, alertHelper.getThresholdValue(1, 2));
        org.junit.Assert.assertEquals(1, alertHelper.getThresholdValue("1", 2));
        org.junit.Assert.assertEquals(1, alertHelper.getThresholdValue("1.00", 2));
        org.junit.Assert.assertEquals(1, alertHelper.getThresholdValue("foo", 1));
        org.junit.Assert.assertEquals(1, alertHelper.getThresholdValue(new java.lang.Object(), 1));
    }

    @org.junit.Test
    public void testStaleAlertsOperations() {
        org.apache.ambari.server.state.alert.AlertHelper alertHelper = new org.apache.ambari.server.state.alert.AlertHelper();
        alertHelper.addStaleAlerts(1L, new java.util.ArrayList<org.apache.ambari.server.agent.StaleAlert>() {
            {
                add(new org.apache.ambari.server.agent.StaleAlert(1L, 111L));
                add(new org.apache.ambari.server.agent.StaleAlert(2L, 111L));
                add(new org.apache.ambari.server.agent.StaleAlert(3L, 111L));
                add(new org.apache.ambari.server.agent.StaleAlert(4L, 111L));
            }
        });
        org.junit.Assert.assertEquals(4, alertHelper.getStaleAlerts(1L).size());
        alertHelper.addStaleAlerts(2L, new java.util.ArrayList<org.apache.ambari.server.agent.StaleAlert>() {
            {
                add(new org.apache.ambari.server.agent.StaleAlert(1L, 111L));
                add(new org.apache.ambari.server.agent.StaleAlert(2L, 111L));
            }
        });
        java.util.List<java.lang.Long> hostIds = alertHelper.getHostIdsByDefinitionId(1L);
        org.junit.Assert.assertEquals(2, hostIds.size());
        org.junit.Assert.assertTrue(hostIds.contains(1L));
        org.junit.Assert.assertTrue(hostIds.contains(2L));
    }
}