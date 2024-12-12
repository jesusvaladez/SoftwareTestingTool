package org.apache.ambari.server.topology;
public class KerberosDescriptorImpl implements org.apache.ambari.server.topology.KerberosDescriptor {
    private final java.lang.String name;

    private final java.lang.String descriptor;

    public KerberosDescriptorImpl(java.lang.String name, java.lang.String descriptor) {
        this.name = name;
        this.descriptor = descriptor;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getDescriptor() {
        return descriptor;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.KerberosDescriptorEntity toEntity() {
        org.apache.ambari.server.orm.entities.KerberosDescriptorEntity entity = new org.apache.ambari.server.orm.entities.KerberosDescriptorEntity();
        entity.setName(getName());
        entity.setKerberosDescriptorText(getDescriptor());
        return entity;
    }
}