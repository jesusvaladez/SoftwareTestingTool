package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ComponentInfo {
    private java.lang.String name;

    private java.lang.String displayName;

    private java.lang.String category;

    private boolean deleted;

    private java.lang.String cardinality;

    @javax.xml.bind.annotation.XmlElement(name = "versionAdvertised")
    private java.lang.Boolean versionAdvertisedField;

    private boolean versionAdvertisedInternal = false;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "decommissionAllowed"))
    private java.lang.String decommissionAllowed;

    @javax.xml.bind.annotation.XmlElement(name = "unlimitedKeyJCERequired")
    private org.apache.ambari.server.state.UnlimitedKeyJCERequirement unlimitedKeyJCERequired;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "rollingRestartSupported"))
    private boolean rollingRestartSupported;

    private org.apache.ambari.server.state.CommandScriptDefinition commandScript;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "logs")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "log"))
    private java.util.List<org.apache.ambari.server.state.LogDefinition> logs;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "clientsToUpdateConfigs")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "client"))
    private java.util.List<java.lang.String> clientsToUpdateConfigs;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "configFiles")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "configFile"))
    private java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigFiles;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "customCommands")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "customCommand"))
    private java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> customCommands;

    @javax.xml.bind.annotation.XmlElement(name = "bulkCommands")
    private org.apache.ambari.server.state.BulkCommandDefinition bulkCommandDefinition;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "dependencies")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "dependency"))
    private java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "configuration-dependencies")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "config-type"))
    private java.util.List<java.lang.String> configDependencies;

    @javax.xml.bind.annotation.XmlElement(name = "auto-deploy")
    private org.apache.ambari.server.state.AutoDeployInfo autoDeploy;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "recovery_enabled"))
    private boolean recoveryEnabled = false;

    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "reassignAllowed"))
    private java.lang.String reassignAllowed;

    private java.lang.String timelineAppid;

    @javax.xml.bind.annotation.XmlElement(name = "customFolder")
    private java.lang.String customFolder;

    private java.lang.String componentType;

    public ComponentInfo() {
    }

    public ComponentInfo(org.apache.ambari.server.state.ComponentInfo prototype) {
        name = prototype.name;
        category = prototype.category;
        deleted = prototype.deleted;
        cardinality = prototype.cardinality;
        versionAdvertisedField = prototype.versionAdvertisedField;
        versionAdvertisedInternal = prototype.versionAdvertisedInternal;
        decommissionAllowed = prototype.decommissionAllowed;
        unlimitedKeyJCERequired = prototype.unlimitedKeyJCERequired;
        clientsToUpdateConfigs = prototype.clientsToUpdateConfigs;
        commandScript = prototype.commandScript;
        logs = prototype.logs;
        customCommands = prototype.customCommands;
        bulkCommandDefinition = prototype.bulkCommandDefinition;
        dependencies = prototype.dependencies;
        autoDeploy = prototype.autoDeploy;
        configDependencies = prototype.configDependencies;
        clientConfigFiles = prototype.clientConfigFiles;
        timelineAppid = prototype.timelineAppid;
        reassignAllowed = prototype.reassignAllowed;
        customFolder = prototype.customFolder;
        rollingRestartSupported = prototype.rollingRestartSupported;
        componentType = prototype.componentType;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getCategory() {
        return category;
    }

    public void setCategory(java.lang.String category) {
        this.category = category;
    }

    public boolean isClient() {
        return "CLIENT".equals(category);
    }

    public boolean isMaster() {
        return "MASTER".equals(category);
    }

    public boolean isSlave() {
        return "SLAVE".equals(category);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public org.apache.ambari.server.state.CommandScriptDefinition getCommandScript() {
        return commandScript;
    }

    public void setCommandScript(org.apache.ambari.server.state.CommandScriptDefinition commandScript) {
        this.commandScript = commandScript;
    }

    public java.util.List<org.apache.ambari.server.state.LogDefinition> getLogs() {
        if (logs == null) {
            logs = new java.util.ArrayList<>();
        }
        return logs;
    }

    public org.apache.ambari.server.state.LogDefinition getPrimaryLog() {
        for (org.apache.ambari.server.state.LogDefinition log : getLogs()) {
            if (log.isPrimary()) {
                return log;
            }
        }
        return null;
    }

    public void setLogs(java.util.List<org.apache.ambari.server.state.LogDefinition> logs) {
        this.logs = logs;
    }

    public java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> getClientConfigFiles() {
        return clientConfigFiles;
    }

    public void setClientConfigFiles(java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigFiles) {
        this.clientConfigFiles = clientConfigFiles;
    }

    public java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> getCustomCommands() {
        if (customCommands == null) {
            customCommands = new java.util.ArrayList<>();
        }
        return customCommands;
    }

    public void setCustomCommands(java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> customCommands) {
        this.customCommands = customCommands;
    }

    public boolean isCustomCommand(java.lang.String commandName) {
        if ((customCommands != null) && (commandName != null)) {
            for (org.apache.ambari.server.state.CustomCommandDefinition cc : customCommands) {
                if (commandName.equals(cc.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public org.apache.ambari.server.state.CustomCommandDefinition getCustomCommandByName(java.lang.String commandName) {
        for (org.apache.ambari.server.state.CustomCommandDefinition ccd : getCustomCommands()) {
            if (ccd.getName().equals(commandName)) {
                return ccd;
            }
        }
        return null;
    }

    public org.apache.ambari.server.state.BulkCommandDefinition getBulkCommandDefinition() {
        return bulkCommandDefinition;
    }

    public void setBulkCommands(org.apache.ambari.server.state.BulkCommandDefinition bulkCommandDefinition) {
        this.bulkCommandDefinition = bulkCommandDefinition;
    }

    public java.util.List<org.apache.ambari.server.state.DependencyInfo> getDependencies() {
        return dependencies;
    }

    public java.util.List<java.lang.String> getConfigDependencies() {
        return configDependencies;
    }

    public void setConfigDependencies(java.util.List<java.lang.String> configDependencies) {
        this.configDependencies = configDependencies;
    }

    public boolean hasConfigType(java.lang.String type) {
        return (configDependencies != null) && configDependencies.contains(type);
    }

    public void setDependencies(java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies) {
        this.dependencies = dependencies;
    }

    public void setAutoDeploy(org.apache.ambari.server.state.AutoDeployInfo autoDeploy) {
        this.autoDeploy = autoDeploy;
    }

    public org.apache.ambari.server.state.AutoDeployInfo getAutoDeploy() {
        return autoDeploy;
    }

    public void setCardinality(java.lang.String cardinality) {
        this.cardinality = cardinality;
    }

    public java.lang.String getCardinality() {
        return cardinality;
    }

    public void setVersionAdvertisedField(java.lang.Boolean versionAdvertisedField) {
        this.versionAdvertisedField = versionAdvertisedField;
    }

    public java.lang.Boolean getVersionAdvertisedField() {
        return this.versionAdvertisedField;
    }

    public void setVersionAdvertised(boolean versionAdvertised) {
        this.versionAdvertisedInternal = versionAdvertised;
    }

    public boolean isVersionAdvertised() {
        if (null != versionAdvertisedField) {
            return versionAdvertisedField.booleanValue();
        }
        return this.versionAdvertisedInternal;
    }

    public java.lang.String getDecommissionAllowed() {
        return decommissionAllowed;
    }

    public void setDecommissionAllowed(java.lang.String decommissionAllowed) {
        this.decommissionAllowed = decommissionAllowed;
    }

    public boolean getRollingRestartSupported() {
        return rollingRestartSupported;
    }

    public void setRollingRestartSupported(boolean rollingRestartSupported) {
        this.rollingRestartSupported = rollingRestartSupported;
    }

    public org.apache.ambari.server.state.UnlimitedKeyJCERequirement getUnlimitedKeyJCERequired() {
        return unlimitedKeyJCERequired;
    }

    public void setUnlimitedKeyJCERequired(org.apache.ambari.server.state.UnlimitedKeyJCERequirement unlimitedKeyJCERequired) {
        this.unlimitedKeyJCERequired = unlimitedKeyJCERequired;
    }

    public void setRecoveryEnabled(boolean recoveryEnabled) {
        this.recoveryEnabled = recoveryEnabled;
    }

    public boolean isRecoveryEnabled() {
        return recoveryEnabled;
    }

    public java.util.List<java.lang.String> getClientsToUpdateConfigs() {
        return clientsToUpdateConfigs;
    }

    public void setClientsToUpdateConfigs(java.util.List<java.lang.String> clientsToUpdateConfigs) {
        this.clientsToUpdateConfigs = clientsToUpdateConfigs;
    }

    public java.lang.String getTimelineAppid() {
        return timelineAppid;
    }

    public void setTimelineAppid(java.lang.String timelineAppid) {
        this.timelineAppid = timelineAppid;
    }

    public java.lang.String getReassignAllowed() {
        return reassignAllowed;
    }

    public void setReassignAllowed(java.lang.String reassignAllowed) {
        this.reassignAllowed = reassignAllowed;
    }

    public java.lang.String getCustomFolder() {
        return customFolder;
    }

    public void setCustomFolder(java.lang.String customFolder) {
        this.customFolder = customFolder;
    }

    public java.lang.String getComponentType() {
        return componentType;
    }

    public void setComponentType(java.lang.String componentType) {
        this.componentType = componentType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.ComponentInfo that = ((org.apache.ambari.server.state.ComponentInfo) (o));
        if (deleted != that.deleted)
            return false;

        if (autoDeploy != null ? !autoDeploy.equals(that.autoDeploy) : that.autoDeploy != null)
            return false;

        if (cardinality != null ? !cardinality.equals(that.cardinality) : that.cardinality != null)
            return false;

        if (versionAdvertisedField != null ? !versionAdvertisedField.equals(that.versionAdvertisedField) : that.versionAdvertisedField != null)
            return false;

        if (versionAdvertisedInternal != that.versionAdvertisedInternal)
            return false;

        if (decommissionAllowed != null ? !decommissionAllowed.equals(that.decommissionAllowed) : that.decommissionAllowed != null)
            return false;

        if (unlimitedKeyJCERequired != null ? !unlimitedKeyJCERequired.equals(that.unlimitedKeyJCERequired) : that.unlimitedKeyJCERequired != null)
            return false;

        if (reassignAllowed != null ? !reassignAllowed.equals(that.reassignAllowed) : that.reassignAllowed != null)
            return false;

        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;

        if (clientConfigFiles != null ? !clientConfigFiles.equals(that.clientConfigFiles) : that.clientConfigFiles != null)
            return false;

        if (commandScript != null ? !commandScript.equals(that.commandScript) : that.commandScript != null)
            return false;

        if (logs != null ? !logs.equals(that.logs) : that.logs != null)
            return false;

        if (configDependencies != null ? !configDependencies.equals(that.configDependencies) : that.configDependencies != null)
            return false;

        if (customCommands != null ? !customCommands.equals(that.customCommands) : that.customCommands != null)
            return false;

        if (bulkCommandDefinition != null ? !bulkCommandDefinition.equals(that.bulkCommandDefinition) : that.bulkCommandDefinition != null)
            return false;

        if (dependencies != null ? !dependencies.equals(that.dependencies) : that.dependencies != null)
            return false;

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        if (clientConfigFiles != null ? !clientConfigFiles.equals(that.clientConfigFiles) : that.clientConfigFiles != null)
            return false;

        if (customFolder != null ? !customFolder.equals(that.customFolder) : that.customFolder != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (name != null) ? name.hashCode() : 0;
        result = (31 * result) + (displayName != null ? displayName.hashCode() : 0);
        result = (31 * result) + (category != null ? category.hashCode() : 0);
        result = (31 * result) + (deleted ? 1 : 0);
        result = (31 * result) + (cardinality != null ? cardinality.hashCode() : 0);
        result = (31 * result) + (decommissionAllowed != null ? decommissionAllowed.hashCode() : 0);
        result = (31 * result) + (unlimitedKeyJCERequired != null ? unlimitedKeyJCERequired.hashCode() : 0);
        result = (31 * result) + (reassignAllowed != null ? reassignAllowed.hashCode() : 0);
        result = (31 * result) + (commandScript != null ? commandScript.hashCode() : 0);
        result = (31 * result) + (logs != null ? logs.hashCode() : 0);
        result = (31 * result) + (clientConfigFiles != null ? clientConfigFiles.hashCode() : 0);
        result = (31 * result) + (customCommands != null ? customCommands.hashCode() : 0);
        result = (31 * result) + (bulkCommandDefinition != null ? bulkCommandDefinition.hashCode() : 0);
        result = (31 * result) + (dependencies != null ? dependencies.hashCode() : 0);
        result = (31 * result) + (autoDeploy != null ? autoDeploy.hashCode() : 0);
        result = (31 * result) + (configDependencies != null ? configDependencies.hashCode() : 0);
        result = (31 * result) + (clientConfigFiles != null ? clientConfigFiles.hashCode() : 0);
        result = (31 * result) + (versionAdvertisedField != null ? versionAdvertisedField.booleanValue() ? 2 : 1 : 0);
        result = (31 * result) + (customFolder != null ? customFolder.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}