package org.apache.ambari.swagger;
@javax.ws.rs.Path("/toplevel2")
@io.swagger.annotations.Api(value = "Top Level 2", description = "Another top level API")
abstract class AnotherTopLevelAPI {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/anotherTop")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();

    @javax.ws.rs.Path("{param}/anotherNested")
    public abstract org.apache.ambari.swagger.NestedAPI getSecondNested(@io.swagger.annotations.ApiParam
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

    @javax.ws.rs.Path("{param}/nestedWithBadPreferredParent")
    public abstract org.apache.ambari.swagger.NestedWithBadPreferredParentAPI getNestedWithBadPreferredParent(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("param")
    java.lang.String param);
}