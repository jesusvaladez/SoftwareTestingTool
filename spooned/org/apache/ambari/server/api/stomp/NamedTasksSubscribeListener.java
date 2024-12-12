package org.apache.ambari.server.api.stomp;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
@org.springframework.stereotype.Component
public class NamedTasksSubscribeListener {
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.stomp.NamedTasksSubscribeListener.class);

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscriptions;

    @org.springframework.context.event.EventListener
    public void subscribe(org.springframework.web.socket.messaging.SessionSubscribeEvent sse) {
        org.springframework.messaging.MessageHeaders msgHeaders = sse.getMessage().getHeaders();
        java.lang.String sessionId = ((java.lang.String) (msgHeaders.get("simpSessionId")));
        java.lang.String destination = ((java.lang.String) (msgHeaders.get("simpDestination")));
        java.lang.String id = ((java.lang.String) (msgHeaders.get("simpSubscriptionId")));
        if (((sessionId != null) && (destination != null)) && (id != null)) {
            namedTasksSubscriptions.addDestination(sessionId, destination, id);
        }
        org.apache.ambari.server.api.stomp.NamedTasksSubscribeListener.LOG.debug(java.lang.String.format("API subscribe was arrived with sessionId = %s, destination = %s and id = %s", sessionId, destination, id));
    }

    @org.springframework.context.event.EventListener
    public void unsubscribe(org.springframework.web.socket.messaging.SessionUnsubscribeEvent suse) {
        org.springframework.messaging.MessageHeaders msgHeaders = suse.getMessage().getHeaders();
        java.lang.String sessionId = ((java.lang.String) (msgHeaders.get("simpSessionId")));
        java.lang.String id = ((java.lang.String) (msgHeaders.get("simpSubscriptionId")));
        if ((sessionId != null) && (id != null)) {
            namedTasksSubscriptions.removeId(sessionId, id);
        }
        org.apache.ambari.server.api.stomp.NamedTasksSubscribeListener.LOG.debug(java.lang.String.format("API unsubscribe was arrived with sessionId = %s and id = %s", sessionId, id));
    }

    @org.springframework.context.event.EventListener
    public void disconnect(org.springframework.web.socket.messaging.SessionDisconnectEvent sde) {
        org.springframework.messaging.MessageHeaders msgHeaders = sde.getMessage().getHeaders();
        java.lang.String sessionId = ((java.lang.String) (msgHeaders.get("simpSessionId")));
        if (sessionId != null) {
            namedTasksSubscriptions.removeSession(sessionId);
        }
        org.apache.ambari.server.api.stomp.NamedTasksSubscribeListener.LOG.debug(java.lang.String.format("API disconnect was arrived with sessionId = %s", sessionId));
    }
}