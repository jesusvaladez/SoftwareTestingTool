package org.apache.ambari.server.api.stomp;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
@org.springframework.messaging.handler.annotation.MessageMapping("clusters")
@org.springframework.messaging.simp.annotation.SendToUser(destinations = "/")
public class ClustersController {}