package org.apache.ambari.server.topology;
public interface KerberosDescriptor {
    java.lang.String getName();

    org.apache.ambari.server.orm.entities.KerberosDescriptorEntity toEntity();
}