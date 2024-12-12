package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface BlueprintSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "Blueprints")
    org.apache.ambari.server.controller.BlueprintSwagger.BlueprintInfo getBlueprintInfo();

    @io.swagger.annotations.ApiModelProperty(name = "configurations")
    java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getConfigurations();

    @io.swagger.annotations.ApiModelProperty(name = "host_groups")
    java.util.List<org.apache.ambari.server.controller.BlueprintSwagger.HostGroupInfo> getHostGroups();

    interface BlueprintInfo {
        @io.swagger.annotations.ApiModelProperty(name = "blueprint_name")
        java.lang.String getBlueprintName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        java.lang.String getStackName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        java.lang.String getStackVersion();

        @io.swagger.annotations.ApiModelProperty(name = "security")
        org.apache.ambari.server.controller.BlueprintSwagger.SecurityInfo getSecurity();
    }

    interface SecurityInfo {
        @io.swagger.annotations.ApiModelProperty(name = "security_type")
        org.apache.ambari.server.state.SecurityType getSecurityType();

        @io.swagger.annotations.ApiModelProperty(name = "kerberos_descriptor")
        java.util.Map<java.lang.String, java.lang.Object> getKerberosDescriptor();

        @io.swagger.annotations.ApiModelProperty(name = "kerberos_descriptor_reference")
        java.lang.String getKerberosDescriptorReference();
    }

    interface HostGroupInfo {
        @io.swagger.annotations.ApiModelProperty(name = "name")
        java.lang.String getHostGroupName();

        @io.swagger.annotations.ApiModelProperty(name = "cardinality")
        int getCardinality();

        @io.swagger.annotations.ApiModelProperty(name = "components")
        java.util.List<org.apache.ambari.server.controller.BlueprintSwagger.ComponentInfo> getComponents();

        @io.swagger.annotations.ApiModelProperty(name = "configurations")
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getConfigurations();
    }

    interface ComponentInfo {
        @io.swagger.annotations.ApiModelProperty(name = "name")
        java.lang.String getComponentName();

        @io.swagger.annotations.ApiModelProperty(name = "provision_action")
        java.lang.String getProvisionAction();
    }
}