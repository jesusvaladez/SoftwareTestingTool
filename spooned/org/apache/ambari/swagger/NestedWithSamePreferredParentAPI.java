package org.apache.ambari.swagger;
@io.swagger.annotations.Api(value = "SameNested", description = "A nested API")
@org.apache.ambari.annotations.SwaggerPreferredParent(preferredParent = org.apache.ambari.swagger.TopLevelAPI.class)
abstract class NestedWithSamePreferredParentAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}