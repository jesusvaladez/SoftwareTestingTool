package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class PermissionConfigTest {
    private static final java.lang.String xml = "<view>\n" + (((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "  <permission>\n") + "    <name>RESTRICTED</name>\n") + "    <description>Access permission for a restricted view resource.</description>\n") + "  </permission>") + "</view>");

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> permissions = org.apache.ambari.server.view.configuration.PermissionConfigTest.getPremissionConfig();
        org.junit.Assert.assertEquals(1, permissions.size());
        org.junit.Assert.assertEquals("RESTRICTED", permissions.get(0).getName());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> permissions = org.apache.ambari.server.view.configuration.PermissionConfigTest.getPremissionConfig();
        org.junit.Assert.assertEquals(1, permissions.size());
        org.junit.Assert.assertEquals("Access permission for a restricted view resource.", permissions.get(0).getDescription());
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> getPremissionConfig() throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.PermissionConfigTest.xml);
        return config.getPermissions();
    }
}