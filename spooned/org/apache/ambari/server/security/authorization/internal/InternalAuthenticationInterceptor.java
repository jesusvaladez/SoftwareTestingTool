package org.apache.ambari.server.security.authorization.internal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class InternalAuthenticationInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    @java.lang.Override
    public java.lang.Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws java.lang.Throwable {
        org.springframework.security.core.Authentication savedAuthContext = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        try {
            org.apache.ambari.server.security.authorization.internal.RunWithInternalSecurityContext securityAuthContextAnnotation = invocation.getMethod().getAnnotation(org.apache.ambari.server.security.authorization.internal.RunWithInternalSecurityContext.class);
            org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken(securityAuthContextAnnotation.token());
            authenticationToken.setAuthenticated(true);
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return invocation.proceed();
        } finally {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(savedAuthContext);
        }
    }
}