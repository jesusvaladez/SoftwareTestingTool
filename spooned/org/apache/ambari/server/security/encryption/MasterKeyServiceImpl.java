package org.apache.ambari.server.security.encryption;
import org.apache.commons.net.ntp.TimeStamp;
public class MasterKeyServiceImpl implements org.apache.ambari.server.security.encryption.MasterKeyService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.class);

    private static final java.lang.String MASTER_PASSPHRASE = "masterpassphrase";

    private static final java.lang.String MASTER_PERSISTENCE_TAG_PREFIX = "#1.0# ";

    private final org.apache.ambari.server.security.encryption.EncryptionService encryptionService = new org.apache.ambari.server.security.encryption.AESEncryptionService();

    private char[] master = null;

    public MasterKeyServiceImpl(java.io.File masterKeyFile) {
        initFromFile(masterKeyFile);
    }

    private void initFromFile(java.io.File masterKeyFile) {
        if (masterKeyFile == null) {
            throw new java.lang.IllegalArgumentException("Master Key location not provided.");
        }
        if (masterKeyFile.exists()) {
            if (org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.isMasterKeyFile(masterKeyFile)) {
                try {
                    initializeFromFile(masterKeyFile);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Cannot initialize master key from %s: %s", masterKeyFile.getAbsolutePath(), e.getLocalizedMessage()), e);
                }
            } else {
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("The file at %s is not a master ket file", masterKeyFile.getAbsolutePath()));
            }
        } else {
            org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Cannot open master key file, %s", masterKeyFile.getAbsolutePath()));
        }
    }

    public MasterKeyServiceImpl(java.lang.String masterKey) {
        if (masterKey != null) {
            master = masterKey.toCharArray();
        } else {
            throw new java.lang.IllegalArgumentException("Master key cannot be null");
        }
    }

    public MasterKeyServiceImpl() {
    }

    public MasterKeyServiceImpl(org.apache.ambari.server.configuration.Configuration configuration) {
        if ((configuration != null) && configuration.isMasterKeyPersisted()) {
            if (configuration.getMasterKeyLocation() == null) {
                throw new java.lang.IllegalArgumentException("The master key file location must be specified if the master key is persisted");
            }
            initFromFile(configuration.getMasterKeyLocation());
        } else {
            initializeFromEnv();
        }
    }

    @java.lang.Override
    public boolean isMasterKeyInitialized() {
        return master != null;
    }

    @java.lang.Override
    public char[] getMasterSecret() {
        return master;
    }

    public static void main(java.lang.String[] args) {
        java.lang.String masterKey = "ThisissomeSecretPassPhrasse";
        java.lang.String masterKeyLocation = org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/keys/master");
        boolean persistMasterKey = false;
        if ((args != null) && (args.length > 0)) {
            masterKey = args[0];
            if (args.length > 1) {
                masterKeyLocation = args[1];
            }
            if ((args.length > 2) && (!args[2].isEmpty())) {
                persistMasterKey = args[2].equalsIgnoreCase("true");
            }
        }
        final org.apache.ambari.server.security.encryption.MasterKeyServiceImpl masterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKey);
        if (persistMasterKey && (!masterKeyService.initializeMasterKeyFile(new java.io.File(masterKeyLocation), masterKey))) {
            java.lang.System.exit(1);
        } else {
            java.lang.System.exit(0);
        }
    }

    public boolean initializeMasterKeyFile(java.io.File masterKeyFile, java.lang.String masterKey) {
        org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.debug("Persisting master key into {}", masterKeyFile.getAbsolutePath());
        java.lang.String encryptedMasterKey = null;
        if (masterKey != null) {
            try {
                encryptedMasterKey = encryptionService.encrypt(masterKey, org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.MASTER_PASSPHRASE);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Failed to encrypt master key, no changes have been made: %s", e.getLocalizedMessage()), e);
                return false;
            }
        }
        if (masterKeyFile.exists()) {
            if ((masterKeyFile.length() == 0) || org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.isMasterKeyFile(masterKeyFile)) {
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.info(java.lang.String.format("Master key file exists at %s, resetting.", masterKeyFile.getAbsolutePath()));
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(masterKeyFile);java.nio.channels.FileChannel fileChannel = fos.getChannel()) {
                    fileChannel.truncate(0);
                } catch (java.io.FileNotFoundException e) {
                    org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Failed to open key file at %s: %s", masterKeyFile.getAbsolutePath(), e.getLocalizedMessage()), e);
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Failed to reset key file at %s: %s", masterKeyFile.getAbsolutePath(), e.getLocalizedMessage()), e);
                }
            } else {
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.info(java.lang.String.format("File exists at %s, but may not be a master key file. " + "It must be manually removed before this file location can be used", masterKeyFile.getAbsolutePath()));
                return false;
            }
        }
        if (encryptedMasterKey != null) {
            try {
                java.util.ArrayList<java.lang.String> lines = new java.util.ArrayList<>();
                lines.add(org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.MASTER_PERSISTENCE_TAG_PREFIX + org.apache.commons.net.ntp.TimeStamp.getCurrentTime().toDateString());
                lines.add(encryptedMasterKey);
                org.apache.commons.io.FileUtils.writeLines(masterKeyFile, "UTF8", lines);
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.protectAccess(masterKeyFile);
            } catch (java.io.IOException e) {
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error(java.lang.String.format("Failed to persist master key to %s: %s ", masterKeyFile.getAbsolutePath(), e.getLocalizedMessage()), e);
                return false;
            }
        }
        return true;
    }

    private static boolean isMasterKeyFile(java.io.File file) {
        try (java.io.FileReader reader = new java.io.FileReader(file)) {
            char[] buffer = new char[org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.MASTER_PERSISTENCE_TAG_PREFIX.length()];
            return (reader.read(buffer) == buffer.length) && java.util.Arrays.equals(buffer, org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.MASTER_PERSISTENCE_TAG_PREFIX.toCharArray());
        } catch (java.lang.Exception e) {
        }
        return false;
    }

    private static void protectAccess(java.io.File file) throws org.apache.ambari.server.AmbariException {
        if (file.exists()) {
            if ((!file.setReadable(false, false)) || (!file.setReadable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s readable only by current user", file.getAbsolutePath());
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if ((!file.setWritable(false, false)) || (!file.setWritable(true, true))) {
                java.lang.String message = java.lang.String.format("Failed to set %s writable only by current user", file.getAbsolutePath());
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            if (file.isDirectory()) {
                if ((!file.setExecutable(false, false)) || (!file.setExecutable(true, true))) {
                    java.lang.String message = java.lang.String.format("Failed to set %s executable by current user", file.getAbsolutePath());
                    org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.warn(message);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            } else if (!file.setExecutable(false, false)) {
                java.lang.String message = java.lang.String.format("Failed to set %s not executable", file.getAbsolutePath());
                org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.warn(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
    }

    private void initializeFromEnv() {
        java.lang.String key;
        java.util.Map<java.lang.String, java.lang.String> envVariables = java.lang.System.getenv();
        if ((envVariables != null) && (!envVariables.isEmpty())) {
            key = envVariables.get(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_ENV_PROP);
            if ((key == null) || key.isEmpty()) {
                java.lang.String keyPath = envVariables.get(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION.getKey());
                if ((keyPath != null) && (!keyPath.isEmpty())) {
                    java.io.File keyFile = new java.io.File(keyPath);
                    if (keyFile.exists()) {
                        try {
                            initializeFromFile(keyFile);
                        } catch (java.lang.Exception e) {
                            org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error("Cannot read master key from file: " + keyPath);
                            e.printStackTrace();
                        }
                    }
                } else {
                    org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.error("Cannot read master key property {1} or master key file property {3} from environment");
                }
            } else {
                master = key.toCharArray();
            }
        }
    }

    private void initializeFromFile(java.io.File masterFile) throws java.lang.Exception {
        try {
            java.util.List<java.lang.String> lines = org.apache.commons.io.FileUtils.readLines(masterFile, "UTF8");
            java.lang.String tag = lines.get(0);
            org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.LOG.info("Loading from persistent master: " + tag);
            master = encryptionService.decrypt(lines.get(1), org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.MASTER_PASSPHRASE).toCharArray();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}