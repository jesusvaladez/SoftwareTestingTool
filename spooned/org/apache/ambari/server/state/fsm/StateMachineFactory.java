package org.apache.ambari.server.state.fsm;
public final class StateMachineFactory<OPERAND, STATE extends java.lang.Enum<STATE>, EVENTTYPE extends java.lang.Enum<EVENTTYPE>, EVENT> {
    private final org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>.TransitionsListNode transitionsListNode;

    private java.util.Map<STATE, java.util.Map<EVENTTYPE, org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> stateMachineTable;

    private STATE defaultInitialState;

    private final boolean optimized;

    public StateMachineFactory(STATE defaultInitialState) {
        this.transitionsListNode = null;
        this.defaultInitialState = defaultInitialState;
        this.optimized = false;
        this.stateMachineTable = null;
    }

    private StateMachineFactory(org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> that, org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableTransition t) {
        this.defaultInitialState = that.defaultInitialState;
        this.transitionsListNode = new TransitionsListNode(t, that.transitionsListNode);
        this.optimized = false;
        this.stateMachineTable = null;
    }

    private StateMachineFactory(org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> that, boolean optimized) {
        this.defaultInitialState = that.defaultInitialState;
        this.transitionsListNode = that.transitionsListNode;
        this.optimized = optimized;
        if (optimized) {
            makeStateMachineTable();
        } else {
            stateMachineTable = null;
        }
    }

    private interface ApplicableTransition<OPERAND, STATE extends java.lang.Enum<STATE>, EVENTTYPE extends java.lang.Enum<EVENTTYPE>, EVENT> {
        void apply(org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> subject);
    }

    private class TransitionsListNode {
        final org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> transition;

        final org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>.TransitionsListNode next;

        TransitionsListNode(org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> transition, org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>.TransitionsListNode next) {
            this.transition = transition;
            this.next = next;
        }
    }

    private static class ApplicableSingleOrMultipleTransition<OPERAND, STATE extends java.lang.Enum<STATE>, EVENTTYPE extends java.lang.Enum<EVENTTYPE>, EVENT> implements org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> {
        final STATE preState;

        final EVENTTYPE eventType;

        final org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition;

        ApplicableSingleOrMultipleTransition(STATE preState, EVENTTYPE eventType, org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition) {
            this.preState = preState;
            this.eventType = eventType;
            this.transition = transition;
        }

