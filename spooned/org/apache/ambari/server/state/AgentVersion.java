package org.apache.ambari.server.state;
public class AgentVersion {
    private final java.lang.String version;

    public AgentVersion(java.lang.String version) {
        this.version = version;
    }

    public java.lang.String getVersion() {
        return version;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof org.apache.ambari.server.state.AgentVersion)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        org.apache.ambari.server.state.AgentVersion a = ((org.apache.ambari.server.state.AgentVersion) (object));
        return a.version.equals(this.version);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (version != null) ? version.hashCode() : 0;
        return result;
    }
}