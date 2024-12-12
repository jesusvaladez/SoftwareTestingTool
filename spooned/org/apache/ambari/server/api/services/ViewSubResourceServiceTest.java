package org.apache.ambari.server.api.services;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewSubResourceServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("subResource");
        org.apache.ambari.server.api.services.views.ViewSubResourceService service = new org.apache.ambari.server.api.services.ViewSubResourceServiceTest.TestViewSubResourceService(type, viewInstanceEntity);
        java.lang.reflect.Method m = service.getClass().getMethod("getSubResource1", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ViewSubResourceServiceTest.TestViewSubResourceService(type, viewInstanceEntity);
        m = service.getClass().getMethod("getSubResource2", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ViewSubResourceServiceTest.TestViewSubResourceService(type, viewInstanceEntity);
        m = service.getClass().getMethod("postSubResource", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ViewSubResourceServiceTest.TestViewSubResourceService(type, viewInstanceEntity);
        m = service.getClass().getMethod("putSubResource", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ViewSubResourceServiceTest.TestViewSubResourceService(type, viewInstanceEntity);
        m = service.getClass().getMethod("deleteSubResource", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, null));
        return listInvocations;
    }

    @org.junit.Test
    public void testGetResultSerializer_Text() throws java.lang.Exception {
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child = tree.addChild(resource, "resource1");
        child.setProperty("href", "this is an href");
        java.util.Map<java.lang.String, java.lang.Object> mapRootProps = new java.util.LinkedHashMap<>();
        mapRootProps.put("prop2", "value2");
        mapRootProps.put("prop1", "value1");
        java.util.Map<java.lang.String, java.lang.Object> mapCategoryProps = new java.util.LinkedHashMap<>();
        mapCategoryProps.put("catProp1", "catValue1");
        mapCategoryProps.put("catProp2", "catValue2");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = new java.util.LinkedHashMap<>();
        propertyMap.put(null, mapRootProps);
        propertyMap.put("category", mapCategoryProps);
        EasyMock.expect(resource.getPropertiesMap()).andReturn(propertyMap).anyTimes();
        EasyMock.expect(resource.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Cluster).anyTimes();
        EasyMock.replay(uriInfo, resource);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("subResource");
        org.apache.ambari.server.api.services.views.ViewSubResourceService service = new org.apache.ambari.server.api.services.views.ViewSubResourceService(type, viewInstanceEntity);
        org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = service.getResultSerializer(MediaType.TEXT_PLAIN_TYPE);
        java.lang.Object o = serializer.serialize(result);
        java.lang.String expected = "{\n" + ((((((("  \"href\" : \"this is an href\",\n" + "  \"prop2\" : \"value2\",\n") + "  \"prop1\" : \"value1\",\n") + "  \"category\" : {\n") + "    \"catProp1\" : \"catValue1\",\n") + "    \"catProp2\" : \"catValue2\"\n") + "  }\n") + "}");
        org.junit.Assert.assertEquals(expected, o.toString().replace("\r", ""));
        EasyMock.verify(uriInfo, resource);
    }

    @org.junit.Test
    public void testGetResultSerializer_Json() throws java.lang.Exception {
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child = tree.addChild(resource, "resource1");
        child.setProperty("href", "this is an href");
        java.util.HashMap<java.lang.String, java.lang.Object> mapRootProps = new java.util.HashMap<>();
        mapRootProps.put("prop1", "value1");
        mapRootProps.put("prop2", "value2");
        java.util.HashMap<java.lang.String, java.lang.Object> mapCategoryProps = new java.util.HashMap<>();
        mapCategoryProps.put("catProp1", "catValue1");
        mapCategoryProps.put("catProp2", "catValue2");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = new java.util.HashMap<>();
        propertyMap.put(null, mapRootProps);
        propertyMap.put("category", mapCategoryProps);
        EasyMock.expect(resource.getPropertiesMap()).andReturn(propertyMap).anyTimes();
        EasyMock.expect(resource.getType()).andReturn(org.apache.ambari.server.controller.spi.Resource.Type.Cluster).anyTimes();
        EasyMock.replay(uriInfo, resource);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("subResource");
        org.apache.ambari.server.api.services.views.ViewSubResourceService service = new org.apache.ambari.server.api.services.views.ViewSubResourceService(type, viewInstanceEntity);
        org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = service.getResultSerializer(MediaType.APPLICATION_JSON_TYPE);
        java.lang.Object o = serializer.serialize(result);
        org.junit.Assert.assertTrue(o instanceof java.util.Map);
        java.util.Map map = ((java.util.Map) (o));
        org.junit.Assert.assertEquals(4, map.size());
        org.junit.Assert.assertEquals("value1", map.get("prop1"));
        org.junit.Assert.assertEquals("value2", map.get("prop2"));
        org.junit.Assert.assertEquals("this is an href", map.get("href"));
        java.lang.Object o2 = map.get("category");
        org.junit.Assert.assertNotNull(o2);
        org.junit.Assert.assertTrue(o2 instanceof java.util.Map);
        java.util.Map subMap = ((java.util.Map) (o2));
        org.junit.Assert.assertEquals(2, subMap.size());
        org.junit.Assert.assertEquals("catValue1", subMap.get("catProp1"));
        org.junit.Assert.assertEquals("catValue2", subMap.get("catProp2"));
        EasyMock.verify(uriInfo, resource);
    }

    private class TestViewSubResourceService extends org.apache.ambari.server.api.services.views.ViewSubResourceService {
        public TestViewSubResourceService(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
            super(type, viewInstanceDefinition);
        }

        public javax.ws.rs.core.Response getSubResource1(@javax.ws.rs.core.Context
        javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
        javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("resourceId")
        java.lang.String resourceId) {
            return handleRequest(headers, ui, resourceId);
        }

        public javax.ws.rs.core.Response getSubResource2(@javax.ws.rs.core.Context
        javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
        javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("resourceId")
        java.lang.String resourceId) {
            return handleRequest(headers, ui, org.apache.ambari.view.ViewResourceHandler.RequestType.GET, org.apache.ambari.view.ViewResourceHandler.MediaType.TEXT_PLAIN, resourceId);
        }

        public javax.ws.rs.core.Response postSubResource(@javax.ws.rs.core.Context
        javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
        javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("resourceId")
        java.lang.String resourceId) {
            return handleRequest(headers, ui, org.apache.ambari.view.ViewResourceHandler.RequestType.POST, org.apache.ambari.view.ViewResourceHandler.MediaType.TEXT_PLAIN, resourceId);
        }

        public javax.ws.rs.core.Response putSubResource(@javax.ws.rs.core.Context
        javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
        javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("resourceId")
        java.lang.String resourceId) {
            return handleRequest(headers, ui, org.apache.ambari.view.ViewResourceHandler.RequestType.PUT, org.apache.ambari.view.ViewResourceHandler.MediaType.TEXT_PLAIN, resourceId);
        }

        public javax.ws.rs.core.Response deleteSubResource(@javax.ws.rs.core.Context
        javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
        javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("resourceId")
        java.lang.String resourceId) {
            return handleRequest(headers, ui, org.apache.ambari.view.ViewResourceHandler.RequestType.DELETE, org.apache.ambari.view.ViewResourceHandler.MediaType.TEXT_PLAIN, resourceId);
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String resourceId) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
            return getTestRequestFactory();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
            return getTestBodyParser();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
            return getTestResultSerializer();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer(javax.ws.rs.core.MediaType mediaType) {
            return getTestResultSerializer();
        }
    }
}