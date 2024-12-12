package org.apache.ambari.server.utils;
import static org.apache.ambari.server.utils.VersionUtils.DEV_VERSION;
public class TestVersionUtils {
    private static final java.lang.String MODULE_ERR_MESSAGE = "Module version can't be empty or null";

    private static final java.lang.String STACK_ERR_MESSAGE = "Stack version can't be empty or null";

    private static final java.lang.String MPACK_ERR_MESSAGE = "Mpack version can't be empty or null";

    @org.junit.Rule
    public org.junit.rules.ExpectedException expectedException = org.junit.rules.ExpectedException.none();

    @org.junit.Test
    public void testStackVersions() {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.2.0.0", "2.2.0.0", false));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.2.0.0-111", "2.2.0.0-999", false));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.0", "2.2.0.1"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.0", "2.2.0.10"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.2.0.20", "2.2.2.145"));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.1", "2.2.0.0"));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.10", "2.2.0.0"));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.2.145", "2.2.2.20"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.0-200", "2.2.0.1-100"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.0-101", "2.2.0.10-20"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.2.0.20-996", "2.2.2.145-846"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2", "2.2.VER"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.VAR", "2.2.VER"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.3", "2.2.3.VER1.V"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.2.0.1-200", "2.2.0.1-100"));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersionsWithBuild("2.2.0.1-200", "2.2.0.1-100", 4));
    }

    @org.junit.Test
    public void testVersionCompareSuccess() {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3", "1.2.3", false));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3", "1.2.3", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", "", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, null, true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, "1.2.3", false));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, "", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, null, true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3.1", "1.2.3", false));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.1.3", "1.2.3", false));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3.1", "1.2.3", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.1.3", "1.2.3", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", "1.2.3", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", null, true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, "", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, "1.2.3", true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.4"));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4", "1.2.3"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.3"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.4", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4", "1.2.3", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.3", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.9", "1.2.4.6", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.4.6", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2", "1.2.4.6", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4.8", "1.2.3.6.7", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.3.4", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7", "1.2.3.4", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7", "1.2.3.4", 4));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.3.0", 4));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3", "1.2.3.1", 4));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7\n", "1.2.3.4\n", 4));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.1", "1.2.3", true));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.1.3", "1.2.3", true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("", "1.2.3", true));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("", null, true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions(null, "", true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions(null, "1.2.3", true));
    }

    @org.junit.Test
    public void testVersionCompareSuccessCustomVersion() {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", false));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", "", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, null, true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, "1.2.3_MYAMBARI_000000", false));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, "", true));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION, null, true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3.1_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", false));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.1.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", false));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3.1_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("2.1.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", null, true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, "", true));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4_MYAMBARI_000000", "1.2.3_MYAMBARI_000000"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000"));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.99.99.0", "2.99.99"));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.4_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.9_MYAMBARI_000000", "1.2.4.6_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.4.6_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2_MYAMBARI_000000", "1.2.4.6_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.4.8_MYAMBARI_000000", "1.2.3.6.7_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.3.4_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7_MYAMBARI_000000", "1.2.3.4_MYAMBARI_000000", 3));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7_MYAMBARI_000000", "1.2.3.4_MYAMBARI_000000", 4));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.3.0_MYAMBARI_000000", 4));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3_MYAMBARI_000000", "1.2.3.1_MYAMBARI_000000", 4));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.6.7_MYAMBARI_000000\n", "1.2.3.4_MYAMBARI_000000\n", 4));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("1.2.3.1_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("2.1.3_MYAMBARI_000000", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions("", "1.2.3_MYAMBARI_000000", true));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.VersionUtils.compareVersions("", null, true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions(null, "", true));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.VersionUtils.compareVersions(null, "1.2.3_MYAMBARI_000000", true));
    }

    @org.junit.Test
    public void testVersionCompareError() {
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage("version2 cannot be empty");
        org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("1.2.3", "", false);
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage("version1 cannot be null");
        org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(null, "", false);
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage("version2 cannot be null");
        org.apache.ambari.server.utils.VersionUtils.areVersionsEqual("", null, false);
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage("version1 cannot be empty");
        org.apache.ambari.server.utils.VersionUtils.compareVersions("", "1", 2);
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage("maxLengthToCompare cannot be less than 0");
        org.apache.ambari.server.utils.VersionUtils.compareVersions("2", "1", -1);
    }

    @org.junit.Test
    public void testMpackVersionWithNotValidVersions() {
        java.lang.String errMessage = null;
        try {
            org.apache.ambari.server.utils.MpackVersion.parse(null);
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.MPACK_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.MpackVersion.parse("");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.MPACK_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.MpackVersion.parse("1.2.3.4-b10");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals("Wrong format for mpack version, should be N.N.N-bN or N.N.N-hN-bN", errMessage);
    }

    @org.junit.Test
    public void testStackVersionWithNotValidVersions() {
        java.lang.String errMessage = null;
        try {
            errMessage = null;
            org.apache.ambari.server.utils.MpackVersion.parseStackVersion(null);
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.STACK_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.MpackVersion.parseStackVersion("");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.STACK_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3-10");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals("Wrong format for stack version, should be N.N.N.N-N or N.N.N-hN-bN", errMessage);
    }

    @org.junit.Test
    public void testModuleVersionWithNotValidVersions() {
        java.lang.String errMessage = null;
        try {
            errMessage = null;
            org.apache.ambari.server.utils.ModuleVersion.parse(null);
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.MODULE_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.ModuleVersion.parse("");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals(org.apache.ambari.server.utils.TestVersionUtils.MODULE_ERR_MESSAGE, errMessage);
        try {
            errMessage = null;
            org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3-10");
        } catch (java.lang.IllegalArgumentException e) {
            errMessage = e.getMessage();
        }
        junit.framework.Assert.assertEquals("Wrong format for module version, should be N.N.N.N-bN or N.N.N-hN-bN", errMessage);
    }

    @org.junit.Test
    public void testMpackVersionWithValidVersions() {
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("2.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.3.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.4-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b1000").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b888")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.4-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.3.3-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("2.2.3-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h1-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h0-b11")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-b0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-b0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b0", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("2", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.1", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.0.1", false).compareTo(org.apache.ambari.server.utils.MpackVersion.parse("1.0.0-h0-b111")));
    }

    @org.junit.Test
    public void testStackVersionWithValidVersions() {
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.4-h1-b1").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("1.3.3-h1-b1").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parse("2.2.3-h1-b1").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h4-b888").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.11-888")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.4.1-1")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.3.3.1-1")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parse("1.2.3-h10-b10").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("2.2.3.1-1")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-999").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-999").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-999")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.1-999").compareTo(org.apache.ambari.server.utils.MpackVersion.parseStackVersion("1.2.3.4-888")));
    }

    @org.junit.Test
    public void testModuleVersionWithValidVersions() {
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h10-b888").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b888")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.5-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.4.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("1.3.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("2.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b11").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10")));
        junit.framework.Assert.assertEquals(0, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-b888")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.5-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.2.4.4-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("1.3.3.4-b10")));
        junit.framework.Assert.assertEquals(-1, org.apache.ambari.server.utils.ModuleVersion.parse("1.2.3.4-h0-b10").compareTo(org.apache.ambari.server.utils.ModuleVersion.parse("2.2.3.4-b10")));
    }
}