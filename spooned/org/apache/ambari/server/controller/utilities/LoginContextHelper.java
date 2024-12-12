package org.apache.ambari.server.controller.utilities;
@com.google.inject.Singleton
public class LoginContextHelper {
    public javax.security.auth.login.LoginContext createLoginContext(java.lang.String krb5ModuleEntryName, javax.security.auth.callback.CallbackHandler callbackHandler) throws javax.security.auth.login.LoginException {
        return new javax.security.auth.login.LoginContext(krb5ModuleEntryName, callbackHandler);
    }

    public javax.security.auth.login.LoginContext createLoginContext(java.lang.String krb5ModuleEntryName) throws javax.security.auth.login.LoginException {
        return new javax.security.auth.login.LoginContext(krb5ModuleEntryName);
    }
}