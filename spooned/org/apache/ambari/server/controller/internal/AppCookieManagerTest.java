package org.apache.ambari.server.controller.internal;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
public class AppCookieManagerTest {
    @org.junit.Test
    public void getCachedKnoxAppCookie() {
        org.junit.Assert.assertNull(new org.apache.ambari.server.controller.internal.AppCookieManager().getCachedAppCookie("http://dummy"));
    }

    @org.junit.Test
    public void getHadoopAuthCookieValueWithNullHeaders() {
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.AppCookieManager.getHadoopAuthCookieValue(null));
    }

    @org.junit.Test
    public void getHadoopAuthCookieValueWitEmptylHeaders() {
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.AppCookieManager.getHadoopAuthCookieValue(new org.apache.http.Header[0]));
    }

    @org.junit.Test
    public void getHadoopAuthCookieValueWithValidlHeaders() {
        org.apache.http.Header[] headers = new org.apache.http.Header[1];
        headers[0] = new org.apache.http.message.BasicHeader("Set-Cookie", org.apache.ambari.server.controller.internal.AppCookieManager.HADOOP_AUTH + "=dummyvalue");
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.internal.AppCookieManager.getHadoopAuthCookieValue(headers));
    }
}