package org.apache.ambari.server.state;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
public class Alert {
    private java.lang.String name;

    private java.lang.String instance;

    private java.lang.String service;

    private java.lang.String component;

    private java.lang.String hostName;

    private org.apache.ambari.server.state.AlertState state = org.apache.ambari.server.state.AlertState.UNKNOWN;

    private java.lang.String label;

    private java.lang.String text;

    private long timestamp;

    private java.lang.Long clusterId;

    private java.lang.String uuid;

    protected static final int MAX_ALERT_TEXT_SIZE = 32617;

    public Alert(java.lang.String alertName, java.lang.String alertInstance, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName, org.apache.ambari.server.state.AlertState alertState) {
        name = alertName;
        instance = alertInstance;
        service = serviceName;
        component = componentName;
        this.hostName = hostName;
        state = alertState;
        timestamp = java.lang.System.currentTimeMillis();
    }

    public Alert() {
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("service")
    @com.fasterxml.jackson.annotation.JsonProperty("service")
    public java.lang.String getService() {
        return service;
    }

    @org.codehaus.jackson.annotate.JsonProperty("component")
    @com.fasterxml.jackson.annotation.JsonProperty("component")
    public java.lang.String getComponent() {
        return component;
    }

    @org.codehaus.jackson.annotate.JsonProperty("host")
    @com.fasterxml.jackson.annotation.JsonProperty("host")
    public java.lang.String getHostName() {
        return hostName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("state")
    @com.fasterxml.jackson.annotation.JsonProperty("state")
    public org.apache.ambari.server.state.AlertState getState() {
        return state;
    }

    @org.codehaus.jackson.annotate.JsonProperty("label")
    @com.fasterxml.jackson.annotation.JsonProperty("label")
    public java.lang.String getLabel() {
        return label;
    }

    @org.codehaus.jackson.annotate.JsonProperty("label")
    @com.fasterxml.jackson.annotation.JsonProperty("label")
    public void setLabel(java.lang.String alertLabel) {
        label = alertLabel;
    }

    @org.codehaus.jackson.annotate.JsonProperty("text")
    @com.fasterxml.jackson.annotation.JsonProperty("text")
    public java.lang.String getText() {
        return text;
    }

    @org.codehaus.jackson.annotate.JsonProperty("text")
    @com.fasterxml.jackson.annotation.JsonProperty("text")
    public void setText(java.lang.String alertText) {
        text = org.apache.commons.lang.StringUtils.abbreviateMiddle(alertText, "…", org.apache.ambari.server.state.Alert.MAX_ALERT_TEXT_SIZE);
    }

    @org.codehaus.jackson.annotate.JsonProperty("instance")
    @com.fasterxml.jackson.annotation.JsonProperty("instance")
    public java.lang.String getInstance() {
        return instance;
    }

    @org.codehaus.jackson.annotate.JsonProperty("instance")
    @com.fasterxml.jackson.annotation.JsonProperty("instance")
    public void setInstance(java.lang.String instance) {
        this.instance = instance;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("service")
    @com.fasterxml.jackson.annotation.JsonProperty("service")
    public void setService(java.lang.String service) {
        this.service = service;
    }

    @org.codehaus.jackson.annotate.JsonProperty("component")
    @com.fasterxml.jackson.annotation.JsonProperty("component")
    public void setComponent(java.lang.String component) {
        this.component = component;
    }

    @org.codehaus.jackson.annotate.JsonProperty("host")
    @com.fasterxml.jackson.annotation.JsonProperty("host")
    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("state")
    @com.fasterxml.jackson.annotation.JsonProperty("state")
    public void setState(org.apache.ambari.server.state.AlertState state) {
        this.state = state;
    }

    @org.codehaus.jackson.annotate.JsonProperty("timestamp")
    @com.fasterxml.jackson.annotation.JsonProperty("timestamp")
    public void setTimestamp(long ts) {
        timestamp = ts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("timestamp")
    @com.fasterxml.jackson.annotation.JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusterId")
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("uuid")
    public java.lang.String getUUID() {
        return uuid;
    }

    public void setUUID(java.lang.String uuid) {
        this.uuid = uuid;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(state, name, service, component, hostName, instance, clusterId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.state.Alert other = ((org.apache.ambari.server.state.Alert) (obj));
        return (((((java.util.Objects.equals(state, other.state) && java.util.Objects.equals(name, other.name)) && java.util.Objects.equals(service, other.service)) && java.util.Objects.equals(component, other.component)) && java.util.Objects.equals(hostName, other.hostName)) && java.util.Objects.equals(instance, other.instance)) && java.util.Objects.equals(clusterId, other.clusterId);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append('{');
        sb.append("clusterId=").append(clusterId).append(", ");
        sb.append("state=").append(state).append(", ");
        sb.append("name=").append(name).append(", ");
        sb.append("service=").append(service).append(", ");
        sb.append("component=").append(component).append(", ");
        sb.append("host=").append(hostName).append(", ");
        sb.append("instance=").append(instance).append(", ");
        sb.append("text='").append(text).append("'");
        sb.append('}');
        return sb.toString();
    }
}