package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Table(name = "mpacks")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "mpack_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "mpack_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "MpackEntity.findById", query = "SELECT mpack FROM MpackEntity mpack where mpack.id = :id"), @javax.persistence.NamedQuery(name = "MpackEntity.findAll", query = "SELECT mpack FROM MpackEntity mpack"), @javax.persistence.NamedQuery(name = "MpackEntity.findByNameVersion", query = "SELECT mpack FROM MpackEntity mpack where mpack.mpackName = :mpackName and mpack.mpackVersion = :mpackVersion") })
public class MpackEntity {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.entities.MpackEntity.class);

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "mpack_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "registry_id", nullable = true, insertable = true, updatable = false, length = 10)
    private java.lang.Long registryId;

    @javax.persistence.Column(name = "mpack_name", nullable = false, updatable = true)
    private java.lang.String mpackName;

    @javax.persistence.Column(name = "mpack_version", nullable = false)
    private java.lang.String mpackVersion;

    @javax.persistence.Column(name = "mpack_uri", nullable = false)
    private java.lang.String mpackUri;

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getRegistryId() {
        return registryId;
    }

    public java.lang.String getMpackName() {
        return mpackName;
    }

    public java.lang.String getMpackVersion() {
        return mpackVersion;
    }

    public java.lang.String getMpackUri() {
        return mpackUri;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public void setRegistryId(java.lang.Long registryId) {
        this.registryId = registryId;
    }

    public void setMpackName(java.lang.String mpackName) {
        this.mpackName = mpackName;
    }

    public void setMpackVersion(java.lang.String mpackVersion) {
        this.mpackVersion = mpackVersion;
    }

    public void setMpackUri(java.lang.String mpackUri) {
        this.mpackUri = mpackUri;
    }

    public MpackEntity() {
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.MpackEntity that = ((org.apache.ambari.server.orm.entities.MpackEntity) (object));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(id, that.id);
        equalsBuilder.append(mpackName, that.mpackName);
        equalsBuilder.append(mpackVersion, that.mpackVersion);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, mpackName, mpackVersion);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("MpackEntity{");
        buffer.append("id=").append(id);
        if (null != registryId) {
            buffer.append(", registryId=").append(registryId);
        }
        buffer.append(", mpackName=").append(mpackName);
        buffer.append(", mpackVersion=").append(mpackVersion);
        buffer.append(", mpackUri=").append(mpackUri);
        buffer.append("}");
        return buffer.toString();
    }
}