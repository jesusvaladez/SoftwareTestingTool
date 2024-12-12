package org.apache.ambari.server.serveraction.kerberos;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
@java.lang.SuppressWarnings("UnstableApiUsage")
public class CreateKeytabFilesServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> m_locksByKeytab = com.google.common.util.concurrent.Striped.lazyWeakLock(25);

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> visitedIdentities = new java.util.concurrent.ConcurrentHashMap<>();

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        return processIdentities(requestSharedDataContext);
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder auditEventBuilder = org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.builder();
        auditEventBuilder.withTimestamp(java.lang.System.currentTimeMillis());
        auditEventBuilder.withRequestId(getHostRoleCommand() != null ? getHostRoleCommand().getRequestId() : -1);
        auditEventBuilder.withTaskId(getHostRoleCommand() != null ? getHostRoleCommand().getTaskId() : -1);
        org.apache.ambari.server.agent.CommandReport commandReport = null;
        java.lang.String message = null;
        java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> keytabsToCreate = kerberosKeytabController.getFromPrincipalExceptServiceMapping(resolvedPrincipal);
        org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity = kerberosPrincipalDAO.find(resolvedPrincipal.getPrincipal());
        try {
            java.lang.String dataDirectory = getDataDirectoryPath();
            if (operationHandler == null) {
                message = java.lang.String.format("Failed to create keytab file for %s, missing KerberosOperationHandler", resolvedPrincipal.getPrincipal());
                actionLog.writeStdErr(message);
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
            } else if (dataDirectory == null) {
                message = "The data directory has not been set. Generated keytab files can not be stored.";
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
            } else {
                java.util.Map<java.lang.String, java.lang.String> principalPasswordMap = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalPasswordMap(requestSharedDataContext);
                java.util.Map<java.lang.String, java.lang.Integer> principalKeyNumberMap = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalKeyNumberMap(requestSharedDataContext);
                for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab rkk : keytabsToCreate) {
                    java.lang.String hostName = resolvedPrincipal.getHostName();
                    java.lang.String keytabFilePath = rkk.getFile();
                    if ((((hostName != null) && (!hostName.isEmpty())) && (keytabFilePath != null)) && (!keytabFilePath.isEmpty())) {
                        java.util.concurrent.locks.Lock lock = m_locksByKeytab.get(keytabFilePath);
                        lock.lock();
                        try {
                            java.util.Set<java.lang.String> visitedPrincipalKeys = visitedIdentities.get(resolvedPrincipal.getPrincipal());
                            java.lang.String visitationKey = java.lang.String.format("%s|%s", hostName, keytabFilePath);
                            if ((visitedPrincipalKeys == null) || (!visitedPrincipalKeys.contains(visitationKey))) {
                                java.lang.String password = principalPasswordMap.get(resolvedPrincipal.getPrincipal());
                                java.lang.Integer keyNumber = principalKeyNumberMap.get(resolvedPrincipal.getPrincipal());
                                message = java.lang.String.format("Creating keytab file for %s on host %s", resolvedPrincipal.getPrincipal(), hostName);
                                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.info(message);
                                actionLog.writeStdOut(message);
                                auditEventBuilder.withPrincipal(resolvedPrincipal.getPrincipal()).withHostName(hostName).withKeyTabFilePath(keytabFilePath);
                                java.io.File hostDirectory = new java.io.File(dataDirectory, hostName);
                                if ((!hostDirectory.exists()) && hostDirectory.mkdirs()) {
                                    ensureAmbariOnlyAccess(hostDirectory);
                                }
                                if (hostDirectory.exists()) {
                                    java.io.File destinationKeytabFile = new java.io.File(hostDirectory, org.apache.commons.codec.digest.DigestUtils.sha256Hex(keytabFilePath));
                                    boolean regenerateKeytabs = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getOperationType(getCommandParameters()) == org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.RECREATE_ALL;
                                    if (!includedInFilter) {
                                        regenerateKeytabs = false;
                                    }
                                    java.lang.String cachedKeytabPath = (principalEntity == null) ? null : principalEntity.getCachedKeytabPath();
                                    if (password == null) {
                                        if ((!regenerateKeytabs) && hostName.equalsIgnoreCase(org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_HOST_NAME)) {
                                            message = java.lang.String.format("Skipping keytab file for %s, missing password indicates nothing to do", resolvedPrincipal.getPrincipal());
                                            org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.info(message);
                                        } else if (cachedKeytabPath == null) {
                                            message = java.lang.String.format("Failed to create keytab for %s, missing cached file", resolvedPrincipal.getPrincipal());
                                            actionLog.writeStdErr(message);
                                            org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                                            commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                        } else {
                                            try {
                                                operationHandler.createKeytabFile(new java.io.File(cachedKeytabPath), destinationKeytabFile);
                                            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                                                message = java.lang.String.format("Failed to create keytab file for %s - %s", resolvedPrincipal.getPrincipal(), e.getMessage());
                                                actionLog.writeStdErr(message);
                                                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message, e);
                                                commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                            }
                                        }
                                    } else {
                                        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = createKeytab(resolvedPrincipal.getPrincipal(), principalEntity, password, keyNumber, operationHandler, visitedPrincipalKeys != null, true, actionLog);
                                        if (keytab != null) {
                                            try {
                                                if (operationHandler.createKeytabFile(keytab, destinationKeytabFile)) {
                                                    ensureAmbariOnlyAccess(destinationKeytabFile);
                                                    message = java.lang.String.format("Successfully created keytab file for %s at %s", resolvedPrincipal.getPrincipal(), destinationKeytabFile.getAbsolutePath());
                                                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.info(message);
                                                    auditEventBuilder.withPrincipal(resolvedPrincipal.getPrincipal()).withHostName(hostName).withKeyTabFilePath(destinationKeytabFile.getAbsolutePath());
                                                } else {
                                                    message = java.lang.String.format("Failed to create keytab file for %s at %s", resolvedPrincipal.getPrincipal(), destinationKeytabFile.getAbsolutePath());
                                                    actionLog.writeStdErr(message);
                                                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                                                    commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                                }
                                            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                                                message = java.lang.String.format("Failed to create keytab file for %s - %s", resolvedPrincipal.getPrincipal(), e.getMessage());
                                                actionLog.writeStdErr(message);
                                                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message, e);
                                                commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                            }
                                        } else {
                                            commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                        }
                                        if (visitedPrincipalKeys == null) {
                                            visitedPrincipalKeys = new java.util.HashSet<>();
                                            visitedIdentities.put(resolvedPrincipal.getPrincipal(), visitedPrincipalKeys);
                                        }
                                        visitedPrincipalKeys.add(visitationKey);
                                    }
                                } else {
                                    message = java.lang.String.format("Failed to create keytab file for %s, the container directory does not exist: %s", resolvedPrincipal.getPrincipal(), hostDirectory.getAbsolutePath());
                                    actionLog.writeStdErr(message);
                                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                                    commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                                }
                            } else {
                                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.debug("Skipping previously processed keytab for {} on host {}", resolvedPrincipal.getPrincipal(), hostName);
                            }
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }
        } finally {
            if ((commandReport != null) && org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString().equals(commandReport.getStatus())) {
                auditEventBuilder.withReasonOfFailure(message == null ? "Unknown error" : message);
            }
            if ((commandReport != null) || auditEventBuilder.hasPrincipal()) {
                auditLog(auditEventBuilder.build());
            }
        }
        return commandReport;
    }

    public org.apache.directory.server.kerberos.shared.keytab.Keytab createKeytab(java.lang.String principal, org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity, java.lang.String password, java.lang.Integer keyNumber, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, boolean checkCache, boolean canCache, org.apache.ambari.server.serveraction.ActionLog actionLog) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.debug("Creating keytab for {} with kvno {}", principal, keyNumber);
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = null;
        if (checkCache) {
            java.lang.String cachedKeytabPath = (principalEntity == null) ? null : principalEntity.getCachedKeytabPath();
            if (cachedKeytabPath != null) {
                try {
                    keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(new java.io.File(cachedKeytabPath));
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.warn("Failed to read the cached keytab for {}, recreating if possible - {}", principal, e.getMessage());
                }
            }
        }
        if (keytab == null) {
            try {
                keytab = operationHandler.createKeytab(principal, password, keyNumber);
                if (principalEntity != null) {
                    if (canCache) {
                        java.io.File cachedKeytabFile = cacheKeytab(principal, keytab);
                        java.lang.String previousCachedFilePath = principalEntity.getCachedKeytabPath();
                        java.lang.String cachedKeytabFilePath = (cachedKeytabFile.exists()) ? cachedKeytabFile.getAbsolutePath() : null;
                        if ((previousCachedFilePath == null) || (!previousCachedFilePath.equals(cachedKeytabFilePath))) {
                            principalEntity.setCachedKeytabPath(cachedKeytabFilePath);
                            kerberosPrincipalDAO.merge(principalEntity);
                        }
                        if (previousCachedFilePath != null) {
                            if (!new java.io.File(previousCachedFilePath).delete()) {
                                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.debug("Failed to remove orphaned cache file {}", previousCachedFilePath);
                            }
                        }
                    }
                }
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                java.lang.String message = java.lang.String.format("Failed to create keytab file for %s - %s", principal, e.getMessage());
                if (actionLog != null) {
                    actionLog.writeStdErr(message);
                }
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message, e);
            }
        }
        return keytab;
    }

    private java.io.File cacheKeytab(java.lang.String principal, org.apache.directory.server.kerberos.shared.keytab.Keytab keytab) throws org.apache.ambari.server.AmbariException {
        java.io.File cacheDirectory = configuration.getKerberosKeytabCacheDir();
        if (cacheDirectory == null) {
            java.lang.String message = "The Kerberos keytab cache directory is not configured in the Ambari properties";
            org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        if (!cacheDirectory.exists()) {
            if (cacheDirectory.mkdirs()) {
                ensureAmbariOnlyAccess(cacheDirectory);
                if (!cacheDirectory.exists()) {
                    java.lang.String message = java.lang.String.format("Failed to create the keytab cache directory %s", cacheDirectory.getAbsolutePath());
                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            }
        }
        java.io.File cachedKeytabFile = new java.io.File(cacheDirectory, org.apache.commons.codec.digest.DigestUtils.sha256Hex(principal + java.lang.String.valueOf(java.lang.System.currentTimeMillis())));
        try {
            keytab.write(cachedKeytabFile);
        } catch (java.io.IOException e) {
            java.lang.String message = java.lang.String.format("Failed to write the keytab for %s to the cache location (%s)", principal, cachedKeytabFile.getAbsolutePath());
            org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.error(message, e);
            throw new org.apache.ambari.server.AmbariException(message, e);
        }
        ensureAmbariOnlyAccess(cachedKeytabFile);
        return cachedKeytabFile;
    }

    protected void ensureAmbariOnlyAccess(java.io.File file) throws org.apache.ambari.server.AmbariException {
        if (file.exists()) {
            if ((!file.setReadable(false, false)) || (!file.setReadable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s readable only by Ambari", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if ((!file.setWritable(false, false)) || (!file.setWritable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s writable only by Ambari", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if (file.isDirectory()) {
                if ((!file.setExecutable(false, false)) || (!file.setExecutable(true, true))) {
                    java.lang.String message = java.lang.String.format("Failed to set %s executable by Ambari", file.getAbsolutePath());
                    org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.warn(message);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            } else if (!file.setExecutable(false, false)) {
                java.lang.String message = java.lang.String.format("Failed to set %s not executable", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
    }
}