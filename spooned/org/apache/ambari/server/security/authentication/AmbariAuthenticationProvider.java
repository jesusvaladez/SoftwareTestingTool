package org.apache.ambari.server.security.authentication;
import org.springframework.security.authentication.AuthenticationProvider;
public abstract class AmbariAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.AmbariAuthenticationProvider.class);

    private final org.apache.ambari.server.security.authorization.Users users;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    protected AmbariAuthenticationProvider(org.apache.ambari.server.security.authorization.Users users, org.apache.ambari.server.configuration.Configuration configuration) {
        this.users = users;
        this.configuration = configuration;
    }

    protected org.apache.ambari.server.orm.entities.UserAuthenticationEntity getAuthenticationEntity(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType type) {
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = (userEntity == null) ? null : userEntity.getAuthenticationEntities();
        if (authenticationEntities != null) {
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                if (authenticationEntity.getAuthenticationType() == type) {
                    return authenticationEntity;
                }
            }
        }
        return null;
    }

    protected java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getAuthenticationEntities(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType type) {
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> foundAuthenticationEntities = null;
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = (userEntity == null) ? null : userEntity.getAuthenticationEntities();
        if (authenticationEntities != null) {
            foundAuthenticationEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                if (authenticationEntity.getAuthenticationType() == type) {
                    foundAuthenticationEntities.add(authenticationEntity);
                }
            }
        }
        return foundAuthenticationEntities;
    }

    protected java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getAuthenticationEntities(org.apache.ambari.server.security.authorization.UserAuthenticationType type, java.lang.String key) {
        return users.getUserAuthenticationEntities(type, key);
    }

    protected org.apache.ambari.server.security.authorization.Users getUsers() {
        return users;
    }

    protected org.apache.ambari.server.configuration.Configuration getConfiguration() {
        return configuration;
    }
}