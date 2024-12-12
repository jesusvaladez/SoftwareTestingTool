package org.apache.ambari.server.state;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
public class DesiredConfig {
    private java.lang.String tag;

    private java.lang.String serviceName;

    private java.lang.Long version;

    private java.util.List<org.apache.ambari.server.state.DesiredConfig.HostOverride> hostOverrides = new java.util.ArrayList<>();

    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    @org.codehaus.jackson.annotate.JsonProperty("tag")
    public java.lang.String getTag() {
        return tag;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
    @org.codehaus.jackson.annotate.JsonProperty("service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String name) {
        serviceName = name;
    }

    public void setHostOverrides(java.util.List<org.apache.ambari.server.state.DesiredConfig.HostOverride> overrides) {
        hostOverrides = overrides;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("host_overrides")
    public java.util.List<org.apache.ambari.server.state.DesiredConfig.HostOverride> getHostOverrides() {
        return hostOverrides;
    }

    @org.codehaus.jackson.annotate.JsonProperty("version")
    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    public static final class HostOverride {
        private final java.lang.String hostName;

        private final java.lang.String versionOverrideTag;

        public HostOverride(java.lang.String name, java.lang.String tag) {
            hostName = name;
            versionOverrideTag = tag;
        }

        @org.codehaus.jackson.annotate.JsonProperty("host_name")
        public java.lang.String getName() {
            return hostName;
        }

        @org.codehaus.jackson.annotate.JsonProperty("tag")
        public java.lang.String getVersionTag() {
            return versionOverrideTag;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.state.DesiredConfig.HostOverride that = ((org.apache.ambari.server.state.DesiredConfig.HostOverride) (o));
            return new org.apache.commons.lang.builder.EqualsBuilder().append(hostName, that.hostName).append(versionOverrideTag, that.versionOverrideTag).isEquals();
        }

        @java.lang.Override
        public int hashCode() {
            return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(hostName).append(versionOverrideTag).toHashCode();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{");
        sb.append("tag=").append(tag);
        if (null != serviceName)
            sb.append(", service=").append(serviceName);

        if ((null != hostOverrides) && (hostOverrides.size() > 0)) {
            sb.append(", hosts=[");
            int i = 0;
            for (org.apache.ambari.server.state.DesiredConfig.HostOverride h : hostOverrides) {
                if ((i++) != 0)
                    sb.append(",");

                sb.append(h.getName()).append(':').append(h.getVersionTag());
            }
            sb.append(']');
        }
        sb.append("}");
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.DesiredConfig that = ((org.apache.ambari.server.state.DesiredConfig) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(tag, that.tag).append(serviceName, that.serviceName).append(version, that.version).append(hostOverrides, that.hostOverrides).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(tag).append(serviceName).append(version).append(hostOverrides).toHashCode();
    }
}