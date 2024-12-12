package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
public class LoggingService extends org.apache.ambari.server.api.services.BaseService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.LoggingService.class);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> REQUIRED_AUTHORIZATIONS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_OPERATIONAL_LOGS);

    private final org.apache.ambari.server.api.services.LoggingService.ControllerFactory controllerFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory;

    private final java.lang.String clusterName;

    public LoggingService(java.lang.String clusterName) {
        this(clusterName, new org.apache.ambari.server.api.services.LoggingService.DefaultControllerFactory());
    }

    public LoggingService(java.lang.String clusterName, org.apache.ambari.server.api.services.LoggingService.ControllerFactory controllerFactory) {
        this.clusterName = clusterName;
        this.controllerFactory = controllerFactory;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("searchEngine")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getSearchEngine(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uri) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceId(), org.apache.ambari.server.api.services.LoggingService.REQUIRED_AUTHORIZATIONS)) {
            return handleDirectRequest(uri, MediaType.TEXT_PLAIN_TYPE);
        } else {
            javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.status(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN).getStatusCode());
            responseBuilder.entity("The authenticated user is not authorized to perform this operation.");
            return responseBuilder.build();
        }
    }

    private java.lang.Long getClusterResourceId() {
        java.lang.Long clusterResourceId = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
            try {
                org.apache.ambari.server.state.Cluster cluster = controllerFactory.getController().getClusters().getCluster(clusterName);
                if (cluster == null) {
                    org.apache.ambari.server.api.services.LoggingService.LOG.warn("No cluster found with the name {}, assuming null resource id", clusterName);
                } else {
                    clusterResourceId = cluster.getResourceId();
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.api.services.LoggingService.LOG.warn("An exception occurred looking up the cluster named {}, assuming null resource id: {}", clusterName, e.getLocalizedMessage());
            }
        } else {
            org.apache.ambari.server.api.services.LoggingService.LOG.debug("The cluster name is not set, assuming null resource id");
        }
        return clusterResourceId;
    }

    protected javax.ws.rs.core.Response handleDirectRequest(javax.ws.rs.core.UriInfo uriInfo, javax.ws.rs.core.MediaType mediaType) {
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParameters = uriInfo.getQueryParameters();
        java.util.Map<java.lang.String, java.lang.String> enumeratedQueryParameters = new java.util.HashMap<>();
        for (java.lang.String queryName : queryParameters.keySet()) {
            java.util.List<java.lang.String> queryValue = queryParameters.get(queryName);
            for (java.lang.String value : queryValue) {
                enumeratedQueryParameters.put(queryName, value);
            }
        }
        org.apache.ambari.server.controller.AmbariManagementController controller = controllerFactory.getController();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper requestHelper = helperFactory.getHelper(controller, clusterName);
        if (requestHelper != null) {
            org.apache.ambari.server.controller.logging.LogQueryResponse response = requestHelper.sendQueryRequest(enumeratedQueryParameters);
            if (response != null) {
                org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = (mediaType == null) ? getResultSerializer() : getResultSerializer(mediaType);
                org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
                org.apache.ambari.server.controller.spi.Resource loggingResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.LoggingQuery);
                loggingResource.setProperty("startIndex", response.getStartIndex());
                loggingResource.setProperty("pageSize", response.getPageSize());
                loggingResource.setProperty("resultSize", response.getResultSize());
                loggingResource.setProperty("queryTimeMMS", response.getQueryTimeMS());
                loggingResource.setProperty("totalCount", response.getTotalCount());
                loggingResource.setProperty("logList", response.getListOfResults());
                result.getResultTree().addChild(loggingResource, "logging");
                javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.status(result.getStatus().getStatusCode()).entity(serializer.serialize(result));
                if (mediaType != null) {
                    builder.type(mediaType);
                }
                org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
                return builder.build();
            }
        } else {
            org.apache.ambari.server.api.services.LoggingService.LOG.debug("LogSearch is not currently available, an empty response will be returned");
        }
        final javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.status(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND).getStatusCode());
        responseBuilder.entity("LogSearch is not currently available.  If LogSearch is deployed in this cluster, please verify that the LogSearch services are running.");
        return responseBuilder.build();
    }

    void setLoggingRequestHelperFactory(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory) {
        this.helperFactory = helperFactory;
    }

    interface ControllerFactory {
        org.apache.ambari.server.controller.AmbariManagementController getController();
    }

    private static class DefaultControllerFactory implements org.apache.ambari.server.api.services.LoggingService.ControllerFactory {
        @java.lang.Override
        public org.apache.ambari.server.controller.AmbariManagementController getController() {
            return org.apache.ambari.server.controller.AmbariServer.getController();
        }
    }
}