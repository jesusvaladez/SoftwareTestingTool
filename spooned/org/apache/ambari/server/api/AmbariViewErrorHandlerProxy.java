package org.apache.ambari.server.api;
import javassist.util.proxy.MethodHandler;
import org.apache.http.HttpStatus;
public class AmbariViewErrorHandlerProxy extends org.eclipse.jetty.server.handler.ErrorHandler implements javassist.util.proxy.MethodHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.AmbariViewErrorHandlerProxy.class);

    private final org.eclipse.jetty.server.handler.ErrorHandler webAppErrorHandler;

    private final org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler;

    public AmbariViewErrorHandlerProxy(org.eclipse.jetty.server.handler.ErrorHandler webAppErrorHandler, org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler) {
        this.webAppErrorHandler = webAppErrorHandler;
        this.ambariErrorHandler = ambariErrorHandler;
    }

    @java.lang.Override
    public void handle(java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
        if (isInternalError(request, response)) {
            ambariErrorHandler.handle(target, baseRequest, request, response);
        } else {
            webAppErrorHandler.handle(target, baseRequest, request, response);
        }
    }

    @java.lang.Override
    public void doError(java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
        if (isInternalError(request, response)) {
            ambariErrorHandler.handle(target, baseRequest, request, response);
        } else {
            webAppErrorHandler.doError(target, baseRequest, request, response);
        }
    }

    private boolean isInternalError(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        java.lang.Throwable th = ((java.lang.Throwable) (request.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)));
        return (null != th) && (response.getStatus() == org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @java.lang.Override
    public void setShowStacks(boolean showStacks) {
        ambariErrorHandler.setShowStacks(showStacks);
        webAppErrorHandler.setShowStacks(showStacks);
    }

    @java.lang.Override
    public java.lang.Object invoke(java.lang.Object self, java.lang.reflect.Method thisMethod, java.lang.reflect.Method proceed, java.lang.Object[] args) throws java.lang.Throwable {
        org.apache.ambari.server.api.AmbariViewErrorHandlerProxy.LOGGER.debug("invoked method: " + thisMethod.getName());
        java.lang.reflect.Method m = findDeclaredMethod(this.getClass(), thisMethod);
        if (m != null) {
            return m.invoke(this, args);
        }
        m = findMethod(webAppErrorHandler.getClass(), thisMethod);
        if (m != null) {
            return m.invoke(webAppErrorHandler, args);
        }
        return null;
    }

    private java.lang.reflect.Method findDeclaredMethod(java.lang.Class<?> clazz, java.lang.reflect.Method method) {
        try {
            return clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
        } catch (java.lang.NoSuchMethodException e) {
            return null;
        }
    }

    private java.lang.reflect.Method findMethod(java.lang.Class<?> clazz, java.lang.reflect.Method method) {
        try {
            return clazz.getMethod(method.getName(), method.getParameterTypes());
        } catch (java.lang.NoSuchMethodException e) {
            return null;
        }
    }
}