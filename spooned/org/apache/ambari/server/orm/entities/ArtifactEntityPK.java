package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class ArtifactEntityPK {
    @javax.persistence.Id
    @javax.persistence.Column(name = "artifact_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String artifactName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "foreign_keys", nullable = false, insertable = true, updatable = false)
    private java.lang.String foreignKeys;

    public ArtifactEntityPK(java.lang.String artifactName, java.lang.String foreignKeys) {
        this.artifactName = artifactName;
        this.foreignKeys = foreignKeys;
    }

    public java.lang.String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(java.lang.String name) {
        artifactName = name;
    }

    public java.lang.String getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(java.lang.String foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ArtifactEntityPK that = ((org.apache.ambari.server.orm.entities.ArtifactEntityPK) (o));
        return this.artifactName.equals(that.artifactName) && this.foreignKeys.equals(that.foreignKeys);
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * artifactName.hashCode()) + foreignKeys.hashCode();
    }
}