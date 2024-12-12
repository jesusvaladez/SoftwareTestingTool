package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class EntityConfigTest {
    private static final java.lang.String xml = "<view>\n" + (((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "    <persistence>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.TestEntity1</class>\n") + "        <id-property>id</id-property>\n") + "      </entity>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.TestEntity2</class>\n") + "        <id-property>name</id-property>\n") + "      </entity>\n") + "    </persistence>") + "</view>");

    @org.junit.Test
    public void testGetClassName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.EntityConfig> entities = org.apache.ambari.server.view.configuration.EntityConfigTest.getEntityConfigs();
        org.junit.Assert.assertEquals(2, entities.size());
        org.junit.Assert.assertEquals("org.apache.ambari.server.view.TestEntity1", entities.get(0).getClassName());
        org.junit.Assert.assertEquals("org.apache.ambari.server.view.TestEntity2", entities.get(1).getClassName());
    }

    @org.junit.Test
    public void testGetIdProperty() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.EntityConfig> entities = org.apache.ambari.server.view.configuration.EntityConfigTest.getEntityConfigs();
        org.junit.Assert.assertEquals(2, entities.size());
        org.junit.Assert.assertEquals("id", entities.get(0).getIdProperty());
        org.junit.Assert.assertEquals("name", entities.get(1).getIdProperty());
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.EntityConfig> getEntityConfigs() throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.EntityConfigTest.xml);
        org.apache.ambari.server.view.configuration.PersistenceConfig persistenceConfig = config.getPersistence();
        return persistenceConfig.getEntities();
    }
}