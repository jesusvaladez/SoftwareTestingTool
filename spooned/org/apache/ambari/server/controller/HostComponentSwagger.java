package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface HostComponentSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES)
    org.apache.ambari.server.controller.ServiceComponentHostResponse getHostRole();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_PROPERTY_ID)
    org.apache.ambari.server.controller.HostComponentSwagger.HostComponentHost getHost();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.METRICS_PROPERTY_ID)
    org.apache.ambari.server.controller.HostComponentSwagger.HostComponentMetrics getMetrics();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PROCESSES_PROPERTY_ID)
    java.util.List<org.apache.ambari.server.controller.HostComponentSwagger.HostComponentProcesses> getProcesses();

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_PROPERTY_ID)
    java.util.List<org.apache.ambari.server.controller.HostComponentSwagger.HostComponentComponent> getComponent();

    interface HostComponentHost extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HREF_PROPERTY_ID)
        java.lang.String getHref();
    }

    interface HostComponentMetrics extends org.apache.ambari.server.controller.ApiModel {}

    interface HostComponentProcesses extends org.apache.ambari.server.controller.ApiModel {}

    interface HostComponentComponent extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HREF_PROPERTY_ID)
        java.lang.String getHref();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_COMPONENT_INFO)
        org.apache.ambari.server.controller.HostComponentSwagger.HostComponentComponent.HostComponentServiceComponentInfo getHostComponentServiceComponentInfo();

        interface HostComponentServiceComponentInfo extends org.apache.ambari.server.controller.ApiModel {
            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME_PROPERTY_ID)
            java.lang.String getClusterName();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID)
            java.lang.String getComponentName();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME_PROPERTY_ID)
            java.lang.String getServiceName();
        }
    }
}