package org.apache.ambari.server.security.authorization;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
public class AmbariLdapAuthoritiesPopulator implements org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class);

    private org.apache.ambari.server.security.authorization.AuthorizationHelper authorizationHelper;

    org.apache.ambari.server.orm.dao.UserDAO userDAO;

    org.apache.ambari.server.orm.dao.MemberDAO memberDAO;

    org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO;

    org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.Inject
    public AmbariLdapAuthoritiesPopulator(org.apache.ambari.server.security.authorization.AuthorizationHelper authorizationHelper, org.apache.ambari.server.orm.dao.UserDAO userDAO, org.apache.ambari.server.orm.dao.MemberDAO memberDAO, org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO, org.apache.ambari.server.security.authorization.Users users) {
        this.authorizationHelper = authorizationHelper;
        this.userDAO = userDAO;
        this.memberDAO = memberDAO;
        this.privilegeDAO = privilegeDAO;
        this.users = users;
    }

    @java.lang.Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getGrantedAuthorities(org.springframework.ldap.core.DirContextOperations userData, java.lang.String username) {
        username = org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(username);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.log.info(("Get authorities for user " + username) + " from local DB");
        org.apache.ambari.server.orm.entities.UserEntity user;
        user = userDAO.findUserByName(username);
        if (user == null) {
            org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.log.error(("Can't get authorities for user " + username) + ", he is not present in local DB");
            return java.util.Collections.emptyList();
        }
        if (!user.getActive()) {
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username);
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = users.getUserPrivileges(user);
        return authorizationHelper.convertPrivilegesToAuthorities(privilegeEntities);
    }
}