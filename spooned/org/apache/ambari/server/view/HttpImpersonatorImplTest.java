package org.apache.ambari.server.view;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class HttpImpersonatorImplTest {
    @org.junit.Test
    public void testRequestURL() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        org.apache.ambari.view.ViewContext viewContext = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        java.lang.String responseBody = "Response body...";
        java.io.InputStream inputStream = new java.io.ByteArrayInputStream(responseBody.getBytes(java.nio.charset.Charset.forName("UTF-8")));
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("spec?doAs=joe"), EasyMock.eq("requestMethod"), EasyMock.eq(((java.lang.String) (null))), EasyMock.eq(((java.util.Map<java.lang.String, java.util.List<java.lang.String>>) (null))))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.expect(viewContext.getUsername()).andReturn("joe").anyTimes();
        EasyMock.replay(streamProvider, urlConnection, viewContext);
        org.apache.ambari.server.view.HttpImpersonatorImpl impersonator = new org.apache.ambari.server.view.HttpImpersonatorImpl(viewContext, streamProvider);
        org.apache.ambari.view.ImpersonatorSetting setting = new org.apache.ambari.server.view.ImpersonatorSettingImpl(viewContext);
        org.junit.Assert.assertEquals(responseBody, impersonator.requestURL("spec", "requestMethod", setting));
        EasyMock.verify(streamProvider, urlConnection, viewContext);
    }

    @org.junit.Test
    public void testRequestURLWithCustom() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        org.apache.ambari.view.ViewContext viewContext = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        java.lang.String responseBody = "Response body...";
        java.io.InputStream inputStream = new java.io.ByteArrayInputStream(responseBody.getBytes(java.nio.charset.Charset.forName("UTF-8")));
        EasyMock.expect(streamProvider.processURL(EasyMock.eq("spec?impersonate=hive"), EasyMock.eq("requestMethod"), EasyMock.eq(((java.lang.String) (null))), EasyMock.eq(((java.util.Map<java.lang.String, java.util.List<java.lang.String>>) (null))))).andReturn(urlConnection);
        EasyMock.expect(urlConnection.getInputStream()).andReturn(inputStream);
        EasyMock.replay(streamProvider, urlConnection);
        org.apache.ambari.server.view.HttpImpersonatorImpl impersonator = new org.apache.ambari.server.view.HttpImpersonatorImpl(viewContext, streamProvider);
        org.apache.ambari.view.ImpersonatorSetting setting = new org.apache.ambari.server.view.ImpersonatorSettingImpl("hive", "impersonate");
        org.junit.Assert.assertEquals(responseBody, impersonator.requestURL("spec", "requestMethod", setting));
        EasyMock.verify(streamProvider, urlConnection);
    }
}