package org.apache.ambari.server.serveraction.kerberos;
public class KerberosConfigDataFileTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    private org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReaderFactory kerberosConfigDataFileReaderFactory = new org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReaderFactory();

    private org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory kerberosConfigDataFileWriterFactory = new org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory();

    @org.junit.Test
    public void testKerberosConfigDataFile() throws java.lang.Exception {
        java.io.File file = folder.newFile();
        junit.framework.Assert.assertNotNull(file);
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter writer = kerberosConfigDataFileWriterFactory.createKerberosConfigDataFileWriter(file);
        junit.framework.Assert.assertFalse(writer.isClosed());
        for (int i = 0; i < 10; i++) {
            writer.addRecord("config-type" + i, "key" + i, "value" + i, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_SET);
        }
        for (int i = 10; i < 15; i++) {
            writer.addRecord("config-type" + i, "key" + i, "value" + i, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_REMOVE);
        }
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader reader = kerberosConfigDataFileReaderFactory.createKerberosConfigDataFileReader(file);
        junit.framework.Assert.assertFalse(reader.isClosed());
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.String>> iterator = reader.iterator();
        junit.framework.Assert.assertNotNull(iterator);
        int i = 0;
        while (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.String> record = iterator.next();
            if (i < 15) {
                junit.framework.Assert.assertEquals("config-type" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.CONFIGURATION_TYPE));
                junit.framework.Assert.assertEquals("key" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.KEY));
                junit.framework.Assert.assertEquals("value" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.VALUE));
                if (i < 10) {
                    junit.framework.Assert.assertEquals("SET", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION));
                } else {
                    junit.framework.Assert.assertEquals("REMOVE", record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION));
                }
            }
            i++;
        } 
        junit.framework.Assert.assertEquals(15, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        reader.open();
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            if (i < 10) {
                junit.framework.Assert.assertEquals("config-type" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.CONFIGURATION_TYPE));
                junit.framework.Assert.assertEquals("key" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.KEY));
                junit.framework.Assert.assertEquals("value" + i, record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.VALUE));
            }
            i++;
        }
        junit.framework.Assert.assertEquals(15, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        writer.open();
        junit.framework.Assert.assertFalse(writer.isClosed());
        writer.addRecord("config-type", "key", "value", org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION_TYPE_SET);
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        reader = kerberosConfigDataFileReaderFactory.createKerberosConfigDataFileReader(file);
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(16, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        writer = kerberosConfigDataFileWriterFactory.createKerberosConfigDataFileWriter(file);
        junit.framework.Assert.assertFalse(writer.isClosed());
        writer.addRecord("config-type", "key", "value", org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION_TYPE_REMOVE);
        writer.close();
        junit.framework.Assert.assertTrue(writer.isClosed());
        reader.open();
        junit.framework.Assert.assertFalse(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(17, i);
        reader.close();
        junit.framework.Assert.assertTrue(reader.isClosed());
        i = 0;
        for (java.util.Map<java.lang.String, java.lang.String> record : reader) {
            i++;
        }
        junit.framework.Assert.assertEquals(0, i);
    }
}