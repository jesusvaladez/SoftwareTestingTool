package org.apache.ambari.server.controller;
import javassist.util.proxy.ProxyFactory;
@javax.inject.Singleton
public class AmbariHandlerList extends org.eclipse.jetty.server.handler.HandlerCollection implements org.apache.ambari.server.view.ViewInstanceHandlerList {
    private static final java.util.regex.Pattern VIEW_RESOURCE_TARGET_PATTERN = java.util.regex.Pattern.compile("/api/(\\S+)/views/(\\S+)/versions/(\\S+)/instances/(\\S+)/resources/(\\S+)");

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewRegistry viewRegistry;

    @javax.inject.Inject
    org.eclipse.jetty.server.session.SessionHandler sessionHandler;

    @javax.inject.Inject
    javax.inject.Provider<org.eclipse.jetty.webapp.WebAppContext> webAppContextProvider;

    @javax.inject.Inject
    org.apache.ambari.server.api.AmbariPersistFilter persistFilter;

    @javax.inject.Inject
    org.springframework.web.filter.DelegatingFilterProxy springSecurityFilter;

    @javax.inject.Inject
    org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilter ambariViewsSecurityHeaderFilter;

    @javax.inject.Inject
    org.apache.ambari.server.controller.SessionHandlerConfigurer sessionHandlerConfigurer;

    @javax.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @javax.inject.Inject
    org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler;

    private final java.util.Map<org.apache.ambari.server.orm.entities.ViewInstanceEntity, org.eclipse.jetty.webapp.WebAppContext> viewHandlerMap = new java.util.HashMap<>();

