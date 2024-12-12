package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.ValidatingPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
@javax.inject.Singleton
public class LdapConnectionTemplateFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory.class);

    @javax.inject.Inject
    private javax.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> ambariLdapConfigurationProvider;

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.LdapConnectionConfigService ldapConnectionConfigService;

    private org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplateInstance;

    @javax.inject.Inject
    public LdapConnectionTemplateFactory() {
    }

    public org.apache.directory.ldap.client.template.LdapConnectionTemplate create(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory.LOG.info("Constructing new instance based on the provided ambari ldap configuration: {}", ambariLdapConfiguration);
        org.apache.directory.ldap.client.api.LdapConnectionConfig ldapConnectionConfig = ldapConnectionConfigService.createLdapConnectionConfig(ambariLdapConfiguration);
        org.apache.directory.ldap.client.api.LdapConnectionFactory ldapConnectionFactory = new org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory(ldapConnectionConfig);
        org.apache.directory.ldap.client.api.LdapConnectionPool ldapConnectionPool = new org.apache.directory.ldap.client.api.LdapConnectionPool(new org.apache.directory.ldap.client.api.ValidatingPoolableLdapConnectionFactory(ldapConnectionFactory));
        org.apache.directory.ldap.client.template.LdapConnectionTemplate template = new org.apache.directory.ldap.client.template.LdapConnectionTemplate(ldapConnectionPool);
        org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory.LOG.info("Ldap connection template instance: {}", template);
        return template;
    }

    public org.apache.directory.ldap.client.template.LdapConnectionTemplate load() throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        if (null == ldapConnectionTemplateInstance) {
            ldapConnectionTemplateInstance = create(ambariLdapConfigurationProvider.get());
        }
        return ldapConnectionTemplateInstance;
    }

    @com.google.common.eventbus.Subscribe
    public void onConfigChange(org.apache.ambari.server.events.AmbariConfigurationChangedEvent event) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        ldapConnectionTemplateInstance = create(ambariLdapConfigurationProvider.get());
    }
}