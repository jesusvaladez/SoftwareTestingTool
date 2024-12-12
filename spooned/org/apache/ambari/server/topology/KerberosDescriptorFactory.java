package org.apache.ambari.server.topology;
public class KerberosDescriptorFactory {
    public org.apache.ambari.server.topology.KerberosDescriptor createKerberosDescriptor(java.lang.String name, java.lang.String descriptor) {
        org.apache.ambari.server.topology.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.topology.KerberosDescriptorImpl(name, descriptor);
        return kerberosDescriptor;
    }
}