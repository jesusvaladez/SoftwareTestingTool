package org.apache.ambari.swagger;
@io.swagger.annotations.Api(value = "Nested", description = "A nested API")
@org.apache.ambari.annotations.SwaggerPreferredParent(preferredParent = org.apache.ambari.swagger.AnotherTopLevelAPI.class)
abstract class NestedWithPreferredParentAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}