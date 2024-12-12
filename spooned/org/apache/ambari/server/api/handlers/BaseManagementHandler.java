package org.apache.ambari.server.api.handlers;
public abstract class BaseManagementHandler implements org.apache.ambari.server.api.handlers.RequestHandler {
    public static final java.lang.String RESOURCES_NODE_NAME = "resources";

    org.apache.ambari.server.api.services.persistence.PersistenceManager m_pm = new org.apache.ambari.server.api.services.persistence.PersistenceManagerImpl(getClusterController());

    protected BaseManagementHandler() {
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result handleRequest(org.apache.ambari.server.api.services.Request request) {
        org.apache.ambari.server.api.query.Query query = request.getResource().getQuery();
        org.apache.ambari.server.controller.spi.Predicate queryPredicate = request.getQueryPredicate();
        query.setRenderer(request.getRenderer());
        if (queryPredicate != null) {
            query.setUserPredicate(queryPredicate);
        }
        return persist(request.getResource(), request.getBody());
    }

    protected org.apache.ambari.server.api.services.Result createResult(org.apache.ambari.server.controller.spi.RequestStatus requestStatus) {
        boolean isSynchronous = requestStatus.getStatus() == org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete;
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(isSynchronous);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        if (!isSynchronous) {
            tree.addChild(requestStatus.getRequestResource(), "request");
        }
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResources = requestStatus.getAssociatedResources();
        if (!setResources.isEmpty()) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resourcesNode = tree.addChild(null, org.apache.ambari.server.api.handlers.BaseManagementHandler.RESOURCES_NODE_NAME);
            resourcesNode.setProperty("isCollection", "true");
            int count = 1;
            for (org.apache.ambari.server.controller.spi.Resource resource : setResources) {
                resourcesNode.addChild(resource, (resource.getType() + ":") + (count++));
            }
        }
        result.setResultMetadata(convert(requestStatus.getStatusMetadata()));
        return result;
    }

    protected org.apache.ambari.server.controller.spi.ClusterController getClusterController() {
        return org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
    }

    protected org.apache.ambari.server.api.services.persistence.PersistenceManager getPersistenceManager() {
        return m_pm;
    }

    protected abstract org.apache.ambari.server.api.services.Result persist(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody body);

    protected abstract org.apache.ambari.server.api.services.ResultMetadata convert(org.apache.ambari.server.controller.spi.RequestStatusMetaData requestStatusMetaData);
}