package org.apache.ambari.server.agent.stomp.dto;
public class TopologyUpdateHandlingReport {
    private java.util.Set<java.lang.String> updatedHostNames = new java.util.HashSet<>();

    private boolean mappingChanged = false;

    public boolean wasChanged() {
        return mappingChanged || (!updatedHostNames.isEmpty());
    }

    public java.util.Set<java.lang.String> getUpdatedHostNames() {
        return updatedHostNames;
    }

    public void addHostName(java.lang.String updatedHostName) throws org.apache.ambari.server.NullHostNameException {
        if (updatedHostName == null) {
            throw new org.apache.ambari.server.NullHostNameException("Host name could not be a null");
        }
        this.updatedHostNames.add(updatedHostName);
    }

    public void addHostsNames(java.util.Set<java.lang.String> updatedHostNames) {
        this.updatedHostNames.addAll(updatedHostNames);
    }

    public void mappingWasChanged() {
        this.mappingChanged = true;
    }
}