package org.apache.ambari.server.orm.entities;
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
import javax.persistence.UniqueConstraint;
@javax.persistence.Table(name = "extensionlink", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "stack_id", "extension_id" }))
@javax.persistence.TableGenerator(name = "link_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "link_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ExtensionLinkEntity.findAll", query = "SELECT link FROM ExtensionLinkEntity link"), @javax.persistence.NamedQuery(name = "ExtensionLinkEntity.findByStackAndExtensionName", query = "SELECT link FROM ExtensionLinkEntity link WHERE link.stack.stackName = :stackName AND link.stack.stackVersion = :stackVersion AND link.extension.extensionName = :extensionName"), @javax.persistence.NamedQuery(name = "ExtensionLinkEntity.findByStackAndExtension", query = "SELECT link FROM ExtensionLinkEntity link WHERE link.stack.stackName = :stackName AND link.stack.stackVersion = :stackVersion AND link.extension.extensionName = :extensionName AND link.extension.extensionVersion = :extensionVersion"), @javax.persistence.NamedQuery(name = "ExtensionLinkEntity.findByStack", query = "SELECT link FROM ExtensionLinkEntity link WHERE link.stack.stackName = :stackName AND link.stack.stackVersion = :stackVersion"), @javax.persistence.NamedQuery(name = "ExtensionLinkEntity.findByExtension", query = "SELECT link FROM ExtensionLinkEntity link WHERE link.extension.extensionName = :extensionName AND link.extension.extensionVersion = :extensionVersion") })
@javax.persistence.Entity
public class ExtensionLinkEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "link_id_generator")
    @javax.persistence.Column(name = "link_id", nullable = false, updatable = false)
    private java.lang.Long linkId;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "stack_id", unique = false, nullable = false, insertable = true, updatable = false)
    private org.apache.ambari.server.orm.entities.StackEntity stack;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "extension_id", unique = false, nullable = false, insertable = true, updatable = false)
    private org.apache.ambari.server.orm.entities.ExtensionEntity extension;

    public ExtensionLinkEntity() {
    }

    public java.lang.Long getLinkId() {
        return linkId;
    }

    public void setLinkId(java.lang.Long linkId) {
        this.linkId = linkId;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStack() {
        return stack;
    }

    public void setStack(org.apache.ambari.server.orm.entities.StackEntity stack) {
        this.stack = stack;
    }

    public org.apache.ambari.server.orm.entities.ExtensionEntity getExtension() {
        return extension;
    }

    public void setExtension(org.apache.ambari.server.orm.entities.ExtensionEntity extension) {
        this.extension = extension;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ExtensionLinkEntity that = ((org.apache.ambari.server.orm.entities.ExtensionLinkEntity) (object));
        if (linkId != null ? !linkId.equals(that.linkId) : that.linkId != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != linkId) ? linkId.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("linkId=").append(linkId);
        buffer.append(", stackId=").append(stack.getStackId());
        buffer.append(", extensionId=").append(extension.getExtensionId());
        buffer.append("}");
        return buffer.toString();
    }
}