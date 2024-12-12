package org.apache.ambari.server.controller.internal;
public class FeedResourceProvider extends org.apache.ambari.server.controller.internal.AbstractDRResourceProvider {
    protected static final java.lang.String FEED_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed", "name");

    protected static final java.lang.String FEED_DESCRIPTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed", "description");

    protected static final java.lang.String FEED_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed", "status");

    protected static final java.lang.String FEED_SCHEDULE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed", "frequency");

    protected static final java.lang.String FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/sourceCluster", "name");

    protected static final java.lang.String FEED_SOURCE_CLUSTER_START_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/sourceCluster/validity", "start");

    protected static final java.lang.String FEED_SOURCE_CLUSTER_END_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/sourceCluster/validity", "end");

    protected static final java.lang.String FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/sourceCluster/retention", "limit");

    protected static final java.lang.String FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/sourceCluster/retention", "action");

    protected static final java.lang.String FEED_TARGET_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/targetCluster", "name");

    protected static final java.lang.String FEED_TARGET_CLUSTER_START_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/targetCluster/validity", "start");

    protected static final java.lang.String FEED_TARGET_CLUSTER_END_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/targetCluster/validity", "end");

    protected static final java.lang.String FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/targetCluster/retention", "limit");

    protected static final java.lang.String FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed/targetCluster/retention", "action");

    protected static final java.lang.String FEED_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Feed", "properties");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_START_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_END_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_START_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_END_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_PROPERTIES_PROPERTY_ID);

    public FeedResourceProvider(org.apache.ambari.server.controller.ivory.IvoryService ivoryService) {
        super(org.apache.ambari.server.controller.internal.FeedResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.FeedResourceProvider.keyPropertyIds, ivoryService);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = request.getProperties();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertiesSet) {
            service.submitFeed(org.apache.ambari.server.controller.internal.FeedResourceProvider.getFeed(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID))), propertyMap));
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.List<java.lang.String> feedNames = service.getFeedNames();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (java.lang.String feedName : feedNames) {
            org.apache.ambari.server.controller.ivory.Feed feed = service.getFeed(feedName);
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID, feed.getName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID, feed.getDescription(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID, feed.getStatus(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID, feed.getSchedule(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID, feed.getSourceClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_START_PROPERTY_ID, feed.getSourceClusterStart(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_END_PROPERTY_ID, feed.getSourceClusterEnd(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID, feed.getSourceClusterLimit(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID, feed.getSourceClusterAction(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID, feed.getTargetClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_START_PROPERTY_ID, feed.getTargetClusterStart(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_END_PROPERTY_ID, feed.getTargetClusterEnd(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID, feed.getTargetClusterLimit(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID, feed.getTargetClusterAction(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_PROPERTIES_PROPERTY_ID, feed.getProperties(), requestedIds);
            if ((predicate == null) || predicate.evaluate(resource)) {
                resources.add(resource);
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
            java.lang.String desiredStatus = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID)));
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                org.apache.ambari.server.controller.ivory.IvoryService service = getService();
                if (desiredStatus != null) {
                    java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID)));
                    java.lang.String feedName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID)));
                    if (desiredStatus.equals("SCHEDULED")) {
                        service.scheduleFeed(feedName);
                    } else if (desiredStatus.equals("SUSPENDED")) {
                        service.suspendFeed(feedName);
                    } else if (status.equals("SUSPENDED") && desiredStatus.equals("RUNNING")) {
                        service.resumeFeed(feedName);
                    }
                }
                service.updateFeed(org.apache.ambari.server.controller.internal.FeedResourceProvider.getFeed(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID))), org.apache.ambari.server.controller.internal.FeedResourceProvider.getUpdateMap(resource, propertyMap)));
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            service.deleteFeed(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID))));
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.FeedResourceProvider.keyPropertyIds.values());
    }

    protected static org.apache.ambari.server.controller.ivory.Feed getFeed(java.lang.String feedName, java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : propertyMap.entrySet()) {
            java.lang.String property = entry.getKey();
            java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(property);
            if (category.equals(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_PROPERTIES_PROPERTY_ID)) {
                properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(property), ((java.lang.String) (entry.getValue())));
            }
        }
        return new org.apache.ambari.server.controller.ivory.Feed(feedName, ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_START_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_END_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_START_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_END_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID))), properties);
    }

    protected static java.util.Map<java.lang.String, java.lang.Object> getUpdateMap(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.util.Map<java.lang.String, java.lang.Object> updateMap = new java.util.HashMap<>();
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_NAME_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_DESCRIPTION_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SCHEDULE_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_STATUS_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_NAME_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_START_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_START_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_END_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_END_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_LIMIT_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_SOURCE_CLUSTER_ACTION_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_NAME_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_START_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_START_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_END_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_END_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_LIMIT_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_TARGET_CLUSTER_ACTION_PROPERTY_ID));
        updateMap.put(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_PROPERTIES_PROPERTY_ID, resource.getPropertyValue(org.apache.ambari.server.controller.internal.FeedResourceProvider.FEED_PROPERTIES_PROPERTY_ID));
        updateMap.putAll(propertyMap);
        return updateMap;
    }
}