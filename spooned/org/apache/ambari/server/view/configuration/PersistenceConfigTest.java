package org.apache.ambari.server.view.configuration;
public class PersistenceConfigTest {
    private static final java.lang.String xml = "<view>\n" + (((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "    <persistence>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.TestEntity1</class>\n") + "        <id-property>id</id-property>\n") + "      </entity>\n") + "      <entity>\n") + "        <class>org.apache.ambari.server.view.TestEntity2</class>\n") + "        <id-property>name</id-property>\n") + "      </entity>\n") + "    </persistence>") + "</view>");

    private static final java.lang.String xml_no_entities = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "    <persistence>\n") + "    </persistence>") + "</view>");

    private static final java.lang.String xml_no_persistence = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "    <persistence>\n") + "    </persistence>") + "</view>");

    @org.junit.Test
    public void testGetEntities() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.PersistenceConfigTest.xml);
        org.apache.ambari.server.view.configuration.PersistenceConfig persistenceConfig = config.getPersistence();
        org.junit.Assert.assertEquals(2, persistenceConfig.getEntities().size());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.PersistenceConfigTest.xml_no_entities);
        persistenceConfig = config.getPersistence();
        org.junit.Assert.assertTrue(persistenceConfig.getEntities().isEmpty());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.PersistenceConfigTest.xml_no_persistence);
        persistenceConfig = config.getPersistence();
        org.junit.Assert.assertTrue(persistenceConfig.getEntities().isEmpty());
    }
}