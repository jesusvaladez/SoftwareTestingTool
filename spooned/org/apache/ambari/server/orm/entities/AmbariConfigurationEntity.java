package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.persistence.Table(name = "ambari_configuration")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AmbariConfigurationEntity.findByCategory", query = "select ace from AmbariConfigurationEntity ace where ace.categoryName = :categoryName"), @javax.persistence.NamedQuery(name = "AmbariConfigurationEntity.deleteByCategory", query = "delete from AmbariConfigurationEntity ace where ace.categoryName = :categoryName") })
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK.class)
@javax.persistence.Entity
public class AmbariConfigurationEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "category_name")
    private java.lang.String categoryName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "property_name")
    private java.lang.String propertyName;

    @javax.persistence.Column(name = "property_value")
    private java.lang.String propertyValue;

    public java.lang.String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(java.lang.String category) {
        this.categoryName = category;
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }

    public java.lang.String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(java.lang.String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("AmbariConfigurationEntity{" + " category=") + categoryName) + ", name=") + propertyName) + ", value=") + propertyValue) + '}';
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntity that = ((org.apache.ambari.server.orm.entities.AmbariConfigurationEntity) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(categoryName, that.categoryName).append(propertyName, that.propertyName).append(propertyValue, that.propertyValue).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(categoryName).append(propertyName).append(propertyValue).toHashCode();
    }
}