package org.apache.ambari.swagger;
@io.swagger.annotations.Api(value = "Nested", description = "A nested API")
abstract class NestedAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}