package org.apache.ambari.server.security.encryption;
public class CredentialProvider {
    public static final java.util.regex.Pattern PASSWORD_ALIAS_PATTERN = java.util.regex.Pattern.compile("\\$\\{alias=[\\w\\.]+\\}");

    protected char[] chars = new char[]{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

    private org.apache.ambari.server.security.encryption.CredentialStore keystoreService;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.CredentialProvider.class);

    public CredentialProvider(java.lang.String masterKey, org.apache.ambari.server.configuration.Configuration configuration) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService;
        if (masterKey != null) {
            masterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKey);
        } else {
            masterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(configuration);
        }
        if (!masterKeyService.isMasterKeyInitialized()) {
            throw new org.apache.ambari.server.AmbariException("Master key initialization failed.");
        }
        this.keystoreService = new org.apache.ambari.server.security.encryption.FileBasedCredentialStore(configuration.getMasterKeyStoreLocation());
        this.keystoreService.setMasterKeyService(masterKeyService);
    }

    public char[] getPasswordForAlias(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.credential.Credential credential = (org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(alias)) ? keystoreService.getCredential(getAliasFromString(alias)) : keystoreService.getCredential(alias);
        return credential instanceof org.apache.ambari.server.security.credential.GenericKeyCredential ? ((org.apache.ambari.server.security.credential.GenericKeyCredential) (credential)).getKey() : null;
    }

    public void generateAliasWithPassword(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        java.lang.String passwordString = generatePassword(16);
        addAliasToCredentialStore(alias, passwordString);
    }

    public void addAliasToCredentialStore(java.lang.String alias, java.lang.String passwordString) throws org.apache.ambari.server.AmbariException {
        if ((alias == null) || alias.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Alias cannot be null or empty.");
        }
        if ((passwordString == null) || passwordString.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Empty or null password not allowed.");
        }
        keystoreService.addCredential(alias, new org.apache.ambari.server.security.credential.GenericKeyCredential(passwordString.toCharArray()));
    }

    private java.lang.String generatePassword(int length) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars[r.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    public static boolean isAliasString(java.lang.String aliasStr) {
        if ((aliasStr == null) || aliasStr.isEmpty()) {
            return false;
        }
        java.util.regex.Matcher matcher = org.apache.ambari.server.security.encryption.CredentialProvider.PASSWORD_ALIAS_PATTERN.matcher(aliasStr);
        return matcher.matches();
    }

    private java.lang.String getAliasFromString(java.lang.String strPasswd) {
        return strPasswd.substring(strPasswd.indexOf("=") + 1, strPasswd.length() - 1);
    }

    protected org.apache.ambari.server.security.encryption.CredentialStore getKeystoreService() {
        return keystoreService;
    }

    public static void main(java.lang.String[] args) {
        if ((args != null) && (args.length > 0)) {
            java.lang.String action = args[0];
            java.lang.String alias = null;
            java.lang.String masterKey = null;
            org.apache.ambari.server.security.encryption.CredentialProvider credentialProvider = null;
            org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
            if ((args.length > 1) && (!args[1].isEmpty())) {
                alias = args[1];
            } else {
                org.apache.ambari.server.security.encryption.CredentialProvider.LOG.error("No valid arguments provided.");
                java.lang.System.exit(1);
            }
            if (((args.length > 3) && (!args[3].isEmpty())) && (!args[3].equalsIgnoreCase("None"))) {
                masterKey = args[3];
                org.apache.ambari.server.security.encryption.CredentialProvider.LOG.debug("Master key provided as an argument.");
            }
            try {
                credentialProvider = new org.apache.ambari.server.security.encryption.CredentialProvider(masterKey, configuration);
            } catch (java.lang.Exception ex) {
                ex.printStackTrace();
                java.lang.System.exit(1);
            }
            org.apache.ambari.server.security.encryption.CredentialProvider.LOG.info((("action => " + action) + ", alias => ") + alias);
            if (action.equalsIgnoreCase("PUT")) {
                java.lang.String password = null;
                if ((args.length > 2) && (!args[2].isEmpty())) {
                    password = args[2];
                }
                if ((password != null) && (!password.isEmpty())) {
                    try {
                        credentialProvider.addAliasToCredentialStore(alias, password);
                    } catch (org.apache.ambari.server.AmbariException e) {
                        e.printStackTrace();
                    }
                } else {
                    org.apache.ambari.server.security.encryption.CredentialProvider.LOG.error("Alias and password are required arguments.");
                    java.lang.System.exit(1);
                }
            } else if (action.equalsIgnoreCase("GET")) {
                java.lang.String writeFilePath = null;
                if ((args.length > 2) && (!args[2].isEmpty())) {
                    writeFilePath = args[2];
                }
                if ((writeFilePath != null) && (!writeFilePath.isEmpty())) {
                    java.lang.String passwd = "";
                    try {
                        char[] retPasswd = credentialProvider.getPasswordForAlias(alias);
                        if (retPasswd != null) {
                            passwd = new java.lang.String(retPasswd);
                        }
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.security.encryption.CredentialProvider.LOG.error("Error retrieving password for alias.");
                        e.printStackTrace();
                    }
                    java.io.FileOutputStream fo = null;
                    try {
                        fo = new java.io.FileOutputStream(writeFilePath);
                        fo.write(passwd.getBytes());
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    } finally {
                        org.apache.ambari.server.utils.Closeables.closeSilently(fo);
                    }
                } else {
                    org.apache.ambari.server.security.encryption.CredentialProvider.LOG.error("Alias and file path are required arguments.");
                }
            }
        } else {
            org.apache.ambari.server.security.encryption.CredentialProvider.LOG.error("No arguments provided to " + "CredentialProvider");
            java.lang.System.exit(1);
        }
        java.lang.System.exit(0);
    }
}