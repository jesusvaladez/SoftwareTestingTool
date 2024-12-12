package org.apache.ambari.server.ldap.service;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
public class AmbariLdapFacadeTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    public org.apache.ambari.server.ldap.service.LdapConfigurationService ldapConfigurationServiceMock;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    public org.apache.ambari.server.ldap.service.LdapAttributeDetectionService ldapAttributeDetectionServiceMock;

    @org.easymock.TestSubject
    private org.apache.ambari.server.ldap.service.LdapFacade ldapFacade = new org.apache.ambari.server.ldap.service.AmbariLdapFacade();

    private org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration;

    private org.easymock.Capture<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> ambariLdapConfigurationCapture;

    @org.junit.Before
    public void before() {
        ambariLdapConfiguration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(com.google.common.collect.Maps.newHashMap());
        ambariLdapConfigurationCapture = org.easymock.Capture.newInstance();
        resetAll();
    }

    @org.junit.Test
    public void testShouldConfigurationCheckDelegateToTheRightServiceCall() throws java.lang.Exception {
        ldapConfigurationServiceMock.checkConnection(org.easymock.EasyMock.capture(ambariLdapConfigurationCapture));
        replayAll();
        ldapFacade.checkConnection(ambariLdapConfiguration);
        org.junit.Assert.assertEquals("The configuration instance souldn't change before passing it to the service", ambariLdapConfiguration, ambariLdapConfigurationCapture.getValue());
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldConfigurationCheckFailureResultInAmbariLdapException() throws java.lang.Exception {
        ldapConfigurationServiceMock.checkConnection(org.easymock.EasyMock.anyObject(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class));
        org.easymock.EasyMock.expectLastCall().andThrow(new org.apache.ambari.server.ldap.service.AmbariLdapException("Testing ..."));
        replayAll();
        ldapFacade.checkConnection(ambariLdapConfiguration);
    }

    @org.junit.Test
    public void testShouldLdapAttributesCheckDelegateToTheRightServiceCalls() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> parameters = com.google.common.collect.Maps.newHashMap();
        parameters.put(org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_NAME.getParameterKey(), "testUser");
        parameters.put(org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_PASSWORD.getParameterKey(), "testPassword");
        org.easymock.Capture<java.lang.String> testUserCapture = org.easymock.Capture.newInstance();
        org.easymock.Capture<java.lang.String> testPasswordCapture = org.easymock.Capture.newInstance();
        org.easymock.Capture<java.lang.String> userDnCapture = org.easymock.Capture.newInstance();
        org.easymock.EasyMock.expect(ldapConfigurationServiceMock.checkUserAttributes(org.easymock.EasyMock.capture(testUserCapture), org.easymock.EasyMock.capture(testPasswordCapture), org.easymock.EasyMock.capture(ambariLdapConfigurationCapture))).andReturn("userDn");
        org.easymock.EasyMock.expect(ldapConfigurationServiceMock.checkGroupAttributes(org.easymock.EasyMock.capture(userDnCapture), org.easymock.EasyMock.capture(ambariLdapConfigurationCapture))).andReturn(com.google.common.collect.Sets.newHashSet("userGroup"));
        replayAll();
        java.util.Set<java.lang.String> testUserGroups = ldapFacade.checkLdapAttributes(parameters, ambariLdapConfiguration);
        org.junit.Assert.assertEquals("testUser", testUserCapture.getValue());
        org.junit.Assert.assertEquals("testPassword", testPasswordCapture.getValue());
        org.junit.Assert.assertEquals("userDn", userDnCapture.getValue());
        org.junit.Assert.assertTrue(testUserGroups.contains("userGroup"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldAttributeCheckFailuresResultInAmbariLdapException() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> parameters = com.google.common.collect.Maps.newHashMap();
        parameters.put(org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_NAME.getParameterKey(), "testUser");
        parameters.put(org.apache.ambari.server.ldap.service.AmbariLdapFacade.Parameters.TEST_USER_PASSWORD.getParameterKey(), "testPassword");
        org.easymock.EasyMock.expect(ldapConfigurationServiceMock.checkUserAttributes(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class))).andThrow(new org.apache.ambari.server.ldap.service.AmbariLdapException("Testing ..."));
        replayAll();
        java.util.Set<java.lang.String> testUserGroups = ldapFacade.checkLdapAttributes(parameters, ambariLdapConfiguration);
    }

    @org.junit.Test
    public void testShouldLdapAttributeDetectionDelegateToTheRightServiceCalls() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> userConfigMap = com.google.common.collect.Maps.newHashMap();
        userConfigMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.key(), "uid");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration userAttrDecoratedConfig = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(userConfigMap);
        java.util.Map<java.lang.String, java.lang.String> groupConfigMap = com.google.common.collect.Maps.newHashMap(userConfigMap);
        groupConfigMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE.key(), "dn");
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration groupAttrDecoratedConfig = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(groupConfigMap);
        org.easymock.Capture<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> userAttrDetectionConfigCapture = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> groupAttrDetectionConfigCapture = org.easymock.Capture.newInstance();
        org.easymock.EasyMock.expect(ldapAttributeDetectionServiceMock.detectLdapUserAttributes(org.easymock.EasyMock.capture(userAttrDetectionConfigCapture))).andReturn(userAttrDecoratedConfig);
        org.easymock.EasyMock.expect(ldapAttributeDetectionServiceMock.detectLdapGroupAttributes(org.easymock.EasyMock.capture(groupAttrDetectionConfigCapture))).andReturn(groupAttrDecoratedConfig);
        replayAll();
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detected = ldapFacade.detectAttributes(ambariLdapConfiguration);
        org.junit.Assert.assertEquals("User attribute detection called with the wrong configuration", ambariLdapConfiguration, userAttrDetectionConfigCapture.getValue());
        org.junit.Assert.assertEquals("Group attribute detection called with the wrong configuration", userAttrDecoratedConfig, groupAttrDetectionConfigCapture.getValue());
        org.junit.Assert.assertEquals("Attribute detection returned an invalid configuration", groupAttrDecoratedConfig, detected);
    }

    @org.junit.Test(expected = org.apache.ambari.server.ldap.service.AmbariLdapException.class)
    public void testShouldAttributeDetectionFailuresResultInAmbariLdapException() throws java.lang.Exception {
        org.easymock.EasyMock.expect(ldapAttributeDetectionServiceMock.detectLdapUserAttributes(org.easymock.EasyMock.anyObject(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class))).andThrow(new org.apache.ambari.server.ldap.service.AmbariLdapException("Testing ..."));
        replayAll();
        ldapFacade.detectAttributes(ambariLdapConfiguration);
    }
}