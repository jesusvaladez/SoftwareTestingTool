package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public final class ServicePropertyInfo {
    private java.lang.String name;

    private java.lang.String value;

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (!(o instanceof org.apache.ambari.server.state.ServicePropertyInfo)))
            return false;

        org.apache.ambari.server.state.ServicePropertyInfo that = ((org.apache.ambari.server.state.ServicePropertyInfo) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(name, that.name).append(value, that.value).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(name).append(value).toHashCode();
    }
}