package org.apache.ambari.server.state;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class PropertyInfo {
    private java.lang.String name;

    private java.lang.String value;

    private java.lang.String description;

    @javax.xml.bind.annotation.XmlElement(name = "display-name")
    private java.lang.String displayName;

    private java.lang.String filename;

    private boolean deleted;

    @javax.xml.bind.annotation.XmlElement(name = "on-ambari-upgrade", required = true)
    private org.apache.ambari.server.state.PropertyUpgradeBehavior propertyAmbariUpgradeBehavior;

    @javax.xml.bind.annotation.XmlElement(name = "on-stack-upgrade")
    private org.apache.ambari.server.state.PropertyStackUpgradeBehavior propertyStackUpgradeBehavior = new org.apache.ambari.server.state.PropertyStackUpgradeBehavior();

    @javax.xml.bind.annotation.XmlAttribute(name = "require-input")
    private boolean requireInput;

    @javax.xml.bind.annotation.XmlElement(name = "property-type")
    @javax.xml.bind.annotation.XmlList
    private java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes = new java.util.HashSet<>();

    @javax.xml.bind.annotation.XmlAnyElement
    private java.util.List<org.w3c.dom.Element> propertyAttributes = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElement(name = "value-attributes")
    private org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes = new org.apache.ambari.server.state.ValueAttributesInfo();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "depends-on")
    @javax.xml.bind.annotation.XmlElement(name = "property")
    private java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties = new java.util.HashSet<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "property_depended_by")
    private java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependedByProperties = new java.util.HashSet<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "used-by")
    @javax.xml.bind.annotation.XmlElement(name = "property")
    private java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> usedByProperties = new java.util.HashSet<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "supported-refresh-commands")
    @javax.xml.bind.annotation.XmlElement(name = "refresh-command")
    private java.util.Set<org.apache.ambari.server.state.RefreshCommand> supportedRefreshCommands = new java.util.HashSet<>();

    void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, java.lang.Object parent) {
        propertyTypes.remove(null);
    }

    public PropertyInfo() {
        propertyAmbariUpgradeBehavior = new org.apache.ambari.server.state.PropertyUpgradeBehavior();
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getUsedByProperties() {
        return usedByProperties;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getFilename() {
        return filename;
    }

    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public org.apache.ambari.server.state.PropertyUpgradeBehavior getPropertyAmbariUpgradeBehavior() {
        return propertyAmbariUpgradeBehavior;
    }

    public void setPropertyAmbariUpgradeBehavior(org.apache.ambari.server.state.PropertyUpgradeBehavior propertyAmbariUpgradeBehavior) {
        this.propertyAmbariUpgradeBehavior = propertyAmbariUpgradeBehavior;
    }

    public org.apache.ambari.server.controller.StackConfigurationResponse convertToResponse() {
        return new org.apache.ambari.server.controller.StackConfigurationResponse(getName(), getValue(), getDescription(), getDisplayName(), getFilename(), isRequireInput(), getPropertyTypes(), getAttributesMap(), getPropertyValueAttributes(), getDependsOnProperties());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAttributesMap() {
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        for (org.w3c.dom.Element propertyAttribute : propertyAttributes) {
            attributes.put(propertyAttribute.getTagName(), propertyAttribute.getFirstChild().getNodeValue());
        }
        java.lang.String hidden = getPropertyValueAttributes().getHidden();
        if (hidden != null) {
            attributes.putIfAbsent("hidden", hidden);
        }
        return attributes;
    }

    public org.apache.ambari.server.state.ValueAttributesInfo getPropertyValueAttributes() {
        return propertyValueAttributes;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
        return dependsOnProperties;
    }

    public void setPropertyValueAttributes(org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes) {
        this.propertyValueAttributes = propertyValueAttributes;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependedByProperties() {
        return dependedByProperties;
    }

    public boolean isRequireInput() {
        return requireInput;
    }

    public void setRequireInput(boolean requireInput) {
        this.requireInput = requireInput;
    }

    public java.util.List<org.w3c.dom.Element> getPropertyAttributes() {
        return propertyAttributes;
    }

    public void setPropertyAttributes(java.util.List<org.w3c.dom.Element> propertyAttributes) {
        this.propertyAttributes = propertyAttributes;
    }

    public java.util.Set<org.apache.ambari.server.state.RefreshCommand> getSupportedRefreshCommands() {
        return supportedRefreshCommands;
    }

    public void setSupportedRefreshCommands(java.util.Set<org.apache.ambari.server.state.RefreshCommand> supportedRefreshCommands) {
        this.supportedRefreshCommands = supportedRefreshCommands;
    }

    public boolean shouldBeConfigured() {
        return !getName().contains("*");
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (description == null ? 0 : description.hashCode());
        result = (prime * result) + (filename == null ? 0 : filename.hashCode());
        result = (prime * result) + (name == null ? 0 : name.hashCode());
        result = (prime * result) + (value == null ? 0 : value.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        org.apache.ambari.server.state.PropertyInfo other = ((org.apache.ambari.server.state.PropertyInfo) (obj));
        if (description == null) {
            if (other.description != null)
                return false;

        } else if (!description.equals(other.description))
            return false;

        if (filename == null) {
            if (other.filename != null)
                return false;

        } else if (!filename.equals(other.filename))
            return false;

        if (name == null) {
            if (other.name != null)
                return false;

        } else if (!name.equals(other.name))
            return false;

        if (value == null) {
            if (other.value != null)
                return false;

        } else if (!value.equals(other.value))
            return false;

        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((((((("PropertyInfo{" + "name='") + name) + '\'') + ", value='") + value) + '\'') + ", description='") + description) + '\'') + ", filename='") + filename) + '\'') + ", deleted=") + deleted) + ", requireInput=") + requireInput) + ", propertyTypes=") + propertyTypes) + ", propertyAttributes=") + propertyAttributes) + ", propertyValueAttributes=") + propertyValueAttributes) + ", dependsOnProperties=") + dependsOnProperties) + ", dependedByProperties=") + dependedByProperties) + '}';
    }

    public org.apache.ambari.server.state.PropertyStackUpgradeBehavior getPropertyStackUpgradeBehavior() {
        return propertyStackUpgradeBehavior;
    }

    public void setPropertyStackUpgradeBehavior(org.apache.ambari.server.state.PropertyStackUpgradeBehavior propertyStackUpgradeBehavior) {
        this.propertyStackUpgradeBehavior = propertyStackUpgradeBehavior;
    }

    public enum PropertyType {

        PASSWORD,
        USER,
        UID,
        GROUP,
        GID,
        TEXT,
        ADDITIONAL_USER_PROPERTY,
        NOT_MANAGED_HDFS_PATH,
        VALUE_FROM_PROPERTY_FILE,
        KERBEROS_PRINCIPAL;}
}