    private final java.util.Collection<org.eclipse.jetty.server.Handler> nonViewHandlers = new java.util.HashSet<>();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariHandlerList.class);

    public AmbariHandlerList() {
        super(true);
    }

    @java.lang.Override
    public void handle(java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getTargetView(target);
        if (viewEntity == null) {
            processHandlers(target, baseRequest, request, response);
        } else {
            java.lang.ClassLoader contextClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
            try {
                java.lang.ClassLoader viewClassLoader = viewEntity.getClassLoader();
                if (viewClassLoader == null) {
                    org.apache.ambari.server.controller.AmbariHandlerList.LOG.debug("No class loader associated with view {}.", viewEntity.getName());
                } else {
                    java.lang.Thread.currentThread().setContextClassLoader(viewClassLoader);
                }
                processHandlers(target, baseRequest, request, response);
            } finally {
                java.lang.Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        }
    }

    @java.lang.Override
    public void addHandler(org.eclipse.jetty.server.Handler handler) {
        nonViewHandlers.add(handler);
        super.addHandler(handler);
    }

    @java.lang.Override
    public void addViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) throws org.apache.ambari.view.SystemException {
        org.eclipse.jetty.webapp.WebAppContext handler = getHandler(viewInstanceDefinition);
        viewHandlerMap.put(viewInstanceDefinition, handler);
        super.addHandler(handler);
        if ((!isStopped()) && (!isStopping())) {
            try {
                handler.start();
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.view.SystemException("Caught exception adding a view instance.", e);
            }
        }
        handler.getSessionHandler().setSessionCache(sessionHandler.getSessionCache());
    }

    @java.lang.Override
    public void shareSessionCacheToViews(org.eclipse.jetty.server.session.SessionCache serverSessionCache) {
        for (org.eclipse.jetty.webapp.WebAppContext webAppContext : viewHandlerMap.values()) {
            webAppContext.getSessionHandler().setSessionCache(serverSessionCache);
        }
    }

    @java.lang.Override
    public void removeViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        org.eclipse.jetty.server.Handler handler = viewHandlerMap.get(viewInstanceDefinition);
        if (handler != null) {
            viewHandlerMap.remove(viewInstanceDefinition);
            removeHandler(handler);
        }
    }

    private void processHandlers(java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        final org.eclipse.jetty.server.Handler[] handlers = getHandlers();
        if ((handlers != null) && isStarted()) {
            if (!processHandlers(viewHandlerMap.values(), target, baseRequest, request, response)) {
                processHandlers(nonViewHandlers, target, baseRequest, request, response);
            }
        }
    }

    private boolean processHandlers(java.util.Collection<? extends org.eclipse.jetty.server.Handler> handlers, java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        for (org.eclipse.jetty.server.Handler handler : handlers) {
            handler.handle(target, baseRequest, request, response);
            if (baseRequest.isHandled()) {
                return true;
            }
        }
        return false;
    }

    private org.eclipse.jetty.webapp.WebAppContext getHandler(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) throws org.apache.ambari.view.SystemException {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = viewInstanceDefinition.getViewEntity();
        org.eclipse.jetty.webapp.WebAppContext webAppContext = webAppContextProvider.get();
        webAppContext.setWar(viewDefinition.getArchive());
        webAppContext.setContextPath(viewInstanceDefinition.getContextPath());
        webAppContext.setClassLoader(viewInstanceDefinition.getViewEntity().getClassLoader());
        webAppContext.setAttribute(org.apache.ambari.view.ViewContext.CONTEXT_ATTRIBUTE, new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry));
        webAppContext.setSessionHandler(new org.apache.ambari.server.controller.AmbariHandlerList.SharedSessionHandler(sessionHandler));
        webAppContext.addFilter(new org.eclipse.jetty.servlet.FilterHolder(ambariViewsSecurityHeaderFilter), "/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
        webAppContext.addFilter(new org.eclipse.jetty.servlet.FilterHolder(persistFilter), "/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
        webAppContext.addFilter(new org.eclipse.jetty.servlet.FilterHolder(springSecurityFilter), "/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
        webAppContext.setAllowNullPathInfo(true);
        if (webAppContext.getErrorHandler() != null) {
            org.eclipse.jetty.server.handler.ErrorHandler errorHandlerProxy = createAmbariViewErrorHandlerProxy(webAppContext.getErrorHandler());
            if (errorHandlerProxy != null) {
                webAppContext.setErrorHandler(errorHandlerProxy);
            }
            webAppContext.getErrorHandler().setShowStacks(configuration.isServerShowErrorStacks());
        }
        return webAppContext;
    }

    private org.eclipse.jetty.server.handler.ErrorHandler createAmbariViewErrorHandlerProxy(org.eclipse.jetty.server.handler.ErrorHandler errorHandler) {
        org.eclipse.jetty.server.handler.ErrorHandler proxy = null;
        try {
            javassist.util.proxy.ProxyFactory proxyFactory = new javassist.util.proxy.ProxyFactory();
            proxyFactory.setSuperclass(org.eclipse.jetty.server.handler.ErrorHandler.class);
            proxy = ((org.eclipse.jetty.server.handler.ErrorHandler) (proxyFactory.create(new java.lang.Class[0], new java.lang.Object[0], new org.apache.ambari.server.api.AmbariViewErrorHandlerProxy(errorHandler, ambariErrorHandler))));
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariHandlerList.LOG.error("An error occurred while instantiating the error handler proxy instance", e);
        }
        return proxy;
    }

    private org.apache.ambari.server.orm.entities.ViewEntity getTargetView(java.lang.String target) {
        java.util.regex.Matcher matcher = org.apache.ambari.server.controller.AmbariHandlerList.VIEW_RESOURCE_TARGET_PATTERN.matcher(target);
        return matcher.matches() ? viewRegistry.getDefinition(matcher.group(2), matcher.group(3)) : null;
    }

    private class SharedSessionHandler extends org.eclipse.jetty.server.session.SessionHandler {
        public SharedSessionHandler(org.eclipse.jetty.server.session.SessionHandler sessionHandler) {
            setSessionIdManager(sessionHandler.getSessionIdManager());
            sessionHandlerConfigurer.configureSessionHandler(this);
        }

        @java.lang.Override
        protected void doStop() throws java.lang.Exception {
        }
    }
}