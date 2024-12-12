package org.apache.ambari.server.serveraction.kerberos;
@com.google.inject.Singleton
public class KerberosConfigDataFileWriterFactory {
    public org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter createKerberosConfigDataFileWriter(java.io.File file) throws java.io.IOException {
        return new org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter(file);
    }
}