package org.apache.ambari.server.security.authorization;
import org.easymock.EasyMockSupport;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ldap.core.DirContextOperations;
import static org.easymock.EasyMock.expect;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.security.authorization.AuthorizationHelper.class)
public class TestAmbariLdapAuthoritiesPopulator extends org.easymock.EasyMockSupport {
    org.apache.ambari.server.security.authorization.AuthorizationHelper helper = new org.apache.ambari.server.security.authorization.AuthorizationHelper();

    org.apache.ambari.server.orm.dao.UserDAO userDAO = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);

    org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);

    org.apache.ambari.server.orm.dao.MemberDAO memberDAO = createMock(org.apache.ambari.server.orm.dao.MemberDAO.class);

    org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);

    org.springframework.ldap.core.DirContextOperations userData = createMock(org.springframework.ldap.core.DirContextOperations.class);

    org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);

    org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        resetAll();
        org.powermock.api.easymock.PowerMock.resetAll();
    }

    @org.junit.Test
    public void testGetGrantedAuthorities() throws java.lang.Exception {
        java.lang.String username = "user";
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator populator = createMockBuilder(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class).withConstructor(helper, userDAO, memberDAO, privilegeDAO, users).createMock();
        EasyMock.expect(userEntity.getActive()).andReturn(true);
        EasyMock.expect(users.getUserPrivileges(userEntity)).andReturn(java.util.Collections.singletonList(privilegeEntity));
        EasyMock.expect(userDAO.findUserByName(username)).andReturn(userEntity);
        replayAll();
        populator.getGrantedAuthorities(userData, username);
        verifyAll();
    }

    @org.junit.Test
    public void testGetGrantedAuthoritiesWithLoginAlias() throws java.lang.Exception {
        java.lang.String loginAlias = "testLoginAlias@testdomain.com";
        java.lang.String ambariUserName = "user";
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        EasyMock.expect(org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(loginAlias)).andReturn(ambariUserName);
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator populator = createMockBuilder(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class).withConstructor(helper, userDAO, memberDAO, privilegeDAO, users).createMock();
        EasyMock.expect(userEntity.getActive()).andReturn(true);
        EasyMock.expect(users.getUserPrivileges(userEntity)).andReturn(java.util.Collections.singletonList(privilegeEntity));
        EasyMock.expect(userDAO.findUserByName(ambariUserName)).andReturn(userEntity);
        replayAll();
        populator.getGrantedAuthorities(userData, loginAlias);
        org.powermock.api.easymock.PowerMock.verify(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        verifyAll();
    }
}