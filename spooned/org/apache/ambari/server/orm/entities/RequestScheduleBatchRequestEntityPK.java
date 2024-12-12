package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class RequestScheduleBatchRequestEntityPK implements java.io.Serializable {
    private java.lang.Long scheduleId;

    private java.lang.Long batchId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "schedule_id", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(java.lang.Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    @javax.persistence.Id
    @javax.persistence.Column(name = "batch_id", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getBatchId() {
        return batchId;
    }

    public void setBatchId(java.lang.Long batchId) {
        this.batchId = batchId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntityPK that = ((org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntityPK) (o));
        if (!batchId.equals(that.batchId))
            return false;

        if (!scheduleId.equals(that.scheduleId))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = scheduleId.hashCode();
        result = (31 * result) + batchId.hashCode();
        return result;
    }
}