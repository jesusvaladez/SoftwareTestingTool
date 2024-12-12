package org.apache.ambari.server.api.handlers;
public class CreateHandler extends org.apache.ambari.server.api.handlers.BaseManagementHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.handlers.CreateHandler.class);

    @java.lang.Override
    protected org.apache.ambari.server.api.services.Result persist(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody body) {
        org.apache.ambari.server.api.services.Result result;
        try {
            org.apache.ambari.server.controller.spi.RequestStatus status = getPersistenceManager().create(resource, body);
            result = createResult(status);
            if (result.isSynchronous()) {
                if (resource.getResourceDefinition().isCreatable()) {
                    result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.CREATED));
                } else {
                    result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
                }
            } else {
                result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.ACCEPTED));
            }
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e.getMessage()));
            org.apache.ambari.server.api.handlers.CreateHandler.LOG.error("Bad request received: " + e.getMessage());
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            if (org.apache.ambari.server.api.handlers.CreateHandler.LOG.isErrorEnabled()) {
                org.apache.ambari.server.api.handlers.CreateHandler.LOG.error("Caught a system exception while attempting to create a resource: {}", e.getMessage(), e);
            }
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.CONFLICT, e.getMessage()));
        } catch (java.lang.IllegalArgumentException e) {
            org.apache.ambari.server.api.handlers.CreateHandler.LOG.error("Bad request received: " + e.getMessage(), e);
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e.getMessage()));
        } catch (java.lang.RuntimeException e) {
            if (org.apache.ambari.server.api.handlers.CreateHandler.LOG.isErrorEnabled()) {
                org.apache.ambari.server.api.handlers.CreateHandler.LOG.error("Caught a runtime exception while attempting to create a resource: {}", e.getMessage(), e);
            }
            throw e;
        }
        return result;
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.services.ResultMetadata convert(org.apache.ambari.server.controller.spi.RequestStatusMetaData requestStatusMetaData) {
        if (requestStatusMetaData == null) {
            return null;
        }
        if (requestStatusMetaData.getClass() == org.apache.ambari.server.controller.internal.OperationStatusMetaData.class) {
            return ((org.apache.ambari.server.controller.internal.OperationStatusMetaData) (requestStatusMetaData));
        } else {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("RequestStatusDetails is of an expected type: %s", requestStatusMetaData.getClass().getName()));
        }
    }
}