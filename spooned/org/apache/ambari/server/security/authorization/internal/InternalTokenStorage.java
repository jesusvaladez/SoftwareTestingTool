package org.apache.ambari.server.security.authorization.internal;
@com.google.inject.Singleton
public class InternalTokenStorage {
    private final java.security.SecureRandom random;

    private final java.lang.String token;

    @com.google.inject.Inject
    public InternalTokenStorage(java.security.SecureRandom secureRandom) {
        this.random = secureRandom;
        token = createNewToken();
    }

    public java.lang.String getInternalToken() {
        return token;
    }

    public boolean isValidInternalToken(java.lang.String token) {
        return this.token.equals(token);
    }

    public java.lang.String createNewToken() {
        return new java.math.BigInteger(130, random).toString(32);
    }
}