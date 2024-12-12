package org.apache.ambari.server.view;
import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewAmbariStreamProviderTest {
    @org.junit.Test
    public void testReadFrom() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        java.io.InputStream inputStream = EasyMock.createNiceMock(java.io.InputStream.class);
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<>();
        headers.put("header", "headerValue");
        headers.put("Cookie", "FOO=bar");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("header", java.util.Collections.singletonList("headerValue"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar; AMBARISESSIONID=abcdefg"));
        EasyMock.expect(sessionManager.getCurrentSessionId()).andReturn("abcdefg");
        EasyMock.expect(sessionManager.getSessionCookie()).andReturn("AMBARISESSIONID");
        EasyMock.expect(controller.getAmbariServerURI("/spec")).andReturn("http://c6401.ambari.apache.org:8080/spec");
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("http://c6401.ambari.apache.org:8080/spec"), EasyMock.eq("requestMethod"), EasyMock.aryEq("params".getBytes()), EasyMock.eq(headerMap))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.replay(streamProvider, sessionManager, controller, urlConnection, inputStream);
        org.apache.ambari.server.view.ViewAmbariStreamProvider viewAmbariStreamProvider = new org.apache.ambari.server.view.ViewAmbariStreamProvider(streamProvider, sessionManager, controller);
        org.junit.Assert.assertEquals(inputStream, viewAmbariStreamProvider.readFrom("spec", "requestMethod", "params", headers));
        EasyMock.verify(streamProvider, sessionManager, urlConnection, inputStream);
    }

    @org.junit.Test
    public void testReadFromNew() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        java.io.InputStream inputStream = EasyMock.createNiceMock(java.io.InputStream.class);
        java.io.InputStream body = new java.io.ByteArrayInputStream("params".getBytes());
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<>();
        headers.put("header", "headerValue");
        headers.put("Cookie", "FOO=bar");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("header", java.util.Collections.singletonList("headerValue"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar; AMBARISESSIONID=abcdefg"));
        EasyMock.expect(sessionManager.getCurrentSessionId()).andReturn("abcdefg");
        EasyMock.expect(sessionManager.getSessionCookie()).andReturn("AMBARISESSIONID");
        EasyMock.expect(controller.getAmbariServerURI("/spec")).andReturn("http://c6401.ambari.apache.org:8080/spec");
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("http://c6401.ambari.apache.org:8080/spec"), EasyMock.eq("requestMethod"), EasyMock.aryEq("params".getBytes()), EasyMock.eq(headerMap))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.replay(streamProvider, sessionManager, controller, urlConnection, inputStream);
        org.apache.ambari.server.view.ViewAmbariStreamProvider viewAmbariStreamProvider = new org.apache.ambari.server.view.ViewAmbariStreamProvider(streamProvider, sessionManager, controller);
        org.junit.Assert.assertEquals(inputStream, viewAmbariStreamProvider.readFrom("spec", "requestMethod", body, headers));
        EasyMock.verify(streamProvider, sessionManager, urlConnection, inputStream);
    }

    @org.junit.Test
    public void testReadFromNullStringBody() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        java.io.InputStream inputStream = EasyMock.createNiceMock(java.io.InputStream.class);
        java.lang.String body = null;
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<>();
        headers.put("header", "headerValue");
        headers.put("Cookie", "FOO=bar");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("header", java.util.Collections.singletonList("headerValue"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar; AMBARISESSIONID=abcdefg"));
        EasyMock.expect(sessionManager.getCurrentSessionId()).andReturn("abcdefg");
        EasyMock.expect(sessionManager.getSessionCookie()).andReturn("AMBARISESSIONID");
        EasyMock.expect(controller.getAmbariServerURI("/spec")).andReturn("http://c6401.ambari.apache.org:8080/spec");
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("http://c6401.ambari.apache.org:8080/spec"), EasyMock.eq("requestMethod"), EasyMock.aryEq(((byte[]) (null))), EasyMock.eq(headerMap))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.replay(streamProvider, sessionManager, controller, urlConnection, inputStream);
        org.apache.ambari.server.view.ViewAmbariStreamProvider viewAmbariStreamProvider = new org.apache.ambari.server.view.ViewAmbariStreamProvider(streamProvider, sessionManager, controller);
        org.junit.Assert.assertEquals(inputStream, viewAmbariStreamProvider.readFrom("spec", "requestMethod", body, headers));
        EasyMock.verify(streamProvider, sessionManager, urlConnection, inputStream);
    }

    @org.junit.Test
    public void testReadFromNullInputStreamBody() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        java.io.InputStream inputStream = EasyMock.createNiceMock(java.io.InputStream.class);
        java.io.InputStream body = null;
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<>();
        headers.put("header", "headerValue");
        headers.put("Cookie", "FOO=bar");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        headerMap.put("header", java.util.Collections.singletonList("headerValue"));
        headerMap.put("Cookie", java.util.Collections.singletonList("FOO=bar; AMBARISESSIONID=abcdefg"));
        EasyMock.expect(sessionManager.getCurrentSessionId()).andReturn("abcdefg");
        EasyMock.expect(sessionManager.getSessionCookie()).andReturn("AMBARISESSIONID");
        EasyMock.expect(controller.getAmbariServerURI("/spec")).andReturn("http://c6401.ambari.apache.org:8080/spec");
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("http://c6401.ambari.apache.org:8080/spec"), EasyMock.eq("requestMethod"), EasyMock.aryEq(((byte[]) (null))), EasyMock.eq(headerMap))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.replay(streamProvider, sessionManager, controller, urlConnection, inputStream);
        org.apache.ambari.server.view.ViewAmbariStreamProvider viewAmbariStreamProvider = new org.apache.ambari.server.view.ViewAmbariStreamProvider(streamProvider, sessionManager, controller);
        org.junit.Assert.assertEquals(inputStream, viewAmbariStreamProvider.readFrom("spec", "requestMethod", body, headers));
        EasyMock.verify(streamProvider, sessionManager, urlConnection, inputStream);
    }
}