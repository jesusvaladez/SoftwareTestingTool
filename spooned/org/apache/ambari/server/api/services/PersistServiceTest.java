package org.apache.ambari.server.api.services;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.codehaus.jettison.json.JSONException;
public class PersistServiceTest extends org.apache.ambari.server.RandomPortJerseyTest {
    static java.lang.String PACKAGE_NAME = "org.apache.ambari.server.api.services";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.PersistServiceTest.class);

    com.google.inject.Injector injector;

    protected com.sun.jersey.api.client.Client client;

    public PersistServiceTest() {
        super(new com.sun.jersey.test.framework.WebAppDescriptor.Builder(org.apache.ambari.server.api.services.PersistServiceTest.PACKAGE_NAME).servletClass(com.sun.jersey.spi.container.servlet.ServletContainer.class).initParam("com.sun.jersey.api.json.POJOMappingFeature", "true").build());
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            requestStaticInjection(org.apache.ambari.server.api.services.PersistKeyValueService.class);
        }
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule(), new org.apache.ambari.server.api.services.PersistServiceTest.MockModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @java.lang.Override
    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        super.tearDown();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testPersist() throws com.sun.jersey.api.client.UniformInterfaceException, org.codehaus.jettison.json.JSONException, java.io.IOException {
        com.sun.jersey.api.client.config.ClientConfig clientConfig = new com.sun.jersey.api.client.config.DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, java.lang.Boolean.TRUE);
        client = com.sun.jersey.api.client.Client.create(clientConfig);
        com.sun.jersey.api.client.WebResource webResource = client.resource(java.lang.String.format("http://localhost:%d/persist", getTestPort()));
        webResource.post("{\"xyx\" : \"t\"}");
        org.apache.ambari.server.api.services.PersistServiceTest.LOG.info("Done posting to the server");
        java.lang.String output = webResource.get(java.lang.String.class);
        org.apache.ambari.server.api.services.PersistServiceTest.LOG.info("All key values " + output);
        java.util.Map<java.lang.String, java.lang.String> jsonOutput = org.apache.ambari.server.utils.StageUtils.fromJson(output, java.util.Map.class);
        java.lang.String value = jsonOutput.get("xyx");
        junit.framework.Assert.assertEquals("t", value);
        webResource = client.resource(java.lang.String.format("http://localhost:%d/persist/xyx", getTestPort()));
        output = webResource.get(java.lang.String.class);
        junit.framework.Assert.assertEquals("t", output);
        org.apache.ambari.server.api.services.PersistServiceTest.LOG.info("Value for xyx " + output);
    }
}