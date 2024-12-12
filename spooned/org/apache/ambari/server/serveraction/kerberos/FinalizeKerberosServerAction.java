package org.apache.ambari.server.serveraction.kerberos;
@java.lang.SuppressWarnings("UnstableApiUsage")
public class FinalizeKerberosServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.class);

    private final org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> m_locksByKeytab = com.google.common.util.concurrent.Striped.lazyWeakLock(25);

    @com.google.inject.Inject
    public FinalizeKerberosServerAction(org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder) {
        this.topologyHolder = topologyHolder;
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        if (resolvedPrincipal != null) {
            if (org.apache.ambari.server.utils.StageUtils.getHostName().equals(resolvedPrincipal.getHostName())) {
                java.util.Map<java.lang.String, java.lang.String> principalPasswordMap = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalPasswordMap(requestSharedDataContext);
                if ((principalPasswordMap == null) || (!principalPasswordMap.containsKey(resolvedPrincipal.getPrincipal()))) {
                    java.lang.String keytabFilePath = resolvedPrincipal.getKeytabPath();
                    if (org.apache.commons.lang3.StringUtils.isEmpty(keytabFilePath)) {
                        return null;
                    }
                    java.util.concurrent.locks.Lock lock = m_locksByKeytab.get(keytabFilePath);
                    lock.lock();
                    try {
                        java.util.Set<java.lang.String> visited = ((java.util.Set<java.lang.String>) (requestSharedDataContext.get(this.getClass().getName() + "_visited")));
                        if (!visited.contains(keytabFilePath)) {
                            java.lang.String ownerName = resolvedPrincipal.getResolvedKerberosKeytab().getOwnerName();
                            java.lang.String ownerAccess = resolvedPrincipal.getResolvedKerberosKeytab().getOwnerAccess();
                            boolean ownerWritable = "w".equalsIgnoreCase(ownerAccess) || "rw".equalsIgnoreCase(ownerAccess);
                            boolean ownerReadable = "r".equalsIgnoreCase(ownerAccess) || "rw".equalsIgnoreCase(ownerAccess);
                            java.lang.String groupName = resolvedPrincipal.getResolvedKerberosKeytab().getGroupName();
                            java.lang.String groupAccess = resolvedPrincipal.getResolvedKerberosKeytab().getGroupAccess();
                            boolean groupWritable = "w".equalsIgnoreCase(groupAccess) || "rw".equalsIgnoreCase(groupAccess);
                            boolean groupReadable = "r".equalsIgnoreCase(groupAccess) || "rw".equalsIgnoreCase(groupAccess);
                            org.apache.ambari.server.utils.ShellCommandUtil.Result result;
                            java.lang.String message;
                            result = org.apache.ambari.server.utils.ShellCommandUtil.setFileOwner(keytabFilePath, ownerName);
                            if (result.isSuccessful()) {
                                message = java.lang.String.format("Updated the owner of the keytab file at %s to %s", keytabFilePath, ownerName);
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.info(message);
                                actionLog.writeStdOut(message);
                            } else {
                                message = java.lang.String.format("Failed to update the owner of the keytab file at %s to %s: %s", keytabFilePath, ownerName, result.getStderr());
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.error(message);
                                actionLog.writeStdOut(message);
                                actionLog.writeStdErr(message);
                            }
                            result = org.apache.ambari.server.utils.ShellCommandUtil.setFileGroup(keytabFilePath, groupName);
                            if (result.isSuccessful()) {
                                message = java.lang.String.format("Updated the group of the keytab file at %s to %s", keytabFilePath, groupName);
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.info(message);
                                actionLog.writeStdOut(message);
                            } else {
                                message = java.lang.String.format("Failed to update the group of the keytab file at %s to %s: %s", keytabFilePath, groupName, result.getStderr());
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.error(message);
                                actionLog.writeStdOut(message);
                                actionLog.writeStdErr(message);
                            }
                            result = org.apache.ambari.server.utils.ShellCommandUtil.setFileMode(keytabFilePath, ownerReadable, ownerWritable, false, groupReadable, groupWritable, false, false, false, false);
                            if (result.isSuccessful()) {
                                message = java.lang.String.format("Updated the access mode of the keytab file at %s to owner:'%s' and group:'%s'", keytabFilePath, ownerAccess, groupAccess);
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.info(message);
                                actionLog.writeStdOut(message);
                            } else {
                                message = java.lang.String.format("Failed to update the access mode of the keytab file at %s to owner:'%s' and group:'%s': %s", keytabFilePath, ownerAccess, groupAccess, result.getStderr());
                                org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.error(message);
                                actionLog.writeStdOut(message);
                                actionLog.writeStdErr(message);
                            }
                            visited.add(keytabFilePath);
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String dataDirectoryPath = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        if (org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getKDCType(getCommandParameters()) != org.apache.ambari.server.serveraction.kerberos.KDCType.NONE) {
            requestSharedDataContext.put(this.getClass().getName() + "_visited", new java.util.HashSet<java.lang.String>());
            processIdentities(requestSharedDataContext);
            requestSharedDataContext.remove(this.getClass().getName() + "_visited");
        }
        deleteDataDirectory(dataDirectoryPath);
        return sendTopologyUpdateEvent();
    }

    private org.apache.ambari.server.agent.CommandReport sendTopologyUpdateEvent() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.agent.CommandReport commandReport = null;
        try {
            final org.apache.ambari.server.events.TopologyUpdateEvent updateEvent = topologyHolder.getCurrentData();
            topologyHolder.updateData(updateEvent);
        } catch (java.lang.Exception e) {
            java.lang.String message = "Could not send topology update event when enabling kerberos";
            actionLog.writeStdErr(message);
            org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.LOG.error(message, e);
            commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
        }
        return commandReport == null ? createCompletedCommandReport() : commandReport;
    }
}