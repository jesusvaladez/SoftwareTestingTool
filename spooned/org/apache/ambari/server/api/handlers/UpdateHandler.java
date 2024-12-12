package org.apache.ambari.server.api.handlers;
public class UpdateHandler extends org.apache.ambari.server.api.handlers.BaseManagementHandler {
    @java.lang.Override
    protected org.apache.ambari.server.api.services.Result persist(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody body) {
        org.apache.ambari.server.api.services.Result result;
        try {
            org.apache.ambari.server.controller.spi.RequestStatus status = getPersistenceManager().update(resource, body);
            result = createResult(status);
            if (result.isSynchronous()) {
                result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
            } else {
                result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.ACCEPTED));
            }
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN, e.getMessage()));
        } catch (org.apache.ambari.server.controller.spi.UnsupportedPropertyException | java.lang.IllegalArgumentException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e));
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e));
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            if (resource.isCollectionResource()) {
                result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK, e));
            } else {
                result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e));
            }
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR, e));
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