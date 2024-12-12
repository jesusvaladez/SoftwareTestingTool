package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public abstract class BaseRequest implements org.apache.ambari.server.api.services.Request {
    private javax.ws.rs.core.UriInfo m_uriInfo;

    private javax.ws.rs.core.HttpHeaders m_headers;

    private org.apache.ambari.server.api.services.RequestBody m_body;

    private java.lang.String m_remoteAddress;

    private org.apache.ambari.server.controller.spi.Predicate m_predicate;

    private org.apache.ambari.server.api.resources.ResourceInstance m_resource;

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final java.lang.String PAGE_SIZE_PROPERTY_KEY = "Request_Info/max_results";

    public static final java.lang.String ASC_ORDER_PROPERTY_KEY = "Request_Info/asc_order";

    private org.apache.ambari.server.api.query.render.Renderer m_renderer;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.Request.class);

    public BaseRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        m_headers = headers;
        m_uriInfo = uriInfo;
        m_resource = resource;
        m_body = body;
        m_remoteAddress = org.apache.ambari.server.utils.RequestUtils.getRemoteAddress();
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result process() {
        if (org.apache.ambari.server.api.services.BaseRequest.LOG.isDebugEnabled()) {
            org.apache.ambari.server.api.services.BaseRequest.LOG.debug("Handling API Request: '{}'", getURI());
        }
        org.apache.ambari.server.api.services.Result result;
        try {
            parseRenderer();
            parseQueryPredicate();
            result = getRequestHandler().handleRequest(this);
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
            java.lang.String message = "Unable to compile query predicate: " + e.getMessage();
            org.apache.ambari.server.api.services.BaseRequest.LOG.error(message, e);
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, message));
        } catch (java.lang.IllegalArgumentException e) {
            java.lang.String message = "Invalid Request: " + e.getMessage();
            org.apache.ambari.server.api.services.BaseRequest.LOG.error(message, e);
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, message));
        }
        if (!result.getStatus().isErrorState()) {
            getResultPostProcessor().process(result);
        }
        return result;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.resources.ResourceInstance getResource() {
        return m_resource;
    }

    @java.lang.Override
    public java.lang.String getURI() {
        return m_uriInfo.getRequestUri().toASCIIString();
    }

    @java.lang.Override
    public int getAPIVersion() {
        return 1;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate getQueryPredicate() {
        return m_predicate;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> getFields() {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapProperties;
        java.lang.String partialResponseFields = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FIELDS);
        if (partialResponseFields == null) {
            mapProperties = java.util.Collections.emptyMap();
        } else {
            java.util.Set<java.lang.String> setMatches = new java.util.HashSet<>();
            java.util.regex.Pattern re = java.util.regex.Pattern.compile("[^,\\[]*?\\[[^\\]]*?\\]|[^,]+");
            java.util.regex.Matcher m = re.matcher(partialResponseFields);
            while (m.find()) {
                for (int groupIdx = 0; groupIdx < (m.groupCount() + 1); groupIdx++) {
                    setMatches.add(m.group(groupIdx));
                }
            } 
            mapProperties = new java.util.HashMap<>(setMatches.size());
            for (java.lang.String field : setMatches) {
                org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = null;
                if (field.contains("[")) {
                    java.lang.String[] temporalData = field.substring(field.indexOf('[') + 1, field.indexOf(']')).split(",");
                    field = field.substring(0, field.indexOf('['));
                    long start = java.lang.Long.parseLong(temporalData[0].trim());
                    long end = -1;
                    long step = -1;
                    if (temporalData.length >= 2) {
                        end = java.lang.Long.parseLong(temporalData[1].trim());
                        if (temporalData.length == 3) {
                            step = java.lang.Long.parseLong(temporalData[2].trim());
                        }
                    }
                    temporalInfo = new org.apache.ambari.server.controller.internal.TemporalInfoImpl(start, end, step);
                }
                mapProperties.put(field, temporalInfo);
            }
        }
        return mapProperties;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer() {
        return m_renderer;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHttpHeaders() {
        return m_headers.getRequestHeaders();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.PageRequest getPageRequest() {
        java.lang.String pageSize = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_PAGE_SIZE);
        java.lang.String from = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FROM);
        java.lang.String to = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_TO);
        if (((pageSize == null) && (from == null)) && (to == null)) {
            return null;
        }
        int offset = 0;
        org.apache.ambari.server.controller.spi.PageRequest.StartingPoint startingPoint;
        if (from != null) {
            if (from.equals("start")) {
                startingPoint = org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning;
            } else {
                offset = java.lang.Integer.parseInt(from);
                startingPoint = org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetStart;
            }
        } else if (to != null) {
            if (to.equals("end")) {
                startingPoint = org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.End;
            } else {
                offset = java.lang.Integer.parseInt(to);
                startingPoint = org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetEnd;
            }
        } else {
            startingPoint = org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning;
        }
        return new org.apache.ambari.server.controller.internal.PageRequestImpl(startingPoint, pageSize == null ? org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE : java.lang.Integer.parseInt(pageSize), offset, null, null);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.RequestBody getBody() {
        return m_body;
    }

    protected org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor() {
        return m_renderer.getResultPostProcessor(this);
    }

    protected org.apache.ambari.server.api.predicate.PredicateCompiler getPredicateCompiler() {
        return new org.apache.ambari.server.api.predicate.PredicateCompiler();
    }

    private boolean isMinimal() {
        java.lang.String minimal = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL);
        return (minimal != null) && minimal.equalsIgnoreCase("true");
    }

    private void parseQueryPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.lang.String queryString = m_body.getQueryString();
        if (queryString == null) {
            java.lang.String uri = getURI();
            int qsBegin = uri.indexOf("?");
            queryString = (qsBegin == (-1)) ? null : uri.substring(qsBegin + 1);
        }
        if (queryString != null) {
            try {
                java.util.Collection<java.lang.String> ignoredProperties = null;
                switch (getRequestType()) {
                    case PUT :
                        ignoredProperties = m_resource.getResourceDefinition().getUpdateDirectives();
                        break;
                    case POST :
                        ignoredProperties = m_resource.getResourceDefinition().getCreateDirectives();
                        break;
                    case GET :
                        ignoredProperties = m_resource.getResourceDefinition().getReadDirectives();
                        break;
                    case DELETE :
                        ignoredProperties = m_resource.getResourceDefinition().getDeleteDirectives();
                        break;
                    default :
                        break;
                }
                m_predicate = (ignoredProperties == null) ? getPredicateCompiler().compile(java.net.URLDecoder.decode(queryString, "UTF-8")) : getPredicateCompiler().compile(java.net.URLDecoder.decode(queryString, "UTF-8"), ignoredProperties);
            } catch (java.io.UnsupportedEncodingException e) {
                throw new java.lang.RuntimeException("Unable to decode URI: " + e, e);
            }
        }
    }

    private void parseRenderer() {
        java.lang.String rendererName = (isMinimal()) ? "minimal" : m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT);
        m_renderer = m_resource.getResourceDefinition().getRenderer(rendererName);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.SortRequest getSortRequest() {
        java.lang.String sortByParams = m_uriInfo.getQueryParameters().getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_SORT);
        if ((sortByParams != null) && (!sortByParams.isEmpty())) {
            java.lang.String[] params = sortByParams.split(",");
            java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> properties = new java.util.ArrayList<>();
            if (params.length > 0) {
                for (java.lang.String property : params) {
                    org.apache.ambari.server.controller.spi.SortRequest.Order order = org.apache.ambari.server.controller.spi.SortRequest.Order.ASC;
                    java.lang.String propertyId = property;
                    int idx = property.indexOf(".");
                    if (idx != (-1)) {
                        order = org.apache.ambari.server.controller.spi.SortRequest.Order.valueOf(property.substring(idx + 1).toUpperCase());
                        propertyId = property.substring(0, idx);
                    }
                    properties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(propertyId, order));
                }
            }
            return new org.apache.ambari.server.controller.internal.SortRequestImpl(properties);
        }
        return null;
    }

    protected abstract org.apache.ambari.server.api.handlers.RequestHandler getRequestHandler();

    @java.lang.Override
    public java.lang.String getRemoteAddress() {
        return m_remoteAddress;
    }
}