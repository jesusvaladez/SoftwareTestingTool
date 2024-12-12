package org.apache.ambari.server.security.authentication.jwt;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.StringUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.verify;
public class AmbariJwtAuthenticationFilterTest extends org.easymock.EasyMockSupport {
    private static java.security.interfaces.RSAPublicKey publicKey;

    private static java.security.interfaces.RSAPrivateKey privateKey;

    private static java.security.interfaces.RSAPrivateKey invalidPrivateKey;

    @org.junit.BeforeClass
    public static void generateKeyPair() throws java.security.NoSuchAlgorithmException {
        java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        java.security.KeyPair keyPair = keyPairGenerator.generateKeyPair();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.publicKey = ((java.security.interfaces.RSAPublicKey) (keyPair.getPublic()));
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.privateKey = ((java.security.interfaces.RSAPrivateKey) (keyPair.getPrivate()));
        keyPair = keyPairGenerator.generateKeyPair();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.invalidPrivateKey = ((java.security.interfaces.RSAPrivateKey) (keyPair.getPrivate()));
    }

    @org.junit.Before
    public void setup() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    private org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties createTestProperties() {
        return createTestProperties(java.util.Collections.singletonList("test-audience"));
    }

    private org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties createTestProperties(java.util.List<java.lang.String> audiences) {
        final java.util.Map<java.lang.String, java.lang.String> configurationMap = new java.util.HashMap<>();
        configurationMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_COOKIE_NAME.key(), "non-default");
        configurationMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES.key(), (audiences == null) || audiences.isEmpty() ? "" : org.apache.commons.lang.StringUtils.join(audiences, ","));
        configurationMap.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_AUTHENTICATION_ENABLED.key(), "true");
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties properties = new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties(configurationMap);
        properties.setPublicKey(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.publicKey);
        return properties;
    }

    private com.nimbusds.jwt.SignedJWT getSignedToken() throws com.nimbusds.jose.JOSEException {
        return getSignedToken("test-audience");
    }

    private com.nimbusds.jwt.SignedJWT getSignedToken(java.lang.String audience) throws com.nimbusds.jose.JOSEException {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
        calendar.add(java.util.Calendar.DATE, 1);
        return getSignedToken(calendar.getTime(), audience);
    }

    private com.nimbusds.jwt.SignedJWT getSignedToken(java.util.Date expirationTime, java.lang.String audience) throws com.nimbusds.jose.JOSEException {
        com.nimbusds.jose.crypto.RSASSASigner signer = new com.nimbusds.jose.crypto.RSASSASigner(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.privateKey);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
        com.nimbusds.jwt.JWTClaimsSet claimsSet = new com.nimbusds.jwt.JWTClaimsSet.Builder().subject("test-user").issuer("unit-test").issueTime(calendar.getTime()).expirationTime(expirationTime).audience(audience).build();
        com.nimbusds.jwt.SignedJWT signedJWT = new com.nimbusds.jwt.SignedJWT(new com.nimbusds.jose.JWSHeader(com.nimbusds.jose.JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }

    private com.nimbusds.jwt.SignedJWT getInvalidToken() throws com.nimbusds.jose.JOSEException {
        com.nimbusds.jose.crypto.RSASSASigner signer = new com.nimbusds.jose.crypto.RSASSASigner(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilterTest.invalidPrivateKey);
        java.util.Calendar issueTime = java.util.Calendar.getInstance();
        issueTime.setTimeInMillis(java.lang.System.currentTimeMillis());
        issueTime.add(java.util.Calendar.DATE, -2);
        java.util.Calendar expirationTime = java.util.Calendar.getInstance();
        issueTime.setTimeInMillis(java.lang.System.currentTimeMillis());
        expirationTime.add(java.util.Calendar.DATE, -1);
        com.nimbusds.jwt.JWTClaimsSet claimsSet = new com.nimbusds.jwt.JWTClaimsSet.Builder().subject("test-user").issuer("unit-test").issueTime(issueTime.getTime()).expirationTime(issueTime.getTime()).audience("test-audience-invalid").build();
        com.nimbusds.jwt.SignedJWT signedJWT = new com.nimbusds.jwt.SignedJWT(new com.nimbusds.jose.JWSHeader(com.nimbusds.jose.JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }

    @org.junit.Test
    public void testGetJWTFromCookie() throws java.lang.Exception {
        javax.servlet.http.HttpServletRequest request = createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.Cookie cookie = createNiceMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("non-default");
        EasyMock.expect(cookie.getValue()).andReturn("stubtokenstring");
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie });
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        java.lang.String jwtFromCookie = filter.getJWTFromCookie(request);
        verifyAll();
        org.junit.Assert.assertEquals("stubtokenstring", jwtFromCookie);
    }

    @org.junit.Test
    public void testValidateSignature() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.validateSignature(getSignedToken()));
        org.junit.Assert.assertFalse(filter.validateSignature(getInvalidToken()));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateAudiences() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.validateAudiences(getSignedToken()));
        org.junit.Assert.assertFalse(filter.validateAudiences(getInvalidToken()));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateNullAudiences() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties(null)).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.validateAudiences(getSignedToken()));
        org.junit.Assert.assertTrue(filter.validateAudiences(getInvalidToken()));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateTokenWithoutAudiences() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertFalse(filter.validateAudiences(getSignedToken(null)));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateExpiration() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.validateExpiration(getSignedToken()));
        org.junit.Assert.assertFalse(filter.validateExpiration(getInvalidToken()));
        verifyAll();
    }

    @org.junit.Test
    public void testValidateNoExpiration() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.validateExpiration(getSignedToken(null, "test-audience")));
        org.junit.Assert.assertFalse(filter.validateExpiration(getInvalidToken()));
        verifyAll();
    }

    @org.junit.Test
    public void testShouldApplyTrue() throws com.nimbusds.jose.JOSEException {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        com.nimbusds.jwt.SignedJWT token = getInvalidToken();
        javax.servlet.http.Cookie cookie = createMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("non-default").atLeastOnce();
        EasyMock.expect(cookie.getValue()).andReturn(token.serialize()).atLeastOnce();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie });
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.shouldApply(request));
        verifyAll();
    }

    @org.junit.Test
    public void testShouldApplyTrueBadToken() throws com.nimbusds.jose.JOSEException {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        javax.servlet.http.Cookie cookie = createMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("non-default").atLeastOnce();
        EasyMock.expect(cookie.getValue()).andReturn("bad token").atLeastOnce();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie });
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertTrue(filter.shouldApply(request));
        verifyAll();
    }

    @org.junit.Test
    public void testShouldApplyFalseMissingCookie() throws com.nimbusds.jose.JOSEException {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        javax.servlet.http.Cookie cookie = createMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("some-other-cookie").atLeastOnce();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie });
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertFalse(filter.shouldApply(request));
        verifyAll();
    }

    @org.junit.Test
    public void testShouldApplyFalseNotEnabled() throws com.nimbusds.jose.JOSEException {
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(null).anyTimes();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(null, jwtAuthenticationPropertiesProvider, null, eventHandler);
        org.junit.Assert.assertFalse(filter.shouldApply(request));
        EasyMock.verify(request);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void ensureNonNullEventHandler() {
        new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(createNiceMock(org.apache.ambari.server.security.AmbariEntryPoint.class), createNiceMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class), createNiceMock(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.class), null);
    }

    @org.junit.Test
    public void testDoFilterSuccessful() throws java.lang.Exception {
        org.easymock.Capture<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> captureFilter = EasyMock.newCapture(CaptureType.ALL);
        com.nimbusds.jwt.SignedJWT token = getSignedToken();
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        org.apache.ambari.server.configuration.Configuration configuration = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMaxAuthenticationFailures()).andReturn(10).anyTimes();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        javax.servlet.http.Cookie cookie = createMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("non-default").once();
        EasyMock.expect(cookie.getValue()).andReturn(token.serialize()).once();
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie }).once();
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = createMock(org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        EasyMock.expect(userAuthenticationEntity.getAuthenticationType()).andReturn(org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT).anyTimes();
        EasyMock.expect(userAuthenticationEntity.getAuthenticationKey()).andReturn("").anyTimes();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getAuthenticationEntities()).andReturn(java.util.Collections.singletonList(userAuthenticationEntity)).atLeastOnce();
        org.apache.ambari.server.security.authorization.User user = createMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity("test-user")).andReturn(userEntity).once();
        EasyMock.expect(users.getUser(userEntity)).andReturn(user).once();
        EasyMock.expect(user.getUserName()).andReturn("test-user").atLeastOnce();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(java.util.Collections.emptyList()).once();
        users.validateLogin(userEntity, "test-user");
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        eventHandler.beforeAttemptAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response));
        EasyMock.expectLastCall().once();
        eventHandler.onSuccessfulAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.springframework.security.core.Authentication.class));
        EasyMock.expectLastCall().once();
        filterChain.doFilter(request, response);
        EasyMock.expectLastCall().once();
        org.springframework.security.web.AuthenticationEntryPoint entryPoint = createNiceMock(org.apache.ambari.server.security.AmbariEntryPoint.class);
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider provider = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider(users, configuration);
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(entryPoint, jwtAuthenticationPropertiesProvider, provider, eventHandler);
        filter.doFilter(request, response, filterChain);
        verifyAll();
        java.util.List<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> capturedFilters = captureFilter.getValues();
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter capturedFiltered : capturedFilters) {
            org.junit.Assert.assertSame(filter, capturedFiltered);
        }
    }

    @org.junit.Test
    public void testDoFilterUnsuccessful() throws java.lang.Exception {
        org.easymock.Capture<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> captureFilter = EasyMock.newCapture(CaptureType.ALL);
        com.nimbusds.jwt.SignedJWT token = getSignedToken();
        org.apache.ambari.server.configuration.Configuration configuration = createMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider = createMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(jwtAuthenticationPropertiesProvider.get()).andReturn(createTestProperties()).anyTimes();
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        javax.servlet.http.Cookie cookie = createMock(javax.servlet.http.Cookie.class);
        EasyMock.expect(cookie.getName()).andReturn("non-default").once();
        EasyMock.expect(cookie.getValue()).andReturn(token.serialize()).once();
        EasyMock.expect(request.getCookies()).andReturn(new javax.servlet.http.Cookie[]{ cookie }).once();
        org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity("test-user")).andReturn(null).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler = createNiceMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        eventHandler.beforeAttemptAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response));
        EasyMock.expectLastCall().once();
        eventHandler.onUnsuccessfulAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class));
        EasyMock.expectLastCall().once();
        org.springframework.security.web.AuthenticationEntryPoint entryPoint = createNiceMock(org.apache.ambari.server.security.AmbariEntryPoint.class);
        entryPoint.commence(EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class));
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider provider = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider(users, configuration);
        org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationFilter(entryPoint, jwtAuthenticationPropertiesProvider, provider, eventHandler);
        filter.doFilter(request, response, filterChain);
        verifyAll();
        java.util.List<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> capturedFilters = captureFilter.getValues();
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter capturedFiltered : capturedFilters) {
            org.junit.Assert.assertSame(filter, capturedFiltered);
        }
    }
}