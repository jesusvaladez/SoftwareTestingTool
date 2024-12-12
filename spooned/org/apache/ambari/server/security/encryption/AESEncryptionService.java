package org.apache.ambari.server.security.encryption;
@com.google.inject.Singleton
public class AESEncryptionService implements org.apache.ambari.server.security.encryption.EncryptionService {
    private static final java.lang.String ENCODED_TEXT_FIELD_DELIMITER = "::";

    private static final java.lang.String UTF_8_CHARSET = java.nio.charset.StandardCharsets.UTF_8.name();

    private final com.google.common.cache.Cache<java.lang.String, org.apache.ambari.server.security.encryption.AESEncryptor> aesEncryptorCache = com.google.common.cache.CacheBuilder.newBuilder().build();

    private org.apache.ambari.server.security.encryption.MasterKeyService environmentMasterKeyService;

    @javax.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @java.lang.Override
    public java.lang.String encrypt(java.lang.String toBeEncrypted) {
        return encrypt(toBeEncrypted, org.apache.ambari.server.utils.TextEncoding.BASE_64);
    }

    @java.lang.Override
    public java.lang.String encrypt(java.lang.String toBeEncrypted, org.apache.ambari.server.utils.TextEncoding textEncoding) {
        return encrypt(toBeEncrypted, getAmbariMasterKey(), textEncoding);
    }

    @java.lang.Override
    public java.lang.String encrypt(java.lang.String toBeEncrypted, java.lang.String key) {
        return encrypt(toBeEncrypted, key, org.apache.ambari.server.utils.TextEncoding.BASE_64);
    }

    @java.lang.Override
    public java.lang.String encrypt(java.lang.String toBeEncrypted, java.lang.String key, org.apache.ambari.server.utils.TextEncoding textEncoding) {
        try {
            final org.apache.ambari.server.security.encryption.EncryptionResult encryptionResult = getAesEncryptor(key).encrypt(toBeEncrypted);
            return org.apache.ambari.server.utils.TextEncoding.BASE_64 == textEncoding ? encodeEncryptionResultBase64(encryptionResult) : encodeEncryptionResultBinHex(encryptionResult);
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    private org.apache.ambari.server.security.encryption.AESEncryptor getAesEncryptor(java.lang.String key) {
        org.apache.ambari.server.security.encryption.AESEncryptor aesEncryptor = aesEncryptorCache.getIfPresent(key);
        if (aesEncryptor == null) {
            aesEncryptor = new org.apache.ambari.server.security.encryption.AESEncryptor(key);
            aesEncryptorCache.put(key, aesEncryptor);
        }
        return aesEncryptor;
    }

    @java.lang.Override
    public final java.lang.String getAmbariMasterKey() {
        initEnvironmentMasterKeyService();
        return java.lang.String.valueOf(environmentMasterKeyService.getMasterSecret());
    }

    private void initEnvironmentMasterKeyService() {
        if (environmentMasterKeyService == null) {
            environmentMasterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(configuration);
            if (!environmentMasterKeyService.isMasterKeyInitialized()) {
                throw new java.lang.SecurityException("You are trying to use a persisted master key but its initialization has been failed!");
            }
        }
    }

    private java.lang.String encodeEncryptionResultBase64(org.apache.ambari.server.security.encryption.EncryptionResult encryptionResult) throws java.io.UnsupportedEncodingException {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(((((org.apache.commons.codec.binary.Base64.encodeBase64String(encryptionResult.salt) + org.apache.ambari.server.security.encryption.AESEncryptionService.ENCODED_TEXT_FIELD_DELIMITER) + org.apache.commons.codec.binary.Base64.encodeBase64String(encryptionResult.iv)) + org.apache.ambari.server.security.encryption.AESEncryptionService.ENCODED_TEXT_FIELD_DELIMITER) + org.apache.commons.codec.binary.Base64.encodeBase64String(encryptionResult.cipher)).getBytes(org.apache.ambari.server.security.encryption.AESEncryptionService.UTF_8_CHARSET));
    }

    private java.lang.String encodeEncryptionResultBinHex(org.apache.ambari.server.security.encryption.EncryptionResult encryptionResult) throws java.io.UnsupportedEncodingException {
        return org.apache.commons.codec.binary.Hex.encodeHexString(((((org.apache.commons.codec.binary.Hex.encodeHexString(encryptionResult.salt) + org.apache.ambari.server.security.encryption.AESEncryptionService.ENCODED_TEXT_FIELD_DELIMITER) + org.apache.commons.codec.binary.Hex.encodeHexString(encryptionResult.iv)) + org.apache.ambari.server.security.encryption.AESEncryptionService.ENCODED_TEXT_FIELD_DELIMITER) + org.apache.commons.codec.binary.Hex.encodeHexString(encryptionResult.cipher)).getBytes(org.apache.ambari.server.security.encryption.AESEncryptionService.UTF_8_CHARSET));
    }

    @java.lang.Override
    public java.lang.String decrypt(java.lang.String toBeDecrypted) {
        return decrypt(toBeDecrypted, org.apache.ambari.server.utils.TextEncoding.BASE_64);
    }

    @java.lang.Override
    public java.lang.String decrypt(java.lang.String toBeDecrypted, org.apache.ambari.server.utils.TextEncoding textEncoding) {
        return decrypt(toBeDecrypted, getAmbariMasterKey(), textEncoding);
    }

    @java.lang.Override
    public java.lang.String decrypt(java.lang.String toBeDecrypted, java.lang.String key) {
        return decrypt(toBeDecrypted, key, org.apache.ambari.server.utils.TextEncoding.BASE_64);
    }

    @java.lang.Override
    public java.lang.String decrypt(java.lang.String toBeDecrypted, java.lang.String key, org.apache.ambari.server.utils.TextEncoding textEncoding) {
        try {
            final byte[] decodedValue = (org.apache.ambari.server.utils.TextEncoding.BASE_64 == textEncoding) ? org.apache.commons.codec.binary.Base64.decodeBase64(toBeDecrypted) : org.apache.commons.codec.binary.Hex.decodeHex(toBeDecrypted.toCharArray());
            final java.lang.String decodedText = new java.lang.String(decodedValue, org.apache.ambari.server.security.encryption.AESEncryptionService.UTF_8_CHARSET);
            final java.lang.String[] decodedParts = decodedText.split(org.apache.ambari.server.security.encryption.AESEncryptionService.ENCODED_TEXT_FIELD_DELIMITER);
            final org.apache.ambari.server.security.encryption.AESEncryptor aes = getAesEncryptor(key);
            if (org.apache.ambari.server.utils.TextEncoding.BASE_64 == textEncoding) {
                return new java.lang.String(aes.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(decodedParts[0]), org.apache.commons.codec.binary.Base64.decodeBase64(decodedParts[1]), org.apache.commons.codec.binary.Base64.decodeBase64(decodedParts[2])), org.apache.ambari.server.security.encryption.AESEncryptionService.UTF_8_CHARSET);
            } else {
                return new java.lang.String(aes.decrypt(org.apache.commons.codec.binary.Hex.decodeHex(decodedParts[0].toCharArray()), org.apache.commons.codec.binary.Hex.decodeHex(decodedParts[1].toCharArray()), org.apache.commons.codec.binary.Hex.decodeHex(decodedParts[2].toCharArray())), org.apache.ambari.server.security.encryption.AESEncryptionService.UTF_8_CHARSET);
            }
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        } catch (org.apache.commons.codec.DecoderException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}