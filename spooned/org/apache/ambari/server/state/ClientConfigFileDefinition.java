package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ClientConfigFileDefinition {
    private java.lang.String type;

    private java.lang.String fileName;

    private java.lang.String dictionaryName;

    private boolean optional = false;

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getType() {
        return type;
    }

    public java.lang.String getFileName() {
        return fileName;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    public java.lang.String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(java.lang.String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof org.apache.ambari.server.state.ClientConfigFileDefinition)) {
            return false;
        }
        org.apache.ambari.server.state.ClientConfigFileDefinition rhs = ((org.apache.ambari.server.state.ClientConfigFileDefinition) (obj));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(type, rhs.type).append(fileName, rhs.fileName).append(dictionaryName, rhs.dictionaryName).append(optional, rhs.optional).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 31).append(type).append(fileName).append(dictionaryName).append(optional).toHashCode();
    }
}