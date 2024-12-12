package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.template.EntryMapper;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
public class DefaultLdapAttributeDetectionServiceTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory attributeDetectorFactoryMock;

    @org.easymock.Mock
    private org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory ldapConnectionTemplateFactoryMock;

    @org.easymock.Mock
    private org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplateMock;

    @org.easymock.Mock
    private org.apache.directory.api.ldap.model.message.SearchRequest searchRequestMock;

    @org.easymock.TestSubject
    private org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService defaultLdapAttributeDetectionService = new org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService();

    @org.junit.Before
    public void before() {
        resetAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void shouldLdapUserAttributeDetection() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE.key(), "dc=example,dc=com");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        java.util.List<java.lang.Object> entryList = com.google.common.collect.Lists.newArrayList(new org.apache.directory.api.ldap.model.entry.DefaultEntry("uid=gauss"));
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactoryMock.create(ldapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.search(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchRequest.class), org.easymock.EasyMock.anyObject(entryMapperMock().getClass()))).andReturn(entryList);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.newSearchRequest(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class))).andReturn(searchRequestMock);
        org.easymock.EasyMock.expect(attributeDetectorFactoryMock.userAttributDetector()).andReturn(new org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector(com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector())));
        org.easymock.EasyMock.expect(searchRequestMock.setSizeLimit(50)).andReturn(searchRequestMock);
        replayAll();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration decorated = defaultLdapAttributeDetectionService.detectLdapUserAttributes(ldapConfiguration);
        org.junit.Assert.assertNotNull(decorated);
        org.junit.Assert.assertEquals("N/A", ldapConfiguration.userNameAttribute());
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldUserAttributeDetectionFailWhenLdapOerationFails() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE.key(), "dc=example,dc=com");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactoryMock.create(ldapConfiguration)).andThrow(new org.apache.ambari.server.ldap.service.AmbariLdapException("Testing ..."));
        replayAll();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration decorated = defaultLdapAttributeDetectionService.detectLdapUserAttributes(ldapConfiguration);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void shouldLdapGroupAttributeDetection() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_BASE.key(), "dc=example,dc=com");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        java.util.List<java.lang.Object> entryList = com.google.common.collect.Lists.newArrayList(new org.apache.directory.api.ldap.model.entry.DefaultEntry("uid=gauss"));
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactoryMock.create(ldapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.search(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchRequest.class), org.easymock.EasyMock.anyObject(entryMapperMock().getClass()))).andReturn(entryList);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.newSearchRequest(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class))).andReturn(searchRequestMock);
        org.easymock.EasyMock.expect(attributeDetectorFactoryMock.groupAttributeDetector()).andReturn(new org.apache.ambari.server.ldap.service.ads.detectors.ChainedAttributeDetector(com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector())));
        org.easymock.EasyMock.expect(searchRequestMock.setSizeLimit(50)).andReturn(searchRequestMock);
        replayAll();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration decorated = defaultLdapAttributeDetectionService.detectLdapGroupAttributes(ldapConfiguration);
        org.junit.Assert.assertNotNull(decorated);
        org.junit.Assert.assertEquals("N/A", ldapConfiguration.groupMemberAttribute());
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldGroupAttributeDetectionFailWhenLdapOerationFails() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_BASE.key(), "dc=example,dc=com");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactoryMock.create(ldapConfiguration)).andThrow(new org.apache.ambari.server.ldap.service.AmbariLdapException("Testing ..."));
        replayAll();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration decorated = defaultLdapAttributeDetectionService.detectLdapGroupAttributes(ldapConfiguration);
    }

    private org.apache.directory.ldap.client.template.EntryMapper<org.apache.directory.api.ldap.model.entry.Entry> entryMapperMock() {
        return new org.apache.directory.ldap.client.template.EntryMapper<org.apache.directory.api.ldap.model.entry.Entry>() {
            @java.lang.Override
            public org.apache.directory.api.ldap.model.entry.Entry map(org.apache.directory.api.ldap.model.entry.Entry entry) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return null;
            }
        };
    }
}