package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntityPK.class)
@javax.persistence.Table(name = "role_success_criteria")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "RoleSuccessCriteriaEntity.removeByRequestStageIds", query = "DELETE FROM RoleSuccessCriteriaEntity criteria WHERE criteria.stageId = :stageId AND criteria.requestId = :requestId") })
public class RoleSuccessCriteriaEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "request_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long requestId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "stage_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long stageId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "role")
    private java.lang.String role;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "success_factor", nullable = false)
    private java.lang.Double successFactor = 1.0;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "request_id", nullable = false), @javax.persistence.JoinColumn(name = "stage_id", referencedColumnName = "stage_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.StageEntity stage;

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.Long getStageId() {
        return stageId;
    }

    public void setStageId(java.lang.Long stageId) {
        this.stageId = stageId;
    }

    public org.apache.ambari.server.Role getRole() {
        return org.apache.ambari.server.Role.valueOf(role);
    }

    public void setRole(org.apache.ambari.server.Role role) {
        this.role = role.name();
    }

    public java.lang.Double getSuccessFactor() {
        return successFactor;
    }

    public void setSuccessFactor(java.lang.Double successFactor) {
        this.successFactor = successFactor;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity that = ((org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity) (o));
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null)
            return false;

        if (role != null ? !role.equals(that.role) : that.role != null)
            return false;

        if (stageId != null ? !stageId.equals(that.stageId) : that.stageId != null)
            return false;

        if (successFactor != null ? !successFactor.equals(that.successFactor) : that.successFactor != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (requestId != null) ? requestId.hashCode() : 0;
        result = (31 * result) + (stageId != null ? stageId.hashCode() : 0);
        result = (31 * result) + (role != null ? role.hashCode() : 0);
        result = (31 * result) + (successFactor != null ? successFactor.hashCode() : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.StageEntity getStage() {
        return stage;
    }

    public void setStage(org.apache.ambari.server.orm.entities.StageEntity stage) {
        this.stage = stage;
    }
}