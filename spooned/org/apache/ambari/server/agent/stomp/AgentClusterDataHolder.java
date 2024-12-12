package org.apache.ambari.server.agent.stomp;
public abstract class AgentClusterDataHolder<T extends org.apache.ambari.server.events.STOMPEvent & org.apache.ambari.server.agent.stomp.dto.Hashable> extends org.apache.ambari.server.agent.stomp.AgentDataHolder<T> {
    @javax.inject.Inject
    protected org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    private volatile T data;

    public T getUpdateIfChanged(java.lang.String agentHash) throws org.apache.ambari.server.AmbariException {
        initializeDataIfNeeded(true);
        return !java.util.Objects.equals(agentHash, data.getHash()) ? data : getEmptyData();
    }

    protected abstract T getCurrentData() throws org.apache.ambari.server.AmbariException;

    protected abstract boolean handleUpdate(T update) throws org.apache.ambari.server.AmbariException;

    public boolean updateData(T update) throws org.apache.ambari.server.AmbariException {
        updateLock.lock();
        try {
            initializeDataIfNeeded(true);
            boolean changed = handleUpdate(update);
            if (changed) {
                regenerateDataIdentifiers(data);
                update.setHash(getData().getHash());
                STOMPUpdatePublisher.publish(update);
            }
            return changed;
        } finally {
            updateLock.unlock();
        }
    }

    protected final void initializeDataIfNeeded(boolean regenerateHash) throws org.apache.ambari.server.AmbariException {
        if (data == null) {
            updateLock.lock();
            try {
                if (data == null) {
                    T localData = getCurrentData();
                    if (regenerateHash) {
                        regenerateDataIdentifiers(localData);
                    }
                    data = localData;
                }
            } finally {
                updateLock.unlock();
            }
        }
    }

    public final T getData() {
        return data;
    }
}