package org.apache.ambari.server.configuration.spring;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
@org.springframework.context.annotation.Configuration
@org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
@org.springframework.context.annotation.ComponentScan(basePackageClasses = { org.apache.ambari.server.agent.stomp.HeartbeatController.class })
@org.springframework.context.annotation.Import({ org.apache.ambari.server.configuration.spring.RootStompConfig.class, org.apache.ambari.server.configuration.spring.GuiceBeansConfig.class })
public class AgentStompConfig extends org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer {
    private org.apache.ambari.server.configuration.Configuration configuration;

    private final javax.servlet.ServletContext servletContext;

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.configuration.spring.AgentRegisteringQueueChecker agentRegisteringQueueChecker;

    public AgentStompConfig(javax.servlet.ServletContext servletContext, com.google.inject.Injector injector) {
        this.servletContext = servletContext;
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.events.listeners.requests.STOMPUpdateListener requestSTOMPListener(com.google.inject.Injector injector) {
        return new org.apache.ambari.server.events.listeners.requests.STOMPUpdateListener(injector, org.apache.ambari.server.events.DefaultMessageEmitter.DEFAULT_AGENT_EVENT_TYPES);
    }

    public org.springframework.web.socket.server.support.DefaultHandshakeHandler getHandshakeHandler() {
        org.eclipse.jetty.websocket.server.WebSocketServerFactory webSocketServerFactory = new org.eclipse.jetty.websocket.server.WebSocketServerFactory(servletContext);
        webSocketServerFactory.getPolicy().setMaxTextMessageSize(configuration.getStompMaxIncomingMessageSize());
        return new org.springframework.web.socket.server.support.DefaultHandshakeHandler(new org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy(webSocketServerFactory));
    }

    @java.lang.Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/v1").setHandshakeHandler(getHandshakeHandler()).setAllowedOriginPatterns("*");
    }

    @java.lang.Override
    public void configureClientInboundChannel(org.springframework.messaging.simp.config.ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(configuration.getSpringMessagingThreadPoolSize());
    }

    @java.lang.Override
    public void configureClientOutboundChannel(org.springframework.messaging.simp.config.ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(configuration.getSpringMessagingThreadPoolSize());
        registration.setInterceptors(agentRegisteringQueueChecker);
    }

    @java.lang.Override
    public void configureWebSocketTransport(org.springframework.web.socket.config.annotation.WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(configuration.getStompMaxIncomingMessageSize());
        registration.setSendBufferSizeLimit(configuration.getStompMaxBufferMessageSize());
    }

    @java.lang.Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.setPreservePublishOrder(true);
    }
}