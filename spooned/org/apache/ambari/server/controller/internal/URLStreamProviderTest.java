package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class URLStreamProviderTest {
    @org.junit.Test
    public void testProcessURL() throws java.lang.Exception {
        java.net.HttpURLConnection connection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        org.apache.ambari.server.controller.internal.AppCookieManager appCookieManager = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.AppCookieManager.class);
        org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider = EasyMock.createMockBuilder(org.apache.ambari.server.controller.internal.URLStreamProvider.class).withConstructor(java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class, java.lang.String.class).withArgs(1000, 1000, "path", "password", "type").addMockedMethod("getAppCookieManager").addMockedMethod("getConnection", java.net.URL.class).createMock();
        java.lang.String fakeURL = "http://fakehost";
        java.net.URL url = new java.net.URL(fakeURL);
        EasyMock.expect(urlStreamProvider.getAppCookieManager()).andReturn(appCookieManager).anyTimes();
        EasyMock.expect(urlStreamProvider.getConnection(url)).andReturn(connection);
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("Header1", java.util.Collections.singletonList("value"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar"));
        EasyMock.expect(appCookieManager.getCachedAppCookie(fakeURL)).andReturn("APPCOOKIE=abcdef");
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Header1", "value");
        connection.setRequestProperty("Cookie", "FOO=bar; APPCOOKIE=abcdef");
        EasyMock.replay(urlStreamProvider, connection, appCookieManager);
        org.junit.Assert.assertEquals(connection, urlStreamProvider.processURL(fakeURL, "GET", ((java.lang.String) (null)), headerMap));
        EasyMock.verify(urlStreamProvider, connection, appCookieManager);
    }

    @org.junit.Test
    public void testProcessURL_securityNotSetup() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider = EasyMock.createMockBuilder(org.apache.ambari.server.controller.internal.URLStreamProvider.class).withConstructor(java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class, java.lang.String.class, java.lang.String.class).withArgs(1000, 1000, null, null, null).addMockedMethod("getAppCookieManager").createMock();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("Header1", java.util.Collections.singletonList("value"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar"));
        EasyMock.replay(urlStreamProvider);
        try {
            urlStreamProvider.processURL("https://spec", "GET", ((java.lang.String) (null)), headerMap);
            org.junit.Assert.fail("Expected IllegalStateException");
        } catch (java.lang.IllegalStateException e) {
        }
        EasyMock.verify(urlStreamProvider);
    }

    @org.junit.Test
    public void testAppendCookie() throws java.lang.Exception {
        org.junit.Assert.assertEquals("newCookie", org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie(null, "newCookie"));
        org.junit.Assert.assertEquals("newCookie", org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie("", "newCookie"));
        org.junit.Assert.assertEquals("oldCookie; newCookie", org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie("oldCookie", "newCookie"));
        org.junit.Assert.assertEquals("oldCookie1; oldCookie2; newCookie", org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie("oldCookie1; oldCookie2", "newCookie"));
    }
}