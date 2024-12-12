package org.apache.oozie.ambari.view.assets;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
public class AssetResource {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.assets.AssetResource.class);

    private final org.apache.oozie.ambari.view.assets.AssetService assetService;

    private final org.apache.ambari.view.ViewContext viewContext;

    private final org.apache.oozie.ambari.view.HDFSFileUtils hdfsFileUtils;

    private final org.apache.oozie.ambari.view.OozieUtils oozieUtils = new org.apache.oozie.ambari.view.OozieUtils();

    private final org.apache.oozie.ambari.view.OozieDelegate oozieDelegate;

    public AssetResource(org.apache.ambari.view.ViewContext viewContext) {
        this.viewContext = viewContext;
        this.assetService = new org.apache.oozie.ambari.view.assets.AssetService(viewContext);
        hdfsFileUtils = new org.apache.oozie.ambari.view.HDFSFileUtils(viewContext);
        oozieDelegate = new org.apache.oozie.ambari.view.OozieDelegate(viewContext);
    }

    @org.apache.oozie.ambari.view.GET
    public org.apache.oozie.ambari.view.Response getAssets() {
        try {
            java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> assets = assetService.getAssets();
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            result.getPaging().setTotal(assets != null ? assets.size() : 0L);
            result.setData(assets);
            return Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @org.apache.oozie.ambari.view.GET
    @org.apache.oozie.ambari.view.Path("/mine")
    public org.apache.oozie.ambari.view.Response getMyAssets() {
        try {
            java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> assets = assetService.getMyAssets();
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            result.getPaging().setTotal(assets != null ? assets.size() : 0L);
            result.setData(assets);
            return Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @org.apache.oozie.ambari.view.POST
    public org.apache.oozie.ambari.view.Response saveAsset(@org.apache.oozie.ambari.view.Context
    HttpHeaders headers, @org.apache.oozie.ambari.view.QueryParam("id")
    java.lang.String id, @org.apache.oozie.ambari.view.Context
    UriInfo ui, java.lang.String body) {
        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            org.apache.oozie.ambari.view.assets.model.AssetDefintion assetDefinition = gson.fromJson(body, org.apache.oozie.ambari.view.assets.model.AssetDefintion.class);
            java.util.Map<java.lang.String, java.lang.String> validateAsset = validateAsset(headers, assetDefinition.getDefinition(), ui.getQueryParameters());
            if (!org.apache.oozie.ambari.view.Constants.STATUS_OK.equals(validateAsset.get(org.apache.oozie.ambari.view.Constants.STATUS_KEY))) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.ASSET_INVALID_FROM_OOZIE);
            }
            assetService.saveAsset(id, viewContext.getUsername(), assetDefinition);
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            return Response.ok(result).build();
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    private java.util.List<java.lang.String> getAsList(java.lang.String string) {
        java.util.ArrayList<java.lang.String> li = new java.util.ArrayList<>(1);
        li.add(string);
        return li;
    }

    public java.util.Map<java.lang.String, java.lang.String> validateAsset(HttpHeaders headers, java.lang.String postBody, MultivaluedMap<java.lang.String, java.lang.String> queryParams) {
        java.lang.String workflowXml = oozieUtils.generateWorkflowXml(postBody);
        java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
        java.lang.String tempWfPath = (("/tmp" + "/tmpooziewfs/tempwf_") + java.lang.Math.round(java.lang.Math.random() * 100000)) + ".xml";
        try {
            hdfsFileUtils.writeToFile(tempWfPath, workflowXml, true);
        } catch (java.io.IOException ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex, org.apache.oozie.ambari.view.exception.ErrorCode.FILE_ACCESS_UNKNOWN_ERROR);
        }
        queryParams.put("oozieparam.action", getAsList("dryrun"));
        queryParams.put("oozieconfig.rerunOnFailure", getAsList("false"));
        queryParams.put("oozieconfig.useSystemLibPath", getAsList("true"));
        queryParams.put("resourceManager", getAsList("useDefault"));
        java.lang.String dryRunResp = oozieDelegate.submitWorkflowJobToOozie(headers, tempWfPath, queryParams, org.apache.oozie.ambari.view.JobType.WORKFLOW);
        org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.info(java.lang.String.format("resp from validating asset=[%s]", dryRunResp));
        try {
            hdfsFileUtils.deleteFile(tempWfPath);
        } catch (java.io.IOException ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex, org.apache.oozie.ambari.view.exception.ErrorCode.FILE_ACCESS_UNKNOWN_ERROR);
        }
        if ((dryRunResp != null) && dryRunResp.trim().startsWith("{")) {
            com.google.gson.JsonElement jsonElement = new com.google.gson.JsonParser().parse(dryRunResp);
            com.google.gson.JsonElement idElem = jsonElement.getAsJsonObject().get("id");
            if (idElem != null) {
                result.put(org.apache.oozie.ambari.view.Constants.STATUS_KEY, org.apache.oozie.ambari.view.Constants.STATUS_OK);
            } else {
                result.put(org.apache.oozie.ambari.view.Constants.STATUS_KEY, org.apache.oozie.ambari.view.Constants.STATUS_FAILED);
                result.put(org.apache.oozie.ambari.view.Constants.MESSAGE_KEY, dryRunResp);
            }
        } else {
            result.put(org.apache.oozie.ambari.view.Constants.STATUS_KEY, org.apache.oozie.ambari.view.Constants.STATUS_FAILED);
            result.put(org.apache.oozie.ambari.view.Constants.MESSAGE_KEY, dryRunResp);
        }
        return result;
    }

    @org.apache.oozie.ambari.view.GET
    @org.apache.oozie.ambari.view.Path("/assetNameAvailable")
    public org.apache.oozie.ambari.view.Response assetNameAvailable(@org.apache.oozie.ambari.view.QueryParam("name")
    java.lang.String name) {
        try {
            boolean available = assetService.isAssetNameAvailable(name);
            return Response.ok(available).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @org.apache.oozie.ambari.view.GET
    @org.apache.oozie.ambari.view.Path("/{id}")
    public org.apache.oozie.ambari.view.Response getAssetDetail(@org.apache.oozie.ambari.view.assets.PathParam("id")
    java.lang.String id) {
        try {
            org.apache.oozie.ambari.view.assets.model.AssetDefintion assetDefinition = assetService.getAssetDetail(id);
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            result.setData(assetDefinition);
            return Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @org.apache.oozie.ambari.view.GET
    @org.apache.oozie.ambari.view.Path("/definition/id}")
    public org.apache.oozie.ambari.view.Response getAssetDefinition(@org.apache.oozie.ambari.view.assets.PathParam("defnitionId")
    java.lang.String id) {
        try {
            org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition assetDefinition = assetService.getAssetDefinition(id);
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            result.setData(assetDefinition);
            return Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @org.apache.oozie.ambari.view.DELETE
    @org.apache.oozie.ambari.view.Path("/{id}")
    public org.apache.oozie.ambari.view.Response delete(@org.apache.oozie.ambari.view.assets.PathParam("id")
    java.lang.String id) {
        try {
            org.apache.oozie.ambari.view.assets.model.ActionAsset asset = assetService.getAsset(id);
            if (asset == null) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.ASSET_NOT_EXIST);
            }
            if (!viewContext.getUsername().equals(asset.getOwner())) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.PERMISSION_ERROR);
            }
            assetService.deleteAsset(id);
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            return Response.ok(result).build();
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.assets.AssetResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }
}