package org.apache.ambari.server.security;
import org.springframework.security.core.GrantedAuthority;
public interface SecurityHelper {
    java.lang.String getCurrentUserName();

    java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getCurrentAuthorities();
}