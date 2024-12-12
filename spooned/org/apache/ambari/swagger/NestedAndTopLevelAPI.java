package org.apache.ambari.swagger;
@javax.ws.rs.Path("/canBeReachedFromTopToo")
@io.swagger.annotations.Api(value = "Nested and Top Level", description = "An API that is both nested and top level")
abstract class NestedAndTopLevelAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}