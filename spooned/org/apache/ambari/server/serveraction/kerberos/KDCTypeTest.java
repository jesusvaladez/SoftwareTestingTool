package org.apache.ambari.server.serveraction.kerberos;
public class KDCTypeTest {
    @org.junit.Test
    public void testTranslateExact() {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC, org.apache.ambari.server.serveraction.kerberos.KDCType.translate("MIT_KDC"));
    }

    @org.junit.Test
    public void testTranslateCaseInsensitive() {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC, org.apache.ambari.server.serveraction.kerberos.KDCType.translate("mit_kdc"));
    }

    @org.junit.Test
    public void testTranslateHyphen() {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC, org.apache.ambari.server.serveraction.kerberos.KDCType.translate("MIT-KDC"));
    }

    @org.junit.Test
    public void testTranslateNull() {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.NONE, org.apache.ambari.server.serveraction.kerberos.KDCType.translate(null));
    }

    @org.junit.Test
    public void testTranslateEmptyString() {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.NONE, org.apache.ambari.server.serveraction.kerberos.KDCType.translate(""));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testTranslateNoTranslation() {
        org.apache.ambari.server.serveraction.kerberos.KDCType.translate("NO TRANSLATION");
    }
}