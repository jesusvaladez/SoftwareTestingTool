package org.apache.ambari.server.security.authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
public class AmbariLocalAuthenticationProviderTest extends org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest {
    private static final java.lang.String TEST_USER_PASS = "userPass";

    private static final java.lang.String TEST_USER_INCORRECT_PASS = "userIncorrectPass";

    @java.lang.Override
    protected org.springframework.security.authentication.AuthenticationProvider getAuthenticationProvider(com.google.inject.Injector injector) {
        return injector.getInstance(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.class);
    }

    @java.lang.Override
    protected org.springframework.security.core.Authentication getAuthentication(boolean correctUsername, boolean correctCredential) {
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(correctUsername ? org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME : "incorrect_username", correctCredential ? org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProviderTest.TEST_USER_PASS : org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProviderTest.TEST_USER_INCORRECT_PASS);
    }

    @java.lang.Override
    protected org.apache.ambari.server.orm.entities.UserEntity getUserEntity(com.google.inject.Injector injector, java.lang.String username, int consecutiveFailures, boolean active) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
        userAuthenticationEntity.setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        userAuthenticationEntity.setAuthenticationKey(injector.getInstance(org.springframework.security.crypto.password.PasswordEncoder.class).encode(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProviderTest.TEST_USER_PASS));
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(1);
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(username).toString());
        userEntity.setPrincipal(principalEntity);
        userEntity.setAuthenticationEntities(java.util.Collections.singletonList(userAuthenticationEntity));
        userEntity.setConsecutiveFailures(consecutiveFailures);
        userEntity.setActive(active);
        return userEntity;
    }

    @java.lang.Override
    protected com.google.inject.Module getAdditionalModule() {
        return new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
            }
        };
    }

    @java.lang.Override
    protected void validateAuthenticationResult(org.apache.ambari.server.security.authentication.AmbariUserAuthentication result) {
        org.junit.Assert.assertEquals(((java.lang.Integer) (1)), result.getUserId());
        org.junit.Assert.assertEquals(((java.lang.Integer) (1)), result.getPrincipal().getUserId());
    }
}