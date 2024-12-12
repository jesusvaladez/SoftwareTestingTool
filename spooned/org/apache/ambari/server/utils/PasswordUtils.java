package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class PasswordUtils {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.PasswordUtils.class);

    private static final java.util.concurrent.locks.Lock LOCK = new java.util.concurrent.locks.ReentrantLock();

    private static final org.apache.ambari.server.utils.PasswordUtils INSTANCE = new org.apache.ambari.server.utils.PasswordUtils();

    private PasswordUtils() {
    }

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    private volatile org.apache.ambari.server.security.encryption.CredentialProvider credentialProvider = null;

    public static org.apache.ambari.server.utils.PasswordUtils getInstance() {
        return org.apache.ambari.server.utils.PasswordUtils.INSTANCE;
    }

    public java.lang.String readPassword(java.lang.String passwordProperty, java.lang.String defaultPassword) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(passwordProperty)) {
            if (org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(passwordProperty)) {
                return readPasswordFromStore(passwordProperty);
            } else {
                return readPasswordFromFile(passwordProperty, defaultPassword);
            }
        }
        return defaultPassword;
    }

    public java.lang.String readPasswordFromFile(java.lang.String filePath, java.lang.String defaultPassword) {
        if (org.apache.commons.lang.StringUtils.isBlank(filePath) || (!fileExistsAndCanBeRead(filePath))) {
            org.apache.ambari.server.utils.PasswordUtils.LOG.debug("DB password file not specified or does not exist/can not be read - using default");
            return defaultPassword;
        } else {
            org.apache.ambari.server.utils.PasswordUtils.LOG.debug("Reading password from file {}", filePath);
            java.lang.String password = null;
            try {
                password = org.apache.commons.io.FileUtils.readFileToString(new java.io.File(filePath), java.nio.charset.Charset.defaultCharset());
                return org.apache.commons.lang.StringUtils.chomp(password);
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(("Unable to read password from file [" + filePath) + "]", e);
            }
        }
    }

    private boolean fileExistsAndCanBeRead(java.lang.String filePath) {
        final java.io.File passwordFile = new java.io.File(filePath);
        return (passwordFile.exists() && passwordFile.canRead()) && passwordFile.isFile();
    }

    private java.lang.String readPasswordFromStore(java.lang.String aliasStr) {
        return readPasswordFromStore(aliasStr, org.apache.ambari.server.utils.PasswordUtils.configuration);
    }

    public java.lang.String readPasswordFromStore(java.lang.String aliasStr, org.apache.ambari.server.configuration.Configuration configuration) {
        java.lang.String password = null;
        loadCredentialProvider(configuration);
        if (credentialProvider != null) {
            char[] result = null;
            try {
                result = credentialProvider.getPasswordForAlias(aliasStr);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.utils.PasswordUtils.LOG.error("Error reading from credential store.", e);
            }
            if (result != null) {
                password = new java.lang.String(result);
            } else if (org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(aliasStr)) {
                org.apache.ambari.server.utils.PasswordUtils.LOG.error("Cannot read password for alias = " + aliasStr);
            } else {
                org.apache.ambari.server.utils.PasswordUtils.LOG.warn("Raw password provided, not an alias. It cannot be read from credential store.");
            }
        }
        return password;
    }

    private void loadCredentialProvider(org.apache.ambari.server.configuration.Configuration configuration) {
        if (credentialProvider == null) {
            try {
                org.apache.ambari.server.utils.PasswordUtils.LOCK.lock();
                credentialProvider = new org.apache.ambari.server.security.encryption.CredentialProvider(null, configuration);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.utils.PasswordUtils.LOG.info("Credential provider creation failed", e);
                credentialProvider = null;
            } finally {
                org.apache.ambari.server.utils.PasswordUtils.LOCK.unlock();
            }
        }
    }
}