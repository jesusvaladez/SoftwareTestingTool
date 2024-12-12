package org.apache.ambari.server.stack;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
public class ComponentModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.ComponentModule, org.apache.ambari.server.state.ComponentInfo> implements org.apache.ambari.server.stack.Validable {
    private org.apache.ambari.server.state.ComponentInfo componentInfo;

    protected boolean valid = true;

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    public ComponentModule(org.apache.ambari.server.state.ComponentInfo componentInfo) {
        this.componentInfo = componentInfo;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.ComponentModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) {
        if (parent != null) {
            org.apache.ambari.server.state.ComponentInfo parentInfo = parent.getModuleInfo();
            if (!parent.isValid()) {
                setValid(false);
                addErrors(parent.getErrors());
            }
            if (componentInfo.getCommandScript() == null) {
                componentInfo.setCommandScript(parentInfo.getCommandScript());
            }
            if (componentInfo.getDisplayName() == null) {
                componentInfo.setDisplayName(parentInfo.getDisplayName());
            }
            if (componentInfo.getConfigDependencies() == null) {
                componentInfo.setConfigDependencies(parentInfo.getConfigDependencies());
            }
            if (componentInfo.getClientConfigFiles() == null) {
                componentInfo.setClientConfigFiles(parentInfo.getClientConfigFiles());
            }
            if (componentInfo.getClientsToUpdateConfigs() == null) {
                componentInfo.setClientsToUpdateConfigs(parentInfo.getClientsToUpdateConfigs());
            }
            if (componentInfo.getCategory() == null) {
                componentInfo.setCategory(parentInfo.getCategory());
            }
            if (componentInfo.getCardinality() == null) {
                componentInfo.setCardinality(parentInfo.getCardinality());
            }
            if (null == componentInfo.getVersionAdvertisedField()) {
                componentInfo.setVersionAdvertised(parentInfo.isVersionAdvertised());
            } else {
                componentInfo.setVersionAdvertised(componentInfo.getVersionAdvertisedField().booleanValue());
            }
            if (componentInfo.getDecommissionAllowed() == null) {
                componentInfo.setDecommissionAllowed(parentInfo.getDecommissionAllowed());
            }
            if (componentInfo.getUnlimitedKeyJCERequired() == null) {
                componentInfo.setUnlimitedKeyJCERequired(parentInfo.getUnlimitedKeyJCERequired());
            }
            if (componentInfo.getAutoDeploy() == null) {
                componentInfo.setAutoDeploy(parentInfo.getAutoDeploy());
            }
            componentInfo.setRecoveryEnabled(parentInfo.isRecoveryEnabled());
            if (componentInfo.getBulkCommandDefinition() == null) {
                componentInfo.setBulkCommands(parentInfo.getBulkCommandDefinition());
            }
            if (componentInfo.getReassignAllowed() == null) {
                componentInfo.setReassignAllowed(parentInfo.getReassignAllowed());
            }
            if (componentInfo.getTimelineAppid() == null) {
                componentInfo.setTimelineAppid(parentInfo.getTimelineAppid());
            }
            mergeComponentDependencies(parentInfo.getDependencies(), componentInfo.getDependencies());
            mergeCustomCommands(parentInfo.getCustomCommands(), componentInfo.getCustomCommands());
            mergeLogs(parentInfo.getLogs(), componentInfo.getLogs());
        } else {
            componentInfo.setCardinality("0+");
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ComponentInfo getModuleInfo() {
        return componentInfo;
    }

    @java.lang.Override
    public boolean isDeleted() {
        return componentInfo.isDeleted();
    }

    @java.lang.Override
    public java.lang.String getId() {
        return componentInfo.getName();
    }

    private void mergeComponentDependencies(java.util.List<org.apache.ambari.server.state.DependencyInfo> parentDependencies, java.util.List<org.apache.ambari.server.state.DependencyInfo> childDependencies) {
        java.util.Collection<java.lang.String> existingNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.DependencyInfo childDependency : childDependencies) {
            existingNames.add(childDependency.getName());
        }
        if (parentDependencies != null) {
            for (org.apache.ambari.server.state.DependencyInfo parentDependency : parentDependencies) {
                if (!existingNames.contains(parentDependency.getName())) {
                    childDependencies.add(parentDependency);
                }
            }
        }
    }

    private void mergeCustomCommands(java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> parentCommands, java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> childCommands) {
        java.util.Collection<java.lang.String> existingNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.CustomCommandDefinition childCmd : childCommands) {
            existingNames.add(childCmd.getName());
        }
        if (parentCommands != null) {
            for (org.apache.ambari.server.state.CustomCommandDefinition parentCmd : parentCommands) {
                if (!existingNames.contains(parentCmd.getName())) {
                    childCommands.add(parentCmd);
                }
            }
        }
    }

    private void mergeLogs(java.util.List<org.apache.ambari.server.state.LogDefinition> parentLogs, java.util.List<org.apache.ambari.server.state.LogDefinition> childLogs) {
        java.util.Collection<java.lang.String> existingLogIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.LogDefinition childLog : childLogs) {
            existingLogIds.add(childLog.getLogId());
        }
        if (parentLogs != null) {
            for (org.apache.ambari.server.state.LogDefinition parentLog : parentLogs) {
                if (!existingLogIds.contains(parentLog.getLogId())) {
                    childLogs.add(parentLog);
                }
            }
        }
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}