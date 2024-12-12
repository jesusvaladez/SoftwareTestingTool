package org.apache.ambari.server.ldap;
import javax.persistence.EntityManager;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.template.ConnectionCallback;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
import static org.easymock.EasyMock.createNiceMock;
@org.junit.Ignore
public class LdapModuleFunctionalTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.LdapModuleFunctionalTest.class);

    private static com.google.inject.Injector injector;

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        com.google.inject.Module testModule = com.google.inject.util.Modules.override(new org.apache.ambari.server.ldap.LdapModule()).with(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(EasyMock.createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            }
        });
        org.apache.ambari.server.ldap.LdapModuleFunctionalTest.injector = com.google.inject.Guice.createInjector(testModule);
    }

    @org.junit.Test
    public void shouldLdapTemplateBeInstantiated() throws java.lang.Exception {
        org.junit.Assert.assertNotNull(org.apache.ambari.server.ldap.LdapModuleFunctionalTest.injector);
        org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory ldapConnectionTemplateFactory = org.apache.ambari.server.ldap.LdapModuleFunctionalTest.injector.getInstance(org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(org.apache.ambari.server.ldap.LdapModuleFunctionalTest.getProps());
        org.apache.directory.ldap.client.template.LdapConnectionTemplate template = ldapConnectionTemplateFactory.create(ldapConfiguration);
        org.junit.Assert.assertNotNull(template);
        java.lang.Boolean success = template.execute(new org.apache.directory.ldap.client.template.ConnectionCallback<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean doWithConnection(org.apache.directory.ldap.client.api.LdapConnection connection) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return connection.isConnected() && connection.isAuthenticated();
            }
        });
        org.junit.Assert.assertTrue("Could not bind to the LDAP server", success);
    }

    private static java.util.Map<java.lang.String, java.lang.String> getProps() {
        java.util.Map<java.lang.String, java.lang.String> ldapPropsMap = com.google.common.collect.Maps.newHashMap();
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND.key(), "true");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST.key(), "ldap.forumsys.com");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT.key(), "389");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN.key(), "cn=read-only-admin,dc=example,dc=com");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD.key(), "password");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS.key(), SchemaConstants.PERSON_OC);
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.key(), SchemaConstants.UID_AT);
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE.key(), "dc=example,dc=com");
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DN_ATTRIBUTE.key(), SchemaConstants.UID_AT);
        ldapPropsMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE_TYPE.key(), "JKS");
        return ldapPropsMap;
    }

    @org.junit.Test
    public void testShouldDetectorsBeBound() throws java.lang.Exception {
        org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory f = org.apache.ambari.server.ldap.LdapModuleFunctionalTest.injector.getInstance(org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.class);
        org.junit.Assert.assertNotNull(f);
        org.apache.ambari.server.ldap.LdapModuleFunctionalTest.LOG.info(f.groupAttributeDetector().toString());
        org.apache.ambari.server.ldap.LdapModuleFunctionalTest.LOG.info(f.userAttributDetector().toString());
    }
}