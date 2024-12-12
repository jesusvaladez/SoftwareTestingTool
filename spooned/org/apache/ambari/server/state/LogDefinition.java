package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class LogDefinition {
    private java.lang.String logId;

    private boolean primary;

    public java.lang.String getLogId() {
        return logId;
    }

    public boolean isPrimary() {
        return primary;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (!(obj instanceof org.apache.ambari.server.state.LogDefinition))
            return false;

        org.apache.ambari.server.state.LogDefinition other = ((org.apache.ambari.server.state.LogDefinition) (obj));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(logId, other.logId).append(primary, other.primary).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 31).append(logId).append(primary).toHashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}