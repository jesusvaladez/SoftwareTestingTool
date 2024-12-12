package org.apache.ambari.server.notifications.dispatchers;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import org.easymock.EasyMock;
public class EmailDispatcherTest {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.notifications.DispatchFactory m_dispatchFactory;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.notifications.dispatchers.EmailDispatcherTest.MockModule()));
        m_dispatchFactory = m_injector.getInstance(org.apache.ambari.server.notifications.DispatchFactory.class);
    }

    @org.junit.Test
    public void testNoRecipients() {
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.Callback = callback;
        java.util.List<java.lang.String> callbackIds = new java.util.ArrayList<>();
        callbackIds.add(java.util.UUID.randomUUID().toString());
        notification.CallbackIds = callbackIds;
        callback.onFailure(callbackIds);
        org.easymock.EasyMock.expectLastCall();
        org.easymock.EasyMock.replay(callback);
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.EMAIL.name());
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback);
    }

    @org.junit.Test
    public void testNoEmailPropeties() {
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        org.apache.ambari.server.notifications.DispatchCallback callback = org.easymock.EasyMock.createMock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.Callback = callback;
        notification.Recipients = new java.util.ArrayList<>();
        org.apache.ambari.server.notifications.Recipient recipient = new org.apache.ambari.server.notifications.Recipient();
        recipient.Identifier = "foo";
        notification.Recipients.add(recipient);
        java.util.List<java.lang.String> callbackIds = new java.util.ArrayList<>();
        callbackIds.add(java.util.UUID.randomUUID().toString());
        notification.CallbackIds = callbackIds;
        callback.onFailure(callbackIds);
        org.easymock.EasyMock.expectLastCall();
        org.easymock.EasyMock.replay(callback);
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = m_dispatchFactory.getDispatcher(org.apache.ambari.server.state.alert.TargetType.EMAIL.name());
        dispatcher.dispatch(notification);
        org.easymock.EasyMock.verify(callback);
    }

    @org.junit.Test
    public void testValidateTargetConfig_invalidOnAuthenticationException() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        javax.mail.Transport mockedTransport = org.easymock.EasyMock.createNiceMock(javax.mail.Transport.class);
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher dispatcher = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.class).addMockedMethods("getMailTransport").createNiceMock();
        org.easymock.EasyMock.expect(dispatcher.getMailTransport(properties)).andReturn(mockedTransport);
        mockedTransport.connect();
        org.easymock.EasyMock.expectLastCall().andThrow(new javax.mail.AuthenticationFailedException());
        org.easymock.EasyMock.replay(dispatcher, mockedTransport);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateTargetConfig_invalidOnMessagingException() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        javax.mail.Transport mockedTransport = org.easymock.EasyMock.createNiceMock(javax.mail.Transport.class);
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher dispatcher = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.class).addMockedMethods("getMailTransport").createNiceMock();
        org.easymock.EasyMock.expect(dispatcher.getMailTransport(properties)).andReturn(mockedTransport);
        mockedTransport.connect();
        org.easymock.EasyMock.expectLastCall().andThrow(new javax.mail.MessagingException());
        org.easymock.EasyMock.replay(dispatcher, mockedTransport);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateTargetConfig_validIfNoErrors() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        javax.mail.Transport mockedTransport = org.easymock.EasyMock.createNiceMock(javax.mail.Transport.class);
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher dispatcher = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.class).addMockedMethods("getMailTransport").createNiceMock();
        org.easymock.EasyMock.expect(dispatcher.getMailTransport(properties)).andReturn(mockedTransport);
        org.easymock.EasyMock.replay(dispatcher, mockedTransport);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
        }
    }
}