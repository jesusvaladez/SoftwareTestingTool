package org.apache.ambari.server.serveraction.kerberos;
public class KerberosIdentityDataFileWriter extends org.apache.ambari.server.serveraction.kerberos.AbstractKerberosDataFileWriter implements org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile {
    KerberosIdentityDataFileWriter(java.io.File file) throws java.io.IOException {
        super(file);
    }

    public void writeRecord(java.lang.String hostName, java.lang.String serviceName, java.lang.String serviceComponentName, java.lang.String principal, java.lang.String principalType, java.lang.String keytabFilePath, java.lang.String keytabFileOwnerName, java.lang.String keytabFileOwnerAccess, java.lang.String keytabFileGroupName, java.lang.String keytabFileGroupAccess, java.lang.String keytabFileCanCache) throws java.io.IOException {
        super.appendRecord(hostName, serviceName, serviceComponentName, principal, principalType, keytabFilePath, keytabFileOwnerName, keytabFileOwnerAccess, keytabFileGroupName, keytabFileGroupAccess, keytabFileCanCache);
    }

    @java.lang.Override
    protected java.lang.Iterable<java.lang.String> getHeaderRecord() {
        return java.util.Arrays.asList(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.HOSTNAME, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.SERVICE, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.COMPONENT, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.PRINCIPAL, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.PRINCIPAL_TYPE, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_PATH, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_OWNER_NAME, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_OWNER_ACCESS, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_GROUP_NAME, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_GROUP_ACCESS, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFile.KEYTAB_FILE_IS_CACHABLE);
    }
}