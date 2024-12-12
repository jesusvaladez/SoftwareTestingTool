package org.apache.ambari.swagger;
@javax.ws.rs.Path("/toplevel4")
@io.swagger.annotations.Api(value = "Top Level 4", description = "Yet another top level API")
abstract class TopLevel4API {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/top")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();

    @javax.ws.rs.Path("{param}/nested")
    public abstract org.apache.ambari.swagger.NestedWithOverwrite getNested(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);
}