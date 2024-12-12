package org.apache.ambari.server.state;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class HostConfig {
    private final java.util.Map<java.lang.Long, java.lang.String> configGroupOverrides = new java.util.concurrent.ConcurrentHashMap<>();

    private java.lang.String defaultVersionTag;

    public HostConfig() {
    }

    @org.codehaus.jackson.annotate.JsonProperty("default")
    public java.lang.String getDefaultVersionTag() {
        return defaultVersionTag;
    }

    public void setDefaultVersionTag(java.lang.String defaultVersionTag) {
        this.defaultVersionTag = defaultVersionTag;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("overrides")
    public java.util.Map<java.lang.Long, java.lang.String> getConfigGroupOverrides() {
        return configGroupOverrides;
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(defaultVersionTag.hashCode(), configGroupOverrides.hashCode());
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{");
        if (defaultVersionTag != null) {
            sb.append("default = ").append(defaultVersionTag);
        }
        if (!configGroupOverrides.isEmpty()) {
            sb.append(", overrides = [ ");
            int i = 0;
            for (java.util.Map.Entry<java.lang.Long, java.lang.String> entry : configGroupOverrides.entrySet()) {
                if ((i++) != 0) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append(" : ").append(entry.getValue());
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
}