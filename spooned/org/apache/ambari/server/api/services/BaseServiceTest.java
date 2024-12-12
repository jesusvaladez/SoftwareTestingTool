package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public abstract class BaseServiceTest {
    protected org.apache.ambari.server.api.resources.ResourceInstance resourceInstance = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);

    protected org.apache.ambari.server.api.services.RequestFactory requestFactory = EasyMock.createStrictMock(org.apache.ambari.server.api.services.RequestFactory.class);

    protected org.apache.ambari.server.api.services.Request request = EasyMock.createNiceMock(org.apache.ambari.server.api.services.Request.class);

    protected javax.ws.rs.core.HttpHeaders httpHeaders = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);

    protected javax.ws.rs.core.UriInfo uriInfo = EasyMock.createNiceMock(javax.ws.rs.core.UriInfo.class);

    protected org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);

    protected org.apache.ambari.server.api.services.RequestBody requestBody = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);

    protected org.apache.ambari.server.api.services.parsers.RequestBodyParser bodyParser = EasyMock.createStrictMock(org.apache.ambari.server.api.services.parsers.RequestBodyParser.class);

    protected org.apache.ambari.server.api.services.ResultStatus status = EasyMock.createNiceMock(org.apache.ambari.server.api.services.ResultStatus.class);

    protected org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = EasyMock.createStrictMock(org.apache.ambari.server.api.services.serializers.ResultSerializer.class);

    protected java.lang.Object serializedResult = new java.lang.Object();

    public org.apache.ambari.server.api.resources.ResourceInstance getTestResource() {
        return resourceInstance;
    }

    public org.apache.ambari.server.api.services.RequestFactory getTestRequestFactory() {
        return requestFactory;
    }

    public org.apache.ambari.server.api.services.Request getRequest() {
        return request;
    }

    public javax.ws.rs.core.HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public javax.ws.rs.core.UriInfo getUriInfo() {
        return uriInfo;
    }

    public org.apache.ambari.server.api.services.parsers.RequestBodyParser getTestBodyParser() {
        return bodyParser;
    }

    public org.apache.ambari.server.api.services.serializers.ResultSerializer getTestResultSerializer() {
        return serializer;
    }

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    public org.apache.ambari.server.audit.request.RequestAuditLogger requestAuditLogger;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.api.services.BaseService.init(requestAuditLogger);
    }

    @org.junit.Test
    public void testService() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listTestInvocations = getTestInvocations();
        for (org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testInvocation : listTestInvocations) {
            testMethod(testInvocation);
            testMethod_bodyParseException(testInvocation);
            testMethod_resultInErrorState(testInvocation);
        }
    }

    private void testMethod(org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testMethod) throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        try {
            EasyMock.expect(bodyParser.parse(testMethod.getBody())).andReturn(java.util.Collections.singleton(requestBody));
        } catch (org.apache.ambari.server.api.services.parsers.BodyParseException e) {
        }
        assertCreateRequest(testMethod);
        EasyMock.expect(request.process()).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(status).atLeastOnce();
        EasyMock.expect(status.getStatusCode()).andReturn(testMethod.getStatusCode()).atLeastOnce();
        EasyMock.expect(serializer.serialize(result)).andReturn(serializedResult);
        replayMocks();
        javax.ws.rs.core.Response r = testMethod.invoke();
        org.junit.Assert.assertEquals(serializedResult, r.getEntity());
        org.junit.Assert.assertEquals(testMethod.getStatusCode(), r.getStatus());
        verifyAndResetMocks();
    }

    protected void assertCreateRequest(org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testMethod) {
        addExpectForInitialRequest(testMethod);
        EasyMock.expect(requestFactory.createRequest(httpHeaders, requestBody, uriInfo, testMethod.getRequestType(), resourceInstance)).andReturn(request);
    }

    private void testMethod_bodyParseException(org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testMethod) throws java.lang.Exception {
        addExpectForInitialRequest(testMethod);
        org.easymock.Capture<org.apache.ambari.server.api.services.Result> resultCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.api.services.parsers.BodyParseException e = new org.apache.ambari.server.api.services.parsers.BodyParseException("TEST MSG");
        EasyMock.expect(bodyParser.parse(testMethod.getBody())).andThrow(e);
        EasyMock.expect(serializer.serialize(EasyMock.capture(resultCapture))).andReturn(serializedResult);
        replayMocks();
        javax.ws.rs.core.Response r = testMethod.invoke();
        org.junit.Assert.assertEquals(serializedResult, r.getEntity());
        org.junit.Assert.assertEquals(400, r.getStatus());
        verifyAndResetMocks();
    }

    private void testMethod_resultInErrorState(org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testMethod) throws java.lang.Exception {
        try {
            EasyMock.expect(bodyParser.parse(testMethod.getBody())).andReturn(java.util.Collections.singleton(requestBody));
        } catch (org.apache.ambari.server.api.services.parsers.BodyParseException e) {
        }
        assertCreateRequest(testMethod);
        EasyMock.expect(request.process()).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(status).atLeastOnce();
        EasyMock.expect(status.getStatusCode()).andReturn(400).atLeastOnce();
        EasyMock.expect(serializer.serialize(result)).andReturn(serializedResult);
        replayMocks();
        javax.ws.rs.core.Response r = testMethod.invoke();
        org.junit.Assert.assertEquals(serializedResult, r.getEntity());
        org.junit.Assert.assertEquals(400, r.getStatus());
        verifyAndResetMocks();
    }

    private void replayMocks() {
        EasyMock.replay(resourceInstance, requestFactory, request, result, requestBody, bodyParser, status, serializer);
    }

    private void verifyAndResetMocks() {
        EasyMock.verify(resourceInstance, requestFactory, request, result, requestBody, bodyParser, status, serializer);
        EasyMock.reset(resourceInstance, requestFactory, request, result, requestBody, bodyParser, status, serializer);
    }

    private void addExpectForInitialRequest(org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation testMethod) {
        org.apache.ambari.server.api.services.RequestBody rb = new org.apache.ambari.server.api.services.RequestBody();
        rb.setBody(testMethod.getBody());
        EasyMock.expect(requestFactory.createRequest(org.easymock.EasyMock.eq(httpHeaders), org.easymock.EasyMock.anyObject(org.apache.ambari.server.api.services.RequestBody.class), org.easymock.EasyMock.eq(uriInfo), org.easymock.EasyMock.eq(testMethod.getRequestType()), org.easymock.EasyMock.eq(resourceInstance))).andReturn(request);
    }

    public static class ServiceTestInvocation {
        private org.apache.ambari.server.api.services.Request.Type m_type;

        private org.apache.ambari.server.api.services.BaseService m_instance;

        private java.lang.reflect.Method m_method;

        private java.lang.Object[] m_args;

        private java.lang.String m_body;

        private static final java.util.Map<org.apache.ambari.server.api.services.Request.Type, java.lang.Integer> mapStatusCodes = new java.util.HashMap<>();

        static {
            mapStatusCodes.put(org.apache.ambari.server.api.services.Request.Type.GET, 200);
            mapStatusCodes.put(org.apache.ambari.server.api.services.Request.Type.POST, 201);
            mapStatusCodes.put(org.apache.ambari.server.api.services.Request.Type.PUT, 200);
            mapStatusCodes.put(org.apache.ambari.server.api.services.Request.Type.DELETE, 200);
            mapStatusCodes.put(org.apache.ambari.server.api.services.Request.Type.QUERY_POST, 201);
        }

        public ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.services.BaseService instance, java.lang.reflect.Method method, java.lang.Object[] args, java.lang.String body) {
            m_type = requestType;
            m_instance = instance;
            m_method = method;
            m_args = args;
            m_body = body;
        }

        public int getStatusCode() {
            return org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation.mapStatusCodes.get(m_type);
        }

        public org.apache.ambari.server.api.services.Request.Type getRequestType() {
            return m_type;
        }

        public java.lang.String getBody() {
            return m_body;
        }

        public javax.ws.rs.core.Response invoke() throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
            return ((javax.ws.rs.core.Response) (m_method.invoke(m_instance, m_args)));
        }
    }

    public abstract java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception;
}