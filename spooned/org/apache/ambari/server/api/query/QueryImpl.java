package org.apache.ambari.server.api.query;
public class QueryImpl implements org.apache.ambari.server.api.query.Query , org.apache.ambari.server.api.resources.ResourceInstance {
    private final org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition;

    private final org.apache.ambari.server.controller.spi.ClusterController clusterController;

    private final java.util.Set<java.lang.String> requestedProperties = new java.util.HashSet<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();

    java.util.Map<org.apache.ambari.server.controller.spi.Resource, org.apache.ambari.server.api.query.QueryImpl.QueryResult> queryResults = new java.util.LinkedHashMap<>();

    java.util.Map<org.apache.ambari.server.controller.spi.Resource, org.apache.ambari.server.api.query.QueryImpl.QueryResult> populatedQueryResults = new java.util.LinkedHashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> requestedSubResources = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> availableSubResources;

    private boolean allProperties = false;

    private org.apache.ambari.server.controller.spi.Predicate userPredicate;

    private org.apache.ambari.server.controller.spi.PageRequest pageRequest;

    private org.apache.ambari.server.controller.spi.SortRequest sortRequest;

    private final java.util.Set<java.lang.String> subResourcePredicateProperties = new java.util.HashSet<>();

    private org.apache.ambari.server.api.query.render.Renderer renderer;

    private org.apache.ambari.server.controller.spi.Predicate subResourcePredicate;

