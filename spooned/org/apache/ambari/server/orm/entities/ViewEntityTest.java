package org.apache.ambari.server.orm.entities;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewEntityTest {
    private static java.lang.String with_ambari_versions = "<view>\n" + ((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <min-ambari-version>1.6.1</min-ambari-version>\n") + "    <max-ambari-version>2.0.0</max-ambari-version>\n") + "</view>");

    public static org.apache.ambari.server.orm.entities.ViewEntity getViewEntity() throws java.lang.Exception {
        return org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig());
    }

    public static org.apache.ambari.server.orm.entities.ViewEntity getViewEntity(org.apache.ambari.server.view.configuration.ViewConfig viewConfig) throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        properties.put("p2", "v2");
        properties.put("p3", "v3");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, ambariConfig, "view.jar");
        viewEntity.setClassLoader(org.apache.ambari.server.orm.entities.ViewEntityTest.class.getClassLoader());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName(viewEntity.getName());
        viewEntity.setResourceType(resourceTypeEntity);
        long id = 20L;
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewEntity.getInstances()) {
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
            resourceEntity.setId(id++);
            resourceEntity.setResourceType(resourceTypeEntity);
            viewInstanceEntity.setResource(resourceEntity);
        }
        return viewEntity;
    }

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("MY_VIEW{1.0.0}", viewDefinition.getName());
    }

    @org.junit.Test
    public void testGetCommonName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("MY_VIEW", viewDefinition.getCommonName());
    }

    @org.junit.Test
    public void testGetLabel() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("My View!", viewDefinition.getLabel());
    }

    @org.junit.Test
    public void testGetVersion() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("1.0.0", viewDefinition.getVersion());
    }

    @org.junit.Test
    public void testGetBuild() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("99", viewDefinition.getBuild());
    }

    @org.junit.Test
    public void testGetIcon() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon.png", viewDefinition.getIcon());
        viewDefinition.setIcon("/a/different/icon.png");
        org.junit.Assert.assertEquals("/a/different/icon.png", viewDefinition.getIcon());
    }

    @org.junit.Test
    public void testGetIcon64() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("/this/is/the/icon/url/icon64.png", viewDefinition.getIcon64());
        viewDefinition.setIcon64("/a/different/icon.png");
        org.junit.Assert.assertEquals("/a/different/icon.png", viewDefinition.getIcon64());
    }

    @org.junit.Test
    public void testSetGetConfiguration() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(viewConfig);
        org.junit.Assert.assertEquals(viewConfig, viewDefinition.getConfiguration());
        org.apache.ambari.server.view.configuration.ViewConfig newViewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewEntityTest.with_ambari_versions);
        viewDefinition.setConfiguration(newViewConfig);
        org.junit.Assert.assertEquals(newViewConfig, viewDefinition.getConfiguration());
    }

    @org.junit.Test
    public void testIsClusterConfigurable() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(viewConfig);
        org.junit.Assert.assertEquals(viewConfig, viewDefinition.getConfiguration());
        org.apache.ambari.server.view.configuration.ViewConfig newViewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        viewDefinition.setConfiguration(newViewConfig);
        org.junit.Assert.assertTrue(viewDefinition.isClusterConfigurable());
        newViewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.orm.entities.ViewEntityTest.with_ambari_versions);
        viewDefinition.setConfiguration(newViewConfig);
        org.junit.Assert.assertFalse(viewDefinition.isClusterConfigurable());
    }

    @org.junit.Test
    public void testGetAmbariProperty() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(viewConfig);
        org.junit.Assert.assertEquals("v1", viewDefinition.getAmbariProperty("p1"));
        org.junit.Assert.assertEquals("v2", viewDefinition.getAmbariProperty("p2"));
        org.junit.Assert.assertEquals("v3", viewDefinition.getAmbariProperty("p3"));
    }

    @org.junit.Test
    public void testAddGetResourceProvider() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.controller.spi.ResourceProvider provider1 = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type1 = new org.apache.ambari.server.controller.spi.Resource.Type("myType1");
        viewDefinition.addResourceProvider(type1, provider1);
        org.junit.Assert.assertEquals(provider1, viewDefinition.getResourceProvider(type1));
        org.apache.ambari.server.controller.spi.ResourceProvider provider2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type2 = new org.apache.ambari.server.controller.spi.Resource.Type("myType2");
        viewDefinition.addResourceProvider(type2, provider2);
        org.junit.Assert.assertEquals(provider2, viewDefinition.getResourceProvider(type2));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> types = viewDefinition.getViewResourceTypes();
        org.junit.Assert.assertEquals(2, types.size());
        org.junit.Assert.assertTrue(types.contains(type1));
        org.junit.Assert.assertTrue(types.contains(type2));
    }

    @org.junit.Test
    public void testAddGetResourceDefinition() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.ViewSubResourceDefinition definition = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewSubResourceDefinition.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("myType");
        EasyMock.expect(definition.getType()).andReturn(type);
        EasyMock.replay(definition);
        viewDefinition.addResourceDefinition(definition);
        org.junit.Assert.assertEquals(definition, viewDefinition.getResourceDefinition(type));
        EasyMock.verify(definition);
    }

    @org.junit.Test
    public void testAddGetResourceConfiguration() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.configuration.ResourceConfig config = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs().get(0);
        org.apache.ambari.server.controller.spi.Resource.Type type1 = new org.apache.ambari.server.controller.spi.Resource.Type("myType");
        viewDefinition.addResourceConfiguration(type1, config);
        org.junit.Assert.assertEquals(config, viewDefinition.getResourceConfigurations().get(type1));
        org.apache.ambari.server.controller.spi.Resource.Type type2 = new org.apache.ambari.server.controller.spi.Resource.Type("myType2");
        viewDefinition.addResourceConfiguration(type2, config);
        org.junit.Assert.assertEquals(config, viewDefinition.getResourceConfigurations().get(type2));
    }

    @org.junit.Test
    public void testAddGetInstanceDefinition() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity definition = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(definition.getName()).andReturn("instance1").anyTimes();
        EasyMock.replay(definition);
        viewDefinition.addInstanceDefinition(definition);
        org.junit.Assert.assertEquals(definition, viewDefinition.getInstanceDefinition("instance1"));
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> definitions = viewDefinition.getInstances();
        org.junit.Assert.assertEquals(1, definitions.size());
        org.junit.Assert.assertTrue(definitions.contains(definition));
        EasyMock.verify(definition);
    }

    @org.junit.Test
    public void testGetClassLoader() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.ViewEntityTest.class.getClassLoader(), viewDefinition.getClassLoader());
    }

    @org.junit.Test
    public void testGetArchive() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.junit.Assert.assertEquals("view.jar", viewDefinition.getArchive());
    }

    @org.junit.Test
    public void testGetAmbariConfiguration() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.configuration.Configuration configuration = viewDefinition.getAmbariConfiguration();
        org.junit.Assert.assertEquals("v1", configuration.getProperty("p1"));
        org.junit.Assert.assertEquals("v2", configuration.getProperty("p2"));
        org.junit.Assert.assertEquals("v3", configuration.getProperty("p3"));
    }

    @org.junit.Test
    public void testGetSetStatus() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.PENDING);
        org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.PENDING, viewDefinition.getStatus());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING, viewDefinition.getStatus());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
        org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED, viewDefinition.getStatus());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR);
        org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, viewDefinition.getStatus());
    }

    @org.junit.Test
    public void testGetSetStatusDetail() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setStatusDetail("status detail");
        org.junit.Assert.assertEquals("status detail", viewDefinition.getStatusDetail());
    }

    @org.junit.Test
    public void testGetSetValidator() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.view.validation.Validator validator = new org.apache.ambari.server.orm.entities.ViewEntityTest.TestValidator();
        viewDefinition.setValidator(validator);
        org.junit.Assert.assertEquals(validator, viewDefinition.getValidator());
    }

    @org.junit.Test
    public void testisDeployed() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.PENDING);
        org.junit.Assert.assertFalse(viewDefinition.isDeployed());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.junit.Assert.assertFalse(viewDefinition.isDeployed());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
        org.junit.Assert.assertTrue(viewDefinition.isDeployed());
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR);
        org.junit.Assert.assertFalse(viewDefinition.isDeployed());
    }

    @org.junit.Test
    public void testSetIsSystem() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setSystem(false);
        org.junit.Assert.assertFalse(viewDefinition.isSystem());
        viewDefinition.setSystem(true);
        org.junit.Assert.assertTrue(viewDefinition.isSystem());
    }

    public static class TestValidator implements org.apache.ambari.view.validation.Validator {
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
}