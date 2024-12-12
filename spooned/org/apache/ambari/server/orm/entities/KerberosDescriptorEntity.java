package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.Table(name = "kerberos_descriptor")
@javax.persistence.Entity
@javax.persistence.NamedQuery(name = "allKerberosDescriptors", query = "SELECT kerberosDescriptor  FROM KerberosDescriptorEntity kerberosDescriptor")
public class KerberosDescriptorEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "kerberos_descriptor_name", nullable = false, insertable = true, updatable = false, unique = true, length = 100)
    private java.lang.String name;

    @javax.persistence.Column(name = "kerberos_descriptor", nullable = false, insertable = true, updatable = false)
    private java.lang.String kerberosDescriptorText;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getKerberosDescriptorText() {
        return kerberosDescriptorText;
    }

    public void setKerberosDescriptorText(java.lang.String kerberosDescriptorText) {
        this.kerberosDescriptorText = kerberosDescriptorText;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.KerberosDescriptorEntity other = ((org.apache.ambari.server.orm.entities.KerberosDescriptorEntity) (obj));
        return java.util.Objects.equals(name, other.name) && java.util.Objects.equals(kerberosDescriptorText, other.kerberosDescriptorText);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(name, kerberosDescriptorText);
    }
}