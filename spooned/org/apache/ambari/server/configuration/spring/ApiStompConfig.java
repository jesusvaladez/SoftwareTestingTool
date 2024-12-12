package org.apache.ambari.server.configuration.spring;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
@org.springframework.context.annotation.Configuration
@org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
@org.springframework.context.annotation.ComponentScan(basePackageClasses = { org.apache.ambari.server.api.stomp.TestController.class })
@org.springframework.context.annotation.Import(org.apache.ambari.server.configuration.spring.RootStompConfig.class)
public class ApiStompConfig extends org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer {
    private final java.lang.String HEARTBEAT_THREAD_NAME = "ws-heartbeat-thread-";

    private final int HEARTBEAT_POOL_SIZE = 1;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    public ApiStompConfig(com.google.inject.Injector injector) {
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.events.listeners.requests.STOMPUpdateListener requestSTOMPListener(com.google.inject.Injector injector) {
        return new org.apache.ambari.server.events.listeners.requests.STOMPUpdateListener(injector, org.apache.ambari.server.events.DefaultMessageEmitter.DEFAULT_API_EVENT_TYPES);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscribtions(com.google.inject.Injector injector) {
        return injector.getInstance(org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.class);
    }

    @java.lang.Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/v1").setAllowedOriginPatterns("*").withSockJS().setHeartbeatTime(configuration.getAPIHeartbeatInterval());
    }

    @java.lang.Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler taskScheduler = new org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(HEARTBEAT_POOL_SIZE);
        taskScheduler.setThreadNamePrefix(HEARTBEAT_THREAD_NAME);
        taskScheduler.initialize();
        registry.setPreservePublishOrder(true).enableSimpleBroker("/").setTaskScheduler(taskScheduler).setHeartbeatValue(new long[]{ configuration.getAPIHeartbeatInterval(), configuration.getAPIHeartbeatInterval() });
    }
}