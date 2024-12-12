package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
@java.lang.SuppressWarnings("serial")
public class RoleSuccessCriteriaEntityPK implements java.io.Serializable {
    private java.lang.Long requestId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "request_id")
    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    private java.lang.Long stageId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "stage_id")
    public java.lang.Long getStageId() {
        return stageId;
    }

    public void setStageId(java.lang.Long stageId) {
        this.stageId = stageId;
    }

    private java.lang.String role;

    @javax.persistence.Column(name = "role")
    @javax.persistence.Id
    public org.apache.ambari.server.Role getRole() {
        return org.apache.ambari.server.Role.valueOf(role);
    }

    public void setRole(org.apache.ambari.server.Role role) {
        this.role = role.name();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntityPK that = ((org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntityPK) (o));
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null)
            return false;

        if (role != null ? !role.equals(that.role) : that.role != null)
            return false;

        if (stageId != null ? !stageId.equals(that.stageId) : that.stageId != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (requestId != null) ? requestId.hashCode() : 0;
        result = (31 * result) + (stageId != null ? stageId.hashCode() : 0);
        result = (31 * result) + (role != null ? role.hashCode() : 0);
        return result;
    }
}