package org.apache.ambari.server.serveraction.kerberos;
public interface KerberosConfigDataFile extends org.apache.ambari.server.serveraction.kerberos.KerberosDataFile {
    java.lang.String DATA_FILE_NAME = "configs.dat";

    java.lang.String CONFIGURATION_TYPE = "config";

    java.lang.String KEY = "key";

    java.lang.String VALUE = "value";

    java.lang.String OPERATION = "operation";

    java.lang.String OPERATION_TYPE_SET = "SET";

    java.lang.String OPERATION_TYPE_REMOVE = "REMOVE";
}