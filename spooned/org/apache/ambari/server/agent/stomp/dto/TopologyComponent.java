package org.apache.ambari.server.agent.stomp.dto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class TopologyComponent {
    private java.lang.String componentName;

    private java.lang.String serviceName;

    private java.lang.String displayName;

    private java.lang.String version;

    private java.util.Set<java.lang.Long> hostIds = new java.util.HashSet<>();

    private java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();

    private java.util.Set<java.lang.String> publicHostNames = new java.util.HashSet<>();

    private java.util.TreeMap<java.lang.String, java.lang.String> componentLevelParams = new java.util.TreeMap<>();

    private java.util.TreeMap<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();

    private org.apache.ambari.server.state.State lastComponentState;

    private TopologyComponent() {
    }

    public static org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder newBuilder() {
        return new org.apache.ambari.server.agent.stomp.dto.TopologyComponent().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setComponentName(java.lang.String componentName) {
            TopologyComponent.this.setComponentName(componentName);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setServiceName(java.lang.String serviceName) {
            TopologyComponent.this.setServiceName(serviceName);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setDisplayName(java.lang.String displayName) {
            TopologyComponent.this.setDisplayName(displayName);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setVersion(java.lang.String version) {
            TopologyComponent.this.setVersion(version);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setHostIdentifiers(java.util.Set<java.lang.Long> hostIds, java.util.Set<java.lang.String> hostNames) {
            TopologyComponent.this.setHostIds(hostIds);
            TopologyComponent.this.setHostNames(hostNames);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setPublicHostNames(java.util.Set<java.lang.String> publicHostNames) {
            TopologyComponent.this.setPublicHostNames(publicHostNames);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setComponentLevelParams(java.util.TreeMap<java.lang.String, java.lang.String> componentLevelParams) {
            TopologyComponent.this.setComponentLevelParams(componentLevelParams);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setCommandParams(java.util.TreeMap<java.lang.String, java.lang.String> commandParams) {
            TopologyComponent.this.setCommandParams(commandParams);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent.Builder setLastComponentState(org.apache.ambari.server.state.State lastComponentState) {
            TopologyComponent.this.setLastComponentState(lastComponentState);
            return this;
        }

        public org.apache.ambari.server.agent.stomp.dto.TopologyComponent build() {
            return TopologyComponent.this;
        }
    }

    public boolean updateComponent(org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToUpdate) {
        boolean changed = false;
        if (componentToUpdate.getComponentName().equals(getComponentName())) {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(componentToUpdate.getVersion()) && (!componentToUpdate.getVersion().equals(getVersion()))) {
                setVersion(componentToUpdate.getVersion());
                changed = true;
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToUpdate.getHostIds())) {
                if (hostIds == null) {
                    hostIds = new java.util.HashSet<>();
                }
                changed |= hostIds.addAll(componentToUpdate.getHostIds());
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToUpdate.getHostNames())) {
                if (hostNames == null) {
                    hostNames = new java.util.HashSet<>();
                }
                changed |= hostNames.addAll(componentToUpdate.getHostNames());
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToUpdate.getPublicHostNames())) {
                if (publicHostNames == null) {
                    publicHostNames = new java.util.HashSet<>();
                }
                changed |= publicHostNames.addAll(componentToUpdate.getPublicHostNames());
            }
            changed |= mergeParams(componentLevelParams, componentToUpdate.getComponentLevelParams());
            changed |= mergeParams(commandParams, componentToUpdate.getCommandParams());
        }
        return changed;
    }

    private boolean mergeParams(java.util.TreeMap<java.lang.String, java.lang.String> currentParams, java.util.TreeMap<java.lang.String, java.lang.String> updateParams) {
        boolean changed = false;
        if (org.apache.commons.collections.MapUtils.isNotEmpty(updateParams)) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> updateParam : updateParams.entrySet()) {
                java.lang.String updateParamName = updateParam.getKey();
                java.lang.String updateParamValue = updateParam.getValue();
                if ((!currentParams.containsKey(updateParamName)) || (!org.apache.commons.lang.StringUtils.equals(currentParams.get(updateParamName), updateParamValue))) {
                    currentParams.put(updateParamName, updateParamValue);
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean removeComponent(org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToRemove) {
        boolean changed = false;
        if (componentToRemove.getComponentName().equals(getComponentName())) {
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToRemove.getHostIds())) {
                if (hostIds != null) {
                    hostIds.removeAll(componentToRemove.getHostIds());
                    changed = true;
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToRemove.getHostNames())) {
                if (hostNames != null) {
                    hostNames.removeAll(componentToRemove.getHostNames());
                    changed = true;
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentToRemove.getPublicHostNames())) {
                if (publicHostNames != null) {
                    publicHostNames.removeAll(componentToRemove.getPublicHostNames());
                    changed = true;
                }
            }
        }
        return changed;
    }

    public org.apache.ambari.server.agent.stomp.dto.TopologyComponent deepCopy() {
        return org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(getComponentName()).setDisplayName(getDisplayName()).setServiceName(getServiceName()).setComponentLevelParams(getComponentLevelParams() == null ? null : new java.util.TreeMap<>(getComponentLevelParams())).setHostIdentifiers(getHostIds() == null ? null : new java.util.HashSet<>(getHostIds()), getHostNames() == null ? null : new java.util.HashSet<>(getHostNames())).setPublicHostNames(getPublicHostNames() == null ? null : new java.util.HashSet<>(getPublicHostNames())).setCommandParams(getCommandParams() == null ? null : new java.util.TreeMap<>(getCommandParams())).build();
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.util.Set<java.lang.Long> getHostIds() {
        return hostIds;
    }

    public void setHostIds(java.util.Set<java.lang.Long> hostIds) {
        this.hostIds = hostIds;
    }

    public void addHostId(java.lang.Long hostId) {
        this.hostIds.add(hostId);
    }

    public void addHostName(java.lang.String hostName) {
        this.hostNames.add(hostName);
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getComponentLevelParams() {
        return componentLevelParams;
    }

    public void setComponentLevelParams(java.util.TreeMap<java.lang.String, java.lang.String> componentLevelParams) {
        this.componentLevelParams = componentLevelParams;
    }

    public java.util.Set<java.lang.String> getHostNames() {
        return hostNames;
    }

    public void setHostNames(java.util.Set<java.lang.String> hostNames) {
        this.hostNames = hostNames;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.util.Set<java.lang.String> getPublicHostNames() {
        return publicHostNames;
    }

    public void setPublicHostNames(java.util.Set<java.lang.String> publicHostNames) {
        this.publicHostNames = publicHostNames;
    }

    public java.util.TreeMap<java.lang.String, java.lang.String> getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(java.util.TreeMap<java.lang.String, java.lang.String> commandParams) {
        this.commandParams = commandParams;
    }

    public org.apache.ambari.server.state.State getLastComponentState() {
        return lastComponentState;
    }

    public void setLastComponentState(org.apache.ambari.server.state.State lastComponentState) {
        this.lastComponentState = lastComponentState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.TopologyComponent that = ((org.apache.ambari.server.agent.stomp.dto.TopologyComponent) (o));
        if (!componentName.equals(that.componentName))
            return false;

        return serviceName.equals(that.serviceName);
    }

    @java.lang.Override
    public int hashCode() {
        int result = componentName.hashCode();
        result = (31 * result) + serviceName.hashCode();
        return result;
    }
}