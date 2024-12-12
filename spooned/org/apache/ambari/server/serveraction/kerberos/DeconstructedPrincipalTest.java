package org.apache.ambari.server.serveraction.kerberos;
public class DeconstructedPrincipalTest {
    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testNullPrincipal() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf(null, null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testEmptyPrincipal() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("", null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testInvalidPrincipal() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("/invalid", null);
    }

    @org.junit.Test
    public void testPrimary() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("primary", "REALM");
        org.junit.Assert.assertNotNull(deconstructedPrincipal);
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrimary());
        org.junit.Assert.assertNull(deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("REALM", deconstructedPrincipal.getRealm());
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrincipalName());
        org.junit.Assert.assertEquals("primary@REALM", deconstructedPrincipal.getNormalizedPrincipal());
    }

    @org.junit.Test
    public void testPrimaryRealm() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("primary@MYREALM", "REALM");
        org.junit.Assert.assertNotNull(deconstructedPrincipal);
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrimary());
        org.junit.Assert.assertNull(deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("MYREALM", deconstructedPrincipal.getRealm());
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrincipalName());
        org.junit.Assert.assertEquals("primary@MYREALM", deconstructedPrincipal.getNormalizedPrincipal());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testInstance() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("/instance", "REALM");
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testInstanceRealm() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("/instance@MYREALM", "REALM");
    }

    @org.junit.Test
    public void testPrimaryInstance() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("primary/instance", "REALM");
        org.junit.Assert.assertNotNull(deconstructedPrincipal);
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrimary());
        org.junit.Assert.assertEquals("instance", deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("instance", deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("REALM", deconstructedPrincipal.getRealm());
        org.junit.Assert.assertEquals("primary/instance", deconstructedPrincipal.getPrincipalName());
        org.junit.Assert.assertEquals("primary/instance@REALM", deconstructedPrincipal.getNormalizedPrincipal());
    }

    @org.junit.Test
    public void testPrimaryInstanceRealm() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("primary/instance@MYREALM", "REALM");
        org.junit.Assert.assertNotNull(deconstructedPrincipal);
        org.junit.Assert.assertEquals("primary", deconstructedPrincipal.getPrimary());
        org.junit.Assert.assertEquals("instance", deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("MYREALM", deconstructedPrincipal.getRealm());
        org.junit.Assert.assertEquals("primary/instance", deconstructedPrincipal.getPrincipalName());
        org.junit.Assert.assertEquals("primary/instance@MYREALM", deconstructedPrincipal.getNormalizedPrincipal());
    }

    @org.junit.Test
    public void testOddCharacters() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf("p_ri.ma-ry/i.n_s-tance@M_Y-REALM.COM", "REALM");
        org.junit.Assert.assertNotNull(deconstructedPrincipal);
        org.junit.Assert.assertEquals("p_ri.ma-ry", deconstructedPrincipal.getPrimary());
        org.junit.Assert.assertEquals("i.n_s-tance", deconstructedPrincipal.getInstance());
        org.junit.Assert.assertEquals("M_Y-REALM.COM", deconstructedPrincipal.getRealm());
        org.junit.Assert.assertEquals("p_ri.ma-ry/i.n_s-tance", deconstructedPrincipal.getPrincipalName());
        org.junit.Assert.assertEquals("p_ri.ma-ry/i.n_s-tance@M_Y-REALM.COM", deconstructedPrincipal.getNormalizedPrincipal());
    }
}