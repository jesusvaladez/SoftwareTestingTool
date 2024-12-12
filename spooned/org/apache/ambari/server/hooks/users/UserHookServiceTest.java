package org.apache.ambari.server.hooks.users;
import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class UserHookServiceTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.hooks.AmbariEventFactory eventFactoryMock;

    @org.easymock.Mock
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisherMock;

    @org.easymock.Mock
    private org.apache.ambari.server.actionmanager.ActionManager actionManagerMock;

    @org.easymock.Mock
    private org.apache.ambari.server.actionmanager.RequestFactory requestFactoryMock;

    @org.easymock.Mock
    private org.apache.ambari.server.actionmanager.StageFactory stageFactoryMock;

    @org.easymock.Mock
    private org.apache.ambari.server.configuration.Configuration configurationMock;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clustersMock;

    @org.easymock.Mock
    private org.codehaus.jackson.map.ObjectMapper objectMapperMock;

    @org.easymock.Mock
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clustersMap;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster clusterMock;

    @org.easymock.Mock
    private org.apache.ambari.server.actionmanager.Stage stageMock;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Config configMock;

    @org.easymock.TestSubject
    private org.apache.ambari.server.hooks.users.UserHookService hookService = new org.apache.ambari.server.hooks.users.UserHookService();

    private org.apache.ambari.server.hooks.HookContext hookContext;

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> usersToGroups;

    private org.apache.ambari.server.hooks.users.UserCreatedEvent userCreatedEvent;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        usersToGroups = new java.util.HashMap<>();
        usersToGroups.put("testUser", new java.util.HashSet<>(java.util.Arrays.asList("hdfs", "yarn")));
        hookContext = new org.apache.ambari.server.hooks.users.PostUserCreationHookContext(usersToGroups);
        userCreatedEvent = new org.apache.ambari.server.hooks.users.UserCreatedEvent(hookContext);
        resetAll();
    }

    @org.junit.Test
    public void shouldServiceQuitWhenFeatureIsDisabled() {
        org.easymock.EasyMock.expect(configurationMock.isUserHookEnabled()).andReturn(java.lang.Boolean.FALSE);
        replayAll();
        java.lang.Boolean triggered = hookService.execute(hookContext);
        org.junit.Assert.assertFalse("The hook must not be triggered if feature is disabled!", triggered);
    }

    @org.junit.Test
    public void shouldServiceQuitWhenClusterDoesNotExist() {
        org.easymock.EasyMock.expect(configurationMock.isUserHookEnabled()).andReturn(java.lang.Boolean.TRUE);
        org.easymock.EasyMock.expect(clustersMap.isEmpty()).andReturn(java.lang.Boolean.TRUE);
        org.easymock.EasyMock.expect(clustersMock.getClusters()).andReturn(clustersMap);
        replayAll();
        java.lang.Boolean triggered = hookService.execute(hookContext);
        org.junit.Assert.assertFalse("The hook must not be triggered if there's no cluster!", triggered);
    }

    @org.junit.Test
    public void shouldServiceQuitWhenCalledWithEmptyContext() {
        org.easymock.EasyMock.expect(configurationMock.isUserHookEnabled()).andReturn(java.lang.Boolean.TRUE);
        org.easymock.EasyMock.expect(clustersMap.isEmpty()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.expect(clustersMock.getClusters()).andReturn(clustersMap);
        replayAll();
        java.lang.Boolean triggered = hookService.execute(new org.apache.ambari.server.hooks.users.PostUserCreationHookContext(java.util.Collections.emptyMap()));
        org.junit.Assert.assertFalse("The hook should not be triggered if there is no users in the context!", triggered);
    }

    @org.junit.Test
    public void shouldServiceTriggerHookWhenPrerequisitesAreSatisfied() {
        org.easymock.EasyMock.expect(configurationMock.isUserHookEnabled()).andReturn(java.lang.Boolean.TRUE);
        org.easymock.EasyMock.expect(clustersMap.isEmpty()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.expect(clustersMock.getClusters()).andReturn(clustersMap);
        org.easymock.Capture<org.apache.ambari.server.hooks.HookContext> contextCapture = org.easymock.EasyMock.newCapture();
        org.easymock.EasyMock.expect(eventFactoryMock.newUserCreatedEvent(org.easymock.EasyMock.capture(contextCapture))).andReturn(userCreatedEvent);
        org.easymock.Capture<org.apache.ambari.server.hooks.users.UserCreatedEvent> userCreatedEventCapture = org.easymock.EasyMock.newCapture();
        ambariEventPublisherMock.publish(org.easymock.EasyMock.capture(userCreatedEventCapture));
        replayAll();
        java.lang.Boolean triggered = hookService.execute(hookContext);
        org.junit.Assert.assertTrue("The hook must be triggered if prerequisites satisfied!", triggered);
        org.junit.Assert.assertEquals("The hook context the event is generated from is not as expected ", hookContext, contextCapture.getValue());
        org.junit.Assert.assertEquals("The user created event is not the expected ", userCreatedEvent, userCreatedEventCapture.getValue());
    }

    @org.junit.Test
    public void shouldCommandParametersBeSet() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clsMap = new java.util.HashMap<>();
        clsMap.put("test-cluster", clusterMock);
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        configMap.put("hdfs_user", "hdfs-test-user");
        org.easymock.EasyMock.expect(clusterMock.getClusterId()).andReturn(1L);
        org.easymock.EasyMock.expect(clusterMock.getClusterName()).andReturn("test-cluster");
        org.easymock.EasyMock.expect(clusterMock.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.NONE).times(3);
        org.easymock.EasyMock.expect(clusterMock.getDesiredConfigByType("hadoop-env")).andReturn(configMock);
        org.easymock.EasyMock.expect(configMock.getProperties()).andReturn(configMap);
        org.easymock.EasyMock.expect(actionManagerMock.getNextRequestId()).andReturn(1L);
        org.easymock.EasyMock.expect(clustersMock.getClusters()).andReturn(clsMap);
        org.easymock.EasyMock.expect(configurationMock.getServerTempDir()).andReturn("/var/lib/ambari-server/tmp").times(2);
        org.easymock.EasyMock.expect(configurationMock.getProperty(org.apache.ambari.server.configuration.Configuration.POST_USER_CREATION_HOOK)).andReturn("/var/lib/ambari-server/resources/scripts/post-user-creation-hook.sh").anyTimes();
        org.easymock.EasyMock.expect(objectMapperMock.writeValueAsString(((org.apache.ambari.server.hooks.users.PostUserCreationHookContext) (userCreatedEvent.getContext())).getUserGroups())).andReturn("{testUser=[hdfs, yarn]}");
        stageMock.setStageId(-1);
        stageMock.addServerActionCommand(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.Role.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.RoleCommand.class), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent.class), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyInt(), org.easymock.EasyMock.anyBoolean(), org.easymock.EasyMock.anyBoolean());
        org.easymock.EasyMock.expect(requestFactoryMock.createNewFromStages(java.util.Arrays.asList(stageMock), "{}")).andReturn(null);
        org.easymock.EasyMock.expect(stageFactoryMock.createNew(1, "/var/lib/ambari-server/tmp:1", "test-cluster", 1, "Post user creation hook for [ 1 ] users", "{}", "{}")).andReturn(stageMock);
        replayAll();
        hookService.onUserCreatedEvent(userCreatedEvent);
    }
}