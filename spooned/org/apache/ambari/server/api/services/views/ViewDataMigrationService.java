package org.apache.ambari.server.api.services.views;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
@javax.ws.rs.Path("/views/{viewName}/versions/{version}/instances/{instanceName}/migrate")
@io.swagger.annotations.Api(tags = "Views", description = "Endpoint for view specific operations")
public class ViewDataMigrationService extends org.apache.ambari.server.api.services.BaseService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.views.ViewDataMigrationService.class);

    private org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();

    private org.apache.ambari.server.view.ViewDataMigrationUtility viewDataMigrationUtility;

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{originVersion}/{originInstanceName}")
    @io.swagger.annotations.ApiOperation(value = "Migrate view instance data", notes = "Migrates view instance persistence data from origin view instance specified in the path params.")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response migrateData(@io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String viewVersion, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName, @io.swagger.annotations.ApiParam("origin version")
    @javax.ws.rs.PathParam("originVersion")
    java.lang.String originViewVersion, @io.swagger.annotations.ApiParam("origin instance name")
    @javax.ws.rs.PathParam("originInstanceName")
    java.lang.String originInstanceName) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        if (!viewRegistry.checkAdmin()) {
            throw new javax.ws.rs.WebApplicationException(Response.Status.FORBIDDEN);
        }
        org.apache.ambari.server.api.services.views.ViewDataMigrationService.LOG.info((((((((((("Data Migration to view instance " + viewName) + "/") + viewVersion) + "/") + instanceName) + " from ") + viewName) + "/") + originViewVersion) + "/") + originInstanceName);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition = viewRegistry.getInstanceDefinition(viewName, viewVersion, instanceName);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity originInstanceDefinition = viewRegistry.getInstanceDefinition(viewName, originViewVersion, originInstanceName);
        getViewDataMigrationUtility().migrateData(instanceDefinition, originInstanceDefinition, false);
        javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.status(Response.Status.OK);
        return builder.build();
    }

    protected org.apache.ambari.server.view.ViewDataMigrationUtility getViewDataMigrationUtility() {
        if (viewDataMigrationUtility == null) {
            viewDataMigrationUtility = new org.apache.ambari.server.view.ViewDataMigrationUtility(viewRegistry);
        }
        return viewDataMigrationUtility;
    }

    protected void setViewDataMigrationUtility(org.apache.ambari.server.view.ViewDataMigrationUtility viewDataMigrationUtility) {
        this.viewDataMigrationUtility = viewDataMigrationUtility;
    }
}