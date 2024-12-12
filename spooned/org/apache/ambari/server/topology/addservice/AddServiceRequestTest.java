package org.apache.ambari.server.topology.addservice;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY;
import static org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY;
import static org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY;
import static org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE;
import static org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.PERMISSIVE;
import static org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.STRICT;
public class AddServiceRequestTest {
    private static java.lang.String REQUEST_ALL_FIELDS_SET;

    private static java.lang.String REQUEST_MINIMAL_SERVICES_AND_COMPONENTS;

    private static java.lang.String REQUEST_MINIMAL_SERVICES_ONLY;

    private static java.lang.String REQUEST_MINIMAL_COMPONENTS_ONLY;

    private static java.lang.String REQUEST_INVALID_NO_SERVICES_AND_COMPONENTS;

    private static java.lang.String REQUEST_INVALID_INVALID_FIELD;

    private static java.lang.String REQUEST_INVALID_INVALID_CONFIG;

    private static final java.util.Map<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> KERBEROS_DESCRIPTOR1 = com.google.common.collect.ImmutableMap.of("services", com.google.common.collect.ImmutableList.of(com.google.common.collect.ImmutableMap.of("name", "ZOOKEEPER")));

    private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    @org.junit.BeforeClass
    public static void setUpClass() {
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_ALL_FIELDS_SET = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request1.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_SERVICES_AND_COMPONENTS = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request2.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_SERVICES_ONLY = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request3.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_COMPONENTS_ONLY = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request4.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_NO_SERVICES_AND_COMPONENTS = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request_invalid_1.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_INVALID_FIELD = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request_invalid_2.json");
        org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_INVALID_CONFIG = org.apache.ambari.server.topology.addservice.AddServiceRequestTest.read("add_service_api/request_invalid_3.json");
    }

