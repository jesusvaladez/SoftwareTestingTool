package org.apache.ambari.server.api.handlers;
public class QueryCreateHandler extends org.apache.ambari.server.api.handlers.BaseManagementHandler {
    private org.apache.ambari.server.api.handlers.RequestHandler m_readHandler = new org.apache.ambari.server.api.handlers.ReadHandler();

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result handleRequest(org.apache.ambari.server.api.services.Request request) {
        org.apache.ambari.server.api.services.Result queryResult = getReadHandler().handleRequest(request);
        if (queryResult.getStatus().isErrorState() || queryResult.getResultTree().getChildren().isEmpty()) {
            return queryResult;
        }
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> mapProperties;
        try {
            mapProperties = buildCreateSet(request, queryResult);
        } catch (java.lang.IllegalArgumentException e) {
            return createInvalidRequestResult(e.getMessage());
        }
        if (mapProperties.size() != 1) {
            return createInvalidRequestResult(mapProperties.size() == 0 ? "A minimum of one sub-resource must be specified for creation." : "Multiple sub-resource types may not be created in the same request.");
        }
        final java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> entry = mapProperties.entrySet().iterator().next();
        org.apache.ambari.server.api.resources.ResourceInstance createResource = getResourceFactory().createResource(entry.getKey(), request.getResource().getKeyValueMap());
        org.apache.ambari.server.api.services.RequestBody requestBody = new org.apache.ambari.server.api.services.RequestBody();
        requestBody.setBody(request.getBody().getBody());
        for (java.util.Map<java.lang.String, java.lang.Object> map : entry.getValue()) {
            requestBody.addPropertySet(new org.apache.ambari.server.api.services.NamedPropertySet("", map));
        }
        return persist(createResource, requestBody);
    }

    private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> buildCreateSet(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result queryResult) throws java.lang.IllegalArgumentException {
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setRequestProps = request.getBody().getNamedPropertySets();
        java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> mapProps = new java.util.HashMap<>();
        org.apache.ambari.server.api.resources.ResourceInstance resource = request.getResource();
        org.apache.ambari.server.controller.spi.Resource.Type type = resource.getResourceDefinition().getType();
        org.apache.ambari.server.controller.spi.ClusterController controller = getClusterController();
        java.lang.String resourceKeyProperty = controller.getSchema(type).getKeyPropertyId(type);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = queryResult.getResultTree();
        java.util.Collection<org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource>> treeChildren = tree.getChildren();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : treeChildren) {
            org.apache.ambari.server.controller.spi.Resource r = node.getObject();
            java.lang.Object keyVal = r.getPropertyValue(resourceKeyProperty);
            for (org.apache.ambari.server.api.services.NamedPropertySet namedProps : setRequestProps) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : namedProps.getProperties().entrySet()) {
                    java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> set = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (entry.getValue()));
                    for (java.util.Map<java.lang.String, java.lang.Object> map : set) {
                        java.util.Map<java.lang.String, java.lang.Object> mapResourceProps = new java.util.HashMap<>(map);
                        org.apache.ambari.server.controller.spi.Resource.Type createType = getCreateType(resource, entry.getKey());
                        mapResourceProps.put(controller.getSchema(createType).getKeyPropertyId(resource.getResourceDefinition().getType()), keyVal);
                        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setCreateProps = mapProps.get(createType);
                        if (setCreateProps == null) {
                            setCreateProps = new java.util.HashSet<>();
                            mapProps.put(createType, setCreateProps);
                        }
                        setCreateProps.add(mapResourceProps);
                    }
                }
            }
        }
        return mapProps;
    }

    private org.apache.ambari.server.controller.spi.Resource.Type getCreateType(org.apache.ambari.server.api.resources.ResourceInstance resource, java.lang.String subResourceName) throws java.lang.IllegalArgumentException {
        if ((subResourceName == null) || subResourceName.equals("")) {
            throw new java.lang.IllegalArgumentException("A sub-resource name must be supplied.");
        }
        org.apache.ambari.server.api.resources.ResourceInstance res = resource.getSubResources().get(subResourceName);
        if (res == null) {
            throw new java.lang.IllegalArgumentException(("The specified sub-resource name is not valid: '" + subResourceName) + "'.");
        }
        return res.getResourceDefinition().getType();
    }

    private org.apache.ambari.server.api.services.Result createInvalidRequestResult(java.lang.String msg) {
        return new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, "Invalid Request: " + msg));
    }

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
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, e));
        } catch (org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.CONFLICT, e));
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, e));
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
        throw new java.lang.UnsupportedOperationException();
    }

    protected org.apache.ambari.server.api.resources.ResourceInstanceFactory getResourceFactory() {
        return new org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl();
    }

    protected org.apache.ambari.server.api.handlers.RequestHandler getReadHandler() {
        return m_readHandler;
    }
}