package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntityPK.class)
@javax.persistence.Entity
@javax.persistence.Table(name = "requestschedulebatchrequest")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "findByScheduleId", query = "SELECT batchreqs FROM " + "RequestScheduleBatchRequestEntity  batchreqs WHERE batchreqs.scheduleId=:id") })
public class RequestScheduleBatchRequestEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "schedule_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long scheduleId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "batch_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long batchId;

    @javax.persistence.Column(name = "request_id")
    private java.lang.Long requestId;

    @javax.persistence.Column(name = "request_type", length = 255)
    private java.lang.String requestType;

    @javax.persistence.Column(name = "request_uri", length = 1024)
    private java.lang.String requestUri;

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "request_body")
    private byte[] requestBody;

    @javax.persistence.Column(name = "request_status", length = 255)
    private java.lang.String requestStatus;

    @javax.persistence.Column(name = "return_code")
    private java.lang.Integer returnCode;

    @javax.persistence.Column(name = "return_message", length = 2000)
    private java.lang.String returnMessage;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id", nullable = false, insertable = false, updatable = false) })
    private org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity;

    public java.lang.Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(java.lang.Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public java.lang.Long getBatchId() {
        return batchId;
    }

    public void setBatchId(java.lang.Long batchId) {
        this.batchId = batchId;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.String getRequestType() {
        return requestType;
    }

    public void setRequestType(java.lang.String requestType) {
        this.requestType = requestType;
    }

    public java.lang.String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(java.lang.String requestUri) {
        this.requestUri = requestUri;
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

    public java.lang.String getRequestBodyAsString() {
        return requestBody != null ? new java.lang.String(requestBody) : null;
    }

    public void setRequestBody(byte[] requestBody) {
        this.requestBody = requestBody;
    }

    public void setRequestBody(java.lang.String requestBodyStr) {
        if (requestBodyStr != null) {
            requestBody = requestBodyStr.getBytes();
        }
    }

    public java.lang.String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(java.lang.String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public java.lang.Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(java.lang.Integer returnCode) {
        this.returnCode = returnCode;
    }

    public java.lang.String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(java.lang.String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public org.apache.ambari.server.orm.entities.RequestScheduleEntity getRequestScheduleEntity() {
        return requestScheduleEntity;
    }

    public void setRequestScheduleEntity(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        this.requestScheduleEntity = requestScheduleEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity that = ((org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity) (o));
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

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((("RequestScheduleBatchRequestEntity{" + "scheduleId=") + scheduleId) + ", batchId=") + batchId) + ", requestId=") + requestId) + ", requestType='") + requestType) + '\'') + ", requestUri='") + requestUri) + '\'') + ", requestStatus='") + requestStatus) + '\'') + ", returnCode=") + returnCode) + ", returnMessage='") + returnMessage) + '\'') + '}';
    }
}