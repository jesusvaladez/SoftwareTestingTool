package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackServiceComponentResponse {
    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private java.lang.String componentDisplayName;

    private java.lang.String componentCategory;

    private boolean isClient;

    private boolean isMaster;

    private java.lang.String cardinality;

    private boolean versionAdvertised;

    private java.lang.String decommissionAllowed;

    private boolean rollingRestartSupported;

    private org.apache.ambari.server.state.AutoDeployInfo autoDeploy;

    private java.util.List<java.lang.String> customCommands;

    private java.util.List<java.lang.String> visibleCustomCommands;

    private boolean recoveryEnabled;

    private java.lang.String bulkCommandsDisplayName;

    private java.lang.String bulkCommandMasterComponentName;

    private boolean hasBulkCommands;

    private java.lang.String reassignAllowed;

    private java.lang.String componentType;

    public StackServiceComponentResponse(org.apache.ambari.server.state.ComponentInfo component) {
        componentName = component.getName();
        componentDisplayName = component.getDisplayName();
        componentCategory = component.getCategory();
        isClient = component.isClient();
        isMaster = component.isMaster();
        cardinality = component.getCardinality();
        versionAdvertised = component.isVersionAdvertised();
        decommissionAllowed = component.getDecommissionAllowed();
        autoDeploy = component.getAutoDeploy();
        recoveryEnabled = component.isRecoveryEnabled();
        hasBulkCommands = componentHasBulkCommands(component);
        bulkCommandsDisplayName = getBulkCommandsDisplayName(component);
        bulkCommandMasterComponentName = getBulkCommandsMasterComponentName(component);
        reassignAllowed = component.getReassignAllowed();
        rollingRestartSupported = component.getRollingRestartSupported();
        componentType = component.getComponentType();
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> definitions = component.getCustomCommands();
        if ((null == definitions) || (definitions.size() == 0)) {
            customCommands = java.util.Collections.emptyList();
            visibleCustomCommands = java.util.Collections.emptyList();
        } else {
            customCommands = new java.util.ArrayList<>(definitions.size());
            visibleCustomCommands = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.CustomCommandDefinition command : definitions) {
                customCommands.add(command.getName());
                if (!command.isHidden()) {
                    visibleCustomCommands.add(command.getName());
                }
            }
        }
    }

    private java.lang.String getBulkCommandsMasterComponentName(org.apache.ambari.server.state.ComponentInfo component) {
        org.apache.ambari.server.state.BulkCommandDefinition o = component.getBulkCommandDefinition();
        if (o == null) {
            return "";
        } else {
            return o.getMasterComponent();
        }
    }

    private java.lang.String getBulkCommandsDisplayName(org.apache.ambari.server.state.ComponentInfo component) {
        org.apache.ambari.server.state.BulkCommandDefinition o = component.getBulkCommandDefinition();
        if (o == null) {
            return "";
        } else {
            return o.getDisplayName();
        }
    }

    private boolean componentHasBulkCommands(org.apache.ambari.server.state.ComponentInfo component) {
        org.apache.ambari.server.state.BulkCommandDefinition o = component.getBulkCommandDefinition();
        if (o == null) {
            return false;
        } else {
            return (o.getDisplayName() != null) && (!o.getDisplayName().trim().isEmpty());
        }
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_name")
    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_version")
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "component_name")
    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "display_name")
    public java.lang.String getComponentDisplayName() {
        return componentDisplayName;
    }

    public void setComponentDisplayName(java.lang.String componentDisplayName) {
        this.componentDisplayName = componentDisplayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "component_category")
    public java.lang.String getComponentCategory() {
        return componentCategory;
    }

    public void setComponentCategory(java.lang.String componentCategory) {
        this.componentCategory = componentCategory;
    }

    @io.swagger.annotations.ApiModelProperty(name = "is_client")
    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean isClient) {
        this.isClient = isClient;
    }

    @io.swagger.annotations.ApiModelProperty(name = "is_master")
    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    @io.swagger.annotations.ApiModelProperty(name = "cardinality")
    public java.lang.String getCardinality() {
        return cardinality;
    }

    public void setCardinality(java.lang.String cardinality) {
        this.cardinality = cardinality;
    }

    @io.swagger.annotations.ApiModelProperty(name = "advertise_version")
    public boolean isVersionAdvertised() {
        return versionAdvertised;
    }

    public void setVersionAdvertised(boolean versionAdvertised) {
        this.versionAdvertised = versionAdvertised;
    }

    @io.swagger.annotations.ApiModelProperty(name = "decommission_allowed")
    public boolean isDecommissionAlllowed() {
        if ((decommissionAllowed != null) && decommissionAllowed.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setDecommissionAllowed(java.lang.String decommissionAllowed) {
        this.decommissionAllowed = decommissionAllowed;
    }

    @io.swagger.annotations.ApiModelProperty(name = "rollingRestartSupported")
    public boolean isRollingRestartSupported() {
        return rollingRestartSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "reassign_allowed")
    public boolean isReassignAlllowed() {
        if ((reassignAllowed != null) && reassignAllowed.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setReassignAllowed(java.lang.String reassignAllowed) {
        this.reassignAllowed = reassignAllowed;
    }

    @io.swagger.annotations.ApiModelProperty(name = "recovery_enabled")
    public boolean isRecoveryEnabled() {
        return recoveryEnabled;
    }

    public void setRecoveryEnabled(boolean recoveryEnabled) {
        this.recoveryEnabled = recoveryEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public org.apache.ambari.server.state.AutoDeployInfo getAutoDeploy() {
        return autoDeploy;
    }

    public void setAutoDeploy(org.apache.ambari.server.state.AutoDeployInfo autoDeploy) {
        this.autoDeploy = autoDeploy;
    }

    public java.util.List<java.lang.String> getCustomCommands() {
        return customCommands;
    }

    @io.swagger.annotations.ApiModelProperty(name = "custom_commands")
    public java.util.List<java.lang.String> getVisibleCustomCommands() {
        return visibleCustomCommands;
    }

    @io.swagger.annotations.ApiModelProperty(name = "has_bulk_commands_definition")
    public boolean hasBulkCommands() {
        return hasBulkCommands;
    }

    public java.lang.String getBulkCommandsDisplayName() {
        return bulkCommandsDisplayName == null ? "" : bulkCommandsDisplayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "bulk_commands_master_component_namen")
    public java.lang.String getBulkCommandsMasterComponentName() {
        return bulkCommandMasterComponentName == null ? "" : bulkCommandMasterComponentName;
    }

    public java.lang.String getComponentType() {
        return componentType;
    }

    public interface StackServiceComponentResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "StackServiceComponents")
        public org.apache.ambari.server.controller.StackServiceComponentResponse getStackServiceComponentResponse();
    }
}