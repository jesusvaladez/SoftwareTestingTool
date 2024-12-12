package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
@javax.persistence.Table(name = "key_value_store")
@javax.persistence.Entity
public class KeyValueEntity {
    @javax.persistence.Column(name = "\"key\"", length = 255)
    @javax.persistence.Id
    private java.lang.String key;

    @javax.persistence.Column(name = "\"value\"", length = 32000)
    @javax.persistence.Lob
    private java.lang.String value;

    public java.lang.String getKey() {
        return key;
    }

    public void setKey(java.lang.String key) {
        this.key = key;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.KeyValueEntity that = ((org.apache.ambari.server.orm.entities.KeyValueEntity) (o));
        if (key != null ? !key.equals(that.key) : that.key != null)
            return false;

        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (key != null) ? key.hashCode() : 0;
        result = (31 * result) + (value != null ? value.hashCode() : 0);
        return result;
    }
}