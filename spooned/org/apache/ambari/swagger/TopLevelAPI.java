package org.apache.ambari.swagger;
@javax.ws.rs.Path("/toplevel")
@io.swagger.annotations.Api(value = "Top Level", description = "A top level API")
abstract class TopLevelAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/top")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();

    @javax.ws.rs.Path("{param}/nested")
    public abstract org.apache.ambari.swagger.NestedAPI getNested(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);

    @javax.ws.rs.Path("{param}/nestedWithPreferredParent")
    public abstract org.apache.ambari.swagger.NestedWithPreferredParentAPI getNestedWithPreferredParent(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);

    @javax.ws.rs.Path("{param}/nestedWithSamePreferredParent")
    public abstract org.apache.ambari.swagger.NestedWithSamePreferredParentAPI getNestedWithSamePreferredParent(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);
}