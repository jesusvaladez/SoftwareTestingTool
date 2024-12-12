package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
public class MITKerberosOperationHandler extends org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.VariableReplacementHelper variableReplacementHelper;

    private java.lang.String createAttributes = null;

    private java.lang.String executableKadmin = null;

    @java.lang.Override
    public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredentials, java.lang.String realm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (kerberosConfiguration != null) {
            createAttributes = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_KDC_CREATE_ATTRIBUTES);
        }
        executableKadmin = getExecutable("kadmin");
        super.open(administratorCredentials, realm, kerberosConfiguration);
    }

    @java.lang.Override
    public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        createAttributes = null;
        executableKadmin = null;
        super.close();
    }

    @java.lang.Override
    public boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeKAdmin(java.lang.String.format("get_principal %s", principal));
            java.lang.String stdOut = result.getStdout();
            return (stdOut != null) && stdOut.contains(java.lang.String.format("Principal: %s", principal));
        }
        return false;
    }

    @java.lang.Override
    public java.lang.Integer createPrincipal(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to create new principal - no principal specified");
        }
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeKAdmin(java.lang.String.format("add_principal -randkey %s %s", createAttributes == null ? "" : createAttributes, principal));
        java.lang.String stdOut = result.getStdout();
        java.lang.String stdErr = result.getStderr();
        if ((stdOut != null) && stdOut.contains(java.lang.String.format("Principal \"%s\" created", principal))) {
            return 0;
        } else if ((stdErr != null) && stdErr.contains(java.lang.String.format("Principal or policy already exists while creating \"%s\"", principal))) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException(principal);
        } else {
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.error("Failed to execute kadmin query: add_principal -pw \"********\" {} {}\nSTDOUT: {}\nSTDERR: {}", createAttributes == null ? "" : createAttributes, principal, stdOut, result.getStderr());
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Failed to create service principal for %s\nSTDOUT: %s\nSTDERR: %s", principal, stdOut, result.getStderr()));
        }
    }

    @java.lang.Override
    public boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to remove principal - no principal specified");
        }
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeKAdmin(java.lang.String.format("delete_principal -force %s", principal));
        java.lang.String stdOut = result.getStdout();
        return (stdOut != null) && (!stdOut.contains("Principal does not exist"));
    }

    protected org.apache.ambari.server.utils.ShellCommandUtil.Result invokeKAdmin(java.lang.String query) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (org.apache.commons.lang.StringUtils.isEmpty(query)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Missing kadmin query");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(executableKadmin)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("No path for kadmin is available - this KerberosOperationHandler may not have been opened.");
        }
        java.util.List<java.lang.String> command = new java.util.ArrayList<>();
        command.add(executableKadmin);
        java.lang.String credentialCacheFilePath = getCredentialCacheFilePath();
        if (!org.apache.commons.lang.StringUtils.isEmpty(credentialCacheFilePath)) {
            command.add("-c");
            command.add(credentialCacheFilePath);
        }
        java.lang.String adminSeverHost = getAdminServerHost(true);
        if (!org.apache.commons.lang.StringUtils.isEmpty(adminSeverHost)) {
            command.add("-s");
            command.add(adminSeverHost);
        }
        java.lang.String defaultRealm = getDefaultRealm();
        if (!org.apache.commons.lang.StringUtils.isEmpty(defaultRealm)) {
            command.add("-r");
            command.add(defaultRealm);
        }
        command.add("-q");
        command.add(query);
        if (org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.isDebugEnabled()) {
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.debug("Executing: {}", command);
        }
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = null;
        int retryCount = configuration.getKerberosOperationRetries();
        int tries = 0;
        while (tries <= retryCount) {
            try {
                result = executeCommand(command.toArray(new java.lang.String[command.size()]));
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException exception) {
                if (tries == retryCount) {
                    throw exception;
                }
            }
            if ((result != null) && result.isSuccessful()) {
                break;
            }
            tries++;
            try {
                java.lang.Thread.sleep(1000 * configuration.getKerberosOperationRetryTimeout());
            } catch (java.lang.InterruptedException ignored) {
            }
            java.lang.String message = java.lang.String.format("Retrying to execute kadmin after a wait of %d seconds :\n\tCommand: %s", configuration.getKerberosOperationRetryTimeout(), command);
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.warn(message);
        } 
        if ((result == null) || (!result.isSuccessful())) {
            int exitCode = (result == null) ? -999 : result.getExitCode();
            java.lang.String stdOut = (result == null) ? "" : result.getStdout();
            java.lang.String stdErr = (result == null) ? "" : result.getStderr();
            java.lang.String message = java.lang.String.format("Failed to execute kadmin:\n\tCommand: %s\n\tExitCode: %s\n\tSTDOUT: %s\n\tSTDERR: %s", command, exitCode, stdOut, stdErr);
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.warn(message);
            if (stdErr.contains("Client not found in Kerberos database")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException(stdErr);
            } else if (stdErr.contains("Incorrect password while initializing")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException(stdErr);
            } else if (stdErr.contains("Cannot contact any KDC")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException(stdErr);
            } else if (stdErr.contains("Cannot resolve network address for admin server in requested realm while initializing kadmin interface")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException(stdErr);
            } else if (stdErr.contains("Missing parameters in krb5.conf required for kadmin client")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosRealmException(stdErr);
            } else if (stdErr.contains("Cannot find KDC for requested realm while initializing kadmin interface")) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosRealmException(stdErr);
            } else {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Unexpected error condition executing the kadmin command. STDERR: %s", stdErr));
            }
        } else if (org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.isDebugEnabled()) {
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.debug("Executed the following command:\n{}\nSTDOUT: {}\nSTDERR: {}", org.apache.commons.lang.StringUtils.join(command, " "), result.getStdout(), result.getStderr());
        }
        return result;
    }

    @java.lang.Override
    protected java.lang.String[] getKinitCommand(java.lang.String executableKinit, org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials, java.lang.String credentialsCache, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.lang.String kadminPrincipalName = variableReplacementHelper.replaceVariables(kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_KADMIN_PRINCIPAL_NAME), buildReplacementsMap(kerberosConfiguration));
        if (kadminPrincipalName == null) {
            kadminPrincipalName = java.lang.String.format("kadmin/%s", getAdminServerHost(false));
        }
        java.lang.String[] command = new java.lang.String[]{ executableKinit, "-c", credentialsCache, "-S", kadminPrincipalName, credentials.getPrincipal() };
        if (java.util.Arrays.asList(command).contains(null)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Got a null value, can not create 'kinit' command");
        }
        return command;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> buildReplacementsMap(java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) {
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsMap = new java.util.HashMap<>();
        replacementsMap.put("", kerberosConfiguration);
        return replacementsMap;
    }

    @java.lang.Override
    protected void exportKeytabFile(java.lang.String principal, java.lang.String keytabFileDestinationPath, java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> keyEncryptionTypes) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.lang.String encryptionTypeSpec = null;
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(keyEncryptionTypes)) {
            java.lang.StringBuilder encryptionTypeSpecBuilder = new java.lang.StringBuilder();
            for (org.apache.directory.shared.kerberos.codec.types.EncryptionType encryptionType : keyEncryptionTypes) {
                if (encryptionTypeSpecBuilder.length() > 0) {
                    encryptionTypeSpecBuilder.append(',');
                }
                encryptionTypeSpecBuilder.append(encryptionType.getName());
                encryptionTypeSpecBuilder.append(":normal");
            }
            encryptionTypeSpec = encryptionTypeSpecBuilder.toString();
        }
        java.lang.String query = (org.apache.commons.lang.StringUtils.isEmpty(encryptionTypeSpec)) ? java.lang.String.format("xst -k \"%s\" %s", keytabFileDestinationPath, principal) : java.lang.String.format("xst -k \"%s\" -e %s %s", keytabFileDestinationPath, encryptionTypeSpec, principal);
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeKAdmin(query);
        if (!result.isSuccessful()) {
            java.lang.String message = java.lang.String.format("Failed to export the keytab file for %s:\n\tExitCode: %s\n\tSTDOUT: %s\n\tSTDERR: %s", principal, result.getExitCode(), result.getStdout(), result.getStderr());
            org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.LOG.warn(message);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message);
        }
    }
}