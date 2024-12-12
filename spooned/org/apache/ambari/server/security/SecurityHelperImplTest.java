package org.apache.ambari.server.security;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
public class SecurityHelperImplTest {
    private final java.lang.String USER_FROM_PRINCIPAL = "user from principal";

    private final java.lang.String USER_DETAILS_USER_NAME = "user details user name";

    @org.junit.Test
    public void testSecurityHelperWithUser() {
        org.springframework.security.core.context.SecurityContext ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setPrincipal(new org.apache.ambari.server.orm.entities.PrincipalEntity());
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString("userName").toString());
        userEntity.setUserId(1);
        org.apache.ambari.server.security.authorization.User user = new org.apache.ambari.server.security.authorization.User(userEntity);
        org.springframework.security.core.Authentication auth = new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(null, new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(user, null, null));
        ctx.setAuthentication(auth);
        org.junit.Assert.assertEquals("username", org.apache.ambari.server.security.SecurityHelperImpl.getInstance().getCurrentUserName());
    }

    @org.junit.Test
    public void testSecurityHelperWithUserDetails() {
        org.springframework.security.core.context.SecurityContext ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.apache.ambari.server.security.SecurityHelperImplTest.TestUserDetails userDetails = new org.apache.ambari.server.security.SecurityHelperImplTest.TestUserDetails();
        org.springframework.security.core.Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null);
        ctx.setAuthentication(auth);
        org.junit.Assert.assertEquals(USER_DETAILS_USER_NAME, org.apache.ambari.server.security.SecurityHelperImpl.getInstance().getCurrentUserName());
    }

    @org.junit.Test
    public void testSecurityHelperWithUnknownPrincipal() {
        org.springframework.security.core.context.SecurityContext ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(new org.apache.ambari.server.security.SecurityHelperImplTest.TestPrincipal(), null);
        ctx.setAuthentication(auth);
        org.junit.Assert.assertEquals(USER_FROM_PRINCIPAL, org.apache.ambari.server.security.SecurityHelperImpl.getInstance().getCurrentUserName());
    }

    class TestUserDetails implements org.springframework.security.core.userdetails.UserDetails {
        @java.lang.Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return null;
        }

        @java.lang.Override
        public java.lang.String getPassword() {
            return null;
        }

        @java.lang.Override
        public java.lang.String getUsername() {
            return USER_DETAILS_USER_NAME;
        }

        @java.lang.Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @java.lang.Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @java.lang.Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @java.lang.Override
        public boolean isEnabled() {
            return false;
        }
    }

    class TestPrincipal {
        @java.lang.Override
        public java.lang.String toString() {
            return USER_FROM_PRINCIPAL;
        }
    }
}