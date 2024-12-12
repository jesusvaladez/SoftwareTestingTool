package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class ActiveWidgetLayoutResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String WIDGETLAYOUT_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "id");

    public static final java.lang.String WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "cluster_name");

    public static final java.lang.String WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "section_name");

    public static final java.lang.String WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "layout_name");

    public static final java.lang.String WIDGETLAYOUT_SCOPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "scope");

    public static final java.lang.String WIDGETLAYOUT_WIDGETS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "widgets");

    public static final java.lang.String WIDGETLAYOUT_USERNAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "user_name");

    public static final java.lang.String WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetLayoutInfo", "display_name");

    public static final java.lang.String WIDGETLAYOUT = "WidgetLayouts";

    public static final java.lang.String ID = "id";

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout, org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).build();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.UserDAO userDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO;

    @com.google.inject.Inject
    private static com.google.gson.Gson gson;

    public static void init(org.apache.ambari.server.orm.dao.UserDAO userDAO, org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO, org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO, com.google.gson.Gson gson) {
        org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.userDAO = userDAO;
        org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.widgetDAO = widgetDAO;
        org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.widgetLayoutDAO = widgetLayoutDAO;
        org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.gson = gson;
    }

    public ActiveWidgetLayoutResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout, org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("The request is not supported");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> layoutEntities = new java.util.ArrayList<>();
        boolean isUserAdministrator = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.String userName = getUserName(propertyMap);
            if ((!isUserAdministrator) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName().equalsIgnoreCase(userName))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException();
            }
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Set<java.util.Map<java.lang.String, java.lang.String>>>() {}.getType();
            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> activeWidgetLayouts = org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.gson.fromJson(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.userDAO.findUserByName(userName).getActiveWidgetLayouts(), type);
            if (activeWidgetLayouts != null) {
                for (java.util.Map<java.lang.String, java.lang.String> widgetLayoutId : activeWidgetLayouts) {
                    layoutEntities.add(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.widgetLayoutDAO.findById(java.lang.Long.parseLong(widgetLayoutId.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.ID))));
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.WidgetLayoutEntity layoutEntity : layoutEntities) {
            org.apache.ambari.server.controller.ActiveWidgetLayoutResponse activeWidgetLayoutResponse = getResponse(layoutEntity);
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout);
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID, activeWidgetLayoutResponse.getId());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_CLUSTER_NAME_PROPERTY_ID, activeWidgetLayoutResponse.getClusterName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID, activeWidgetLayoutResponse.getLayoutName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID, activeWidgetLayoutResponse.getSectionName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID, activeWidgetLayoutResponse.getScope());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID, activeWidgetLayoutResponse.getUserName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID, activeWidgetLayoutResponse.getDisplayName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID, activeWidgetLayoutResponse.getWidgets());
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.ActiveWidgetLayoutResponse getResponse(org.apache.ambari.server.orm.entities.WidgetLayoutEntity layoutEntity) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String clusterName = null;
        try {
            clusterName = getManagementController().getClusters().getClusterById(layoutEntity.getClusterId()).getClusterName();
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage());
        }
        java.util.List<java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse>> widgets = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> widgetLayoutUserWidgetEntityList = layoutEntity.getListWidgetLayoutUserWidgetEntity();
        for (org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity widgetLayoutUserWidgetEntity : widgetLayoutUserWidgetEntityList) {
            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = widgetLayoutUserWidgetEntity.getWidget();
            java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse> widgetInfoMap = new java.util.HashMap<>();
            widgetInfoMap.put("WidgetInfo", org.apache.ambari.server.controller.WidgetResponse.coerce(widgetEntity));
            widgets.add(widgetInfoMap);
        }
        return new org.apache.ambari.server.controller.ActiveWidgetLayoutResponse(layoutEntity.getId(), clusterName, layoutEntity.getDisplayName(), layoutEntity.getLayoutName(), layoutEntity.getSectionName(), layoutEntity.getScope(), layoutEntity.getUserName(), widgets);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = request.getProperties();
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                boolean isUserAdministrator = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    final java.lang.String userName = getUserName(propertyMap);
                    if ((!isUserAdministrator) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName().equalsIgnoreCase(userName))) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException();
                    }
                    java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> widgetLayouts = ((java.util.Set) (propertyMap.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT)));
                    for (java.util.HashMap<java.lang.String, java.lang.String> widgetLayout : widgetLayouts) {
                        final java.lang.Long layoutId;
                        try {
                            layoutId = java.lang.Long.parseLong(widgetLayout.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.ID));
                        } catch (java.lang.Exception ex) {
                            throw new org.apache.ambari.server.AmbariException("WidgetLayout should have numerical id");
                        }
                        final org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity = org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.widgetLayoutDAO.findById(layoutId);
                        if (entity == null) {
                            throw new org.apache.ambari.server.AmbariException("There is no widget layout with id " + layoutId);
                        }
                    }
                    org.apache.ambari.server.orm.entities.UserEntity user = org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.userDAO.findUserByName(userName);
                    user.setActiveWidgetLayouts(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.gson.toJson(propertyMap.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT)));
                    org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.userDAO.merge(user);
                }
                return null;
            }
        });
        return getRequestStatus(null);
    }

    private java.lang.String getUserName(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.lang.String userName = (propertyMap.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID) == null) ? "" : propertyMap.get(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).toString();
        if (org.apache.commons.lang3.StringUtils.isBlank(userName)) {
            userName = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
        }
        return userName;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("The request is not supported");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.pkPropertyIds;
    }
}