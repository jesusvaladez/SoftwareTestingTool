package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestPostRequest extends org.apache.ambari.server.controller.ApiModel {
    java.lang.String NOTES_ACTION_OR_COMMAND = "Either action or command must be specified, but not both";

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INFO)
    org.apache.ambari.server.controller.RequestPostRequest.RequestInfo getRequestInfo();

    @io.swagger.annotations.ApiModelProperty(name = "Body")
    org.apache.ambari.server.controller.RequestPostRequest.Body getBody();

    interface RequestInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID, notes = org.apache.ambari.server.controller.RequestPostRequest.NOTES_ACTION_OR_COMMAND)
        java.lang.String getAction();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, notes = org.apache.ambari.server.controller.RequestPostRequest.NOTES_ACTION_OR_COMMAND)
        java.lang.String getCommand();

        @io.swagger.annotations.ApiModelProperty(name = "operation_level", notes = "Must be specified along with command.")
        org.apache.ambari.server.controller.RequestPostRequest.OperationLevel getOperationLevel();

        @io.swagger.annotations.ApiModelProperty(name = "parameters")
        java.util.Map<java.lang.String, java.lang.Object> getParameters();
    }

    interface RequestResourceFilter {
        @io.swagger.annotations.ApiModelProperty(name = "service_name")
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "component_name")
        java.lang.String getComponentName();

        @io.swagger.annotations.ApiModelProperty(name = "hosts")
        java.lang.String getHosts();

        @io.swagger.annotations.ApiModelProperty(name = "hosts_predicate")
        java.lang.String getHostsPredicate();
    }

    interface OperationLevel {
        @io.swagger.annotations.ApiModelProperty(name = "level")
        java.lang.String getLevel();

        @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
        java.lang.String getClusterName();
    }

    interface Body {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS)
        org.apache.ambari.server.controller.RequestPostRequest.Request getRequest();
    }

    interface Request {
        @io.swagger.annotations.ApiModelProperty(name = "resource_filters")
        java.util.List<org.apache.ambari.server.controller.RequestPostRequest.RequestResourceFilter> getResourceFilters();

        @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
        java.lang.String getClusterName();

        @io.swagger.annotations.ApiModelProperty(name = "exclusive")
        boolean isExclusive();
    }
}