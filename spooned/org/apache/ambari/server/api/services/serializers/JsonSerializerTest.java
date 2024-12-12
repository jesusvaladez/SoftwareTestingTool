package org.apache.ambari.server.api.services.serializers;
import javax.ws.rs.core.UriInfo;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class JsonSerializerTest {
    @org.junit.Test
    public void testSerialize() throws java.lang.Exception {
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
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.JsonSerializer().serialize(result).toString().replace("\r", "");
        java.lang.String expected = "{\n" + ((((((("  \"href\" : \"this is an href\",\n" + "  \"prop2\" : \"value2\",\n") + "  \"prop1\" : \"value1\",\n") + "  \"category\" : {\n") + "    \"catProp1\" : \"catValue1\",\n") + "    \"catProp2\" : \"catValue2\"\n") + "  }\n") + "}");
        org.junit.Assert.assertEquals(expected, o);
        EasyMock.verify(uriInfo, resource);
    }

    @org.junit.Test
    public void testSerializeResources() throws java.lang.Exception {
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resourcesNode = tree.addChild(null, "resources");
        resourcesNode.addChild(resource, "resource1");
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
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.JsonSerializer().serialize(result).toString().replace("\r", "");
        java.lang.String expected = "{\n" + (((((((((("  \"resources\" : [\n" + "    {\n") + "      \"prop2\" : \"value2\",\n") + "      \"prop1\" : \"value1\",\n") + "      \"category\" : {\n") + "        \"catProp1\" : \"catValue1\",\n") + "        \"catProp2\" : \"catValue2\"\n") + "      }\n") + "    }\n") + "  ]\n") + "}");
        org.junit.Assert.assertEquals(expected, o);
        EasyMock.verify(uriInfo, resource);
    }

    @org.junit.Test
    public void testSerializeResourcesAsArray() throws java.lang.Exception {
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child = tree.addChild(resource, "resource1");
        child.setProperty("href", "this is an href");
        tree.addChild(resource, "resource2");
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
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.JsonSerializer().serialize(result).toString().replace("\r", "");
        java.lang.String expected = "[\n" + ((((((((((((((((("  {\n" + "    \"href\" : \"this is an href\",\n") + "    \"prop2\" : \"value2\",\n") + "    \"prop1\" : \"value1\",\n") + "    \"category\" : {\n") + "      \"catProp1\" : \"catValue1\",\n") + "      \"catProp2\" : \"catValue2\"\n") + "    }\n") + "  },\n") + "  {\n") + "    \"prop2\" : \"value2\",\n") + "    \"prop1\" : \"value1\",\n") + "    \"category\" : {\n") + "      \"catProp1\" : \"catValue1\",\n") + "      \"catProp2\" : \"catValue2\"\n") + "    }\n") + "  }\n") + "]");
        org.junit.Assert.assertEquals(expected, o);
        EasyMock.verify(uriInfo, resource);
    }

    @org.junit.Test
    public void testDeleteResultMetadata() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.services.DeleteResultMetadata metadata = new org.apache.ambari.server.api.services.DeleteResultMetadata();
        metadata.addDeletedKey("key1");
        metadata.addException("key2", new org.apache.ambari.server.security.authorization.AuthorizationException());
        result.setResultMetadata(metadata);
        java.lang.String expected = "{\n" + (((((((((((((("  \"deleteResult\" : [\n" + "    {\n") + "      \"deleted\" : {\n") + "        \"key\" : \"key1\"\n") + "      }\n") + "    },\n") + "    {\n") + "      \"error\" : {\n") + "        \"key\" : \"key2\",\n") + "        \"code\" : 403,\n") + "        \"message\" : \"The authenticated user is not authorized to perform the requested operation\"\n") + "      }\n") + "    }\n") + "  ]\n") + "}");
        java.lang.String json = new org.apache.ambari.server.api.services.serializers.JsonSerializer().serialize(result).toString().replace("\r", "");
        org.junit.Assert.assertEquals(expected, json);
    }
}