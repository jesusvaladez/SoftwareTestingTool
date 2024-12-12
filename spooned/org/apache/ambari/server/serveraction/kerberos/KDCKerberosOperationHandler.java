package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.MapUtils;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
abstract class KDCKerberosOperationHandler extends org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.class);

    private java.lang.String adminServerHost = null;

    private java.lang.String adminServerHostAndPort = null;

    private java.util.HashMap<java.lang.String, org.apache.directory.server.kerberos.shared.keytab.Keytab> cachedKeytabs = null;

    private java.lang.String executableKinit = null;

    private java.io.File credentialsCacheFile = null;

    private java.util.Map<java.lang.String, java.lang.String> environmentMap = null;

    @java.lang.Override
    public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredentials, java.lang.String realm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        super.open(administratorCredentials, realm, kerberosConfiguration);
        if (kerberosConfiguration != null) {
            java.lang.String value = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_ADMIN_SERVER_HOST);
            org.apache.ambari.server.utils.HostAndPort hostAndPort = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
            if (hostAndPort == null) {
                adminServerHost = value;
                adminServerHostAndPort = value;
            } else {
                adminServerHost = hostAndPort.host;
                adminServerHostAndPort = value;
            }
        }
        executableKinit = getExecutable("kinit");
        setOpen(init(kerberosConfiguration));
    }

    @java.lang.Override
    public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (credentialsCacheFile != null) {
            if (credentialsCacheFile.delete()) {
                org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.debug("Failed to remove the cache file, {}", credentialsCacheFile.getAbsolutePath());
            }
            credentialsCacheFile = null;
        }
        environmentMap = null;
        executableKinit = null;
        cachedKeytabs = null;
        adminServerHost = null;
        adminServerHostAndPort = null;
        super.close();
    }

    @java.lang.Override
    public java.lang.Integer setPrincipalPassword(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (!principalExists(principal, service)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalDoesNotExistException(java.lang.String.format("Principal does not exist while attempting to set its password: %s", principal));
        }
        return 0;
    }

    @java.lang.Override
    protected org.apache.directory.server.kerberos.shared.keytab.Keytab createKeytab(java.lang.String principal, java.lang.String password, java.lang.Integer keyNumber) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if ((principal == null) || principal.isEmpty()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to create keytab file, missing principal");
        }
        if (cachedKeytabs.containsKey(principal)) {
            return cachedKeytabs.get(principal);
        }
        java.io.File keytabFile = null;
        try {
            try {
                keytabFile = java.io.File.createTempFile("ambari_tmp", ".keytab");
                if (!keytabFile.delete()) {
                    org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn("Failed to remove temporary file to hold keytab.  Exporting the keytab file for {} may fail.", principal);
                }
            } catch (java.io.IOException e) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Failed to create the temporary file needed to hold the exported keytab file for %s: %s", principal, e.getLocalizedMessage()), e);
            }
            exportKeytabFile(principal, keytabFile.getAbsolutePath(), getKeyEncryptionTypes());
            org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = readKeytabFile(keytabFile);
            cachedKeytabs.put(principal, keytab);
            return keytab;
        } finally {
            if ((keytabFile != null) && keytabFile.exists()) {
                if (!keytabFile.delete()) {
                    org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.debug("Failed to remove the temporary keytab file, {}", keytabFile.getAbsolutePath());
                }
            }
        }
    }

    @java.lang.Override
    protected org.apache.ambari.server.utils.ShellCommandUtil.Result executeCommand(java.lang.String[] command, java.util.Map<java.lang.String, java.lang.String> envp, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.util.Map<java.lang.String, java.lang.String> _envp;
        if (org.apache.commons.collections.MapUtils.isEmpty(environmentMap)) {
            _envp = envp;
        } else if (org.apache.commons.collections.MapUtils.isEmpty(envp)) {
            _envp = environmentMap;
        } else {
            _envp = new java.util.HashMap<>();
            _envp.putAll(envp);
            _envp.putAll(environmentMap);
        }
        return super.executeCommand(command, _envp, interactiveHandler);
    }

    java.lang.String getAdminServerHost(boolean includePort) {
        return includePort ? adminServerHostAndPort : adminServerHost;
    }

    java.lang.String getCredentialCacheFilePath() {
        return credentialsCacheFile == null ? null : credentialsCacheFile.getAbsolutePath();
    }

    protected abstract java.lang.String[] getKinitCommand(java.lang.String executableKinit, org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials, java.lang.String credentialsCache, java.util.Map<java.lang.String, java.lang.String> kerberosConfigurations) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    protected abstract void exportKeytabFile(java.lang.String principal, java.lang.String keytabFileDestinationPath, java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> keyEncryptionTypes) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    protected boolean init(java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (credentialsCacheFile != null) {
            if (!credentialsCacheFile.delete()) {
                org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.debug("Failed to remove the orphaned cache file, {}", credentialsCacheFile.getAbsolutePath());
            }
            credentialsCacheFile = null;
        }
        try {
            credentialsCacheFile = java.io.File.createTempFile("ambari_krb_", "cc");
            credentialsCacheFile.deleteOnExit();
            ensureAmbariOnlyAccess(credentialsCacheFile);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Failed to create the temporary file needed to hold the administrator ticket cache: %s", e.getLocalizedMessage()), e);
        }
        java.lang.String credentialsCache = java.lang.String.format("FILE:%s", credentialsCacheFile.getAbsolutePath());
        environmentMap = new java.util.HashMap<>();
        environmentMap.put("KRB5CCNAME", credentialsCache);
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = getAdministratorCredential();
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = executeCommand(getKinitCommand(executableKinit, credentials, credentialsCache, kerberosConfiguration), environmentMap, new org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.InteractivePasswordHandler(java.lang.String.valueOf(credentials.getKey()), null));
        if (!result.isSuccessful()) {
            java.lang.String message = java.lang.String.format("Failed to kinit as the KDC administrator user, %s:\n\tExitCode: %s\n\tSTDOUT: %s\n\tSTDERR: %s", credentials.getPrincipal(), result.getExitCode(), result.getStdout(), result.getStderr());
            org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn(message);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException(message);
        }
        cachedKeytabs = new java.util.HashMap<>();
        return true;
    }

    private void ensureAmbariOnlyAccess(java.io.File file) throws org.apache.ambari.server.AmbariException {
        if (file.exists()) {
            if ((!file.setReadable(false, false)) || (!file.setReadable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s readable only by Ambari", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if ((!file.setWritable(false, false)) || (!file.setWritable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s writable only by Ambari", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if (file.isDirectory()) {
                if ((!file.setExecutable(false, false)) || (!file.setExecutable(true, true))) {
                    java.lang.String message = java.lang.String.format("Failed to set %s executable by Ambari", file.getAbsolutePath());
                    org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn(message);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            } else if (!file.setExecutable(false, false)) {
                java.lang.String message = java.lang.String.format("Failed to set %s not executable", file.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
    }

    protected static class InteractivePasswordHandler implements org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler {
        private java.util.LinkedList<java.lang.String> responses;

        private java.util.Queue<java.lang.String> currentResponses;

        InteractivePasswordHandler(java.lang.String adminPassword, java.lang.String userPassword) {
            responses = new java.util.LinkedList<>();
            if (adminPassword != null) {
                responses.offer(adminPassword);
            }
            if (userPassword != null) {
                responses.offer(userPassword);
                responses.offer(userPassword);
            }
            currentResponses = new java.util.LinkedList<>(responses);
        }

        @java.lang.Override
        public boolean done() {
            return currentResponses.size() == 0;
        }

        @java.lang.Override
        public java.lang.String getResponse(java.lang.String query) {
            return currentResponses.poll();
        }

        @java.lang.Override
        public void start() {
            currentResponses = new java.util.LinkedList<>(responses);
        }
    }
}