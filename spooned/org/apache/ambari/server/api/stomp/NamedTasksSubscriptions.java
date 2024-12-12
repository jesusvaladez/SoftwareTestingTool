package org.apache.ambari.server.api.stomp;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class NamedTasksSubscriptions {
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.class);

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId>> taskIds = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.lang.String subscriptionPrefix = "/events/tasks/";

    private final java.util.concurrent.locks.Lock taskIdsLock = new java.util.concurrent.locks.ReentrantLock();

    private com.google.inject.Provider<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener> taskStatusListenerProvider;

    @com.google.inject.Inject
    public NamedTasksSubscriptions(com.google.inject.Provider<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener> taskStatusListenerProvider) {
        this.taskStatusListenerProvider = taskStatusListenerProvider;
    }

    public void addTaskId(java.lang.String sessionId, java.lang.Long taskId, java.lang.String id) {
        try {
            taskIdsLock.lock();
            taskIds.compute(sessionId, (sid, ids) -> {
                if (ids == null) {
                    ids = new java.util.ArrayList<>();
                }
                java.util.concurrent.atomic.AtomicBoolean completed = new java.util.concurrent.atomic.AtomicBoolean(false);
                taskStatusListenerProvider.get().getActiveTasksMap().computeIfPresent(taskId, (tid, task) -> {
                    if (task.getStatus().isCompletedState()) {
                        completed.set(true);
                    }
                    return task;
                });
                if (!completed.get()) {
                    ids.add(new org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId(taskId, id));
                }
                return ids;
            });
            org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.LOG.debug(java.lang.String.format("Task subscription was added for sessionId = %s, taskId = %s, id = %s", sessionId, taskId, id));
        } finally {
            taskIdsLock.unlock();
        }
    }

    public void removeId(java.lang.String sessionId, java.lang.String id) {
        taskIds.computeIfPresent(sessionId, (sid, tasks) -> {
            java.util.Iterator<org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(id)) {
                    iterator.remove();
                    org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.LOG.debug(java.lang.String.format("Task subscription was removed for sessionId = %s, id = %s", sessionId, id));
                }
            } 
            return tasks;
        });
    }

    public void removeTaskId(java.lang.Long taskId) {
        try {
            taskIdsLock.lock();
            for (java.lang.String sessionId : taskIds.keySet()) {
                taskIds.computeIfPresent(sessionId, (id, tasks) -> {
                    java.util.Iterator<org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId> iterator = tasks.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().getTaskId().equals(taskId)) {
                            iterator.remove();
                            org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.LOG.debug(java.lang.String.format("Task subscription was removed for sessionId = %s and taskId = %s", sessionId, taskId));
                        }
                    } 
                    return tasks;
                });
            }
        } finally {
            taskIdsLock.unlock();
        }
    }

    public void removeSession(java.lang.String sessionId) {
        try {
            taskIdsLock.lock();
            taskIds.remove(sessionId);
            org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.LOG.debug(java.lang.String.format("Task subscriptions were removed for sessionId = %s", sessionId));
        } finally {
            taskIdsLock.unlock();
        }
    }

    public java.util.Optional<java.lang.Long> matchDestination(java.lang.String destination) {
        java.util.Optional<java.lang.Long> taskIdOpt = java.util.Optional.of(org.apache.commons.lang.StringUtils.substringAfter(destination, subscriptionPrefix)).filter(StringUtils::isNotEmpty).filter(StringUtils::isNumeric).map(java.lang.Long::parseLong);
        return taskIdOpt;
    }

    public void addDestination(java.lang.String sessionId, java.lang.String destination, java.lang.String id) {
        java.util.Optional<java.lang.Long> taskIdOpt = matchDestination(destination);
        if (taskIdOpt.isPresent()) {
            addTaskId(sessionId, taskIdOpt.get(), id);
        }
    }

    public boolean checkTaskId(java.lang.Long taskId) {
        for (java.util.List<org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId> ids : taskIds.values()) {
            for (org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.SubscriptionId subscriptionId : ids) {
                if (subscriptionId.getTaskId().equals(taskId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public class SubscriptionId {
        private final java.lang.Long taskId;

        private final java.lang.String id;

        public SubscriptionId(java.lang.Long taskId, java.lang.String id) {
            this.taskId = taskId;
            this.id = id;
        }

        public java.lang.Long getTaskId() {
            return taskId;
        }

        public java.lang.String getId() {
            return id;
        }
    }
}