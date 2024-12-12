package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
public class ConfigureAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class);

    private static final java.lang.String ALL_SYMBOL = "*";

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController m_controller;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper m_configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigMergeHelper m_mergeHelper;

    @com.google.inject.Inject
    private com.google.gson.Gson m_gson;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementControllerImpl> m_ambariManagementController;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.MetadataHolder> m_metadataHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.AgentConfigsHolder> m_agentConfigsHolder;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        if ((null == commandParameters) || commandParameters.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to change configuration values without command parameters");
        }
        java.lang.String clusterName = commandParameters.get("clusterName");
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        java.lang.String configType = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE);
        java.lang.String serviceName = cluster.getServiceByConfigType(configType);
        if (org.apache.commons.lang.StringUtils.isBlank(serviceName)) {
            serviceName = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_ASSOCIATED_SERVICE);
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepoVersion = upgradeContext.getSourceRepositoryVersion(serviceName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
        org.apache.ambari.server.state.StackId sourceStackId = sourceRepoVersion.getStackId();
        org.apache.ambari.server.state.StackId targetStackId = targetRepoVersion.getStackId();
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs = java.util.Collections.emptyList();
        java.lang.String keyValuePairJson = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS);
        if (null != keyValuePairJson) {
            keyValuePairs = m_gson.fromJson(keyValuePairJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue>>() {}.getType());
            keyValuePairs = getAllowedSets(cluster, configType, keyValuePairs);
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = java.util.Collections.emptyList();
        java.lang.String transferJson = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS);
        if (null != transferJson) {
            transfers = m_gson.fromJson(transferJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer>>() {}.getType());
            transfers = getAllowedTransfers(cluster, configType, transfers);
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = java.util.Collections.emptyList();
        java.lang.String replaceJson = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS);
        if (null != replaceJson) {
            replacements = m_gson.fromJson(replaceJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace>>() {}.getType());
            replacements = getAllowedReplacements(cluster, configType, replacements);
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> insertions = java.util.Collections.emptyList();
        java.lang.String insertJson = commandParameters.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_INSERTIONS);
        if (null != insertJson) {
            insertions = m_gson.fromJson(insertJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert>>() {}.getType());
            insertions = getAllowedInsertions(cluster, configType, insertions);
        }
        if (((keyValuePairs.isEmpty() && transfers.isEmpty()) && replacements.isEmpty()) && insertions.isEmpty()) {
            java.lang.String message = "cluster={0}, type={1}, transfers={2}, replacements={3}, insertions={4}, configurations={5}";
            message = java.text.MessageFormat.format(message, clusterName, configType, transfers, replacements, insertions, keyValuePairs);
            java.lang.StringBuilder buffer = new java.lang.StringBuilder("Skipping this configuration task since none of the conditions were met and there are no transfers, replacements, or insertions.").append("\n");
            buffer.append(message);
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", buffer.toString(), "");
        }
        if (((null == clusterName) || (null == configType)) || (((keyValuePairs.isEmpty() && transfers.isEmpty()) && replacements.isEmpty()) && insertions.isEmpty())) {
            java.lang.String message = "cluster={0}, type={1}, transfers={2}, replacements={3}, insertions={4}, configurations={5}";
            message = java.text.MessageFormat.format(message, clusterName, configType, transfers, replacements, insertions, keyValuePairs);
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", message);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (desiredConfig == null) {
            throw new org.apache.ambari.server.AmbariException("Could not find desired config type with name " + configType);
        }
        org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
        if (config == null) {
            throw new org.apache.ambari.server.AmbariException("Could not find config type with name " + configType);
        }
        org.apache.ambari.server.state.StackId configStack = config.getStackId();
        java.util.Map<java.lang.String, java.lang.String> base = config.getProperties();
        java.util.Map<java.lang.String, java.lang.String> newValues = new java.util.HashMap<>(base);
        boolean changedValues = false;
        java.lang.StringBuilder outputBuffer = new java.lang.StringBuilder(250);
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer : transfers) {
            switch (transfer.operation) {
                case COPY :
                    java.lang.String valueToCopy = null;
                    if (null == transfer.fromType) {
                        valueToCopy = base.get(transfer.fromKey);
                    } else {
                        org.apache.ambari.server.state.Config other = cluster.getDesiredConfigByType(transfer.fromType);
                        if (null != other) {
                            java.util.Map<java.lang.String, java.lang.String> otherValues = other.getProperties();
                            if (otherValues.containsKey(transfer.fromKey)) {
                                valueToCopy = otherValues.get(transfer.fromKey);
                            }
                        }
                    }
                    if (org.apache.commons.lang.StringUtils.isBlank(valueToCopy) && (!org.apache.commons.lang.StringUtils.isBlank(transfer.defaultValue))) {
                        valueToCopy = transfer.defaultValue;
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(valueToCopy)) {
                        if (transfer.coerceTo != null) {
                            switch (transfer.coerceTo) {
                                case YAML_ARRAY :
                                    {
                                        java.lang.String[] splitValues = org.apache.commons.lang.StringUtils.split(valueToCopy, ',');
                                        java.util.List<java.lang.String> quotedValues = new java.util.ArrayList<>(splitValues.length);
                                        for (java.lang.String splitValue : splitValues) {
                                            quotedValues.add(("'" + org.apache.commons.lang.StringUtils.trim(splitValue)) + "'");
                                        }
                                        valueToCopy = ("[" + org.apache.commons.lang.StringUtils.join(quotedValues, ',')) + "]";
                                        break;
                                    }
                                default :
                                    break;
                            }
                        }
                        changedValues = true;
                        newValues.put(transfer.toKey, valueToCopy);
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Created {0}/{1} = \"{2}\"", configType, transfer.toKey, org.apache.ambari.server.serveraction.upgrades.ConfigureAction.mask(transfer, valueToCopy)));
                    }
                    break;
                case MOVE :
                    if (newValues.containsKey(transfer.fromKey)) {
                        newValues.put(transfer.toKey, newValues.remove(transfer.fromKey));
                        changedValues = true;
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Renamed {0}/{1} to {2}/{3}", configType, transfer.fromKey, configType, transfer.toKey));
                    } else if (org.apache.commons.lang.StringUtils.isNotBlank(transfer.defaultValue)) {
                        newValues.put(transfer.toKey, transfer.defaultValue);
                        changedValues = true;
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Created {0}/{1} with default value \"{2}\"", configType, transfer.toKey, org.apache.ambari.server.serveraction.upgrades.ConfigureAction.mask(transfer, transfer.defaultValue)));
                    }
                    break;
                case DELETE :
                    if (org.apache.ambari.server.serveraction.upgrades.ConfigureAction.ALL_SYMBOL.equals(transfer.deleteKey)) {
                        newValues.clear();
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Deleted all keys from {0}", configType));
                        for (java.lang.String keeper : transfer.keepKeys) {
                            if (base.containsKey(keeper) && (base.get(keeper) != null)) {
                                newValues.put(keeper, base.get(keeper));
                                updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Preserved {0}/{1} after delete", configType, keeper));
                            }
                        }
                        if (transfer.preserveEdits) {
                            java.util.List<java.lang.String> edited = findValuesToPreserve(clusterName, config);
                            for (java.lang.String changed : edited) {
                                newValues.put(changed, base.get(changed));
                                updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Preserved {0}/{1} after delete", configType, changed));
                            }
                        }
                        changedValues = true;
                    } else {
                        newValues.remove(transfer.deleteKey);
                        changedValues = true;
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Deleted {0}/{1}", configType, transfer.deleteKey));
                    }
                    break;
            }
        }
        if (!keyValuePairs.isEmpty()) {
            for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValuePair : keyValuePairs) {
                java.lang.String key = keyValuePair.key;
                java.lang.String value = keyValuePair.value;
                if (null != key) {
                    java.lang.String oldValue = base.get(key);
                    if (org.apache.commons.lang.StringUtils.equals(value, oldValue)) {
                        if (sourceStackId.equals(targetStackId) && (!changedValues)) {
                            updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("{0}/{1} for cluster {2} would not change, skipping setting", configType, key, clusterName));
                            continue;
                        }
                    }
                    newValues.put(key, value);
                    final java.lang.String message;
                    if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
                        message = java.text.MessageFormat.format("{0}/{1} changed to an empty value", configType, key);
                    } else {
                        message = java.text.MessageFormat.format("{0}/{1} changed to \"{2}\"", configType, key, org.apache.ambari.server.serveraction.upgrades.ConfigureAction.mask(keyValuePair, value));
                    }
                    updateBufferWithMessage(outputBuffer, message);
                }
            }
        }
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replacement : replacements) {
            java.lang.String toReplace = newValues.get(replacement.key);
            if (org.apache.commons.lang.StringUtils.isNotBlank(toReplace)) {
                if (!toReplace.contains(replacement.find)) {
                    updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("String \"{0}\" was not found in {1}/{2}", replacement.find, configType, replacement.key));
                } else {
                    java.lang.String replaced = org.apache.commons.lang.StringUtils.replace(toReplace, replacement.find, replacement.replaceWith);
                    newValues.put(replacement.key, replaced);
                    if (org.apache.commons.lang.StringUtils.isEmpty(replacement.replaceWith)) {
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Removed \"{0}\" from {1}/{2}", replacement.find, configType, replacement.key));
                    } else {
                        updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Replaced {0}/{1} containing \"{2}\" with \"{3}\"", configType, replacement.key, replacement.find, replacement.replaceWith));
                    }
                }
            } else {
                updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Skipping replacement for {0}/{1} because it does not exist or is empty.", configType, replacement.key));
            }
        }
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert insert : insertions) {
            java.lang.String valueToInsertInto = newValues.get(insert.key);
            if (org.apache.commons.lang.StringUtils.isNotBlank(valueToInsertInto)) {
                if (org.apache.commons.lang.StringUtils.contains(valueToInsertInto, insert.value)) {
                    updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Skipping insertion for {0}/{1} because it already contains {2}", configType, insert.key, insert.value));
                    continue;
                }
                java.lang.String valueToInsert = insert.value;
                if (insert.newlineBefore) {
                    valueToInsert = java.lang.System.lineSeparator() + valueToInsert;
                }
                if (insert.newlineAfter) {
                    valueToInsert = valueToInsert + java.lang.System.lineSeparator();
                }
                switch (insert.insertType) {
                    case APPEND :
                        valueToInsertInto = valueToInsertInto + valueToInsert;
                        break;
                    case PREPEND :
                        valueToInsertInto = valueToInsert + valueToInsertInto;
                        break;
                    default :
                        org.apache.ambari.server.serveraction.upgrades.ConfigureAction.LOG.error("Unable to insert {0}/{1} with unknown insertion type of {2}", configType, insert.key, insert.insertType);
                        break;
                }
                newValues.put(insert.key, valueToInsertInto);
                updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Updated {0}/{1} by inserting \"{2}\"", configType, insert.key, insert.value));
            } else {
                updateBufferWithMessage(outputBuffer, java.text.MessageFormat.format("Skipping insertion for {0}/{1} because it does not exist or is empty.", configType, insert.key));
            }
        }
        if ((!targetStackId.equals(sourceStackId)) && targetStackId.equals(configStack)) {
            config.setProperties(newValues);
            config.save();
            m_metadataHolder.get().updateData(m_ambariManagementController.get().getClusterMetadataOnConfigsUpdate(cluster));
            m_agentConfigsHolder.get().updateData(cluster.getClusterId(), null);
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", outputBuffer.toString(), "");
        }
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        java.lang.String serviceVersionNote = java.lang.String.format("%s %s %s", direction.getText(true), direction.getPreposition(), upgradeContext.getRepositoryVersion().getVersion());
        java.lang.String auditName = getExecutionCommand().getRoleParams().get(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME);
        if (auditName == null) {
            auditName = m_configuration.getAnonymousAuditName();
        }
        m_configHelper.createConfigType(cluster, targetStackId, m_controller, configType, newValues, auditName, serviceVersionNote);
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", outputBuffer.toString(), "");
    }

    private java.util.List<java.lang.String> findValuesToPreserve(java.lang.String clusterName, org.apache.ambari.server.state.Config config) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> conflicts = m_mergeHelper.getConflicts(clusterName, config.getStackId());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue> conflictMap = conflicts.get(config.getType());
        if ((null != conflictMap) && (!conflictMap.isEmpty())) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue> entry : conflictMap.entrySet()) {
                org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue twv = entry.getValue();
                if (null == twv.oldStackValue) {
                    result.add(entry.getKey());
                } else if ((null != twv.savedValue) && (!twv.oldStackValue.equals(twv.savedValue))) {
                    result.add(entry.getKey());
                }
            }
        }
        java.lang.String configType = config.getType();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.state.StackId oldStack = cluster.getCurrentStackVersion();
        java.util.Set<java.lang.String> stackPropertiesForType = new java.util.HashSet<>(50);
        for (java.lang.String serviceName : cluster.getServices().keySet()) {
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = m_ambariMetaInfo.get().getServiceProperties(oldStack.getStackName(), oldStack.getStackVersion(), serviceName);
            for (org.apache.ambari.server.state.PropertyInfo property : serviceProperties) {
                java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(property.getFilename());
                if (type.equals(configType)) {
                    stackPropertiesForType.add(property.getName());
                }
            }
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = m_ambariMetaInfo.get().getStackProperties(oldStack.getStackName(), oldStack.getStackVersion());
        for (org.apache.ambari.server.state.PropertyInfo property : stackProperties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(property.getFilename());
            if (type.equals(configType)) {
                stackPropertiesForType.add(property.getName());
            }
        }
        java.util.Map<java.lang.String, java.lang.String> base = config.getProperties();
        java.util.Set<java.lang.String> baseKeys = base.keySet();
        for (java.lang.String baseKey : baseKeys) {
            if (!stackPropertiesForType.contains(baseKey)) {
                result.add(baseKey);
            }
        }
        return result;
    }

    private static java.lang.String mask(org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Masked mask, java.lang.String value) {
        if (mask.mask) {
            return org.apache.commons.lang.StringUtils.repeat("*", value.length());
        }
        return value;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> getAllowedReplacements(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> allowedReplacements = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replacement : replacements) {
            if (isOperationAllowed(cluster, configType, replacement.key, replacement)) {
                allowedReplacements.add(replacement);
            }
        }
        return allowedReplacements;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> getAllowedSets(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> sets) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> allowedSets = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue configurationKeyValue : sets) {
            if (isOperationAllowed(cluster, configType, configurationKeyValue.key, configurationKeyValue)) {
                allowedSets.add(configurationKeyValue);
            }
        }
        return allowedSets;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> getAllowedTransfers(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers) {
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> allowedTransfers = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer : transfers) {
            java.lang.String key;
            if (transfer.operation == org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE) {
                key = transfer.deleteKey;
            } else {
                key = transfer.fromKey;
            }
            if (isOperationAllowed(cluster, configType, key, transfer)) {
                allowedTransfers.add(transfer);
            }
        }
        return allowedTransfers;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> getAllowedInsertions(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> insertions) {
        return insertions.stream().filter(insertion -> isOperationAllowed(cluster, configType, insertion.key, insertion)).collect(java.util.stream.Collectors.toList());
    }

    private boolean isOperationAllowed(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String targetPropertyKey, org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConditionalField operationItem) {
        boolean isAllowed = true;
        boolean ifKeyIsNotBlank = org.apache.commons.lang.StringUtils.isNotBlank(operationItem.ifKey);
        boolean ifTypeIsNotBlank = org.apache.commons.lang.StringUtils.isNotBlank(operationItem.ifType);
        boolean ifValueIsBlank = org.apache.commons.lang.StringUtils.isBlank(operationItem.ifValue);
        if (((ifKeyIsNotBlank && ifTypeIsNotBlank) && ifValueIsBlank) && (operationItem.ifKeyState == org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT)) {
            boolean keyPresent = getDesiredConfigurationKeyPresence(cluster, operationItem.ifType, operationItem.ifKey);
            if (keyPresent) {
                org.apache.ambari.server.serveraction.upgrades.ConfigureAction.LOG.info("Skipping property operation for {}/{} as the key {} for {} is present", configType, targetPropertyKey, operationItem.ifKey, operationItem.ifType);
                isAllowed = false;
            }
        } else if (((ifKeyIsNotBlank && ifTypeIsNotBlank) && ifValueIsBlank) && (operationItem.ifKeyState == org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT)) {
            boolean keyPresent = getDesiredConfigurationKeyPresence(cluster, operationItem.ifType, operationItem.ifKey);
            if (!keyPresent) {
                org.apache.ambari.server.serveraction.upgrades.ConfigureAction.LOG.info("Skipping property operation for {}/{} as the key {} for {} is not present", configType, targetPropertyKey, operationItem.ifKey, operationItem.ifType);
                isAllowed = false;
            }
        } else if ((ifKeyIsNotBlank && ifTypeIsNotBlank) && (!ifValueIsBlank)) {
            java.lang.String checkValue = getDesiredConfigurationValue(cluster, operationItem.ifType, operationItem.ifKey);
            if (operationItem.ifKeyState == org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT) {
                boolean keyPresent = getDesiredConfigurationKeyPresence(cluster, operationItem.ifType, operationItem.ifKey);
                if (!keyPresent) {
                    return true;
                }
            }
            if (operationItem.ifValueMatchType == org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.PARTIAL) {
                if ((!org.apache.commons.lang.StringUtils.containsIgnoreCase(checkValue, operationItem.ifValue)) ^ operationItem.ifValueNotMatched) {
                    org.apache.ambari.server.serveraction.upgrades.ConfigureAction.LOG.info("Skipping property operation for {}/{} as the value {} for {}/{} is not found in {}", configType, targetPropertyKey, operationItem.ifValue, operationItem.ifType, operationItem.ifKey, checkValue);
                    isAllowed = false;
                }
            } else if ((!org.apache.commons.lang.StringUtils.equalsIgnoreCase(operationItem.ifValue, checkValue)) ^ operationItem.ifValueNotMatched) {
                org.apache.ambari.server.serveraction.upgrades.ConfigureAction.LOG.info("Skipping property operation for {}/{} as the value {} for {}/{} is not equal to {}", configType, targetPropertyKey, checkValue, operationItem.ifType, operationItem.ifKey, operationItem.ifValue);
                isAllowed = false;
            }
        }
        return isAllowed;
    }

    private boolean getDesiredConfigurationKeyPresence(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyKey) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (null == desiredConfig) {
            return false;
        }
        org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
        if (null == config) {
            return false;
        }
        return config.getProperties().containsKey(propertyKey);
    }

    private java.lang.String getDesiredConfigurationValue(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyKey) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (null == desiredConfig) {
            return null;
        }
        org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
        if (null == config) {
            return null;
        }
        return config.getProperties().get(propertyKey);
    }

    private void updateBufferWithMessage(java.lang.StringBuilder buffer, java.lang.String message) {
        buffer.append(message).append(java.lang.System.lineSeparator());
    }
}