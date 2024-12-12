package org.apache.oozie.ambari.view.assets.model;
public class ActionAsset extends org.apache.oozie.ambari.view.model.BaseModel implements org.apache.oozie.ambari.view.model.Indexed {
    private static final long serialVersionUID = 1L;

    private java.lang.String id;

    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String type;

    private java.lang.String definitionRefType = org.apache.oozie.ambari.view.AssetDefinitionRefType.DB.name();

    private java.lang.String definitionRef;

    private java.lang.String status = org.apache.oozie.ambari.view.EntityStatus.DRAFT.name();

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
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

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getDefinitionRef() {
        return definitionRef;
    }

    public void setDefinitionRef(java.lang.String definitionRef) {
        this.definitionRef = definitionRef;
    }

    public java.lang.String getDefinitionRefType() {
        return definitionRefType;
    }

    public void setDefinitionRefType(java.lang.String definitionRefType) {
        this.definitionRefType = definitionRefType;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }
}