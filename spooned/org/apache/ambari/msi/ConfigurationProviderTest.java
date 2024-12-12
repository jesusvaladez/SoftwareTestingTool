package org.apache.ambari.msi;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.*;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.msi.ConfigurationProvider.class, com.thoughtworks.xstream.io.xml.StaxDriver.class, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class, java.io.InputStream.class })
public class ConfigurationProviderTest {
    @org.junit.Test
    public void testConfigurationProvider_init_method_file_doesnt_exists() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinitionMock = createStrictMock(org.apache.ambari.msi.ClusterDefinition.class);
        org.powermock.api.easymock.PowerMock.suppress(org.powermock.api.easymock.PowerMock.methods(org.apache.ambari.msi.ConfigurationProvider.class, "initConfigurationResources"));
        com.thoughtworks.xstream.io.xml.StaxDriver staxDriver = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.io.xml.StaxDriver.class);
        com.thoughtworks.xstream.XStream xstream = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.XStream.class);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.io.xml.StaxDriver.class).andReturn(staxDriver);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.XStream.class, staxDriver).andReturn(xstream);
        xstream.alias("configuration", java.util.Map.class);
        expectLastCall();
        xstream.registerConverter(anyObject(org.apache.ambari.msi.ConfigurationProvider.ScomConfigConverter.class));
        expectLastCall();
        org.powermock.api.easymock.PowerMock.replay(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class);
        replay(clusterDefinitionMock);
        new org.apache.ambari.msi.ConfigurationProvider(clusterDefinitionMock);
        org.powermock.api.easymock.PowerMock.verify(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class);
        verify(clusterDefinitionMock);
    }

    @org.junit.Test
    public void testConfigurationProvider_init_method_file_exists() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinitionMock = createStrictMock(org.apache.ambari.msi.ClusterDefinition.class);
        org.powermock.api.easymock.PowerMock.suppress(org.powermock.api.easymock.PowerMock.methods(org.apache.ambari.msi.ConfigurationProvider.class, "initConfigurationResources"));
        com.thoughtworks.xstream.io.xml.StaxDriver staxDriver = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.io.xml.StaxDriver.class);
        com.thoughtworks.xstream.XStream xstream = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.XStream.class);
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.ClassLoader.class);
        java.io.InputStream mockInputStream = createMock(java.io.InputStream.class);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.io.xml.StaxDriver.class).andReturn(staxDriver);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.XStream.class, staxDriver).andReturn(xstream);
        xstream.alias("configuration", java.util.Map.class);
        expectLastCall();
        xstream.registerConverter(anyObject(org.apache.ambari.msi.ConfigurationProvider.ScomConfigConverter.class));
        expectLastCall();
        expect(java.lang.ClassLoader.getSystemResourceAsStream(anyObject(java.lang.String.class))).andReturn(mockInputStream).times(5);
        expect(xstream.fromXML(mockInputStream)).andReturn(new java.util.HashMap<java.lang.String, java.lang.String>()).times(5);
        org.powermock.api.easymock.PowerMock.replay(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        replay(clusterDefinitionMock, mockInputStream);
        new org.apache.ambari.msi.ConfigurationProvider(clusterDefinitionMock);
        org.powermock.api.easymock.PowerMock.verify(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        verify(clusterDefinitionMock, mockInputStream);
    }

    @org.junit.Test
    public void testConfigurationProvider_initConfigurationResources_method() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinitionMock = createStrictMock(org.apache.ambari.msi.ClusterDefinition.class);
        com.thoughtworks.xstream.io.xml.StaxDriver staxDriver = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.io.xml.StaxDriver.class);
        com.thoughtworks.xstream.XStream xstream = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.XStream.class);
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.ClassLoader.class);
        java.io.InputStream mockInputStream = createMock(java.io.InputStream.class);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.io.xml.StaxDriver.class).andReturn(staxDriver);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.XStream.class, staxDriver).andReturn(xstream);
        xstream.alias("configuration", java.util.Map.class);
        expectLastCall();
        xstream.registerConverter(anyObject(org.apache.ambari.msi.ConfigurationProvider.ScomConfigConverter.class));
        expectLastCall();
        expect(java.lang.ClassLoader.getSystemResourceAsStream(anyObject(java.lang.String.class))).andReturn(mockInputStream).times(5);
        expect(xstream.fromXML(mockInputStream)).andReturn(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("property_key", "propery_value");
            }
        }).times(5);
        expect(clusterDefinitionMock.getClusterName()).andReturn("ambari");
        org.powermock.api.easymock.PowerMock.replay(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        replay(clusterDefinitionMock, mockInputStream);
        org.apache.ambari.msi.ConfigurationProvider configurationProvider = new org.apache.ambari.msi.ConfigurationProvider(clusterDefinitionMock);
        org.powermock.api.easymock.PowerMock.verify(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        verify(clusterDefinitionMock, mockInputStream);
        org.junit.Assert.assertEquals(5, configurationProvider.getResources().size());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicate() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinitionMock = createStrictMock(org.apache.ambari.msi.ClusterDefinition.class);
        com.thoughtworks.xstream.io.xml.StaxDriver staxDriver = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.io.xml.StaxDriver.class);
        com.thoughtworks.xstream.XStream xstream = org.powermock.api.easymock.PowerMock.createStrictMock(com.thoughtworks.xstream.XStream.class);
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.ClassLoader.class);
        java.io.InputStream mockInputStream = createMock(java.io.InputStream.class);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.io.xml.StaxDriver.class).andReturn(staxDriver);
        org.powermock.api.easymock.PowerMock.expectNew(com.thoughtworks.xstream.XStream.class, staxDriver).andReturn(xstream);
        xstream.alias("configuration", java.util.Map.class);
        expectLastCall();
        xstream.registerConverter(anyObject(org.apache.ambari.msi.ConfigurationProvider.ScomConfigConverter.class));
        expectLastCall();
        expect(java.lang.ClassLoader.getSystemResourceAsStream(anyObject(java.lang.String.class))).andReturn(mockInputStream).times(5);
        expect(xstream.fromXML(mockInputStream)).andReturn(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("property_key", "propery_value");
            }
        }).times(5);
        expect(clusterDefinitionMock.getClusterName()).andReturn("ambari");
        org.powermock.api.easymock.PowerMock.replay(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        replay(clusterDefinitionMock, mockInputStream);
        org.apache.ambari.msi.ConfigurationProvider configurationProvider = new org.apache.ambari.msi.ConfigurationProvider(clusterDefinitionMock);
        org.powermock.api.easymock.PowerMock.verify(staxDriver, com.thoughtworks.xstream.io.xml.StaxDriver.class, xstream, com.thoughtworks.xstream.XStream.class, java.lang.ClassLoader.class);
        verify(clusterDefinitionMock, mockInputStream);
        org.apache.ambari.server.controller.spi.Predicate configPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CLUSTER_NAME_PROPERTY_ID).equals("ambari").and().property(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CONFIG_TYPE_PROPERTY_ID).equals("yarn-site").and().property(org.apache.ambari.msi.ConfigurationProvider.CONFIGURATION_CONFIG_TAG_PROPERTY_ID).equals("version1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = configurationProvider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), configPredicate);
        org.junit.Assert.assertNotNull(resources);
        org.junit.Assert.assertEquals(1, resources.size());
    }
}