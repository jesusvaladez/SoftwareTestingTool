package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.template.ConnectionCallback;
import org.apache.directory.ldap.client.template.EntryMapper;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
public class DefaultLdapConfigurationServiceTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory ldapConnectionTemplateFactory;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplateMock;

    @org.easymock.TestSubject
    private org.apache.ambari.server.ldap.service.LdapConfigurationService ldapConfigurationService = new org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService();

    @org.junit.Before
    public void before() {
        resetAll();
    }

    @org.junit.Test
    public void testShouldConnectionCheckSucceedWhenConnectionCallbackSucceeds() throws java.lang.Exception {
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(com.google.common.collect.Maps.newHashMap());
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.execute(org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.ConnectionCallback.class))).andReturn(java.lang.Boolean.TRUE);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        replayAll();
        ldapConfigurationService.checkConnection(ambariLdapConfiguration);
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldConnectionCheckFailWhenConnectionCallbackFails() throws java.lang.Exception {
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(com.google.common.collect.Maps.newHashMap());
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.execute(org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.ConnectionCallback.class))).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        replayAll();
        ldapConfigurationService.checkConnection(ambariLdapConfiguration);
    }

    @org.junit.Test
    public void testShouldUserAttributeConfigurationCheckSucceedWhenUserDnIsFound() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS.key(), "person");
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.key(), "uid");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.searchFirst(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.name.Dn.class), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class), org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.EntryMapper.class))).andReturn("dn");
        replayAll();
        java.lang.String userDn = ldapConfigurationService.checkUserAttributes("testUser", "testPassword", ambariLdapConfiguration);
        org.junit.Assert.assertEquals("The found userDn is not the expected one", userDn, "dn");
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldUserAttributeConfigurationCheckFailWhenNoUsersFound() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS.key(), "posixAccount");
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.key(), "dn");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.searchFirst(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.name.Dn.class), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class), org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.EntryMapper.class))).andReturn(null);
        replayAll();
        java.lang.String userDn = ldapConfigurationService.checkUserAttributes("testUser", "testPassword", ambariLdapConfiguration);
        org.junit.Assert.assertEquals("The found userDn is not the expected one", userDn, "dn");
    }

    @org.junit.Test
    public void testShouldGroupAttributeConfigurationCheckSucceedWhenGroupForUserDnIsFound() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = groupConfigObjectMap();
        org.apache.directory.api.ldap.model.message.SearchRequest sr = new org.apache.directory.api.ldap.model.message.SearchRequestImpl();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.newSearchRequest(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.name.Dn.class), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class))).andReturn(sr);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.search(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchRequest.class), org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.EntryMapper.class))).andReturn(com.google.common.collect.Lists.newArrayList("userGroup"));
        replayAll();
        java.util.Set<java.lang.String> userGroups = ldapConfigurationService.checkGroupAttributes("userDn", ambariLdapConfiguration);
        org.junit.Assert.assertNotNull("No groups found", userGroups);
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldGroupAttributeConfigurationCheckFailWhenNoGroupsForUserDnFound() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = groupConfigObjectMap();
        org.apache.directory.api.ldap.model.message.SearchRequest sr = new org.apache.directory.api.ldap.model.message.SearchRequestImpl();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(configMap);
        org.easymock.EasyMock.expect(ldapConnectionTemplateFactory.create(ambariLdapConfiguration)).andReturn(ldapConnectionTemplateMock);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.newSearchRequest(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.name.Dn.class), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchScope.class))).andReturn(sr);
        org.easymock.EasyMock.expect(ldapConnectionTemplateMock.search(org.easymock.EasyMock.anyObject(org.apache.directory.api.ldap.model.message.SearchRequest.class), org.easymock.EasyMock.anyObject(org.apache.directory.ldap.client.template.EntryMapper.class))).andReturn(com.google.common.collect.Lists.newArrayList());
        replayAll();
        java.util.Set<java.lang.String> userGroups = ldapConfigurationService.checkGroupAttributes("userDn", ambariLdapConfiguration);
        org.junit.Assert.assertNotNull("No groups found", userGroups);
    }

    private java.util.Map<java.lang.String, java.lang.String> groupConfigObjectMap() {
        java.util.Map<java.lang.String, java.lang.String> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS.key(), "groupOfNames");
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_BASE.key(), "dc=example,dc=com");
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE.key(), "uid");
        configMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE.key(), "member");
        return configMap;
    }
}