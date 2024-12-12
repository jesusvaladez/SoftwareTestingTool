package org.apache.ambari.server.security.authentication.jwt;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
@org.springframework.stereotype.Component
@org.springframework.core.annotation.Order(1)
public class AmbariJwtAuthenticationFilter implements org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.class);

    private final org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler;

    private final org.springframework.security.web.AuthenticationEntryPoint ambariEntryPoint;

    private final org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

    private final org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider propertiesProvider;

    AmbariJwtAuthenticationFilter(org.springframework.security.web.AuthenticationEntryPoint ambariEntryPoint, org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider propertiesProvider, org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider authenticationProvider, org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler) {
        if (eventHandler == null) {
            throw new java.lang.IllegalArgumentException("The AmbariAuthenticationEventHandler must not be null");
        }
        this.ambariEntryPoint = ambariEntryPoint;
        this.eventHandler = eventHandler;
        this.propertiesProvider = propertiesProvider;
        this.authenticationProvider = authenticationProvider;
    }

    @java.lang.Override
    public boolean shouldApply(javax.servlet.http.HttpServletRequest httpServletRequest) {
        boolean shouldApply = false;
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = propertiesProvider.get();
        if ((jwtProperties != null) && jwtProperties.isEnabledForAmbari()) {
            java.lang.String serializedJWT = getJWTFromCookie(httpServletRequest);
            shouldApply = (serializedJWT != null) && isAuthenticationRequired(serializedJWT);
        }
        return shouldApply;
    }

    @java.lang.Override
    public boolean shouldIncrementFailureCount() {
        return false;
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        eventHandler.beforeAttemptAuthentication(this, servletRequest, servletResponse);
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = propertiesProvider.get();
        if ((jwtProperties == null) || (!jwtProperties.isEnabledForAmbari())) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        javax.servlet.http.HttpServletRequest httpServletRequest = ((javax.servlet.http.HttpServletRequest) (servletRequest));
        javax.servlet.http.HttpServletResponse httpServletResponse = ((javax.servlet.http.HttpServletResponse) (servletResponse));
        try {
            java.lang.String serializedJWT = getJWTFromCookie(httpServletRequest);
            if ((serializedJWT != null) && isAuthenticationRequired(serializedJWT)) {
                try {
                    com.nimbusds.jwt.SignedJWT jwtToken = com.nimbusds.jwt.SignedJWT.parse(serializedJWT);
                    boolean valid = validateToken(jwtToken);
                    if (valid) {
                        java.lang.String userName = jwtToken.getJWTClaimsSet().getSubject();
                        org.springframework.security.core.Authentication authentication = authenticationProvider.authenticate(new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationToken(userName, serializedJWT, null));
                        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                        eventHandler.onSuccessfulAuthentication(this, httpServletRequest, httpServletResponse, authentication);
                    } else {
                        throw new org.springframework.security.authentication.BadCredentialsException("Invalid JWT token");
                    }
                } catch (java.text.ParseException e) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("Unable to parse the JWT token", e);
                    throw new org.springframework.security.authentication.BadCredentialsException("Unable to parse the JWT token - " + e.getLocalizedMessage());
                }
            } else {
                org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.trace("No JWT cookie found, do nothing");
            }
            chain.doFilter(servletRequest, servletResponse);
        } catch (org.springframework.security.core.AuthenticationException e) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT authentication failed - {}", e.getLocalizedMessage());
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
            org.apache.ambari.server.security.authentication.AmbariAuthenticationException cause;
            if (e instanceof org.apache.ambari.server.security.authentication.AmbariAuthenticationException) {
                cause = ((org.apache.ambari.server.security.authentication.AmbariAuthenticationException) (e));
            } else {
                cause = new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(null, e.getMessage(), false, e);
            }
            eventHandler.onUnsuccessfulAuthentication(this, httpServletRequest, httpServletResponse, cause);
            ambariEntryPoint.commence(httpServletRequest, httpServletResponse, e);
        }
    }

    @java.lang.Override
    public void destroy() {
    }

    private boolean isAuthenticationRequired(java.lang.String token) {
        org.springframework.security.core.Authentication existingAuth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if ((existingAuth == null) || (!existingAuth.isAuthenticated())) {
            return true;
        }
        if ((existingAuth instanceof org.apache.ambari.server.security.authentication.AmbariUserAuthentication) && (!org.apache.commons.lang.StringUtils.equals(token, ((java.lang.String) (existingAuth.getCredentials()))))) {
            return true;
        }
        return existingAuth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken;
    }

    java.lang.String getJWTFromCookie(javax.servlet.http.HttpServletRequest req) {
        java.lang.String serializedJWT = null;
        javax.servlet.http.Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = propertiesProvider.get();
            java.lang.String jwtCookieName = (jwtProperties == null) ? null : jwtProperties.getCookieName();
            if (org.apache.commons.lang.StringUtils.isEmpty(jwtCookieName)) {
                jwtCookieName = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_COOKIE_NAME.getDefaultValue();
            }
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (jwtCookieName.equals(cookie.getName())) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.info("{} cookie has been found and is being processed", jwtCookieName);
                    serializedJWT = cookie.getValue();
                    break;
                }
            }
        }
        return serializedJWT;
    }

    private boolean validateToken(com.nimbusds.jwt.SignedJWT jwtToken) {
        boolean sigValid = validateSignature(jwtToken);
        if (!sigValid) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("Signature could not be verified");
        }
        boolean audValid = validateAudiences(jwtToken);
        if (!audValid) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("Audience validation failed.");
        }
        boolean expValid = validateExpiration(jwtToken);
        if (!expValid) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.info("Expiration validation failed.");
        }
        return (sigValid && audValid) && expValid;
    }

    boolean validateSignature(com.nimbusds.jwt.SignedJWT jwtToken) {
        boolean valid = false;
        if (JWSObject.State.SIGNED == jwtToken.getState()) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.debug("JWT token is in a SIGNED state");
            if (jwtToken.getSignature() != null) {
                org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.debug("JWT token signature is not null");
                org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = propertiesProvider.get();
                java.security.interfaces.RSAPublicKey publicKey = (jwtProperties == null) ? null : jwtProperties.getPublicKey();
                if (publicKey == null) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("SSO server public key has not be set, validation of the JWT token cannot be performed.");
                } else {
                    try {
                        com.nimbusds.jose.JWSVerifier verifier = new com.nimbusds.jose.crypto.RSASSAVerifier(publicKey);
                        if (jwtToken.verify(verifier)) {
                            valid = true;
                            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.debug("JWT token has been successfully verified");
                        } else {
                            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT signature verification failed.");
                        }
                    } catch (com.nimbusds.jose.JOSEException je) {
                        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("Error while validating signature", je);
                    }
                }
            }
        }
        return valid;
    }

    boolean validateAudiences(com.nimbusds.jwt.SignedJWT jwtToken) {
        boolean valid = false;
        try {
            java.util.List<java.lang.String> tokenAudienceList = jwtToken.getJWTClaimsSet().getAudience();
            org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = propertiesProvider.get();
            java.util.List<java.lang.String> audiences = (jwtProperties == null) ? null : jwtProperties.getAudiences();
            if (audiences == null) {
                valid = true;
            } else {
                if (tokenAudienceList == null) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT token has no audiences, validation failed.");
                    return false;
                }
                org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.info("Audience List: {}", audiences);
                for (java.lang.String aud : tokenAudienceList) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.info("Found audience: {}", aud);
                    if (audiences.contains(aud)) {
                        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.debug("JWT token audience has been successfully validated");
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT audience validation failed.");
                }
            }
        } catch (java.text.ParseException pe) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("Unable to parse the JWT token.", pe);
        }
        return valid;
    }

    boolean validateExpiration(com.nimbusds.jwt.SignedJWT jwtToken) {
        boolean valid = false;
        try {
            java.util.Date expires = jwtToken.getJWTClaimsSet().getExpirationTime();
            if ((expires == null) || new java.util.Date().before(expires)) {
                org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.debug("JWT token expiration date has been successfully validated");
                valid = true;
            } else {
                org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT expiration date validation failed.");
            }
        } catch (java.text.ParseException pe) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter.LOG.warn("JWT expiration date validation failed.", pe);
        }
        return valid;
    }
}