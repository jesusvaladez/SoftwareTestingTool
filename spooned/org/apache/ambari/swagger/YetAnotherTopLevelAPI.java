package org.apache.ambari.swagger;
@javax.ws.rs.Path("/toplevel3")
@io.swagger.annotations.Api(value = "Top Level 3", description = "Yet another top level API")
abstract class YetAnotherTopLevelAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/yetAnotherTop")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();

    @javax.ws.rs.Path("{param}/nested")
    public abstract org.apache.ambari.swagger.NestedAPI getFirstNested(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);
}