        @java.lang.Override
        public void apply(org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> subject) {
            java.util.Map<EVENTTYPE, org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT>> transitionMap = subject.stateMachineTable.get(preState);
            if (transitionMap == null) {
                transitionMap = new java.util.HashMap<>();
                subject.stateMachineTable.put(preState, transitionMap);
            }
            transitionMap.put(eventType, transition);
        }
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(STATE preState, STATE postState, EVENTTYPE eventType) {
        return addTransition(preState, postState, eventType, null);
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(STATE preState, STATE postState, java.util.Set<EVENTTYPE> eventTypes) {
        return addTransition(preState, postState, eventTypes, null);
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(STATE preState, STATE postState, java.util.Set<EVENTTYPE> eventTypes, org.apache.ambari.server.state.fsm.SingleArcTransition<OPERAND, EVENT> hook) {
        org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> factory = null;
        for (EVENTTYPE event : eventTypes) {
            if (factory == null) {
                factory = addTransition(preState, postState, event, hook);
            } else {
                factory = factory.addTransition(preState, postState, event, hook);
            }
        }
        return factory;
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(STATE preState, STATE postState, EVENTTYPE eventType, org.apache.ambari.server.state.fsm.SingleArcTransition<OPERAND, EVENT> hook) {
        return new org.apache.ambari.server.state.fsm.StateMachineFactory<>(this, new org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableSingleOrMultipleTransition<>(preState, eventType, new SingleInternalArc(postState, hook)));
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(STATE preState, java.util.Set<STATE> postStates, EVENTTYPE eventType, org.apache.ambari.server.state.fsm.MultipleArcTransition<OPERAND, EVENT, STATE> hook) {
        return new org.apache.ambari.server.state.fsm.StateMachineFactory<>(this, new org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableSingleOrMultipleTransition<>(preState, eventType, new MultipleInternalArc(postStates, hook)));
    }

    public org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> installTopology() {
        return new org.apache.ambari.server.state.fsm.StateMachineFactory<>(this, true);
    }

    private STATE doTransition(OPERAND operand, STATE oldState, EVENTTYPE eventType, EVENT event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        java.util.Map<EVENTTYPE, org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT>> transitionMap = stateMachineTable.get(oldState);
        if (transitionMap != null) {
            org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition = transitionMap.get(eventType);
            if (transition != null) {
                return transition.doTransition(operand, oldState, event, eventType);
            }
        }
        throw new org.apache.ambari.server.state.fsm.InvalidStateTransitionException(oldState, eventType);
    }

    private synchronized void maybeMakeStateMachineTable() {
        if (stateMachineTable == null) {
            makeStateMachineTable();
        }
    }

    private void makeStateMachineTable() {
        java.util.Stack<org.apache.ambari.server.state.fsm.StateMachineFactory.ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT>> stack = new java.util.Stack<>();
        java.util.Map<STATE, java.util.Map<EVENTTYPE, org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> prototype = new java.util.HashMap<>();
        prototype.put(defaultInitialState, null);
        stateMachineTable = new java.util.EnumMap<>(prototype);
        for (org.apache.ambari.server.state.fsm.StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>.TransitionsListNode cursor = transitionsListNode; cursor != null; cursor = cursor.next) {
            stack.push(cursor.transition);
        }
        while (!stack.isEmpty()) {
            stack.pop().apply(this);
        } 
    }

    private interface Transition<OPERAND, STATE extends java.lang.Enum<STATE>, EVENTTYPE extends java.lang.Enum<EVENTTYPE>, EVENT> {
        STATE doTransition(OPERAND operand, STATE oldState, EVENT event, EVENTTYPE eventType) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException;
    }

    private class SingleInternalArc implements org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT> {
        private STATE postState;

        private org.apache.ambari.server.state.fsm.SingleArcTransition<OPERAND, EVENT> hook;

        SingleInternalArc(STATE postState, org.apache.ambari.server.state.fsm.SingleArcTransition<OPERAND, EVENT> hook) {
            this.postState = postState;
            this.hook = hook;
        }

        @java.lang.Override
        public STATE doTransition(OPERAND operand, STATE oldState, EVENT event, EVENTTYPE eventType) {
            if (hook != null) {
                hook.transition(operand, event);
            }
            return postState;
        }
    }

    private class MultipleInternalArc implements org.apache.ambari.server.state.fsm.StateMachineFactory.Transition<OPERAND, STATE, EVENTTYPE, EVENT> {
        private java.util.Set<STATE> validPostStates;

        private org.apache.ambari.server.state.fsm.MultipleArcTransition<OPERAND, EVENT, STATE> hook;

        MultipleInternalArc(java.util.Set<STATE> postStates, org.apache.ambari.server.state.fsm.MultipleArcTransition<OPERAND, EVENT, STATE> hook) {
            this.validPostStates = postStates;
            this.hook = hook;
        }

        @java.lang.Override
        public STATE doTransition(OPERAND operand, STATE oldState, EVENT event, EVENTTYPE eventType) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
            STATE postState = hook.transition(operand, event);
            if (!validPostStates.contains(postState)) {
                throw new org.apache.ambari.server.state.fsm.InvalidStateTransitionException(oldState, eventType);
            }
            return postState;
        }
    }

    public org.apache.ambari.server.state.fsm.StateMachine<STATE, EVENTTYPE, EVENT> make(OPERAND operand, STATE initialState) {
        return new InternalStateMachine(operand, initialState);
    }

    public org.apache.ambari.server.state.fsm.StateMachine<STATE, EVENTTYPE, EVENT> make(OPERAND operand) {
        return new InternalStateMachine(operand, defaultInitialState);
    }

    private class InternalStateMachine implements org.apache.ambari.server.state.fsm.StateMachine<STATE, EVENTTYPE, EVENT> {
        private final OPERAND operand;

        private STATE currentState;

        InternalStateMachine(OPERAND operand, STATE initialState) {
            this.operand = operand;
            this.currentState = initialState;
            if (!optimized) {
                maybeMakeStateMachineTable();
            }
        }

        @java.lang.Override
        public synchronized STATE getCurrentState() {
            return currentState;
        }

        @java.lang.Override
        public synchronized STATE doTransition(EVENTTYPE eventType, EVENT event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
            currentState = StateMachineFactory.this.doTransition(operand, currentState, eventType, event);
            return currentState;
        }

        @java.lang.Override
        public synchronized void setCurrentState(STATE state) {
            currentState = state;
        }
    }
}