package org.apache.ambari.server.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
public class TestAuthenticationFactory {
    public static org.springframework.security.core.Authentication createAdministrator() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin");
    }

    public static org.springframework.security.core.Authentication createAdministrator(java.lang.String name) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministratorGrantedAuthority()));
    }

    public static org.springframework.security.core.Authentication createClusterAdministrator() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("clusterAdmin", 4L);
    }

    public static org.springframework.security.core.Authentication createClusterOperator() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator("clusterOp", 4L);
    }

    public static org.springframework.security.core.Authentication createClusterAdministrator(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministratorGrantedAuthority(clusterResourceId)));
    }

    public static org.springframework.security.core.Authentication createClusterOperator(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperatorGrantedAuthority(clusterResourceId)));
    }

    public static org.springframework.security.core.Authentication createServiceAdministrator() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("serviceAdmin", 4L);
    }

    public static org.springframework.security.core.Authentication createServiceAdministrator(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministratorGrantedAuthority(clusterResourceId)));
    }

    public static org.springframework.security.core.Authentication createServiceOperator() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator("serviceOp", 4L);
    }

    public static org.springframework.security.core.Authentication createServiceOperator(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperatorGrantedAuthority(clusterResourceId)));
    }

    public static org.springframework.security.core.Authentication createClusterUser() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser("clusterUser", 4L);
    }

    public static org.springframework.security.core.Authentication createClusterUser(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUserGrantedAuthority(clusterResourceId)));
    }

    public static org.springframework.security.core.Authentication createViewUser(java.lang.Long viewResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("viewUser", viewResourceId);
    }

    public static org.springframework.security.core.Authentication createViewUser(java.lang.String name, java.lang.Long viewResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.singleton(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUserGrantedAuthority(viewResourceId)));
    }

    public static org.springframework.security.core.Authentication createNoRoleUser() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createNoRoleUser("noRoleUser", 4L);
    }

    public static org.springframework.security.core.Authentication createNoRoleUser(java.lang.String name, java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariUserAuthentication(1, name, java.util.Collections.emptySet());
    }

    private static org.springframework.security.core.GrantedAuthority createAdministratorGrantedAuthority() {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministratorPrivilegeEntity());
    }

    private static org.springframework.security.core.GrantedAuthority createClusterAdministratorGrantedAuthority(java.lang.Long clusterResourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministratorPrivilegeEntity(clusterResourceId));
    }

    private static org.springframework.security.core.GrantedAuthority createClusterOperatorGrantedAuthority(java.lang.Long clusterResourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperatorPrivilegeEntity(clusterResourceId));
    }

    private static org.springframework.security.core.GrantedAuthority createServiceAdministratorGrantedAuthority(java.lang.Long clusterResourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministratorPrivilegeEntity(clusterResourceId));
    }

    private static org.springframework.security.core.GrantedAuthority createServiceOperatorGrantedAuthority(java.lang.Long clusterResourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperatorPrivilegeEntity(clusterResourceId));
    }

    private static org.springframework.security.core.GrantedAuthority createClusterUserGrantedAuthority(java.lang.Long clusterResourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUserPrivilegeEntity(clusterResourceId));
    }

    private static org.springframework.security.core.GrantedAuthority createViewUserGrantedAuthority(java.lang.Long resourceId) {
        return new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUserPrivilegeEntity(resourceId));
    }

    public static org.apache.ambari.server.orm.entities.PrivilegeEntity createPrivilegeEntity(org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity, org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity, org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity) {
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity.setResource(resourceEntity);
        privilegeEntity.setPermission(permissionEntity);
        privilegeEntity.setPrincipal(principalEntity);
        return privilegeEntity;
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createAdministratorPrivilegeEntity() {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createAmbariResourceEntity(), org.apache.ambari.server.security.TestAuthenticationFactory.createAdministratorPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createClusterAdministratorPrivilegeEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterResourceEntity(clusterResourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministratorPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createClusterOperatorPrivilegeEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterResourceEntity(clusterResourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperatorPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createServiceAdministratorPrivilegeEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterResourceEntity(clusterResourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministratorPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createServiceOperatorPrivilegeEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterResourceEntity(clusterResourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperatorPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createClusterUserPrivilegeEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterResourceEntity(clusterResourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUserPermission(), null);
    }

    private static org.apache.ambari.server.orm.entities.PrivilegeEntity createViewUserPrivilegeEntity(java.lang.Long resourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(org.apache.ambari.server.security.TestAuthenticationFactory.createViewResourceEntity(resourceId), org.apache.ambari.server.security.TestAuthenticationFactory.createViewUserPermission(), null);
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createAdministratorPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.AMBARI));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(1L));
        permissionEntity.addAuthorizations(java.util.EnumSet.allOf(org.apache.ambari.server.security.authorization.RoleAuthorization.class));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createClusterAdministratorPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_ADMINISTRATOR_PERMISSION);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(2L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_OPERATIONAL_LOGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_SET_SERVICE_USERS_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MOVE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ENABLE_HA, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CREDENTIALS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERT_NOTIFICATIONS));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createClusterOperatorPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(5);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(3L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_OPERATIONAL_LOGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MOVE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ENABLE_HA, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CREDENTIALS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_AUTO_START));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createServiceAdministratorPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(5);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(4L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_OPERATIONAL_LOGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createServiceOperatorPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(6);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(5L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createClusterUserPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_USER_PERMISSION);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(6L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA));
        return permissionEntity;
    }

    public static org.apache.ambari.server.orm.entities.PermissionEntity createViewUserPermission() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity.setId(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION);
        permissionEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER));
        permissionEntity.setPrincipal(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalEntity(7L));
        permissionEntity.addAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.VIEW_USE));
        return permissionEntity;
    }

    private static org.apache.ambari.server.orm.entities.ResourceEntity createAmbariResourceEntity() {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(null);
        resourceEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.AMBARI));
        return resourceEntity;
    }

    private static org.apache.ambari.server.orm.entities.ResourceEntity createClusterResourceEntity(java.lang.Long clusterResourceId) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createResourceEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusterResourceId);
    }

    private static org.apache.ambari.server.orm.entities.ResourceEntity createResourceEntity(org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(resourceId);
        resourceEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(resourceType));
        return resourceEntity;
    }

    private static org.apache.ambari.server.orm.entities.ResourceEntity createViewResourceEntity(java.lang.Long resourceId) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(resourceId);
        if (resourceId != null) {
            resourceEntity.setResourceType(org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resourceId.intValue()));
        }
        return resourceEntity;
    }

    private static org.apache.ambari.server.orm.entities.ResourceTypeEntity createResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType resourceType) {
        return org.apache.ambari.server.security.TestAuthenticationFactory.createResourceTypeEntity(resourceType.name(), resourceType.getId());
    }

    private static org.apache.ambari.server.orm.entities.ResourceTypeEntity createResourceTypeEntity(java.lang.String resourceName, java.lang.Integer resourceId) {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(resourceId);
        resourceTypeEntity.setName(resourceName);
        return resourceTypeEntity;
    }

    private static org.apache.ambari.server.orm.entities.PrincipalEntity createPrincipalEntity(java.lang.Long principalId) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setId(principalId);
        principalEntity.setPrincipalType(org.apache.ambari.server.security.TestAuthenticationFactory.createPrincipalTypeEntity());
        return principalEntity;
    }

    private static org.apache.ambari.server.orm.entities.PrincipalTypeEntity createPrincipalTypeEntity() {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setId(1);
        principalTypeEntity.setName("ROLE");
        return principalTypeEntity;
    }

    private static org.springframework.security.core.Authentication createAmbariUserAuthentication(int userId, java.lang.String username, java.util.Set<org.springframework.security.core.GrantedAuthority> authorities) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principal = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principal.setPrivileges(java.util.Collections.emptySet());
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserName(username);
        userEntity.setPrincipal(principal);
        return new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(null, new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(new org.apache.ambari.server.security.authorization.User(userEntity), null, authorities), true);
    }
}