package org.apache.ambari.server.api.services.stackadvisor.commands;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class StackAdvisorCommandTest {
    private static final java.lang.String SINGLE_HOST_RESPONSE = "{\"href\":\"/api/v1/hosts?fields=Hosts/*&Hosts/host_name.in(%1$s)\",\"items\":[{\"href\":\"/api/v1/hosts/%1$s\",\"Hosts\":{\"host_name\":\"%1$s\"}}]}";

    private static final java.lang.String TWO_HOST_RESPONSE = "{\"href\":\"/api/v1/hosts?fields=Hosts/*&Hosts/host_name.in(%1$s,%2$s)\",\"items\":[{\"href\":\"/api/v1/hosts/%1$s\",\"Hosts\":{\"host_name\":\"%1$s\"}},{\"href\":\"/api/v1/hosts/%2$s\",\"Hosts\":{\"host_name\":\"%2$s\"}}]}";

    private org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    @org.mockito.Mock
    org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler;

    @org.junit.Before
    public void setUp() throws java.io.IOException {
        temp.create();
    }

    @org.junit.After
    public void tearDown() throws java.io.IOException {
        temp.delete();
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException.class)
    public void testInvoke_invalidRequest_throwsException() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.io.File recommendationsDir = temp.newFolder("recommendationDir");
        java.lang.String recommendationsArtifactsLifetime = "1w";
        int requestId = 0;
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        Mockito.doReturn(java.util.Collections.emptyList()).when(metaInfo).getStackParentVersions(Matchers.anyString(), Matchers.anyString());
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(recommendationsDir, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, requestId, saRunner, metaInfo, null));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack("stackName", "stackVersion").build();
        Mockito.doThrow(new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("message")).when(command).validate(request);
        command.invoke(request, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON);
        org.junit.Assert.assertTrue(false);
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException.class)
    public void testInvoke_saRunnerNotSucceed_throwsException() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.io.File recommendationsDir = temp.newFolder("recommendationDir");
        java.lang.String recommendationsArtifactsLifetime = "1w";
        int requestId = 0;
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        Mockito.doReturn(java.util.Collections.emptyList()).when(metaInfo).getStackParentVersions(Matchers.anyString(), Matchers.anyString());
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(recommendationsDir, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, requestId, saRunner, metaInfo, null));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack("stackName", "stackVersion").build();
        java.lang.String hostsJSON = "{\"hosts\" : \"localhost\"";
        java.lang.String servicesJSON = "{\"services\" : \"HDFS\"";
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData data = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData(hostsJSON, servicesJSON);
        Mockito.doReturn(hostsJSON).when(command).getHostsInformation(request);
        Mockito.doReturn(servicesJSON).when(command).getServicesInformation(request);
        Mockito.doReturn(data).when(command).adjust(Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class));
        Mockito.doThrow(new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException("error")).when(saRunner).runScript(Matchers.any(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.class), Matchers.any(java.io.File.class));
        command.invoke(request, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON);
        org.junit.Assert.assertTrue(false);
    }

    @org.junit.Test(expected = javax.ws.rs.WebApplicationException.class)
    public void testInvoke_adjustThrowsException_throwsException() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.io.File recommendationsDir = temp.newFolder("recommendationDir");
        java.lang.String recommendationsArtifactsLifetime = "1w";
        int requestId = 0;
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        Mockito.doReturn(java.util.Collections.emptyList()).when(metaInfo).getStackParentVersions(Matchers.anyString(), Matchers.anyString());
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(recommendationsDir, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, requestId, saRunner, metaInfo, null));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack("stackName", "stackVersion").build();
        Mockito.doReturn("{\"hosts\" : \"localhost\"").when(command).getHostsInformation(request);
        Mockito.doReturn("{\"services\" : \"HDFS\"").when(command).getServicesInformation(request);
        Mockito.doThrow(new javax.ws.rs.WebApplicationException()).when(command).adjust(Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class));
        Mockito.doThrow(new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("error")).when(saRunner).runScript(Matchers.any(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.class), Matchers.any(java.io.File.class));
        command.invoke(request, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON);
        org.junit.Assert.assertTrue(false);
    }

    @org.junit.Test
    public void testInvoke_success() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.lang.String expected = "success";
        final java.lang.String testResourceString = java.lang.String.format("{\"type\": \"%s\"}", expected);
        final java.io.File recommendationsDir = temp.newFolder("recommendationDir");
        java.lang.String recommendationsArtifactsLifetime = "1w";
        final int requestId = 2;
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        Mockito.doReturn(java.util.Collections.emptyList()).when(metaInfo).getStackParentVersions(Matchers.anyString(), Matchers.anyString());
        final org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(recommendationsDir, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, requestId, saRunner, metaInfo, null));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack("stackName", "stackVersion").build();
        java.lang.String hostsJSON = "{\"hosts\" : \"localhost\"";
        java.lang.String servicesJSON = "{\"services\" : \"HDFS\"";
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData data = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData(hostsJSON, servicesJSON);
        Mockito.doReturn(hostsJSON).when(command).getHostsInformation(request);
        Mockito.doReturn(servicesJSON).when(command).getServicesInformation(request);
        Mockito.doReturn(data).when(command).adjust(Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class));
        Mockito.doAnswer(invocation -> {
            java.lang.String resultFilePath = java.lang.String.format("%s/%s", requestId, command.getResultFileName());
            java.io.File resultFile = new java.io.File(recommendationsDir, resultFilePath);
            resultFile.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.writeStringToFile(resultFile, testResourceString, java.nio.charset.Charset.defaultCharset());
            return null;
        }).when(saRunner).runScript(Matchers.any(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.class), Matchers.any(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.class), Matchers.any(java.io.File.class));
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource result = command.invoke(request, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON);
        org.junit.Assert.assertEquals(expected, result.getType());
        org.junit.Assert.assertEquals(requestId, result.getId());
    }

    @org.junit.Test
    public void testPopulateStackHierarchy() throws java.lang.Exception {
        java.io.File file = Mockito.mock(java.io.File.class);
        java.lang.String recommendationsArtifactsLifetime = "1w";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner stackAdvisorRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> cmd = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(file, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, stackAdvisorRunner, ambariMetaInfo, null);
        org.codehaus.jackson.node.ObjectNode objectNode = ((org.codehaus.jackson.node.ObjectNode) (cmd.mapper.readTree("{\"Versions\": " + "{\"stack_name\": \"stack\", \"stack_version\":\"1.0.0\"}}")));
        Mockito.doReturn(java.util.Arrays.asList("0.9", "0.8")).when(ambariMetaInfo).getStackParentVersions("stack", "1.0.0");
        cmd.populateStackHierarchy(objectNode);
        org.codehaus.jackson.JsonNode stackHierarchy = objectNode.get("Versions").get("stack_hierarchy");
        org.junit.Assert.assertNotNull(stackHierarchy);
        org.codehaus.jackson.JsonNode stackName = stackHierarchy.get("stack_name");
        org.junit.Assert.assertNotNull(stackName);
        org.junit.Assert.assertEquals("stack", stackName.asText());
        org.codehaus.jackson.node.ArrayNode stackVersions = ((org.codehaus.jackson.node.ArrayNode) (stackHierarchy.get("stack_versions")));
        org.junit.Assert.assertNotNull(stackVersions);
        org.junit.Assert.assertEquals(2, stackVersions.size());
        java.util.Iterator<org.codehaus.jackson.JsonNode> stackVersionsElements = stackVersions.getElements();
        org.junit.Assert.assertEquals("0.9", stackVersionsElements.next().asText());
        org.junit.Assert.assertEquals("0.8", stackVersionsElements.next().asText());
    }

    @org.junit.Test
    public void testPopulateAmbariServerProperties() throws java.lang.Exception {
        java.io.File file = Mockito.mock(java.io.File.class);
        java.lang.String recommendationsArtifactsLifetime = "1w";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner stackAdvisorRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> cmd = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(file, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, stackAdvisorRunner, ambariMetaInfo, null);
        org.codehaus.jackson.node.ObjectNode objectNode = ((org.codehaus.jackson.node.ObjectNode) (cmd.mapper.readTree("{\"Versions\": " + "{\"stack_name\": \"stack\", \"stack_version\":\"1.0.0\"}}")));
        java.util.Map<java.lang.String, java.lang.String> props = java.util.Collections.singletonMap("a", "b");
        Mockito.doReturn(props).when(ambariMetaInfo).getAmbariServerProperties();
        cmd.populateAmbariServerInfo(objectNode);
        org.codehaus.jackson.JsonNode serverProperties = objectNode.get("ambari-server-properties");
        org.junit.Assert.assertNotNull(serverProperties);
        org.junit.Assert.assertEquals("b", serverProperties.iterator().next().getTextValue());
    }

    @org.junit.Test
    public void testPopulateStackHierarchy_noParents() throws java.lang.Exception {
        java.io.File file = Mockito.mock(java.io.File.class);
        java.lang.String recommendationsArtifactsLifetime = "1w";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner stackAdvisorRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> cmd = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(file, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, stackAdvisorRunner, ambariMetaInfo, null);
        org.codehaus.jackson.node.ObjectNode objectNode = ((org.codehaus.jackson.node.ObjectNode) (cmd.mapper.readTree("{\"Versions\": " + "{\"stack_name\": \"stack\", \"stack_version\":\"1.0.0\"}}")));
        Mockito.doReturn(java.util.Collections.emptyList()).when(ambariMetaInfo).getStackParentVersions("stack", "1.0.0");
        cmd.populateStackHierarchy(objectNode);
        org.codehaus.jackson.JsonNode stackHierarchy = objectNode.get("Versions").get("stack_hierarchy");
        org.junit.Assert.assertNotNull(stackHierarchy);
        org.codehaus.jackson.JsonNode stackName = stackHierarchy.get("stack_name");
        org.junit.Assert.assertNotNull(stackName);
        org.junit.Assert.assertEquals("stack", stackName.asText());
        org.codehaus.jackson.node.ArrayNode stackVersions = ((org.codehaus.jackson.node.ArrayNode) (stackHierarchy.get("stack_versions")));
        org.junit.Assert.assertNotNull(stackVersions);
        org.junit.Assert.assertEquals(0, stackVersions.size());
    }

    @org.junit.Test
    public void testPopulateLdapConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> storedConfig = java.util.Collections.singletonMap("ldap-configuration", java.util.Collections.singletonMap("authentication.ldap.secondaryUrl", "localhost:333"));
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand command = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(temp.newFolder("recommendationDir"), "1w", org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 0, Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class), Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class), null);
        Mockito.when(ambariServerConfigurationHandler.getConfigurations()).thenReturn(storedConfig);
        org.codehaus.jackson.JsonNode servicesRootNode = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.json("{}");
        command.populateAmbariConfiguration(((org.codehaus.jackson.node.ObjectNode) (servicesRootNode)));
        org.codehaus.jackson.JsonNode expectedLdapConfig = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.json("{\"ambari-server-configuration\":{\"ldap-configuration\":{\"authentication.ldap.secondaryUrl\":\"localhost:333\"}}}");
        org.junit.Assert.assertEquals(expectedLdapConfig, servicesRootNode);
    }

    @org.junit.Test
    public void testPopulateLdapConfig_NoConfigs() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand command = new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(temp.newFolder("recommendationDir"), "1w", org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 0, Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class), Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class), null);
        Mockito.when(ambariServerConfigurationHandler.getConfigurations()).thenReturn(java.util.Collections.emptyMap());
        org.codehaus.jackson.JsonNode servicesRootNode = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.json("{}");
        command.populateAmbariConfiguration(((org.codehaus.jackson.node.ObjectNode) (servicesRootNode)));
        org.codehaus.jackson.JsonNode expectedLdapConfig = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.json("{\"ambari-server-configuration\":{}}");
        org.junit.Assert.assertEquals(expectedLdapConfig, servicesRootNode);
    }

    @org.junit.Test
    public void testHostInfoCachingSingleHost() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.io.File file = Mockito.mock(java.io.File.class);
        java.lang.String recommendationsArtifactsLifetime = "1w";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner stackAdvisorRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache = new java.util.HashMap<>();
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(file, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, stackAdvisorRunner, ambariMetaInfo, hostInfoCache));
        Mockito.doReturn(javax.ws.rs.core.Response.status(200).entity(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName1")).build()).doReturn(null).when(command).handleRequest(Matchers.any(javax.ws.rs.core.HttpHeaders.class), Matchers.any(java.lang.String.class), Matchers.any(javax.ws.rs.core.UriInfo.class), Matchers.any(org.apache.ambari.server.api.services.Request.Type.class), Matchers.any(javax.ws.rs.core.MediaType.class), Matchers.any(org.apache.ambari.server.api.resources.ResourceInstance.class));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(null, null).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS).forHosts(java.util.Arrays.asList(new java.lang.String[]{ "hostName1" })).build();
        java.lang.String firstResponse = command.getHostsInformation(request);
        org.junit.Assert.assertEquals(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName1"), firstResponse);
        java.lang.String secondResponse = command.getHostsInformation(request);
        org.junit.Assert.assertEquals(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName1"), secondResponse);
    }

    @org.junit.Test
    public void testHostInfoCachingTwoHost() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.io.File file = Mockito.mock(java.io.File.class);
        java.lang.String recommendationsArtifactsLifetime = "1w";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner stackAdvisorRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache = new java.util.HashMap<>();
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand command = Mockito.spy(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestStackAdvisorCommand(file, recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, stackAdvisorRunner, ambariMetaInfo, hostInfoCache));
        Mockito.doReturn(javax.ws.rs.core.Response.status(200).entity(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName1")).build()).doReturn(javax.ws.rs.core.Response.status(200).entity(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName2")).build()).doReturn(null).when(command).handleRequest(Matchers.any(javax.ws.rs.core.HttpHeaders.class), Matchers.any(java.lang.String.class), Matchers.any(javax.ws.rs.core.UriInfo.class), Matchers.any(org.apache.ambari.server.api.services.Request.Type.class), Matchers.any(javax.ws.rs.core.MediaType.class), Matchers.any(org.apache.ambari.server.api.resources.ResourceInstance.class));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(null, null).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS).forHosts(java.util.Arrays.asList(new java.lang.String[]{ "hostName1" })).build();
        java.lang.String firstResponse = command.getHostsInformation(request);
        org.junit.Assert.assertEquals(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.SINGLE_HOST_RESPONSE, "hostName1"), firstResponse);
        request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(null, null).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS).forHosts(java.util.Arrays.asList(new java.lang.String[]{ "hostName1", "hostName2" })).build();
        java.lang.String secondResponse = command.getHostsInformation(request);
        org.junit.Assert.assertEquals(java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TWO_HOST_RESPONSE, "hostName1", "hostName2"), secondResponse);
    }

    private static java.lang.String jsonString(java.lang.Object obj) throws java.io.IOException {
        return new org.codehaus.jackson.map.ObjectMapper().writeValueAsString(obj);
    }

    private static org.codehaus.jackson.JsonNode json(java.lang.Object obj) throws java.io.IOException {
        return new org.codehaus.jackson.map.ObjectMapper().convertValue(obj, org.codehaus.jackson.JsonNode.class);
    }

    private static org.codehaus.jackson.JsonNode json(java.lang.String jsonString) throws java.io.IOException {
        return new org.codehaus.jackson.map.ObjectMapper().readTree(jsonString);
    }

    private static java.util.List<java.lang.Object> list(java.lang.Object... items) {
        return com.google.common.collect.Lists.newArrayList(items);
    }

    private static java.util.Map<java.lang.String, java.lang.Object> map(java.lang.Object... keysAndValues) {
        java.util.Map<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        java.util.Iterator<java.lang.Object> iterator = java.util.Arrays.asList(keysAndValues).iterator();
        while (iterator.hasNext()) {
            map.put(iterator.next().toString(), iterator.next());
        } 
        return map;
    }

    class TestStackAdvisorCommand extends org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource> {
        public TestStackAdvisorCommand(java.io.File recommendationsDir, java.lang.String recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, int requestId, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache) {
            super(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, hostInfoCache);
        }

        @java.lang.Override
        protected void validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        }

        @java.lang.Override
        protected java.lang.String getResultFileName() {
            return "result.json";
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType getCommandType() {
            return org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_COMPONENT_LAYOUT;
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource updateResponse(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request, org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandTest.TestResource response) {
            return response;
        }

        @java.lang.Override
        public javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, java.lang.String body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.services.Request.Type requestType, javax.ws.rs.core.MediaType mediaType, org.apache.ambari.server.api.resources.ResourceInstance resource) {
            return super.handleRequest(headers, body, uriInfo, requestType, mediaType, resource);
        }
    }

    public static class TestResource extends org.apache.ambari.server.api.services.stackadvisor.StackAdvisorResponse {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String type;

        public java.lang.String getType() {
            return type;
        }

        public void setType(java.lang.String type) {
            this.type = type;
        }
    }
}