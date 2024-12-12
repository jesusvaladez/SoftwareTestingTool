package org.apache.ambari.server.api.handlers;
public class ReadHandler implements org.apache.ambari.server.api.handlers.RequestHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.handlers.ReadHandler.class);

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result handleRequest(org.apache.ambari.server.api.services.Request request) {
        org.apache.ambari.server.api.query.Query query = request.getResource().getQuery();
        query.setPageRequest(request.getPageRequest());
        query.setSortRequest(request.getSortRequest());
        query.setRenderer(request.getRenderer());
        org.apache.ambari.server.api.services.RequestBody body = request.getBody();
        if (body != null) {
            query.setRequestInfoProps(body.getRequestInfoProperties());
        }
        try {
            addFieldsToQuery(request, query);
        } catch (java.lang.IllegalArgumentException e) {
            return new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e.getMessage()));
        }
        org.apache.ambari.server.api.services.Result result;
        org.apache.ambari.server.controller.spi.Predicate p = null;
        try {
            p = request.getQueryPredicate();
            query.setUserPredicate(p);
            result = query.execute();
            result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, e));
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            if (p == null) {
                result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e.getMessage()));
            } else {
                result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK, e));
                result.getResultTree().setProperty("isCollection", "true");
            }
        } catch (java.lang.IllegalArgumentException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, "Invalid Request: " + e.getMessage()));
            org.apache.ambari.server.api.handlers.ReadHandler.LOG.error("Bad request: ", e);
        } catch (java.lang.RuntimeException e) {
            if (org.apache.ambari.server.api.handlers.ReadHandler.LOG.isErrorEnabled()) {
                org.apache.ambari.server.api.handlers.ReadHandler.LOG.error("Caught a runtime exception executing a query", e);
            }
            throw e;
        }
        return result;
    }

    private void addFieldsToQuery(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.query.Query query) {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> entry : request.getFields().entrySet()) {
            java.lang.String propertyId = entry.getKey();
            query.addProperty(propertyId, entry.getValue());
        }
    }
}