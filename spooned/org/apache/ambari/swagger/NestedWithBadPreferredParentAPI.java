package org.apache.ambari.swagger;
@io.swagger.annotations.Api(value = "BadNested", description = "A nested API")
@org.apache.ambari.annotations.SwaggerPreferredParent(preferredParent = org.apache.ambari.swagger.YetAnotherTopLevelAPI.class)
abstract class NestedWithBadPreferredParentAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}