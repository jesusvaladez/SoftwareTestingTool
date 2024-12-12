package org.apache.ambari.server.controller.internal;
public class ClusterControllerImpl implements org.apache.ambari.server.controller.spi.ClusterController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ClusterControllerImpl.class);

    private final org.apache.ambari.server.controller.spi.ProviderModule providerModule;

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper> resourceProviders = new java.util.HashMap<>();

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider>> propertyProviders = new java.util.HashMap<>();

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.Schema> schemas = new java.util.HashMap<>();

    private final org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceComparator comparator = new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceComparator();

    private static final org.apache.ambari.server.controller.internal.DefaultResourcePredicateEvaluator DEFAULT_RESOURCE_PREDICATE_EVALUATOR = new org.apache.ambari.server.controller.internal.DefaultResourcePredicateEvaluator();

    public ClusterControllerImpl(org.apache.ambari.server.controller.spi.ProviderModule providerModule) {
        this.providerModule = providerModule;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate getAmendedPredicate(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Predicate predicate) {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper provider = ensureResourceProviderWrapper(type);
        ensurePropertyProviders(type);
        return provider.getAmendedPredicate(predicate);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.QueryResponse getResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = null;
        org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper provider = ensureResourceProviderWrapper(type);
        ensurePropertyProviders(type);
        if (provider != null) {
            if (org.apache.ambari.server.controller.internal.ClusterControllerImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.ClusterControllerImpl.LOG.debug("Using resource provider {} for request type {}", provider.getClass().getName(), type);
            }
            checkProperties(type, request, predicate);
            queryResponse = provider.queryForResources(request, predicate);
        }
        return queryResponse == null ? new org.apache.ambari.server.controller.internal.QueryResponseImpl(java.util.Collections.emptySet()) : queryResponse;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> keepers = resources;
        java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> propertyProviders = ensurePropertyProviders(type);
        for (org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider : propertyProviders) {
            if (providesRequestProperties(propertyProvider, request, predicate)) {
                keepers = propertyProvider.populateResources(keepers, request, predicate);
            }
        }
        return keepers;
    }

    @java.lang.Override
    public java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> getIterable(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.QueryResponse queryResponse, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.PageRequest pageRequest, org.apache.ambari.server.controller.spi.SortRequest sortRequest) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.SystemException {
        return getPage(type, queryResponse, request, predicate, pageRequest, sortRequest).getIterable();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.PageResponse getPage(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.QueryResponse queryResponse, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.PageRequest pageRequest, org.apache.ambari.server.controller.spi.SortRequest sortRequest) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> providerResources = queryResponse.getResources();
        org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper provider = ensureResourceProviderWrapper(type);
        int totalCount = 0;
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = providerResources;
        if (!providerResources.isEmpty()) {
            boolean providerAlreadyPaged = queryResponse.isPagedResponse();
            boolean providerAlreadySorted = queryResponse.isSortedResponse();
            java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> resourceComparator = comparator;
            if (null != sortRequest) {
                checkSortRequestProperties(sortRequest, type, provider);
                resourceComparator = new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceComparator(sortRequest);
            }
            if (!providerAlreadySorted) {
                java.util.TreeSet<org.apache.ambari.server.controller.spi.Resource> sortedResources = new java.util.TreeSet<>(resourceComparator);
                sortedResources.addAll(providerResources);
                resources = sortedResources;
            }
            totalCount = resources.size();
            if ((null != pageRequest) && (!providerAlreadyPaged)) {
                switch (pageRequest.getStartingPoint()) {
                    case Beginning :
                        return getPageFromOffset(pageRequest.getPageSize(), 0, resources, predicate, provider);
                    case End :
                        return getPageToOffset(pageRequest.getPageSize(), -1, resources, predicate, provider);
                    case OffsetStart :
                        return getPageFromOffset(pageRequest.getPageSize(), pageRequest.getOffset(), resources, predicate, provider);
                    case OffsetEnd :
                        return getPageToOffset(pageRequest.getPageSize(), pageRequest.getOffset(), resources, predicate, provider);
                    case PredicateStart :
                    case PredicateEnd :
                        break;
                    default :
                        break;
                }
            } else if (providerAlreadyPaged) {
                totalCount = queryResponse.getTotalResourceCount();
            }
        }
        return new org.apache.ambari.server.controller.internal.PageResponseImpl(new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceIterable(resources, predicate, provider), 0, null, null, totalCount);
    }

    private void checkSortRequestProperties(org.apache.ambari.server.controller.spi.SortRequest sortRequest, org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.ResourceProvider provider) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException {
        java.util.Set<java.lang.String> requestPropertyIds = provider.checkPropertyIds(new java.util.HashSet<>(sortRequest.getPropertyIds()));
        if (requestPropertyIds.size() > 0) {
            java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> propertyProviders = ensurePropertyProviders(type);
            for (org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider : propertyProviders) {
                requestPropertyIds = propertyProvider.checkPropertyIds(requestPropertyIds);
                if (requestPropertyIds.size() == 0) {
                    return;
                }
            }
            throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(type, requestPropertyIds);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Schema getSchema(org.apache.ambari.server.controller.spi.Resource.Type type) {
        org.apache.ambari.server.controller.spi.Schema schema = schemas.get(type);
        if (schema == null) {
            synchronized(schemas) {
                schema = schemas.get(type);
                if (schema == null) {
                    schema = new org.apache.ambari.server.controller.internal.SchemaImpl(ensureResourceProvider(type));
                    schemas.put(type, schema);
                }
            }
        }
        return schema;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = ensureResourceProvider(type);
        if (provider != null) {
            checkProperties(type, request, null);
            return provider.createResources(request);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = ensureResourceProvider(type);
        if (provider != null) {
            if (!checkProperties(type, request, predicate)) {
                predicate = resolvePredicate(type, predicate);
                if (predicate == null) {
                    return null;
                }
            }
            return provider.updateResources(request, predicate);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = ensureResourceProvider(type);
        if (provider != null) {
            if (!checkProperties(type, null, predicate)) {
                predicate = resolvePredicate(type, predicate);
                if (predicate == null) {
                    return null;
                }
            }
            return provider.deleteResources(request, predicate);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.ResourceProvider ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper providerWrapper = ensureResourceProviderWrapper(type);
        return providerWrapper == null ? null : providerWrapper.resourceProvider;
    }

    private org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper ensureResourceProviderWrapper(org.apache.ambari.server.controller.spi.Resource.Type type) {
        synchronized(resourceProviders) {
            if (!resourceProviders.containsKey(type)) {
                resourceProviders.put(type, new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ExtendedResourceProviderWrapper(providerModule.getResourceProvider(type)));
            }
        }
        return resourceProviders.get(type);
    }

    protected java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> getResourceIterable(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        return getResources(type, request, predicate, null, null).getIterable();
    }

    protected org.apache.ambari.server.controller.spi.PageResponse getResources(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.PageRequest pageRequest, org.apache.ambari.server.controller.spi.SortRequest sortRequest) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = getResources(type, request, predicate);
        populateResources(type, queryResponse.getResources(), request, predicate);
        return getPage(type, queryResponse, request, predicate, pageRequest, sortRequest);
    }

    private boolean checkProperties(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException {
        java.util.Set<java.lang.String> requestPropertyIds = (request == null) ? new java.util.HashSet<>() : org.apache.ambari.server.controller.utilities.PropertyHelper.getAssociatedPropertyIds(request);
        if (predicate != null) {
            requestPropertyIds.addAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate));
        }
        if (requestPropertyIds.size() > 0) {
            org.apache.ambari.server.controller.spi.ResourceProvider provider = ensureResourceProvider(type);
            requestPropertyIds = provider.checkPropertyIds(requestPropertyIds);
            if (requestPropertyIds.size() > 0) {
                java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> propertyProviders = ensurePropertyProviders(type);
                for (org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider : propertyProviders) {
                    requestPropertyIds = propertyProvider.checkPropertyIds(requestPropertyIds);
                    if (requestPropertyIds.size() == 0) {
                        return false;
                    }
                }
                throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(type, requestPropertyIds);
            }
        }
        return true;
    }

    private org.apache.ambari.server.controller.spi.Predicate resolvePredicate(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = ensureResourceProvider(type);
        java.util.Set<java.lang.String> keyPropertyIds = new java.util.HashSet<>(provider.getKeyPropertyIds().values());
        org.apache.ambari.server.controller.spi.Request readRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(keyPropertyIds);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> resources = getResourceIterable(type, readRequest, predicate);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.utilities.PredicateBuilder.PredicateBuilderWithPredicate pbWithPredicate = null;
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            if (pbWithPredicate != null) {
                pb = pbWithPredicate.or();
            }
            pb = pb.begin();
            pbWithPredicate = null;
            for (java.lang.String keyPropertyId : keyPropertyIds) {
                if (pbWithPredicate != null) {
                    pb = pbWithPredicate.and();
                }
                pbWithPredicate = pb.property(keyPropertyId).equals(((java.lang.Comparable) (resource.getPropertyValue(keyPropertyId))));
            }
            if (pbWithPredicate != null) {
                pbWithPredicate = pbWithPredicate.end();
            }
        }
        return pbWithPredicate == null ? null : pbWithPredicate.toPredicate();
    }

    private boolean providesRequestProperties(org.apache.ambari.server.controller.spi.PropertyProvider provider, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> requestPropertyIds = new java.util.HashSet<>(request.getPropertyIds());
        if (requestPropertyIds.size() == 0) {
            return true;
        }
        requestPropertyIds.addAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate));
        int size = requestPropertyIds.size();
        return size > provider.checkPropertyIds(requestPropertyIds).size();
    }

    private java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> ensurePropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
        synchronized(propertyProviders) {
            if (!propertyProviders.containsKey(type)) {
                java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> providers = providerModule.getPropertyProviders(type);
                propertyProviders.put(type, providers == null ? java.util.Collections.emptyList() : providers);
            }
        }
        return propertyProviders.get(type);
    }

    private java.util.LinkedList<org.apache.ambari.server.controller.spi.Resource> getEvaluatedResources(org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceIterable resourceIterable) {
        java.util.LinkedList<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedList<>();
        if (resourceIterable != null) {
            for (org.apache.ambari.server.controller.spi.Resource resource : resourceIterable) {
                resources.add(resource);
            }
        }
        return resources;
    }

    private org.apache.ambari.server.controller.spi.PageResponse getPageFromOffset(int pageSize, int offset, java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator) {
        int currentOffset = 0;
        org.apache.ambari.server.controller.spi.Resource previous = null;
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> pageResources = new java.util.LinkedHashSet<>();
        java.util.LinkedList<org.apache.ambari.server.controller.spi.Resource> filteredResources = getEvaluatedResources(new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceIterable(resources, predicate, evaluator));
        java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> iterator = filteredResources.iterator();
        while ((currentOffset < offset) && iterator.hasNext()) {
            previous = iterator.next();
            ++currentOffset;
        } 
        for (int i = 0; (i < pageSize) && iterator.hasNext(); ++i) {
            pageResources.add(iterator.next());
        }
        return new org.apache.ambari.server.controller.internal.PageResponseImpl(pageResources, currentOffset, previous, iterator.hasNext() ? iterator.next() : null, filteredResources.size());
    }

    private org.apache.ambari.server.controller.spi.PageResponse getPageToOffset(int pageSize, int offset, java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator) {
        int currentOffset = resources.size() - 1;
        org.apache.ambari.server.controller.spi.Resource next = null;
        java.util.List<org.apache.ambari.server.controller.spi.Resource> pageResources = new java.util.LinkedList<>();
        java.util.LinkedList<org.apache.ambari.server.controller.spi.Resource> filteredResources = getEvaluatedResources(new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceIterable(resources, predicate, evaluator));
        java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> iterator = filteredResources.descendingIterator();
        if (offset != (-1)) {
            while ((currentOffset > offset) && iterator.hasNext()) {
                next = iterator.next();
                --currentOffset;
            } 
        }
        for (int i = 0; (i < pageSize) && iterator.hasNext(); ++i) {
            pageResources.add(0, iterator.next());
            --currentOffset;
        }
        return new org.apache.ambari.server.controller.internal.PageResponseImpl(pageResources, currentOffset + 1, iterator.hasNext() ? iterator.next() : null, next, filteredResources.size());
    }

    protected java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> getComparator() {
        return comparator;
    }

    private static class ResourceIterable implements java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> {
        private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources;

        private final org.apache.ambari.server.controller.spi.Predicate predicate;

        private final org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator;

        private ResourceIterable(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator) {
            this.resources = resources;
            this.predicate = predicate;
            this.evaluator = evaluator;
        }

        @java.lang.Override
        public java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> iterator() {
            return new org.apache.ambari.server.controller.internal.ClusterControllerImpl.ResourceIterator(resources, predicate, evaluator);
        }
    }

    private static class ResourceIterator implements java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> {
        private final java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> iterator;

        private final org.apache.ambari.server.controller.spi.Predicate predicate;

        private org.apache.ambari.server.controller.spi.Resource nextResource;

        private final org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator;

        private ResourceIterator(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator) {
            iterator = resources.iterator();
            this.predicate = predicate;
            this.evaluator = evaluator;
            nextResource = getNextResource();
        }

        @java.lang.Override
        public boolean hasNext() {
            return nextResource != null;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.Resource next() {
            if (nextResource == null) {
                throw new java.util.NoSuchElementException("Iterator has no more elements.");
            }
            org.apache.ambari.server.controller.spi.Resource currentResource = nextResource;
            nextResource = getNextResource();
            return currentResource;
        }

        @java.lang.Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Remove not supported.");
        }

        private org.apache.ambari.server.controller.spi.Resource getNextResource() {
            while (iterator.hasNext()) {
                org.apache.ambari.server.controller.spi.Resource next = iterator.next();
                if ((predicate == null) || evaluator.evaluate(predicate, next)) {
                    return next;
                }
            } 
            return null;
        }
    }

    protected class ResourceComparator implements java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> {
        org.apache.ambari.server.controller.spi.SortRequest sortRequest;

        protected ResourceComparator() {
        }

        protected ResourceComparator(org.apache.ambari.server.controller.spi.SortRequest sortRequest) {
            this.sortRequest = sortRequest;
        }

        @java.lang.Override
        public int compare(org.apache.ambari.server.controller.spi.Resource resource1, org.apache.ambari.server.controller.spi.Resource resource2) {
            org.apache.ambari.server.controller.spi.Resource.Type resourceType = resource1.getType();
            int compVal = resourceType.compareTo(resource2.getType());
            if ((compVal == 0) && (sortRequest != null)) {
                for (org.apache.ambari.server.controller.spi.SortRequestProperty property : sortRequest.getProperties()) {
                    compVal = compareValues(resource1.getPropertyValue(property.getPropertyId()), resource2.getPropertyValue(property.getPropertyId()), property.getOrder());
                    if (compVal != 0) {
                        return compVal;
                    }
                }
            }
            if (compVal == 0) {
                org.apache.ambari.server.controller.spi.Schema schema = getSchema(resourceType);
                for (org.apache.ambari.server.controller.spi.Resource.Type type : schema.getKeyTypes()) {
                    java.lang.String keyPropertyId = schema.getKeyPropertyId(type);
                    if (keyPropertyId != null) {
                        compVal = compareValues(resource1.getPropertyValue(keyPropertyId), resource2.getPropertyValue(keyPropertyId));
                        if (compVal != 0) {
                            return compVal;
                        }
                    }
                }
            }
            return resource1.toString().compareTo(resource2.toString());
        }

        @java.lang.SuppressWarnings("unchecked")
        private int compareValues(java.lang.Object val1, java.lang.Object val2) {
            if ((val1 == null) || (val2 == null)) {
                return (val1 == null) && (val2 == null) ? 0 : val1 == null ? -1 : 1;
            }
            if (val1 instanceof java.lang.Comparable) {
                try {
                    return ((java.lang.Comparable) (val1)).compareTo(val2);
                } catch (java.lang.ClassCastException e) {
                    return 0;
                }
            }
            return 0;
        }

        private int compareValues(java.lang.Object val1, java.lang.Object val2, org.apache.ambari.server.controller.spi.SortRequest.Order order) {
            if (order == org.apache.ambari.server.controller.spi.SortRequest.Order.ASC) {
                return compareValues(val1, val2);
            } else {
                return (-1) * compareValues(val1, val2);
            }
        }
    }

    private static class ExtendedResourceProviderWrapper implements org.apache.ambari.server.controller.spi.ExtendedResourceProvider , org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator {
        private final org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider;

        private final org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator evaluator;

        private final org.apache.ambari.server.controller.spi.ExtendedResourceProvider extendedResourceProvider;

        public ExtendedResourceProviderWrapper(org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider) {
            this.resourceProvider = resourceProvider;
            extendedResourceProvider = (resourceProvider instanceof org.apache.ambari.server.controller.spi.ExtendedResourceProvider) ? ((org.apache.ambari.server.controller.spi.ExtendedResourceProvider) (resourceProvider)) : null;
            evaluator = (resourceProvider instanceof org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator) ? ((org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator) (resourceProvider)) : org.apache.ambari.server.controller.internal.ClusterControllerImpl.DEFAULT_RESOURCE_PREDICATE_EVALUATOR;
        }

        public org.apache.ambari.server.controller.spi.Predicate getAmendedPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
            if (org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider.class.isInstance(resourceProvider)) {
                return ((org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider) (resourceProvider)).amendPredicate(predicate);
            } else {
                return null;
            }
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.QueryResponse queryForResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return extendedResourceProvider == null ? new org.apache.ambari.server.controller.internal.QueryResponseImpl(resourceProvider.getResources(request, predicate)) : extendedResourceProvider.queryForResources(request, predicate);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return resourceProvider.createResources(request);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return resourceProvider.getResources(request, predicate);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return resourceProvider.updateResources(request, predicate);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return resourceProvider.deleteResources(request, predicate);
        }

        @java.lang.Override
        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
            return resourceProvider.getKeyPropertyIds();
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            return resourceProvider.checkPropertyIds(propertyIds);
        }

        @java.lang.Override
        public boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource) {
            return evaluator.evaluate(predicate, resource);
        }
    }
}