package org.apache.ambari.server.checks;
public class HardcodedStackVersionPropertiesCheckTest {
    private static final java.lang.String currentVersion = "2.3.4.0-1234";

    @org.junit.Test
    public void testGetHardcodeSearchPattern() throws java.lang.Exception {
        java.util.regex.Pattern p = org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.getHardcodeSearchPattern(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheckTest.currentVersion);
        org.junit.Assert.assertEquals(p.pattern(), "(?<!-Dhdp\\.version=)2\\.3\\.4\\.0-1234");
    }

    @org.junit.Test
    public void testStringContainsVersionHardcode() throws java.lang.Exception {
        java.util.regex.Pattern pattern = org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.getHardcodeSearchPattern(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheckTest.currentVersion);
        java.lang.String content = "";
        org.junit.Assert.assertFalse(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
        content = "2.3.4.0-1234";
        org.junit.Assert.assertTrue(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
        content = "dfsdfds fdsfds -Dhdp.version=2.3.4.0-1234 sfdfdsfds";
        org.junit.Assert.assertFalse(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
        content = "dfsdfds fdsfds -Dhdp.version=2.3.4.0-1234 \n sfdfdsfds 2.3.4.0-1234 \n fdsfds";
        org.junit.Assert.assertTrue(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
        content = "hdp.version=2.3.4.0-1234";
        org.junit.Assert.assertTrue(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
        content = "kgflkfld fdf\nld;ls;f d hdp.version=2.3.4.0-1234 \n sfdfdsfds \n fdsfds";
        org.junit.Assert.assertTrue(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(content, pattern));
    }
}