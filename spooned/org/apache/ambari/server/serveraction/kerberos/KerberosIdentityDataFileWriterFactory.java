package org.apache.ambari.server.serveraction.kerberos;
@com.google.inject.Singleton
public class KerberosIdentityDataFileWriterFactory {
    public org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter createKerberosIdentityDataFileWriter(java.io.File file) throws java.io.IOException {
        return new org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter(file);
    }
}