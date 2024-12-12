package org.apache.ambari.server.controller.internal;
public class LoggingResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final java.lang.String LOGGING_SEARCH_SERVICE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Logging", "search_service_name");

    private static final java.lang.String LOGGING_SEARCH_TERM_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Logging", "searchTerm");

    private static final java.lang.String LOGGING_COMPONENT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Logging", "component");

    private static final java.util.Set<java.lang.String> PROPERTY_IDS;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS;

    static {
        java.util.Set<java.lang.String> localSet = new java.util.HashSet<>();
        localSet.add(LOGGING_SEARCH_SERVICE_PROPERTY_ID);
        localSet.add(LOGGING_SEARCH_TERM_PROPERTY_ID);
        localSet.add(LOGGING_COMPONENT_PROPERTY_ID);
        PROPERTY_IDS = java.util.Collections.unmodifiableSet(localSet);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> localMap = new java.util.HashMap<>();
        localMap.put(org.apache.ambari.server.controller.spi.Resource.Type.LoggingQuery, LOGGING_SEARCH_SERVICE_PROPERTY_ID);
        KEY_PROPERTY_IDS = java.util.Collections.unmodifiableMap(localMap);
    }

    public LoggingResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.LoggingQuery, org.apache.ambari.server.controller.internal.LoggingResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.LoggingResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return java.util.Collections.emptySet();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.LoggingQuery);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.LoggingResourceProvider.LOGGING_SEARCH_SERVICE_PROPERTY_ID, "logging", getRequestPropertyIds(request, predicate));
        org.apache.ambari.server.controller.logging.LoggingRequestHelper requestHelper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl().getHelper(org.apache.ambari.server.controller.AmbariServer.getController(), "");
        java.util.Map<java.lang.String, java.lang.String> queryParameters = new java.util.HashMap<>();
        queryParameters.put("level", "ERROR");
        org.apache.ambari.server.controller.logging.LogQueryResponse response = requestHelper.sendQueryRequest(queryParameters);
        resource.setProperty("startIndex", response.getStartIndex());
        resource.setProperty("pageSize", response.getPageSize());
        resource.setProperty("resultSize", response.getResultSize());
        resource.setProperty("queryTimeMMS", response.getQueryTimeMS());
        resource.setProperty("totalCount", response.getTotalCount());
        resource.setProperty("logList", response.getListOfResults());
        return java.util.Collections.singleton(resource);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> unSupportedProperties = super.checkPropertyIds(propertyIds);
        unSupportedProperties.remove("searchTerm");
        return unSupportedProperties;
    }
}