package org.apache.ambari.server.orm.entities;
public class AlertDefinitionEntityTest {
    @org.junit.Test
    public void testHashCodeAndEquals() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition1 = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition2 = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        org.junit.Assert.assertEquals(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(definition1, definition2));
        definition1.setClusterId(1L);
        definition2.setClusterId(1L);
        org.junit.Assert.assertEquals(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(definition1, definition2));
        definition1.setDefinitionName("definition-name");
        definition2.setDefinitionName("definition-name");
        org.junit.Assert.assertEquals(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(definition1, definition2));
        definition2.setDefinitionName("definition-name-foo");
        org.junit.Assert.assertNotSame(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(definition1, definition2));
        definition2.setDefinitionName("definition-name");
        definition2.setClusterId(2L);
        org.junit.Assert.assertNotSame(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(definition1, definition2));
        definition2.setClusterId(1L);
        definition1.setDefinitionId(1L);
        org.junit.Assert.assertNotSame(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(definition1, definition2));
        definition2.setDefinitionId(1L);
        org.junit.Assert.assertEquals(definition1.hashCode(), definition2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(definition1, definition2));
    }
}