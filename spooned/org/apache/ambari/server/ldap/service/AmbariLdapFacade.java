package org.apache.ambari.server.ldap.service;
@javax.inject.Singleton
public class AmbariLdapFacade implements org.apache.ambari.server.ldap.service.LdapFacade {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.AmbariLdapFacade.class);

    public enum Parameters {

        TEST_USER_NAME("ambari.ldap.test.user.name"),
        TEST_USER_PASSWORD("ambari.ldap.test.user.password");
        private java.lang.String parameterKey;

        Parameters(java.lang.String parameterKey) {
            this.parameterKey = parameterKey;
        }

        public java.lang.String getParameterKey() {
            return parameterKey;
        }
    }

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.LdapConfigurationService ldapConfigurationService;

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.LdapAttributeDetectionService ldapAttributeDetectionService;

    @javax.inject.Inject
    public AmbariLdapFacade() {
    }

    @java.lang.Override
    public void checkConnection(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        try {
            ldapConfigurationService.checkConnection(ambariLdapConfiguration);
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Validating LDAP connection related configuration: SUCCESS");
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.error("Validating LDAP connection configuration failed", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Detecting LDAP configuration attributes ...");
        try {
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Detecting user attributes ....");
            ambariLdapConfiguration = ldapAttributeDetectionService.detectLdapUserAttributes(ambariLdapConfiguration);
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Detecting group attributes ....");
            ambariLdapConfiguration = ldapAttributeDetectionService.detectLdapGroupAttributes(ambariLdapConfiguration);
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Attribute detection finished.");
            return ambariLdapConfiguration;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.error("Error during LDAP attribute detection", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkLdapAttributes(java.util.Map<java.lang.String, java.lang.Object> parameters, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        java.lang.String userName = getTestUserNameFromParameters(parameters);
        java.lang.String testUserPass = getTestUserPasswordFromParameters(parameters);
        if (null == userName) {
            throw new java.lang.IllegalArgumentException("No test user available for testing LDAP attributes");
        }
        org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Testing LDAP user attributes with test user: {}", userName);
        java.lang.String userDn = ldapConfigurationService.checkUserAttributes(userName, testUserPass, ldapConfiguration);
        org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.info("Testing LDAP group attributes with test user dn: {}", userDn);
        return ldapConfigurationService.checkGroupAttributes(userDn, ldapConfiguration);
    }

    private java.lang.String getTestUserNameFromParameters(java.util.Map<java.lang.String, java.lang.Object> parameters) {
        return ((java.lang.String) (parameterValue(parameters, org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_NAME)));
    }

    private java.lang.String getTestUserPasswordFromParameters(java.util.Map<java.lang.String, java.lang.Object> parameters) {
        return ((java.lang.String) (parameterValue(parameters, org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_PASSWORD)));
    }

    private java.lang.Object parameterValue(java.util.Map<java.lang.String, java.lang.Object> parameters, org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters parameter) {
        java.lang.Object value = null;
        if (parameters.containsKey(parameter.getParameterKey())) {
            value = parameters.get(parameter.getParameterKey());
        } else {
            org.apache.ambari.server.ldap.service.AmbariLdapFacade.LOGGER.warn("Parameter [{}] is missing from parameters", parameter.getParameterKey());
        }
        return value;
    }
}