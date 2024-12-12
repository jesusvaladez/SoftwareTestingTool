package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class AutoInstanceConfigTest {
    private static java.lang.String VIEW_XML = "<view>\n" + (((((((((((((((((((((((((((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <description>Description</description>") + "    <version>1.0.0</version>\n") + "    <system>true</system>\n") + "    <icon64>/this/is/the/icon/url/icon64.png</icon64>\n") + "    <icon>/this/is/the/icon/url/icon.png</icon>\n") + "    <validator-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyValidator</validator-class>") + "    <masker-class>org.apache.ambari.server.view.DefaultMasker</masker-class>") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <label>Label 1.</label>\n") + "        <placeholder>Placeholder 1.</placeholder>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <default-value>Default value 1.</default-value>\n") + "        <cluster-config>hdfs-site/dfs.namenode.http-address</cluster-config>\n") + "        <required>false</required>\n") + "        <masked>true</masked>") + "    </parameter>\n") + "    <auto-instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "        <description>This is a description.</description>\n") + "        <icon64>/this/is/the/icon/url/instance_1_icon64.png</icon64>\n") + "        <icon>/this/is/the/icon/url/instance_1_icon.png</icon>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-1</value>\n") + "        </property>\n") + "        <property>\n") + "            <key>p2</key>\n") + "            <value>v2-1</value>\n") + "        </property>\n") + "        <stack-id>HDP-2.0</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "        <roles><role>CLUSTER.OPERATOR </role><role> CLUSTER.USER</role></roles>\n") + "    </auto-instance>\n") + "</view>");

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.AutoInstanceConfig config = org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.getAutoInstanceConfigs(org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.VIEW_XML);
        junit.framework.Assert.assertEquals("INSTANCE1", config.getName());
    }

    @org.junit.Test
    public void testDescription() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.AutoInstanceConfig config = org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.getAutoInstanceConfigs(org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.VIEW_XML);
        org.junit.Assert.assertEquals("This is a description.", config.getDescription());
    }

    @org.junit.Test
    public void testGetStackId() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.AutoInstanceConfig config = org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.getAutoInstanceConfigs(org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.VIEW_XML);
        org.junit.Assert.assertEquals("HDP-2.0", config.getStackId());
    }

    @org.junit.Test
    public void testGetServices() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.AutoInstanceConfig config = org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.getAutoInstanceConfigs(org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.VIEW_XML);
        java.util.List<java.lang.String> serviceNames = config.getServices();
        org.junit.Assert.assertEquals(2, serviceNames.size());
        org.junit.Assert.assertTrue(serviceNames.contains("HIVE"));
        org.junit.Assert.assertTrue(serviceNames.contains("HDFS"));
    }

    @org.junit.Test
    public void shouldParseClusterInheritedPermissions() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.AutoInstanceConfig config = org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.getAutoInstanceConfigs(org.apache.ambari.server.view.configuration.AutoInstanceConfigTest.VIEW_XML);
        java.util.Collection<java.lang.String> roles = config.getRoles();
        org.junit.Assert.assertEquals(2, roles.size());
        org.junit.Assert.assertTrue(roles.contains("CLUSTER.OPERATOR"));
        org.junit.Assert.assertTrue(roles.contains("CLUSTER.USER"));
    }

    private static org.apache.ambari.server.view.configuration.AutoInstanceConfig getAutoInstanceConfigs(java.lang.String xml) throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(xml);
        return config.getAutoInstance();
    }
}