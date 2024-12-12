package org.apache.ambari.server.stack;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosDescriptorTest {
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.KerberosDescriptorTest.class);

    private static final java.util.regex.Pattern PATTERN_KERBEROS_DESCRIPTOR_FILENAME = java.util.regex.Pattern.compile("^kerberos(?:_preconfigure)?\\.json$");

    private static java.io.File stacksDirectory;

    private static java.io.File commonServicesDirectory;

    @org.junit.BeforeClass
    public static void beforeClass() {
        java.net.URL rootDirectoryURL = org.apache.ambari.server.stack.KerberosDescriptorTest.class.getResource("/");
        junit.framework.Assert.assertNotNull(rootDirectoryURL);
        java.io.File resourcesDirectory = new java.io.File(new java.io.File(rootDirectoryURL.getFile()).getParentFile().getParentFile(), "src/main/resources");
        junit.framework.Assert.assertNotNull(resourcesDirectory);
        junit.framework.Assert.assertTrue(resourcesDirectory.canRead());
        org.apache.ambari.server.stack.KerberosDescriptorTest.stacksDirectory = new java.io.File(resourcesDirectory, "stacks");
        junit.framework.Assert.assertNotNull(org.apache.ambari.server.stack.KerberosDescriptorTest.stacksDirectory);
        junit.framework.Assert.assertTrue(org.apache.ambari.server.stack.KerberosDescriptorTest.stacksDirectory.canRead());
        org.apache.ambari.server.stack.KerberosDescriptorTest.commonServicesDirectory = new java.io.File(resourcesDirectory, "common-services");
        junit.framework.Assert.assertNotNull(org.apache.ambari.server.stack.KerberosDescriptorTest.commonServicesDirectory);
        junit.framework.Assert.assertTrue(org.apache.ambari.server.stack.KerberosDescriptorTest.commonServicesDirectory.canRead());
    }

    @org.junit.Test
    public void testCommonServiceDescriptor() throws java.lang.Exception {
        com.networknt.schema.JsonSchema schema = getJsonSchemaFromPath("kerberos_descriptor_schema.json");
        junit.framework.Assert.assertTrue(visitFile(schema, org.apache.ambari.server.stack.KerberosDescriptorTest.commonServicesDirectory, true));
    }

    @org.junit.Test
    public void testStackServiceDescriptor() throws java.lang.Exception {
        com.networknt.schema.JsonSchema schema = getJsonSchemaFromPath("kerberos_descriptor_schema.json");
        junit.framework.Assert.assertTrue(visitFile(schema, org.apache.ambari.server.stack.KerberosDescriptorTest.stacksDirectory, true));
    }

    private boolean visitFile(com.networknt.schema.JsonSchema schema, java.io.File file, boolean previousResult) throws java.lang.Exception {
        if (file.isDirectory()) {
            boolean currentResult = true;
            java.io.File[] files = file.listFiles();
            if (files != null) {
                for (java.io.File currentFile : files) {
                    currentResult = visitFile(schema, currentFile, previousResult) && currentResult;
                }
            }
            return previousResult && currentResult;
        } else if (file.isFile()) {
            if (org.apache.ambari.server.stack.KerberosDescriptorTest.PATTERN_KERBEROS_DESCRIPTOR_FILENAME.matcher(file.getName()).matches()) {
                org.apache.ambari.server.stack.KerberosDescriptorTest.LOG.info("Validating " + file.getAbsolutePath());
                com.fasterxml.jackson.databind.JsonNode node = getJsonNodeFromUrl(file.toURI().toURL().toExternalForm());
                java.util.Set<com.networknt.schema.ValidationMessage> errors = schema.validate(node);
                if ((errors != null) && (!errors.isEmpty())) {
                    for (com.networknt.schema.ValidationMessage message : errors) {
                        org.apache.ambari.server.stack.KerberosDescriptorTest.LOG.error(message.getMessage());
                    }
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }
        return previousResult;
    }

    private com.fasterxml.jackson.databind.JsonNode getJsonNodeFromUrl(java.lang.String url) throws java.lang.Exception {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return mapper.readTree(new java.net.URL(url));
    }

    private com.networknt.schema.JsonSchema getJsonSchemaFromPath(java.lang.String name) throws java.lang.Exception {
        com.networknt.schema.JsonSchemaFactory factory = new com.networknt.schema.JsonSchemaFactory();
        java.io.InputStream is = java.lang.Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        return factory.getSchema(is);
    }
}