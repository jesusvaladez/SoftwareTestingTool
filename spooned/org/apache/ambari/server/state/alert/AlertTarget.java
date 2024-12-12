package org.apache.ambari.server.state.alert;
import org.codehaus.jackson.annotate.JsonProperty;
public class AlertTarget {
    private java.lang.Long m_id;

    private java.lang.String m_name;

    private java.lang.String m_description;

    private java.lang.String m_notificationType;

    private java.util.Map<java.lang.String, java.lang.String> m_properties;

    private boolean m_isGlobal;

    private boolean m_isEnabled;

    @org.codehaus.jackson.annotate.JsonProperty("id")
    public java.lang.Long getId() {
        return m_id;
    }

    public void setId(java.lang.Long id) {
        m_id = id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getName() {
        return m_name;
    }

    public void setName(java.lang.String name) {
        m_name = name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("description")
    public java.lang.String getDescription() {
        return m_description;
    }

    public void setDescription(java.lang.String description) {
        m_description = description;
    }

    @org.codehaus.jackson.annotate.JsonProperty("notification_type")
    public java.lang.String getNotificationType() {
        return m_notificationType;
    }

    public void setNotificationType(java.lang.String notificationType) {
        m_notificationType = notificationType;
    }

    @org.codehaus.jackson.annotate.JsonProperty("properties")
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return m_properties;
    }

    public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        m_properties = properties;
    }

    @org.codehaus.jackson.annotate.JsonProperty("global")
    public boolean isGlobal() {
        return m_isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        m_isGlobal = isGlobal;
    }

    @org.codehaus.jackson.annotate.JsonProperty("enabled")
    public boolean isEnabled() {
        return m_isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        m_isEnabled = isEnabled;
    }

    public static org.apache.ambari.server.state.alert.AlertTarget coerce(org.apache.ambari.server.orm.entities.AlertTargetEntity entity) {
        org.apache.ambari.server.state.alert.AlertTarget target = new org.apache.ambari.server.state.alert.AlertTarget();
        target.setId(entity.getTargetId());
        target.setDescription(entity.getDescription());
        target.setName(entity.getTargetName());
        target.setNotificationType(entity.getNotificationType());
        target.setGlobal(entity.isGlobal());
        target.setEnabled(entity.isEnabled());
        return target;
    }
}