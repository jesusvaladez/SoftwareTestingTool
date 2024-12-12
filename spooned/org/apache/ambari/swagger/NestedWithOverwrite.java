package org.apache.ambari.swagger;
@io.swagger.annotations.Api(value = "NestedWithOverWrite", description = "A nested API")
@org.apache.ambari.annotations.SwaggerOverwriteNestedAPI(parentApi = org.apache.ambari.swagger.YetAnotherTopLevelAPI.class, parentApiPath = "/toplevel3", parentMethodPath = "{foo}/bar", pathParameters = { "foo" })
abstract class NestedWithOverwrite {
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @io.swagger.annotations.ApiOperation("list")
    public abstract io.swagger.models.Response getList();
}