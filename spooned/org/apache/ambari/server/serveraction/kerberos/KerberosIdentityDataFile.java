package org.apache.ambari.server.serveraction.kerberos;
public interface KerberosIdentityDataFile extends org.apache.ambari.server.serveraction.kerberos.KerberosDataFile {
    java.lang.String DATA_FILE_NAME = "identity.dat";

    java.lang.String HOSTNAME = "hostname";

    java.lang.String SERVICE = "service";

    java.lang.String COMPONENT = "component";

    java.lang.String PRINCIPAL = "principal";

    java.lang.String PRINCIPAL_TYPE = "principal_type";

    java.lang.String KEYTAB_FILE_PATH = "keytab_file_path";

    java.lang.String KEYTAB_FILE_OWNER_NAME = "keytab_file_owner_name";

    java.lang.String KEYTAB_FILE_OWNER_ACCESS = "keytab_file_owner_access";

    java.lang.String KEYTAB_FILE_GROUP_NAME = "keytab_file_group_name";

    java.lang.String KEYTAB_FILE_GROUP_ACCESS = "keytab_file_group_access";

    java.lang.String KEYTAB_FILE_IS_CACHABLE = "keytab_file_is_cachable";
}