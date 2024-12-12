package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
public class IPAKerberosOperationHandler extends org.apache.ambari.server.serveraction.kerberos.KDCKerberosOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.class);

    private java.lang.String userPrincipalGroup = null;

    private java.lang.String executableIpaGetKeytab = null;

    private java.lang.String executableIpa = null;

    @java.lang.Override
    public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredentials, java.lang.String realm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (kerberosConfiguration != null) {
            userPrincipalGroup = kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_USER_PRINCIPAL_GROUP);
        }
        executableIpa = getExecutable("ipa");
        executableIpaGetKeytab = getExecutable("ipa-getkeytab");
        super.open(administratorCredentials, realm, kerberosConfiguration);
    }

    @java.lang.Override
    public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        userPrincipalGroup = null;
        executableIpa = null;
        executableIpaGetKeytab = null;
        super.close();
    }

    @java.lang.Override
    public boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = createDeconstructPrincipal(principal);
            java.lang.String principalName = deconstructedPrincipal.getPrincipalName();
            java.lang.String[] ipaCommand = new java.lang.String[]{ service ? "service-show" : "user-show", principalName };
            org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeIpa(ipaCommand);
            if (result.isSuccessful()) {
                return true;
            }
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
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = createDeconstructPrincipal(principal);
        java.lang.String normalizedPrincipal = deconstructedPrincipal.getNormalizedPrincipal();
        java.lang.String[] ipaCommand;
        if (service) {
            ipaCommand = new java.lang.String[]{ "service-add", normalizedPrincipal };
        } else {
            java.lang.String principalName = deconstructedPrincipal.getPrincipalName();
            if (!principalName.equals(principalName.toLowerCase())) {
                org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.warn("{} is not in lowercase. FreeIPA does not recognize user " + ("principals that are not entirely in lowercase. This can lead to issues with kinit and keytabs. Make " + "sure users are in lowercase."), principalName);
            }
            ipaCommand = new java.lang.String[]{ "user-add", deconstructedPrincipal.getPrimary(), "--principal", principalName, "--first", deconstructedPrincipal.getPrimary(), "--last", deconstructedPrincipal.getPrimary(), "--cn", deconstructedPrincipal.getPrimary() };
        }
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = invokeIpa(ipaCommand);
        if (!result.isSuccessful()) {
            java.lang.String message = java.lang.String.format("Failed to create principal for %s\n%s\nSTDOUT: %s\nSTDERR: %s", normalizedPrincipal, org.apache.commons.lang.StringUtils.join(ipaCommand, " "), result.getStdout(), result.getStderr());
            org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.error(message);
            java.lang.String stdErr = result.getStderr();
            if ((stdErr != null) && ((service && stdErr.contains(java.lang.String.format("service with name \"%s\" already exists", normalizedPrincipal))) || ((!service) && stdErr.contains(java.lang.String.format("user with name \"%s\" already exists", deconstructedPrincipal.getPrimary()))))) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosPrincipalAlreadyExistsException(principal);
            } else {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Failed to create principal for %s\nSTDOUT: %s\nSTDERR: %s", normalizedPrincipal, result.getStdout(), result.getStderr()));
            }
        }
        if ((!service) && (!org.apache.commons.lang.StringUtils.isEmpty(userPrincipalGroup))) {
            result = invokeIpa(new java.lang.String[]{ "group-add-member", userPrincipalGroup, "--users", deconstructedPrincipal.getPrimary() });
            if (!result.isSuccessful()) {
                org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.warn("Failed to add account for {} to group {}: \nSTDOUT: {}\nSTDERR: {}", normalizedPrincipal, userPrincipalGroup, result.getStdout(), result.getStderr());
            }
        }
        return 0;
    }

    @java.lang.Override
    public boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to remove principal - no principal specified");
        }
        org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = createDeconstructPrincipal(principal);
        java.lang.String[] ipaCommand = (service) ? new java.lang.String[]{ "service-del", deconstructedPrincipal.getNormalizedPrincipal() } : new java.lang.String[]{ "user-del", deconstructedPrincipal.getPrincipalName() };
        return invokeIpa(ipaCommand).isSuccessful();
    }

    @java.lang.Override
    protected java.lang.String[] getKinitCommand(java.lang.String executableKinit, org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials, java.lang.String credentialsCache, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.lang.String[] command = new java.lang.String[]{ executableKinit, "-c", credentialsCache, credentials.getPrincipal() };
        if (java.util.Arrays.asList(command).contains(null)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Got a null value, can not create 'kinit' command");
        }
        return command;
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
            }
            encryptionTypeSpec = encryptionTypeSpecBuilder.toString();
        }
        java.lang.String[] createKeytabFileCommand = (org.apache.commons.lang.StringUtils.isEmpty(encryptionTypeSpec)) ? new java.lang.String[]{ executableIpaGetKeytab, "-s", getAdminServerHost(true), "-p", principal, "-k", keytabFileDestinationPath } : new java.lang.String[]{ executableIpaGetKeytab, "-s", getAdminServerHost(true), "-e", encryptionTypeSpec, "-p", principal, "-k", keytabFileDestinationPath };
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = executeCommand(createKeytabFileCommand);
        if (!result.isSuccessful()) {
            java.lang.String message = java.lang.String.format("Failed to export the keytab file for %s:\n\tExitCode: %s\n\tSTDOUT: %s\n\tSTDERR: %s", principal, result.getExitCode(), result.getStdout(), result.getStderr());
            org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.warn(message);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message);
        }
    }

    private org.apache.ambari.server.utils.ShellCommandUtil.Result invokeIpa(java.lang.String[] query) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if ((query == null) || (query.length == 0)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Missing ipa query");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(executableIpa)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("No path for ipa is available - this KerberosOperationHandler may not have been opened.");
        }
        java.lang.String[] command = new java.lang.String[query.length + 1];
        command[0] = executableIpa;
        java.lang.System.arraycopy(query, 0, command, 1, query.length);
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = executeCommand(command);
        if (result.isSuccessful()) {
            if (org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.isDebugEnabled()) {
                org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.debug("Executed the following command:\n{}\nSTDOUT: {}\nSTDERR: {}", org.apache.commons.lang.StringUtils.join(command, " "), result.getStdout(), result.getStderr());
            }
        } else {
            org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.LOG.error("Failed to execute the following command:\n{}\nSTDOUT: {}\nSTDERR: {}", org.apache.commons.lang.StringUtils.join(command, " "), result.getStdout(), result.getStderr());
        }
        return result;
    }
}