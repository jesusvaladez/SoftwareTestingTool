package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public abstract class BaseService {
    public static final javax.ws.rs.core.MediaType MEDIA_TYPE_TEXT_CSV_TYPE = new javax.ws.rs.core.MediaType("text", "csv");

    public static final java.lang.String MSG_SUCCESSFUL_OPERATION = "Successful operation";

    public static final java.lang.String MSG_REQUEST_ACCEPTED = "Request is accepted, but not completely processed yet";

    public static final java.lang.String MSG_INVALID_ARGUMENTS = "Invalid arguments";

    public static final java.lang.String MSG_INVALID_REQUEST = "Invalid request";

    public static final java.lang.String MSG_CLUSTER_NOT_FOUND = "Cluster not found";

    public static final java.lang.String MSG_CLUSTER_OR_HOST_NOT_FOUND = "Cluster or host not found";

    public static final java.lang.String MSG_VIEW_NOT_FOUND = "View not found";

    public static final java.lang.String MSG_NOT_AUTHENTICATED = "Not authenticated";

    public static final java.lang.String MSG_PERMISSION_DENIED = "Not permitted to perform the operation";

    public static final java.lang.String MSG_SERVER_ERROR = "Internal server error";

    public static final java.lang.String MSG_RESOURCE_ALREADY_EXISTS = "The requested resource already exists.";

    public static final java.lang.String MSG_RESOURCE_NOT_FOUND = "The requested resource doesn't exist.";

    public static final java.lang.String QUERY_FIELDS = "fields";

    public static final java.lang.String QUERY_FILTER_DESCRIPTION = "Filter fields in the response (identifier fields are mandatory)";

    public static final java.lang.String QUERY_SORT = "sortBy";

    public static final java.lang.String QUERY_SORT_DESCRIPTION = "Sort resources in result by (asc | desc)";

    public static final java.lang.String QUERY_PAGE_SIZE = "page_size";

    public static final java.lang.String QUERY_PAGE_SIZE_DESCRIPTION = "The number of resources to be returned for the paged response.";

    public static final java.lang.String DEFAULT_PAGE_SIZE = "10";

    public static final java.lang.String QUERY_FROM = "from";

    public static final java.lang.String QUERY_FROM_DESCRIPTION = "The starting page resource (inclusive).  \"start\" is also accepted.";

    public static final java.lang.String QUERY_FROM_VALUES = "range[0, infinity]";

    public static final java.lang.String DEFAULT_FROM = "0";

    public static final java.lang.String QUERY_TO = "to";

    public static final java.lang.String QUERY_TO_DESCRIPTION = "The ending page resource (inclusive).  \"end\" is also accepted.";

    public static final java.lang.String QUERY_TO_TYPE = "integer";

    public static final java.lang.String QUERY_TO_VALUES = "range[1, infinity]";

    public static final java.lang.String QUERY_PREDICATE = "{predicate}";

    public static final java.lang.String QUERY_PREDICATE_DESCRIPTION = "The predicate to filter resources by. Omitting the predicate will " + "match all resources.";

    public static final java.lang.String RESPONSE_CONTAINER_LIST = "List";

    public static final java.lang.String DATA_TYPE_INT = "integer";

    public static final java.lang.String DATA_TYPE_STRING = "string";

    public static final java.lang.String PARAM_TYPE_QUERY = "query";

    public static final java.lang.String PARAM_TYPE_BODY = "body";

    public static final java.lang.String FIELDS_SEPARATOR = ", ";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.BaseService.class);

    private org.apache.ambari.server.api.resources.ResourceInstanceFactory m_resourceFactory = new org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl();

    private org.apache.ambari.server.api.services.serializers.ResultSerializer m_serializer = new org.apache.ambari.server.api.services.serializers.JsonSerializer();

    protected static org.apache.ambari.server.audit.request.RequestAuditLogger requestAuditLogger;

    public static void init(org.apache.ambari.server.audit.request.RequestAuditLogger instance) {
        org.apache.ambari.server.api.services.BaseService.requestAuditLogger = instance;
    }

    protected javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, java.lang.String body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.services.Request.Type requestType, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        return handleRequest(headers, body, uriInfo, requestType, null, resource);
    }

    protected javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, java.lang.String body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.services.Request.Type requestType, javax.ws.rs.core.MediaType mediaType, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        org.apache.ambari.server.api.services.RequestBody rb = new org.apache.ambari.server.api.services.RequestBody();
        rb.setBody(body);
        org.apache.ambari.server.api.services.Request request = getRequestFactory().createRequest(headers, rb, uriInfo, requestType, resource);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        try {
            java.util.Set<org.apache.ambari.server.api.services.RequestBody> requestBodySet = getBodyParser().parse(body);
            java.util.Iterator<org.apache.ambari.server.api.services.RequestBody> iterator = requestBodySet.iterator();
            while (iterator.hasNext() && result.getStatus().getStatus().equals(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK)) {
                org.apache.ambari.server.api.services.RequestBody requestBody = iterator.next();
                request = getRequestFactory().createRequest(headers, requestBody, uriInfo, requestType, resource);
                result = request.process();
                if (org.apache.ambari.server.api.services.ResultStatus.STATUS.OK.equals(result.getStatus().getStatus())) {
                    org.apache.ambari.server.api.services.BaseService.requestAuditLogger.log(request, result);
                }
            } 
            if (requestBodySet.isEmpty() || (!org.apache.ambari.server.api.services.ResultStatus.STATUS.OK.equals(result.getStatus().getStatus()))) {
                org.apache.ambari.server.api.services.BaseService.requestAuditLogger.log(request, result);
            }
        } catch (org.apache.ambari.server.api.services.parsers.BodyParseException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e.getMessage()));
            org.apache.ambari.server.api.services.BaseService.LOG.error("Bad request received: " + e.getMessage());
            org.apache.ambari.server.api.services.BaseService.requestAuditLogger.log(request, result);
        } catch (java.lang.Throwable t) {
            org.apache.ambari.server.api.services.BaseService.requestAuditLogger.log(request, new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, t.getMessage())));
            throw t;
        }
        org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = (mediaType == null) ? getResultSerializer() : getResultSerializer(mediaType);
        javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.status(result.getStatus().getStatusCode()).entity(serializer.serialize(result));
        if (mediaType != null) {
            builder.type(mediaType);
        }
        org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
        return builder.build();
    }

    org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
        return new org.apache.ambari.server.api.services.RequestFactory();
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createResource(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds) {
        return m_resourceFactory.createResource(type, mapIds);
    }

    protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer(final javax.ws.rs.core.MediaType mediaType) {
        final org.apache.ambari.server.api.services.serializers.ResultSerializer serializer = getResultSerializer();
        if (mediaType.equals(MediaType.TEXT_PLAIN_TYPE)) {
            return new org.apache.ambari.server.api.services.serializers.ResultSerializer() {
                @java.lang.Override
                public java.lang.Object serialize(org.apache.ambari.server.api.services.Result result) {
                    return serializer.serialize(result).toString();
                }

                @java.lang.Override
                public java.lang.Object serializeError(org.apache.ambari.server.api.services.ResultStatus error) {
                    return serializer.serializeError(error).toString();
                }
            };
        } else if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)) {
            return new org.apache.ambari.server.api.services.serializers.ResultSerializer() {
                @java.lang.Override
                public java.lang.Object serialize(org.apache.ambari.server.api.services.Result result) {
                    return org.eclipse.jetty.util.ajax.JSON.parse(serializer.serialize(result).toString());
                }

                @java.lang.Override
                public java.lang.Object serializeError(org.apache.ambari.server.api.services.ResultStatus error) {
                    return org.eclipse.jetty.util.ajax.JSON.parse(serializer.serializeError(error).toString());
                }
            };
        } else if (mediaType.equals(org.apache.ambari.server.api.services.BaseService.MEDIA_TYPE_TEXT_CSV_TYPE)) {
            return new org.apache.ambari.server.api.services.serializers.CsvSerializer();
        }
        throw new java.lang.IllegalArgumentException(("The media type " + mediaType) + " is not supported.");
    }

    protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
        return m_serializer;
    }

    protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
        return new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
    }
}