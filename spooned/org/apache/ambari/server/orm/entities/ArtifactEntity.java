package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ArtifactEntityPK.class)
@javax.persistence.Table(name = "artifact")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "artifactByNameAndForeignKeys", query = "SELECT artifact FROM ArtifactEntity artifact WHERE artifact.artifactName=:artifactName AND artifact.foreignKeys=:foreignKeys", hints = { @javax.persistence.QueryHint(name = org.eclipse.persistence.config.QueryHints.QUERY_RESULTS_CACHE, value = org.eclipse.persistence.config.HintValues.TRUE), @javax.persistence.QueryHint(name = org.eclipse.persistence.config.QueryHints.QUERY_RESULTS_CACHE_SIZE, value = "100") }), @javax.persistence.NamedQuery(name = "artifactByName", query = "SELECT artifact FROM ArtifactEntity artifact WHERE artifact.artifactName=:artifactName"), @javax.persistence.NamedQuery(name = "artifactByForeignKeys", query = "SELECT artifact FROM ArtifactEntity artifact WHERE artifact.foreignKeys=:foreignKeys") })
@javax.persistence.Entity
public class ArtifactEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "artifact_name", nullable = false, insertable = true, updatable = false, unique = true)
    private java.lang.String artifactName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "foreign_keys", nullable = false, insertable = true, updatable = false)
    @javax.persistence.Basic
    private java.lang.String foreignKeys;

    @javax.persistence.Column(name = "artifact_data", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String artifactData;

    @javax.persistence.Transient
    private static final com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();

    public java.lang.String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(java.lang.String artifactName) {
        this.artifactName = artifactName;
    }

    public void setArtifactData(java.util.Map<java.lang.String, java.lang.Object> artifactData) {
        this.artifactData = org.apache.ambari.server.orm.entities.ArtifactEntity.jsonSerializer.toJson(artifactData);
    }

    public java.util.Map<java.lang.String, java.lang.Object> getArtifactData() {
        return org.apache.ambari.server.orm.entities.ArtifactEntity.jsonSerializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(artifactData, java.util.Map.class);
    }

    public void setForeignKeys(java.util.Map<java.lang.String, java.lang.String> foreignKeys) {
        this.foreignKeys = org.apache.ambari.server.orm.entities.ArtifactEntity.serializeForeignKeys(foreignKeys);
    }

    public java.util.Map<java.lang.String, java.lang.String> getForeignKeys() {
        return foreignKeys == null ? java.util.Collections.emptyMap() : org.apache.ambari.server.orm.entities.ArtifactEntity.jsonSerializer.<java.util.Map<java.lang.String, java.lang.String>>fromJson(foreignKeys, java.util.Map.class);
    }

    public static java.lang.String serializeForeignKeys(java.util.Map<java.lang.String, java.lang.String> foreignKeys) {
        return org.apache.ambari.server.orm.entities.ArtifactEntity.jsonSerializer.toJson(foreignKeys);
    }
}