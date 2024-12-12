package org.apache.ambari.server.stageplanner;
public class RoleGraphNode {
    public RoleGraphNode(org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command) {
        this.role = role;
        this.command = command;
    }

    private org.apache.ambari.server.Role role;

    private org.apache.ambari.server.RoleCommand command;

    private int inDegree = 0;

    private java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stageplanner.RoleGraphNode> edges = new java.util.TreeMap<>();

    public synchronized void addHost(java.lang.String host) {
        hosts.add(host);
    }

    public synchronized void addEdge(org.apache.ambari.server.stageplanner.RoleGraphNode rgn) {
        if (edges.containsKey(rgn.getRole().toString())) {
            return;
        }
        edges.put(rgn.getRole().toString(), rgn);
        rgn.incrementInDegree();
    }

    private synchronized void incrementInDegree() {
        inDegree++;
    }

    public org.apache.ambari.server.Role getRole() {
        return role;
    }

    public org.apache.ambari.server.RoleCommand getCommand() {
        return command;
    }

    public java.util.List<java.lang.String> getHosts() {
        return hosts;
    }

    public int getInDegree() {
        return inDegree;
    }

    java.util.Collection<org.apache.ambari.server.stageplanner.RoleGraphNode> getEdges() {
        return edges.values();
    }

    public synchronized void decrementInDegree() {
        inDegree--;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("(").append(role).append(", ").append(command).append(", ").append(inDegree).append(")");
        return builder.toString();
    }
}