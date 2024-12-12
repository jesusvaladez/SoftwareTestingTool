package org.apache.ambari.server.controller.utilities;
public class KerberosChecker {
    static final java.lang.String HTTP_SPNEGO_STANDARD_ENTRY = "com.sun.security.jgss.krb5.initiate";

    private static final java.lang.String KRB5_LOGIN_MODULE = "com.sun.security.auth.module.Krb5LoginModule";

    public static final java.lang.String JAVA_SECURITY_AUTH_LOGIN_CONFIG = "java.security.auth.login.config";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.KerberosChecker.class);

    @com.google.inject.Inject
    static org.apache.ambari.server.configuration.Configuration config;

    @com.google.inject.Inject
    static org.apache.ambari.server.controller.utilities.LoginContextHelper loginContextHelper;

    public static void checkJaasConfiguration() throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.controller.utilities.KerberosChecker.config.isKerberosJaasConfigurationCheckEnabled()) {
            org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.info("Checking Ambari Server Kerberos credentials.");
            java.lang.String jaasConfPath = java.lang.System.getProperty(org.apache.ambari.server.controller.utilities.KerberosChecker.JAVA_SECURITY_AUTH_LOGIN_CONFIG);
            javax.security.auth.login.Configuration jaasConf = javax.security.auth.login.Configuration.getConfiguration();
            javax.security.auth.login.AppConfigurationEntry[] jaasConfEntries = jaasConf.getAppConfigurationEntry(org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY);
            if (jaasConfEntries == null) {
                org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn((("Can't find " + org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY) + " entry in ") + jaasConfPath);
            } else {
                boolean krb5LoginModulePresent = false;
                for (javax.security.auth.login.AppConfigurationEntry ace : jaasConfEntries) {
                    if (org.apache.ambari.server.controller.utilities.KerberosChecker.KRB5_LOGIN_MODULE.equals(ace.getLoginModuleName())) {
                        krb5LoginModulePresent = true;
                        java.util.Map<java.lang.String, ?> options = ace.getOptions();
                        if (options != null) {
                            if (options.containsKey("keyTab")) {
                                java.lang.String keytabPath = ((java.lang.String) (options.get("keyTab")));
                                java.io.File keytabFile = new java.io.File(keytabPath);
                                if (!keytabFile.exists()) {
                                    org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn(keytabPath + " doesn't exist.");
                                } else if (!keytabFile.canRead()) {
                                    org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn((("Unable to read " + keytabPath) + " Please check the file access permissions for user ") + java.lang.System.getProperty("user.name"));
                                }
                            } else {
                                org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn((((("Can't find keyTab option in " + org.apache.ambari.server.controller.utilities.KerberosChecker.KRB5_LOGIN_MODULE) + " module of ") + org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY) + " entry in ") + jaasConfPath);
                            }
                            if (!options.containsKey("principal")) {
                                org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn((((("Can't find principal option in " + org.apache.ambari.server.controller.utilities.KerberosChecker.KRB5_LOGIN_MODULE) + " module of ") + org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY) + " entry in ") + jaasConfPath);
                            }
                        }
                    }
                }
                if (!krb5LoginModulePresent) {
                    org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.warn((((("Can't find " + org.apache.ambari.server.controller.utilities.KerberosChecker.KRB5_LOGIN_MODULE) + " module in ") + org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY) + " entry in ") + jaasConfPath);
                }
            }
            try {
                javax.security.auth.login.LoginContext loginContext = org.apache.ambari.server.controller.utilities.KerberosChecker.loginContextHelper.createLoginContext(org.apache.ambari.server.controller.utilities.KerberosChecker.HTTP_SPNEGO_STANDARD_ENTRY);
                loginContext.login();
                loginContext.logout();
            } catch (javax.security.auth.login.LoginException le) {
                org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.error(le.getMessage());
                throw new org.apache.ambari.server.AmbariException(("Ambari Server Kerberos credentials check failed. \n" + "Check KDC availability and JAAS configuration in ") + jaasConfPath);
            }
            org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.info("Ambari Server Kerberos credentials check passed.");
        } else {
            org.apache.ambari.server.controller.utilities.KerberosChecker.LOG.info("Skipping Ambari Server Kerberos credentials check.");
        }
    }
}