package org.apache.ambari.server.security.authorization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@io.swagger.annotations.ApiModel
public class User {
    private final int userId;

    private final java.lang.String userName;

    private final java.util.Date createTime;

    private final boolean active;

    private final java.util.Collection<java.lang.String> groups;

    private final java.util.Collection<org.apache.ambari.server.security.authorization.AuthenticationMethod> authenticationMethods;

    private final boolean admin;

    public User(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        userId = userEntity.getUserId();
        userName = userEntity.getUserName();
        createTime = new java.util.Date(userEntity.getCreateTime());
        active = userEntity.getActive();
        groups = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : userEntity.getMemberEntities()) {
            groups.add(memberEntity.getGroup().getGroupName());
        }
        authenticationMethods = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
            authenticationMethods.add(new org.apache.ambari.server.security.authorization.AuthenticationMethod(authenticationEntity.getAuthenticationType(), authenticationEntity.getAuthenticationKey()));
        }
        boolean admin = false;
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : userEntity.getPrincipal().getPrivileges()) {
            if (privilegeEntity.getPermission().getPermissionName().equals(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION_NAME)) {
                admin = true;
                break;
            }
        }
        this.admin = admin;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public int getUserId() {
        return userId;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/user_name", required = true, access = "public", notes = "username containing only lowercase letters")
    public java.lang.String getUserName() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Date getCreateTime() {
        return createTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/active")
    public boolean isActive() {
        return active;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/admin")
    public boolean isAdmin() {
        return admin;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/groups")
    public java.util.Collection<java.lang.String> getGroups() {
        return groups;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/authentication_methods")
    public java.util.Collection<org.apache.ambari.server.security.authorization.AuthenticationMethod> getAuthenticationMethods() {
        return authenticationMethods;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Users/ldap_user")
    public boolean isLdapUser() {
        for (org.apache.ambari.server.security.authorization.AuthenticationMethod authenticationMethod : authenticationMethods) {
            if (authenticationMethod.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return userName;
    }
}