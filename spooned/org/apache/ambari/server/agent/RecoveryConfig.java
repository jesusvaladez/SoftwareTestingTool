package org.apache.ambari.server.agent;
public class RecoveryConfig {
    @com.google.gson.annotations.SerializedName("components")
    @com.fasterxml.jackson.annotation.JsonProperty("components")
    private java.util.List<org.apache.ambari.server.agent.RecoveryConfigComponent> enabledComponents;

    public RecoveryConfig(java.util.List<org.apache.ambari.server.agent.RecoveryConfigComponent> enabledComponents) {
        this.enabledComponents = enabledComponents;
    }

    public java.util.List<org.apache.ambari.server.agent.RecoveryConfigComponent> getEnabledComponents() {
        return enabledComponents == null ? null : java.util.Collections.unmodifiableList(enabledComponents);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.RecoveryConfig that = ((org.apache.ambari.server.agent.RecoveryConfig) (o));
        return enabledComponents != null ? enabledComponents.equals(that.enabledComponents) : that.enabledComponents == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (enabledComponents != null) ? enabledComponents.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("RecoveryConfig{");
        buffer.append(", components=").append(enabledComponents);
        buffer.append('}');
        return buffer.toString();
    }
}