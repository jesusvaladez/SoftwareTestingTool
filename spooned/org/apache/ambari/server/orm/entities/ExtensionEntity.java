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
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "extension", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "extension_name", "extension_version" }))
@javax.persistence.TableGenerator(name = "extension_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "extension_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ExtensionEntity.findAll", query = "SELECT extension FROM ExtensionEntity extension"), @javax.persistence.NamedQuery(name = "ExtensionEntity.findByNameAndVersion", query = "SELECT extension FROM ExtensionEntity extension WHERE extension.extensionName = :extensionName AND extension.extensionVersion = :extensionVersion") })
public class ExtensionEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "extension_id_generator")
    @javax.persistence.Column(name = "extension_id", nullable = false, updatable = false)
    private java.lang.Long extensionId;

    @javax.persistence.Column(name = "extension_name", length = 255, nullable = false)
    private java.lang.String extensionName;

    @javax.persistence.Column(name = "extension_version", length = 255, nullable = false)
    private java.lang.String extensionVersion;

    public ExtensionEntity() {
    }

    public java.lang.Long getExtensionId() {
        return extensionId;
    }

    public java.lang.String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(java.lang.String extensionName) {
        this.extensionName = extensionName;
    }

    public java.lang.String getExtensionVersion() {
        return extensionVersion;
    }

    public void setExtensionVersion(java.lang.String extensionVersion) {
        this.extensionVersion = extensionVersion;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ExtensionEntity that = ((org.apache.ambari.server.orm.entities.ExtensionEntity) (object));
        if (extensionId != null ? !extensionId.equals(that.extensionId) : that.extensionId != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != extensionId) ? extensionId.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(extensionId);
        buffer.append(", name=").append(extensionName);
        buffer.append(", version=").append(extensionVersion);
        buffer.append("}");
        return buffer.toString();
    }
}