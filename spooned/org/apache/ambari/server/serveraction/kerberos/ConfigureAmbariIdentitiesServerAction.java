package org.apache.ambari.server.serveraction.kerberos;
public class ConfigureAmbariIdentitiesServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final java.lang.String KEYTAB_PATTERN = "keyTab=\"(.+)?\"";

    private static final java.lang.String PRINCIPAL_PATTERN = "principal=\"(.+)?\"";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabDAO kerberosKeytabDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> m_locksByKeytab = com.google.common.util.concurrent.Striped.lazyWeakLock(25);

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        return processIdentities(requestSharedDataContext);
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.CommandReport commandReport = null;
        if ((includedInFilter && (resolvedPrincipal != null)) && org.apache.ambari.server.utils.StageUtils.getHostName().equals(resolvedPrincipal.getHostName())) {
            final java.lang.String hostName = resolvedPrincipal.getHostName();
            final java.lang.String dataDirectory = getDataDirectoryPath();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> serviceMappingEntry : resolvedPrincipal.getServiceMapping().entries()) {
                if (org.apache.ambari.server.controller.RootService.AMBARI.name().equals(serviceMappingEntry.getKey())) {
                    org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab keytab = resolvedPrincipal.getResolvedKerberosKeytab();
                    java.lang.String destKeytabFilePath = resolvedPrincipal.getResolvedKerberosKeytab().getFile();
                    java.io.File hostDirectory = new java.io.File(dataDirectory, hostName);
                    java.io.File srcKeytabFile = new java.io.File(hostDirectory, org.apache.commons.codec.digest.DigestUtils.sha256Hex(destKeytabFilePath));
                    java.util.concurrent.locks.Lock lock = m_locksByKeytab.get(destKeytabFilePath);
                    lock.lock();
                    try {
                        if (srcKeytabFile.exists()) {
                            java.lang.String ownerAccess = keytab.getOwnerAccess();
                            java.lang.String groupAccess = keytab.getGroupAccess();
                            installAmbariServerIdentity(resolvedPrincipal, srcKeytabFile.getAbsolutePath(), destKeytabFilePath, keytab.getOwnerName(), ownerAccess, keytab.getGroupName(), groupAccess, actionLog);
                            if (serviceMappingEntry.getValue().contains("AMBARI_SERVER_SELF")) {
                                configureJAAS(resolvedPrincipal.getPrincipal(), destKeytabFilePath, actionLog);
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
        return commandReport;
    }

    public boolean installAmbariServerIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal, java.lang.String srcKeytabFilePath, java.lang.String destKeytabFilePath, java.lang.String ownerName, java.lang.String ownerAccess, java.lang.String groupName, java.lang.String groupAccess, org.apache.ambari.server.serveraction.ActionLog actionLog) throws org.apache.ambari.server.AmbariException {
        try {
            boolean ownerWritable = "w".equalsIgnoreCase(ownerAccess) || "rw".equalsIgnoreCase(ownerAccess);
            boolean ownerReadable = "r".equalsIgnoreCase(ownerAccess) || "rw".equalsIgnoreCase(ownerAccess);
            boolean groupWritable = "w".equalsIgnoreCase(groupAccess) || "rw".equalsIgnoreCase(groupAccess);
            boolean groupReadable = "r".equalsIgnoreCase(groupAccess) || "rw".equalsIgnoreCase(groupAccess);
            copyFile(srcKeytabFilePath, destKeytabFilePath);
            setFileACL(destKeytabFilePath, ownerName, ownerReadable, ownerWritable, groupName, groupReadable, groupWritable);
            java.lang.Long ambariServerHostID = ambariServerHostID();
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = null;
            if (ambariServerHostID != null) {
                hostEntity = hostDAO.findById(ambariServerHostID);
            }
            org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke = kerberosKeytabDAO.find(destKeytabFilePath);
            if (kke == null) {
                kke = new org.apache.ambari.server.orm.entities.KerberosKeytabEntity(destKeytabFilePath);
                kke.setOwnerName(ownerName);
                kke.setOwnerAccess(ownerAccess);
                kke.setGroupName(groupName);
                kke.setGroupAccess(groupAccess);
                kerberosKeytabDAO.create(kke);
            }
            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = kerberosPrincipalDAO.find(principal.getPrincipal());
            if (kpe == null) {
                kpe = new org.apache.ambari.server.orm.entities.KerberosPrincipalEntity(principal.getPrincipal(), principal.isService(), principal.getCacheFile());
                kerberosPrincipalDAO.create(kpe);
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> mapping : principal.getServiceMapping().entries()) {
                java.lang.String serviceName = mapping.getKey();
                java.lang.String componentName = mapping.getValue();
                org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity = kerberosKeytabPrincipalDAO.findOrCreate(kke, hostEntity, kpe, null).kkp;
                entity.setDistributed(true);
                entity.putServiceMapping(serviceName, componentName);
                kerberosKeytabPrincipalDAO.merge(entity);
                kke.addKerberosKeytabPrincipal(entity);
                kerberosKeytabDAO.merge(kke);
                kpe.addKerberosKeytabPrincipal(entity);
                kerberosPrincipalDAO.merge(kpe);
            }
            if (actionLog != null) {
                actionLog.writeStdOut(java.lang.String.format("Created Ambari server keytab file for %s at %s", principal, destKeytabFilePath));
            }
        } catch (java.lang.InterruptedException | java.io.IOException e) {
            throw new org.apache.ambari.server.AmbariException(e.getLocalizedMessage(), e);
        }
        return true;
    }

    public void configureJAAS(java.lang.String principal, java.lang.String keytabFilePath, org.apache.ambari.server.serveraction.ActionLog actionLog) {
        java.lang.String jaasConfPath = getJAASConfFilePath();
        if (jaasConfPath != null) {
            java.io.File jaasConfigFile = new java.io.File(jaasConfPath);
            try {
                java.lang.String jaasConfig = org.apache.commons.io.FileUtils.readFileToString(jaasConfigFile, java.nio.charset.Charset.defaultCharset());
                java.io.File oldJaasConfigFile = new java.io.File(jaasConfPath + ".bak");
                org.apache.commons.io.FileUtils.writeStringToFile(oldJaasConfigFile, jaasConfig, java.nio.charset.Charset.defaultCharset());
                jaasConfig = jaasConfig.replaceFirst(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.KEYTAB_PATTERN, ("keyTab=\"" + keytabFilePath) + "\"");
                jaasConfig = jaasConfig.replaceFirst(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.PRINCIPAL_PATTERN, ("principal=\"" + principal) + "\"");
                org.apache.commons.io.FileUtils.writeStringToFile(jaasConfigFile, jaasConfig, java.nio.charset.Charset.defaultCharset());
                java.lang.String message = java.lang.String.format("JAAS config file %s modified successfully for principal %s.", jaasConfigFile.getName(), principal);
                if (actionLog != null) {
                    actionLog.writeStdOut(message);
                }
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to configure JAAS file %s for %s - %s", jaasConfigFile, principal, e.getMessage());
                if (actionLog != null) {
                    actionLog.writeStdErr(message);
                }
                org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.LOG.error(message, e);
            }
        } else {
            java.lang.String message = java.lang.String.format("Failed to configure JAAS, config file should be passed to Ambari server as: " + "%s.", org.apache.ambari.server.controller.utilities.KerberosChecker.JAVA_SECURITY_AUTH_LOGIN_CONFIG);
            if (actionLog != null) {
                actionLog.writeStdErr(message);
            }
            org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.LOG.error(message);
        }
    }

    void copyFile(java.lang.String srcKeytabFilePath, java.lang.String destKeytabFilePath) throws java.io.IOException, java.lang.InterruptedException {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result;
        java.io.File destKeytabFile = new java.io.File(destKeytabFilePath);
        result = org.apache.ambari.server.utils.ShellCommandUtil.mkdir(destKeytabFile.getParent(), true);
        if (!result.isSuccessful()) {
            throw new org.apache.ambari.server.AmbariException(result.getStderr());
        }
        result = org.apache.ambari.server.utils.ShellCommandUtil.copyFile(srcKeytabFilePath, destKeytabFilePath, true, true);
        if (!result.isSuccessful()) {
            throw new org.apache.ambari.server.AmbariException(result.getStderr());
        }
    }

    void setFileACL(java.lang.String filePath, java.lang.String ownerName, boolean ownerReadable, boolean ownerWritable, java.lang.String groupName, boolean groupReadable, boolean groupWritable) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.utils.ShellCommandUtil.Result result;
        result = org.apache.ambari.server.utils.ShellCommandUtil.setFileOwner(filePath, ownerName);
        if (result.isSuccessful()) {
            result = org.apache.ambari.server.utils.ShellCommandUtil.setFileGroup(filePath, groupName);
            if (!result.isSuccessful()) {
                org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.LOG.warn("Failed to set the group for the file at {} to {}: {}", filePath, groupName, result.getStderr());
            }
            result = org.apache.ambari.server.utils.ShellCommandUtil.setFileMode(filePath, ownerReadable, ownerWritable, false, groupReadable, groupWritable, false, false, false, false);
        }
        if (!result.isSuccessful()) {
            throw new org.apache.ambari.server.AmbariException(result.getStderr());
        }
    }

    java.lang.String getJAASConfFilePath() {
        return java.lang.System.getProperty(org.apache.ambari.server.controller.utilities.KerberosChecker.JAVA_SECURITY_AUTH_LOGIN_CONFIG);
    }
}