    private org.apache.ambari.server.controller.spi.Predicate processedPredicate;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.query.QueryImpl.class);

    public QueryImpl(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap, org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition, org.apache.ambari.server.controller.spi.ClusterController clusterController) {
        this.resourceDefinition = resourceDefinition;
        this.clusterController = clusterController;
        setKeyValueMap(keyValueMap);
    }

    @java.lang.Override
    public void addProperty(java.lang.String propertyId, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        if (propertyId.equals("*")) {
            addAllProperties(temporalInfo);
        } else if (!addPropertyToSubResource(propertyId, temporalInfo)) {
            if (propertyId.endsWith("/*")) {
                propertyId = propertyId.substring(0, propertyId.length() - 2);
            }
            addLocalProperty(propertyId);
            if (temporalInfo != null) {
                temporalInfoMap.put(propertyId, temporalInfo);
            }
        }
    }

    @java.lang.Override
    public void addLocalProperty(java.lang.String property) {
        requestedProperties.add(property);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result execute() throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        queryForResources();
        return getResult(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return createPredicate();
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getProperties() {
        return java.util.Collections.unmodifiableSet(requestedProperties);
    }

    @java.lang.Override
    public void setUserPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        userPredicate = predicate;
    }

    @java.lang.Override
    public void setPageRequest(org.apache.ambari.server.controller.spi.PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    @java.lang.Override
    public void setSortRequest(org.apache.ambari.server.controller.spi.SortRequest sortRequest) {
        this.sortRequest = sortRequest;
    }

    @java.lang.Override
    public void setRenderer(org.apache.ambari.server.api.query.render.Renderer renderer) {
        this.renderer = renderer;
        renderer.init(clusterController);
    }

    @java.lang.Override
    public void setRequestInfoProps(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        if (requestInfoProperties != null) {
            this.requestInfoProperties.putAll(requestInfoProperties);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getRequestInfoProps() {
        return this.requestInfoProperties;
    }

    @java.lang.Override
    public void setKeyValueMap(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap) {
        this.keyValueMap.putAll(keyValueMap);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyValueMap() {
        return new java.util.HashMap<>(keyValueMap);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.Query getQuery() {
        return this;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.resources.ResourceDefinition getResourceDefinition() {
        return resourceDefinition;
    }

    @java.lang.Override
    public boolean isCollectionResource() {
        return getKeyValueMap().get(getResourceDefinition().getType()) == null;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.api.resources.ResourceInstance> getSubResources() {
        return new java.util.HashMap<>(ensureSubResources());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.api.query.QueryImpl query = ((org.apache.ambari.server.api.query.QueryImpl) (o));
        return ((((clusterController.equals(query.clusterController) && (!(pageRequest != null ? !pageRequest.equals(query.pageRequest) : query.pageRequest != null))) && requestedProperties.equals(query.requestedProperties)) && resourceDefinition.equals(query.resourceDefinition)) && keyValueMap.equals(query.keyValueMap)) && (!(userPredicate != null ? !userPredicate.equals(query.userPredicate) : query.userPredicate != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = resourceDefinition.hashCode();
        result = (31 * result) + clusterController.hashCode();
        result = (31 * result) + requestedProperties.hashCode();
        result = (31 * result) + keyValueMap.hashCode();
        result = (31 * result) + (userPredicate != null ? userPredicate.hashCode() : 0);
        result = (31 * result) + (pageRequest != null ? pageRequest.hashCode() : 0);
        return result;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> ensureSubResources() {
        if (availableSubResources == null) {
            availableSubResources = new java.util.HashMap<>();
            java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setSubResourceDefs = getResourceDefinition().getSubResourceDefinitions();
            org.apache.ambari.server.controller.spi.ClusterController controller = clusterController;
            for (org.apache.ambari.server.api.resources.SubResourceDefinition subResDef : setSubResourceDefs) {
                org.apache.ambari.server.controller.spi.Resource.Type type = subResDef.getType();
                java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> valueMap = getKeyValueMap();
                org.apache.ambari.server.api.query.QueryImpl resource = new org.apache.ambari.server.api.query.QueryImpl(valueMap, org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(type, valueMap), controller);
                java.lang.String subResourceName = getSubResourceName(resource.getResourceDefinition(), subResDef);
                availableSubResources.put(subResourceName, resource);
            }
        }
        return availableSubResources;
    }

    private boolean populateResourceRequired(org.apache.ambari.server.controller.spi.Resource.Type type) {
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = clusterController.ensureResourceProvider(type);
        java.util.Set<java.lang.String> unsupportedProperties = resourceProvider.checkPropertyIds(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(processedPredicate));
        return (!unsupportedProperties.isEmpty()) || hasSubResourcePredicate();
    }

    private void queryForResources() throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.Resource.Type resourceType = getResourceDefinition().getType();
        org.apache.ambari.server.controller.spi.Predicate queryPredicate = createPredicate(getKeyValueMap(), processUserPredicate(userPredicate));
        finalizeProperties();
        org.apache.ambari.server.controller.spi.Request request = createRequest();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = new java.util.LinkedHashSet<>();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> providerResourceSet = new java.util.LinkedHashSet<>();
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = doQuery(resourceType, request, queryPredicate, true);
        if (((pageRequest != null) || (sortRequest != null)) && (!populateResourceRequired(resourceType))) {
            org.apache.ambari.server.controller.spi.PageResponse pageResponse = clusterController.getPage(resourceType, queryResponse, request, queryPredicate, pageRequest, sortRequest);
            for (org.apache.ambari.server.controller.spi.Resource r : pageResponse.getIterable()) {
                resourceSet.add(r);
                providerResourceSet.add(r);
            }
        } else {
            resourceSet.addAll(queryResponse.getResources());
            providerResourceSet.addAll(queryResponse.getResources());
        }
        populatedQueryResults.put(null, new org.apache.ambari.server.api.query.QueryImpl.QueryResult(request, queryPredicate, userPredicate, getKeyValueMap(), new org.apache.ambari.server.controller.internal.QueryResponseImpl(resourceSet)));
        queryResults.put(null, new org.apache.ambari.server.api.query.QueryImpl.QueryResult(request, queryPredicate, userPredicate, getKeyValueMap(), queryResponse));
        if (renderer.requiresPropertyProviderInput()) {
            clusterController.populateResources(resourceType, providerResourceSet, request, queryPredicate);
        }
        if ((((pageRequest != null) || (userPredicate != null)) && (!hasSubResourcePredicate())) && populateResourceRequired(resourceType)) {
            org.apache.ambari.server.controller.spi.QueryResponse newResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resourceSet, queryResponse.isSortedResponse(), queryResponse.isPagedResponse(), queryResponse.getTotalResourceCount());
            org.apache.ambari.server.controller.spi.PageResponse pageResponse = clusterController.getPage(resourceType, newResponse, request, queryPredicate, pageRequest, sortRequest);
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> newResourceSet = new java.util.LinkedHashSet<>();
            for (org.apache.ambari.server.controller.spi.Resource r : pageResponse.getIterable()) {
                newResourceSet.add(r);
            }
            populatedQueryResults.put(null, new org.apache.ambari.server.api.query.QueryImpl.QueryResult(request, queryPredicate, userPredicate, getKeyValueMap(), new org.apache.ambari.server.controller.internal.QueryResponseImpl(newResourceSet)));
        }
        queryForSubResources();
    }

    private void queryForSubResources() throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> entry : requestedSubResources.entrySet()) {
            org.apache.ambari.server.api.query.QueryImpl subResource = entry.getValue();
            org.apache.ambari.server.controller.spi.Resource.Type resourceType = subResource.getResourceDefinition().getType();
            org.apache.ambari.server.controller.spi.Request request = subResource.createRequest();
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> providerResourceSet = new java.util.HashSet<>();
            for (org.apache.ambari.server.api.query.QueryImpl.QueryResult queryResult : populatedQueryResults.values()) {
                for (org.apache.ambari.server.controller.spi.Resource resource : queryResult.getQueryResponse().getResources()) {
                    java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = getKeyValueMap(resource, queryResult.getKeyValueMap());
                    org.apache.ambari.server.controller.spi.Predicate queryPredicate = subResource.createPredicate(map, subResource.processedPredicate);
                    java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = new java.util.LinkedHashSet<>();
                    try {
                        java.util.Set<org.apache.ambari.server.controller.spi.Resource> queryResources = subResource.doQuery(resourceType, request, queryPredicate, false).getResources();
                        providerResourceSet.addAll(queryResources);
                        resourceSet.addAll(queryResources);
                    } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
                    } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
                        org.apache.ambari.server.api.query.QueryImpl.LOG.debug("User does not have authorization to get {} resources. The data will not be added to the response.", resourceType.name());
                    }
                    subResource.queryResults.put(resource, new org.apache.ambari.server.api.query.QueryImpl.QueryResult(request, queryPredicate, subResourcePredicate, map, new org.apache.ambari.server.controller.internal.QueryResponseImpl(resourceSet)));
                    subResource.populatedQueryResults.put(resource, new org.apache.ambari.server.api.query.QueryImpl.QueryResult(request, queryPredicate, subResourcePredicate, map, new org.apache.ambari.server.controller.internal.QueryResponseImpl(resourceSet)));
                }
            }
            if (renderer.requiresPropertyProviderInput()) {
                clusterController.populateResources(resourceType, providerResourceSet, request, subResourcePredicate);
            }
            subResource.queryForSubResources();
        }
    }

    private org.apache.ambari.server.controller.spi.QueryResponse doQuery(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, boolean checkEmptyResponse) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        if (org.apache.ambari.server.api.query.QueryImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.api.query.QueryImpl.LOG.debug("Executing resource query: {} where {}", request, predicate);
        }
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = clusterController.getResources(type, request, predicate);
        if (checkEmptyResponse && queryResponse.getResources().isEmpty()) {
            if (!isCollectionResource()) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(((("The requested resource doesn't exist: " + type) + " not found where ") + predicate) + ".");
            }
        }
        return queryResponse;
    }

    protected java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> getJoinedResourceProperties(java.util.Set<java.lang.String> propertyIds, org.apache.ambari.server.controller.spi.Resource parentResource, java.lang.String category) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> resourcePropertyMaps = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> categoryPropertyIdMap = getPropertyIdsForCategory(propertyIds, category);
        for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource, org.apache.ambari.server.api.query.QueryImpl.QueryResult> queryResultEntry : populatedQueryResults.entrySet()) {
            org.apache.ambari.server.api.query.QueryImpl.QueryResult queryResult = queryResultEntry.getValue();
            org.apache.ambari.server.controller.spi.Resource queryParentResource = queryResultEntry.getKey();
            if (queryParentResource == parentResource) {
                java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterResource = clusterController.getIterable(resourceDefinition.getType(), queryResult.getQueryResponse(), queryResult.getRequest(), queryResult.getPredicate(), null, null);
                for (org.apache.ambari.server.controller.spi.Resource resource : iterResource) {
                    java.util.Map<java.lang.String, java.lang.Object> resourcePropertyMap = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> categoryPropertyIdEntry : categoryPropertyIdMap.entrySet()) {
                        java.lang.Object value = resource.getPropertyValue(categoryPropertyIdEntry.getValue());
                        if (value != null) {
                            resourcePropertyMap.put(categoryPropertyIdEntry.getKey(), value);
                        }
                    }
                    java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = new java.util.HashSet<>();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> entry : requestedSubResources.entrySet()) {
                        java.lang.String subResourceCategory = (category == null) ? entry.getKey() : (category + "/") + entry.getKey();
                        org.apache.ambari.server.api.query.QueryImpl subResource = entry.getValue();
                        java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> subResourcePropertyMaps = subResource.getJoinedResourceProperties(propertyIds, resource, subResourceCategory);
                        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> combinedSubResourcePropertyMaps = new java.util.HashSet<>();
                        for (java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> maps : subResourcePropertyMaps.values()) {
                            combinedSubResourcePropertyMaps.addAll(maps);
                        }
                        propertyMaps = org.apache.ambari.server.api.query.QueryImpl.joinPropertyMaps(propertyMaps, combinedSubResourcePropertyMaps);
                    }
                    if (!resourcePropertyMap.isEmpty()) {
                        if (propertyMaps.isEmpty()) {
                            propertyMaps.add(resourcePropertyMap);
                        } else {
                            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                                propertyMap.putAll(resourcePropertyMap);
                            }
                        }
                    }
                    resourcePropertyMaps.put(resource, propertyMaps);
                }
            }
        }
        return resourcePropertyMaps;
    }

    private void finalizeProperties() {
        org.apache.ambari.server.api.resources.ResourceDefinition rootDefinition = resourceDefinition;
        org.apache.ambari.server.api.query.QueryInfo rootQueryInfo = new org.apache.ambari.server.api.query.QueryInfo(rootDefinition, requestedProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> rootNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQueryInfo, rootDefinition.getType().name());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> requestedPropertyTree = buildQueryPropertyTree(this, rootNode);
        mergeFinalizedProperties(renderer.finalizeProperties(requestedPropertyTree, isCollectionResource()), this);
    }

    private org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> buildQueryPropertyTree(org.apache.ambari.server.api.query.QueryImpl query, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> node) {
        for (org.apache.ambari.server.api.query.QueryImpl subQuery : query.requestedSubResources.values()) {
            org.apache.ambari.server.api.resources.ResourceDefinition childResource = subQuery.resourceDefinition;
            org.apache.ambari.server.api.query.QueryInfo queryInfo = new org.apache.ambari.server.api.query.QueryInfo(childResource, subQuery.requestedProperties);
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> childNode = node.addChild(queryInfo, childResource.getType().name());
            buildQueryPropertyTree(subQuery, childNode);
        }
        return node;
    }

    private void mergeFinalizedProperties(org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> node, org.apache.ambari.server.api.query.QueryImpl query) {
        java.util.Set<java.lang.String> finalizedProperties = node.getObject();
        query.requestedProperties.clear();
        query.requestedProperties.addAll(finalizedProperties);
        for (org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> child : node.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource.Type childType = org.apache.ambari.server.controller.spi.Resource.Type.valueOf(child.getName());
            org.apache.ambari.server.api.resources.ResourceDefinition parentResource = query.resourceDefinition;
            java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = parentResource.getSubResourceDefinitions();
            java.lang.String subResourceName = null;
            for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : subResources) {
                if (subResource.getType() == childType) {
                    org.apache.ambari.server.api.resources.ResourceDefinition resource = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(subResource.getType(), query.keyValueMap);
                    subResourceName = getSubResourceName(resource, subResource);
                    break;
                }
            }
            org.apache.ambari.server.api.query.QueryImpl subQuery = query.requestedSubResources.get(subResourceName);
            if (subQuery == null) {
                query.addProperty(subResourceName, null);
                subQuery = query.requestedSubResources.get(subResourceName);
            }
            mergeFinalizedProperties(child, subQuery);
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> getPropertyIdsForCategory(java.util.Set<java.lang.String> propertyIds, java.lang.String category) {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        for (java.lang.String propertyId : propertyIds) {
            if ((category == null) || propertyId.startsWith(category)) {
                map.put(propertyId, category == null ? propertyId : propertyId.substring(category.length() + 1));
            }
        }
        return map;
    }

    private static java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> joinPropertyMaps(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps1, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps2) {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = new java.util.HashSet<>();
        if (propertyMaps1.isEmpty()) {
            return propertyMaps2;
        }
        if (propertyMaps2.isEmpty()) {
            return propertyMaps1;
        }
        for (java.util.Map<java.lang.String, java.lang.Object> map1 : propertyMaps1) {
            for (java.util.Map<java.lang.String, java.lang.Object> map2 : propertyMaps2) {
                java.util.Map<java.lang.String, java.lang.Object> joinedMap = new java.util.HashMap<>(map1);
                joinedMap.putAll(map2);
                propertyMaps.add(joinedMap);
            }
        }
        return propertyMaps;
    }

    private org.apache.ambari.server.api.services.Result getResult(org.apache.ambari.server.controller.spi.Resource parentResource) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.controller.spi.Resource.Type resourceType = getResourceDefinition().getType();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        if (isCollectionResource()) {
            tree.setProperty("isCollection", "true");
        }
        org.apache.ambari.server.api.query.QueryImpl.QueryResult queryResult = queryResults.get(parentResource);
        if (queryResult != null) {
            org.apache.ambari.server.controller.spi.Predicate queryPredicate = queryResult.getPredicate();
            org.apache.ambari.server.controller.spi.Predicate queryUserPredicate = queryResult.getUserPredicate();
            org.apache.ambari.server.controller.spi.Request queryRequest = queryResult.getRequest();
            org.apache.ambari.server.controller.spi.QueryResponse queryResponse = queryResult.getQueryResponse();
            if (hasSubResourcePredicate() && (queryUserPredicate != null)) {
                queryPredicate = getExtendedPredicate(parentResource, queryUserPredicate);
            }
            java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterResource;
            if (pageRequest == null) {
                iterResource = clusterController.getIterable(resourceType, queryResponse, queryRequest, queryPredicate, null, sortRequest);
            } else {
                org.apache.ambari.server.controller.spi.PageResponse pageResponse = clusterController.getPage(resourceType, queryResponse, queryRequest, queryPredicate, pageRequest, sortRequest);
                iterResource = pageResponse.getIterable();
                tree.setProperty("count", pageResponse.getTotalResourceCount().toString());
            }
            int count = 1;
            for (org.apache.ambari.server.controller.spi.Resource resource : iterResource) {
                org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node = tree.addChild(resource, (resource.getType() + ":") + (count++));
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> entry : requestedSubResources.entrySet()) {
                    java.lang.String subResCategory = entry.getKey();
                    org.apache.ambari.server.api.query.QueryImpl subResource = entry.getValue();
                    org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> childResult = subResource.getResult(resource).getResultTree();
                    childResult.setName(subResCategory);
                    childResult.setProperty("isCollection", "false");
                    node.addChild(childResult);
                }
            }
        }
        return renderer.finalizeResult(result);
    }

    private boolean hasSubResourcePredicate() {
        return !subResourcePredicateProperties.isEmpty();
    }

    private org.apache.ambari.server.controller.spi.Predicate getExtendedPredicate(org.apache.ambari.server.controller.spi.Resource parentResource, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> joinedResources = getJoinedResourceProperties(subResourcePredicateProperties, parentResource, null);
        org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor visitor = new org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor(joinedResources);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        return visitor.getExtendedPredicate();
    }

    private void addAllProperties(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        allProperties = true;
        if (temporalInfo != null) {
            temporalInfoMap.put(null, temporalInfo);
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> entry : ensureSubResources().entrySet()) {
            java.lang.String name = entry.getKey();
            if (!requestedSubResources.containsKey(name)) {
                addSubResource(name, entry.getValue());
            }
        }
    }

    private boolean addPropertyToSubResource(java.lang.String propertyId, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        int index = propertyId.indexOf("/");
        java.lang.String category = (index == (-1)) ? propertyId : propertyId.substring(0, index);
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> subResources = ensureSubResources();
        org.apache.ambari.server.api.query.QueryImpl subResource = subResources.get(category);
        if (subResource != null) {
            addSubResource(category, subResource);
            if (index != (-1)) {
                subResource.addProperty(propertyId.substring(index + 1), temporalInfo);
            }
            return true;
        }
        return false;
    }

    private org.apache.ambari.server.controller.spi.Predicate createInternalPredicate(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds) {
        org.apache.ambari.server.controller.spi.Resource.Type resourceType = getResourceDefinition().getType();
        org.apache.ambari.server.controller.spi.Schema schema = clusterController.getSchema(resourceType);
        java.util.Set<org.apache.ambari.server.controller.spi.Predicate> setPredicates = new java.util.HashSet<>();
        for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> entry : mapResourceIds.entrySet()) {
            if (entry.getValue() != null) {
                java.lang.String keyPropertyId = schema.getKeyPropertyId(entry.getKey());
                if (keyPropertyId != null) {
                    setPredicates.add(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(keyPropertyId, entry.getValue()));
                }
            }
        }
        org.apache.ambari.server.controller.spi.Predicate p = null;
        if (setPredicates.size() == 1) {
            p = setPredicates.iterator().next();
        } else if (setPredicates.size() > 1) {
            p = new org.apache.ambari.server.controller.predicate.AndPredicate(setPredicates.toArray(new org.apache.ambari.server.controller.spi.Predicate[setPredicates.size()]));
        } else {
            return null;
        }
        org.apache.ambari.server.controller.spi.Resource.Type type = getResourceDefinition().getType();
        org.apache.ambari.server.controller.spi.Predicate override = clusterController.getAmendedPredicate(type, p);
        if (null != override) {
            p = override;
        }
        return p;
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate() {
        return createPredicate(getKeyValueMap(), userPredicate);
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap, org.apache.ambari.server.controller.spi.Predicate predicate) {
        org.apache.ambari.server.controller.spi.Predicate internalPredicate = createInternalPredicate(keyValueMap);
        if (internalPredicate == null) {
            return predicate;
        }
        return predicate == null ? internalPredicate : new org.apache.ambari.server.controller.predicate.AndPredicate(predicate, internalPredicate);
    }

    private org.apache.ambari.server.controller.spi.Predicate getSubResourcePredicate(org.apache.ambari.server.controller.spi.Predicate predicate, java.lang.String category) {
        if (predicate == null) {
            return null;
        }
        org.apache.ambari.server.api.query.SubResourcePredicateVisitor visitor = new org.apache.ambari.server.api.query.SubResourcePredicateVisitor(category);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        return visitor.getSubResourcePredicate();
    }

    private org.apache.ambari.server.controller.spi.Predicate processUserPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        if (predicate == null) {
            return null;
        }
        org.apache.ambari.server.api.query.ProcessingPredicateVisitor visitor = new org.apache.ambari.server.api.query.ProcessingPredicateVisitor(this);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        java.util.Set<java.lang.String> categories = visitor.getSubResourceCategories();
        for (java.lang.String category : categories) {
            addPropertyToSubResource(category, null);
        }
        subResourcePredicateProperties.addAll(visitor.getSubResourceProperties());
        if (hasSubResourcePredicate()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> entry : requestedSubResources.entrySet()) {
                subResourcePredicate = getSubResourcePredicate(predicate, entry.getKey());
                entry.getValue().processUserPredicate(subResourcePredicate);
            }
        }
        processedPredicate = visitor.getProcessedPredicate();
        return processedPredicate;
    }

    private org.apache.ambari.server.controller.spi.Request createRequest() {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>(this.requestInfoProperties);
        if (pageRequest != null) {
            requestInfoProperties.put(org.apache.ambari.server.api.services.BaseRequest.PAGE_SIZE_PROPERTY_KEY, java.lang.Integer.toString(pageRequest.getPageSize() + pageRequest.getOffset()));
            requestInfoProperties.put(org.apache.ambari.server.api.services.BaseRequest.ASC_ORDER_PROPERTY_KEY, java.lang.Boolean.toString((pageRequest.getStartingPoint() == org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning) || (pageRequest.getStartingPoint() == org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.OffsetStart)));
        }
        if (allProperties) {
            return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet(), requestInfoProperties, null, pageRequest, sortRequest);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapTemporalInfo = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.TemporalInfo globalTemporalInfo = temporalInfoMap.get(null);
        java.util.Set<java.lang.String> setProperties = new java.util.HashSet<>();
        setProperties.addAll(requestedProperties);
        for (java.lang.String propertyId : setProperties) {
            org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = temporalInfoMap.get(propertyId);
            if (temporalInfo != null) {
                mapTemporalInfo.put(propertyId, temporalInfo);
            } else if (globalTemporalInfo != null) {
                mapTemporalInfo.put(propertyId, globalTemporalInfo);
            }
        }
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(setProperties, requestInfoProperties, mapTemporalInfo, pageRequest, sortRequest);
    }

    private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyValueMap(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resourceKeyValueMap = new java.util.HashMap<>(keyValueMap.size());
        for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resourceIdEntry : keyValueMap.entrySet()) {
            org.apache.ambari.server.controller.spi.Resource.Type type = resourceIdEntry.getKey();
            java.lang.String value = resourceIdEntry.getValue();
            if (value == null) {
                java.lang.Object o = resource.getPropertyValue(clusterController.getSchema(type).getKeyPropertyId(type));
                value = (o == null) ? null : o.toString();
            }
            if (value != null) {
                resourceKeyValueMap.put(type, value);
            }
        }
        org.apache.ambari.server.controller.spi.Schema schema = clusterController.getSchema(resource.getType());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> types = schema.getKeyTypes();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : types) {
            java.lang.String resourceKeyProp = schema.getKeyPropertyId(type);
            java.lang.Object resourceValue = resource.getPropertyValue(resourceKeyProp);
            if (null != resourceValue) {
                resourceKeyValueMap.put(type, resourceValue.toString());
            }
        }
        return resourceKeyValueMap;
    }

    private void addSubResource(java.lang.String name, org.apache.ambari.server.api.query.QueryImpl query) {
        query.setRenderer(new org.apache.ambari.server.api.query.render.DefaultRenderer());
        requestedSubResources.put(name, query);
    }

    private java.lang.String getSubResourceName(org.apache.ambari.server.api.resources.ResourceDefinition resource, org.apache.ambari.server.api.resources.SubResourceDefinition subResource) {
        return subResource.isCollection() ? resource.getPluralName() : resource.getSingularName();
    }

    private static class QueryResult {
        private final org.apache.ambari.server.controller.spi.Request request;

        private final org.apache.ambari.server.controller.spi.Predicate predicate;

        private final org.apache.ambari.server.controller.spi.Predicate userPredicate;

        private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap;

        private final org.apache.ambari.server.controller.spi.QueryResponse queryResponse;

        private QueryResult(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Predicate userPredicate, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap, org.apache.ambari.server.controller.spi.QueryResponse queryResponse) {
            this.request = request;
            this.predicate = predicate;
            this.userPredicate = userPredicate;
            this.keyValueMap = keyValueMap;
            this.queryResponse = queryResponse;
        }

        public org.apache.ambari.server.controller.spi.Request getRequest() {
            return request;
        }

        public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
            return predicate;
        }

        public org.apache.ambari.server.controller.spi.Predicate getUserPredicate() {
            return userPredicate;
        }

        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyValueMap() {
            return keyValueMap;
        }

        public org.apache.ambari.server.controller.spi.QueryResponse getQueryResponse() {
            return queryResponse;
        }
    }
}