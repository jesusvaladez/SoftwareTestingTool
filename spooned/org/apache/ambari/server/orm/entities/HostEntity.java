package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Entity
@javax.persistence.Table(name = "hosts")
@javax.persistence.TableGenerator(name = "host_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "host_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "HostEntity.findByHostName", query = "SELECT host FROM HostEntity host WHERE host.hostName = :hostName") })
public class HostEntity implements java.lang.Comparable<org.apache.ambari.server.orm.entities.HostEntity> {
    @javax.persistence.Id
    @javax.persistence.Column(name = "host_id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "host_id_generator")
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "host_name", nullable = false, insertable = true, updatable = true, unique = true)
    @javax.persistence.Basic
    private java.lang.String hostName;

    @javax.persistence.Column(name = "ipv4", nullable = true, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String ipv4;

    @javax.persistence.Column(name = "ipv6", nullable = true, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String ipv6;

    @javax.persistence.Column(name = "public_host_name", nullable = true, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String publicHostName;

    @javax.persistence.Column(name = "total_mem", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Long totalMem = 0L;

    @javax.persistence.Column(name = "cpu_count", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Integer cpuCount = 0;

    @javax.persistence.Column(name = "ph_cpu_count", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Integer phCpuCount = 0;

    @javax.persistence.Column(name = "cpu_info", insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String cpuInfo = "";

    @javax.persistence.Column(name = "os_arch", insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String osArch = "";

    @javax.persistence.Column(name = "os_info", insertable = true, updatable = true, length = 1000)
    @javax.persistence.Basic
    private java.lang.String osInfo = "";

    @javax.persistence.Column(name = "os_type", insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String osType = "";

    @javax.persistence.Column(name = "discovery_status", insertable = true, updatable = true, length = 2000)
    @javax.persistence.Basic
    private java.lang.String discoveryStatus = "";

    @javax.persistence.Column(name = "last_registration_time", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Long lastRegistrationTime = 0L;

    @javax.persistence.Column(name = "rack_info", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String rackInfo = "/default-rack";

    @javax.persistence.Column(name = "host_attributes", insertable = true, updatable = true, length = 20000)
    @javax.persistence.Basic
    @javax.persistence.Lob
    private java.lang.String hostAttributes = "";

    @javax.persistence.OneToMany(mappedBy = "hostEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities;

    @javax.persistence.OneToMany(mappedBy = "hostEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities;

    @javax.persistence.OneToMany(mappedBy = "hostEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities;

    @javax.persistence.ManyToMany
    @javax.persistence.JoinTable(name = "ClusterHostMapping", joinColumns = { @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id") })
    private java.util.Collection<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities;

    @javax.persistence.OneToOne(mappedBy = "hostEntity", cascade = { javax.persistence.CascadeType.REMOVE, javax.persistence.CascadeType.PERSIST })
    private org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity;

    @javax.persistence.OneToMany(mappedBy = "hostEntity", cascade = javax.persistence.CascadeType.REMOVE)
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities;

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

    public java.lang.String getIpv4() {
        return ipv4;
    }

    public void setIpv4(java.lang.String ipv4) {
        this.ipv4 = ipv4;
    }

    public java.lang.String getIpv6() {
        return ipv6;
    }

    public void setIpv6(java.lang.String ipv6) {
        this.ipv6 = ipv6;
    }

    public java.lang.String getPublicHostName() {
        return publicHostName;
    }

    public void setPublicHostName(java.lang.String name) {
        publicHostName = name;
    }

    public java.lang.Long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(java.lang.Long totalMem) {
        this.totalMem = totalMem;
    }

    public java.lang.Integer getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(java.lang.Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    public java.lang.Integer getPhCpuCount() {
        return phCpuCount;
    }

    public void setPhCpuCount(java.lang.Integer phCpuCount) {
        this.phCpuCount = phCpuCount;
    }

    public java.lang.String getCpuInfo() {
        return StringUtils.defaultString(cpuInfo);
    }

    public void setCpuInfo(java.lang.String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public java.lang.String getOsArch() {
        return StringUtils.defaultString(osArch);
    }

    public void setOsArch(java.lang.String osArch) {
        this.osArch = osArch;
    }

    public java.lang.String getOsInfo() {
        return StringUtils.defaultString(osInfo);
    }

    public void setOsInfo(java.lang.String osInfo) {
        this.osInfo = osInfo;
    }

    public java.lang.String getOsType() {
        return StringUtils.defaultString(osType);
    }

    public void setOsType(java.lang.String osType) {
        this.osType = osType;
    }

    public java.lang.String getDiscoveryStatus() {
        return StringUtils.defaultString(discoveryStatus);
    }

    public void setDiscoveryStatus(java.lang.String discoveryStatus) {
        this.discoveryStatus = discoveryStatus;
    }

    public java.lang.Long getLastRegistrationTime() {
        return lastRegistrationTime;
    }

    public void setLastRegistrationTime(java.lang.Long lastRegistrationTime) {
        this.lastRegistrationTime = lastRegistrationTime;
    }

    public java.lang.String getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(java.lang.String rackInfo) {
        this.rackInfo = rackInfo;
    }

    public java.lang.String getHostAttributes() {
        return StringUtils.defaultString(hostAttributes);
    }

    public void setHostAttributes(java.lang.String hostAttributes) {
        this.hostAttributes = hostAttributes;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.HostEntity that = ((org.apache.ambari.server.orm.entities.HostEntity) (o));
        return java.util.Objects.equals(getHostId(), that.getHostId()) && java.util.Objects.equals(hostName, that.hostName);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(getHostId(), hostName);
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.orm.entities.HostEntity other) {
        return hostName.compareTo(other.hostName);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> getHostComponentDesiredStateEntities() {
        return java.util.Collections.unmodifiableCollection(hostComponentDesiredStateEntities);
    }

    public void addHostComponentDesiredStateEntity(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity) {
        hostComponentDesiredStateEntities.add(stateEntity);
    }

    public void removeHostComponentDesiredStateEntity(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity) {
        hostComponentDesiredStateEntities.remove(stateEntity);
    }

    public void setHostComponentDesiredStateEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities) {
        this.hostComponentDesiredStateEntities = hostComponentDesiredStateEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> getHostComponentStateEntities() {
        return java.util.Collections.unmodifiableCollection(hostComponentStateEntities);
    }

    public void addHostComponentStateEntity(org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity) {
        hostComponentStateEntities.add(stateEntity);
    }

    public void removeHostComponentStateEntity(org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity) {
        hostComponentStateEntities.remove(stateEntity);
    }

    public void setHostComponentStateEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities) {
        this.hostComponentStateEntities = hostComponentStateEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ClusterEntity> getClusterEntities() {
        return clusterEntities;
    }

    public void setClusterEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities) {
        this.clusterEntities = clusterEntities;
    }

    public org.apache.ambari.server.orm.entities.HostStateEntity getHostStateEntity() {
        return hostStateEntity;
    }

    public void setHostStateEntity(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        this.hostStateEntity = hostStateEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> getHostRoleCommandEntities() {
        return hostRoleCommandEntities;
    }

    public void setHostRoleCommandEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities) {
        this.hostRoleCommandEntities = hostRoleCommandEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> getHostVersionEntities() {
        return hostVersionEntities;
    }

    public void setHostVersionEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities) {
        this.hostVersionEntities = hostVersionEntities;
    }
}