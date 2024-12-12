package org.apache.ambari.server.configuration.spring;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@org.springframework.context.annotation.Configuration
@org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
@org.springframework.context.annotation.Import(org.apache.ambari.server.configuration.spring.GuiceBeansConfig.class)
@org.springframework.context.annotation.ComponentScan("org.apache.ambari.server.security")
public class ApiSecurityConfig extends org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter {
    private final org.apache.ambari.server.configuration.spring.GuiceBeansConfig guiceBeansConfig;

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.security.AmbariEntryPoint ambariEntryPoint;

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter delegatingAuthenticationFilter;

    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter authorizationFilter;

    public ApiSecurityConfig(org.apache.ambari.server.configuration.spring.GuiceBeansConfig guiceBeansConfig) {
        this.guiceBeansConfig = guiceBeansConfig;
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void configureAuthenticationManager(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder auth, org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider ambariJwtAuthenticationProvider, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider ambariPamAuthenticationProvider, org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider ambariLocalAuthenticationProvider, org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider ambariLdapAuthenticationProvider, org.apache.ambari.server.security.authorization.internal.AmbariInternalAuthenticationProvider ambariInternalAuthenticationProvider, org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider ambariKerberosAuthenticationProvider) {
        auth.authenticationProvider(ambariJwtAuthenticationProvider).authenticationProvider(ambariPamAuthenticationProvider).authenticationProvider(ambariLocalAuthenticationProvider).authenticationProvider(ambariLdapAuthenticationProvider).authenticationProvider(ambariInternalAuthenticationProvider).authenticationProvider(ambariKerberosAuthenticationProvider);
    }

    @java.lang.Override
    @org.springframework.context.annotation.Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManagerBean() throws java.lang.Exception {
        return super.authenticationManagerBean();
    }

    @java.lang.Override
    protected void configure(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws java.lang.Exception {
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().headers().httpStrictTransportSecurity().disable().frameOptions().disable().and().exceptionHandling().authenticationEntryPoint(ambariEntryPoint).and().addFilterBefore(guiceBeansConfig.ambariUserAuthorizationFilter(), org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class).addFilterAt(delegatingAuthenticationFilter, org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class).addFilterBefore(authorizationFilter, org.springframework.security.web.access.intercept.FilterSecurityInterceptor.class);
    }

    @org.springframework.context.annotation.Bean
    public org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider ambariKerberosAuthenticationProvider(org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosTicketValidator ambariKerberosTicketValidator, org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService authToLocalUserDetailsService, org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService proxiedUserDetailsService) {
        return new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider(authToLocalUserDetailsService, proxiedUserDetailsService, ambariKerberosTicketValidator);
    }
}