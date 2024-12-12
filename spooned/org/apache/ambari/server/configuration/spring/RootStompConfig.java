package org.apache.ambari.server.configuration.spring;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
@org.springframework.context.annotation.Configuration
public class RootStompConfig {
    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.messaging.simp.SimpMessagingTemplate brokerTemplate;

    private final javax.servlet.ServletContext servletContext;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    public RootStompConfig(javax.servlet.ServletContext servletContext, com.google.inject.Injector injector) {
        this.servletContext = servletContext;
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.events.DefaultMessageEmitter defaultMessageEmitter(com.google.inject.Injector injector) {
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        return new org.apache.ambari.server.events.DefaultMessageEmitter(injector.getInstance(org.apache.ambari.server.agent.AgentSessionManager.class), brokerTemplate, injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class), configuration.getExecutionCommandsRetryCount(), configuration.getExecutionCommandsRetryInterval());
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.web.socket.server.support.DefaultHandshakeHandler handshakeHandler() {
        return new org.springframework.web.socket.server.support.DefaultHandshakeHandler(new org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy(new org.eclipse.jetty.websocket.server.WebSocketServerFactory(servletContext)));
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void configureRegistryCacheSize(org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler simpleBrokerMessageHandler) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry defaultSubscriptionRegistry = new org.apache.ambari.server.agent.stomp.AmbariSubscriptionRegistry(configuration.getSubscriptionRegistryCacheSize());
        simpleBrokerMessageHandler.setSubscriptionRegistry(defaultSubscriptionRegistry);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void configureGlobal(org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler messageHandler) {
        java.util.List<org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler> handlers = new java.util.ArrayList<>(messageHandler.getReturnValueHandlers());
        java.util.List<org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler> changedHandlers = new java.util.ArrayList<>();
        boolean handlerReplaced = false;
        for (org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler handler : handlers) {
            if ((handler instanceof org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler) && (!handlerReplaced)) {
                org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler sendHandler = ((org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler) (handler));
                org.apache.ambari.server.api.AmbariSendToMethodReturnValueHandler ambariSendToMethodReturnValueHandler = new org.apache.ambari.server.api.AmbariSendToMethodReturnValueHandler(brokerTemplate, true);
                ambariSendToMethodReturnValueHandler.setHeaderInitializer(sendHandler.getHeaderInitializer());
                changedHandlers.add(ambariSendToMethodReturnValueHandler);
                handlerReplaced = true;
            } else {
                changedHandlers.add(handler);
            }
        }
        messageHandler.setReturnValueHandlers(null);
        messageHandler.setReturnValueHandlers(changedHandlers);
    }

    @org.springframework.web.bind.annotation.ControllerAdvice
    public static class ExceptionHandlingAdvice {
        private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.spring.RootStompConfig.ExceptionHandlingAdvice.class);

        @org.springframework.messaging.handler.annotation.MessageExceptionHandler(java.lang.Exception.class)
        @org.springframework.messaging.simp.annotation.SendToUser("/")
        public org.springframework.messaging.support.ErrorMessage handle(java.lang.Exception e) {
            org.apache.ambari.server.configuration.spring.RootStompConfig.ExceptionHandlingAdvice.LOG.error("Exception caught while processing a message: " + e.getMessage(), e);
            return new org.springframework.messaging.support.ErrorMessage(new org.apache.ambari.server.AmbariException(e.getMessage()));
        }
    }
}