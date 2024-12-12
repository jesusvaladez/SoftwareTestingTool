package org.apache.ambari.server.stack;
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.REFACTOR_TO_SPI)
public class HostsType {
    private final java.util.List<org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts> highAvailabilityHosts;

    private java.util.LinkedHashSet<java.lang.String> hosts;

    public java.util.List<org.apache.ambari.server.state.ServiceComponentHost> unhealthy = new java.util.ArrayList<>();

    public boolean hasMasters() {
        return !getMasters().isEmpty();
    }

    public java.util.List<org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts> getHighAvailabilityHosts() {
        return highAvailabilityHosts;
    }

    public void arrangeHostSecondariesFirst() {
        hosts = getHighAvailabilityHosts().stream().flatMap(each -> java.util.stream.Stream.concat(each.getSecondaries().stream(), java.util.stream.Stream.of(each.getMaster()))).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    public boolean hasMastersAndSecondaries() {
        return (!getMasters().isEmpty()) && (!getSecondaries().isEmpty());
    }

    public static class HighAvailabilityHosts {
        private final java.lang.String master;

        private final java.util.List<java.lang.String> secondaries;

        public HighAvailabilityHosts(java.lang.String master, java.util.List<java.lang.String> secondaries) {
            if (master == null) {
                throw new java.lang.IllegalArgumentException("Master host is missing");
            }
            this.master = master;
            this.secondaries = secondaries;
        }

        public java.lang.String getMaster() {
            return master;
        }

        public java.util.List<java.lang.String> getSecondaries() {
            return secondaries;
        }
    }

    public static org.apache.ambari.server.stack.HostsType from(java.lang.String master, java.lang.String secondary, java.util.LinkedHashSet<java.lang.String> hosts) {
        return master == null ? org.apache.ambari.server.stack.HostsType.normal(hosts) : new org.apache.ambari.server.stack.HostsType(java.util.Collections.singletonList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts(master, secondary != null ? java.util.Collections.singletonList(secondary) : java.util.Collections.emptyList())), hosts);
    }

    public static org.apache.ambari.server.stack.HostsType highAvailability(java.lang.String master, java.lang.String secondary, java.util.LinkedHashSet<java.lang.String> hosts) {
        return new org.apache.ambari.server.stack.HostsType(java.util.Collections.singletonList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts(master, java.util.Collections.singletonList(secondary))), hosts);
    }

    public static org.apache.ambari.server.stack.HostsType guessHighAvailability(java.util.LinkedHashSet<java.lang.String> hosts) {
        if (hosts.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot guess HA, empty hosts.");
        }
        java.lang.String master = hosts.iterator().next();
        java.util.List<java.lang.String> secondaries = hosts.stream().skip(1).collect(java.util.stream.Collectors.toList());
        return new org.apache.ambari.server.stack.HostsType(java.util.Collections.singletonList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts(master, secondaries)), hosts);
    }

    public static org.apache.ambari.server.stack.HostsType federated(java.util.List<org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts> highAvailabilityHosts, java.util.LinkedHashSet<java.lang.String> hosts) {
        return new org.apache.ambari.server.stack.HostsType(highAvailabilityHosts, hosts);
    }

    public static org.apache.ambari.server.stack.HostsType normal(java.util.LinkedHashSet<java.lang.String> hosts) {
        return new org.apache.ambari.server.stack.HostsType(java.util.Collections.emptyList(), hosts);
    }

    public static org.apache.ambari.server.stack.HostsType normal(java.lang.String... hosts) {
        return new org.apache.ambari.server.stack.HostsType(java.util.Collections.emptyList(), new java.util.LinkedHashSet<>(java.util.Arrays.asList(hosts)));
    }

    public static org.apache.ambari.server.stack.HostsType single(java.lang.String host) {
        return org.apache.ambari.server.stack.HostsType.normal(host);
    }

    public static org.apache.ambari.server.stack.HostsType healthy(org.apache.ambari.server.state.Cluster cluster) {
        java.util.LinkedHashSet<java.lang.String> hostNames = new java.util.LinkedHashSet<>();
        for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
            org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
            if (maintenanceState == org.apache.ambari.server.state.MaintenanceState.OFF) {
                hostNames.add(host.getHostName());
            }
        }
        return org.apache.ambari.server.stack.HostsType.normal(hostNames);
    }

    private HostsType(java.util.List<org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts> highAvailabilityHosts, java.util.LinkedHashSet<java.lang.String> hosts) {
        this.highAvailabilityHosts = highAvailabilityHosts;
        this.hosts = hosts;
    }

    public java.util.LinkedHashSet<java.lang.String> getMasters() {
        return highAvailabilityHosts.stream().map(each -> each.getMaster()).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    public java.util.LinkedHashSet<java.lang.String> getSecondaries() {
        return highAvailabilityHosts.stream().flatMap(each -> each.getSecondaries().stream()).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    public java.util.Set<java.lang.String> getHosts() {
        return hosts;
    }

    public void setHosts(java.util.LinkedHashSet<java.lang.String> hosts) {
        this.hosts = hosts;
    }
}