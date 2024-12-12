package org.apache.ambari.server.serveraction.kerberos;
public class KerberosConfigDataFileWriter extends org.apache.ambari.server.serveraction.kerberos.AbstractKerberosDataFileWriter implements org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFile {
    KerberosConfigDataFileWriter(java.io.File file) throws java.io.IOException {
        super(file);
    }

    public void addRecord(java.lang.String config, java.lang.String key, java.lang.String value, java.lang.String operation) throws java.io.IOException {
        super.appendRecord(config, key, value, operation);
    }

    @java.lang.Override
    protected java.lang.Iterable<java.lang.String> getHeaderRecord() {
        return java.util.Arrays.asList(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFile.CONFIGURATION_TYPE, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFile.KEY, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFile.VALUE, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFile.OPERATION);
    }
}