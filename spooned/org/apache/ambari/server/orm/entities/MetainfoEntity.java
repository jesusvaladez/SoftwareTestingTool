package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@javax.persistence.Table(name = "metainfo")
@javax.persistence.Entity
public class MetainfoEntity {
    @javax.persistence.Column(name = "\"metainfo_key\"", length = 255)
    @javax.persistence.Id
    private java.lang.String metainfoName;

    @javax.persistence.Column(name = "\"metainfo_value\"", length = 32000)
    @javax.persistence.Basic
    @javax.persistence.Lob
    private java.lang.String metainfoValue;

    public java.lang.String getMetainfoName() {
        return metainfoName;
    }

    public void setMetainfoName(java.lang.String metainfoName) {
        this.metainfoName = metainfoName;
    }

    public java.lang.String getMetainfoValue() {
        return metainfoValue;
    }

    public void setMetainfoValue(java.lang.String metainfoValue) {
        this.metainfoValue = metainfoValue;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.MetainfoEntity that = ((org.apache.ambari.server.orm.entities.MetainfoEntity) (o));
        if (metainfoName != null ? !metainfoName.equals(that.metainfoName) : that.metainfoName != null) {
            return false;
        }
        if (metainfoValue != null ? !metainfoValue.equals(that.metainfoValue) : that.metainfoValue != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (metainfoName != null) ? metainfoName.hashCode() : 0;
        result = (31 * result) + (metainfoValue != null ? metainfoValue.hashCode() : 0);
        return result;
    }
}