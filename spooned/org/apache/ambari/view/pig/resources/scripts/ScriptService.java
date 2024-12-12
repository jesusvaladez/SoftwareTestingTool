package org.apache.ambari.view.pig.resources.scripts;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
public class ScriptService extends org.apache.ambari.view.pig.services.BaseService {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewResourceHandler handler;

    protected org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager resourceManager = null;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.scripts.ScriptService.class);

    protected synchronized org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.scripts.models.PigScript> getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager(context);
        }
        return resourceManager;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{scriptId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getScript(@javax.ws.rs.PathParam("scriptId")
    java.lang.String scriptId) {
        org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.info("Fetching scriptId : {}", scriptId);
        try {
            org.apache.ambari.view.pig.resources.scripts.models.PigScript script = null;
            script = getResourceManager().read(scriptId);
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("script", script);
            return javax.ws.rs.core.Response.ok(object).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", itemNotFound);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{scriptId}")
    public javax.ws.rs.core.Response deleteScript(@javax.ws.rs.PathParam("scriptId")
    java.lang.String scriptId) {
        org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.info("Deleting scriptId : {}", scriptId);
        try {
            getResourceManager().delete(scriptId);
            return javax.ws.rs.core.Response.status(204).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", itemNotFound);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getScriptList() {
        try {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.debug("Getting all scripts");
            java.util.List allScripts = getResourceManager().readAll(new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername()));
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("scripts", allScripts);
            return javax.ws.rs.core.Response.ok(object).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{scriptId}")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response updateScript(org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest request, @javax.ws.rs.PathParam("scriptId")
    java.lang.String scriptId) {
        org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.info("updating scriptId : {} ", scriptId);
        try {
            getResourceManager().update(request.script, scriptId);
            return javax.ws.rs.core.Response.status(204).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", itemNotFound);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response saveScript(org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest request, @javax.ws.rs.core.Context
    javax.servlet.http.HttpServletResponse response, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.info("Creating new script : {}", request);
        try {
            getResourceManager().create(request.script);
            org.apache.ambari.view.pig.resources.scripts.models.PigScript script = null;
            script = getResourceManager().read(request.script.getId());
            response.setHeader("Location", java.lang.String.format("%s/%s", ui.getAbsolutePath().toString(), request.script.getId()));
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("script", script);
            return javax.ws.rs.core.Response.ok(object).status(201).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", itemNotFound);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.scripts.ScriptService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static class PigScriptRequest {
        public org.apache.ambari.view.pig.resources.scripts.models.PigScript script;
    }
}