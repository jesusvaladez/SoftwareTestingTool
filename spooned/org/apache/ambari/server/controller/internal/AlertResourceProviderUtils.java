package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
public class AlertResourceProviderUtils {
    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_VIEW_CLUSTER_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_VIEW_SERVICE_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_ALERTS);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_EXECUTE_CLUSTER_ALERTS = org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_CLUSTER_ALERTS;

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_EXECUTE_SERVICE_ALERTS = org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_SERVICE_ALERTS;

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_TOGGLE_CLUSTER_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_TOGGLE_SERVICE_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_ALERTS);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_MANAGE_CLUSTER_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS);

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_MANAGE_SERVICE_ALERTS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_ALERTS);

    public static boolean hasViewAuthorization(org.apache.ambari.server.orm.entities.AlertGroupEntity entity, java.lang.Long clusterResourceId) {
        return (null != entity) && org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.hasViewAuthorization(entity.getServiceName(), clusterResourceId);
    }

    public static boolean hasViewAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.hasAuthorization(serviceName, clusterResourceId, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_SERVICE_ALERTS);
    }

    public static void verifyViewAuthorization(org.apache.ambari.server.orm.entities.AlertCurrentEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (entity != null) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity.getAlertHistory());
        }
    }

    public static void verifyViewAuthorization(org.apache.ambari.server.orm.entities.AlertHistoryEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (entity != null) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity.getAlertDefinition());
        }
    }

    public static void verifyViewAuthorization(org.apache.ambari.server.orm.entities.AlertGroupEntity entity, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (entity != null) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity.getServiceName(), clusterResourceId);
        }
    }

    public static void verifyViewAuthorization(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(entity, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_SERVICE_ALERTS, "view");
    }

    public static void verifyViewAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(serviceName, clusterResourceId, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_VIEW_SERVICE_ALERTS, "view");
    }

    public static void verifyExecuteAuthorization(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(entity, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_EXECUTE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_EXECUTE_SERVICE_ALERTS, "execute");
    }

    public static void verifyExecuteAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(serviceName, clusterResourceId, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_EXECUTE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_EXECUTE_SERVICE_ALERTS, "execute");
    }

    public static void verifyToggleAuthorization(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(entity, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_TOGGLE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_TOGGLE_SERVICE_ALERTS, "execute");
    }

    public static void verifyToggleAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(serviceName, clusterResourceId, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_TOGGLE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_TOGGLE_SERVICE_ALERTS, "execute");
    }

    public static void verifyManageAuthorization(org.apache.ambari.server.orm.entities.AlertGroupEntity entity, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (entity != null) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity.getServiceName(), clusterResourceId);
        }
    }

    public static void verifyManageAuthorization(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(entity, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_MANAGE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_MANAGE_SERVICE_ALERTS, "manage");
    }

    public static void verifyManageAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(serviceName, clusterResourceId, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_MANAGE_CLUSTER_ALERTS, org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.AUTHORIZATIONS_MANAGE_SERVICE_ALERTS, "manage");
    }

    public static boolean hasAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> clusterLevelAuthorizations, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> serviceLevelAuthorizations) {
        if (null == clusterResourceId) {
            clusterResourceId = -1L;
        }
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusterResourceId, org.apache.commons.lang.StringUtils.isEmpty(serviceName) || "AMBARI".equals(serviceName) ? clusterLevelAuthorizations : serviceLevelAuthorizations);
    }

    public static void verifyAuthorization(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> clusterLevelAuthorizations, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> serviceLevelAuthorizations, java.lang.String operation) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = (null == entity) ? null : entity.getCluster();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = (null == clusterEntity) ? null : clusterEntity.getResource();
        java.lang.Long resourceId = (null == resourceEntity) ? null : resourceEntity.getId();
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyAuthorization(null == entity ? null : entity.getServiceName(), resourceId, clusterLevelAuthorizations, serviceLevelAuthorizations, operation);
    }

    public static void verifyAuthorization(java.lang.String serviceName, java.lang.Long clusterResourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> clusterLevelAuthorizations, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> serviceLevelAuthorizations, java.lang.String operation) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (null == clusterResourceId) {
            clusterResourceId = -1L;
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(serviceName) || "AMBARI".equals(serviceName)) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusterResourceId, clusterLevelAuthorizations)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user is not authorized to %s cluster-level alerts", operation));
            }
        } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusterResourceId, serviceLevelAuthorizations)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user is not authorized to %s service-level alerts", operation));
        }
    }
}