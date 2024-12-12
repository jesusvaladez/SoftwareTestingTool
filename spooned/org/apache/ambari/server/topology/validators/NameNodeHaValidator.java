package org.apache.ambari.server.topology.validators;
public class NameNodeHaValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.NameNodeHaValidator.class);

    private static final com.google.common.base.Splitter SPLITTER = com.google.common.base.Splitter.on(',').omitEmptyStrings().trimResults();

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        if (topology.isNameNodeHAEnabled()) {
            if (!allHostNamesAreKnown(topology)) {
                org.apache.ambari.server.topology.validators.NameNodeHaValidator.LOG.warn("Provision cluster template contains hostgroup(s) without explicit hostnames. Cannot validate NAMENODE HA setup in this case.");
                return;
            }
            java.util.Collection<java.lang.String> nnHosts = topology.getHostAssignmentsForComponent("NAMENODE");
            if (nnHosts.isEmpty()) {
                org.apache.ambari.server.topology.validators.NameNodeHaValidator.LOG.info("NAMENODE HA is enabled but there are no NAMENODE components in the cluster. Assuming external name nodes.");
                validateExternalNamenodeHa(topology);
            } else if (nnHosts.size() == 1) {
                throw new org.apache.ambari.server.topology.InvalidTopologyException("NAMENODE HA requires at least two hosts running NAMENODE but there is only one: " + nnHosts.iterator().next());
            }
            java.util.Map<java.lang.String, java.lang.String> hadoopEnvConfig = topology.getConfiguration().getFullProperties().get("hadoop-env");
            if ((((hadoopEnvConfig != null) && (!hadoopEnvConfig.isEmpty())) && hadoopEnvConfig.containsKey("dfs_ha_initial_namenode_active")) && hadoopEnvConfig.containsKey("dfs_ha_initial_namenode_standby")) {
                if (((!org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(hadoopEnvConfig.get("dfs_ha_initial_namenode_active")).matches()) && (!nnHosts.contains(hadoopEnvConfig.get("dfs_ha_initial_namenode_active")))) || ((!org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(hadoopEnvConfig.get("dfs_ha_initial_namenode_standby")).matches()) && (!nnHosts.contains(hadoopEnvConfig.get("dfs_ha_initial_namenode_standby"))))) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyException("NAMENODE HA hosts mapped incorrectly for properties 'dfs_ha_initial_namenode_active' and 'dfs_ha_initial_namenode_standby'. Expected hosts are: " + nnHosts);
                }
            }
        }
    }

    private boolean allHostNamesAreKnown(org.apache.ambari.server.topology.ClusterTopology topology) {
        return topology.getHostGroupInfo().values().stream().allMatch(hg -> !hg.getHostNames().isEmpty());
    }

    public void validateExternalNamenodeHa(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Map<java.lang.String, java.lang.String> hdfsSite = topology.getConfiguration().getFullProperties().get("hdfs-site");
        java.util.Set<java.lang.String> hosts = topology.getAllHosts();
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : getNameServicesToNameNodes(hdfsSite).entrySet()) {
            java.lang.String nameService = entry.getKey();
            java.util.List<java.lang.String> nameNodes = entry.getValue();
            for (java.lang.String nameNode : nameNodes) {
                java.lang.String address = hdfsSite.get((("dfs.namenode.rpc-address." + nameService) + ".") + nameNode);
                checkValidExternalNameNodeAddress(nameService, nameNode, address, hosts);
            }
        }
    }

    private void checkValidExternalNameNodeAddress(java.lang.String nameService, java.lang.String nameNode, java.lang.String address, java.util.Set<java.lang.String> hosts) throws org.apache.ambari.server.topology.InvalidTopologyException {
        final java.lang.String errorMessage = "Illegal external HA NAMENODE address for name service [%s], namenode [%s]: [%s]. Address " + "must be in <host>:<port> format where host is external to the cluster.";
        checkInvalidTopology(((com.google.common.base.Strings.isNullOrEmpty(address) || address.contains("localhost")) || address.contains("%HOSTGROUP")) || (!address.contains(":")), errorMessage, nameService, nameNode, address);
        java.lang.String hostName = address.substring(0, address.indexOf(':'));
        checkInvalidTopology(hostName.isEmpty() || hosts.contains(hostName), errorMessage, nameService, nameNode, address);
    }

    java.util.Map<java.lang.String, java.util.List<java.lang.String>> getNameServicesToNameNodes(java.util.Map<java.lang.String, java.lang.String> hdfsSite) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.lang.String nameServices = (null != hdfsSite.get("dfs.internal.nameservices")) ? hdfsSite.get("dfs.internal.nameservices") : hdfsSite.get("dfs.nameservices");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> nameServicesToNameNodes = new java.util.HashMap<>();
        for (java.lang.String ns : org.apache.ambari.server.topology.validators.NameNodeHaValidator.SPLITTER.splitToList(nameServices)) {
            java.lang.String nameNodes = hdfsSite.get("dfs.ha.namenodes." + ns);
            checkInvalidTopology(com.google.common.base.Strings.isNullOrEmpty(nameNodes), "No namenodes specified for nameservice %s.", ns);
            nameServicesToNameNodes.put(ns, org.apache.ambari.server.topology.validators.NameNodeHaValidator.SPLITTER.splitToList(nameNodes));
        }
        return nameServicesToNameNodes;
    }

    private void checkInvalidTopology(boolean errorCondition, java.lang.String errorMessageTemplate, java.lang.Object... errorMessageParams) throws org.apache.ambari.server.topology.InvalidTopologyException {
        if (errorCondition)
            throw new org.apache.ambari.server.topology.InvalidTopologyException(java.lang.String.format(errorMessageTemplate, errorMessageParams));

    }
}