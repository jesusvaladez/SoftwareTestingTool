package org.apache.ambari.server.api;
import org.apache.http.HttpStatus;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.api.AmbariErrorHandler.class, org.slf4j.LoggerFactory.class, org.eclipse.jetty.server.HttpConnection.class, java.util.UUID.class })
public class AmbariErrorHandlerTest extends org.easymock.EasyMockSupport {
    private com.google.gson.Gson gson = new com.google.gson.Gson();

    private org.slf4j.Logger logger = createNiceMock(org.slf4j.Logger.class);

    private org.eclipse.jetty.server.HttpConnection httpConnection = createNiceMock(org.eclipse.jetty.server.HttpConnection.class);

    private org.eclipse.jetty.server.HttpChannel httpChannel = createNiceMock(org.eclipse.jetty.server.HttpChannel.class);

    private org.eclipse.jetty.server.Response response = createNiceMock(org.eclipse.jetty.server.Response.class);

    private org.eclipse.jetty.server.Request request = createNiceMock(org.eclipse.jetty.server.Request.class);

    private javax.servlet.http.HttpServletResponse httpServletResponse = createNiceMock(javax.servlet.http.HttpServletResponse.class);

    private javax.servlet.http.HttpServletRequest httpServletRequest = createNiceMock(javax.servlet.http.HttpServletRequest.class);

    private org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider propertiesProvider = createNiceMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);

    final java.lang.String target = "target";

    @org.junit.Test
    public void testHandleInternalServerError() throws java.io.IOException {
        final java.util.UUID requestId = java.util.UUID.fromString("4db659b2-7902-477b-b8e6-c35261d3334a");
        mockStatic(org.eclipse.jetty.server.HttpConnection.class, java.util.UUID.class, org.slf4j.LoggerFactory.class);
        Mockito.when(org.eclipse.jetty.server.HttpConnection.getCurrentConnection()).thenReturn(httpConnection);
        Mockito.when(java.util.UUID.randomUUID()).thenReturn(requestId);
        Mockito.when(org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.AmbariErrorHandler.class)).thenReturn(logger);
        java.lang.Throwable th = createNiceMock(java.lang.Throwable.class);
        org.easymock.Capture<java.lang.String> captureLogMessage = org.easymock.EasyMock.newCapture();
        logger.error(EasyMock.capture(captureLogMessage), EasyMock.eq(th));
        EasyMock.expectLastCall();
        EasyMock.expect(httpConnection.getHttpChannel()).andReturn(httpChannel);
        EasyMock.expect(httpChannel.getRequest()).andReturn(request);
        EasyMock.expect(httpChannel.getResponse()).andReturn(response).times(2);
        EasyMock.expect(response.getStatus()).andReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        final java.lang.String requestUri = "/path/to/target";
        EasyMock.expect(httpServletRequest.getRequestURI()).andReturn(requestUri);
        EasyMock.expect(httpServletRequest.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)).andReturn(th);
        final java.io.StringWriter writer = new java.io.StringWriter();
        EasyMock.expect(httpServletResponse.getWriter()).andReturn(new java.io.PrintWriter(writer));
        EasyMock.expect(propertiesProvider.get()).andReturn(null).anyTimes();
        replayAll();
        final java.lang.String expectedResponse = ("{\"status\":500,\"message\":\"Internal server error, please refer the exception by " + requestId) + " in the server log file\"}";
        final java.lang.String expectedErrorMessage = (("Internal server error, please refer the exception by " + requestId) + " in the server log file, requestURI: ") + requestUri;
        org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler = new org.apache.ambari.server.api.AmbariErrorHandler(gson, propertiesProvider);
        ambariErrorHandler.setShowStacks(false);
        ambariErrorHandler.handle(target, request, httpServletRequest, httpServletResponse);
        org.junit.Assert.assertEquals(expectedResponse, writer.toString());
        org.junit.Assert.assertEquals(expectedErrorMessage, captureLogMessage.getValue());
        verifyAll();
    }

    @org.junit.Test
    public void testHandleGeneralError() throws java.lang.Exception {
        mockStatic(org.eclipse.jetty.server.HttpConnection.class);
        Mockito.when(org.eclipse.jetty.server.HttpConnection.getCurrentConnection()).thenReturn(httpConnection);
        EasyMock.expect(httpConnection.getHttpChannel()).andReturn(httpChannel);
        EasyMock.expect(httpChannel.getRequest()).andReturn(request);
        EasyMock.expect(httpChannel.getResponse()).andReturn(response).anyTimes();
        EasyMock.expect(response.getStatus()).andReturn(HttpStatus.SC_BAD_REQUEST);
        final java.io.StringWriter writer = new java.io.StringWriter();
        EasyMock.expect(httpServletResponse.getWriter()).andReturn(new java.io.PrintWriter(writer));
        EasyMock.expect(propertiesProvider.get()).andReturn(null).anyTimes();
        replayAll();
        final java.lang.String expectedResponse = "{\"status\":400,\"message\":\"Bad Request\"}";
        org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler = new org.apache.ambari.server.api.AmbariErrorHandler(gson, propertiesProvider);
        ambariErrorHandler.handle(target, request, httpServletRequest, httpServletResponse);
        java.lang.System.out.println(writer.toString());
        org.junit.Assert.assertEquals(expectedResponse, writer.toString());
        verifyAll();
    }
}