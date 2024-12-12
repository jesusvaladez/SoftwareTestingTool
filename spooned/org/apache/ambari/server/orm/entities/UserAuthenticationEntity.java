package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.persistence.Table(name = "user_authentication")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "UserAuthenticationEntity.findAll", query = "SELECT entity FROM UserAuthenticationEntity entity"), @javax.persistence.NamedQuery(name = "UserAuthenticationEntity.findByType", query = "SELECT entity FROM UserAuthenticationEntity entity where lower(entity.authenticationType)=lower(:authenticationType)"), @javax.persistence.NamedQuery(name = "UserAuthenticationEntity.findByTypeAndKey", query = "SELECT entity FROM UserAuthenticationEntity entity where lower(entity.authenticationType)=lower(:authenticationType) and entity.authenticationKey=:authenticationKey"), @javax.persistence.NamedQuery(name = "UserAuthenticationEntity.findByUser", query = "SELECT entity FROM UserAuthenticationEntity entity where entity.user.userId=:userId") })
@javax.persistence.TableGenerator(name = "user_authentication_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "user_authentication_id_seq", initialValue = 2)
public class UserAuthenticationEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "user_authentication_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "user_authentication_id_generator")
    private java.lang.Long userAuthenticationId;

    @javax.persistence.Column(name = "authentication_type", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Basic
    private org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType = org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL;

    @javax.persistence.Column(name = "authentication_key")
    @javax.persistence.Basic
    private java.lang.String authenticationKey;

    @javax.persistence.Column(name = "create_time", nullable = false)
    @javax.persistence.Basic
    private long createTime;

    @javax.persistence.Column(name = "update_time", nullable = false)
    @javax.persistence.Basic
    private long updateTime;

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private org.apache.ambari.server.orm.entities.UserEntity user;

    public java.lang.Long getUserAuthenticationId() {
        return userAuthenticationId;
    }

    public void setUserAuthenticationId(java.lang.Long userAuthenticationId) {
        this.userAuthenticationId = userAuthenticationId;
    }

    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public java.lang.String getAuthenticationKey() {
        int firstCommaIndex = authenticationKey.indexOf(",");
        if (firstCommaIndex != (-1)) {
            return authenticationKey.substring(0, firstCommaIndex);
        }
        return authenticationKey;
    }

    public java.lang.String getFullAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(java.lang.String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public void updateAuthenticationKey(java.lang.String newAuthenticationKey, int historyCount) {
        if ((this.authenticationKey == null) || this.authenticationKey.isEmpty()) {
            this.authenticationKey = newAuthenticationKey;
        } else {
            java.lang.String[] keys = this.authenticationKey.split(",");
            java.util.List<java.lang.String> keyList = new java.util.ArrayList<>(java.util.Arrays.asList(keys));
            if (keyList.size() >= historyCount) {
                keyList = keyList.subList(0, historyCount - 1);
            }
            keyList.add(0, newAuthenticationKey);
            this.authenticationKey = java.lang.String.join(",", keyList);
        }
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public org.apache.ambari.server.orm.entities.UserEntity getUser() {
        return user;
    }

    public void setUser(org.apache.ambari.server.orm.entities.UserEntity user) {
        this.user = user;
    }

    @javax.persistence.PrePersist
    protected void onCreate() {
        final long now = java.lang.System.currentTimeMillis();
        createTime = now;
        updateTime = now;
    }

    @javax.persistence.PreUpdate
    protected void onUpdate() {
        updateTime = java.lang.System.currentTimeMillis();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        } else if ((o == null) || (getClass() != o.getClass())) {
            return false;
        } else {
            org.apache.ambari.server.orm.entities.UserAuthenticationEntity that = ((org.apache.ambari.server.orm.entities.UserAuthenticationEntity) (o));
            org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
            equalsBuilder.append(userAuthenticationId, that.userAuthenticationId);
            equalsBuilder.append(authenticationType, that.authenticationType);
            equalsBuilder.append(authenticationKey, that.authenticationKey);
            equalsBuilder.append(createTime, that.createTime);
            equalsBuilder.append(updateTime, that.updateTime);
            return equalsBuilder.isEquals();
        }
    }

    @java.lang.Override
    public int hashCode() {
        org.apache.commons.lang.builder.HashCodeBuilder hashCodeBuilder = new org.apache.commons.lang.builder.HashCodeBuilder();
        hashCodeBuilder.append(userAuthenticationId);
        hashCodeBuilder.append(authenticationType);
        hashCodeBuilder.append(authenticationKey);
        hashCodeBuilder.append(createTime);
        hashCodeBuilder.append(updateTime);
        return hashCodeBuilder.toHashCode();
    }
}