package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "requestresourcefilter")
@javax.persistence.TableGenerator(name = "resourcefilter_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "resourcefilter_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "RequestResourceFilterEntity.removeByRequestIds", query = "DELETE FROM RequestResourceFilterEntity filter WHERE filter.requestId IN :requestIds") })
public class RequestResourceFilterEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "filter_id", nullable = false, insertable = true, updatable = true)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "resourcefilter_id_generator")
    private java.lang.Long filterId;

    @javax.persistence.Column(name = "request_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long requestId;

    @javax.persistence.Column(name = "service_name")
    @javax.persistence.Basic
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "component_name")
    @javax.persistence.Basic
    private java.lang.String componentName;

    @javax.persistence.Column(name = "hosts")
    @javax.persistence.Lob
    private byte[] hosts;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "request_id", nullable = false, insertable = false, updatable = false)
    private org.apache.ambari.server.orm.entities.RequestEntity requestEntity;

    public java.lang.Long getFilterId() {
        return filterId;
    }

    public void setFilterId(java.lang.Long filterId) {
        this.filterId = filterId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getHosts() {
        return hosts != null ? new java.lang.String(hosts) : null;
    }

    public void setHosts(java.lang.String hosts) {
        this.hosts = (hosts != null) ? hosts.getBytes() : null;
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