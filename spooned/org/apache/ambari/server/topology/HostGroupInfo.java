package org.apache.ambari.server.topology;
public class HostGroupInfo {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.HostGroupInfo.class);

    private static org.apache.ambari.server.api.predicate.PredicateCompiler predicateCompiler = new org.apache.ambari.server.api.predicate.PredicateCompiler();

    private java.lang.String hostGroupName;

    private final java.util.Collection<java.lang.String> hostNames = new java.util.HashSet<>();

    private final java.util.Map<java.lang.String, java.lang.String> hostRackInfo = new java.util.HashMap<>();

    private int requested_count = 0;

    org.apache.ambari.server.topology.Configuration configuration;

    java.lang.String predicateString;

    org.apache.ambari.server.controller.spi.Predicate predicate;

    public HostGroupInfo(java.lang.String hostGroupName) {
        this.hostGroupName = hostGroupName;
    }

    public java.lang.String getHostGroupName() {
        return hostGroupName;
    }

    public java.util.Collection<java.lang.String> getHostNames() {
        synchronized(hostNames) {
            return new java.util.HashSet<>(hostNames);
        }
    }

    public int getRequestedHostCount() {
        synchronized(hostNames) {
            return requested_count == 0 ? hostNames.size() : requested_count;
        }
    }

    public void addHost(java.lang.String hostName) {
        synchronized(hostNames) {
            java.lang.String lowerHostName = hostName.toLowerCase();
            if (!hostName.equals(lowerHostName)) {
                org.apache.ambari.server.topology.HostGroupInfo.LOG.warn("Host name {} contains upper case letters, will be converted to lowercase!", hostName);
            }
            hostNames.add(lowerHostName);
        }
    }

    public void addHosts(java.util.Collection<java.lang.String> hosts) {
        synchronized(hostNames) {
            for (java.lang.String host : hosts) {
                addHost(host);
            }
        }
    }

    public void setRequestedCount(int num) {
        requested_count = num;
    }

    public void setConfiguration(org.apache.ambari.server.topology.Configuration configuration) {
        this.configuration = configuration;
    }

    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    public void setPredicate(java.lang.String predicateString) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        this.predicate = org.apache.ambari.server.topology.HostGroupInfo.predicateCompiler.compile(predicateString);
        this.predicateString = predicateString;
    }

    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return predicate;
    }

    public java.lang.String getPredicateString() {
        return predicateString;
    }

    public void addHostRackInfo(java.lang.String host, java.lang.String rackInfo) {
        synchronized(hostRackInfo) {
            hostRackInfo.put(host, rackInfo);
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getHostRackInfo() {
        synchronized(hostRackInfo) {
            return new java.util.HashMap<>(hostRackInfo);
        }
    }

    public void removeHost(java.lang.String hostname) {
        synchronized(hostNames) {
            hostNames.remove(hostname);
        }
    }
}