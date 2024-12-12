package org.apache.ambari.server.notifications.dispatchers;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ java.lang.ProcessBuilder.class, org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class })
public class AlertScriptDispatcherTest {
    private static final java.lang.String SCRIPT_CONFIG_VALUE = "/foo/script.py";

    private static final java.lang.String DISPATCH_PROPERTY_SCRIPT_DIRECTORY_KEY = "notification.dispatch.alert.script.directory";

    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.notifications.DispatchFactory m_dispatchFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tempFolder = new org.junit.rules.TemporaryFolder();

    private final org.apache.ambari.server.state.alert.AlertNotification mockNotification = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.alert.AlertNotification.class);

    private final org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher();

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.MockModule());
        m_injector.injectMembers(this);
    }

    @org.junit.Test
    public void testNullScriptPath() {
        try {
            dispatcher.getProcessBuilder(null, mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Script path cannot be null or empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testEmptyScriptPath() {
        try {
            dispatcher.getProcessBuilder("", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Script path cannot be null or empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testRelativeScriptPath() {
        try {
            dispatcher.getProcessBuilder("relative/path/to/script.sh", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Invalid script path format: relative/path/to/script.sh", e.getMessage());
        }
    }

    @org.junit.Test
    public void testNonExistentScriptPath() {
        try {
            dispatcher.getProcessBuilder("/non/existent/path/to/script.sh", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Script does not exist: /non/existent/path/to/script.sh", e.getMessage());
        }
    }

    @org.junit.Test
    public void testNonExecutableScriptPath() throws java.io.IOException {
        java.io.File nonExecutableFile = tempFolder.newFile("non_executable_script.sh");
        try {
            dispatcher.getProcessBuilder(nonExecutableFile.getAbsolutePath(), mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Script is not executable: " + nonExecutableFile.getAbsolutePath(), e.getMessage());
        }
    }

    @org.junit.Test
    public void testValidScriptPath() throws java.io.IOException {
        java.io.File validScript = tempFolder.newFile("valid_script.sh");
        validScript.setExecutable(true);
        java.lang.String scriptAbsolutePath = validScript.getAbsolutePath();
        final java.lang.String ALERT_DEFINITION_NAME = "mock_alert_with_quotes";
        final java.lang.String ALERT_DEFINITION_LABEL = "Mock alert with Quotes";
        final java.lang.String ALERT_LABEL = "Alert Label";
        final java.lang.String ALERT_SERVICE_NAME = "FOO_SERVICE";
        final java.lang.String ALERT_TEXT = "Did you know, \"Quotes are hard!!!\"";
        final java.lang.String ALERT_TEXT_ESCAPED = "Did you know, \\\"Quotes are hard\\!\\!\\!\\\"";
        final java.lang.String ALERT_HOST = "mock_host";
        final long ALERT_TIMESTAMP = 1111111L;
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName(ALERT_DEFINITION_NAME);
        definition.setLabel(ALERT_DEFINITION_LABEL);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setAlertLabel(ALERT_LABEL);
        history.setAlertText(ALERT_TEXT);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setServiceName(ALERT_SERVICE_NAME);
        history.setHostName(ALERT_HOST);
        history.setAlertTimestamp(ALERT_TIMESTAMP);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(history);
        notification.setAlertInfo(alertInfo);
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher();
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder processBuilder = dispatcher.getProcessBuilder(scriptAbsolutePath, notification);
        junit.framework.Assert.assertNotNull(processBuilder);
        junit.framework.Assert.assertEquals("sh", processBuilder.command().get(0));
        junit.framework.Assert.assertEquals("-c", processBuilder.command().get(1));
        junit.framework.Assert.assertTrue(processBuilder.command().get(2).contains(scriptAbsolutePath));
    }

    @org.junit.Test
    public void testInjectionAttemptWithSemicolon() {
        try {
            dispatcher.getProcessBuilder("/path/to/script.sh; rm -rf /", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Invalid script path format: /path/to/script.sh; rm -rf /", e.getMessage());
        }
    }

    @org.junit.Test
    public void testInjectionAttemptWithAndOperator() {
        try {
            dispatcher.getProcessBuilder("/path/to/script.sh && touch /tmp/hacked", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Invalid script path format: /path/to/script.sh && touch /tmp/hacked", e.getMessage());
        }
    }

    @org.junit.Test
    public void testInjectionAttemptWithPipeOperator() {
        try {
            dispatcher.getProcessBuilder("/path/to/script.sh | ls", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Invalid script path format: /path/to/script.sh | ls", e.getMessage());
        }
    }

    @org.junit.Test
    public void testInjectionAttemptWithBackticks() {
        try {
            dispatcher.getProcessBuilder("/path/to/script.sh `rm -rf /`", mockNotification);
            junit.framework.Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (java.lang.IllegalArgumentException e) {
            junit.framework.Assert.assertEquals("Invalid script path format: /path/to/script.sh `rm -rf /`", e.getMessage());
        }
    }

    @org.junit.Test
    public void testNonAlertNotification() throws java.lang.Exception {
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.notifications.Notification notification = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        callback.onFailure(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(callback, notification);
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name());
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback, notification);
    }

    @org.junit.Test
    public void testMissingScriptConfiguration() throws java.lang.Exception {
        m_configuration.setProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_DEFAULT_KEY, null);
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        callback.onFailure(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(callback);
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name());
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback);
    }

    @org.junit.Test
    public void testProcessBuilderInvocation() throws java.lang.Exception {
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        callback.onSuccess(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = ((org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher) (m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name())));
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder powerMockProcessBuilder = m_injector.getInstance(java.lang.ProcessBuilder.class);
        org.easymock.EasyMock.expect(dispatcher.getProcessBuilder(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.SCRIPT_CONFIG_VALUE, notification)).andReturn(powerMockProcessBuilder).once();
        org.easymock.EasyMock.replay(callback, dispatcher);
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback, dispatcher);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    @org.junit.Test
    public void testCustomScriptConfiguration() throws java.lang.Exception {
        final java.lang.String customScriptKey = "foo.bar.key";
        final java.lang.String customScriptValue = "/foo/bar/foobar.py";
        m_configuration.setProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_DEFAULT_KEY, null);
        m_configuration.setProperty(customScriptKey, customScriptValue);
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        notification.DispatchProperties = new java.util.HashMap<>();
        notification.DispatchProperties.put(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_CONFIG_KEY, customScriptKey);
        callback.onSuccess(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = ((org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher) (m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name())));
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder powerMockProcessBuilder = m_injector.getInstance(java.lang.ProcessBuilder.class);
        org.easymock.EasyMock.expect(dispatcher.getProcessBuilder(customScriptValue, notification)).andReturn(powerMockProcessBuilder).once();
        org.easymock.EasyMock.replay(callback, dispatcher);
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback, dispatcher);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    @org.junit.Test
    public void testGetScriptLocation() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = ((org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher) (m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name())));
        m_injector.injectMembers(dispatcher);
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        notification.DispatchProperties = new java.util.HashMap();
        junit.framework.Assert.assertEquals(dispatcher.getScriptLocation(notification), null);
        final java.lang.String filename = "foo.py";
        notification.DispatchProperties.put(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_FILENAME_KEY, filename);
        junit.framework.Assert.assertEquals(dispatcher.getScriptLocation(notification), "/var/lib/ambari-server/resources/scripts/foo.py");
        final java.lang.String scriptDirectory = "/var/lib/ambari-server/resources/scripts/foo";
        m_configuration.setProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.DISPATCH_PROPERTY_SCRIPT_DIRECTORY_KEY, scriptDirectory);
        junit.framework.Assert.assertEquals(dispatcher.getScriptLocation(notification), "/var/lib/ambari-server/resources/scripts/foo/foo.py");
    }

    @org.junit.Test
    public void testCustomScriptConfigurationByScriptFilename() throws java.lang.Exception {
        final java.lang.String filename = "foo.py";
        final java.lang.String scriptDirectory = "/var/lib/ambari-server/resources/scripts/foo";
        m_configuration.setProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.DISPATCH_PROPERTY_SCRIPT_DIRECTORY_KEY, scriptDirectory);
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        notification.DispatchProperties = new java.util.HashMap();
        notification.DispatchProperties.put(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_FILENAME_KEY, filename);
        callback.onSuccess(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = ((org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher) (m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name())));
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder powerMockProcessBuilder = m_injector.getInstance(java.lang.ProcessBuilder.class);
        org.easymock.EasyMock.expect(dispatcher.getProcessBuilder(dispatcher.getScriptLocation(notification), notification)).andReturn(powerMockProcessBuilder).once();
        org.easymock.EasyMock.replay(callback, dispatcher);
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback, dispatcher);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    @org.junit.Test
    public void testFailedProcess() throws java.lang.Exception {
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        callback.onFailure(notification.CallbackIds);
        org.easymock.EasyMock.expectLastCall().once();
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = ((org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher) (m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name())));
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder powerMockProcessBuilder = m_injector.getInstance(java.lang.ProcessBuilder.class);
        org.easymock.EasyMock.expect(dispatcher.getProcessBuilder(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.SCRIPT_CONFIG_VALUE, notification)).andReturn(powerMockProcessBuilder).once();
        java.lang.Process mockProcess = powerMockProcessBuilder.start();
        org.easymock.EasyMock.expect(mockProcess.exitValue()).andReturn(255).anyTimes();
        org.easymock.EasyMock.replay(callback, dispatcher, mockProcess);
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback, dispatcher);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    @org.junit.Test
    public void testArgumentEscaping() throws java.lang.Exception {
        final java.lang.String ALERT_DEFINITION_NAME = "mock_alert_with_quotes";
        final java.lang.String ALERT_DEFINITION_LABEL = "Mock alert with Quotes";
        final java.lang.String ALERT_LABEL = "Alert Label";
        final java.lang.String ALERT_SERVICE_NAME = "FOO_SERVICE";
        final java.lang.String ALERT_TEXT = "Did you know, \"Quotes are hard!!!\"";
        final java.lang.String ALERT_TEXT_ESCAPED = "Did you know, \\\"Quotes are hard\\!\\!\\!\\\"";
        final java.lang.String ALERT_HOST = "mock_host";
        final long ALERT_TIMESTAMP = 1111111L;
        java.io.File validScript = tempFolder.newFile("valid_script.sh");
        validScript.setExecutable(true);
        java.lang.String scriptAbsolutePath = validScript.getAbsolutePath();
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = callback;
        notification.CallbackIds = java.util.Collections.singletonList(java.util.UUID.randomUUID().toString());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName(ALERT_DEFINITION_NAME);
        definition.setLabel(ALERT_DEFINITION_LABEL);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setAlertLabel(ALERT_LABEL);
        history.setAlertText(ALERT_TEXT);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setServiceName(ALERT_SERVICE_NAME);
        history.setHostName(ALERT_HOST);
        history.setAlertTimestamp(ALERT_TIMESTAMP);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(history);
        notification.setAlertInfo(alertInfo);
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher();
        m_injector.injectMembers(dispatcher);
        java.lang.ProcessBuilder processBuilder = dispatcher.getProcessBuilder(scriptAbsolutePath, notification);
        java.util.List<java.lang.String> commands = processBuilder.command();
        junit.framework.Assert.assertEquals(3, commands.size());
        junit.framework.Assert.assertEquals("sh", commands.get(0));
        junit.framework.Assert.assertEquals("-c", commands.get(1));
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(scriptAbsolutePath).append(" ");
        buffer.append(ALERT_DEFINITION_NAME).append(" ");
        buffer.append("\"").append(ALERT_DEFINITION_LABEL).append("\"").append(" ");
        buffer.append(ALERT_SERVICE_NAME).append(" ");
        buffer.append(org.apache.ambari.server.state.AlertState.OK).append(" ");
        buffer.append("\"").append(ALERT_TEXT_ESCAPED).append("\"").append(" ");
        buffer.append(ALERT_TIMESTAMP).append(" ");
        buffer.append(ALERT_HOST);
        junit.framework.Assert.assertEquals(buffer.toString(), commands.get(2));
        history.setHostName(null);
        alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(history);
        notification.setAlertInfo(alertInfo);
        processBuilder = dispatcher.getProcessBuilder(scriptAbsolutePath, notification);
        commands = processBuilder.command();
        buffer = new java.lang.StringBuilder();
        buffer.append(scriptAbsolutePath).append(" ");
        buffer.append(ALERT_DEFINITION_NAME).append(" ");
        buffer.append("\"").append(ALERT_DEFINITION_LABEL).append("\"").append(" ");
        buffer.append(ALERT_SERVICE_NAME).append(" ");
        buffer.append(org.apache.ambari.server.state.AlertState.OK).append(" ");
        buffer.append("\"").append(ALERT_TEXT_ESCAPED).append("\"").append(" ");
        buffer.append(ALERT_TIMESTAMP).append(" ");
        buffer.append("");
        junit.framework.Assert.assertEquals(buffer.toString(), commands.get(2));
    }

    private class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            try {
                org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
                configuration.setProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_DEFAULT_KEY, org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.SCRIPT_CONFIG_VALUE);
                org.apache.ambari.server.notifications.DispatchFactory dispatchFactory = org.apache.ambari.server.notifications.DispatchFactory.getInstance();
                bind(org.apache.ambari.server.notifications.DispatchFactory.class).toInstance(dispatchFactory);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher dispatcher = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class).addMockedMethods("getProcessBuilder").createNiceMock();
                org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.SynchronizedExecutor synchronizedExecutor = new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcherTest.SynchronizedExecutor();
                java.lang.reflect.Field field = org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class.getDeclaredField("m_executor");
                field.setAccessible(true);
                field.set(dispatcher, synchronizedExecutor);
                dispatchFactory.register(dispatcher.getType(), dispatcher);
                bind(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class).toInstance(dispatcher);
                java.lang.Process processMock = org.easymock.EasyMock.createNiceMock(java.lang.Process.class);
                java.lang.ProcessBuilder powerMockProcessBuilder = org.powermock.api.easymock.PowerMock.createNiceMock(java.lang.ProcessBuilder.class);
                org.easymock.EasyMock.expect(powerMockProcessBuilder.start()).andReturn(processMock).atLeastOnce();
                org.powermock.api.easymock.PowerMock.replay(powerMockProcessBuilder);
                bind(java.lang.ProcessBuilder.class).toInstance(powerMockProcessBuilder);
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private static final class SynchronizedExecutor implements java.util.concurrent.Executor {
        @java.lang.Override
        public void execute(java.lang.Runnable command) {
            command.run();
        }
    }
}