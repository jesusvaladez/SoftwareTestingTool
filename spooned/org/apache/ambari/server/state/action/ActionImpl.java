package org.apache.ambari.server.state.action;
public class ActionImpl implements org.apache.ambari.server.state.action.Action {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.action.ActionImpl.class);

    private final java.util.concurrent.locks.Lock readLock;

    private final java.util.concurrent.locks.Lock writeLock;

    private org.apache.ambari.server.state.action.ActionId id;

    private long startTime;

    private long lastUpdateTime;

    private long completionTime;

    private static final org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionState, org.apache.ambari.server.state.action.ActionEventType, org.apache.ambari.server.state.action.ActionEvent> stateMachineFactory = new org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionState, org.apache.ambari.server.state.action.ActionEventType, org.apache.ambari.server.state.action.ActionEvent>(org.apache.ambari.server.state.action.ActionState.INIT).addTransition(org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionEventType.ACTION_IN_PROGRESS, new org.apache.ambari.server.state.action.ActionImpl.ActionProgressUpdateTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionState.COMPLETED, org.apache.ambari.server.state.action.ActionEventType.ACTION_COMPLETED, new org.apache.ambari.server.state.action.ActionImpl.ActionCompletedTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionState.FAILED, org.apache.ambari.server.state.action.ActionEventType.ACTION_FAILED, new org.apache.ambari.server.state.action.ActionImpl.ActionFailedTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionEventType.ACTION_IN_PROGRESS, new org.apache.ambari.server.state.action.ActionImpl.ActionProgressUpdateTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionEventType.ACTION_IN_PROGRESS, new org.apache.ambari.server.state.action.ActionImpl.ActionProgressUpdateTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionState.COMPLETED, org.apache.ambari.server.state.action.ActionEventType.ACTION_COMPLETED, new org.apache.ambari.server.state.action.ActionImpl.ActionCompletedTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, org.apache.ambari.server.state.action.ActionState.FAILED, org.apache.ambari.server.state.action.ActionEventType.ACTION_FAILED, new org.apache.ambari.server.state.action.ActionImpl.ActionFailedTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.COMPLETED, org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionEventType.ACTION_INIT, new org.apache.ambari.server.state.action.ActionImpl.NewActionTransition()).addTransition(org.apache.ambari.server.state.action.ActionState.FAILED, org.apache.ambari.server.state.action.ActionState.INIT, org.apache.ambari.server.state.action.ActionEventType.ACTION_INIT, new org.apache.ambari.server.state.action.ActionImpl.NewActionTransition()).installTopology();

    private final org.apache.ambari.server.state.fsm.StateMachine<org.apache.ambari.server.state.action.ActionState, org.apache.ambari.server.state.action.ActionEventType, org.apache.ambari.server.state.action.ActionEvent> stateMachine;

    public ActionImpl(org.apache.ambari.server.state.action.ActionId id, long startTime) {
        super();
        this.id = id;
        this.stateMachine = org.apache.ambari.server.state.action.ActionImpl.stateMachineFactory.make(this);
        java.util.concurrent.locks.ReadWriteLock rwLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
        this.startTime = startTime;
        this.lastUpdateTime = -1;
        this.completionTime = -1;
    }

    private void reset() {
        try {
            writeLock.lock();
            this.startTime = -1;
            this.lastUpdateTime = -1;
            this.completionTime = -1;
        } finally {
            writeLock.unlock();
        }
    }

    static class NewActionTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.action.ActionImpl action, org.apache.ambari.server.state.action.ActionEvent event) {
            org.apache.ambari.server.state.action.ActionInitEvent e = ((org.apache.ambari.server.state.action.ActionInitEvent) (event));
            action.reset();
            action.setId(e.getActionId());
            action.setStartTime(e.getStartTime());
            org.apache.ambari.server.state.action.ActionImpl.LOG.info(((("Launching a new Action" + ", actionId=") + action.getId()) + ", startTime=") + action.getStartTime());
        }
    }

    static class ActionProgressUpdateTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.action.ActionImpl action, org.apache.ambari.server.state.action.ActionEvent event) {
            org.apache.ambari.server.state.action.ActionProgressUpdateEvent e = ((org.apache.ambari.server.state.action.ActionProgressUpdateEvent) (event));
            action.setLastUpdateTime(e.getProgressUpdateTime());
            if (org.apache.ambari.server.state.action.ActionImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.action.ActionImpl.LOG.debug("Progress update for Action, actionId={}, startTime={}, lastUpdateTime={}", action.getId(), action.getStartTime(), action.getLastUpdateTime());
            }
        }
    }

    static class ActionCompletedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.action.ActionImpl action, org.apache.ambari.server.state.action.ActionEvent event) {
            org.apache.ambari.server.state.action.ActionCompletedEvent e = ((org.apache.ambari.server.state.action.ActionCompletedEvent) (event));
            action.setCompletionTime(e.getCompletionTime());
            action.setLastUpdateTime(e.getCompletionTime());
            org.apache.ambari.server.state.action.ActionImpl.LOG.info(((((("Action completed successfully" + ", actionId=") + action.getId()) + ", startTime=") + action.getStartTime()) + ", completionTime=") + action.getCompletionTime());
        }
    }

    static class ActionFailedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.action.ActionImpl, org.apache.ambari.server.state.action.ActionEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.action.ActionImpl action, org.apache.ambari.server.state.action.ActionEvent event) {
            org.apache.ambari.server.state.action.ActionFailedEvent e = ((org.apache.ambari.server.state.action.ActionFailedEvent) (event));
            action.setCompletionTime(e.getCompletionTime());
            action.setLastUpdateTime(e.getCompletionTime());
            org.apache.ambari.server.state.action.ActionImpl.LOG.info(((((("Action failed to complete" + ", actionId=") + action.getId()) + ", startTime=") + action.getStartTime()) + ", completionTime=") + action.getCompletionTime());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.action.ActionState getState() {
        try {
            readLock.lock();
            return stateMachine.getCurrentState();
        } finally {
            readLock.unlock();
        }
    }

    @java.lang.Override
    public void setState(org.apache.ambari.server.state.action.ActionState state) {
        try {
            writeLock.lock();
            stateMachine.setCurrentState(state);
        } finally {
            writeLock.unlock();
        }
    }

    @java.lang.Override
    public void handleEvent(org.apache.ambari.server.state.action.ActionEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        if (org.apache.ambari.server.state.action.ActionImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.action.ActionImpl.LOG.debug("Handling Action event, eventType={}, event={}", event.getType().name(), event);
        }
        org.apache.ambari.server.state.action.ActionState oldState = getState();
        try {
            writeLock.lock();
            try {
                stateMachine.doTransition(event.getType(), event);
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.state.action.ActionImpl.LOG.error(((((((("Can't handle Action event at current state" + ", actionId=") + this.getId()) + ", currentState=") + oldState) + ", eventType=") + event.getType()) + ", event=") + event);
                throw e;
            }
        } finally {
            writeLock.unlock();
        }
        if (oldState != getState()) {
            if (org.apache.ambari.server.state.action.ActionImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.action.ActionImpl.LOG.debug("Action transitioned to a new state, actionId={}, oldState={}, currentState={}, eventType={}, event={}", getId(), oldState, getState(), event.getType().name(), event);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.action.ActionId getId() {
        try {
            readLock.lock();
            return id;
        } finally {
            readLock.unlock();
        }
    }

    private void setId(org.apache.ambari.server.state.action.ActionId id) {
        try {
            writeLock.lock();
            this.id = id;
        } finally {
            writeLock.unlock();
        }
    }

    @java.lang.Override
    public long getStartTime() {
        try {
            readLock.lock();
            return startTime;
        } finally {
            readLock.unlock();
        }
    }

    public void setStartTime(long startTime) {
        try {
            writeLock.lock();
            this.startTime = startTime;
        } finally {
            writeLock.unlock();
        }
    }

    @java.lang.Override
    public long getLastUpdateTime() {
        try {
            readLock.lock();
            return lastUpdateTime;
        } finally {
            readLock.unlock();
        }
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        try {
            writeLock.lock();
            this.lastUpdateTime = lastUpdateTime;
        } finally {
            writeLock.unlock();
        }
    }

    @java.lang.Override
    public long getCompletionTime() {
        try {
            readLock.lock();
            return completionTime;
        } finally {
            readLock.unlock();
        }
    }

    public void setCompletionTime(long completionTime) {
        try {
            writeLock.lock();
            this.completionTime = completionTime;
        } finally {
            writeLock.unlock();
        }
    }
}