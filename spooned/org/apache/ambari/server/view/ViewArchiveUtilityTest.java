package org.apache.ambari.server.view;
public class ViewArchiveUtilityTest {
    @org.junit.Test
    public void testValidateConfig() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewArchiveUtility utility = new org.apache.ambari.server.view.ViewArchiveUtility();
        java.io.InputStream configStream = getClass().getClassLoader().getResourceAsStream("test_view.xml");
        utility.validateConfig(configStream);
    }
}