    @org.junit.Test
    public void testDeserialize_basic() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_ALL_FIELDS_SET, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE, request.getOperationType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY, request.getRecommendationStrategy());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY, request.getProvisionAction());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.PERMISSIVE, request.getValidationType());
        org.junit.Assert.assertEquals("HDP", request.getStackName());
        org.junit.Assert.assertEquals("3.0", request.getStackVersion());
        org.apache.ambari.server.topology.Configuration configuration = request.getConfiguration();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("storm-site", com.google.common.collect.ImmutableMap.of("final", com.google.common.collect.ImmutableMap.of("fs.defaultFS", "true"))), configuration.getAttributes());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("storm-site", com.google.common.collect.ImmutableMap.of("ipc.client.connect.max.retries", "50")), configuration.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Component.of("NIMBUS", org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY, "c7401.ambari.apache.org", "c7402.ambari.apache.org"), org.apache.ambari.server.topology.addservice.Component.of("BEACON_SERVER", "c7402.ambari.apache.org", "c7403.ambari.apache.org")), request.getComponents());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Service.of("STORM", org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START), org.apache.ambari.server.topology.addservice.Service.of("BEACON")), request.getServices());
        org.junit.Assert.assertEquals(java.util.Optional.of(org.apache.ambari.server.topology.SecurityConfiguration.forTest(org.apache.ambari.server.state.SecurityType.KERBEROS, "ref_to_kerb_desc", org.apache.ambari.server.topology.addservice.AddServiceRequestTest.KERBEROS_DESCRIPTOR1)), request.getSecurity());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kdc.admin.credential", new org.apache.ambari.server.topology.Credential("kdc.admin.credential", "admin/admin@EXAMPLE.COM", "k", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)), request.getCredentials());
    }

    @org.junit.Test
    public void testDeserialize_defaultAndEmptyValues() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_SERVICES_AND_COMPONENTS, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Component.of("NIMBUS", "c7401.ambari.apache.org", "c7402.ambari.apache.org"), org.apache.ambari.server.topology.addservice.Component.of("BEACON_SERVER", "c7402.ambari.apache.org", "c7403.ambari.apache.org")), request.getComponents());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Service.of("STORM"), org.apache.ambari.server.topology.addservice.Service.of("BEACON")), request.getServices());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE, request.getOperationType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigRecommendationStrategy.defaultForAddService(), request.getRecommendationStrategy());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, request.getProvisionAction());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.STRICT, request.getValidationType());
        org.junit.Assert.assertNull(request.getStackName());
        org.junit.Assert.assertNull(request.getStackVersion());
        org.junit.Assert.assertEquals(java.util.Optional.empty(), request.getSecurity());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of(), request.getCredentials());
        org.apache.ambari.server.topology.Configuration configuration = request.getConfiguration();
        org.junit.Assert.assertTrue(configuration.getFullAttributes().isEmpty());
        org.junit.Assert.assertTrue(configuration.getFullProperties().isEmpty());
    }

    @org.junit.Test
    public void testDeserialize_onlyServices() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_SERVICES_ONLY, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Service.of("STORM"), org.apache.ambari.server.topology.addservice.Service.of("BEACON")), request.getServices());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE, request.getOperationType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigRecommendationStrategy.defaultForAddService(), request.getRecommendationStrategy());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, request.getProvisionAction());
        org.junit.Assert.assertNull(request.getStackName());
        org.junit.Assert.assertNull(request.getStackVersion());
        org.apache.ambari.server.topology.Configuration configuration = request.getConfiguration();
        org.junit.Assert.assertTrue(configuration.getFullAttributes().isEmpty());
        org.junit.Assert.assertTrue(configuration.getFullProperties().isEmpty());
        org.junit.Assert.assertTrue(request.getComponents().isEmpty());
    }

    @org.junit.Test
    public void testDeserialize_onlyComponents() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_COMPONENTS_ONLY, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.topology.addservice.Component.of("NIMBUS", "c7401.ambari.apache.org", "c7402.ambari.apache.org"), org.apache.ambari.server.topology.addservice.Component.of("BEACON_SERVER", "c7402.ambari.apache.org", "c7403.ambari.apache.org")), request.getComponents());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE, request.getOperationType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigRecommendationStrategy.defaultForAddService(), request.getRecommendationStrategy());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START, request.getProvisionAction());
        org.junit.Assert.assertNull(request.getStackName());
        org.junit.Assert.assertNull(request.getStackVersion());
        org.apache.ambari.server.topology.Configuration configuration = request.getConfiguration();
        org.junit.Assert.assertTrue(configuration.getFullAttributes().isEmpty());
        org.junit.Assert.assertTrue(configuration.getFullProperties().isEmpty());
        org.junit.Assert.assertTrue(request.getServices().isEmpty());
    }

    @org.junit.Test
    public void testDeserialize_invalid_noServicesAndComponents() throws java.lang.Exception {
        mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_NO_SERVICES_AND_COMPONENTS, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
    }

    @org.junit.Test(expected = com.fasterxml.jackson.core.JsonProcessingException.class)
    public void testDeserialize_invalid_invalidField() throws java.lang.Exception {
        mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_INVALID_FIELD, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
    }

    @org.junit.Test(expected = com.fasterxml.jackson.core.JsonProcessingException.class)
    public void testDeserialize_invalid_invalidConfiguration() throws java.lang.Exception {
        mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_INVALID_INVALID_CONFIG, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
    }

    @org.junit.Test
    public void testSerialize_basic() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_ALL_FIELDS_SET, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.apache.ambari.server.topology.addservice.AddServiceRequest serialized = serialize(request);
        org.junit.Assert.assertEquals(request, serialized);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of(), serialized.getCredentials());
    }

    @org.junit.Test
    public void testSerialize_EmptyOmitted() throws java.lang.Exception {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = mapper.readValue(org.apache.ambari.server.topology.addservice.AddServiceRequestTest.REQUEST_MINIMAL_SERVICES_ONLY, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        org.apache.ambari.server.topology.addservice.AddServiceRequest serialized = serialize(request);
        org.junit.Assert.assertEquals(request, serialized);
    }

    private org.apache.ambari.server.topology.addservice.AddServiceRequest serialize(org.apache.ambari.server.topology.addservice.AddServiceRequest request) throws java.io.IOException {
        java.lang.String serialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        return mapper.readValue(serialized, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
    }

    private static java.lang.String read(java.lang.String resourceName) {
        try {
            return com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource(resourceName), java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }
}