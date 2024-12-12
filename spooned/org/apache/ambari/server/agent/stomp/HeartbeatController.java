package org.apache.ambari.server.agent.stomp;
import com.google.inject.persist.UnitOfWork;
import javax.ws.rs.WebApplicationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
@org.springframework.stereotype.Controller
@org.springframework.messaging.simp.annotation.SendToUser("/")
@org.springframework.messaging.handler.annotation.MessageMapping("/")
@org.springframework.context.annotation.Import(org.apache.ambari.server.configuration.spring.GuiceBeansConfig.class)
public class HeartbeatController {
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.HeartbeatController.class);

    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    private final org.apache.ambari.server.state.cluster.ClustersImpl clusters;

    private final org.apache.ambari.server.agent.AgentSessionManager agentSessionManager;

    private final java.util.concurrent.LinkedBlockingQueue queue;

    private final java.util.concurrent.ThreadFactory threadFactoryExecutor = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-register-processor-%d").build();

    private final java.util.concurrent.ThreadFactory threadFactoryTimeout = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-register-timeout-%d").build();

    private final java.util.concurrent.ExecutorService executor;

    private final java.util.concurrent.ScheduledExecutorService scheduledExecutorService;

    private final com.google.inject.persist.UnitOfWork unitOfWork;

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue agentsRegistrationQueue;

    public HeartbeatController(com.google.inject.Injector injector) {
        hh = injector.getInstance(org.apache.ambari.server.agent.HeartBeatHandler.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.cluster.ClustersImpl.class);
        unitOfWork = injector.getInstance(com.google.inject.persist.UnitOfWork.class);
        agentSessionManager = injector.getInstance(org.apache.ambari.server.agent.AgentSessionManager.class);
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        queue = new java.util.concurrent.LinkedBlockingQueue(configuration.getAgentsRegistrationQueueSize());
        executor = new java.util.concurrent.ThreadPoolExecutor(configuration.getRegistrationThreadPoolSize(), configuration.getRegistrationThreadPoolSize(), 0L, java.util.concurrent.TimeUnit.MILLISECONDS, queue, threadFactoryExecutor);
        scheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(1, threadFactoryTimeout);
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/register")
    public java.util.concurrent.CompletableFuture<org.apache.ambari.server.agent.RegistrationResponse> register(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.Register message) throws javax.ws.rs.WebApplicationException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        java.util.concurrent.CompletableFuture<org.apache.ambari.server.agent.RegistrationResponse> completableFuture = new java.util.concurrent.CompletableFuture<>();
        java.util.concurrent.Future<org.apache.ambari.server.agent.RegistrationResponse> future = executor.submit(() -> {
            try {
                unitOfWork.begin();
                org.apache.ambari.server.agent.RegistrationResponse response = null;
                try {
                    response = hh.handleRegistration(message);
                    agentSessionManager.register(simpSessionId, clusters.getHost(message.getHostname()));
                    org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.debug("Sending registration response " + response);
                } catch (java.lang.Exception ex) {
                    org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.info(ex.getMessage(), ex);
                    response = new org.apache.ambari.server.agent.RegistrationResponse();
                    response.setResponseId(-1);
                    response.setResponseStatus(org.apache.ambari.server.agent.RegistrationStatus.FAILED);
                    response.setExitstatus(1);
                    response.setLog(ex.getMessage());
                    completableFuture.complete(response);
                    return response;
                }
                completableFuture.complete(response);
                return response;
            } finally {
                unitOfWork.end();
            }
        });
        scheduledExecutorService.schedule(new org.apache.ambari.server.agent.stomp.HeartbeatController.RegistrationTimeoutTask(future, completableFuture), 8, java.util.concurrent.TimeUnit.SECONDS);
        return completableFuture;
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/heartbeat")
    public org.apache.ambari.server.agent.HeartBeatResponse heartbeat(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.HeartBeat message) {
        try {
            unitOfWork.begin();
            if (org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.isDebugEnabled()) {
                org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.debug("Received Heartbeat message " + message);
            }
            org.apache.ambari.server.agent.HeartBeatResponse heartBeatResponse;
            try {
                if (!agentSessionManager.isRegistered(simpSessionId)) {
                    org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.error(java.lang.String.format("Host with [%s] sessionId not registered", simpSessionId));
                    return hh.createRegisterCommand();
                }
                message.setHostname(agentSessionManager.getHost(simpSessionId).getHostName());
                heartBeatResponse = hh.handleHeartBeat(message);
                agentsRegistrationQueue.complete(simpSessionId);
                if (org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.debug("Sending heartbeat response with response id " + heartBeatResponse.getResponseId());
                    org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.debug("Response details " + heartBeatResponse);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.agent.stomp.HeartbeatController.LOG.warn("Error in HeartBeat", e);
                throw new javax.ws.rs.WebApplicationException(500);
            }
            return heartBeatResponse;
        } finally {
            unitOfWork.end();
        }
    }

    private class RegistrationTimeoutTask implements java.lang.Runnable {
        private java.util.concurrent.Future<org.apache.ambari.server.agent.RegistrationResponse> task;

        private java.util.concurrent.CompletableFuture<org.apache.ambari.server.agent.RegistrationResponse> completableFuture;

        public RegistrationTimeoutTask(java.util.concurrent.Future<org.apache.ambari.server.agent.RegistrationResponse> task, java.util.concurrent.CompletableFuture<org.apache.ambari.server.agent.RegistrationResponse> completableFuture) {
            this.task = task;
            this.completableFuture = completableFuture;
        }

        @java.lang.Override
        public void run() {
            boolean cancelled = task.cancel(false);
            if (cancelled) {
                completableFuture.cancel(false);
            }
        }
    }
}