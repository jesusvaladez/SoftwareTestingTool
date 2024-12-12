package org.apache.ambari.server.state.kerberos;
@com.google.inject.Singleton
public class KerberosDescriptorFactory extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorFactory {
    public org.apache.ambari.server.state.kerberos.KerberosDescriptor createInstance(java.io.File file) throws java.io.IOException {
        try {
            return new org.apache.ambari.server.state.kerberos.KerberosDescriptor(parseFile(file));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("An error occurred processing the JSON-formatted file: %s", file.getAbsolutePath()), e);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosDescriptor createInstance(java.lang.String json) throws org.apache.ambari.server.AmbariException {
        try {
            return new org.apache.ambari.server.state.kerberos.KerberosDescriptor(parseJSON(json));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariException("An error occurred processing the JSON-formatted string", e);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosDescriptor createInstance(java.util.Map<?, ?> map) {
        return new org.apache.ambari.server.state.kerberos.KerberosDescriptor(map);
    }
}