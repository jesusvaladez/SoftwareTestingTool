package org.apache.ambari.server.orm.entities;
public class AlertCurrentEntityTest {
    @org.junit.Test
    public void testHashCodeAndEquals() {
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history1.setAlertId(1L);
        history2.setAlertId(2L);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current2 = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        org.junit.Assert.assertEquals(current1.hashCode(), current2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(current1, current2));
        current1.setAlertHistory(history1);
        current2.setAlertHistory(history2);
        org.junit.Assert.assertNotSame(current1.hashCode(), current2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(current1, current2));
        current2.setAlertHistory(history1);
        org.junit.Assert.assertEquals(current1.hashCode(), current2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(current1, current2));
        current1.setAlertId(1L);
        current2.setAlertId(2L);
        org.junit.Assert.assertNotSame(current1.hashCode(), current2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(current1, current2));
        current2.setAlertId(1L);
        org.junit.Assert.assertEquals(current1.hashCode(), current2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(current1, current2));
    }
}