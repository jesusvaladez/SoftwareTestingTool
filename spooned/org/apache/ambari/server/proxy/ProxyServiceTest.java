package org.apache.ambari.server.proxy;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.spi.factory.ResponseImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.proxy.ProxyServiceTest.class, org.apache.ambari.server.proxy.ProxyService.class, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class, java.net.URI.class })
public class ProxyServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @org.junit.Test
    public void testProxyGetRequest() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        java.io.InputStream is = new java.io.ByteArrayInputStream("test".getBytes());
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(streamProviderMock.processURL("testurl", "GET", ((java.io.InputStream) (null)), headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(is);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(javax.ws.rs.core.Response.status(200)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(is)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("text/plain")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, responseBuilderMock, uriMock, java.net.URI.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForGetRequest = ps.processGetRequestForwarding(getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForGetRequest, responseMock);
    }

    @org.junit.Test
    public void testProxyPostRequest() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        java.io.InputStream is = new java.io.ByteArrayInputStream("test".getBytes());
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(getHttpHeaders().getMediaType()).andReturn(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        EasyMock.expect(streamProviderMock.processURL("testurl", "POST", is, headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(is);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(javax.ws.rs.core.Response.status(200)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(is)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("text/plain")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, responseBuilderMock, uriMock, java.net.URI.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForPostRequest = ps.processPostRequestForwarding(is, getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForPostRequest, responseMock);
    }

    @org.junit.Test
    public void testProxyPutRequest() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        java.io.InputStream is = new java.io.ByteArrayInputStream("test".getBytes());
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(getHttpHeaders().getMediaType()).andReturn(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        EasyMock.expect(streamProviderMock.processURL("testurl", "PUT", is, headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(is);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(javax.ws.rs.core.Response.status(200)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(is)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("text/plain")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, responseBuilderMock, uriMock, java.net.URI.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForPutRequest = ps.processPutRequestForwarding(is, getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForPutRequest, responseMock);
    }

    @org.junit.Test
    public void testProxyDeleteRequest() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        java.io.InputStream is = new java.io.ByteArrayInputStream("test".getBytes());
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(streamProviderMock.processURL("testurl", "DELETE", ((java.io.InputStream) (null)), headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(is);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(javax.ws.rs.core.Response.status(200)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(is)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("text/plain")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, responseBuilderMock, uriMock, java.net.URI.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForDeleteRequest = ps.processDeleteRequestForwarding(getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForDeleteRequest, responseMock);
    }

    @org.junit.Test
    public void testResponseWithError() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        java.io.InputStream es = new java.io.ByteArrayInputStream("error".getBytes());
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(streamProviderMock.processURL("testurl", "GET", ((java.io.InputStream) (null)), headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(400).times(2);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getErrorStream()).andReturn(es);
        EasyMock.expect(javax.ws.rs.core.Response.status(400)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(es)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("text/plain")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, uriMock, java.net.URI.class, javax.ws.rs.core.Response.class, responseBuilderMock);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForErrorRequest = ps.processGetRequestForwarding(getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForErrorRequest, responseMock);
    }

    @org.junit.Test
    public void testProxyWithJSONResponse() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uriMock = org.powermock.api.easymock.PowerMock.createMock(java.net.URI.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        javax.ws.rs.core.Response.ResponseBuilder responseBuilderMock = org.powermock.api.easymock.PowerMock.createMock(com.sun.jersey.core.spi.factory.ResponseBuilderImpl.class);
        javax.ws.rs.core.Response responseMock = EasyMock.createMock(com.sun.jersey.core.spi.factory.ResponseImpl.class);
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        java.util.Map map = new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(new java.io.ByteArrayInputStream("{ \"test\":\"test\" }".getBytes())), java.util.Map.class);
        org.powermock.api.easymock.PowerMock.mockStatic(javax.ws.rs.core.Response.class);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uriMock);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(uriMock.getQuery()).andReturn("url=testurl");
        EasyMock.expect(streamProviderMock.processURL("testurl", "GET", ((java.io.InputStream) (null)), headerParamsToForward)).andReturn(urlConnectionMock);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("application/json");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(new java.io.ByteArrayInputStream("{ \"test\":\"test\" }".getBytes()));
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(javax.ws.rs.core.Response.status(200)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.entity(map)).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.type("application/json")).andReturn(responseBuilderMock);
        EasyMock.expect(responseBuilderMock.build()).andReturn(responseMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class, javax.ws.rs.core.Response.class, responseBuilderMock, uriMock, java.net.URI.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        javax.ws.rs.core.Response resultForGetRequest = ps.processGetRequestForwarding(getHttpHeaders(), getUriInfo());
        org.junit.Assert.assertSame(resultForGetRequest, responseMock);
    }

    @org.junit.Test
    public void testEscapedURL() throws java.lang.Exception {
        org.apache.ambari.server.proxy.ProxyService ps = new org.apache.ambari.server.proxy.ProxyService();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProviderMock = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> headerParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        java.net.HttpURLConnection urlConnectionMock = EasyMock.createMock(java.net.HttpURLConnection.class);
        java.net.URI uri = javax.ws.rs.core.UriBuilder.fromUri("http://dev01.hortonworks.com:8080/proxy?url=http%3a%2f%2fserver%3a8188%2fws%2fv1%2f" + "timeline%2fHIVE_QUERY_ID%3ffields=events%2cprimaryfilters%26limit=10%26primaryFilter=user%3ahiveuser1").build();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        java.io.InputStream is = new java.io.ByteArrayInputStream("test".getBytes());
        java.util.List<java.lang.String> userRemoteParams = new java.util.LinkedList<>();
        userRemoteParams.add("testuser");
        headerParams.add("AmbariProxy-User-Remote", "testuser");
        headerParams.add("Content-Type", "testtype");
        headerParamsToForward.put("User-Remote", userRemoteParams);
        EasyMock.expect(getHttpHeaders().getRequestHeaders()).andReturn(headerParams);
        EasyMock.expect(getHttpHeaders().getRequestHeader("AmbariProxy-User-Remote")).andReturn(userRemoteParams);
        EasyMock.expect(getUriInfo().getRequestUri()).andReturn(uri);
        EasyMock.expect(getUriInfo().getQueryParameters()).andReturn(queryParams);
        EasyMock.expect(urlConnectionMock.getResponseCode()).andReturn(200);
        EasyMock.expect(urlConnectionMock.getContentType()).andReturn("text/plain");
        EasyMock.expect(urlConnectionMock.getInputStream()).andReturn(is);
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.controller.internal.URLStreamProvider.class, 20000, 15000, null, null, null).andReturn(streamProviderMock);
        EasyMock.expect(streamProviderMock.processURL("http://server:8188/ws/v1/timeline/HIVE_QUERY_ID?fields=events,primary" + "filters&limit=10&primaryFilter=user:hiveuser1", "GET", ((java.io.InputStream) (null)), headerParamsToForward)).andReturn(urlConnectionMock);
        org.powermock.api.easymock.PowerMock.replay(streamProviderMock, org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        EasyMock.replay(getUriInfo(), urlConnectionMock, getHttpHeaders());
        ps.processGetRequestForwarding(getHttpHeaders(), getUriInfo());
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        return java.util.Collections.emptyList();
    }
}