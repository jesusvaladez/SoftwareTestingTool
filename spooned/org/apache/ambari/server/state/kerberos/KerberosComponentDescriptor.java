package org.apache.ambari.server.state.kerberos;
public class KerberosComponentDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer {
    public KerberosComponentDescriptor(java.util.Map<?, ?> data) {
        super(data);
        setName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, "name"));
    }

    @java.lang.Override
    public java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> getChildContainers() {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer getChildContainer(java.lang.String name) {
        return null;
    }

    @java.lang.Override
    public int hashCode() {
        return 35 * super.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        return (object == this) || (((object != null) && (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.class)) && super.equals(object));
    }
}