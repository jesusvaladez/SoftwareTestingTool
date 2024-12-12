package org.apache.oozie.ambari.view;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
public class OozieDelegate {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.OozieDelegate.class);

    private static final java.lang.String OOZIEPARAM_PREFIX = "oozieparam.";

    private static final int OOZIEPARAM_PREFIX_LENGTH = org.apache.oozie.ambari.view.OozieDelegate.OOZIEPARAM_PREFIX.length();

    private static final java.lang.String EQUAL_SYMBOL = "=";

    private static final java.lang.String OOZIE_WF_RERUN_FAILNODES_CONF_KEY = "oozie.wf.rerun.failnodes";

    private static final java.lang.String OOZIE_USE_SYSTEM_LIBPATH_CONF_KEY = "oozie.use.system.libpath";

    private static final java.lang.String USER_NAME_HEADER = "user.name";

    private static final java.lang.String USER_OOZIE_SUPER = "oozie";

    private static final java.lang.String DO_AS_HEADER = "doAs";

    private static final java.lang.String SERVICE_URI_PROP = "oozie.service.uri";

    private static final java.lang.String DEFAULT_SERVICE_URI = "http://sandbox.hortonworks.com:11000/oozie";

    private org.apache.ambari.view.ViewContext viewContext;

    private org.apache.oozie.ambari.view.OozieUtils oozieUtils = new org.apache.oozie.ambari.view.OozieUtils();

    private final org.apache.oozie.ambari.view.Utils utils = new org.apache.oozie.ambari.view.Utils();

    private final org.apache.oozie.ambari.view.AmbariIOUtil ambariIOUtil;

    public OozieDelegate(org.apache.ambari.view.ViewContext viewContext) {
        super();
        this.viewContext = viewContext;
        this.ambariIOUtil = new org.apache.oozie.ambari.view.AmbariIOUtil(viewContext);
    }

    public java.lang.String submitWorkflowJobToOozie(javax.ws.rs.core.HttpHeaders headers, java.lang.String filePath, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams, org.apache.oozie.ambari.view.JobType jobType) {
        java.lang.String nameNode = viewContext.getProperties().get("webhdfs.url");
        if (nameNode == null) {
            org.apache.oozie.ambari.view.OozieDelegate.LOGGER.error("Name Node couldn't be determined automatically.");
            throw new java.lang.RuntimeException("Name Node couldn't be determined automatically.");
        }
        if (!queryParams.containsKey("config.nameNode")) {
            java.util.ArrayList<java.lang.String> nameNodes = new java.util.ArrayList<java.lang.String>();
            org.apache.oozie.ambari.view.OozieDelegate.LOGGER.info("Namenode===" + nameNode);
            nameNodes.add(nameNode);
            queryParams.put("config.nameNode", nameNodes);
        }
        java.util.Map<java.lang.String, java.lang.String> workflowConigs = getWorkflowConfigs(filePath, queryParams, jobType, nameNode);
        java.lang.String configXMl = oozieUtils.generateConfigXml(workflowConigs);
        org.apache.oozie.ambari.view.OozieDelegate.LOGGER.info("Config xml==" + configXMl);
        java.util.HashMap<java.lang.String, java.lang.String> customHeaders = new java.util.HashMap<java.lang.String, java.lang.String>();
        customHeaders.put("Content-Type", "application/xml;charset=UTF-8");
        javax.ws.rs.core.Response serviceResponse = consumeService(headers, (getServiceUri() + "/v2/jobs?") + getJobSumbitOozieParams(queryParams), HttpMethod.POST, configXMl, customHeaders);
        org.apache.oozie.ambari.view.OozieDelegate.LOGGER.info("Resp from oozie status entity==" + serviceResponse.getEntity());
        java.lang.String oozieResp = null;
        if (serviceResponse.getEntity() instanceof java.lang.String) {
            oozieResp = ((java.lang.String) (serviceResponse.getEntity()));
        } else {
            oozieResp = serviceResponse.getEntity().toString();
        }
        if ((oozieResp != null) && oozieResp.trim().startsWith("{")) {
            return oozieResp;
        } else {
            throw new org.apache.oozie.ambari.view.exception.WfmException(oozieResp, org.apache.oozie.ambari.view.exception.ErrorCode.OOZIE_SUBMIT_ERROR);
        }
    }

    public javax.ws.rs.core.Response consumeService(javax.ws.rs.core.HttpHeaders headers, java.lang.String path, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParameters, java.lang.String method, java.lang.String body) throws java.lang.Exception {
        return consumeService(headers, this.buildUri(path, queryParameters), method, body, null);
    }

    private javax.ws.rs.core.Response consumeService(javax.ws.rs.core.HttpHeaders headers, java.lang.String urlToRead, java.lang.String method, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> customHeaders) {
        javax.ws.rs.core.Response response = null;
        java.io.InputStream stream = readFromOozie(headers, urlToRead, method, body, customHeaders);
        java.lang.String stringResponse = null;
        try {
            stringResponse = org.apache.commons.io.IOUtils.toString(stream);
        } catch (java.io.IOException e) {
            org.apache.oozie.ambari.view.OozieDelegate.LOGGER.error("Error while converting stream to string", e);
            throw new java.lang.RuntimeException(e);
        }
        if (stringResponse.contains(Response.Status.BAD_REQUEST.name())) {
            response = javax.ws.rs.core.Response.status(Response.Status.BAD_REQUEST).entity(stringResponse).type(MediaType.TEXT_PLAIN).build();
        } else {
            response = javax.ws.rs.core.Response.status(Response.Status.OK).entity(stringResponse).type(utils.deduceType(stringResponse)).build();
        }
        return response;
    }

    public java.io.InputStream readFromOozie(javax.ws.rs.core.HttpHeaders headers, java.lang.String urlToRead, java.lang.String method, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> customHeaders) {
        java.util.Map<java.lang.String, java.lang.String> newHeaders = utils.getHeaders(headers);
        newHeaders.put(org.apache.oozie.ambari.view.OozieDelegate.USER_NAME_HEADER, org.apache.oozie.ambari.view.OozieDelegate.USER_OOZIE_SUPER);
        newHeaders.put(org.apache.oozie.ambari.view.OozieDelegate.DO_AS_HEADER, viewContext.getUsername());
        newHeaders.put("Accept", MediaType.APPLICATION_JSON);
        if (customHeaders != null) {
            newHeaders.putAll(customHeaders);
        }
        org.apache.oozie.ambari.view.OozieDelegate.LOGGER.info(java.lang.String.format("Proxy request for url: [%s] %s", method, urlToRead));
        return ambariIOUtil.readFromUrl(urlToRead, method, body, newHeaders);
    }

    private java.util.Map<java.lang.String, java.lang.String> getWorkflowConfigs(java.lang.String filePath, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams, org.apache.oozie.ambari.view.JobType jobType, java.lang.String nameNode) {
        java.util.HashMap<java.lang.String, java.lang.String> workflowConigs = new java.util.HashMap<java.lang.String, java.lang.String>();
        if (queryParams.containsKey("resourceManager") && "useDefault".equals(queryParams.getFirst("resourceManager"))) {
            java.lang.String jobTrackerNode = viewContext.getProperties().get("yarn.resourcemanager.address");
            org.apache.oozie.ambari.view.OozieDelegate.LOGGER.info("jobTrackerNode===" + jobTrackerNode);
            workflowConigs.put("resourceManager", jobTrackerNode);
            workflowConigs.put("jobTracker", jobTrackerNode);
        }
        if (queryParams != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : queryParams.entrySet()) {
                if (entry.getKey().startsWith("config.")) {
                    if ((entry.getValue() != null) && (entry.getValue().size() > 0)) {
                        workflowConigs.put(entry.getKey().substring(7), entry.getValue().get(0));
                    }
                }
            }
        }
        if (queryParams.containsKey("oozieconfig.useSystemLibPath")) {
            java.lang.String useSystemLibPath = queryParams.getFirst("oozieconfig.useSystemLibPath");
            workflowConigs.put(org.apache.oozie.ambari.view.OozieDelegate.OOZIE_USE_SYSTEM_LIBPATH_CONF_KEY, useSystemLibPath);
        } else {
            workflowConigs.put(org.apache.oozie.ambari.view.OozieDelegate.OOZIE_USE_SYSTEM_LIBPATH_CONF_KEY, "true");
        }
        if (queryParams.containsKey("oozieconfig.rerunOnFailure")) {
            java.lang.String rerunFailnodes = queryParams.getFirst("oozieconfig.rerunOnFailure");
            workflowConigs.put(org.apache.oozie.ambari.view.OozieDelegate.OOZIE_WF_RERUN_FAILNODES_CONF_KEY, rerunFailnodes);
        } else {
            workflowConigs.put(org.apache.oozie.ambari.view.OozieDelegate.OOZIE_WF_RERUN_FAILNODES_CONF_KEY, "true");
        }
        workflowConigs.put("user.name", viewContext.getUsername());
        workflowConigs.put(oozieUtils.getJobPathPropertyKey(jobType), nameNode + filePath);
        return workflowConigs;
    }

    private java.lang.String getJobSumbitOozieParams(javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams) {
        java.lang.StringBuilder query = new java.lang.StringBuilder();
        if (queryParams != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : queryParams.entrySet()) {
                if (entry.getKey().startsWith(org.apache.oozie.ambari.view.OozieDelegate.OOZIEPARAM_PREFIX)) {
                    if ((entry.getValue() != null) && (entry.getValue().size() > 0)) {
                        for (java.lang.String val : entry.getValue()) {
                            query.append(entry.getKey().substring(org.apache.oozie.ambari.view.OozieDelegate.OOZIEPARAM_PREFIX_LENGTH)).append(org.apache.oozie.ambari.view.OozieDelegate.EQUAL_SYMBOL).append(val).append("&");
                        }
                    }
                }
            }
        }
        return query.toString();
    }

    private java.lang.String getServiceUri() {
        java.lang.String serviceURI = (viewContext.getProperties().get(org.apache.oozie.ambari.view.OozieDelegate.SERVICE_URI_PROP) != null) ? viewContext.getProperties().get(org.apache.oozie.ambari.view.OozieDelegate.SERVICE_URI_PROP) : org.apache.oozie.ambari.view.OozieDelegate.DEFAULT_SERVICE_URI;
        return serviceURI;
    }

    private java.lang.String buildUri(java.lang.String absolutePath, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParameters) {
        int index = absolutePath.indexOf("proxy/") + 5;
        absolutePath = absolutePath.substring(index);
        java.lang.String serviceURI = getServiceUri();
        serviceURI += absolutePath;
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> params = addOrReplaceUserName(queryParameters);
        return serviceURI + utils.convertParamsToUrl(params);
    }

    private javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> addOrReplaceUserName(javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> parameters) {
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : parameters.entrySet()) {
            if ("user.name".equals(entry.getKey())) {
                java.util.ArrayList<java.lang.String> vals = new java.util.ArrayList<java.lang.String>(1);
                vals.add(viewContext.getUsername());
                entry.setValue(vals);
            }
        }
        return parameters;
    }

    public java.lang.String getDagUrl(java.lang.String jobid) {
        return ((getServiceUri() + "/v2/job/") + jobid) + "?show=graph";
    }
}