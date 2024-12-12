package org.apache.ambari.server.serveraction.kerberos;
public class KerberosIdentityDataFileTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    private org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReaderFactory kerberosIdentityDataFileReaderFactory = new org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReaderFactory();

    private org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory kerberosIdentityDataFileWriterFactory = new org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory();

    @org.junit.Test
    public void testKerberosIdentityDataFile() throws java.lang.Exception {
        java.io.File file = folder.newFile();
        junit.framework.Assert.assertNotNull(file);
        org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter writer = kerberosIdentityDataFileWriterFactory.createKerberosIdentityDataFileWriter(file);
        junit.framework.Assert.assertFalse(writer.isClosed());
        for (int i = 0; i < 10; i++) {
            writer.writeRecord("hostName" + i, "serviceName" + i, "serviceComponentName" + i, "principal" + i, "principal_type" + i, "keytabFilePath" + i, "keytabFileOwnerName" + i, "keytabFileOwnerAccess" + i, "keytabFileGroupName" + i, "keytabFileGroupAccess" + i, "false");
        }
        writer.writeRecord("hostName's", "serviceName#", "serviceComponentName\"", "principal", "principal_type", "keytabFilePath", "'keytabFileOwnerName'", "<keytabFileOwnerAccess>", "\"keytabFileGroupName\"", "keytab,File,Group,Access", "false");
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader reader = kerberosIdentityDataFileReaderFactory.createKerberosIdentityDataFileReader(file);
        junit.framework.Assert.assertFalse(reader.isClosed());
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.String>> iterator = reader.iterator();
        junit.framework.Assert.assertNotNull(iterator);
        int i = 0;
        while (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.String> record = iterator.next();
            if (i < 10) {
                junit.framework.Assert.assertEquals("hostName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME));
                junit.framework.Assert.assertEquals("serviceName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.SERVICE));
                junit.framework.Assert.assertEquals("serviceComponentName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.COMPONENT));
                junit.framework.Assert.assertEquals("principal" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL));
                junit.framework.Assert.assertEquals("principal_type" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL_TYPE));
                junit.framework.Assert.assertEquals("keytabFilePath" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH));
                junit.framework.Assert.assertEquals("keytabFileOwnerName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_NAME));
                junit.framework.Assert.assertEquals("keytabFileOwnerAccess" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_ACCESS));
                junit.framework.Assert.assertEquals("keytabFileGroupName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_NAME));
                junit.framework.Assert.assertEquals("keytabFileGroupAccess" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_ACCESS));
                junit.framework.Assert.assertEquals("false", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_IS_CACHABLE));
            } else {
                junit.framework.Assert.assertEquals("hostName's", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME));
                junit.framework.Assert.assertEquals("serviceName#", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.SERVICE));
                junit.framework.Assert.assertEquals("serviceComponentName\"", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.COMPONENT));
                junit.framework.Assert.assertEquals("principal", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL));
                junit.framework.Assert.assertEquals("principal_type", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL_TYPE));
                junit.framework.Assert.assertEquals("keytabFilePath", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH));
                junit.framework.Assert.assertEquals("'keytabFileOwnerName'", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_NAME));
                junit.framework.Assert.assertEquals("<keytabFileOwnerAccess>", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_ACCESS));
                junit.framework.Assert.assertEquals("\"keytabFileGroupName\"", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_NAME));
                junit.framework.Assert.assertEquals("keytab,File,Group,Access", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_ACCESS));
                junit.framework.Assert.assertEquals("false", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_IS_CACHABLE));
            }
            i++;
        } 
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        reader.open();
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            if (i < 10) {
                junit.framework.Assert.assertEquals("hostName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME));
                junit.framework.Assert.assertEquals("serviceName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.SERVICE));
                junit.framework.Assert.assertEquals("serviceComponentName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.COMPONENT));
                junit.framework.Assert.assertEquals("principal" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL));
                junit.framework.Assert.assertEquals("principal_type" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL_TYPE));
                junit.framework.Assert.assertEquals("keytabFilePath" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH));
                junit.framework.Assert.assertEquals("keytabFileOwnerName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_NAME));
                junit.framework.Assert.assertEquals("keytabFileOwnerAccess" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_ACCESS));
                junit.framework.Assert.assertEquals("keytabFileGroupName" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_NAME));
                junit.framework.Assert.assertEquals("keytabFileGroupAccess" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_ACCESS));
            } else {
                junit.framework.Assert.assertEquals("hostName's", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME));
                junit.framework.Assert.assertEquals("serviceName#", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.SERVICE));
                junit.framework.Assert.assertEquals("serviceComponentName\"", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.COMPONENT));
                junit.framework.Assert.assertEquals("principal", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL));
                junit.framework.Assert.assertEquals("principal_type", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL_TYPE));
                junit.framework.Assert.assertEquals("keytabFilePath", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH));
                junit.framework.Assert.assertEquals("'keytabFileOwnerName'", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_NAME));
                junit.framework.Assert.assertEquals("<keytabFileOwnerAccess>", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_ACCESS));
                junit.framework.Assert.assertEquals("\"keytabFileGroupName\"", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_NAME));
                junit.framework.Assert.assertEquals("keytab,File,Group,Access", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_ACCESS));
            }
            i++;
        }
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        writer.open();
        junit.framework.Assert.assertFalse(writer.isClosed());
        writer.writeRecord("hostName", "serviceName", "serviceComponentName", "principal", "principal_type", "keytabFilePath", "keytabFileOwnerName", "keytabFileOwnerAccess", "keytabFileGroupName", "keytabFileGroupAccess", "true");
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        reader = kerberosIdentityDataFileReaderFactory.createKerberosIdentityDataFileReader(file);
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(12, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        writer = kerberosIdentityDataFileWriterFactory.createKerberosIdentityDataFileWriter(file);
        junit.framework.Assert.assertFalse(writer.isClosed());
        writer.writeRecord("hostName", "serviceName", "serviceComponentName", "principal", "principal_type", "keytabFilePath", "keytabFileOwnerName", "keytabFileOwnerAccess", "keytabFileGroupName", "keytabFileGroupAccess", "true");
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        reader.open();
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(13, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(0, i);
    }
}