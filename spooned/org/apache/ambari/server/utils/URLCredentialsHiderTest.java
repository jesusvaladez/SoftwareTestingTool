package org.apache.ambari.server.utils;
public class URLCredentialsHiderTest {
    @org.junit.Test
    public void testHideUserInfo() {
        java.lang.String testURL1 = "http://user01:pass@host:8443/api/v1";
        org.junit.Assert.assertEquals(java.lang.String.format("http://%s@host:8443/api/v1", org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_CREDENTIALS), org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(testURL1));
        java.lang.String testURL2 = "http://user01@host:8443/api/v1";
        org.junit.Assert.assertEquals(java.lang.String.format("http://%s@host:8443/api/v1", org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_USER), org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(testURL2));
        java.lang.String invalidURL = "htt://user01:pass@host:8443/api/v1";
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.URLCredentialsHider.INVALID_URL, org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(invalidURL));
        java.lang.String testURL3 = "http://***:***@host:8443/api/v1";
        org.junit.Assert.assertEquals(java.lang.String.format("http://%s@host:8443/api/v1", org.apache.ambari.server.utils.URLCredentialsHider.HIDDEN_CREDENTIALS), org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(testURL3));
    }
}