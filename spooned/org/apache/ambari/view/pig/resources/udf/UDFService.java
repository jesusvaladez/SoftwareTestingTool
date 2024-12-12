package org.apache.ambari.view.pig.resources.udf;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.simple.JSONObject;
public class UDFService extends org.apache.ambari.view.pig.services.BaseService {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewResourceHandler handler;

    protected org.apache.ambari.view.pig.resources.udf.UDFResourceManager resourceManager = null;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.udf.UDFService.class);

    protected synchronized org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.udf.models.UDF> getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new org.apache.ambari.view.pig.resources.udf.UDFResourceManager(context);
        }
        return resourceManager;
    }

    @org.apache.ambari.view.pig.resources.udf.GET
    @org.apache.ambari.view.pig.resources.udf.Path("{udfId}")
    @org.apache.ambari.view.pig.resources.udf.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.resources.udf.Response getUDF(@org.apache.ambari.view.pig.resources.udf.PathParam("udfId")
    java.lang.String udfId) {
        try {
            org.apache.ambari.view.pig.resources.udf.models.UDF udf = getResourceManager().read(udfId);
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("udf", udf);
            return Response.ok(object).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.pig.resources.udf.DELETE
    @org.apache.ambari.view.pig.resources.udf.Path("{udfId}")
    public org.apache.ambari.view.pig.resources.udf.Response deleteUDF(@org.apache.ambari.view.pig.resources.udf.PathParam("udfId")
    java.lang.String udfId) {
        try {
            getResourceManager().delete(udfId);
            return Response.status(204).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.pig.resources.udf.GET
    @org.apache.ambari.view.pig.resources.udf.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.resources.udf.Response getUDFList(@org.apache.ambari.view.pig.resources.udf.Context
    UriInfo ui) {
        try {
            org.apache.ambari.view.pig.resources.udf.UDFService.LOG.debug("Getting all UDFs");
            java.util.List allUDFs = getResourceManager().readAll(new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername()));
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("udfs", allUDFs);
            return Response.ok(object).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.pig.resources.udf.PUT
    @org.apache.ambari.view.pig.resources.udf.Path("{udfId}")
    @org.apache.ambari.view.pig.resources.udf.Consumes(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.resources.udf.Response updateUDF(org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest request, @org.apache.ambari.view.pig.resources.udf.PathParam("udfId")
    java.lang.String udfId) {
        try {
            getResourceManager().update(request.udf, udfId);
            return Response.status(204).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.pig.resources.udf.POST
    @org.apache.ambari.view.pig.resources.udf.Consumes(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.resources.udf.Response createUDF(org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest request, @org.apache.ambari.view.pig.resources.udf.Context
    javax.servlet.http.HttpServletResponse response, @org.apache.ambari.view.pig.resources.udf.Context
    UriInfo ui) {
        try {
            getResourceManager().create(request.udf);
            org.apache.ambari.view.pig.resources.udf.models.UDF udf = getResourceManager().read(request.udf.getId());
            response.setHeader("Location", java.lang.String.format("%s/%s", ui.getAbsolutePath().toString(), request.udf.getId()));
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("udf", udf);
            return Response.ok(object).status(201).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static class UDFRequest {
        public org.apache.ambari.view.pig.resources.udf.models.UDF udf;
    }
}