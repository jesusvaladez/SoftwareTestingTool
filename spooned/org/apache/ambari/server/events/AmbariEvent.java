package org.apache.ambari.server.events;
public abstract class AmbariEvent {
    public enum AmbariEventType {

        SERVICE_INSTALL_SUCCESS,
        SERVICE_REMOVED_SUCCESS,
        SERVICE_COMPONENT_INSTALL_SUCCESS,
        SERVICE_COMPONENT_UNINSTALLED_SUCCESS,
        ALERT_DEFINITION_REGISTRATION,
        ALERT_DEFINITION_CHANGED,
        ALERT_DEFINITION_REMOVAL,
        ALERT_DEFINITION_HASH_INVALIDATION,
        ALERT_DEFINITION_DISABLED,
        HOST_REGISTERED,
        HOST_COMPONENT_VERSION_ADVERTISED,
        HOST_ADDED,
        HOST_REMOVED,
        MAINTENANCE_MODE,
        ACTION_EXECUTION_FINISHED,
        REQUEST_FINISHED,
        CLUSTER_RENAME,
        CLUSTER_PROVISIONED,
        CLUSTER_PROVISION_STARTED,
        SERVICE_COMPONENT_RECOVERY_CHANGED,
        FINALIZE_UPGRADE_FINISH,
        CLUSTER_CONFIG_CHANGED,
        CLUSTER_CONFIG_FINISHED,
        METRICS_COLLECTOR_HOST_DOWN,
        USER_CREATED,
        HOST_STATUS_CHANGE,
        HOST_STATE_CHANGE,
        HOST_HEARTBEAT_UPDATED,
        AMBARI_CONFIGURATION_CHANGED,
        AMBARI_PROPERTIES_CHANGED,
        JPA_INITIALIZED,
        STALE_CONFIGS_UPDATE,
        SERVICE_COMPONENT_REPO_CHANGE,
        SERVICE_CREDENTIAL_STORE_UPDATE,
        MESSAGE_NOT_DELIVERED;}

    protected final org.apache.ambari.server.events.AmbariEvent.AmbariEventType m_eventType;

    public AmbariEvent(org.apache.ambari.server.events.AmbariEvent.AmbariEventType eventType) {
        m_eventType = eventType;
    }

    public org.apache.ambari.server.events.AmbariEvent.AmbariEventType getType() {
        return m_eventType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(getClass().getSimpleName());
        buffer.append("{eventType=").append(m_eventType);
        buffer.append("}");
        return buffer.toString();
    }
}