package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class BulkCommandDefinition {
    private java.lang.String displayName;

    private java.lang.String masterComponent;

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getMasterComponent() {
        return masterComponent;
    }

    public void setMasterComponent(java.lang.String masterComponent) {
        this.masterComponent = masterComponent;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.apache.ambari.server.state.BulkCommandDefinition)) {
            return false;
        }
        org.apache.ambari.server.state.BulkCommandDefinition rhs = ((org.apache.ambari.server.state.BulkCommandDefinition) (obj));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(masterComponent, rhs.masterComponent).append(displayName, rhs.displayName).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 31).append(displayName).append(masterComponent).toHashCode();
    }
}