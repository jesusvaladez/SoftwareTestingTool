package org.apache.ambari.server.serveraction.kerberos;
@com.google.inject.Singleton
public class KerberosIdentityDataFileReaderFactory {
    public org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader createKerberosIdentityDataFileReader(java.io.File file) throws java.io.IOException {
        return new org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader(file);
    }
}