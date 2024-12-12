package org.apache.ambari.server.events.publishers;
import javax.annotation.Nullable;
import org.apache.commons.collections.CollectionUtils;
import static org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS;
import static org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB;
import static org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB;
@com.google.inject.Singleton
public class AgentCommandsPublisher {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.utils.ThreadPools threadPools;

    public void sendAgentCommand(com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> agentCommands) throws org.apache.ambari.server.AmbariRuntimeException {
        if ((agentCommands != null) && (!agentCommands.isEmpty())) {
            java.util.Map<java.lang.Long, java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster>> executionCommandsClusters = new java.util.concurrent.ConcurrentHashMap<>();
            java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> clusterDesiredConfigs = new java.util.concurrent.ConcurrentHashMap<>();
            try {
                threadPools.getAgentPublisherCommandsPool().submit(() -> {
                    agentCommands.entries().stream().parallel().forEach(acHostEntry -> {
                        java.lang.Long hostId = acHostEntry.getKey();
                        org.apache.ambari.server.agent.AgentCommand ac = acHostEntry.getValue();
                        java.lang.Long clusterId = null;
                        if (ac instanceof org.apache.ambari.server.agent.ExecutionCommand) {
                            try {
                                clusterId = java.lang.Long.valueOf(((org.apache.ambari.server.agent.ExecutionCommand) (ac)).getClusterId());
                                if (clusterId >= 0) {
                                    if (!clusterDesiredConfigs.containsKey(clusterId)) {
                                        clusterDesiredConfigs.put(clusterId, clusters.getCluster(clusterId).getDesiredConfigs());
                                    }
                                } else {
                                    org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.warn("The cluster not found or has not been created yet. clusterID={}.", clusterId);
                                }
                            } catch (java.lang.NumberFormatException | org.apache.ambari.server.AmbariException e) {
                                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.error("Exception on sendAgentCommand", e);
                            }
                        }
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = ((clusterId != null) && clusterDesiredConfigs.containsKey(clusterId)) ? clusterDesiredConfigs.get(clusterId) : null;
                        populateExecutionCommandsClusters(executionCommandsClusters, hostId, ac, desiredConfigs);
                    });
                }).get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.error("Exception on sendAgentCommand", e);
            }
            try {
                threadPools.getAgentPublisherCommandsPool().submit(() -> {
                    executionCommandsClusters.entrySet().stream().parallel().forEach(entry -> {
                        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ExecutionCommandEvent(entry.getKey(), agentConfigsHolder.initializeDataIfNeeded(entry.getKey(), true).getTimestamp(), entry.getValue()));
                    });
                }).get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.error("Exception on sendAgentCommand", e);
            }
        }
    }

    public void sendAgentCommand(java.lang.Long hostId, org.apache.ambari.server.agent.AgentCommand agentCommand) throws org.apache.ambari.server.AmbariRuntimeException {
        com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> agentCommands = com.google.common.collect.ArrayListMultimap.create();
        agentCommands.put(hostId, agentCommand);
        sendAgentCommand(agentCommands);
    }

    private void populateExecutionCommandsClusters(java.util.Map<java.lang.Long, java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster>> executionCommandsClusters, java.lang.Long hostId, org.apache.ambari.server.agent.AgentCommand ac, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariRuntimeException {
        if (org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.isDebugEnabled()) {
            try {
                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.debug("Sending command string = " + org.apache.ambari.server.utils.StageUtils.jaxbToString(ac));
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.AmbariRuntimeException("Could not get jaxb string for command", e);
            }
        }
        switch (ac.getCommandType()) {
            case BACKGROUND_EXECUTION_COMMAND :
            case EXECUTION_COMMAND :
                {
                    org.apache.ambari.server.agent.ExecutionCommand ec = ((org.apache.ambari.server.agent.ExecutionCommand) (ac));
                    org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.info("AgentCommandsPublisher.sendCommands: sending ExecutionCommand for host {}, role {}, roleCommand {}, and command ID {}, task ID {}", ec.getHostname(), ec.getRole(), ec.getRoleCommand(), ec.getCommandId(), ec.getTaskId());
                    java.util.Map<java.lang.String, java.lang.String> hlp = ec.getCommandParams();
                    if (hlp != null) {
                        java.lang.String customCommand = hlp.get("custom_command");
                        if ((org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB.equalsIgnoreCase(customCommand) || org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB.equalsIgnoreCase(customCommand)) || org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS.equalsIgnoreCase(customCommand)) {
                            org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.info(java.lang.String.format("%s called", customCommand));
                            try {
                                injectKeytab(ec, customCommand, clusters.getHostById(hostId).getHostName(), desiredConfigs);
                            } catch (java.io.IOException e) {
                                throw new org.apache.ambari.server.AmbariRuntimeException("Could not inject keytab into command", e);
                            }
                        }
                    }
                    java.lang.String clusterName = ec.getClusterName();
                    java.lang.String clusterId = "-1";
                    if (clusterName != null) {
                        try {
                            clusterId = java.lang.Long.toString(clusters.getCluster(clusterName).getClusterId());
                        } catch (org.apache.ambari.server.AmbariException e) {
                            throw new org.apache.ambari.server.AmbariRuntimeException(e);
                        }
                    }
                    ec.setClusterId(clusterId);
                    prepareExecutionCommandsClusters(executionCommandsClusters, hostId, clusterId);
                    executionCommandsClusters.get(hostId).get(clusterId).getExecutionCommands().add(((org.apache.ambari.server.agent.ExecutionCommand) (ac)));
                    break;
                }
            case CANCEL_COMMAND :
                {
                    org.apache.ambari.server.agent.CancelCommand cc = ((org.apache.ambari.server.agent.CancelCommand) (ac));
                    java.lang.String clusterId = java.lang.Long.toString(hostRoleCommandDAO.findByPK(cc.getTargetTaskId()).getStage().getClusterId());
                    prepareExecutionCommandsClusters(executionCommandsClusters, hostId, clusterId);
                    executionCommandsClusters.get(hostId).get(clusterId).getCancelCommands().add(cc);
                    break;
                }
            default :
                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.error("There is no action for agent command =" + ac.getCommandType().name());
        }
    }

    private void prepareExecutionCommandsClusters(java.util.Map<java.lang.Long, java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster>> executionCommandsClusters, java.lang.Long hostId, java.lang.String clusterId) {
        if (!executionCommandsClusters.containsKey(hostId)) {
            executionCommandsClusters.put(hostId, new java.util.TreeMap<>());
        }
        if (!executionCommandsClusters.get(hostId).containsKey(clusterId)) {
            executionCommandsClusters.get(hostId).put(clusterId, new org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster(new java.util.ArrayList<>(), new java.util.ArrayList<>()));
        }
    }

    private void injectKeytab(org.apache.ambari.server.agent.ExecutionCommand ec, java.lang.String command, java.lang.String targetHost, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor processor = org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor.getInstance(command, clusters, ec, kerberosKeytabController);
        if (processor != null) {
            ec.setKerberosCommandParams(processor.process(targetHost, desiredConfigs));
        }
    }

    private static abstract class KerberosCommandParameterProcessor {
        protected final org.apache.ambari.server.state.Clusters clusters;

        protected final org.apache.ambari.server.agent.ExecutionCommand executionCommand;

        protected final org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController;

        protected java.util.List<java.util.Map<java.lang.String, java.lang.String>> kcp;

        protected KerberosCommandParameterProcessor(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController) {
            this.clusters = clusters;
            this.executionCommand = executionCommand;
            this.kerberosKeytabController = kerberosKeytabController;
            kcp = executionCommand.getKerberosCommandParams();
        }

        public static org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor getInstance(java.lang.String command, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController) {
            if (org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB.equalsIgnoreCase(command)) {
                return new org.apache.ambari.server.events.publishers.AgentCommandsPublisher.SetKeytabCommandParameterProcessor(clusters, executionCommand, kerberosKeytabController);
            }
            if (org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS.equalsIgnoreCase(command)) {
                return new org.apache.ambari.server.events.publishers.AgentCommandsPublisher.CheckKeytabsCommandParameterProcessor(clusters, executionCommand, kerberosKeytabController);
            }
            if (org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB.equalsIgnoreCase(command)) {
                return new org.apache.ambari.server.events.publishers.AgentCommandsPublisher.RemoveKeytabCommandParameterProcessor(clusters, executionCommand, kerberosKeytabController);
            }
            return null;
        }

        public java.util.List<java.util.Map<java.lang.String, java.lang.String>> process(java.lang.String targetHost, @javax.annotation.Nullable
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KerberosCommandParameters kerberosCommandParameters = new org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KerberosCommandParameters(executionCommand);
            java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter = getServiceComponentFilter(kerberosCommandParameters.getServiceComponentFilter());
            final java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = (serviceComponentFilter == null) ? null : kerberosKeytabController.getServiceIdentities(executionCommand.getClusterName(), serviceComponentFilter.keySet(), desiredConfigs);
            final java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> keytabsToInject = kerberosKeytabController.getFilteredKeytabs(serviceIdentities, kerberosCommandParameters.getHostFilter(), kerberosCommandParameters.getIdentityFilter());
            keytabsToInject.forEach(resolvedKeytab -> resolvedKeytab.getPrincipals().forEach(resolvedPrincipal -> {
                java.lang.String hostName = resolvedPrincipal.getHostName();
                if (targetHost.equalsIgnoreCase(hostName)) {
                    try {
                        process(targetHost, resolvedKeytab, resolvedPrincipal, serviceComponentFilter);
                    } catch (java.io.IOException e) {
                        throw new org.apache.ambari.server.AmbariRuntimeException("Could not inject keytabs to enable kerberos", e);
                    }
                }
            }));
            return kcp;
        }

        protected void process(java.lang.String hostName, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKeytab, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws java.io.IOException {
            java.util.Map<java.lang.String, java.lang.String> keytabMap = new java.util.HashMap<>();
            keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME, hostName);
            keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL, resolvedPrincipal.getPrincipal());
            keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH, resolvedKeytab.getFile());
            kcp.add(keytabMap);
        }

        protected java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> getServiceComponentFilter(java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws org.apache.ambari.server.AmbariException {
            return serviceComponentFilter;
        }
    }

    private static class SetKeytabCommandParameterProcessor extends org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor {
        private final java.lang.String dataDir;

        private SetKeytabCommandParameterProcessor(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController) {
            super(clusters, executionCommand, kerberosKeytabController);
            dataDir = executionCommand.getCommandParams().get(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        }

        @java.lang.Override
        protected void process(java.lang.String hostName, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKeytab, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws java.io.IOException {
            if (dataDir != null) {
                java.lang.String principal = resolvedPrincipal.getPrincipal();
                java.lang.String keytabFilePath = resolvedKeytab.getFile();
                org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.info("Processing principal {} for host {} and keytab file path {}", principal, hostName, keytabFilePath);
                if (keytabFilePath != null) {
                    java.lang.String sha1Keytab = org.apache.commons.codec.digest.DigestUtils.sha256Hex(keytabFilePath);
                    java.io.File keytabFile = java.nio.file.Paths.get(dataDir, hostName, sha1Keytab).toFile();
                    if (keytabFile.canRead()) {
                        java.util.Map<java.lang.String, java.lang.String> keytabMap = new java.util.HashMap<>();
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.HOSTNAME, hostName);
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.PRINCIPAL, principal);
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_PATH, keytabFilePath);
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_NAME, resolvedKeytab.getOwnerName());
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_OWNER_ACCESS, resolvedKeytab.getOwnerAccess());
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_NAME, resolvedKeytab.getGroupName());
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileReader.KEYTAB_FILE_GROUP_ACCESS, resolvedKeytab.getGroupAccess());
                        java.io.BufferedInputStream bufferedIn = new java.io.BufferedInputStream(new java.io.FileInputStream(keytabFile));
                        byte[] keytabContent;
                        try {
                            keytabContent = org.apache.commons.io.IOUtils.toByteArray(bufferedIn);
                        } finally {
                            bufferedIn.close();
                        }
                        java.lang.String keytabContentBase64 = org.apache.commons.codec.binary.Base64.encodeBase64String(keytabContent);
                        keytabMap.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KEYTAB_CONTENT_BASE64, keytabContentBase64);
                        kcp.add(keytabMap);
                    } else {
                        org.apache.ambari.server.events.publishers.AgentCommandsPublisher.LOG.warn("Keytab file for principal {} and host {} can not to be read at path {}", principal, hostName, keytabFile.getAbsolutePath());
                    }
                }
            }
        }

        @java.lang.Override
        protected java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> getServiceComponentFilter(java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws org.apache.ambari.server.AmbariException {
            return kerberosKeytabController.adjustServiceComponentFilter(clusters.getCluster(executionCommand.getClusterName()), false, serviceComponentFilter);
        }
    }

    private static class CheckKeytabsCommandParameterProcessor extends org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor {
        private CheckKeytabsCommandParameterProcessor(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController) {
            super(clusters, executionCommand, kerberosKeytabController);
        }
    }

    private static class RemoveKeytabCommandParameterProcessor extends org.apache.ambari.server.events.publishers.AgentCommandsPublisher.KerberosCommandParameterProcessor {
        private RemoveKeytabCommandParameterProcessor(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController) {
            super(clusters, executionCommand, kerberosKeytabController);
        }

        @java.lang.Override
        protected void process(java.lang.String hostName, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKeytab, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws java.io.IOException {
            if (shouldRemove(hostName, resolvedKeytab, resolvedPrincipal, serviceComponentFilter)) {
                super.process(hostName, resolvedKeytab, resolvedPrincipal, serviceComponentFilter);
            }
        }

        private boolean shouldRemove(java.lang.String hostname, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) {
            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab existingResolvedKeytab = kerberosKeytabController.getKeytabByFile(resolvedKerberosKeytab.getFile());
            if (existingResolvedKeytab == null) {
                return true;
            }
            java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> principals = existingResolvedKeytab.getPrincipals();
            for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal : principals) {
                if (hostname.equals(principal.getHostName()) && principal.getPrincipal().equals(resolvedPrincipal.getPrincipal())) {
                    com.google.common.collect.Multimap<java.lang.String, java.lang.String> temp = principal.getServiceMapping();
                    java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceMapping = (temp == null) ? new java.util.HashMap<>() : new java.util.HashMap<>(temp.asMap());
                    if (serviceComponentFilter == null) {
                        serviceMapping.clear();
                    } else {
                        for (java.util.Map.Entry<java.lang.String, ? extends java.util.Collection<java.lang.String>> entry : serviceComponentFilter.entrySet()) {
                            java.lang.String service = entry.getKey();
                            java.util.Collection<java.lang.String> components = entry.getValue();
                            if (serviceMapping.containsKey(service)) {
                                if (org.apache.commons.collections.CollectionUtils.isEmpty(components) || org.apache.commons.collections.CollectionUtils.isEmpty(serviceMapping.get(service))) {
                                    serviceMapping.remove(service);
                                } else {
                                    java.util.Collection<java.lang.String> leftOver = new java.util.HashSet<java.lang.String>(serviceMapping.get(service));
                                    leftOver.removeAll(components);
                                    if (org.apache.commons.collections.CollectionUtils.isEmpty(leftOver)) {
                                        serviceMapping.remove(service);
                                    } else {
                                        serviceMapping.put(service, leftOver);
                                    }
                                }
                            }
                        }
                    }
                    if (serviceMapping.size() > 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}