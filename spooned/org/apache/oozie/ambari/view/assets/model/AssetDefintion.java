package org.apache.oozie.ambari.view.assets.model;
public class AssetDefintion {
    private java.lang.String definition = org.apache.oozie.ambari.view.AssetDefinitionRefType.DB.name();

    private java.lang.String type;

    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String status;

    public java.lang.String getDefinition() {
        return definition;
    }

    public void setDefinition(java.lang.String definition) {
        this.definition = definition;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }
}