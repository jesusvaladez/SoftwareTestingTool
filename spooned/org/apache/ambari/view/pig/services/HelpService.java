package org.apache.ambari.view.pig.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import org.json.simple.JSONObject;
public class HelpService extends org.apache.ambari.view.pig.services.BaseService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.services.HelpService.class);

    private org.apache.ambari.view.ViewContext context;

    private org.apache.ambari.view.ViewResourceHandler handler;

    public HelpService(org.apache.ambari.view.ViewContext context, org.apache.ambari.view.ViewResourceHandler handler) {
        super();
        this.context = context;
        this.handler = handler;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/config")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.services.Response config() {
        org.json.simple.JSONObject object = new org.json.simple.JSONObject();
        java.lang.String fs = context.getProperties().get("webhdfs.url");
        object.put("webhdfs.url", fs);
        return Response.ok(object).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/version")
    @javax.ws.rs.Produces(MediaType.TEXT_PLAIN)
    public org.apache.ambari.view.pig.services.Response version() {
        return Response.ok("0.0.1-SNAPSHOT").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/hdfsStatus")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.services.Response hdfsStatus() {
        org.apache.ambari.view.pig.resources.files.FileService.hdfsSmokeTest(context);
        return getOKResponse();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/userhomeStatus")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.services.Response userhomeStatus() {
        org.apache.ambari.view.pig.resources.files.FileService.userhomeSmokeTest(context);
        return getOKResponse();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/webhcatStatus")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.services.Response webhcatStatus() {
        org.apache.ambari.view.pig.resources.jobs.JobResourceManager.webhcatSmokeTest(context);
        return getOKResponse();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/storageStatus")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.pig.services.Response storageStatus() {
        org.apache.ambari.view.pig.persistence.DataStoreStorage.storageSmokeTest(context);
        return getOKResponse();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/service-check-policy")
    public org.apache.ambari.view.pig.services.Response getServiceCheckList() {
        org.apache.ambari.view.pig.utils.ServiceCheck serviceCheck = new org.apache.ambari.view.pig.utils.ServiceCheck(context);
        try {
            org.apache.ambari.view.pig.utils.ServiceCheck.Policy policy = serviceCheck.getServiceCheckPolicy();
            org.json.simple.JSONObject policyJson = new org.json.simple.JSONObject();
            policyJson.put("serviceCheckPolicy", policy);
            return Response.ok(policyJson).build();
        } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
            org.apache.ambari.view.pig.services.HelpService.LOG.error("Error occurred while generating service check policy : ", e);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(e);
        }
    }

    private org.apache.ambari.view.pig.services.Response getOKResponse() {
        org.json.simple.JSONObject response = new org.json.simple.JSONObject();
        response.put("message", "OK");
        response.put("trace", null);
        response.put("status", "200");
        return Response.ok().entity(response).type(MediaType.APPLICATION_JSON).build();
    }
}