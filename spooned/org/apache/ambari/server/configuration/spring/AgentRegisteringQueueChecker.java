package org.apache.ambari.server.configuration.spring;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
public class AgentRegisteringQueueChecker extends org.springframework.messaging.support.ChannelInterceptorAdapter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue.class);

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue agentsRegistrationQueue;

    @java.lang.Override
    public org.springframework.messaging.Message<?> preSend(org.springframework.messaging.Message<?> message, org.springframework.messaging.MessageChannel channel) {
        org.springframework.messaging.simp.stomp.StompHeaderAccessor headerAccessor = org.springframework.messaging.simp.stomp.StompHeaderAccessor.wrap(message);
        java.lang.String sessionId = headerAccessor.getHeader("simpSessionId").toString();
        if (SimpMessageType.CONNECT_ACK.equals(headerAccessor.getMessageType()) && (!agentsRegistrationQueue.offer(sessionId))) {
            org.springframework.messaging.simp.stomp.StompHeaderAccessor headerAccessorError = org.springframework.messaging.simp.stomp.StompHeaderAccessor.create(StompCommand.ERROR);
            headerAccessorError.setHeader("simpSessionId", sessionId);
            headerAccessorError.setHeader("simpConnectMessage", headerAccessor.getHeader("simpConnectMessage").toString());
            headerAccessorError.setMessage("Connection not allowed");
            return org.springframework.messaging.support.MessageBuilder.createMessage(new byte[0], headerAccessorError.getMessageHeaders());
        } else if (SimpMessageType.DISCONNECT_ACK.equals(headerAccessor.getMessageType())) {
            agentsRegistrationQueue.complete(sessionId);
        }
        return message;
    }
}