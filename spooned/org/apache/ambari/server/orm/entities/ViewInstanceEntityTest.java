package org.apache.ambari.server.orm.entities;
import org.springframework.security.core.GrantedAuthority;
import static org.easymock.EasyMock.createNiceMock;
public class ViewInstanceEntityTest {
    private static java.lang.String xml_with_instance_label = "<view>\n" + (((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <icon>/this/is/the/icon/url/icon.png</icon>\n") + "    <icon64>/this/is/the/icon/url/icon64.png</icon64>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "        <description>This is an instance description.</description>\n") + "        <visible>true</visible>\n") + "        <icon>/this/is/the/icon/url/instance_1_icon.png</icon>\n") + "        <icon64>/this/is/the/icon/url/instance_1_icon64.png</icon64>\n") + "    </instance>\n") + "    <instance>\n") + "        <name>INSTANCE2</name>\n") + "        <label>My Instance 2!</label>\n") + "        <visible>false</visible>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_without_instance_label = "<view>\n" + (((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_valid_instance = "<view>\n" + ((((((((((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <required>false</required>\n") + "    </parameter>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-1</value>\n") + "        </property>\n") + "        <property>\n") + "            <key>p2</key>\n") + "            <value>v2-1</value>\n") + "        </property>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_invalid_instance = "<view>\n" + ((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <required>false</required>\n") + "    </parameter>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_view_with_migrator_v2 = "<view>\n" + ((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>2.0.0</version>\n") + "    <data-version>1</data-version>\n") + "    <data-migrator-class>org.apache.ambari.server.orm.entities.ViewInstanceEntityTest$MyDataMigrator</data-migrator-class>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_view_with_migrator_v1 = "<view>\n" + ((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String XML_CONFIG_INSTANCE = "<view>\n" + (((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <cluster-config>hadoop-env/hdfs_user</cluster-config>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "    </instance>\n") + "</view>");

    @org.junit.Test
    public void testGetViewEntity() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals(viewDefinition, viewInstanceDefinition.getViewEntity());
    }

    @org.junit.Test
    public void testGetConfiguration() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals(instanceConfig, viewInstanceDefinition.getConfiguration());
    }

    @org.junit.Test
    public void testGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.junit.Assert.assertNull(viewInstanceDefinition.getViewInstanceId());
        viewInstanceDefinition.setViewInstanceId(99L);
        org.junit.Assert.assertEquals(99L, ((long) (viewInstanceDefinition.getViewInstanceId())));
    }

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.junit.Assert.assertEquals("INSTANCE1", viewInstanceDefinition.getName());
    }

    @org.junit.Test
    public void testGetLabel() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("My Instance 1!", viewInstanceDefinition.getLabel());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_without_instance_label).get(0);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("My View!", viewInstanceDefinition.getLabel());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("This is an instance description.", viewInstanceDefinition.getDescription());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_without_instance_label).get(0);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertNull(viewInstanceDefinition.getDescription());
    }

    @org.junit.Test
    public void testIsVisible() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertTrue(viewInstanceDefinition.isVisible());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(1);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertFalse(viewInstanceDefinition.isVisible());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_without_instance_label).get(0);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertTrue(viewInstanceDefinition.isVisible());
    }

    @org.junit.Test
    public void testGetIcon() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("/this/is/the/icon/url/instance_1_icon.png", viewInstanceDefinition.getIcon());
        viewInstanceDefinition.setIcon("/a/different/icon.png");
        org.junit.Assert.assertEquals("/a/different/icon.png", viewInstanceDefinition.getIcon());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(1);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon.png", viewInstanceDefinition.getIcon());
    }

    @org.junit.Test
    public void testAlterNames() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertTrue(viewInstanceDefinition.alterNames());
        viewInstanceDefinition.setAlterNames(false);
        org.junit.Assert.assertFalse(viewInstanceDefinition.alterNames());
        viewInstanceDefinition.setAlterNames(true);
        org.junit.Assert.assertTrue(viewInstanceDefinition.alterNames());
    }

    @org.junit.Test
    public void testGetIcon64() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("/this/is/the/icon/url/instance_1_icon64.png", viewInstanceDefinition.getIcon64());
        viewInstanceDefinition.setIcon64("/a/different/icon.png");
        org.junit.Assert.assertEquals("/a/different/icon.png", viewInstanceDefinition.getIcon64());
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_with_instance_label).get(1);
        viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon64.png", viewInstanceDefinition.getIcon64());
    }

    @org.junit.Test
    public void testAddGetProperty() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        viewInstanceDefinition.putProperty("p1", "v1");
        viewInstanceDefinition.putProperty("p2", "v2");
        viewInstanceDefinition.putProperty("p3", "v3");
        java.util.Map<java.lang.String, java.lang.String> properties = viewInstanceDefinition.getPropertyMap();
        org.junit.Assert.assertEquals(3, properties.size());
        org.junit.Assert.assertEquals("v1", properties.get("p1"));
        org.junit.Assert.assertEquals("v2", properties.get("p2"));
        org.junit.Assert.assertEquals("v3", properties.get("p3"));
    }

    @org.junit.Test
    public void testAddGetService() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        java.lang.Object service = new java.lang.Object();
        viewInstanceDefinition.addService("resources", service);
        java.lang.Object service2 = new java.lang.Object();
        viewInstanceDefinition.addService("subresources", service2);
        org.junit.Assert.assertEquals(service, viewInstanceDefinition.getService("resources"));
        org.junit.Assert.assertEquals(service2, viewInstanceDefinition.getService("subresources"));
    }

    @org.junit.Test
    public void testAddGetResourceProvider() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.view.ResourceProvider provider = EasyMock.createNiceMock(org.apache.ambari.view.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW{1.0.0}/myType");
        viewInstanceDefinition.addResourceProvider(type, provider);
        org.junit.Assert.assertEquals(provider, viewInstanceDefinition.getResourceProvider(type));
        org.junit.Assert.assertEquals(provider, viewInstanceDefinition.getResourceProvider("myType"));
    }

    @org.junit.Test
    public void testContextPath() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_PATH_PREFIX + "MY_VIEW/1.0.0/INSTANCE1", viewInstanceDefinition.getContextPath());
    }

    @org.junit.Test
    public void testInstanceData() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.TestSecurityHelper securityHelper = new org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.TestSecurityHelper("user1");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity(securityHelper);
        viewInstanceDefinition.putInstanceData("key1", "foo");
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntity = viewInstanceDefinition.getInstanceData("key1");
        org.junit.Assert.assertNotNull(dataEntity);
        org.junit.Assert.assertEquals("foo", dataEntity.getValue());
        org.junit.Assert.assertEquals("user1", dataEntity.getUser());
        viewInstanceDefinition.putInstanceData("key2", "bar");
        viewInstanceDefinition.putInstanceData("key3", "baz");
        viewInstanceDefinition.putInstanceData("key4", "monkey");
        viewInstanceDefinition.putInstanceData("key5", "runner");
        java.util.Map<java.lang.String, java.lang.String> dataMap = viewInstanceDefinition.getInstanceDataMap();
        org.junit.Assert.assertEquals(5, dataMap.size());
        org.junit.Assert.assertEquals("foo", dataMap.get("key1"));
        org.junit.Assert.assertEquals("bar", dataMap.get("key2"));
        org.junit.Assert.assertEquals("baz", dataMap.get("key3"));
        org.junit.Assert.assertEquals("monkey", dataMap.get("key4"));
        org.junit.Assert.assertEquals("runner", dataMap.get("key5"));
        viewInstanceDefinition.removeInstanceData("key3");
        dataMap = viewInstanceDefinition.getInstanceDataMap();
        org.junit.Assert.assertEquals(4, dataMap.size());
        org.junit.Assert.assertFalse(dataMap.containsKey("key3"));
        securityHelper.setUser("user2");
        dataMap = viewInstanceDefinition.getInstanceDataMap();
        org.junit.Assert.assertTrue(dataMap.isEmpty());
        viewInstanceDefinition.putInstanceData("key1", "aaa");
        viewInstanceDefinition.putInstanceData("key2", "bbb");
        viewInstanceDefinition.putInstanceData("key3", "ccc");
        dataMap = viewInstanceDefinition.getInstanceDataMap();
        org.junit.Assert.assertEquals(3, dataMap.size());
        org.junit.Assert.assertEquals("aaa", dataMap.get("key1"));
        org.junit.Assert.assertEquals("bbb", dataMap.get("key2"));
        org.junit.Assert.assertEquals("ccc", dataMap.get("key3"));
        securityHelper.setUser("user1");
        dataMap = viewInstanceDefinition.getInstanceDataMap();
        org.junit.Assert.assertEquals(4, dataMap.size());
        org.junit.Assert.assertEquals("foo", dataMap.get("key1"));
        org.junit.Assert.assertEquals("bar", dataMap.get("key2"));
        org.junit.Assert.assertNull(dataMap.get("key3"));
        org.junit.Assert.assertEquals("monkey", dataMap.get("key4"));
        org.junit.Assert.assertEquals("runner", dataMap.get("key5"));
    }

    public static org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName(viewDefinition.getName());
        viewDefinition.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(20L);
        resourceEntity.setResourceType(resourceTypeEntity);
        viewInstanceEntity.setResource(resourceEntity);
        return viewInstanceEntity;
    }

    public static java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> getViewInstanceEntities(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> entities = new java.util.HashSet<>();
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        entities.add(new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig));
        instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(1);
        entities.add(new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig));
        return entities;
    }

    @org.junit.Test
    public void testValidate() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_valid_instance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        viewInstanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
    }

    @org.junit.Test
    public void testDataMigrator() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration();
        org.apache.ambari.server.view.configuration.ViewConfig config2 = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_view_with_migrator_v2);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity2 = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config2, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity2 = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity2, config2.getInstances().get(0));
        org.apache.ambari.server.view.configuration.ViewConfig config1 = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_view_with_migrator_v1);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity1 = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config1, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity1 = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity1, config1.getInstances().get(0));
        org.apache.ambari.view.migration.ViewDataMigrationContext context = new org.apache.ambari.server.view.ViewDataMigrationContextImpl(viewInstanceEntity1, viewInstanceEntity2);
        org.apache.ambari.view.migration.ViewDataMigrator migrator2 = viewInstanceEntity2.getDataMigrator(context);
        org.junit.Assert.assertTrue(migrator2 instanceof org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.MyDataMigrator);
        org.apache.ambari.view.migration.ViewDataMigrator migrator1 = viewInstanceEntity1.getDataMigrator(context);
        org.junit.Assert.assertNull(migrator1);
    }

    @org.junit.Test
    public void testValidateWithClusterConfig() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.XML_CONFIG_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        viewInstanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
    }

    @org.junit.Test
    public void testValidateWithValidator() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_valid_instance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator validator = new org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator();
        validator.result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        viewEntity.setValidator(validator);
        viewInstanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
    }

    @org.junit.Test
    public void testValidate_fail() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_invalid_instance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        try {
            viewInstanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
            org.junit.Assert.fail("Expected an IllegalStateException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
    }

    @org.junit.Test
    public void testValidateWithValidator_fail() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_invalid_instance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator validator = new org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator();
        validator.result = new org.apache.ambari.server.view.validation.ValidationResultImpl(false, "detail");
        viewEntity.setValidator(validator);
        try {
            viewInstanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
            org.junit.Assert.fail("Expected an IllegalStateException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
    }

    @org.junit.Test
    public void testGetValidationResult() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.xml_valid_instance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator validator = new org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator();
        validator.result = new org.apache.ambari.server.view.validation.ValidationResultImpl(true, "detail");
        viewEntity.setValidator(validator);
        org.apache.ambari.server.view.validation.InstanceValidationResultImpl result = viewInstanceEntity.getValidationResult(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
        java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults = result.getPropertyResults();
        junit.framework.Assert.assertEquals(2, propertyResults.size());
        junit.framework.Assert.assertTrue(propertyResults.containsKey("p1"));
        junit.framework.Assert.assertTrue(propertyResults.containsKey("p2"));
        junit.framework.Assert.assertTrue(propertyResults.get("p1").isValid());
        junit.framework.Assert.assertTrue(propertyResults.get("p2").isValid());
    }

    public static org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity(org.apache.ambari.server.security.SecurityHelper securityHelper) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        viewInstanceEntity.setSecurityHelper(securityHelper);
        return viewInstanceEntity;
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

    protected static class TestSecurityHelper implements org.apache.ambari.server.security.SecurityHelper {
        private java.lang.String user;

        public TestSecurityHelper(java.lang.String user) {
            this.user = user;
        }

        public void setUser(java.lang.String user) {
            this.user = user;
        }

        @java.lang.Override
        public java.lang.String getCurrentUserName() {
            return user;
        }

        @java.lang.Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getCurrentAuthorities() {
            return java.util.Collections.emptyList();
        }
    }
}