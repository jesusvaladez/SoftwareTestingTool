package org.apache.ambari.server.serveraction.users;
import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class PostUserCreationHookServerActionTest extends org.easymock.EasyMockSupport {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.users.PostUserCreationHookServerActionTest.class);

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.serveraction.users.ShellCommandUtilityWrapper shellCommandUtilityWrapper;

    @org.easymock.Mock
    private org.apache.ambari.server.agent.ExecutionCommand executionCommand;

    @org.easymock.Mock
    private org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand;

    @org.easymock.Mock
    private org.codehaus.jackson.map.ObjectMapper objectMapperMock;

    @org.easymock.Mock
    private org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory collectionPersisterServiceFactoryMock;

    @org.easymock.Mock
    private org.apache.ambari.server.serveraction.users.CsvFilePersisterService collectionPersisterService;

    @org.easymock.TestSubject
    private org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction customScriptServerAction = new org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction();

    private java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = com.google.common.collect.Maps.newConcurrentMap();

    private org.easymock.Capture<java.lang.String[]> commandCapture = null;

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> payload = new java.util.HashMap<>();

    private org.codehaus.jackson.map.ObjectMapper om = new org.codehaus.jackson.map.ObjectMapper();

    @org.junit.Before
    public void before() throws java.io.IOException, java.lang.InterruptedException {
        payload.clear();
        resetAll();
        org.easymock.EasyMock.expect(hostRoleCommand.getRequestId()).andReturn(-1L).times(2);
        org.easymock.EasyMock.expect(hostRoleCommand.getStageId()).andReturn(-1L).times(2);
    }

    @org.junit.Test
    public void shouldCommandStringBeAssembledCorrectlyForSingleUser() throws java.lang.Exception {
        payload = mockPayload(1);
        mockExecutionCommand(payload.size());
        java.lang.String payloadJson = om.writeValueAsString(payload);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param(), payloadJson);
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param(), "/hookfolder/hook.name");
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_TIME_FRAME.param(), "1000");
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param(), "/test/user_data.csv");
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param(), org.apache.ambari.server.state.SecurityType.KERBEROS.name());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param(), "test-hdfs-user");
        org.easymock.EasyMock.expect(executionCommand.getCommandParams()).andReturn(commandParams);
        org.easymock.EasyMock.expect(objectMapperMock.readValue(payloadJson, java.util.Map.class)).andReturn(payload);
        commandCapture = org.easymock.EasyMock.newCapture();
        org.easymock.EasyMock.expect(shellCommandUtilityWrapper.runCommand(org.easymock.EasyMock.capture(commandCapture))).andReturn(new org.apache.ambari.server.utils.ShellCommandUtil.Result(0, null, null)).times(payload.size());
        customScriptServerAction.setExecutionCommand(executionCommand);
        org.easymock.EasyMock.expect(collectionPersisterServiceFactoryMock.createCsvFilePersisterService(org.easymock.EasyMock.anyString())).andReturn(collectionPersisterService);
        org.easymock.EasyMock.expect(collectionPersisterService.persistMap(org.easymock.EasyMock.anyObject())).andReturn(java.lang.Boolean.TRUE);
        replayAll();
        org.apache.ambari.server.agent.CommandReport commandReport = customScriptServerAction.execute(requestSharedDataContext);
        java.lang.String[] commandArray = commandCapture.getValue();
        org.junit.Assert.assertNotNull("The command to be executed must not be null!", commandArray);
        org.junit.Assert.assertEquals("The command argument array length is not as expected!", 6, commandArray.length);
        org.junit.Assert.assertEquals("The command script is not as expected", "/hookfolder/hook.name", commandArray[0]);
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void shouldServerActionFailWhenCommandParametersAreMissing() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.easymock.EasyMock.expect(executionCommand.getCommandParams()).andReturn(commandParams).times(2);
        customScriptServerAction.setExecutionCommand(executionCommand);
        replayAll();
        org.apache.ambari.server.agent.CommandReport commandReport = customScriptServerAction.execute(requestSharedDataContext);
    }

    private void mockExecutionCommand(int callCnt) {
        org.easymock.EasyMock.expect(executionCommand.getClusterId()).andReturn("1").anyTimes();
        org.easymock.EasyMock.expect(executionCommand.getRoleCommand()).andReturn(org.apache.ambari.server.RoleCommand.EXECUTE).times(callCnt);
        org.easymock.EasyMock.expect(executionCommand.getClusterName()).andReturn("unit-test-cluster").times(callCnt);
        org.easymock.EasyMock.expect(executionCommand.getRole()).andReturn(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).times(callCnt);
        org.easymock.EasyMock.expect(executionCommand.getServiceName()).andReturn("custom-hook-script").times(callCnt);
        org.easymock.EasyMock.expect(executionCommand.getTaskId()).andReturn(-1L).times(callCnt);
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> mockPayload(int size) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> ret = new java.util.HashMap<>();
        for (int i = 0; i < size; i++) {
            ret.put("user-" + i, java.util.Arrays.asList("hdfs" + i, "yarn" + i));
        }
        return ret;
    }
}