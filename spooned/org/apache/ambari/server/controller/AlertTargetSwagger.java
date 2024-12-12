package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface AlertTargetSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET)
    org.apache.ambari.server.controller.AlertTargetSwagger.AlertTargetInfo getAlertTargetInfo();

    interface AlertTargetInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ID_PROPERTY_ID)
        java.lang.Long getId();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.NAME_PROPERTY_ID)
        java.lang.String getName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.DESCRIPTION_PROPERTY_ID)
        java.lang.String getDescription();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.NOTIFICATION_TYPE_PROPERTY_ID)
        java.lang.String getNotificationType();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ENABLED_PROPERTY_ID)
        java.lang.Boolean isEnabled();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.PROPERTIES_PROPERTY_ID)
        java.util.Map<java.lang.String, java.lang.String> getProperties();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.STATES_PROPERTY_ID)
        java.util.List<java.lang.String> getAlertStates();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.GLOBAL_PROPERTY_ID)
        java.lang.Boolean isGlobal();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.GROUPS_PROPERTY_ID)
        java.util.List<org.apache.ambari.server.controller.AlertTargetSwagger.AlertTargetInfo.AlertGroup> getAlertGroups();

        interface AlertGroup {
            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ID_PROPERTY_ID)
            java.lang.Long getId();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID)
            java.lang.Long getClusterId();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.NAME_PROPERTY_ID)
            java.lang.String getGroupName();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.DEFAULT_PROPERTY_ID)
            java.lang.Boolean isDefault();
        }
    }
}