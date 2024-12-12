package org.apache.ambari.server.security.authorization;
public enum RoleAuthorization {

    AMBARI_ADD_DELETE_CLUSTERS("AMBARI.ADD_DELETE_CLUSTERS"),
    AMBARI_ASSIGN_ROLES("AMBARI.ASSIGN_ROLES"),
    AMBARI_EDIT_STACK_REPOS("AMBARI.EDIT_STACK_REPOS"),
    AMBARI_MANAGE_SETTINGS("AMBARI.MANAGE_SETTINGS"),
    AMBARI_MANAGE_GROUPS("AMBARI.MANAGE_GROUPS"),
    AMBARI_MANAGE_STACK_VERSIONS("AMBARI.MANAGE_STACK_VERSIONS"),
    AMBARI_MANAGE_USERS("AMBARI.MANAGE_USERS"),
    AMBARI_MANAGE_VIEWS("AMBARI.MANAGE_VIEWS"),
    AMBARI_RENAME_CLUSTER("AMBARI.RENAME_CLUSTER"),
    AMBARI_RUN_CUSTOM_COMMAND("AMBARI.RUN_CUSTOM_COMMAND"),
    AMBARI_MANAGE_CONFIGURATION("AMBARI.MANAGE_CONFIGURATION"),
    AMBARI_VIEW_STATUS_INFO("AMBARI.VIEW_STATUS_INFO"),
    CLUSTER_MANAGE_CREDENTIALS("CLUSTER.MANAGE_CREDENTIALS"),
    CLUSTER_MODIFY_CONFIGS("CLUSTER.MODIFY_CONFIGS"),
    CLUSTER_MANAGE_CONFIG_GROUPS("CLUSTER.MANAGE_CONFIG_GROUPS"),
    CLUSTER_MANAGE_ALERTS("CLUSTER.MANAGE_ALERTS"),
    CLUSTER_MANAGE_USER_PERSISTED_DATA("CLUSTER.MANAGE_USER_PERSISTED_DATA"),
    CLUSTER_TOGGLE_ALERTS("CLUSTER.TOGGLE_ALERTS"),
    CLUSTER_TOGGLE_KERBEROS("CLUSTER.TOGGLE_KERBEROS"),
    CLUSTER_UPGRADE_DOWNGRADE_STACK("CLUSTER.UPGRADE_DOWNGRADE_STACK"),
    CLUSTER_VIEW_ALERTS("CLUSTER.VIEW_ALERTS"),
    CLUSTER_VIEW_CONFIGS("CLUSTER.VIEW_CONFIGS"),
    CLUSTER_VIEW_METRICS("CLUSTER.VIEW_METRICS"),
    CLUSTER_VIEW_STACK_DETAILS("CLUSTER.VIEW_STACK_DETAILS"),
    CLUSTER_VIEW_STATUS_INFO("CLUSTER.VIEW_STATUS_INFO"),
    CLUSTER_RUN_CUSTOM_COMMAND("CLUSTER.RUN_CUSTOM_COMMAND"),
    CLUSTER_MANAGE_AUTO_START("CLUSTER.MANAGE_AUTO_START"),
    CLUSTER_MANAGE_ALERT_NOTIFICATIONS("CLUSTER.MANAGE_ALERT_NOTIFICATIONS"),
    CLUSTER_MANAGE_WIDGETS("CLUSTER.MANAGE_WIDGETS"),
    HOST_ADD_DELETE_COMPONENTS("HOST.ADD_DELETE_COMPONENTS"),
    HOST_ADD_DELETE_HOSTS("HOST.ADD_DELETE_HOSTS"),
    HOST_TOGGLE_MAINTENANCE("HOST.TOGGLE_MAINTENANCE"),
    HOST_VIEW_CONFIGS("HOST.VIEW_CONFIGS"),
    HOST_VIEW_METRICS("HOST.VIEW_METRICS"),
    HOST_VIEW_STATUS_INFO("HOST.VIEW_STATUS_INFO"),
    SERVICE_ADD_DELETE_SERVICES("SERVICE.ADD_DELETE_SERVICES"),
    SERVICE_VIEW_OPERATIONAL_LOGS("SERVICE.VIEW_OPERATIONAL_LOGS"),
    SERVICE_COMPARE_CONFIGS("SERVICE.COMPARE_CONFIGS"),
    SERVICE_DECOMMISSION_RECOMMISSION("SERVICE.DECOMMISSION_RECOMMISSION"),
    SERVICE_ENABLE_HA("SERVICE.ENABLE_HA"),
    SERVICE_MANAGE_CONFIG_GROUPS("SERVICE.MANAGE_CONFIG_GROUPS"),
    SERVICE_MANAGE_ALERTS("SERVICE.MANAGE_ALERTS"),
    SERVICE_MODIFY_CONFIGS("SERVICE.MODIFY_CONFIGS"),
    SERVICE_MOVE("SERVICE.MOVE"),
    SERVICE_RUN_CUSTOM_COMMAND("SERVICE.RUN_CUSTOM_COMMAND"),
    SERVICE_RUN_SERVICE_CHECK("SERVICE.RUN_SERVICE_CHECK"),
    SERVICE_SET_SERVICE_USERS_GROUPS("SERVICE.SET_SERVICE_USERS_GROUPS"),
    SERVICE_START_STOP("SERVICE.START_STOP"),
    SERVICE_TOGGLE_ALERTS("SERVICE.TOGGLE_ALERTS"),
    SERVICE_TOGGLE_MAINTENANCE("SERVICE.TOGGLE_MAINTENANCE"),
    SERVICE_VIEW_ALERTS("SERVICE.VIEW_ALERTS"),
    SERVICE_VIEW_CONFIGS("SERVICE.VIEW_CONFIGS"),
    SERVICE_VIEW_METRICS("SERVICE.VIEW_METRICS"),
    SERVICE_VIEW_STATUS_INFO("SERVICE.VIEW_STATUS_INFO"),
    SERVICE_MANAGE_AUTO_START("SERVICE.MANAGE_AUTO_START"),
    VIEW_USE("VIEW.USE");
    public static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_VIEW_CLUSTER = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK);

    public static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_UPDATE_CLUSTER = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS);

    public static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_VIEW_SERVICE = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ENABLE_HA, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MOVE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK);

    public static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> AUTHORIZATIONS_UPDATE_SERVICE = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ENABLE_HA, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MODIFY_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MOVE, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_ALERTS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_SET_SERVICE_USERS_GROUPS);

    private final java.lang.String id;

    RoleAuthorization(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getId() {
        return id;
    }

    public static org.apache.ambari.server.security.authorization.RoleAuthorization translate(java.lang.String authenticationId) {
        if (authenticationId == null) {
            return null;
        } else {
            authenticationId = authenticationId.trim();
            if (authenticationId.isEmpty()) {
                return null;
            } else {
                return org.apache.ambari.server.security.authorization.RoleAuthorization.valueOf(authenticationId.replace(".", "_").toUpperCase());
            }
        }
    }
}