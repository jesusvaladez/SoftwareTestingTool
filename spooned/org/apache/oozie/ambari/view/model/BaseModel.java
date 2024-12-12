package org.apache.oozie.ambari.view.model;
public class BaseModel implements org.apache.oozie.ambari.view.model.When , java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private java.lang.String createdAt;

    private java.lang.String updatedAt;

    private java.lang.String owner;

    public java.lang.String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.lang.String createdAt) {
        this.createdAt = createdAt;
    }

    public java.lang.String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.lang.String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.lang.String getOwner() {
        return owner;
    }

    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }
}