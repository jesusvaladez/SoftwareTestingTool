package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.LDAP_CONFIGURATIONS;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.AMBARI_MANAGES_LDAP_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED_SERVICES;
@com.google.inject.Singleton
public class AmbariServerLDAPConfigurationHandler extends org.apache.ambari.server.controller.internal.AmbariServerStackAdvisorAwareConfigurationHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.class);

    private final org.apache.ambari.server.ldap.service.LdapFacade ldapFacade;

    private final org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration> encryptor;

    @com.google.inject.Inject
    AmbariServerLDAPConfigurationHandler(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper, org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher, org.apache.ambari.server.ldap.service.LdapFacade ldapFacade, @com.google.inject.name.Named("AmbariServerConfigurationEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration> encryptor) {
        super(ambariConfigurationDAO, publisher, clusters, configHelper, managementController, stackAdvisorHelper);
        this.ldapFacade = ldapFacade;
        this.encryptor = encryptor;
    }

    @java.lang.Override
    public void updateComponentCategory(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean removePropertiesIfNotSpecified) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(properties);
        encryptor.encryptSensitiveData(ldapConfiguration);
        super.updateComponentCategory(categoryName, ldapConfiguration.toMap(), removePropertiesIfNotSpecified);
        if (ldapConfiguration.isAmbariManagesLdapConfiguration()) {
            processClusters(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.LDAP_CONFIGURATIONS);
        }
    }

    public java.util.Set<java.lang.String> getLDAPEnabledServices() {
        return getEnabledServices(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.AMBARI_MANAGES_LDAP_CONFIGURATION.key(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED_SERVICES.key());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler.OperationResult performOperation(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean mergeExistingProperties, java.lang.String operation, java.util.Map<java.lang.String, java.lang.Object> operationParameters) throws org.apache.ambari.server.controller.spi.SystemException {
        if (!org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName().equals(categoryName)) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Unexpected category name for Ambari server LDAP properties: %s", categoryName));
        }
        org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType operationType;
        try {
            operationType = org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType.translate(operation);
        } catch (java.lang.IllegalArgumentException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("The requested operation is not supported for this category: %s", categoryName), e);
        }
        java.util.Map<java.lang.String, java.lang.String> ldapConfigurationProperties = new java.util.HashMap<>();
        if (mergeExistingProperties) {
            java.util.Map<java.lang.String, java.lang.String> _ldapProperties = getConfigurationProperties(categoryName);
            if (_ldapProperties != null) {
                ldapConfigurationProperties.putAll(_ldapProperties);
            }
        }
        if (properties != null) {
            ldapConfigurationProperties.putAll(properties);
        }
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(ldapConfigurationProperties);
        boolean success = true;
        java.lang.String message = null;
        java.lang.Object resultData = null;
        try {
            switch (operationType) {
                case TEST_CONNECTION :
                    org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.LOGGER.debug("Testing connection to the LDAP server ...");
                    ldapFacade.checkConnection(ambariLdapConfiguration);
                    break;
                case TEST_ATTRIBUTES :
                    org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.LOGGER.debug("Testing LDAP attributes ....");
                    java.util.Set<java.lang.String> groups = ldapFacade.checkLdapAttributes(operationParameters, ambariLdapConfiguration);
                    resultData = java.util.Collections.singletonMap("groups", groups);
                    break;
                case DETECT_ATTRIBUTES :
                    org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.LOGGER.info("Detecting LDAP attributes ...");
                    ambariLdapConfiguration = ldapFacade.detectAttributes(ambariLdapConfiguration);
                    resultData = java.util.Collections.singletonMap("attributes", ambariLdapConfiguration.toMap());
                    break;
                default :
                    org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.LOGGER.warn("No action provided ...");
                    throw new java.lang.IllegalArgumentException("No request action provided");
            }
        } catch (org.apache.ambari.server.ldap.service.AmbariLdapException e) {
            success = false;
            message = determineCause(e);
            if (org.apache.commons.lang.StringUtils.isEmpty(message)) {
                message = "An unexpected error has occurred.";
            }
            org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.LOGGER.warn(java.lang.String.format("Failed to perform %s: %s", operationType.name(), message), e);
        }
        return new org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler.OperationResult(operationType.getOperation(), success, message, resultData);
    }

    private java.lang.String determineCause(java.lang.Throwable throwable) {
        if (throwable == null) {
            return null;
        } else {
            java.lang.Throwable cause = throwable.getCause();
            if ((cause == null) || (cause == throwable)) {
                return throwable.getMessage();
            } else {
                java.lang.String message = determineCause(cause);
                return message == null ? throwable.getMessage() : message;
            }
        }
    }

    @java.lang.Override
    protected java.lang.String getServiceVersionNote() {
        return "Ambari managed LDAP configurations";
    }

    enum OperationType {

        TEST_CONNECTION("test-connection"),
        TEST_ATTRIBUTES("test-attributes"),
        DETECT_ATTRIBUTES("detect-attributes");
        private final java.lang.String operation;

        OperationType(java.lang.String operation) {
            this.operation = operation;
        }

        public java.lang.String getOperation() {
            return operation;
        }

        public static org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType translate(java.lang.String operation) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(operation)) {
                operation = operation.trim();
                for (org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType category : org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType.values()) {
                    if (category.getOperation().equals(operation)) {
                        return category;
                    }
                }
            }
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid operation for %s: %s", org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), operation));
        }

        public static java.lang.String translate(org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.OperationType operation) {
            return operation == null ? null : operation.getOperation();
        }
    }
}