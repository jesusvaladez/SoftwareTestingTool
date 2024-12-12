package org.apache.ambari.server.orm.entities;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class AmbariConfigurationEntityPK implements java.io.Serializable {
    private java.lang.String categoryName;

    private java.lang.String propertyName;

    public AmbariConfigurationEntityPK(java.lang.String categoryName, java.lang.String propertyName) {
        this.categoryName = categoryName;
        this.propertyName = propertyName;
    }

    public java.lang.String getCategoryName() {
        return categoryName;
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK that = ((org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(categoryName, that.categoryName).append(propertyName, that.propertyName).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(categoryName).append(propertyName).toHashCode();
    }
}