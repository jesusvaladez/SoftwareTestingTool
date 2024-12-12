package org.apache.ambari.server.agent.stomp.dto;
import org.apache.commons.lang.StringUtils;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class TopologyHost {
    private java.lang.Long hostId;

    private java.lang.String hostName;

    private java.lang.String rackName;

    private java.lang.String ipv4;

    public TopologyHost(java.lang.Long hostId, java.lang.String hostName) {
        this.hostId = hostId;
        this.hostName = hostName;
    }

    public TopologyHost(java.lang.Long hostId, java.lang.String hostName, java.lang.String rackName, java.lang.String ipv4) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.rackName = rackName;
        this.ipv4 = ipv4;
    }

    public boolean updateHost(org.apache.ambari.server.agent.stomp.dto.TopologyHost hostToUpdate) {
        boolean changed = false;
        if (hostToUpdate.getHostId().equals(getHostId())) {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(hostToUpdate.getHostName()) && (!hostToUpdate.getHostName().equals(getHostName()))) {
                setHostName(hostToUpdate.getHostName());
                changed = true;
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(hostToUpdate.getRackName()) && (!hostToUpdate.getRackName().equals(getRackName()))) {
                setRackName(hostToUpdate.getRackName());
                changed = true;
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(hostToUpdate.getIpv4()) && (!hostToUpdate.getIpv4().equals(getIpv4()))) {
                setIpv4(hostToUpdate.getIpv4());
                changed = true;
            }
        }
        return changed;
    }

    public org.apache.ambari.server.agent.stomp.dto.TopologyHost deepCopy() {
        return new org.apache.ambari.server.agent.stomp.dto.TopologyHost(getHostId(), getHostName(), getRackName(), getIpv4());
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getRackName() {
        return rackName;
    }

    public void setRackName(java.lang.String rackName) {
        this.rackName = rackName;
    }

    public java.lang.String getIpv4() {
        return ipv4;
    }

    public void setIpv4(java.lang.String ipv4) {
        this.ipv4 = ipv4;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.TopologyHost that = ((org.apache.ambari.server.agent.stomp.dto.TopologyHost) (o));
        return hostId.equals(that.hostId);
    }

    @java.lang.Override
    public int hashCode() {
        return hostId.hashCode();
    }
}