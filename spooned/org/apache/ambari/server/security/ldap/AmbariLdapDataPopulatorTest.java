package org.apache.ambari.server.security.ldap;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.security.authorization.AmbariLdapUtils.class)
public class AmbariLdapDataPopulatorTest {
    public static class AmbariLdapDataPopulatorTestInstance extends org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.TestAmbariLdapDataPopulator {
        public AmbariLdapDataPopulatorTestInstance(com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider, org.apache.ambari.server.security.authorization.Users users) {
            super(configurationProvider, users);
        }

        @java.lang.Override
        protected org.springframework.ldap.core.LdapTemplate loadLdapTemplate() {
            return ldapTemplate;
        }
    }

    public static class TestAmbariLdapDataPopulator extends org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator {
        protected org.springframework.ldap.core.LdapTemplate ldapTemplate;

        private org.springframework.ldap.core.support.LdapContextSource ldapContextSource;

        private org.springframework.ldap.control.PagedResultsDirContextProcessor processor;

        public TestAmbariLdapDataPopulator(com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider, org.apache.ambari.server.security.authorization.Users users) {
            super(configurationProvider, users);
        }

        @java.lang.Override
        protected org.springframework.ldap.core.support.LdapContextSource createLdapContextSource() {
            return ldapContextSource;
        }

        @java.lang.Override
        protected org.springframework.ldap.core.LdapTemplate createLdapTemplate(org.springframework.ldap.core.support.LdapContextSource ldapContextSource) {
            this.ldapContextSource = ldapContextSource;
            return ldapTemplate;
        }

        @java.lang.Override
        protected org.springframework.ldap.control.PagedResultsDirContextProcessor createPagingProcessor() {
            return processor;
        }

        public void setLdapContextSource(org.springframework.ldap.core.support.LdapContextSource ldapContextSource) {
            this.ldapContextSource = ldapContextSource;
        }

        public void setProcessor(org.springframework.ldap.control.PagedResultsDirContextProcessor processor) {
            this.processor = processor;
        }

        public void setLdapTemplate(org.springframework.ldap.core.LdapTemplate ldapTemplate) {
            this.ldapTemplate = ldapTemplate;
        }

        public org.apache.ambari.server.security.authorization.LdapServerProperties getLdapServerProperties() {
            return this.ldapServerProperties;
        }

        public void setLdapServerProperties(org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties) {
            this.ldapServerProperties = ldapServerProperties;
        }

