package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class ParameterConfigTest {
    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals("p1", parameters.get(0).getName());
        org.junit.Assert.assertEquals("p2", parameters.get(1).getName());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals("Parameter 1.", parameters.get(0).getDescription());
        org.junit.Assert.assertEquals("Parameter 2.", parameters.get(1).getDescription());
    }

    @org.junit.Test
    public void testGetLabel() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals("Label 1.", parameters.get(0).getLabel());
        org.junit.Assert.assertNull(parameters.get(1).getLabel());
    }

    @org.junit.Test
    public void testGetPlaceholder() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals("Placeholder 1.", parameters.get(0).getPlaceholder());
        org.junit.Assert.assertNull(parameters.get(1).getPlaceholder());
    }

    @org.junit.Test
    public void testGetDefaultValue() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertNull(parameters.get(0).getDefaultValue());
        org.junit.Assert.assertEquals("Default value 1.", parameters.get(1).getDefaultValue());
    }

    @org.junit.Test
    public void testGetClusterConfig() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertNull(parameters.get(0).getClusterConfig());
        org.junit.Assert.assertEquals("hdfs-site/dfs.namenode.http-address", parameters.get(1).getClusterConfig());
    }

    @org.junit.Test
    public void testIsRequired() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals(true, parameters.get(0).isRequired());
        org.junit.Assert.assertEquals(false, parameters.get(1).isRequired());
    }

    @org.junit.Test
    public void testIsMasked() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = org.apache.ambari.server.view.configuration.ParameterConfigTest.getParameterConfigs();
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertEquals(false, parameters.get(0).isMasked());
        org.junit.Assert.assertEquals(true, parameters.get(1).isMasked());
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> getParameterConfigs() throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        return viewConfig.getParameters();
    }
}