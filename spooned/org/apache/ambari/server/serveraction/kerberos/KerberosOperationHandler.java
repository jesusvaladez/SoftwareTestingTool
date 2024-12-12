package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.server.kerberos.shared.crypto.encryption.KerberosKeyFactory;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.apache.directory.server.kerberos.shared.keytab.KeytabEntry;
import org.apache.directory.shared.kerberos.KerberosTime;
import org.apache.directory.shared.kerberos.codec.types.EncryptionType;
import org.apache.directory.shared.kerberos.components.EncryptionKey;
public abstract class KerberosOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class);

    public static final java.lang.String KERBEROS_ENV_LDAP_URL = "ldap_url";

    public static final java.lang.String KERBEROS_ENV_PRINCIPAL_CONTAINER_DN = "container_dn";

    public static final java.lang.String KERBEROS_ENV_USER_PRINCIPAL_GROUP = "ipa_user_group";

    public static final java.lang.String KERBEROS_ENV_AD_CREATE_ATTRIBUTES_TEMPLATE = "ad_create_attributes_template";

    public static final java.lang.String KERBEROS_ENV_KDC_CREATE_ATTRIBUTES = "kdc_create_attributes";

    public static final java.lang.String KERBEROS_ENV_ENCRYPTION_TYPES = "encryption_types";

    public static final java.lang.String KERBEROS_ENV_KDC_HOSTS = "kdc_hosts";

    public static final java.lang.String KERBEROS_ENV_ADMIN_SERVER_HOST = "admin_server_host";

    public static final java.lang.String KERBEROS_ENV_KADMIN_PRINCIPAL_NAME = "kadmin_principal_name";

    public static final java.lang.String KERBEROS_ENV_EXECUTABLE_SEARCH_PATHS = "executable_search_paths";

    private static final java.lang.String[] DEFAULT_EXECUTABLE_SEARCH_PATHS = new java.lang.String[]{ "/usr/bin", "/usr/kerberos/bin", "/usr/sbin", "/usr/lib/mit/bin", "/usr/lib/mit/sbin" };

    private static final java.util.Map<java.lang.String, java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType>> ENCRYPTION_TYPE_TRANSLATION_MAP = java.util.Collections.unmodifiableMap(new java.util.HashMap<java.lang.String, java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType>>() {
        {
            put("aes", java.util.EnumSet.of(EncryptionType.AES256_CTS_HMAC_SHA1_96, EncryptionType.AES128_CTS_HMAC_SHA1_96));
            put("aes256-cts-hmac-sha1-96", java.util.EnumSet.of(EncryptionType.AES256_CTS_HMAC_SHA1_96));
            put("aes256-cts", java.util.EnumSet.of(EncryptionType.AES256_CTS_HMAC_SHA1_96));
            put("aes-256", java.util.EnumSet.of(EncryptionType.AES256_CTS_HMAC_SHA1_96));
            put("aes128-cts-hmac-sha1-96", java.util.EnumSet.of(EncryptionType.AES128_CTS_HMAC_SHA1_96));
            put("aes128-cts", java.util.EnumSet.of(EncryptionType.AES128_CTS_HMAC_SHA1_96));
            put("aes-128", java.util.EnumSet.of(EncryptionType.AES128_CTS_HMAC_SHA1_96));
            put("rc4", java.util.EnumSet.of(EncryptionType.RC4_HMAC));
            put("arcfour-hmac", java.util.EnumSet.of(EncryptionType.RC4_HMAC));
            put("rc4-hmac", java.util.EnumSet.of(EncryptionType.RC4_HMAC));
            put("arcfour-hmac-md5", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("arcfour-hmac-exp", java.util.EnumSet.of(EncryptionType.RC4_HMAC_EXP));
            put("rc4-hmac-exp", java.util.EnumSet.of(EncryptionType.RC4_HMAC_EXP));
            put("arcfour-hmac-md5-exp", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("camellia", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("camellia256-cts-cmac", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("camellia256-cts", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("camellia128-cts-cmac", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("camellia128-cts", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("des", java.util.EnumSet.of(EncryptionType.DES_CBC_CRC, EncryptionType.DES_CBC_MD5, EncryptionType.DES_CBC_MD4));
            put("des-cbc-md4", java.util.EnumSet.of(EncryptionType.DES_CBC_MD4));
            put("des-cbc-md5", java.util.EnumSet.of(EncryptionType.DES_CBC_MD5));
            put("des-cbc-crc", java.util.EnumSet.of(EncryptionType.DES_CBC_CRC));
            put("des-cbc-raw", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("des-hmac-sha1", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("des3", java.util.EnumSet.of(EncryptionType.DES3_CBC_SHA1_KD));
            put("des3-cbc-raw", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("des3-cbc-sha1", java.util.EnumSet.of(EncryptionType.DES3_CBC_SHA1_KD));
            put("des3-hmac-sha1", java.util.EnumSet.of(EncryptionType.UNKNOWN));
            put("des3-cbc-sha1-kd", java.util.EnumSet.of(EncryptionType.DES3_CBC_SHA1_KD));
        }
    });

    private static final java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> DEFAULT_CIPHERS = java.util.Collections.unmodifiableSet(new java.util.HashSet<org.apache.directory.shared.kerberos.codec.types.EncryptionType>() {
        {
            add(EncryptionType.DES_CBC_MD5);
            add(EncryptionType.DES3_CBC_SHA1_KD);
            add(EncryptionType.RC4_HMAC);
            add(EncryptionType.AES128_CTS_HMAC_SHA1_96);
            add(EncryptionType.AES256_CTS_HMAC_SHA1_96);
        }
    });

    private org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential = null;

    private java.lang.String defaultRealm = null;

    private java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> keyEncryptionTypes = new java.util.HashSet<>(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.DEFAULT_CIPHERS);

    private boolean open = false;

    private java.lang.String[] executableSearchPaths = null;

    public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential, java.lang.String defaultRealm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        setAdministratorCredential(administratorCredential);
        setDefaultRealm(defaultRealm);
        if (kerberosConfiguration != null) {
            setKeyEncryptionTypes(translateEncryptionTypes(kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_ENCRYPTION_TYPES), "\\s+"));
            setExecutableSearchPaths(kerberosConfiguration.get(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.KERBEROS_ENV_EXECUTABLE_SEARCH_PATHS));
        }
    }

    public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        setOpen(false);
    }

    public abstract boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    public abstract java.lang.Integer createPrincipal(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    public abstract java.lang.Integer setPrincipalPassword(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    public abstract boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    public boolean testAdministratorCredentials() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (!isOpen()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("This operation handler has not been opened");
        }
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential = getAdministratorCredential();
        if (credential == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Missing KDC administrator credential");
        } else {
            return principalExists(credential.getPrincipal(), false);
        }
    }

    protected org.apache.directory.server.kerberos.shared.keytab.Keytab createKeytab(java.lang.String principal, java.lang.String password, java.lang.Integer keyNumber) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("Failed to create keytab file, missing principal");
        }
        if (password == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(java.lang.String.format("Failed to create keytab file for %s, missing password", principal));
        }
        java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> ciphers = new java.util.HashSet<>(keyEncryptionTypes);
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> keytabEntries = new java.util.ArrayList<>();
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = new org.apache.directory.server.kerberos.shared.keytab.Keytab();
        if (!ciphers.isEmpty()) {
            java.util.Map<org.apache.directory.shared.kerberos.codec.types.EncryptionType, org.apache.directory.shared.kerberos.components.EncryptionKey> keys = org.apache.directory.server.kerberos.shared.crypto.encryption.KerberosKeyFactory.getKerberosKeys(principal, password, ciphers);
            if (keys != null) {
                byte keyVersion = (keyNumber == null) ? 0 : keyNumber.byteValue();
                org.apache.directory.shared.kerberos.KerberosTime timestamp = new org.apache.directory.shared.kerberos.KerberosTime();
                for (org.apache.directory.shared.kerberos.components.EncryptionKey encryptionKey : keys.values()) {
                    keytabEntries.add(new org.apache.directory.server.kerberos.shared.keytab.KeytabEntry(principal, 1, timestamp, keyVersion, encryptionKey));
                }
                keytab.setEntries(keytabEntries);
            }
        }
        return keytab;
    }

    protected boolean createKeytabFile(java.io.File sourceKeytabFile, java.io.File destinationKeytabFile) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return createKeytabFile(readKeytabFile(sourceKeytabFile), destinationKeytabFile);
    }

    protected boolean createKeytabFile(java.lang.String principal, java.lang.String password, java.lang.Integer keyNumber, java.io.File destinationKeytabFile) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return createKeytabFile(createKeytab(principal, password, keyNumber), destinationKeytabFile);
    }

    public boolean createKeytabFile(org.apache.directory.server.kerberos.shared.keytab.Keytab keytab, java.io.File destinationKeytabFile) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (destinationKeytabFile == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("The destination file path is null");
        }
        try {
            mergeKeytabs(readKeytabFile(destinationKeytabFile), keytab).write(destinationKeytabFile);
            return true;
        } catch (java.io.IOException e) {
            java.lang.String message = "Failed to export keytab file";
            org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.error(message, e);
            if (!destinationKeytabFile.delete()) {
                destinationKeytabFile.deleteOnExit();
            }
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message, e);
        }
    }

    protected org.apache.directory.server.kerberos.shared.keytab.Keytab mergeKeytabs(org.apache.directory.server.kerberos.shared.keytab.Keytab keytab, org.apache.directory.server.kerberos.shared.keytab.Keytab updates) {
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> keytabEntries = (keytab == null) ? java.util.Collections.emptyList() : new java.util.ArrayList<>(keytab.getEntries());
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> updateEntries = (updates == null) ? java.util.Collections.emptyList() : new java.util.ArrayList<>(updates.getEntries());
        java.util.List<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> mergedEntries = new java.util.ArrayList<>();
        if (keytabEntries.isEmpty()) {
            mergedEntries.addAll(updateEntries);
        } else if (updateEntries.isEmpty()) {
            mergedEntries.addAll(keytabEntries);
        } else {
            java.util.Iterator<org.apache.directory.server.kerberos.shared.keytab.KeytabEntry> iterator = keytabEntries.iterator();
            while (iterator.hasNext()) {
                org.apache.directory.server.kerberos.shared.keytab.KeytabEntry keytabEntry = iterator.next();
                for (org.apache.directory.server.kerberos.shared.keytab.KeytabEntry entry : updateEntries) {
                    if (entry.getPrincipalName().equals(keytabEntry.getPrincipalName()) && entry.getKey().getKeyType().equals(keytabEntry.getKey().getKeyType())) {
                        iterator.remove();
                        break;
                    }
                }
            } 
            mergedEntries.addAll(keytabEntries);
            mergedEntries.addAll(updateEntries);
        }
        org.apache.directory.server.kerberos.shared.keytab.Keytab mergedKeytab = new org.apache.directory.server.kerberos.shared.keytab.Keytab();
        mergedKeytab.setEntries(mergedEntries);
        return mergedKeytab;
    }

    protected org.apache.directory.server.kerberos.shared.keytab.Keytab readKeytabFile(java.io.File file) {
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab;
        if ((file.exists() && file.canRead()) && (file.length() > 0)) {
            try {
                keytab = org.apache.directory.server.kerberos.shared.keytab.Keytab.read(file);
            } catch (java.io.IOException e) {
                keytab = null;
            }
        } else {
            keytab = null;
        }
        return keytab;
    }

    public org.apache.ambari.server.security.credential.PrincipalKeyCredential getAdministratorCredential() {
        return administratorCredential;
    }

    public void setAdministratorCredential(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential) throws org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException {
        if (administratorCredential == null) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("The administrator credential must not be null");
        }
        java.lang.String principal = administratorCredential.getPrincipal();
        if (org.apache.commons.lang.StringUtils.isEmpty(principal)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("Must specify a principal but it is null or empty");
        }
        char[] password = administratorCredential.getKey();
        if (org.apache.commons.lang.ArrayUtils.isEmpty(password)) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("Must specify a password but it is null or empty");
        }
        this.administratorCredential = administratorCredential;
    }

    public java.lang.String getDefaultRealm() {
        return defaultRealm;
    }

    public void setDefaultRealm(java.lang.String defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    public java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> getKeyEncryptionTypes() {
        return keyEncryptionTypes;
    }

    public void setKeyEncryptionTypes(java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> keyEncryptionTypes) {
        this.keyEncryptionTypes = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(keyEncryptionTypes == null ? org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.DEFAULT_CIPHERS : keyEncryptionTypes));
    }

    public java.lang.String[] getExecutableSearchPaths() {
        return executableSearchPaths;
    }

    public void setExecutableSearchPaths(java.lang.String[] executableSearchPaths) {
        this.executableSearchPaths = executableSearchPaths;
    }

    public void setExecutableSearchPaths(java.lang.String delimitedExecutableSearchPaths) {
        java.util.List<java.lang.String> searchPaths = null;
        if (delimitedExecutableSearchPaths != null) {
            searchPaths = new java.util.ArrayList<>();
            for (java.lang.String path : delimitedExecutableSearchPaths.split(",")) {
                path = path.trim();
                if (!path.isEmpty()) {
                    searchPaths.add(path);
                }
            }
        }
        setExecutableSearchPaths(searchPaths == null ? null : searchPaths.toArray(new java.lang.String[searchPaths.size()]));
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    protected java.io.File createKeytabFile(java.lang.String keytabData) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        boolean success = false;
        java.io.File tempFile = null;
        try {
            tempFile = java.io.File.createTempFile("temp", ".dat");
        } catch (java.io.IOException e) {
            org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.error(java.lang.String.format("Failed to create temporary keytab file: %s", e.getLocalizedMessage()), e);
        }
        if ((tempFile != null) && (keytabData != null)) {
            java.io.OutputStream fos = null;
            try {
                fos = new java.io.FileOutputStream(tempFile);
                fos.write(org.apache.commons.codec.binary.Base64.decodeBase64(keytabData));
                success = true;
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to write to temporary keytab file %s: %s", tempFile.getAbsolutePath(), e.getLocalizedMessage());
                org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.error(message, e);
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message, e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (java.io.IOException e) {
                    }
                }
                if (!success) {
                    if (!tempFile.delete()) {
                        tempFile.deleteOnExit();
                    }
                    tempFile = null;
                }
            }
        }
        return tempFile;
    }

    protected org.apache.ambari.server.utils.ShellCommandUtil.Result executeCommand(java.lang.String[] command, java.util.Map<java.lang.String, java.lang.String> envp, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if ((command == null) || (command.length == 0)) {
            return null;
        } else {
            try {
                return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(command, envp, interactiveHandler, false);
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to execute the command: %s", e.getLocalizedMessage());
                org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.error(message, e);
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message, e);
            } catch (java.lang.InterruptedException e) {
                java.lang.String message = java.lang.String.format("Failed to wait for the command to complete: %s", e.getLocalizedMessage());
                org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.error(message, e);
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(message, e);
            }
        }
    }

    protected org.apache.ambari.server.utils.ShellCommandUtil.Result executeCommand(java.lang.String[] command) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return executeCommand(command, null);
    }

    protected org.apache.ambari.server.utils.ShellCommandUtil.Result executeCommand(java.lang.String[] command, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return executeCommand(command, null, interactiveHandler);
    }

    protected org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal createDeconstructPrincipal(java.lang.String principal) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        try {
            return org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf(principal, getDefaultRealm());
        } catch (java.lang.IllegalArgumentException e) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException(e.getMessage(), e);
        }
    }

    protected java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> translateEncryptionType(java.lang.String name) {
        java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> encryptionTypes = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(name)) {
            encryptionTypes = org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.ENCRYPTION_TYPE_TRANSLATION_MAP.get(name.toLowerCase());
        }
        if (encryptionTypes == null) {
            org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.LOG.warn("The given encryption type name ({}) is not supported.", name);
            return java.util.Collections.emptySet();
        }
        return encryptionTypes;
    }

    protected java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> translateEncryptionTypes(java.lang.String names, java.lang.String delimiter) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        java.util.Set<org.apache.directory.shared.kerberos.codec.types.EncryptionType> encryptionTypes = new java.util.HashSet<>();
        if (!org.apache.commons.lang.StringUtils.isEmpty(names)) {
            for (java.lang.String name : names.split(delimiter == null ? "\\s+" : delimiter)) {
                encryptionTypes.addAll(translateEncryptionType(name.trim()));
            }
        }
        if (encryptionTypes.isEmpty()) {
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosOperationException("All the encryption type names you set are not supported. Aborting.");
        }
        return encryptionTypes;
    }

    protected java.lang.String escapeCharacters(java.lang.String string, java.util.Set<java.lang.Character> charactersToEscape, java.lang.Character escapeCharacter) {
        if ((org.apache.commons.lang.StringUtils.isEmpty(string) || (charactersToEscape == null)) || charactersToEscape.isEmpty()) {
            return string;
        } else {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            for (char character : string.toCharArray()) {
                if (charactersToEscape.contains(character)) {
                    builder.append(escapeCharacter);
                }
                builder.append(character);
            }
            return builder.toString();
        }
    }

    protected java.lang.String getExecutable(java.lang.String executable) {
        java.lang.String[] searchPaths = getExecutableSearchPaths();
        java.lang.String executablePath = null;
        if (searchPaths == null) {
            searchPaths = org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.DEFAULT_EXECUTABLE_SEARCH_PATHS;
        }
        for (java.lang.String searchPath : searchPaths) {
            java.io.File executableFile = new java.io.File(searchPath, executable);
            if (executableFile.canExecute()) {
                executablePath = executableFile.getAbsolutePath();
                break;
            }
        }
        return executablePath == null ? executable : executablePath;
    }
}