        public org.springframework.ldap.core.support.LdapContextSource getLdapContextSource() {
            return ldapContextSource;
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsLdapEnabled_badConfiguration() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.ldapEnabled()).andReturn(true);
        EasyMock.expect(ldapTemplate.search(org.easymock.EasyMock.<java.lang.String>anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.<org.springframework.ldap.core.AttributesMapper>anyObject())).andThrow(new java.lang.NullPointerException()).once();
        EasyMock.replay(ldapTemplate, configurationProvider, configuration, ldapServerProperties);
        final org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        junit.framework.Assert.assertFalse(populator.isLdapEnabled());
        EasyMock.verify(populator.loadLdapTemplate(), configurationProvider, configuration);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testReferralMethod() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.support.LdapContextSource ldapContextSource = EasyMock.createNiceMock(org.springframework.ldap.core.support.LdapContextSource.class);
        java.util.List<java.lang.String> ldapUrls = java.util.Collections.singletonList("url");
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getLdapUrls()).andReturn(ldapUrls).anyTimes();
        EasyMock.expect(ldapServerProperties.getReferralMethod()).andReturn("follow");
        ldapContextSource.setReferral("follow");
        ldapTemplate.setIgnorePartialResultException(true);
        EasyMock.replay(ldapTemplate, configurationProvider, configuration, ldapServerProperties, ldapContextSource);
        final org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.TestAmbariLdapDataPopulator populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.TestAmbariLdapDataPopulator(configurationProvider, users);
        populator.setLdapContextSource(ldapContextSource);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        populator.loadLdapTemplate();
        EasyMock.verify(ldapTemplate, configurationProvider, configuration, ldapServerProperties, ldapContextSource);
    }

    @org.junit.Test
    public void testIsLdapEnabled_reallyEnabled() {
        @java.lang.SuppressWarnings("unchecked")
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.ldapEnabled()).andReturn(true);
        EasyMock.expect(ldapTemplate.search(org.easymock.EasyMock.<java.lang.String>anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.<org.springframework.ldap.core.AttributesMapper>anyObject())).andReturn(java.util.Collections.emptyList()).once();
        EasyMock.replay(ldapTemplate, configurationProvider, configuration);
        final org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        junit.framework.Assert.assertTrue(populator.isLdapEnabled());
        EasyMock.verify(populator.loadLdapTemplate(), configurationProvider, configuration);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsLdapEnabled_reallyDisabled() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.ldapEnabled()).andReturn(false);
        EasyMock.replay(ldapTemplate, ldapServerProperties, configurationProvider, configuration);
        final org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        junit.framework.Assert.assertFalse(populator.isLdapEnabled());
        EasyMock.verify(populator.loadLdapTemplate(), populator.getLdapServerProperties(), configurationProvider, configuration);
    }

    private <T> java.util.Set<T> createSet(T... elements) {
        return new java.util.HashSet<>(java.util.Arrays.asList(elements));
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void synchronizeExistingLdapGroups() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group5 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group5.getGroupName()).andReturn("group5").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group5.isLdapGroup()).andReturn(true).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4, group5);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4, group5);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapGroups").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        EasyMock.expect(populator.getLdapGroups("group2")).andReturn(java.util.Collections.emptySet());
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup1), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
        EasyMock.expectLastCall();
        EasyMock.expect(populator.getLdapGroups("group4")).andReturn(java.util.Collections.singleton(externalGroup1));
        EasyMock.expect(populator.getLdapGroups("group5")).andReturn(java.util.Collections.emptySet());
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeExistingLdapGroups(batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBeRemoved(), com.google.common.collect.Sets.newHashSet("group2", "group5"));
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeExistingLdapGroups_removeDuringIteration() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupId()).andReturn(1).anyTimes();
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(true).anyTimes();
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group2.getGroupId()).andReturn(2).anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getAllGroups()).andReturn(java.util.Arrays.asList(group1, group2));
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(new org.apache.ambari.server.security.authorization.LdapServerProperties()).anyTimes();
        org.apache.ambari.server.security.ldap.LdapGroupDto group1Dto = new org.apache.ambari.server.security.ldap.LdapGroupDto();
        group1Dto.setGroupName("group1");
        group1Dto.setMemberAttributes(com.google.common.collect.Sets.newHashSet("group2"));
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupDtos1 = com.google.common.collect.Sets.newHashSet();
        groupDtos1.add(group1Dto);
        org.apache.ambari.server.security.ldap.LdapGroupDto group2Dto = new org.apache.ambari.server.security.ldap.LdapGroupDto();
        group2Dto.setGroupName("group2");
        group2Dto.setMemberAttributes(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupDtos2 = com.google.common.collect.Sets.newHashSet();
        groupDtos2.add(group2Dto);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        EasyMock.replay(configurationProvider, configuration, users, group1, group2);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator dataPopulator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).withConstructor(configurationProvider, users).addMockedMethod("getLdapGroups").addMockedMethod("getLdapUserByMemberAttr").addMockedMethod("getLdapGroupByMemberAttr").createNiceMock();
        EasyMock.expect(dataPopulator.getLdapUserByMemberAttr(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.expect(dataPopulator.getLdapGroupByMemberAttr("group2")).andReturn(group2Dto);
        EasyMock.expect(dataPopulator.getLdapGroups("group1")).andReturn(groupDtos1).anyTimes();
        EasyMock.expect(dataPopulator.getLdapGroups("group2")).andReturn(groupDtos2).anyTimes();
        EasyMock.replay(dataPopulator);
        dataPopulator.synchronizeExistingLdapGroups(batchInfo, false);
        EasyMock.verify(dataPopulator, group1, group2);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapGroups_allExist() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapGroups").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(externalGroup3.getGroupName()).andReturn("xgroup1").anyTimes();
        EasyMock.expect(externalGroup4.getGroupName()).andReturn("xgroup2").anyTimes();
        EasyMock.replay(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup3, externalGroup4);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup1), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
        EasyMock.expectLastCall();
        populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup2), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
        EasyMock.expectLastCall();
        EasyMock.expect(populator.getLdapGroups("x*")).andReturn(externalGroups);
        EasyMock.expect(populator.getLdapGroups("group1")).andReturn(java.util.Collections.singleton(externalGroup1));
        EasyMock.expect(populator.getLdapGroups("group2")).andReturn(java.util.Collections.singleton(externalGroup2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapGroups(createSet("x*", "group1", "group2"), batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBecomeLdap(), com.google.common.collect.Sets.newHashSet("group1"));
        verifyGroupsInSet(result.getGroupsToBeCreated(), com.google.common.collect.Sets.newHashSet("xgroup1", "xgroup2"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        verifyGroupsInSet(result.getGroupsProcessedInternal(), com.google.common.collect.Sets.newHashSet("group1", "group2"));
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapGroups_add() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapGroups").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(externalGroup3.getGroupName()).andReturn("xgroup1").anyTimes();
        EasyMock.expect(externalGroup4.getGroupName()).andReturn("xgroup2").anyTimes();
        EasyMock.replay(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup3, externalGroup4);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup2), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
        EasyMock.expectLastCall();
        EasyMock.expect(populator.getLdapGroups("x*")).andReturn(externalGroups);
        EasyMock.expect(populator.getLdapGroups("group2")).andReturn(java.util.Collections.singleton(externalGroup2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapGroups(createSet("x*", "group2"), batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBeCreated(), com.google.common.collect.Sets.newHashSet("xgroup1", "xgroup2"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapGroups_update() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapGroups").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(externalGroup3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(externalGroup4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.replay(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        EasyMock.expect(populator.getLdapGroups("group*")).andReturn(externalGroups);
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapGroups(createSet("group*"), batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBecomeLdap(), com.google.common.collect.Sets.newHashSet("group1", "group4"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testSynchronizeLdapGroups_absent() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapGroups").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(externalGroup3.getGroupName()).andReturn("xgroup1").anyTimes();
        EasyMock.expect(externalGroup4.getGroupName()).andReturn("xgroup2").anyTimes();
        EasyMock.replay(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup3, externalGroup4);
        EasyMock.expect(populator.getLdapGroups("x*")).andReturn(externalGroups);
        EasyMock.expect(populator.getLdapGroups("group1")).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(populator.getLdapGroups("group2")).andReturn(java.util.Collections.singleton(externalGroup2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        populator.synchronizeLdapGroups(createSet("x*", "group1", "group2"), batchInfo, false);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapGroups() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group5 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group5.getGroupName()).andReturn("group5").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group5.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4, group5);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4, group5);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapGroupInfo").addMockedMethod("refreshGroupMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(externalGroup3.getGroupName()).andReturn("group6").anyTimes();
        EasyMock.expect(externalGroup4.getGroupName()).andReturn("group7").anyTimes();
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        EasyMock.expect(populator.getExternalLdapGroupInfo()).andReturn(externalGroups);
        EasyMock.replay(externalGroup1, externalGroup2, externalGroup3, externalGroup4);
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapGroups(batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBeRemoved(), com.google.common.collect.Sets.newHashSet("group2"));
        verifyGroupsInSet(result.getGroupsToBecomeLdap(), com.google.common.collect.Sets.newHashSet("group3"));
        verifyGroupsInSet(result.getGroupsToBeCreated(), com.google.common.collect.Sets.newHashSet("group6", "group7"));
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapGroups_add() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(false).anyTimes();
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(java.util.Arrays.asList(group1, group2));
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("refreshGroupMembers").addMockedMethod("getExternalLdapGroupInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group3").anyTimes();
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup1, externalGroup2);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        EasyMock.expect(populator.getExternalLdapGroupInfo()).andReturn(externalGroups);
        EasyMock.replay(externalGroup1, externalGroup2);
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapGroups(batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBeCreated(), com.google.common.collect.Sets.newHashSet("group3", "group4"));
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapGroups_remove() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group4.getGroupName()).andReturn("group4").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group4.isLdapGroup()).andReturn(true).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3, group4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3, group4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("refreshGroupMembers").addMockedMethod("getExternalLdapGroupInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group3").anyTimes();
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup1);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        EasyMock.expect(populator.getExternalLdapGroupInfo()).andReturn(externalGroups);
        EasyMock.replay(populator);
        EasyMock.replay(externalGroup1);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapGroups(batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBeRemoved(), com.google.common.collect.Sets.newHashSet("group2", "group4"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapGroups_update() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(group3.getGroupName()).andReturn("group3").anyTimes();
        EasyMock.expect(group1.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(false).anyTimes();
        EasyMock.expect(group3.isLdapGroup()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = java.util.Arrays.asList(group1, group2, group3);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllGroups()).andReturn(groupList);
        EasyMock.expect(users.getAllUsers()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(group1, group2, group3);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("refreshGroupMembers").addMockedMethod("getExternalLdapGroupInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup1.getGroupName()).andReturn("group2").anyTimes();
        EasyMock.expect(externalGroup2.getGroupName()).andReturn("group3").anyTimes();
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = createSet(externalGroup1, externalGroup2);
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            populator.refreshGroupMembers(EasyMock.eq(batchInfo), EasyMock.eq(externalGroup), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false));
            EasyMock.expectLastCall();
        }
        EasyMock.expect(populator.getExternalLdapGroupInfo()).andReturn(externalGroups);
        EasyMock.replay(populator);
        EasyMock.replay(externalGroup1, externalGroup2);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapGroups(batchInfo, false);
        verifyGroupsInSet(result.getGroupsToBecomeLdap(), com.google.common.collect.Sets.newHashSet("group2", "group3"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapUsers() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("synced_user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("synced_user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("unsynced_user1").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("unsynced_user2").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3, user4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user3, user2, user4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapUserInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("synced_user2").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("unsynced_user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("external_user1").anyTimes();
        EasyMock.expect(externalUser4.getUserName()).andReturn("external_user2").anyTimes();
        EasyMock.replay(externalUser1, externalUser2, externalUser3, externalUser4);
        EasyMock.expect(populator.getExternalLdapUserInfo()).andReturn(createSet(externalUser1, externalUser2, externalUser3, externalUser4));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeRemoved(), com.google.common.collect.Sets.newHashSet("synced_user1"));
        verifyUsersInSet(result.getUsersToBeCreated(), com.google.common.collect.Sets.newHashSet("external_user1", "external_user2"));
        verifyUsersInSet(result.getUsersToBecomeLdap(), com.google.common.collect.Sets.newHashSet("unsynced_user2"));
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapSkipLocal() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("local1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("local2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("ldap1").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.syncCollisionHandlingBehavior()).andReturn(org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.SKIP).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user3, user2);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapUserInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("local1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("local2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("ldap1").anyTimes();
        EasyMock.replay(externalUser1, externalUser2, externalUser3);
        EasyMock.expect(populator.getExternalLdapUserInfo()).andReturn(createSet(externalUser1, externalUser2, externalUser3));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersSkipped(), com.google.common.collect.Sets.newHashSet("local1", "local2"));
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapUsers_add() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapUserInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user3").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user4").anyTimes();
        EasyMock.replay(externalUser1, externalUser2);
        EasyMock.expect(populator.getExternalLdapUserInfo()).andReturn(createSet(externalUser1, externalUser2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeCreated(), com.google.common.collect.Sets.newHashSet("user3", "user4"));
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapUsers_remove() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user3").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapUserInfo").withConstructor(configurationProvider, users).createNiceMock();
        EasyMock.expect(populator.getExternalLdapUserInfo()).andReturn(java.util.Collections.emptySet());
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeRemoved(), com.google.common.collect.Sets.newHashSet("user3", "user1"));
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeAllLdapUsers_update() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user3").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getExternalLdapUserInfo").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user3").anyTimes();
        EasyMock.replay(externalUser1, externalUser2);
        EasyMock.expect(populator.getExternalLdapUserInfo()).andReturn(createSet(externalUser1, externalUser2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeAllLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBecomeLdap(), com.google.common.collect.Sets.newHashSet("user3"));
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeExistingLdapUsers() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("synced_user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("synced_user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("unsynced_user1").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("unsynced_user2").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3, user4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3, user4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUsers").withConstructor(configurationProvider, users).createNiceMock();
        EasyMock.expect(populator.getLdapUsers("synced_user1")).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(populator.getLdapUsers("synced_user2")).andReturn(java.util.Collections.singleton(EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class)));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeExistingLdapUsers(new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeRemoved(), com.google.common.collect.Sets.newHashSet("synced_user1"));
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapUsers_allExist() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user5").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("user6").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3, user4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3, user4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUsers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("xuser3").anyTimes();
        EasyMock.expect(externalUser4.getUserName()).andReturn("xuser4").anyTimes();
        EasyMock.replay(externalUser1, externalUser2, externalUser3, externalUser4);
        EasyMock.expect(populator.getLdapUsers("xuser*")).andReturn(createSet(externalUser3, externalUser4));
        EasyMock.expect(populator.getLdapUsers("user1")).andReturn(java.util.Collections.singleton(externalUser1));
        EasyMock.expect(populator.getLdapUsers("user2")).andReturn(java.util.Collections.singleton(externalUser2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapUsers(createSet("user1", "user2", "xuser*"), new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeCreated(), com.google.common.collect.Sets.newHashSet("xuser3", "xuser4"));
        verifyUsersInSet(result.getUsersToBecomeLdap(), com.google.common.collect.Sets.newHashSet("user1"));
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapUsers_add() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user5").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("user6").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3, user4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3, user4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUsers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("xuser3").anyTimes();
        EasyMock.expect(externalUser4.getUserName()).andReturn("xuser4").anyTimes();
        EasyMock.replay(externalUser2, externalUser3, externalUser4);
        EasyMock.expect(populator.getLdapUsers("xuser*")).andReturn(createSet(externalUser3, externalUser4));
        EasyMock.expect(populator.getLdapUsers("user2")).andReturn(java.util.Collections.singleton(externalUser2));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapUsers(createSet("user2", "xuser*"), new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBeCreated(), com.google.common.collect.Sets.newHashSet("xuser3", "xuser4"));
        junit.framework.Assert.assertTrue(result.getUsersToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSynchronizeLdapUsers_update() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user5").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("user6").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = java.util.Arrays.asList(user1, user2, user3, user4);
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(users.getAllUsers()).andReturn(userList);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3, user4);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUsers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("user6").anyTimes();
        EasyMock.replay(externalUser2, externalUser3, externalUser1);
        EasyMock.expect(populator.getLdapUsers("user1")).andReturn(java.util.Collections.singleton(externalUser1));
        EasyMock.expect(populator.getLdapUsers("user2")).andReturn(java.util.Collections.singleton(externalUser2));
        EasyMock.expect(populator.getLdapUsers("user6")).andReturn(java.util.Collections.singleton(externalUser3));
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapBatchDto result = populator.synchronizeLdapUsers(createSet("user2", "user1", "user6"), new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
        verifyUsersInSet(result.getUsersToBecomeLdap(), com.google.common.collect.Sets.newHashSet("user1", "user6"));
        junit.framework.Assert.assertTrue(result.getUsersToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getUsersToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBeCreated().isEmpty());
        junit.framework.Assert.assertTrue(result.getGroupsToBecomeLdap().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToAdd().isEmpty());
        junit.framework.Assert.assertTrue(result.getMembershipToRemove().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testSynchronizeLdapUsers_absent() throws java.lang.Exception {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUsers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("xuser3").anyTimes();
        EasyMock.expect(externalUser4.getUserName()).andReturn("xuser4").anyTimes();
        EasyMock.replay(externalUser1, externalUser2, externalUser3, externalUser4);
        EasyMock.expect(populator.getLdapUsers("xuser*")).andReturn(createSet(externalUser3, externalUser4));
        EasyMock.expect(populator.getLdapUsers("user1")).andReturn(java.util.Collections.singleton(externalUser1));
        EasyMock.expect(populator.getLdapUsers("user2")).andReturn(java.util.Collections.emptySet());
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        populator.synchronizeLdapUsers(createSet("user1", "user2", "xuser*"), new org.apache.ambari.server.security.ldap.LdapBatchDto(), false);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testRefreshGroupMembers() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.User user1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user3 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.User user4 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(user2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(user3.getUserName()).andReturn("user3").anyTimes();
        EasyMock.expect(user4.getUserName()).andReturn("user4").anyTimes();
        EasyMock.expect(user1.isLdapUser()).andReturn(false).anyTimes();
        EasyMock.expect(user2.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user3.isLdapUser()).andReturn(true).anyTimes();
        EasyMock.expect(user4.isLdapUser()).andReturn(false).anyTimes();
        org.apache.ambari.server.security.authorization.Group group1 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        org.apache.ambari.server.security.authorization.Group group2 = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Group.class);
        EasyMock.expect(group1.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group2.isLdapGroup()).andReturn(true).anyTimes();
        EasyMock.expect(group1.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(group2.getGroupName()).andReturn("group2").anyTimes();
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("cn").anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("uid").anyTimes();
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration);
        EasyMock.replay(user1, user2, user3, user4);
        EasyMock.replay(group1, group2);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = EasyMock.createMockBuilder(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance.class).addMockedMethod("getLdapUserByMemberAttr").addMockedMethod("getLdapGroupByMemberAttr").addMockedMethod("getInternalMembers").withConstructor(configurationProvider, users).createNiceMock();
        org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapGroupDto.class);
        EasyMock.expect(externalGroup.getGroupName()).andReturn("group1").anyTimes();
        EasyMock.expect(externalGroup.getMemberAttributes()).andReturn(createSet("user1", "user2", "user4", "user6")).anyTimes();
        EasyMock.replay(externalGroup);
        java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalMembers = new java.util.HashMap<>();
        internalMembers.put("user1", user1);
        internalMembers.put("user3", user3);
        internalMembers.put("user4", user4);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser1 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser2 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser3 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        org.apache.ambari.server.security.ldap.LdapUserDto externalUser4 = EasyMock.createNiceMock(org.apache.ambari.server.security.ldap.LdapUserDto.class);
        EasyMock.expect(externalUser1.getUserName()).andReturn("user1").anyTimes();
        EasyMock.expect(externalUser2.getUserName()).andReturn("user2").anyTimes();
        EasyMock.expect(externalUser3.getUserName()).andReturn("user4").anyTimes();
        EasyMock.expect(externalUser4.getUserName()).andReturn("user6").anyTimes();
        EasyMock.replay(externalUser1, externalUser2, externalUser3, externalUser4);
        EasyMock.expect(populator.getLdapUserByMemberAttr("user1")).andReturn(externalUser1).anyTimes();
        EasyMock.expect(populator.getLdapUserByMemberAttr("user2")).andReturn(externalUser2).anyTimes();
        EasyMock.expect(populator.getLdapUserByMemberAttr("user4")).andReturn(null).anyTimes();
        EasyMock.expect(populator.getLdapGroupByMemberAttr("user4")).andReturn(externalGroup).anyTimes();
        EasyMock.expect(populator.getLdapUserByMemberAttr("user6")).andReturn(externalUser4).anyTimes();
        EasyMock.expect(populator.getInternalMembers("group1")).andReturn(internalMembers).anyTimes();
        EasyMock.replay(populator);
        populator.setLdapTemplate(ldapTemplate);
        populator.setLdapServerProperties(ldapServerProperties);
        java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsers = new java.util.HashMap<>();
        internalUsers.putAll(internalMembers);
        internalUsers.put("user2", user2);
        java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroups = new java.util.HashMap<>();
        internalGroups.put("group2", group2);
        populator.refreshGroupMembers(batchInfo, externalGroup, internalUsers, internalGroups, null, true, false);
        verifyMembershipInSet(batchInfo.getMembershipToAdd(), com.google.common.collect.Sets.newHashSet("user1", "user2", "user6"));
        verifyMembershipInSet(batchInfo.getMembershipToRemove(), com.google.common.collect.Sets.newHashSet("user3", "user4"));
        verifyUsersInSet(batchInfo.getUsersToBeCreated(), com.google.common.collect.Sets.newHashSet("user6"));
        verifyUsersInSet(batchInfo.getUsersToBecomeLdap(), com.google.common.collect.Sets.newHashSet("user1"));
        junit.framework.Assert.assertTrue(batchInfo.getGroupsToBecomeLdap().isEmpty());
        verifyGroupsInSet(batchInfo.getGroupsToBeCreated(), com.google.common.collect.Sets.newHashSet("group1"));
        junit.framework.Assert.assertTrue(batchInfo.getGroupsToBeRemoved().isEmpty());
        junit.framework.Assert.assertTrue(batchInfo.getUsersToBeRemoved().isEmpty());
        EasyMock.verify(populator.loadLdapTemplate(), populator);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings({ "serial", "unchecked" })
    public void testCleanUpLdapUsersWithoutGroup() throws org.apache.ambari.server.AmbariException {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        final org.apache.ambari.server.orm.entities.GroupEntity ldapGroup = new org.apache.ambari.server.orm.entities.GroupEntity();
        ldapGroup.setGroupId(1);
        ldapGroup.setGroupName("ldapGroup");
        ldapGroup.setGroupType(org.apache.ambari.server.security.authorization.GroupType.LDAP);
        ldapGroup.setMemberEntities(new java.util.HashSet<>());
        final org.apache.ambari.server.security.authorization.User ldapUserWithoutGroup = createLdapUserWithoutGroup();
        final org.apache.ambari.server.security.authorization.User ldapUserWithGroup = createLdapUserWithGroup(ldapGroup);
        final org.apache.ambari.server.security.authorization.User localUserWithoutGroup = createLocalUserWithoutGroup();
        final org.apache.ambari.server.security.authorization.User localUserWithGroup = createLocalUserWithGroup(ldapGroup);
        final java.util.List<org.apache.ambari.server.security.authorization.User> allUsers = new java.util.ArrayList<org.apache.ambari.server.security.authorization.User>() {
            {
                add(ldapUserWithoutGroup);
                add(ldapUserWithGroup);
                add(localUserWithoutGroup);
                add(localUserWithGroup);
            }
        };
        EasyMock.expect(users.getAllUsers()).andReturn(new java.util.ArrayList<>(allUsers));
        final java.util.List<org.apache.ambari.server.security.authorization.User> removedUsers = new java.util.ArrayList<>();
        final org.easymock.Capture<org.apache.ambari.server.security.authorization.User> userCapture = org.easymock.EasyMock.newCapture();
        users.removeUser(EasyMock.capture(userCapture));
        EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer() throws java.lang.Throwable {
                removedUsers.add(userCapture.getValue());
                allUsers.remove(userCapture.getValue());
                return null;
            }
        });
        EasyMock.replay(users, configurationProvider, configuration);
        final org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class));
        populator.setLdapServerProperties(EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class));
        populator.cleanUpLdapUsersWithoutGroup();
        org.junit.Assert.assertEquals(removedUsers.size(), 1);
        org.junit.Assert.assertEquals(allUsers.size(), 3);
        junit.framework.Assert.assertTrue(allUsers.contains(ldapUserWithGroup));
        junit.framework.Assert.assertTrue(allUsers.contains(localUserWithoutGroup));
        junit.framework.Assert.assertTrue(allUsers.contains(localUserWithGroup));
        org.junit.Assert.assertEquals(removedUsers.get(0), ldapUserWithoutGroup);
        EasyMock.verify(users);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetLdapUserByMemberAttr() throws java.lang.Exception {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        org.easymock.Capture<org.springframework.ldap.core.ContextMapper> contextMapperCapture = org.easymock.EasyMock.newCapture();
        org.springframework.ldap.control.PagedResultsDirContextProcessor processor = EasyMock.createNiceMock(org.springframework.ldap.control.PagedResultsDirContextProcessor.class);
        org.springframework.ldap.control.PagedResultsCookie cookie = EasyMock.createNiceMock(org.springframework.ldap.control.PagedResultsCookie.class);
        org.apache.ambari.server.security.ldap.LdapUserDto dto = new org.apache.ambari.server.security.ldap.LdapUserDto();
        java.util.List<org.apache.ambari.server.security.ldap.LdapUserDto> list = new java.util.LinkedList<>();
        list.add(dto);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.isPaginationEnabled()).andReturn(true).anyTimes();
        EasyMock.expect(ldapServerProperties.getUserObjectClass()).andReturn("objectClass").anyTimes();
        EasyMock.expect(ldapServerProperties.getDnAttribute()).andReturn("dn").anyTimes();
        EasyMock.expect(ldapServerProperties.getBaseDN()).andReturn("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com").anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("uid").anyTimes();
        EasyMock.expect(processor.getCookie()).andReturn(cookie).anyTimes();
        EasyMock.expect(cookie.getCookie()).andReturn(null).anyTimes();
        EasyMock.expect(ldapTemplate.search(EasyMock.eq(org.springframework.ldap.support.LdapUtils.newLdapName("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com")), EasyMock.eq("(&(objectClass=objectClass)(uid=foo))"), EasyMock.anyObject(javax.naming.directory.SearchControls.class), EasyMock.capture(contextMapperCapture), EasyMock.eq(processor))).andReturn(list);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration, processor, cookie);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(ldapTemplate);
        populator.setProcessor(processor);
        org.junit.Assert.assertEquals(dto, populator.getLdapUserByMemberAttr("foo"));
        EasyMock.verify(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration, processor, cookie);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetLdapUserByMemberAttrNoPagination() throws java.lang.Exception {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.springframework.ldap.core.LdapTemplate ldapTemplate = EasyMock.createNiceMock(org.springframework.ldap.core.LdapTemplate.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        org.easymock.Capture<org.springframework.ldap.core.ContextMapper> contextMapperCapture = org.easymock.EasyMock.newCapture();
        org.springframework.ldap.control.PagedResultsDirContextProcessor processor = EasyMock.createNiceMock(org.springframework.ldap.control.PagedResultsDirContextProcessor.class);
        org.springframework.ldap.control.PagedResultsCookie cookie = EasyMock.createNiceMock(org.springframework.ldap.control.PagedResultsCookie.class);
        org.apache.ambari.server.security.ldap.LdapUserDto dto = new org.apache.ambari.server.security.ldap.LdapUserDto();
        java.util.List<org.apache.ambari.server.security.ldap.LdapUserDto> list = new java.util.LinkedList<>();
        list.add(dto);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.isPaginationEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(ldapServerProperties.getUserObjectClass()).andReturn("objectClass").anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("uid").anyTimes();
        EasyMock.expect(ldapServerProperties.getDnAttribute()).andReturn("dn").anyTimes();
        EasyMock.expect(ldapServerProperties.getBaseDN()).andReturn("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com").anyTimes();
        EasyMock.expect(ldapTemplate.search(EasyMock.eq(org.springframework.ldap.support.LdapUtils.newLdapName("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com")), EasyMock.eq("(&(objectClass=objectClass)(uid=foo))"), EasyMock.anyObject(javax.naming.directory.SearchControls.class), EasyMock.capture(contextMapperCapture))).andReturn(list);
        EasyMock.replay(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration, processor, cookie);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        populator.setLdapTemplate(ldapTemplate);
        populator.setProcessor(processor);
        org.junit.Assert.assertEquals(dto, populator.getLdapUserByMemberAttr("foo"));
        EasyMock.verify(ldapTemplate, ldapServerProperties, users, configurationProvider, configuration, processor, cookie);
    }

    @org.junit.Test
    public void testLdapUserContextMapper_uidIsNull() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("cn").once();
        EasyMock.expect(ldapServerProperties.getBaseDN()).andReturn("dc=SME,dc=support,dc=com").anyTimes();
        org.springframework.ldap.core.DirContextAdapter adapter = EasyMock.createNiceMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapter.getStringAttribute("cn")).andReturn("testUser");
        EasyMock.expect(adapter.getStringAttribute("uid")).andReturn(null);
        EasyMock.expect(adapter.getNameInNamespace()).andReturn("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com");
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.security.authorization.AmbariLdapUtils.class);
        EasyMock.expect(org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, "dc=SME,dc=support,dc=com")).andReturn(false).anyTimes();
        EasyMock.replay(adapter, ldapServerProperties);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper ldapUserContextMapper = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapUserDto userDto = ((org.apache.ambari.server.security.ldap.LdapUserDto) (ldapUserContextMapper.mapFromContext(adapter)));
        junit.framework.Assert.assertNotNull(userDto);
        junit.framework.Assert.assertNull(userDto.getUid());
        org.junit.Assert.assertEquals("testuser", userDto.getUserName());
        org.junit.Assert.assertEquals("cn=testuser,ou=ambari,dc=sme,dc=support,dc=com", userDto.getDn());
    }

    @org.junit.Test
    public void testLdapUserContextMapper_uidAndUsernameAreNull() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("cn").once();
        org.springframework.ldap.core.DirContextAdapter adapter = EasyMock.createNiceMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapter.getStringAttribute("cn")).andReturn(null);
        EasyMock.expect(adapter.getStringAttribute("uid")).andReturn(null);
        EasyMock.replay(ldapServerProperties, adapter);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper ldapUserContextMapper = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper(ldapServerProperties);
        junit.framework.Assert.assertNull(ldapUserContextMapper.mapFromContext(adapter));
    }

    @org.junit.Test
    public void testLdapUserContextMapper() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("cn").once();
        EasyMock.expect(ldapServerProperties.getBaseDN()).andReturn("dc=SME,dc=support,dc=com").anyTimes();
        org.springframework.ldap.core.DirContextAdapter adapter = EasyMock.createNiceMock(org.springframework.ldap.core.DirContextAdapter.class);
        EasyMock.expect(adapter.getStringAttribute("cn")).andReturn("testUser");
        EasyMock.expect(adapter.getStringAttribute("uid")).andReturn("UID1");
        EasyMock.expect(adapter.getNameInNamespace()).andReturn("cn=testUser,ou=Ambari,dc=SME,dc=support,dc=com");
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.security.authorization.AmbariLdapUtils.class);
        EasyMock.expect(org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, "dc=SME,dc=support,dc=com")).andReturn(false).anyTimes();
        EasyMock.replay(ldapServerProperties, adapter);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper ldapUserContextMapper = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper(ldapServerProperties);
        org.apache.ambari.server.security.ldap.LdapUserDto userDto = ((org.apache.ambari.server.security.ldap.LdapUserDto) (ldapUserContextMapper.mapFromContext(adapter)));
        junit.framework.Assert.assertNotNull(userDto);
        org.junit.Assert.assertEquals("uid1", userDto.getUid());
        org.junit.Assert.assertEquals("testuser", userDto.getUserName());
        org.junit.Assert.assertEquals("cn=testuser,ou=ambari,dc=sme,dc=support,dc=com", userDto.getDn());
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("UID");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("CN");
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("CN=mygroupname,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertTrue(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn_withUidMatch() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("UID");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("CN");
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("uid=myuid,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertTrue(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn_withLowerAndUpperCaseValue() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("uid");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("CN");
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("cn=mygroupname,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertTrue(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn_withWrongAttribute() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("uid");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("CN");
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("cnn=mygroupname,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertFalse(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn_withEmptyValues() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn(null);
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("cnn=mygroupname,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertFalse(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testIsMemberAttributeBaseDn_withDifferentUserAndGroupNameAttribute() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        EasyMock.expect(configuration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        EasyMock.expect(ldapServerProperties.getUsernameAttribute()).andReturn("sAMAccountName");
        EasyMock.expect(ldapServerProperties.getGroupNamingAttr()).andReturn("groupOfNames");
        EasyMock.replay(configurationProvider, configuration, users, ldapServerProperties);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        boolean result = populator.isMemberAttributeBaseDn("cn=mygroupname,OU=myOrganizationUnit,DC=apache,DC=org");
        junit.framework.Assert.assertTrue(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetUniqueIdMemberPattern() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        java.lang.String syncUserMemberPattern = "(?<sid>.*);(?<guid>.*);(?<member>.*)";
        java.lang.String memberAttribute = "<SID=...>;<GUID=...>;cn=member,dc=apache,dc=org";
        EasyMock.replay(configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        java.lang.String result = populator.getUniqueIdByMemberPattern(memberAttribute, syncUserMemberPattern);
        org.junit.Assert.assertEquals("cn=member,dc=apache,dc=org", result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetUniqueIdByMemberPatternWhenPatternIsWrong() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        java.lang.String syncUserMemberPattern = "(?<sid>.*);(?<guid>.*);(?<mem>.*)";
        java.lang.String memberAttribute = "<SID=...>;<GUID=...>;cn=member,dc=apache,dc=org";
        EasyMock.replay(configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        java.lang.String result = populator.getUniqueIdByMemberPattern(memberAttribute, syncUserMemberPattern);
        org.junit.Assert.assertEquals(memberAttribute, result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetUniqueIdByMemberPatternWhenPatternIsEmpty() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        java.lang.String memberAttribute = "<SID=...>;<GUID=...>;cn=member,dc=apache,dc=org";
        EasyMock.replay(configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        java.lang.String result = populator.getUniqueIdByMemberPattern(memberAttribute, "");
        org.junit.Assert.assertEquals(memberAttribute, result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testGetUniqueIdByMemberPatternWhenMembershipAttributeIsNull() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        java.lang.String syncUserMemberPattern = "(?<sid>.*);(?<guid>.*);(?<member>.*)";
        EasyMock.replay(configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        java.lang.String result = populator.getUniqueIdByMemberPattern(null, syncUserMemberPattern);
        junit.framework.Assert.assertNull(result);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCreateCustomMemberFilter() {
        final com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration = EasyMock.createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        EasyMock.expect(configurationProvider.get()).andReturn(configuration).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.replay(configurationProvider, configuration);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance populator = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.AmbariLdapDataPopulatorTestInstance(configurationProvider, users);
        org.springframework.ldap.filter.Filter result = populator.createCustomMemberFilter("myUid", "(&(objectclass=posixaccount)(uid={member}))");
        org.junit.Assert.assertEquals("(&(objectclass=posixaccount)(uid=myUid))", result.encode());
    }

    private static int userIdCounter = 1;

    private org.apache.ambari.server.security.authorization.User createUser(java.lang.String name, boolean ldapUser, org.apache.ambari.server.orm.entities.GroupEntity group) {
        final org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulatorTest.userIdCounter++);
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(name).toString());
        userEntity.setCreateTime(0);
        userEntity.setActive(true);
        userEntity.setMemberEntities(new java.util.HashSet<>());
        final org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrivileges(new java.util.HashSet<>());
        userEntity.setPrincipal(principalEntity);
        if (group != null) {
            final org.apache.ambari.server.orm.entities.MemberEntity member = new org.apache.ambari.server.orm.entities.MemberEntity();
            member.setUser(userEntity);
            member.setGroup(group);
            group.getMemberEntities().add(member);
            userEntity.getMemberEntities().add(member);
        }
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
        if (ldapUser) {
            userAuthenticationEntity.setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP);
            userAuthenticationEntity.setAuthenticationKey("some dn");
        } else {
            userAuthenticationEntity.setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
            userAuthenticationEntity.setAuthenticationKey("some password (normally encoded)");
        }
        userEntity.setAuthenticationEntities(java.util.Collections.singletonList(userAuthenticationEntity));
        return new org.apache.ambari.server.security.authorization.User(userEntity);
    }

    private org.apache.ambari.server.security.authorization.User createLdapUserWithoutGroup() {
        return createUser("LdapUserWithoutGroup", true, null);
    }

    private org.apache.ambari.server.security.authorization.User createLocalUserWithoutGroup() {
        return createUser("LocalUserWithoutGroup", false, null);
    }

    private org.apache.ambari.server.security.authorization.User createLdapUserWithGroup(org.apache.ambari.server.orm.entities.GroupEntity group) {
        return createUser("LdapUserWithGroup", true, group);
    }

    private org.apache.ambari.server.security.authorization.User createLocalUserWithGroup(org.apache.ambari.server.orm.entities.GroupEntity group) {
        return createUser("LocalUserWithGroup", false, group);
    }

    private void verifyUsersInSet(java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersToVerify, java.util.HashSet<java.lang.String> expectedUserNames) {
        org.junit.Assert.assertEquals(expectedUserNames.size(), usersToVerify.size());
        java.util.HashSet<org.apache.ambari.server.security.ldap.LdapUserDto> usersToBeVerified = new java.util.HashSet<>(usersToVerify);
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>(expectedUserNames);
        java.util.Iterator<org.apache.ambari.server.security.ldap.LdapUserDto> iterator = usersToBeVerified.iterator();
        while (iterator.hasNext()) {
            org.apache.ambari.server.security.ldap.LdapUserDto user = iterator.next();
            if (expected.remove(user.getUserName())) {
                iterator.remove();
            }
        } 
        junit.framework.Assert.assertTrue(usersToBeVerified.isEmpty());
    }

    private void verifyMembershipInSet(java.util.Set<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> membershipsToVerify, java.util.HashSet<java.lang.String> expectedUserNames) {
        org.junit.Assert.assertEquals(expectedUserNames.size(), membershipsToVerify.size());
        java.util.HashSet<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> membershipsToBeVerified = new java.util.HashSet<>(membershipsToVerify);
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>(expectedUserNames);
        java.util.Iterator<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> iterator = membershipsToBeVerified.iterator();
        while (iterator.hasNext()) {
            org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto membership = iterator.next();
            if (expected.remove(membership.getUserName())) {
                iterator.remove();
            }
        } 
        junit.framework.Assert.assertTrue(membershipsToBeVerified.isEmpty());
    }

    private void verifyGroupsInSet(java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsToVerify, java.util.HashSet<java.lang.String> expectedGroupNames) {
        org.junit.Assert.assertEquals(expectedGroupNames.size(), groupsToVerify.size());
        java.util.HashSet<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsToBeVerified = new java.util.HashSet<>(groupsToVerify);
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>(expectedGroupNames);
        java.util.Iterator<org.apache.ambari.server.security.ldap.LdapGroupDto> iterator = groupsToBeVerified.iterator();
        while (iterator.hasNext()) {
            org.apache.ambari.server.security.ldap.LdapGroupDto group = iterator.next();
            if (expected.remove(group.getGroupName())) {
                iterator.remove();
            }
        } 
        junit.framework.Assert.assertTrue(groupsToBeVerified.isEmpty());
    }
}