package org.apache.ambari.view.capacityscheduler;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
public class ConfigurationService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.capacityscheduler.ConfigurationService.class);

    private final org.apache.ambari.view.utils.ambari.AmbariApi ambariApi;

    private org.apache.ambari.view.ViewContext context;

    private static final java.lang.String REFRESH_RM_REQUEST_DATA = "{\n" + (((((((((("  \"RequestInfo\" : {\n" + "    \"command\" : \"REFRESHQUEUES\",\n") + "    \"context\" : \"Refresh YARN Capacity Scheduler\"\n") + "    \"parameters/forceRefreshConfigTags\" : \"capacity-scheduler\"\n") + "  },\n") + "  \"Requests/resource_filters\": [{\n") + "    \"service_name\" : \"YARN\",\n") + "    \"component_name\" : \"RESOURCEMANAGER\",\n") + "    \"hosts\" : \"%s\"\n") + "  }]\n") + "}");

    private static final java.lang.String RESTART_RM_REQUEST_DATA = "{\"RequestInfo\": {\n" + ((((((((((((((((("    \"command\":\"RESTART\",\n" + "    \"context\":\"Restart ResourceManager\",\n") + "    \"operation_level\": {\n") + "        \"level\":\"HOST_COMPONENT\",\n") + "        \"cluster_name\":\"%s\",\n") + "        \"host_name\":\"%s\",\n") + "        \"service_name\":\"YARN\",\n") + "        \"hostcomponent_name\":\"RESOURCEMANAGER\"\n") + "        }\n") + "    },\n") + "    \"Requests/resource_filters\": [\n") + "        {\n") + "            \"service_name\":\"YARN\",\n") + "            \"component_name\":\"RESOURCEMANAGER\",\n") + "            \"hosts\":\"%s\"\n") + "        }\n") + "    ]\n") + "}\n");

    public ConfigurationService(org.apache.ambari.view.ViewContext context) {
        this.context = context;
        this.ambariApi = new org.apache.ambari.view.utils.ambari.AmbariApi(context);
        this.ambariApi.setRequestedBy("view-capacity-scheduler");
    }

    private static final java.lang.String VERSION_TAG_URL = "?fields=Clusters/desired_configs/capacity-scheduler";

    private static final java.lang.String CONFIGURATION_URL = "configurations?type=capacity-scheduler";

    private static final java.lang.String CONFIGURATION_URL_BY_TAG = "configurations?type=capacity-scheduler&tag=%s";

    private static final java.lang.String RM_GET_NODE_LABEL_URL = "%s/ws/v1/cluster/get-node-labels";

    private static final java.lang.String RM_GET_SCHEDULER_CONFIG = "%s/ws/v1/cluster/scheduler";

    private static final java.lang.String AMBARI_OR_CLUSTER_ADMIN_PRIVILEGE_URL = "/api/v1/users/%s?privileges/PrivilegeInfo/permission_name=AMBARI.ADMINISTRATOR|" + "(privileges/PrivilegeInfo/permission_name.in(CLUSTER.ADMINISTRATOR,CLUSTER.OPERATOR)&privileges/PrivilegeInfo/cluster_name=%s)";

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response readLatestConfiguration() {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("reading all configurations");
        javax.ws.rs.core.Response response = null;
        try {
            java.lang.String versionTag = getVersionTag();
            org.json.simple.JSONObject configurations = getConfigurationFromAmbari(versionTag);
            response = javax.ws.rs.core.Response.ok(configurations).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("cluster")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response readClusterInfo() {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("Reading cluster info.");
        javax.ws.rs.core.Response response = null;
        try {
            org.json.simple.JSONObject configurations = readFromCluster("?fields=Clusters/version");
            response = javax.ws.rs.core.Response.ok(configurations).build();
        } catch (org.apache.ambari.view.AmbariHttpException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            if (ex.getResponseCode() == 403) {
                throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException("You do not have permission to view Capacity Scheduler configuration. Contact your Cluster administrator", ex);
            } else {
                throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
            }
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("all")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response readAllConfigurations() {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("Reading all configurations.");
        javax.ws.rs.core.Response response = null;
        try {
            org.json.simple.JSONObject responseJSON = readFromCluster(org.apache.ambari.view.capacityscheduler.ConfigurationService.CONFIGURATION_URL);
            response = javax.ws.rs.core.Response.ok(responseJSON).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("byTag/{tag}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response readConfigurationByTag(@javax.ws.rs.PathParam("tag")
    java.lang.String tag) {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.info("Reading configurations for tag : {}", tag);
        javax.ws.rs.core.Response response = null;
        try {
            org.json.simple.JSONObject configurations = getConfigurationFromAmbari(tag);
            response = javax.ws.rs.core.Response.ok(configurations).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/privilege")
    public javax.ws.rs.core.Response getPrivilege() {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("Reading privilege.");
        javax.ws.rs.core.Response response = null;
        try {
            boolean operator = isOperator();
            response = javax.ws.rs.core.Response.ok(operator).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/nodeLabels")
    public javax.ws.rs.core.Response getNodeLabels() {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("reading nodeLables");
        javax.ws.rs.core.Response response;
        try {
            java.lang.String url = java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.RM_GET_NODE_LABEL_URL, getRMUrl());
            java.io.InputStream rmResponse = context.getURLStreamProvider().readFrom(url, "GET", ((java.lang.String) (null)), new java.util.HashMap<java.lang.String, java.lang.String>());
            java.lang.String nodeLabels = org.apache.commons.io.IOUtils.toString(rmResponse);
            response = javax.ws.rs.core.Response.ok(nodeLabels).build();
        } catch (java.net.ConnectException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException("Connection to Resource Manager refused", ex);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return response;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/rmCurrentConfig")
    public javax.ws.rs.core.Response getRmSchedulerConfig() {
        try {
            java.lang.String url = java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.RM_GET_SCHEDULER_CONFIG, getRMUrl());
            java.io.InputStream rmResponse = context.getURLStreamProvider().readFrom(url, "GET", ((java.lang.String) (null)), new java.util.HashMap<java.lang.String, java.lang.String>());
            java.lang.String result = org.apache.commons.io.IOUtils.toString(rmResponse);
            return javax.ws.rs.core.Response.ok(result).build();
        } catch (java.net.ConnectException ex) {
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException("Connection to Resource Manager refused", ex);
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    private boolean isOperator() {
        java.lang.String url = java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.AMBARI_OR_CLUSTER_ADMIN_PRIVILEGE_URL, context.getLoggedinUser(), context.getCluster().getName());
        try {
            java.lang.String response = ambariApi.readFromAmbari(url, "GET", null, null);
            if ((response != null) && (!response.isEmpty())) {
                org.json.simple.JSONObject json = ((org.json.simple.JSONObject) (org.json.simple.JSONValue.parse(response)));
                if (json.containsKey("privileges")) {
                    org.json.simple.JSONArray privileges = ((org.json.simple.JSONArray) (json.get("privileges")));
                    if (privileges.size() > 0)
                        return true;

                }
            }
        } catch (org.apache.ambari.view.AmbariHttpException e) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Got Error response from url : {}. Response : {}", url, e.getMessage(), e);
        }
        return false;
    }

    private org.json.simple.JSONObject readFromCluster(java.lang.String url) throws org.apache.ambari.view.AmbariHttpException {
        java.lang.String response = ambariApi.requestClusterAPI(url);
        if ((response == null) || response.isEmpty()) {
            return null;
        }
        return getJsonObject(response);
    }

    private org.json.simple.JSONObject getJsonObject(java.lang.String response) {
        if ((response == null) || response.isEmpty()) {
            return null;
        }
        org.json.simple.JSONObject jsonObject = ((org.json.simple.JSONObject) (org.json.simple.JSONValue.parse(response)));
        if ((jsonObject.get("status") != null) && (((java.lang.Long) (jsonObject.get("status"))) >= 400L)) {
            java.lang.String message;
            if (jsonObject.containsKey("message")) {
                message = ((java.lang.String) (jsonObject.get("message")));
            } else {
                message = "without message";
            }
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(((("Proxy: Server returned error " + jsonObject.get("status")) + " ") + message) + ". Check Capacity-Scheduler instance properties.");
        }
        return jsonObject;
    }

    private org.json.simple.JSONObject getConfigurationFromAmbari(java.lang.String versionTag) throws org.apache.ambari.view.AmbariHttpException {
        java.lang.String url = java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.CONFIGURATION_URL_BY_TAG, versionTag);
        org.json.simple.JSONObject responseJSON = readFromCluster(url);
        return responseJSON;
    }

    private java.lang.String getVersionTag() throws org.apache.ambari.view.AmbariHttpException {
        org.json.simple.JSONObject json = getDesiredConfigs();
        org.json.simple.JSONObject clusters = ((org.json.simple.JSONObject) (json.get("Clusters")));
        org.json.simple.JSONObject configs = ((org.json.simple.JSONObject) (clusters.get("desired_configs")));
        org.json.simple.JSONObject scheduler = ((org.json.simple.JSONObject) (configs.get("capacity-scheduler")));
        return ((java.lang.String) (scheduler.get("tag")));
    }

    private java.lang.String getClusterName() throws org.apache.ambari.view.AmbariHttpException {
        org.json.simple.JSONObject json = getDesiredConfigs();
        org.json.simple.JSONObject clusters = ((org.json.simple.JSONObject) (json.get("Clusters")));
        return ((java.lang.String) (clusters.get("cluster_name")));
    }

    private org.json.simple.JSONObject getDesiredConfigs() throws org.apache.ambari.view.AmbariHttpException {
        org.json.simple.JSONObject response = readFromCluster(org.apache.ambari.view.capacityscheduler.ConfigurationService.VERSION_TAG_URL);
        return response;
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response writeConfiguration(java.lang.String requestBody) {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("writeConfiguration for request : {} ", requestBody);
        org.json.simple.JSONObject response;
        try {
            if (isOperator() == false) {
                org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("returning 401 as not an operator.");
                return javax.ws.rs.core.Response.status(401).build();
            }
            java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            java.lang.String responseString = ambariApi.requestClusterAPI("", "PUT", requestBody, headers);
            response = getJsonObject(responseString);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return javax.ws.rs.core.Response.ok(response).build();
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/saveAndRefresh")
    public javax.ws.rs.core.Response writeAndRefreshConfiguration(org.json.simple.JSONObject request) {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("writeAndRefreshConfiguration for request : {} ", request);
        try {
            if (isOperator() == false) {
                org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("throwing 401 error as not an operator");
                return javax.ws.rs.core.Response.status(401).build();
            }
            java.lang.String rmHosts = getRMHosts();
            org.json.simple.JSONObject data = getJsonObject(java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.REFRESH_RM_REQUEST_DATA, rmHosts));
            java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            ambariApi.requestClusterAPI("requests/", "POST", data.toJSONString(), headers);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.info("Exception Occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.info("Exception Occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return readLatestConfiguration();
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/saveAndRestart")
    public javax.ws.rs.core.Response writeAndRestartConfiguration(org.json.simple.JSONObject request) {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.debug("writeAndRestartConfiguration for request : {} ", request);
        try {
            if (isOperator() == false) {
                org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("throwing 401 error as not an operator.");
                return javax.ws.rs.core.Response.status(401).build();
            }
            java.lang.String rmHosts = getRMHosts();
            org.json.simple.JSONObject data = getJsonObject(java.lang.String.format(org.apache.ambari.view.capacityscheduler.ConfigurationService.RESTART_RM_REQUEST_DATA, context.getCluster().getName(), rmHosts, rmHosts));
            java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            ambariApi.requestClusterAPI("requests/", "POST", data.toJSONString(), headers);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occured : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occured : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
        return readLatestConfiguration();
    }

    private java.lang.String getRMUrl() {
        return ambariApi.getServices().getRMUrl();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Path("/getConfig")
    public javax.ws.rs.core.Response getConfigurationValue(@javax.ws.rs.QueryParam("siteName")
    java.lang.String siteName, @javax.ws.rs.QueryParam("configName")
    java.lang.String configName) {
        org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.info("Get configuration value for siteName {}, configName {}", siteName, configName);
        try {
            java.lang.String configValue = context.getCluster().getConfigurationValue(siteName, configName);
            org.json.simple.JSONObject res = new org.json.simple.JSONObject();
            org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
            org.json.simple.JSONObject conf = new org.json.simple.JSONObject();
            conf.put("siteName", siteName);
            conf.put("configName", configName);
            conf.put("configValue", configValue);
            arr.add(conf);
            res.put("configs", arr);
            return javax.ws.rs.core.Response.ok(res).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.capacityscheduler.ConfigurationService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.capacityscheduler.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    private java.lang.String getRMHosts() {
        java.lang.StringBuilder hosts = new java.lang.StringBuilder();
        boolean first = true;
        for (java.lang.String host : context.getCluster().getHostsForServiceComponent("YARN", "RESOURCEMANAGER")) {
            if (!first) {
                hosts.append(",");
            }
            hosts.append(host);
            first = false;
        }
        return hosts.toString();
    }
}