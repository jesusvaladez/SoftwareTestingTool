package org.apache.ambari.server.security.authentication;
public interface AmbariAuthenticationFilter extends javax.servlet.Filter {
    boolean shouldApply(javax.servlet.http.HttpServletRequest httpServletRequest);

    boolean shouldIncrementFailureCount();
}