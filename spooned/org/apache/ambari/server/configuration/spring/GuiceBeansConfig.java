package org.apache.ambari.server.configuration.spring;
import org.springframework.security.crypto.password.PasswordEncoder;
@org.springframework.context.annotation.Configuration
public class GuiceBeansConfig {
    @org.springframework.beans.factory.annotation.Autowired
    private com.google.inject.Injector injector;

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.configuration.Configuration ambariConfig() {
        return injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return injector.getInstance(org.springframework.security.crypto.password.PasswordEncoder.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.audit.AuditLogger auditLogger() {
        return injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authorization.PermissionHelper permissionHelper() {
        return injector.getInstance(org.apache.ambari.server.security.authorization.PermissionHelper.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider ambariLdapAuthenticationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator ambariLdapDataPopulator() {
        return injector.getInstance(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter ambariUserAuthorizationFilter() {
        return injector.getInstance(org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authorization.internal.AmbariInternalAuthenticationProvider ambariInternalAuthenticationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authorization.internal.AmbariInternalAuthenticationProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider ambariJwtAuthenticationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider ambariPamAuthenticationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider ambariLocalAuthenticationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl ambariAuthenticationEventHandler() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.configuration.spring.AgentRegisteringQueueChecker agentRegisteringQueueChecker() {
        return new org.apache.ambari.server.configuration.spring.AgentRegisteringQueueChecker();
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue agentsRegistrationQueue() {
        return new org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue(injector);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfigurationProvider ambariTProxyConfigurationProvider() {
        return injector.getInstance(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfigurationProvider.class);
    }
}