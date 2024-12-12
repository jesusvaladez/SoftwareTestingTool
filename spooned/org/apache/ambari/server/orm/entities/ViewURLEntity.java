package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "viewurl")
@javax.persistence.TableGenerator(name = "viewurl_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "viewurl_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "allViewUrls", query = "SELECT viewUrl FROM ViewURLEntity viewUrl"), @javax.persistence.NamedQuery(name = "viewUrlByName", query = "SELECT viewUrlEntity FROM ViewURLEntity viewUrlEntity WHERE viewUrlEntity.urlName=:urlName"), @javax.persistence.NamedQuery(name = "viewUrlBySuffix", query = "SELECT viewUrlEntity FROM ViewURLEntity viewUrlEntity WHERE viewUrlEntity.urlSuffix=:urlSuffix") })
@javax.persistence.Entity
public class ViewURLEntity {
    @javax.persistence.Column(name = "url_id")
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "viewentity_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "url_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String urlName;

    @javax.persistence.Column(name = "url_suffix", nullable = false, insertable = true, updatable = true)
    private java.lang.String urlSuffix;

    @javax.persistence.OneToOne(fetch = javax.persistence.FetchType.LAZY, mappedBy = "viewUrl")
    private org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity;

    public java.lang.String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(java.lang.String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getUrlName() {
        return urlName;
    }

    public void setUrlName(java.lang.String urlName) {
        this.urlName = urlName;
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity() {
        return viewInstanceEntity;
    }

    public void setViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        this.viewInstanceEntity = viewInstanceEntity;
    }

    public void clearEntity() {
        this.viewInstanceEntity = null;
    }
}