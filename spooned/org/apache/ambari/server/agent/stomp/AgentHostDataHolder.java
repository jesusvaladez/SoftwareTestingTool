package org.apache.ambari.server.agent.stomp;
public abstract class AgentHostDataHolder<T extends org.apache.ambari.server.events.STOMPHostEvent & org.apache.ambari.server.agent.stomp.dto.Hashable> extends org.apache.ambari.server.agent.stomp.AgentDataHolder<T> {
    public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AgentHostDataHolder.class);

    @javax.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    private final java.util.concurrent.ConcurrentHashMap<java.lang.Long, T> data = new java.util.concurrent.ConcurrentHashMap<>();

    protected abstract T getCurrentData(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException;

    protected abstract T handleUpdate(T current, T update) throws org.apache.ambari.server.AmbariException;

    public T getUpdateIfChanged(java.lang.String agentHash, java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        T hostData = initializeDataIfNeeded(hostId, true);
        return !java.util.Objects.equals(agentHash, hostData.getHash()) ? hostData : getEmptyData();
    }

    public T initializeDataIfNeeded(java.lang.Long hostId, boolean regenerateHash) throws org.apache.ambari.server.AmbariRuntimeException {
        return data.computeIfAbsent(hostId, id -> initializeData(hostId, regenerateHash));
    }

    private T initializeData(java.lang.Long hostId, boolean regenerateHash) {
        T hostData;
        try {
            hostData = getCurrentData(hostId);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.agent.stomp.AgentHostDataHolder.LOG.error("Error during retrieving initial value for host: {} and class {}", hostId, getClass().getName(), e);
            throw new org.apache.ambari.server.AmbariRuntimeException((("Error during retrieving initial value for host: " + hostId) + " and class: ") + getClass().getName(), e);
        }
        if (regenerateHash) {
            regenerateDataIdentifiers(hostData);
        }
        return hostData;
    }

    public void updateData(T update) throws org.apache.ambari.server.AmbariException {
        try {
            data.compute(update.getHostId(), (id, current) -> {
                if (current == null) {
                    current = initializeData(id, true);
                }
                T updated;
                try {
                    updated = handleUpdate(current, update);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.agent.stomp.AgentHostDataHolder.LOG.error("Error during handling update for host: {} and class {}", id, getClass().getName(), e);
                    throw new org.apache.ambari.server.AmbariRuntimeException((("Error during handling update for host: " + id) + " and class: ") + getClass().getName(), e);
                }
                if (updated == null) {
                    return current;
                } else {
                    regenerateDataIdentifiers(updated);
                    setIdentifiersToEventUpdate(update, updated);
                    return updated;
                }
            });
        } catch (org.apache.ambari.server.AmbariRuntimeException e) {
            throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
        }
        if (isIdentifierValid(update)) {
            if (update.getType().equals(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_CONFIGS)) {
                org.apache.ambari.server.agent.stomp.AgentHostDataHolder.LOG.info("Configs update with hash {} will be sent to host {}", update.getHash(), update.getHostId());
            }
            STOMPUpdatePublisher.publish(update);
        }
    }

    protected void setIdentifiersToEventUpdate(T update, T hostData) {
        update.setHash(hostData.getHash());
    }

    public final void resetData(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        T newData = getCurrentData(hostId);
        data.replace(hostId, newData);
        STOMPUpdatePublisher.publish(newData);
    }

    public final void onHostRemoved(java.lang.Long hostId) {
        data.remove(hostId);
    }

    public java.util.Map<java.lang.Long, T> getData() {
        return data;
    }

    public T getData(java.lang.Long hostId) {
        return data.get(hostId);
    }

    public void setData(T data, java.lang.Long hostId) {
        this.data.put(hostId, data);
    }
}