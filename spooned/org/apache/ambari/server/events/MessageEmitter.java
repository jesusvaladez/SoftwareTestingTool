package org.apache.ambari.server.events;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
public abstract class MessageEmitter {
    protected static final java.util.concurrent.atomic.AtomicLong MESSAGE_ID = new java.util.concurrent.atomic.AtomicLong(0);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.MessageEmitter.class);

    public final int retryCount;

    public final int retryInterval;

    protected final org.apache.ambari.server.agent.AgentSessionManager agentSessionManager;

    protected final org.springframework.messaging.simp.SimpMessagingTemplate simpMessagingTemplate;

    protected final java.util.concurrent.ScheduledExecutorService emitExecutor = java.util.concurrent.Executors.newScheduledThreadPool(10, new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-message-emitter-%d").build());

    protected final java.util.concurrent.ExecutorService monitorExecutor = java.util.concurrent.Executors.newSingleThreadExecutor(new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-message-monitor-%d").build());

    protected final java.util.concurrent.ExecutorService retryExecutor = java.util.concurrent.Executors.newSingleThreadExecutor(new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-message-retry-%d").build());

    protected final org.apache.ambari.server.utils.ScheduledExecutorCompletionService<org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper> emitCompletionService = new org.apache.ambari.server.utils.ScheduledExecutorCompletionService(emitExecutor, new java.util.concurrent.LinkedBlockingQueue<>());

    protected java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper> unconfirmedMessages = new java.util.concurrent.ConcurrentHashMap<>();

    protected java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.util.concurrent.BlockingQueue<org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper>> messagesToEmit = new java.util.concurrent.ConcurrentHashMap<>();

    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    public MessageEmitter(org.apache.ambari.server.agent.AgentSessionManager agentSessionManager, org.springframework.messaging.simp.SimpMessagingTemplate simpMessagingTemplate, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, int retryCount, int retryInterval) {
        this.agentSessionManager = agentSessionManager;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.ambariEventPublisher = ambariEventPublisher;
        this.retryCount = retryCount;
        this.retryInterval = retryInterval;
        ambariEventPublisher.register(this);
        monitorExecutor.execute(new org.apache.ambari.server.events.MessageEmitter.MessagesToEmitMonitor());
        retryExecutor.execute(new org.apache.ambari.server.events.MessageEmitter.MessagesToRetryMonitor());
    }

    abstract void emitMessage(org.apache.ambari.server.events.STOMPEvent event) throws org.apache.ambari.server.AmbariException;

    public void emitMessageRetriable(org.apache.ambari.server.events.ExecutionCommandEvent event) {
        org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper wrapper = new org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper(0, org.apache.ambari.server.events.MessageEmitter.MESSAGE_ID.getAndIncrement(), event);
        java.lang.Long hostId = event.getHostId();
        messagesToEmit.compute(hostId, (id, hostMessages) -> {
            if (hostMessages == null) {
                org.apache.ambari.server.events.MessageEmitter.LOG.error("Trying to emit message to unregistered host with id {}", hostId);
                return null;
            } else {
                hostMessages.add(wrapper);
                return hostMessages;
            }
        });
    }

    public void processReceiveReport(java.lang.Long hostId, org.apache.ambari.server.agent.stomp.dto.AckReport ackReport) {
        java.lang.Long messageId = ackReport.getMessageId();
        if (org.apache.ambari.server.agent.stomp.dto.AckReport.AckStatus.OK.equals(ackReport.getStatus())) {
            unconfirmedMessages.compute(hostId, (id, commandInUse) -> {
                if ((commandInUse != null) && commandInUse.getMessageId().equals(ackReport.getMessageId())) {
                    return null;
                } else {
                    org.apache.ambari.server.events.MessageEmitter.LOG.warn("OK agent report was received again for already complete command with message id {}", messageId);
                }
                return commandInUse;
            });
        } else {
            org.apache.ambari.server.events.MessageEmitter.LOG.error("Received {} agent report for execution command with messageId {} with following reason: {}", ackReport.getStatus(), messageId, ackReport.getReason());
        }
    }

    protected abstract java.lang.String getDestination(org.apache.ambari.server.events.STOMPEvent stompEvent);

    protected org.springframework.messaging.MessageHeaders createHeaders(java.lang.String sessionId) {
        return createHeaders(sessionId, null);
    }

    protected org.springframework.messaging.MessageHeaders createHeaders(java.lang.String sessionId, java.lang.Long messageId) {
        org.springframework.messaging.simp.SimpMessageHeaderAccessor headerAccessor = org.springframework.messaging.simp.SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        if (messageId != null) {
            headerAccessor.setNativeHeader("messageId", java.lang.Long.toString(messageId));
        }
        return headerAccessor.getMessageHeaders();
    }

    protected void emitMessageToAll(org.apache.ambari.server.events.STOMPEvent event) {
        org.apache.ambari.server.events.MessageEmitter.LOG.debug("Received status update event {}", event);
        simpMessagingTemplate.convertAndSend(getDestination(event), event);
    }

    protected void emitMessageToHost(org.apache.ambari.server.events.STOMPHostEvent event) throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.Long hostId = event.getHostId();
        java.lang.String sessionId = agentSessionManager.getSessionId(hostId);
        org.apache.ambari.server.events.MessageEmitter.LOG.debug("Received status update event {} for host {} registered with session ID {}", event, hostId, sessionId);
        org.springframework.messaging.MessageHeaders headers = createHeaders(sessionId);
        simpMessagingTemplate.convertAndSendToUser(sessionId, getDestination(event), event, headers);
    }

    protected void emitExecutionCommandToHost(org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper eventWrapper) throws org.apache.ambari.server.HostNotRegisteredException {
        org.apache.ambari.server.events.ExecutionCommandEvent event = eventWrapper.getExecutionCommandEvent();
        java.lang.Long hostId = event.getHostId();
        java.lang.Long messageId = eventWrapper.getMessageId();
        java.lang.String sessionId = agentSessionManager.getSessionId(hostId);
        org.apache.ambari.server.events.MessageEmitter.LOG.debug("Received status update event {} for host {} registered with session ID {}", event, hostId, sessionId);
        org.springframework.messaging.MessageHeaders headers = createHeaders(sessionId, messageId);
        simpMessagingTemplate.convertAndSendToUser(sessionId, getDestination(event), event, headers);
    }

    @com.google.common.eventbus.Subscribe
    public void onHostRegister(org.apache.ambari.server.events.HostRegisteredEvent hostRegisteredEvent) {
        java.lang.Long hostId = hostRegisteredEvent.getHostId();
        messagesToEmit.computeIfAbsent(hostId, id -> new java.util.concurrent.LinkedBlockingQueue<>());
    }

    private class MessagesToEmitMonitor implements java.lang.Runnable {
        private boolean anyActionPerformed;

        @java.lang.Override
        public void run() {
            while (true) {
                anyActionPerformed = false;
                for (java.lang.Long hostId : messagesToEmit.keySet()) {
                    unconfirmedMessages.computeIfAbsent(hostId, id -> {
                        org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper event = messagesToEmit.get(hostId).poll();
                        if (event != null) {
                            org.apache.ambari.server.events.MessageEmitter.LOG.info("Schedule execution command emitting, retry: {}, messageId: {}", event.getRetryCounter(), event.getMessageId());
                            emitCompletionService.submit(new org.apache.ambari.server.events.MessageEmitter.EmitMessageTask(event, false));
                            anyActionPerformed = true;
                        }
                        return event;
                    });
                }
                if (!anyActionPerformed) {
                    try {
                        java.lang.Thread.sleep(200);
                    } catch (java.lang.InterruptedException e) {
                        org.apache.ambari.server.events.MessageEmitter.LOG.error("Exception during sleep", e);
                    }
                }
            } 
        }
    }

    private class MessagesToRetryMonitor implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            while (true) {
                try {
                    java.util.concurrent.Future<org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper> future = emitCompletionService.take();
                    org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper result = future.get();
                    java.lang.Long hostId = result.getExecutionCommandEvent().getHostId();
                    unconfirmedMessages.compute(hostId, (id, commandInUse) -> {
                        if ((commandInUse != null) && commandInUse.getMessageId().equals(result.getMessageId())) {
                            if (result.getRetryCounter() < retryCount) {
                                result.retry();
                                org.apache.ambari.server.events.MessageEmitter.LOG.warn("Reschedule execution command emitting, retry: {}, messageId: {}", result.getRetryCounter(), result.getMessageId());
                                emitCompletionService.schedule(new org.apache.ambari.server.events.MessageEmitter.EmitMessageTask(result, true), retryInterval, java.util.concurrent.TimeUnit.SECONDS);
                            } else {
                                org.apache.ambari.server.events.ExecutionCommandEvent event = result.getExecutionCommandEvent();
                                messagesToEmit.remove(event.getHostId());
                                ambariEventPublisher.publish(new org.apache.ambari.server.events.MessageNotDelivered(event.getHostId()));
                                return null;
                            }
                        }
                        return commandInUse;
                    });
                } catch (java.lang.InterruptedException e) {
                    org.apache.ambari.server.events.MessageEmitter.LOG.error("Retry message emitting monitor was interrupted", e);
                } catch (java.util.concurrent.ExecutionException e) {
                    org.apache.ambari.server.events.MessageEmitter.LOG.error("Exception during message emitting retry", e);
                }
            } 
        }
    }

    private class EmitMessageTask implements java.util.concurrent.Callable<org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper> {
        private final org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper emitTaskWrapper;

        private final boolean checkRelevance;

        public EmitMessageTask(org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper emitTaskWrapper, boolean checkRelevance) {
            this.emitTaskWrapper = emitTaskWrapper;
            this.checkRelevance = checkRelevance;
        }

        @java.lang.Override
        public org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper call() throws java.lang.Exception {
            try {
                if (checkRelevance) {
                    java.lang.Long hostId = emitTaskWrapper.getExecutionCommandEvent().getHostId();
                    org.apache.ambari.server.events.MessageEmitter.EmitTaskWrapper commandInUse = unconfirmedMessages.get(hostId);
                    if ((commandInUse != null) && commandInUse.getMessageId().equals(emitTaskWrapper.getMessageId())) {
                        emitExecutionCommandToHost(emitTaskWrapper);
                    }
                } else {
                    emitExecutionCommandToHost(emitTaskWrapper);
                }
            } catch (org.apache.ambari.server.HostNotRegisteredException e) {
                org.apache.ambari.server.events.MessageEmitter.LOG.error("Trying to emit execution command to unregistered host {} on attempt {}", emitTaskWrapper.getMessageId(), emitTaskWrapper.getRetryCounter(), e);
            }
            return emitTaskWrapper;
        }
    }

    private class EmitTaskWrapper {
        private final java.lang.Long messageId;

        private final org.apache.ambari.server.events.ExecutionCommandEvent executionCommandEvent;

        private final java.util.concurrent.atomic.AtomicInteger retryCounter;

        public EmitTaskWrapper(int retryCounter, java.lang.Long messageId, org.apache.ambari.server.events.ExecutionCommandEvent executionCommandEvent) {
            this.retryCounter = new java.util.concurrent.atomic.AtomicInteger(retryCounter);
            this.messageId = messageId;
            this.executionCommandEvent = executionCommandEvent;
        }

        public int getRetryCounter() {
            return retryCounter.get();
        }

        public org.apache.ambari.server.events.ExecutionCommandEvent getExecutionCommandEvent() {
            return executionCommandEvent;
        }

        public java.lang.Long getMessageId() {
            return messageId;
        }

        public void retry() {
            retryCounter.incrementAndGet();
        }
    }
}