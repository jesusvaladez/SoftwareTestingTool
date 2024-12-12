package org.apache.ambari.server.metadata;
public class RoleCommandPair {
    private final org.apache.ambari.server.Role role;

    private final org.apache.ambari.server.RoleCommand cmd;

    public RoleCommandPair(org.apache.ambari.server.Role _role, org.apache.ambari.server.RoleCommand _cmd) {
        if ((_role == null) || (_cmd == null)) {
            throw new java.lang.IllegalArgumentException((("role = " + _role) + ", cmd = ") + _cmd);
        }
        this.role = _role;
        this.cmd = _cmd;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(role, cmd);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if ((((other != null) && (other instanceof org.apache.ambari.server.metadata.RoleCommandPair)) && ((org.apache.ambari.server.metadata.RoleCommandPair) (other)).role.equals(role)) && ((org.apache.ambari.server.metadata.RoleCommandPair) (other)).cmd.equals(cmd)) {
            return true;
        }
        return false;
    }

    org.apache.ambari.server.Role getRole() {
        return role;
    }

    org.apache.ambari.server.RoleCommand getCmd() {
        return cmd;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("RoleCommandPair{" + "role=") + role) + ", cmd=") + cmd) + '}';
    }
}