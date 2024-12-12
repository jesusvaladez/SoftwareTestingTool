package org.apache.ambari.server.audit.request.creator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
public class AuditEventCreatorTestBase {
    protected static final java.lang.String userName = "testuser";

    @org.junit.BeforeClass
    public static void beforeClass() {
        org.springframework.security.core.context.SecurityContextHolder.setContext(new org.springframework.security.core.context.SecurityContext() {
            @java.lang.Override
            public org.springframework.security.core.Authentication getAuthentication() {
                return new org.springframework.security.core.Authentication() {
                    @java.lang.Override
                    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getCredentials() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getDetails() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getPrincipal() {
                        return new org.springframework.security.core.userdetails.User(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName, "password", java.util.Collections.emptyList());
                    }

                    @java.lang.Override
                    public boolean isAuthenticated() {
                        return true;
                    }

                    @java.lang.Override
                    public void setAuthenticated(boolean b) throws java.lang.IllegalArgumentException {
                    }

                    @java.lang.Override
                    public java.lang.String getName() {
                        return ((org.springframework.security.core.userdetails.User) (getPrincipal())).getUsername();
                    }
                };
            }

            @java.lang.Override
            public void setAuthentication(org.springframework.security.core.Authentication authentication) {
            }
        });
    }

    @org.junit.AfterClass
    public static void afterClass() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}