package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "configure")
public class ConfigureTask extends org.apache.ambari.server.stack.upgrade.ServerSideActionTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ConfigureTask.class);

    public static final java.lang.String PARAMETER_CONFIG_TYPE = "configure-task-config-type";

    public static final java.lang.String PARAMETER_KEY_VALUE_PAIRS = "configure-task-key-value-pairs";

    public static final java.lang.String PARAMETER_TRANSFERS = "configure-task-transfers";

    public static final java.lang.String PARAMETER_REPLACEMENTS = "configure-task-replacements";

    public static final java.lang.String PARAMETER_INSERTIONS = "configure-task-insertions";

    public static final java.lang.String PARAMETER_ASSOCIATED_SERVICE = "configure-task-associated-service";

    public static final java.lang.String actionVerb = "Configuring";

    private com.google.gson.Gson m_gson = new com.google.gson.Gson();

    public ConfigureTask() {
        implClass = org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class.getName();
    }

    @javax.xml.bind.annotation.XmlAttribute(name = "id")
    public java.lang.String id;

    @javax.xml.bind.annotation.XmlAttribute(name = "supports-patch")
    public boolean supportsPatch = false;

    @javax.xml.bind.annotation.XmlTransient
    public java.lang.String associatedService;

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getType() {
        return org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION;
    }

    @java.lang.Override
    public java.lang.String getActionVerb() {
        return org.apache.ambari.server.stack.upgrade.ConfigureTask.actionVerb;
    }

    public java.lang.String getId() {
        return id;
    }

    public java.lang.String getSummary(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(id) && (null != configUpgradePack)) {
            org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition definition = configUpgradePack.enumerateConfigChangesByID().get(id);
            if ((null != definition) && org.apache.commons.lang.StringUtils.isNotBlank(definition.summary)) {
                return definition.summary;
            }
        }
        return super.getSummary();
    }

    public java.util.Map<java.lang.String, java.lang.String> getConfigurationChanges(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack) {
        java.util.Map<java.lang.String, java.lang.String> configParameters = new java.util.HashMap<>();
        if ((id == null) || id.isEmpty()) {
            org.apache.ambari.server.stack.upgrade.ConfigureTask.LOG.warn("Config task id is not defined, skipping config change");
            return configParameters;
        }
        if (configUpgradePack == null) {
            org.apache.ambari.server.stack.upgrade.ConfigureTask.LOG.warn("Config upgrade pack is not defined, skipping config change");
            return configParameters;
        }
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition definition = configUpgradePack.enumerateConfigChangesByID().get(id);
        if (definition == null) {
            org.apache.ambari.server.stack.upgrade.ConfigureTask.LOG.warn(java.lang.String.format("Can not resolve config change definition by id %s, " + "skipping config change", id));
            return configParameters;
        }
        if (null != definition.getConfigType()) {
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, definition.getConfigType());
        }
        if ((null != definition.getKeyValuePairs()) && (!definition.getKeyValuePairs().isEmpty())) {
            java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> allowedSets = getValidSets(cluster, definition.getConfigType(), definition.getKeyValuePairs());
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, m_gson.toJson(allowedSets));
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = definition.getTransfers();
        if ((null != transfers) && (!transfers.isEmpty())) {
            java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> allowedTransfers = getValidTransfers(cluster, definition.getConfigType(), definition.getTransfers());
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, m_gson.toJson(allowedTransfers));
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = new java.util.ArrayList<>();
        replacements.addAll(definition.getReplacements());
        replacements.addAll(definition.getRegexReplacements(cluster));
        if (!replacements.isEmpty()) {
            java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> allowedReplacements = getValidReplacements(cluster, definition.getConfigType(), replacements);
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS, m_gson.toJson(allowedReplacements));
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> insertions = definition.getInsertions();
        if (!insertions.isEmpty()) {
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_INSERTIONS, m_gson.toJson(insertions));
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(associatedService)) {
            configParameters.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_ASSOCIATED_SERVICE, associatedService);
        }
        return configParameters;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> getValidReplacements(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> allowedReplacements = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replacement : replacements) {
            if (isValidConditionSettings(cluster, configType, replacement.key, replacement.ifKey, replacement.ifType, replacement.ifValue, replacement.ifKeyState)) {
                allowedReplacements.add(replacement);
            }
        }
        return allowedReplacements;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> getValidSets(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> sets) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> allowedSets = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue configurationKeyValue : sets) {
            if (isValidConditionSettings(cluster, configType, configurationKeyValue.key, configurationKeyValue.ifKey, configurationKeyValue.ifType, configurationKeyValue.ifValue, configurationKeyValue.ifKeyState)) {
                allowedSets.add(configurationKeyValue);
            }
        }
        return allowedSets;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> getValidTransfers(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> allowedTransfers = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer : transfers) {
            java.lang.String key = "";
            if (transfer.operation == org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE) {
                key = transfer.deleteKey;
            } else {
                key = transfer.fromKey;
            }
            if (isValidConditionSettings(cluster, configType, key, transfer.ifKey, transfer.ifType, transfer.ifValue, transfer.ifKeyState)) {
                allowedTransfers.add(transfer);
            }
        }
        return allowedTransfers;
    }

    private boolean isValidConditionSettings(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String targetPropertyKey, java.lang.String ifKey, java.lang.String ifType, java.lang.String ifValue, org.apache.ambari.server.stack.upgrade.PropertyKeyState ifKeyState) {
        boolean isValid = false;
        boolean ifKeyIsNotBlank = org.apache.commons.lang.StringUtils.isNotBlank(ifKey);
        boolean ifTypeIsNotBlank = org.apache.commons.lang.StringUtils.isNotBlank(ifType);
        boolean ifValueIsNotNull = null != ifValue;
        boolean ifKeyStateIsValid = (org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT == ifKeyState) || (org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT == ifKeyState);
        if ((ifKeyIsNotBlank && ifTypeIsNotBlank) && (ifValueIsNotNull || ifKeyStateIsValid)) {
            isValid = true;
        } else if ((((!ifKeyIsNotBlank) && (!ifTypeIsNotBlank)) && (!ifValueIsNotNull)) && (!ifKeyStateIsValid)) {
            isValid = true;
        }
        return isValid;
    }
}