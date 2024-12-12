package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "requestoperationlevel")
@javax.persistence.TableGenerator(name = "operation_level_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "operation_level_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "requestOperationLevelByHostId", query = "SELECT requestOperationLevel FROM RequestOperationLevelEntity requestOperationLevel " + "WHERE requestOperationLevel.hostId=:hostId"), @javax.persistence.NamedQuery(name = "RequestOperationLevelEntity.removeByRequestIds", query = "DELETE FROM RequestOperationLevelEntity requestOperationLevel WHERE requestOperationLevel.requestId IN :requestIds") })
public class RequestOperationLevelEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "operation_level_id", nullable = false, insertable = true, updatable = true)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "operation_level_id_generator")
    private java.lang.Long operationLevelId;

    @javax.persistence.Column(name = "request_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long requestId;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "request_id", nullable = false, insertable = false, updatable = false)
    private org.apache.ambari.server.orm.entities.RequestEntity requestEntity;

    public java.lang.Long getOperationLevelId() {
        return operationLevelId;
    }

    public void setOperationLevelId(java.lang.Long operationLevelId) {
        this.operationLevelId = operationLevelId;
    }

    @javax.persistence.Column(name = "level_name")
    @javax.persistence.Basic
    private java.lang.String level;

    @javax.persistence.Column(name = "cluster_name")
    @javax.persistence.Basic
    private java.lang.String clusterName;

    @javax.persistence.Column(name = "service_name")
    @javax.persistence.Basic
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "host_component_name")
    @javax.persistence.Basic
    private java.lang.String hostComponentName;

    @javax.persistence.Column(name = "host_id", nullable = true, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Long hostId;

    public java.lang.String getLevel() {
        return level;
    }

    public void setLevel(java.lang.String level) {
        this.level = level;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getHostComponentName() {
        return hostComponentName;
    }

    public void setHostComponentName(java.lang.String hostComponentName) {
        this.hostComponentName = hostComponentName;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public org.apache.ambari.server.orm.entities.RequestEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(org.apache.ambari.server.orm.entities.RequestEntity request) {
        this.requestEntity = request;
    }
}