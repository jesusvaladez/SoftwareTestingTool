package org.apache.ambari.server.api;
import org.apache.http.HttpStatus;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.captureBoolean;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariViewErrorHandlerProxyTest {
    final org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler = EasyMock.createNiceMock(org.apache.ambari.server.api.AmbariErrorHandler.class);

    final org.eclipse.jetty.server.handler.ErrorHandler errorHandler = EasyMock.createNiceMock(org.eclipse.jetty.server.handler.ErrorHandler.class);

    final javax.servlet.http.HttpServletRequest httpServletRequest = EasyMock.createNiceMock(javax.servlet.http.HttpServletRequest.class);

    final javax.servlet.http.HttpServletResponse httpServletResponse = EasyMock.createNiceMock(javax.servlet.http.HttpServletResponse.class);

    final org.eclipse.jetty.server.Request request = EasyMock.createNiceMock(org.eclipse.jetty.server.Request.class);

    final java.lang.String target = "test/target/uri";

    @org.junit.Test
    public void testHandleInternalServerError() throws java.lang.Throwable {
        java.lang.Throwable th = EasyMock.createNiceMock(java.lang.Throwable.class);
        EasyMock.expect(httpServletRequest.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)).andReturn(th).anyTimes();
        EasyMock.expect(httpServletResponse.getStatus()).andReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR).anyTimes();
        ambariErrorHandler.handle(target, request, httpServletRequest, httpServletResponse);
        EasyMock.expectLastCall();
        EasyMock.replay(ambariErrorHandler, errorHandler, httpServletRequest, httpServletResponse, th);
        org.apache.ambari.server.api.AmbariViewErrorHandlerProxy proxy = new org.apache.ambari.server.api.AmbariViewErrorHandlerProxy(errorHandler, ambariErrorHandler);
        proxy.handle(target, request, httpServletRequest, httpServletResponse);
        EasyMock.verify(ambariErrorHandler, errorHandler, httpServletRequest, httpServletResponse, th);
    }

    @org.junit.Test
    public void testHandleGeneralError() throws java.lang.Throwable {
        java.lang.Throwable th = EasyMock.createNiceMock(java.lang.Throwable.class);
        EasyMock.expect(httpServletRequest.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)).andReturn(th).anyTimes();
        EasyMock.expect(httpServletResponse.getStatus()).andReturn(HttpStatus.SC_BAD_REQUEST).anyTimes();
        errorHandler.handle(target, request, httpServletRequest, httpServletResponse);
        EasyMock.expectLastCall();
        EasyMock.replay(ambariErrorHandler, errorHandler, httpServletRequest, httpServletResponse, th);
        org.apache.ambari.server.api.AmbariViewErrorHandlerProxy proxy = new org.apache.ambari.server.api.AmbariViewErrorHandlerProxy(errorHandler, ambariErrorHandler);
        proxy.handle(target, request, httpServletRequest, httpServletResponse);
        EasyMock.verify(ambariErrorHandler, errorHandler, httpServletRequest, httpServletResponse, th);
    }

    @org.junit.Test
    public void testShowStacks() {
        org.easymock.Capture<java.lang.Boolean> captureShowStacksErrorHandler = org.easymock.EasyMock.newCapture();
        errorHandler.setShowStacks(EasyMock.captureBoolean(captureShowStacksErrorHandler));
        EasyMock.expectLastCall();
        org.easymock.Capture<java.lang.Boolean> captureShowStacksAmbariErrorHandler = org.easymock.EasyMock.newCapture();
        ambariErrorHandler.setShowStacks(EasyMock.captureBoolean(captureShowStacksAmbariErrorHandler));
        EasyMock.expectLastCall();
        EasyMock.replay(errorHandler, ambariErrorHandler);
        org.apache.ambari.server.api.AmbariViewErrorHandlerProxy proxy = new org.apache.ambari.server.api.AmbariViewErrorHandlerProxy(errorHandler, ambariErrorHandler);
        proxy.setShowStacks(true);
        junit.framework.Assert.assertTrue(captureShowStacksErrorHandler.getValue());
        junit.framework.Assert.assertTrue(captureShowStacksAmbariErrorHandler.getValue());
        EasyMock.verify(errorHandler, ambariErrorHandler);
    }
}