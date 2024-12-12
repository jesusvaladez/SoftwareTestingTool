package org.apache.ambari.server.controller;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariHandlerListTest {
    private final org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilter ambariViewsSecurityHeaderFilter = EasyMock.createNiceMock(org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilter.class);

    private final org.apache.ambari.server.api.AmbariPersistFilter persistFilter = EasyMock.createNiceMock(org.apache.ambari.server.api.AmbariPersistFilter.class);

    private final org.springframework.web.filter.DelegatingFilterProxy springSecurityFilter = EasyMock.createNiceMock(org.springframework.web.filter.DelegatingFilterProxy.class);

    private final org.eclipse.jetty.server.session.SessionHandler sessionHandler = EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class);

    private final org.eclipse.jetty.server.SessionIdManager sessionIdManager = EasyMock.createNiceMock(org.eclipse.jetty.server.SessionIdManager.class);

    private final org.apache.ambari.server.controller.SessionHandlerConfigurer sessionHandlerConfigurer = EasyMock.createNiceMock(org.apache.ambari.server.controller.SessionHandlerConfigurer.class);

    private final org.eclipse.jetty.server.session.SessionCache sessionCache = EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionCache.class);

    private final org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);

    @org.junit.Test
    public void testAddViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        final org.eclipse.jetty.webapp.WebAppContext handler = EasyMock.createNiceMock(org.eclipse.jetty.webapp.WebAppContext.class);
        org.eclipse.jetty.server.Server server = EasyMock.createNiceMock(org.eclipse.jetty.server.Server.class);
        EasyMock.expect(handler.getServer()).andReturn(server);
        EasyMock.expect(handler.getChildHandlers()).andReturn(new org.eclipse.jetty.server.Handler[]{  });
        EasyMock.expect(handler.getSessionHandler()).andReturn(EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class));
        handler.setServer(null);
        EasyMock.expect(sessionHandler.getSessionCache()).andReturn(sessionCache);
        org.easymock.Capture<org.eclipse.jetty.servlet.FilterHolder> securityHeaderFilterCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.jetty.servlet.FilterHolder> persistFilterCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.eclipse.jetty.servlet.FilterHolder> securityFilterCapture = org.easymock.EasyMock.newCapture();
        handler.addFilter(EasyMock.capture(securityHeaderFilterCapture), EasyMock.eq("/*"), EasyMock.eq(org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES));
        handler.addFilter(EasyMock.capture(persistFilterCapture), EasyMock.eq("/*"), EasyMock.eq(org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES));
        handler.addFilter(EasyMock.capture(securityFilterCapture), EasyMock.eq("/*"), EasyMock.eq(org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES));
        handler.setAllowNullPathInfo(true);
        final boolean showErrorStacks = true;
        EasyMock.expect(configuration.isServerShowErrorStacks()).andReturn(showErrorStacks);
        org.eclipse.jetty.server.handler.ErrorHandler errorHandler = EasyMock.createNiceMock(org.eclipse.jetty.server.handler.ErrorHandler.class);
        org.easymock.Capture<java.lang.Boolean> showStackCapture = org.easymock.EasyMock.newCapture();
        errorHandler.setShowStacks(org.easymock.EasyMock.captureBoolean(showStackCapture));
        EasyMock.expect(handler.getErrorHandler()).andReturn(errorHandler).times(3);
        EasyMock.replay(handler, server, sessionHandler, configuration, errorHandler);
        org.apache.ambari.server.controller.AmbariHandlerList handlerList = getAmbariHandlerList(handler);
        handlerList.addViewInstance(viewInstanceEntity);
        java.util.ArrayList<org.eclipse.jetty.server.Handler> handlers = new java.util.ArrayList<>(java.util.Arrays.asList(handlerList.getHandlers()));
        org.junit.Assert.assertTrue(handlers.contains(handler));
        org.junit.Assert.assertEquals(ambariViewsSecurityHeaderFilter, securityHeaderFilterCapture.getValue().getFilter());
        org.junit.Assert.assertEquals(persistFilter, persistFilterCapture.getValue().getFilter());
        org.junit.Assert.assertEquals(springSecurityFilter, securityFilterCapture.getValue().getFilter());
        org.junit.Assert.assertEquals(showErrorStacks, showStackCapture.getValue());
        EasyMock.verify(handler, server, sessionHandler, configuration, errorHandler);
    }

    @org.junit.Test
    public void testRemoveViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        final org.eclipse.jetty.webapp.WebAppContext handler = EasyMock.createNiceMock(org.eclipse.jetty.webapp.WebAppContext.class);
        org.eclipse.jetty.server.Server server = EasyMock.createNiceMock(org.eclipse.jetty.server.Server.class);
        EasyMock.expect(handler.getServer()).andReturn(server);
        EasyMock.expect(handler.getChildHandlers()).andReturn(new org.eclipse.jetty.server.Handler[]{  });
        EasyMock.expect(handler.getSessionHandler()).andReturn(EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class));
        handler.setServer(null);
        EasyMock.expect(sessionHandler.getSessionCache()).andReturn(sessionCache);
        EasyMock.replay(handler, server, sessionHandler);
        org.apache.ambari.server.controller.AmbariHandlerList handlerList = getAmbariHandlerList(handler);
        handlerList.addViewInstance(viewInstanceEntity);
        java.util.ArrayList<org.eclipse.jetty.server.Handler> handlers = new java.util.ArrayList<>(java.util.Arrays.asList(handlerList.getHandlers()));
        org.junit.Assert.assertTrue(handlers.contains(handler));
        handlerList.removeViewInstance(viewInstanceEntity);
        handlers = new java.util.ArrayList<>(java.util.Arrays.asList(handlerList.getHandlers()));
        org.junit.Assert.assertFalse(handlers.contains(handler));
        EasyMock.verify(handler, server, sessionHandler);
    }

    @org.junit.Test
    public void testHandle() throws java.lang.Exception {
        final org.eclipse.jetty.webapp.WebAppContext handler = EasyMock.createNiceMock(org.eclipse.jetty.webapp.WebAppContext.class);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        java.lang.ClassLoader classLoader = EasyMock.createNiceMock(java.lang.ClassLoader.class);
        org.eclipse.jetty.server.Request baseRequest = EasyMock.createNiceMock(org.eclipse.jetty.server.Request.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createNiceMock(javax.servlet.http.HttpServletResponse.class);
        EasyMock.expect(viewRegistry.getDefinition("TEST", "1.0.0")).andReturn(viewEntity).anyTimes();
        EasyMock.expect(viewEntity.getClassLoader()).andReturn(classLoader).anyTimes();
        EasyMock.expect(handler.isStarted()).andReturn(true).anyTimes();
        EasyMock.expect(handler.getChildHandlers()).andReturn(new org.eclipse.jetty.server.Handler[]{  });
        EasyMock.replay(handler, viewRegistry, viewEntity);
        handler.handle("/api/v1/views/TEST/versions/1.0.0/instances/INSTANCE_1/resources/test", baseRequest, request, response);
        org.apache.ambari.server.controller.AmbariHandlerList handlerList = getAmbariHandlerList(handler);
        handlerList.viewRegistry = viewRegistry;
        handlerList.start();
        handlerList.addHandler(handler);
        handlerList.handle("/api/v1/views/TEST/versions/1.0.0/instances/INSTANCE_1/resources/test", baseRequest, request, response);
        EasyMock.verify(handler, viewRegistry, viewEntity);
    }

    private org.apache.ambari.server.controller.AmbariHandlerList getAmbariHandlerList(final org.eclipse.jetty.webapp.WebAppContext handler) {
        org.apache.ambari.server.controller.AmbariHandlerList handlerList = new org.apache.ambari.server.controller.AmbariHandlerList();
        sessionHandler.setSessionIdManager(sessionIdManager);
        handlerList.webAppContextProvider = new org.apache.ambari.server.controller.AmbariHandlerListTest.HandlerProvider(handler);
        handlerList.ambariViewsSecurityHeaderFilter = ambariViewsSecurityHeaderFilter;
        handlerList.persistFilter = persistFilter;
        handlerList.springSecurityFilter = springSecurityFilter;
        handlerList.sessionHandler = sessionHandler;
        handlerList.sessionHandlerConfigurer = sessionHandlerConfigurer;
        handlerList.configuration = configuration;
        return handlerList;
    }

    private static class HandlerProvider implements javax.inject.Provider<org.eclipse.jetty.webapp.WebAppContext> {
        private final org.eclipse.jetty.webapp.WebAppContext context;

        private HandlerProvider(org.eclipse.jetty.webapp.WebAppContext context) {
            this.context = context;
        }

        @java.lang.Override
        public org.eclipse.jetty.webapp.WebAppContext get() {
            return context;
        }
    }
}