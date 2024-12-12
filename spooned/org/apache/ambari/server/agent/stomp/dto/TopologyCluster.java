package org.apache.ambari.server.agent.stomp.dto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class TopologyCluster {
    @com.fasterxml.jackson.annotation.JsonProperty("components")
    private java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> topologyComponents = new java.util.HashSet<>();

    @com.fasterxml.jackson.annotation.JsonProperty("hosts")
    private java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> topologyHosts = new java.util.HashSet<>();

    public TopologyCluster() {
    }

    public TopologyCluster(java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> topologyComponents, java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> topologyHosts) {
        this.topologyComponents = topologyComponents;
        this.topologyHosts = topologyHosts;
    }

    public void update(java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> componentsToUpdate, java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> hostsToUpdate, org.apache.ambari.server.events.UpdateEventType eventType, org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report) throws org.apache.ambari.server.NullHostNameException {
        for (org.apache.ambari.server.agent.stomp.dto.TopologyComponent componentToUpdate : componentsToUpdate) {
            boolean isPresent = false;
            for (java.util.Iterator<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> iter = getTopologyComponents().iterator(); iter.hasNext() && (!isPresent);) {
                org.apache.ambari.server.agent.stomp.dto.TopologyComponent existsComponent = iter.next();
                if (existsComponent.equals(componentToUpdate)) {
                    if (eventType.equals(org.apache.ambari.server.events.UpdateEventType.DELETE)) {
                        if (org.apache.commons.collections.SetUtils.isEqualSet(existsComponent.getHostIds(), componentToUpdate.getHostIds())) {
                            iter.remove();
                            report.mappingWasChanged();
                            report.addHostsNames(componentToUpdate.getHostNames());
                        } else if (existsComponent.removeComponent(componentToUpdate)) {
                            report.mappingWasChanged();
                            report.addHostsNames(componentToUpdate.getHostNames());
                        }
                    } else {
                        java.util.Set<java.lang.String> preExistNames = new java.util.HashSet<>(existsComponent.getHostNames());
                        if (existsComponent.updateComponent(componentToUpdate)) {
                            report.mappingWasChanged();
                            java.util.Set<java.lang.String> namesToUpdate = new java.util.HashSet<>(componentToUpdate.getHostNames());
                            namesToUpdate.removeAll(preExistNames);
                            report.addHostsNames(namesToUpdate);
                        }
                    }
                    isPresent = true;
                }
            }
            if ((!isPresent) && eventType.equals(org.apache.ambari.server.events.UpdateEventType.UPDATE)) {
                getTopologyComponents().add(componentToUpdate);
                report.mappingWasChanged();
                report.addHostsNames(componentToUpdate.getHostNames());
            }
        }
        for (org.apache.ambari.server.agent.stomp.dto.TopologyHost hostToUpdate : hostsToUpdate) {
            boolean isPresent = false;
            for (java.util.Iterator<org.apache.ambari.server.agent.stomp.dto.TopologyHost> iter = getTopologyHosts().iterator(); iter.hasNext() && (!isPresent);) {
                org.apache.ambari.server.agent.stomp.dto.TopologyHost existsHost = iter.next();
                if (existsHost.equals(hostToUpdate)) {
                    if (eventType.equals(org.apache.ambari.server.events.UpdateEventType.DELETE)) {
                        iter.remove();
                        report.mappingWasChanged();
                        report.addHostName(existsHost.getHostName());
                    } else if (existsHost.updateHost(hostToUpdate)) {
                        report.mappingWasChanged();
                        report.addHostName(existsHost.getHostName());
                        report.addHostName(hostToUpdate.getHostName());
                    }
                    isPresent = true;
                }
            }
            if ((!isPresent) && eventType.equals(org.apache.ambari.server.events.UpdateEventType.UPDATE)) {
                getTopologyHosts().add(hostToUpdate);
                report.mappingWasChanged();
                report.addHostName(hostToUpdate.getHostName());
            }
        }
    }

    public java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> getTopologyComponents() {
        return topologyComponents;
    }

    public void setTopologyComponents(java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> topologyComponents) {
        this.topologyComponents = topologyComponents;
    }

    public java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> getTopologyHosts() {
        return topologyHosts;
    }

    public java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> deepCopyTopologyHosts() {
        return topologyHosts;
    }

    public org.apache.ambari.server.agent.stomp.dto.TopologyCluster deepCopyCluster() {
        java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> copiedComponents = null;
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(topologyComponents)) {
            copiedComponents = new java.util.HashSet<>();
            for (org.apache.ambari.server.agent.stomp.dto.TopologyComponent topologyComponent : topologyComponents) {
                copiedComponents.add(topologyComponent.deepCopy());
            }
        }
        java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> copiedHosts = null;
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(topologyHosts)) {
            copiedHosts = new java.util.HashSet<>();
            for (org.apache.ambari.server.agent.stomp.dto.TopologyHost topologyHost : topologyHosts) {
                copiedHosts.add(topologyHost.deepCopy());
            }
        }
        return new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(copiedComponents, copiedHosts);
    }

    public void setTopologyHosts(java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> topologyHosts) {
        this.topologyHosts = topologyHosts;
    }

    public void addTopologyHost(org.apache.ambari.server.agent.stomp.dto.TopologyHost topologyHost) {
        topologyHosts.add(topologyHost);
    }

    public void addTopologyComponent(org.apache.ambari.server.agent.stomp.dto.TopologyComponent topologyComponent) {
        topologyComponents.add(topologyComponent);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.TopologyCluster that = ((org.apache.ambari.server.agent.stomp.dto.TopologyCluster) (o));
        if (topologyComponents != null ? !topologyComponents.equals(that.topologyComponents) : that.topologyComponents != null)
            return false;

        return topologyHosts != null ? topologyHosts.equals(that.topologyHosts) : that.topologyHosts == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (topologyComponents != null) ? topologyComponents.hashCode() : 0;
        result = (31 * result) + (topologyHosts != null ? topologyHosts.hashCode() : 0);
        return result;
    }
}