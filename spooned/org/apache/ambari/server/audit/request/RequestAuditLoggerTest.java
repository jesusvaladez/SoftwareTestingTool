package org.apache.ambari.server.audit.request;
import org.easymock.Capture;
import org.easymock.EasyMock;
public class RequestAuditLoggerTest {
    private static final java.lang.String TEST_URI = "http://apache.org";

    private static org.apache.ambari.server.audit.request.RequestAuditLogger requestAuditLogger;

    private static org.apache.ambari.server.audit.AuditLogger mockAuditLogger;

    private org.apache.ambari.server.api.services.RequestFactory requestFactory = new org.apache.ambari.server.api.services.RequestFactory();

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.audit.request.RequestAuditLogModule());
        org.apache.ambari.server.audit.request.RequestAuditLoggerTest.requestAuditLogger = injector.getInstance(org.apache.ambari.server.audit.request.RequestAuditLogger.class);
        org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger = injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class);
    }

    @org.junit.Before
    public void before() {
        org.easymock.EasyMock.reset(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
    }

    @org.junit.After
    public void after() {
        org.easymock.EasyMock.verify(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
    }

    @org.junit.Test
    public void defaultEventCreatorPostTest() {
        testCreator(org.apache.ambari.server.audit.request.AllPostAndPutCreator.class, org.apache.ambari.server.api.services.Request.Type.POST, new org.apache.ambari.server.api.resources.BlueprintResourceDefinition(), org.apache.ambari.server.api.services.ResultStatus.STATUS.OK, null);
    }

    @org.junit.Test
    public void customEventCreatorPutTest() {
        testCreator(org.apache.ambari.server.audit.request.PutHostComponentCreator.class, org.apache.ambari.server.api.services.Request.Type.PUT, new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), org.apache.ambari.server.api.services.ResultStatus.STATUS.OK, null);
    }

    @org.junit.Test
    public void noCreatorForRequestTypeTest() {
        org.apache.ambari.server.api.services.Request request = createRequest(new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), org.apache.ambari.server.api.services.Request.Type.GET);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        try {
            createCapture();
            org.apache.ambari.server.audit.request.RequestAuditLoggerTest.requestAuditLogger.log(request, result);
            org.easymock.EasyMock.verify(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
            junit.framework.Assert.fail("Exception is excepted to be thrown");
        } catch (java.lang.AssertionError ae) {
            org.easymock.EasyMock.reset(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
            org.easymock.EasyMock.replay(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
        }
    }

    @org.junit.Test
    public void noRequestTypeTest() {
        org.apache.ambari.server.api.services.Request request = createRequest(new org.apache.ambari.server.api.resources.BlueprintResourceDefinition(), org.apache.ambari.server.api.services.Request.Type.DELETE);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        try {
            createCapture();
            org.apache.ambari.server.audit.request.RequestAuditLoggerTest.requestAuditLogger.log(request, result);
            org.easymock.EasyMock.verify(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
            junit.framework.Assert.fail("Exception is excepted to be thrown");
        } catch (java.lang.AssertionError ae) {
            org.easymock.EasyMock.reset(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
            org.easymock.EasyMock.replay(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
        }
    }

    @org.junit.Test
    public void noGetCreatorForResourceTypeTest__defaultGetCreatorUsed() {
        testCreator(org.apache.ambari.server.audit.request.AllGetCreator.class, org.apache.ambari.server.api.services.Request.Type.GET, new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), org.apache.ambari.server.api.services.ResultStatus.STATUS.ACCEPTED, null);
    }

    private void testCreator(java.lang.Class<? extends org.apache.ambari.server.audit.request.AbstractBaseCreator> expectedCreatorClass, org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition, org.apache.ambari.server.api.services.ResultStatus.STATUS resultStatus, java.lang.String resultStatusMessage) {
        org.apache.ambari.server.api.services.Request request = createRequest(resourceDefinition, requestType);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(resultStatus, resultStatusMessage));
        org.easymock.Capture<org.apache.ambari.server.audit.event.AuditEvent> capture = createCapture();
        org.apache.ambari.server.audit.request.RequestAuditLoggerTest.requestAuditLogger.log(request, result);
        java.lang.String expectedMessage = createExpectedMessage(expectedCreatorClass, requestType, resultStatus, resultStatusMessage);
        junit.framework.Assert.assertEquals(expectedMessage, capture.getValue().getAuditMessage());
    }

    private org.easymock.Capture<org.apache.ambari.server.audit.event.AuditEvent> createCapture() {
        org.easymock.EasyMock.expect(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger.isEnabled()).andReturn(true).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.audit.event.AuditEvent> capture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger.log(org.easymock.EasyMock.capture(capture));
        org.easymock.EasyMock.expectLastCall();
        org.easymock.EasyMock.replay(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.mockAuditLogger);
        return capture;
    }

    private org.apache.ambari.server.api.services.Request createRequest(org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition, org.apache.ambari.server.api.services.Request.Type requestType) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = new org.apache.ambari.server.api.query.QueryImpl(new java.util.HashMap<>(), resourceDefinition, null);
        return requestFactory.createRequest(null, new org.apache.ambari.server.api.services.RequestBody(), new org.apache.ambari.server.api.services.LocalUriInfo(org.apache.ambari.server.audit.request.RequestAuditLoggerTest.TEST_URI), requestType, resource);
    }

    private java.lang.String createExpectedMessage(java.lang.Class<? extends org.apache.ambari.server.audit.request.AbstractBaseCreator> expectedCreatorClass, org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.services.ResultStatus.STATUS resultStatus, java.lang.String resultStatusMessage) {
        return (expectedCreatorClass.getName() + " ") + java.lang.String.format("%s %s %s %s %s", requestType, org.apache.ambari.server.audit.request.RequestAuditLoggerTest.TEST_URI, resultStatus.getStatus(), resultStatus, resultStatusMessage);
    }
}