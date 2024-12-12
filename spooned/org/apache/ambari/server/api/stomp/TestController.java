package org.apache.ambari.server.api.stomp;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
@org.springframework.stereotype.Controller
@org.springframework.messaging.handler.annotation.MessageMapping("/test")
@org.springframework.messaging.simp.annotation.SendToUser("/")
public class TestController {
    @org.springframework.messaging.simp.annotation.SubscribeMapping("/echo")
    public java.lang.String echo(java.lang.String payload) {
        return payload;
    }

    @org.springframework.messaging.simp.annotation.SubscribeMapping("/time")
    public java.lang.Long time() {
        return java.lang.System.currentTimeMillis();
    }
}