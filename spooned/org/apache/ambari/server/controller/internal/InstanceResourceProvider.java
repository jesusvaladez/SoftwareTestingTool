package org.apache.ambari.server.controller.internal;
public class InstanceResourceProvider extends org.apache.ambari.server.controller.internal.AbstractDRResourceProvider {
    protected static final java.lang.String INSTANCE_FEED_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "feedName");

    protected static final java.lang.String INSTANCE_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "id");

    protected static final java.lang.String INSTANCE_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "status");

    protected static final java.lang.String INSTANCE_START_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "startTime");

    protected static final java.lang.String INSTANCE_END_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "endTime");

    protected static final java.lang.String INSTANCE_DETAILS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "details");

    protected static final java.lang.String INSTANCE_LOG_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Instance", "log");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_END_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_DETAILS_PROPERTY_ID, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_LOG_PROPERTY_ID);

    public InstanceResourceProvider(org.apache.ambari.server.controller.ivory.IvoryService ivoryService) {
        super(org.apache.ambari.server.controller.internal.InstanceResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.InstanceResourceProvider.keyPropertyIds, ivoryService);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.List<java.lang.String> feedNames = new java.util.LinkedList<>();
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        if (predicate == null) {
            feedNames = service.getFeedNames();
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                java.lang.String feedName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID)));
                if (feedName == null) {
                    feedNames = service.getFeedNames();
                    break;
                }
                feedNames.add(feedName);
            }
        }
        for (java.lang.String feedName : feedNames) {
            java.util.List<org.apache.ambari.server.controller.ivory.Instance> instances = service.getInstances(feedName);
            for (org.apache.ambari.server.controller.ivory.Instance instance : instances) {
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID, instance.getFeedName(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID, instance.getId(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID, instance.getStatus(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_START_TIME_PROPERTY_ID, instance.getStartTime(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_END_TIME_PROPERTY_ID, instance.getEndTime(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_DETAILS_PROPERTY_ID, instance.getDetails(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_LOG_PROPERTY_ID, instance.getLog(), requestedIds);
                if ((predicate == null) || predicate.evaluate(resource)) {
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
            java.lang.String desiredStatus = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID)));
            if (desiredStatus != null) {
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
                for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                    java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID)));
                    java.lang.String feedName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID)));
                    java.lang.String id = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID)));
                    if (desiredStatus.equals("SUSPENDED")) {
                        service.suspendInstance(feedName, id);
                    } else if (status.equals("SUSPENDED") && desiredStatus.equals("RUNNING")) {
                        service.resumeInstance(feedName, id);
                    }
                }
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            service.killInstance(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_FEED_NAME_PROPERTY_ID))), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_ID_PROPERTY_ID))));
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.InstanceResourceProvider.keyPropertyIds.values());
    }

    protected static org.apache.ambari.server.controller.ivory.Instance getInstance(java.lang.String feedName, java.lang.String instanceId, java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        return new org.apache.ambari.server.controller.ivory.Instance(feedName, instanceId, ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_STATUS_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_START_TIME_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_END_TIME_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_DETAILS_PROPERTY_ID))), ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.InstanceResourceProvider.INSTANCE_LOG_PROPERTY_ID))));
    }
}