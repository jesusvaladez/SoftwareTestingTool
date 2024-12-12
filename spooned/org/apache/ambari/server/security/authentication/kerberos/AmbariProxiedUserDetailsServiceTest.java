package org.apache.ambari.server.security.authentication.kerberos;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class AmbariProxiedUserDetailsServiceTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testValidateHost() throws java.net.UnknownHostException {
        org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService service = createMockBuilder(org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService.class).withConstructor(createNiceMock(org.apache.ambari.server.configuration.Configuration.class), createNiceMock(org.apache.ambari.server.security.authorization.Users.class)).addMockedMethod("getIpAddress", java.lang.String.class).createMock();
        EasyMock.expect(service.getIpAddress("host1.example.com")).andReturn("192.168.74.101").anyTimes();
        EasyMock.expect(service.getIpAddress("host2.example.com")).andReturn("192.168.74.102").anyTimes();
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tproxyConfigration = createMock(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.class);
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("*").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("192.168.74.101").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("host1.example.com").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("192.168.74.0/24").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn(null).once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("192.168.74.102").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("host2.example.com").once();
        EasyMock.expect(tproxyConfigration.getAllowedHosts("proxyUser")).andReturn("192.168.74.1/32").once();
        replayAll();
        org.junit.Assert.assertTrue(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertTrue(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertTrue(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertTrue(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertFalse(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertFalse(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertFalse(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertFalse(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        org.junit.Assert.assertFalse(service.validateHost(tproxyConfigration, "proxyUser", "192.168.74.101"));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateUser() {
        org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService service = new org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService(createNiceMock(org.apache.ambari.server.configuration.Configuration.class), createNiceMock(org.apache.ambari.server.security.authorization.Users.class));
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tproxyConfigration = createMock(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.class);
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("*").once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("validUser").once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("validuser").once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("validUser, tom, *").once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn(null).once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("").once();
        EasyMock.expect(tproxyConfigration.getAllowedUsers("proxyUser")).andReturn("notValidUser").once();
        replayAll();
        org.junit.Assert.assertTrue(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertTrue(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertTrue(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertTrue(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertFalse(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertFalse(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        org.junit.Assert.assertFalse(service.validateUser(tproxyConfigration, "proxyUser", "validUser"));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateGroup() {
        org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService service = new org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService(createNiceMock(org.apache.ambari.server.configuration.Configuration.class), createNiceMock(org.apache.ambari.server.security.authorization.Users.class));
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration tproxyConfigration = createMock(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.class);
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("*").once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("validGroup").once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("validgroup").once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("validGroup, *").once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("").once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn(null).once();
        EasyMock.expect(tproxyConfigration.getAllowedGroups("proxyUser")).andReturn("notValidGroup").once();
        java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = new java.util.HashSet<>();
        memberEntities.add(createMockMemberEntity("validGroup"));
        memberEntities.add(createMockMemberEntity("users"));
        memberEntities.add(createMockMemberEntity(null));
        org.apache.ambari.server.orm.entities.MemberEntity memberEntity = createMock(org.apache.ambari.server.orm.entities.MemberEntity.class);
        EasyMock.expect(memberEntity.getGroup()).andReturn(null).anyTimes();
        memberEntities.add(memberEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getMemberEntities()).andReturn(memberEntities).anyTimes();
        replayAll();
        org.junit.Assert.assertTrue(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertTrue(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertTrue(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertTrue(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertFalse(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertFalse(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        org.junit.Assert.assertFalse(service.validateGroup(tproxyConfigration, "proxyUser", userEntity));
        verifyAll();
    }

    @org.junit.Test
    public void testIsInIpAddressRange() {
        org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService service = new org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService(createNiceMock(org.apache.ambari.server.configuration.Configuration.class), createNiceMock(org.apache.ambari.server.security.authorization.Users.class));
        org.junit.Assert.assertTrue(service.isInIpAddressRange("192.168.74.10/32", "192.168.74.10"));
        org.junit.Assert.assertFalse(service.isInIpAddressRange("192.168.74.10/32", "192.168.74.11"));
        for (int i = 0; i <= 255; i++) {
            org.junit.Assert.assertTrue(service.isInIpAddressRange("192.168.1.0/24", java.lang.String.format("192.168.1.%d", i)));
        }
        org.junit.Assert.assertFalse(service.isInIpAddressRange("192.168.1.0/24", "192.168.2.100"));
    }

    private org.apache.ambari.server.orm.entities.MemberEntity createMockMemberEntity(java.lang.String groupName) {
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getGroupName()).andReturn(groupName).anyTimes();
        org.apache.ambari.server.orm.entities.MemberEntity memberEntity = createMock(org.apache.ambari.server.orm.entities.MemberEntity.class);
        EasyMock.expect(memberEntity.getGroup()).andReturn(groupEntity).anyTimes();
        return memberEntity;
    }
}