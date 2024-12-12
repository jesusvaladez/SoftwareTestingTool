package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
public class ViewConfigTest {
    private static java.lang.String xml = "<view>\n" + ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <description>Description</description>") + "    <version>1.0.0</version>\n") + "    <build>99</build>\n") + "    <data-version>42</data-version>\n") + "    <system>true</system>\n") + "    <icon64>/this/is/the/icon/url/icon64.png</icon64>\n") + "    <icon>/this/is/the/icon/url/icon.png</icon>\n") + "    <cluster-config-options>AMBARI-ONLY</cluster-config-options>\n") + "    <data-migrator-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyDataMigrator</data-migrator-class>") + "    <validator-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyValidator</validator-class>") + "    <masker-class>org.apache.ambari.server.view.DefaultMasker</masker-class>") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <label>Label 1.</label>\n") + "        <placeholder>Placeholder 1.</placeholder>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <default-value>Default value 1.</default-value>\n") + "        <cluster-config>hdfs-site/dfs.namenode.http-address</cluster-config>\n") + "        <required>false</required>\n") + "        <masked>true</masked>") + "    </parameter>\n") + "    <resource>\n") + "        <name>resource</name>\n") + "        <plural-name>resources</plural-name>\n") + "        <id-property>id</id-property>\n") + "        <resource-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResource</resource-class>\n") + "        <provider-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceProvider</provider-class>\n") + "        <service-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceService</service-class>\n") + "        <sub-resource-name>subresource</sub-resource-name>\n") + "    </resource>\n") + "    <resource>\n") + "        <name>subresource</name>\n") + "        <plural-name>subresources</plural-name>\n") + "        <id-property>id</id-property>\n") + "        <resource-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResource</resource-class>\n") + "        <provider-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceProvider</provider-class>\n") + "        <service-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceService</service-class>\n") + "    </resource>\n") + "    <auto-instance>\n") + "        <name>AUTO-INSTANCE</name>\n") + "        <label>My Instance 1!</label>\n") + "        <description>This is a description.</description>\n") + "        <icon64>/this/is/the/icon/url/instance_1_icon64.png</icon64>\n") + "        <icon>/this/is/the/icon/url/instance_1_icon.png</icon>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-1</value>\n") + "        </property>\n") + "        <property>\n") + "            <key>p2</key>\n") + "            <value>v2-1</value>\n") + "        </property>\n") + "        <stack-id>HDP-2.0</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "    </auto-instance>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "        <description>This is a description.</description>\n") + "        <icon64>/this/is/the/icon/url/instance_1_icon64.png</icon64>\n") + "        <icon>/this/is/the/icon/url/instance_1_icon.png</icon>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-1</value>\n") + "        </property>\n") + "        <property>\n") + "            <key>p2</key>\n") + "            <value>v2-1</value>\n") + "        </property>\n") + "    </instance>\n") + "    <instance>\n") + "        <name>INSTANCE2</name>\n") + "        <label>My Instance 2!</label>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-2</value>\n") + "        </property>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String minimal_xml = "<view>\n" + ((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "</view>");

    private static java.lang.String view_class_xml = "<view>\n" + (((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <view-class>ViewImpl</view-class>\n") + "</view>");

    private static java.lang.String system_xml = "<view>\n" + (((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <system>true</system>\n") + "</view>");

    private static java.lang.String non_system_xml = "<view>\n" + (((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <system>false</system>\n") + "</view>");

    private static java.lang.String with_ambari_versions = "<view>\n" + ((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <min-ambari-version>1.6.1</min-ambari-version>\n") + "    <max-ambari-version>2.0.0</max-ambari-version>\n") + "</view>");

    private static java.lang.String EXTRA_CLASSPATH_XML = "<view>\n" + ((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <classpath>") + "      <path>/var/lib/</path>") + "      <path>/tmp/foo.jar</path>") + "    </classpath>\n") + "</view>");

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("MY_VIEW", config.getName());
    }

    @org.junit.Test
    public void testGetLabel() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("My View!", config.getLabel());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("Description", config.getDescription());
    }

    @org.junit.Test
    public void testGetVersion() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("1.0.0", config.getVersion());
    }

    @org.junit.Test
    public void testGetBuild() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("99", config.getBuild());
    }

    @org.junit.Test
    public void testGetDataVersion() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals(42, config.getDataVersion());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.minimal_xml);
        org.junit.Assert.assertEquals(0, config.getDataVersion());
    }

    @org.junit.Test
    public void testGetIcon() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon.png", config.getIcon());
    }

    @org.junit.Test
    public void testGetIcon64() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon64.png", config.getIcon64());
    }

    @org.junit.Test
    public void testGetValidator() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("org.apache.ambari.server.view.configuration.ViewConfigTest$MyValidator", config.getValidator());
    }

    @org.junit.Test
    public void testGetDataMigrator() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("org.apache.ambari.server.view.configuration.ViewConfigTest$MyDataMigrator", config.getDataMigrator());
    }

    @org.junit.Test
    public void testGetDataMigratorClass() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        java.lang.Class migrator = config.getDataMigratorClass(getClass().getClassLoader());
        org.junit.Assert.assertEquals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyDataMigrator.class, migrator);
    }

    @org.junit.Test
    public void testMasker() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("org.apache.ambari.server.view.DefaultMasker", config.getMasker());
    }

    @org.junit.Test
    public void testGetView() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.view_class_xml);
        org.junit.Assert.assertEquals("ViewImpl", config.getView());
    }

    @org.junit.Test
    public void testGetParameters() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = config.getParameters();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals("p1", parameters.get(0).getName());
        org.junit.Assert.assertEquals("p2", parameters.get(1).getName());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.minimal_xml);
        parameters = config.getParameters();
        org.junit.Assert.assertNotNull(parameters);
        org.junit.Assert.assertEquals(0, parameters.size());
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resources = config.getResources();
        org.junit.Assert.assertEquals(2, resources.size());
        org.junit.Assert.assertEquals("resource", resources.get(0).getName());
        org.junit.Assert.assertEquals("subresource", resources.get(1).getName());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.minimal_xml);
        resources = config.getResources();
        org.junit.Assert.assertNotNull(resources);
        org.junit.Assert.assertEquals(0, resources.size());
    }

    @org.junit.Test
    public void testGetAutoInstance() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.xml);
        org.apache.ambari.server.view.configuration.AutoInstanceConfig instance = config.getAutoInstance();
        org.junit.Assert.assertEquals("AUTO-INSTANCE", instance.getName());
    }

    @org.junit.Test
    public void testGetInstances() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.xml);
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = config.getInstances();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("INSTANCE1", instances.get(0).getName());
        org.junit.Assert.assertEquals("INSTANCE2", instances.get(1).getName());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.minimal_xml);
        instances = config.getInstances();
        org.junit.Assert.assertNotNull(instances);
        org.junit.Assert.assertEquals(0, instances.size());
    }

    @org.junit.Test
    public void testIsSystem() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.system_xml);
        org.junit.Assert.assertTrue(config.isSystem());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.non_system_xml);
        org.junit.Assert.assertFalse(config.isSystem());
    }

    @org.junit.Test
    public void testGetExtraClasspath() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.system_xml);
        org.junit.Assert.assertNull(config.getExtraClasspath());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.EXTRA_CLASSPATH_XML);
        org.junit.Assert.assertEquals("/var/lib/,/tmp/foo.jar", config.getExtraClasspath());
    }

    @org.junit.Test
    public void testGetMinAmbariVersion() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertNull(config.getMinAmbariVersion());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.with_ambari_versions);
        org.junit.Assert.assertEquals("1.6.1", config.getMinAmbariVersion());
    }

    @org.junit.Test
    public void testGetMaxAmbariVersion() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertNull(config.getMaxAmbariVersion());
        config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.with_ambari_versions);
        org.junit.Assert.assertEquals("2.0.0", config.getMaxAmbariVersion());
    }

    @org.junit.Test
    public void testGetClusterConfigOptions() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.junit.Assert.assertEquals("AMBARI-ONLY", config.getClusterConfigOptions());
    }

    public static org.apache.ambari.server.view.configuration.ViewConfig getConfig() throws javax.xml.bind.JAXBException {
        return org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.configuration.ViewConfigTest.xml);
    }

    public static org.apache.ambari.server.view.configuration.ViewConfig getConfig(java.lang.String xml) throws javax.xml.bind.JAXBException {
        java.io.InputStream configStream = new java.io.ByteArrayInputStream(xml.getBytes());
        javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.view.configuration.ViewConfig.class);
        javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return ((org.apache.ambari.server.view.configuration.ViewConfig) (unmarshaller.unmarshal(configStream)));
    }

    public static class MyViewServlet extends javax.servlet.http.HttpServlet {}

    public static class MyValidator implements org.apache.ambari.view.validation.Validator {
        org.apache.ambari.view.validation.ValidationResult result;

        @java.lang.Override
        public org.apache.ambari.view.validation.ValidationResult validateInstance(org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode) {
            return result;
        }

        @java.lang.Override
        public org.apache.ambari.view.validation.ValidationResult validateProperty(java.lang.String property, org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode) {
            return result;
        }
    }

    public static class MyDataMigrator implements org.apache.ambari.view.migration.ViewDataMigrator {
        @java.lang.Override
        public boolean beforeMigration() throws org.apache.ambari.view.migration.ViewDataMigrationException {
            return true;
        }

        @java.lang.Override
        public void afterMigration() throws org.apache.ambari.view.migration.ViewDataMigrationException {
        }

        @java.lang.Override
        public void migrateEntity(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        }

        @java.lang.Override
        public void migrateInstanceData() throws org.apache.ambari.view.migration.ViewDataMigrationException {
        }
    }

    public static class MyResource {
        private java.lang.String id;

        public java.lang.String getId() {
            return id;
        }

        public void setId(java.lang.String id) {
            this.id = id;
        }
    }

    public static class MyResourceProvider implements org.apache.ambari.view.ResourceProvider<org.apache.ambari.server.view.configuration.ViewConfigTest.MyResource> {
        @java.lang.Override
        public org.apache.ambari.server.view.configuration.ViewConfigTest.MyResource getResource(java.lang.String resourceId, java.util.Set<java.lang.String> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return null;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.view.configuration.ViewConfigTest.MyResource> getResources(org.apache.ambari.view.ReadRequest request) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return null;
        }

        @java.lang.Override
        public void createResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.ResourceAlreadyExistsException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
        }

        @java.lang.Override
        public boolean updateResource(java.lang.String resourceId, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return false;
        }

        @java.lang.Override
        public boolean deleteResource(java.lang.String resourceId) throws org.apache.ambari.view.SystemException, org.apache.ambari.view.NoSuchResourceException, org.apache.ambari.view.UnsupportedPropertyException {
            return false;
        }
    }

    public static class MyResourceService {}
}