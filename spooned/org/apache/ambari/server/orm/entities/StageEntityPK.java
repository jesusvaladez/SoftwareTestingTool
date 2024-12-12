package org.apache.ambari.server.orm.entities;
import org.apache.commons.lang.builder.EqualsBuilder;
@java.lang.SuppressWarnings("serial")
public class StageEntityPK implements java.io.Serializable {
    private java.lang.Long requestId;

    private java.lang.Long stageId;

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

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.StageEntityPK that = ((org.apache.ambari.server.orm.entities.StageEntityPK) (object));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(requestId, that.requestId);
        equalsBuilder.append(stageId, that.stageId);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(requestId, stageId);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("StageEntityPK{");
        buffer.append("stageId=").append(getStageId());
        buffer.append("requestId=").append(getRequestId());
        buffer.append("}");
        return buffer.toString();
    }
}