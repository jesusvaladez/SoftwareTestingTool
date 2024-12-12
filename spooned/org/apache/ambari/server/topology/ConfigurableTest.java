package org.apache.ambari.server.topology;
public class ConfigurableTest {
    public static final java.lang.String JSON_LOCATION = "add_service_api/configurable.json";

    public static final java.lang.String JSON_LOCATION2 = "add_service_api/configurable2.json";

    public static final java.lang.String JSON_LOCATION3 = "add_service_api/configurable3.json";

    public static final java.lang.String INVALID_CONFIGS_LOCATION = "add_service_api/invalid_configurables.txt";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.ConfigurableTest.class);

    @org.junit.Test
    public void testParseConfigurable() throws java.lang.Exception {
        org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable configurable = new com.fasterxml.jackson.databind.ObjectMapper().readValue(com.google.common.io.Resources.getResource(org.apache.ambari.server.topology.ConfigurableTest.JSON_LOCATION), org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("zoo.cfg", com.google.common.collect.ImmutableMap.of("dataDir", "/zookeeper1")), configurable.getConfiguration().getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("zoo.cfg", com.google.common.collect.ImmutableMap.of("final", com.google.common.collect.ImmutableMap.of("someProp", "true"))), configurable.getConfiguration().getAttributes());
    }

    @org.junit.Test
    public void parseInvalidConfigurables() throws java.lang.Exception {
        java.lang.String invalidConfigsTxt = com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource(org.apache.ambari.server.topology.ConfigurableTest.INVALID_CONFIGS_LOCATION), java.nio.charset.StandardCharsets.UTF_8);
        java.util.List<java.lang.String> invalidConfigs = com.google.common.base.Splitter.on(java.util.regex.Pattern.compile("\'\'\'(.|[\\r\\n])*?\'\'\'|#.*$", java.util.regex.Pattern.MULTILINE)).omitEmptyStrings().trimResults().splitToList(invalidConfigsTxt);
        for (java.lang.String config : invalidConfigs) {
            org.apache.ambari.server.topology.ConfigurableTest.LOG.info("Invalid config to parse:\n{}", config);
            try {
                new com.fasterxml.jackson.databind.ObjectMapper().readValue(config, org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
                org.junit.Assert.fail((("Expected " + com.fasterxml.jackson.core.JsonProcessingException.class.getSimpleName()) + " while processing config:\n") + config);
            } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
                java.lang.Throwable rootCause = org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(ex);
                org.apache.ambari.server.topology.ConfigurableTest.LOG.info("Error message: {}", rootCause.getMessage());
                org.junit.Assert.assertTrue((((("Expected " + java.lang.IllegalArgumentException.class.getSimpleName()) + " during parsing JSON:\n") + config) + "\n found: ") + rootCause, rootCause instanceof java.lang.IllegalArgumentException);
            }
        }
    }

    @org.junit.Test
    public void testSerializeDeserialize() throws java.lang.Exception {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable configurable = mapper.readValue(com.google.common.io.Resources.getResource(org.apache.ambari.server.topology.ConfigurableTest.JSON_LOCATION), org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
        java.lang.String persisted = mapper.writeValueAsString(configurable);
        org.apache.ambari.server.topology.Configurable restored = mapper.readValue(persisted, org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
        org.junit.Assert.assertEquals(configurable.getConfiguration().getProperties(), restored.getConfiguration().getProperties());
        org.junit.Assert.assertEquals(configurable.getConfiguration().getAttributes(), restored.getConfiguration().getAttributes());
    }

    @org.junit.Test
    public void testParseConfigurableFromResourceManager() throws java.lang.Exception {
        java.net.URL url = com.google.common.io.Resources.getResource(org.apache.ambari.server.topology.ConfigurableTest.JSON_LOCATION2);
        org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable configurable = new com.fasterxml.jackson.databind.ObjectMapper().readValue(url, org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("zoo.cfg", com.google.common.collect.ImmutableMap.of("dataDir", "/zookeeper1")), configurable.getConfiguration().getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("zoo.cfg", com.google.common.collect.ImmutableMap.of("final", com.google.common.collect.ImmutableMap.of("someProp", "true"))), configurable.getConfiguration().getAttributes());
    }

    @org.junit.Test
    public void testParseLegacyConfigurable() throws java.lang.Exception {
        java.net.URL url = com.google.common.io.Resources.getResource(org.apache.ambari.server.topology.ConfigurableTest.JSON_LOCATION3);
        org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable configurable = new com.fasterxml.jackson.databind.ObjectMapper().readValue(url, org.apache.ambari.server.topology.ConfigurableTest.TestConfigurable.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("cluster-env", com.google.common.collect.ImmutableMap.of("dataDir", "/zookeeper1", "custom-property", "true")), configurable.getConfiguration().getProperties());
    }

    @org.junit.Test
    public void testTransformAttributesMap() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = com.google.common.collect.ImmutableMap.of("propertyName1", com.google.common.collect.ImmutableMap.of("minimum", "3000", "maximum", "4000"), "propertyName2", com.google.common.collect.ImmutableMap.of("minimum", "3500", "maximum", "4500", "hidden", "true"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> transformed = com.google.common.collect.ImmutableMap.of("minimum", com.google.common.collect.ImmutableMap.of("propertyName1", "3000", "propertyName2", "3500"), "maximum", com.google.common.collect.ImmutableMap.of("propertyName1", "4000", "propertyName2", "4500"), "hidden", com.google.common.collect.ImmutableMap.of("propertyName2", "true"));
        org.junit.Assert.assertEquals(transformed, org.apache.ambari.server.topology.ConfigurableHelper.transformAttributesMap(attributes));
        org.junit.Assert.assertEquals(attributes, org.apache.ambari.server.topology.ConfigurableHelper.transformAttributesMap(transformed));
    }

    static class TestConfigurable implements org.apache.ambari.server.topology.Configurable {
        org.apache.ambari.server.topology.Configuration configuration;

        @java.lang.Override
        public org.apache.ambari.server.topology.Configuration getConfiguration() {
            return configuration;
        }

        @java.lang.Override
        public void setConfiguration(org.apache.ambari.server.topology.Configuration configuration) {
            this.configuration = configuration;
        }
    }
}