package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
@javax.persistence.Entity
@javax.persistence.Table(name = "stack", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "stack_name", "stack_version" }))
@javax.persistence.TableGenerator(name = "stack_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "stack_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "StackEntity.findByMpack", query = "SELECT stack FROM StackEntity stack where stack.mpackId = :mpackId"), @javax.persistence.NamedQuery(name = "StackEntity.findAll", query = "SELECT stack FROM StackEntity stack"), @javax.persistence.NamedQuery(name = "StackEntity.findByNameAndVersion", query = "SELECT stack FROM StackEntity stack WHERE stack.stackName = :stackName AND stack.stackVersion = :stackVersion", hints = { @javax.persistence.QueryHint(name = org.eclipse.persistence.config.QueryHints.QUERY_RESULTS_CACHE, value = org.eclipse.persistence.config.HintValues.TRUE), @javax.persistence.QueryHint(name = org.eclipse.persistence.config.QueryHints.QUERY_RESULTS_CACHE_SIZE, value = "100") }) })
public class StackEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "stack_id_generator")
    @javax.persistence.Column(name = "stack_id", nullable = false, updatable = false)
    private java.lang.Long stackId;

    @javax.persistence.Column(name = "stack_name", length = 255, nullable = false)
    private java.lang.String stackName;

    @javax.persistence.Column(name = "stack_version", length = 255, nullable = false)
    private java.lang.String stackVersion;

    @javax.persistence.Column(name = "mpack_id")
    private java.lang.Long mpackId;

    public java.lang.Long getMpackId() {
        return mpackId;
    }

    public void setMpackId(java.lang.Long mpackId) {
        this.mpackId = mpackId;
    }

    public StackEntity() {
    }

    public java.lang.Long getStackId() {
        return stackId;
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.StackEntity that = ((org.apache.ambari.server.orm.entities.StackEntity) (object));
        if (stackId != null ? !stackId.equals(that.stackId) : that.stackId != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != stackId) ? stackId.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(stackId);
        buffer.append(", name=").append(stackName);
        buffer.append(", version=").append(stackVersion);
        buffer.append(", mpack_id=").append(mpackId);
        buffer.append("}");
        return buffer.toString();
    }
}