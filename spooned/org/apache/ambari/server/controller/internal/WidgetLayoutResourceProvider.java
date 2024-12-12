package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class WidgetLayoutResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String WIDGETLAYOUT_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "id");

    public static final java.lang.String WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "cluster_name");

    public static final java.lang.String WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "section_name");

    public static final java.lang.String WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "layout_name");

    public static final java.lang.String WIDGETLAYOUT_SCOPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "scope");

    public static final java.lang.String WIDGETLAYOUT_WIDGETS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "widgets");

    public static final java.lang.String WIDGETLAYOUT_USERNAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "user_name");

    public static final java.lang.String WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "display_name");

    public enum SCOPE {

        CLUSTER,
        USER;}

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    private static final java.util.concurrent.locks.ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).build();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO;

    public WidgetLayoutResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.orm.entities.WidgetLayoutEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.WidgetLayoutEntity invoke() throws org.apache.ambari.server.AmbariException {
                    final java.lang.String[] requiredProperties = new java.lang.String[]{ org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID };
                    for (java.lang.String propertyName : requiredProperties) {
                        if (properties.get(propertyName) == null) {
                            throw new org.apache.ambari.server.AmbariException(("Property " + propertyName) + " should be provided");
                        }
                    }
                    final org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity = new org.apache.ambari.server.orm.entities.WidgetLayoutEntity();
                    java.lang.String userName = getUserName(properties);
                    java.util.Set widgetsSet = ((java.util.LinkedHashSet) (properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID)));
                    java.lang.String clusterName = properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID).toString();
                    entity.setLayoutName(properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID).toString());
                    entity.setClusterId(getManagementController().getClusters().getCluster(clusterName).getClusterId());
                    entity.setSectionName(properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID).toString());
                    entity.setScope(properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID).toString());
                    entity.setUserName(userName);
                    entity.setDisplayName(properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID).toString());
                    java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = new java.util.LinkedList<>();
                    int order = 0;
                    for (java.lang.Object widgetObject : widgetsSet) {
                        java.util.HashMap<java.lang.String, java.lang.Object> widget = ((java.util.HashMap) (widgetObject));
                        long id = java.lang.Integer.parseInt(widget.get("id").toString());
                        org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetDAO.findById(id);
                        if (widgetEntity == null) {
                            throw new org.apache.ambari.server.AmbariException(("Widget with id " + widget.get("id")) + " does not exists");
                        }
                        org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
                        widgetLayoutUserWidgetEntity.setWidget(widgetEntity);
                        widgetLayoutUserWidgetEntity.setWidgetOrder(order++);
                        widgetLayoutUserWidgetEntity.setWidgetLayout(entity);
                        widgetLayoutUserWidgetEntityList.add(widgetLayoutUserWidgetEntity);
                        widgetEntity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidgetEntity);
                    }
                    entity.setListWidgetLayoutUserWidgetEntity(widgetLayoutUserWidgetEntityList);
                    org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.create(entity);
                    notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout, request);
                    return entity;
                }
            });
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID, widgetLayoutEntity.getId());
            associatedResources.add(resource);
        }
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> layoutEntities = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            if (propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID) != null) {
                final java.lang.Long id;
                try {
                    id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).toString());
                } catch (java.lang.Exception ex) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("WidgetLayout should have numerical id");
                }
                final org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity = org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.findById(id);
                if (entity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(("WidgetLayout with id " + id) + " does not exists");
                }
                layoutEntities.add(entity);
            } else {
                layoutEntities.addAll(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.findAll());
            }
        }
        for (org.apache.ambari.server.orm.entities.WidgetLayoutEntity layoutEntity : layoutEntities) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID, layoutEntity.getId());
            java.lang.String clusterName;
            try {
                clusterName = getManagementController().getClusters().getClusterById(layoutEntity.getClusterId()).getClusterName();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage());
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID, clusterName);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID, layoutEntity.getLayoutName());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID, layoutEntity.getSectionName());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID, layoutEntity.getScope());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID, layoutEntity.getUserName());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID, layoutEntity.getDisplayName());
            java.util.List<java.util.HashMap> widgets = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = layoutEntity.getListWidgetLayoutUserWidgetEntity();
            for (org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity : widgetLayoutUserWidgetEntityList) {
                org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = widgetLayoutUserWidgetEntity.getWidget();
                java.util.HashMap<java.lang.String, java.lang.Object> widgetInfoMap = new java.util.HashMap<>();
                widgetInfoMap.put("WidgetInfo", org.apache.ambari.server.controller.WidgetResponse.coerce(widgetEntity));
                widgets.add(widgetInfoMap);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID, widgets);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = request.getProperties();
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    final java.lang.Long layoutId;
                    try {
                        layoutId = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).toString());
                    } catch (java.lang.Exception ex) {
                        throw new org.apache.ambari.server.AmbariException("WidgetLayout should have numerical id");
                    }
                    org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.lock.writeLock().lock();
                    try {
                        final org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity = org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.findById(layoutId);
                        if (entity == null) {
                            throw new org.apache.ambari.server.ObjectNotFoundException("There is no widget layout with id " + layoutId);
                        }
                        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID)))) {
                            entity.setLayoutName(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID).toString());
                        }
                        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID)))) {
                            entity.setSectionName(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID).toString());
                        }
                        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID)))) {
                            entity.setDisplayName(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID).toString());
                        }
                        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID)))) {
                            entity.setScope(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID).toString());
                        }
                        java.util.Set widgetsSet = ((java.util.LinkedHashSet) (propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID)));
                        for (org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity : entity.getListWidgetLayoutUserWidgetEntity()) {
                            widgetLayoutUserWidgetEntity.getWidget().getListWidgetLayoutUserWidgetEntity().remove(widgetLayoutUserWidgetEntity);
                        }
                        entity.setListWidgetLayoutUserWidgetEntity(new java.util.LinkedList<>());
                        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = new java.util.LinkedList<>();
                        int order = 0;
                        for (java.lang.Object widgetObject : widgetsSet) {
                            java.util.HashMap<java.lang.String, java.lang.Object> widget = ((java.util.HashMap) (widgetObject));
                            long id = java.lang.Integer.parseInt(widget.get("id").toString());
                            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetDAO.findById(id);
                            if (widgetEntity == null) {
                                throw new org.apache.ambari.server.AmbariException(("Widget with id " + widget.get("id")) + " does not exists");
                            }
                            org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity = new org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity();
                            widgetLayoutUserWidgetEntity.setWidget(widgetEntity);
                            widgetLayoutUserWidgetEntity.setWidgetOrder(order++);
                            widgetLayoutUserWidgetEntity.setWidgetLayout(entity);
                            widgetLayoutUserWidgetEntityList.add(widgetLayoutUserWidgetEntity);
                            widgetEntity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidgetEntity);
                            entity.getListWidgetLayoutUserWidgetEntity().add(widgetLayoutUserWidgetEntity);
                        }
                        org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.mergeWithFlush(entity);
                    } finally {
                        org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.lock.writeLock().unlock();
                    }
                }
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        final java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> entitiesToBeRemoved = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.Long id;
            try {
                id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).toString());
            } catch (java.lang.Exception ex) {
                throw new org.apache.ambari.server.controller.spi.SystemException("WidgetLayout should have numerical id");
            }
            final org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity = org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.findById(id);
            if (entity == null) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("There is no widget layout with id " + id);
            }
            entitiesToBeRemoved.add(entity);
        }
        for (org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity : entitiesToBeRemoved) {
            if (entity.getListWidgetLayoutUserWidgetEntity() != null) {
                for (org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity layoutUserWidgetEntity : entity.getListWidgetLayoutUserWidgetEntity()) {
                    if (layoutUserWidgetEntity.getWidget().getListWidgetLayoutUserWidgetEntity() != null) {
                        layoutUserWidgetEntity.getWidget().getListWidgetLayoutUserWidgetEntity().remove(layoutUserWidgetEntity);
                    }
                }
            }
            org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.widgetLayoutDAO.remove(entity);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.pkPropertyIds;
    }

    private java.lang.String getUserName(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID) != null) {
            return properties.get(org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).toString();
        }
        return getManagementController().getAuthName();
    }
}