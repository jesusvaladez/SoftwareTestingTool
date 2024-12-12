package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ComponentDependencyResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "Dependencies")
    public org.apache.ambari.server.controller.ComponentDependencyResponse.ComponentDependencyResponseInfo getDependencyResponseInfo();

    public interface ComponentDependencyResponseInfo {
        @io.swagger.annotations.ApiModelProperty(name = "component_name")
        public java.lang.String getComponentName();

        @io.swagger.annotations.ApiModelProperty(name = "conditions")
        public java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> getDependencyConditions();

        @io.swagger.annotations.ApiModelProperty(name = "dependent_component_name")
        public java.lang.String getDependentComponentName();

        @io.swagger.annotations.ApiModelProperty(name = "dependent_service_name")
        public java.lang.String getDependentServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "scope")
        public java.lang.String getScope();

        @io.swagger.annotations.ApiModelProperty(name = "service_name")
        public java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        public java.lang.String getStackName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        public java.lang.String getStackVersion();
    }
}