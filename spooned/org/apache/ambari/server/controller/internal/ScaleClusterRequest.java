package org.apache.ambari.server.controller.internal;
public class ScaleClusterRequest extends org.apache.ambari.server.controller.internal.BaseClusterRequest {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ScaleClusterRequest.class);

    private java.lang.String clusterName;

    public ScaleClusterRequest(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : propertySet) {
            if (getClusterName() == null) {
                setClusterName(java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            }
            setConfiguration(new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap()));
            parseHostGroups(properties);
        }
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.TopologyRequest.Type getType() {
        return org.apache.ambari.server.topology.TopologyRequest.Type.SCALE;
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return java.lang.String.format("Scale Cluster '%s' (+%s hosts)", clusterName, getTotalRequestedHostCount());
    }

    private void parseHostGroups(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        java.lang.String blueprintName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID));
        if ((blueprintName == null) || blueprintName.equals("null")) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Blueprint name must be specified for all host groups");
        }
        java.lang.String hgName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID));
        if ((hgName == null) || hgName.equals("null")) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("A name must be specified for all host groups");
        }
        org.apache.ambari.server.topology.Blueprint blueprint = getBlueprint();
        if (getBlueprint() == null) {
            blueprint = parseBlueprint(blueprintName);
            setBlueprint(blueprint);
        } else if (!blueprintName.equals(blueprint.getName())) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Currently, a scaling request may only refer to a single blueprint");
        }
        java.lang.String hostName = org.apache.ambari.server.controller.internal.HostResourceProvider.getHostNameFromProperties(properties);
        boolean containsHostCount = properties.containsKey(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID);
        boolean containsHostPredicate = properties.containsKey(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PREDICATE_PROPERTY_ID);
        if ((hostName != null) && (containsHostCount || containsHostPredicate)) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Can't specify host_count or host_predicate if host_name is specified in hostgroup: " + hgName);
        }
        if ((hostName == null) && (!containsHostCount)) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Must specify either host_name or host_count for hostgroup: " + hgName);
        }
        org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = getHostGroupInfo().get(hgName);
        if (hostGroupInfo == null) {
            if (blueprint.getHostGroup(hgName) == null) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Invalid host group specified in request: " + hgName);
            }
            hostGroupInfo = new org.apache.ambari.server.topology.HostGroupInfo(hgName);
            getHostGroupInfo().put(hgName, hostGroupInfo);
        }
        hostGroupInfo.setConfiguration(new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap()));
        if (containsHostCount) {
            if (containsHostPredicate) {
                java.lang.String predicate = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PREDICATE_PROPERTY_ID));
                validateHostPredicateProperties(predicate);
                try {
                    hostGroupInfo.setPredicate(predicate);
                } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Unable to compile host predicate '%s': %s", predicate, e), e);
                }
            }
            if (!hostGroupInfo.getHostNames().isEmpty()) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Can't specify both host_name and host_count for the same hostgroup: " + hgName);
            }
            hostGroupInfo.setRequestedCount(java.lang.Integer.parseInt(java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID))));
        } else {
            if (hostGroupInfo.getRequestedHostCount() != hostGroupInfo.getHostNames().size()) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Invalid host group specified in request: " + hgName);
            }
            hostGroupInfo.addHost(hostName);
            hostGroupInfo.addHostRackInfo(hostName, processRackInfo(properties));
        }
    }

    private java.lang.String processRackInfo(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String rackInfo = null;
        if (properties.containsKey(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID)) {
            rackInfo = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID)));
        } else if (properties.containsKey(org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID)) {
            rackInfo = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID)));
        } else {
            org.apache.ambari.server.controller.internal.ScaleClusterRequest.LOGGER.debug("No rack info provided");
        }
        return rackInfo;
    }

    private org.apache.ambari.server.topology.Blueprint parseBlueprint(java.lang.String blueprintName) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        org.apache.ambari.server.topology.Blueprint blueprint;
        try {
            blueprint = getBlueprintFactory().getBlueprint(blueprintName);
        } catch (org.apache.ambari.server.stack.NoSuchStackException e) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Invalid stack specified in the blueprint: " + blueprintName);
        }
        if (blueprint == null) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("The specified blueprint doesn't exist: " + blueprintName);
        }
        return blueprint;
    }

    private int getTotalRequestedHostCount() {
        int count = 0;
        for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : getHostGroupInfo().values()) {
            count += groupInfo.getRequestedHostCount();
        }
        return count;
    }
}