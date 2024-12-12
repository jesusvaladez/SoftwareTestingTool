package org.apache.ambari.server.serveraction.kerberos;
public interface KerberosDataFile {
    void open() throws java.io.IOException;

    void close() throws java.io.IOException;
}