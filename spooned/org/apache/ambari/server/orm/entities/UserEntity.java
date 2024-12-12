package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@javax.persistence.Table(name = "users", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "user_name" }) })
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "userByName", query = "SELECT user_entity from UserEntity user_entity " + "where lower(user_entity.userName)=lower(:username)") })
@javax.persistence.TableGenerator(name = "user_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "user_id_seq", initialValue = 2, allocationSize = 500)
public class UserEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "user_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "user_id_generator")
    private java.lang.Integer userId;

    @javax.persistence.Column(name = "user_name", nullable = false)
    private java.lang.String userName;

    @javax.persistence.Column(name = "create_time", nullable = false)
    @javax.persistence.Basic
    private long createTime;

    @javax.persistence.Column(name = "active", nullable = false)
    private java.lang.Integer active = 1;

    @javax.persistence.Column(name = "consecutive_failures", nullable = false)
    private java.lang.Integer consecutiveFailures = 0;

    @javax.persistence.Column(name = "display_name")
    private java.lang.String displayName;

    @javax.persistence.Column(name = "local_username")
    private java.lang.String localUsername;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version")
    private java.lang.Long version;

    @javax.persistence.OneToMany(mappedBy = "user", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = new java.util.HashSet<>();

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "principal_id", referencedColumnName = "principal_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PrincipalEntity principal;

    @javax.persistence.Column(name = "active_widget_layouts")
    private java.lang.String activeWidgetLayouts;

    @javax.persistence.OneToMany(mappedBy = "user", cascade = javax.persistence.CascadeType.ALL, fetch = javax.persistence.FetchType.LAZY)
    private java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = new java.util.ArrayList<>();

    public java.lang.Integer getUserId() {
        return userId;
    }

    public void setUserId(java.lang.Integer userId) {
        this.userId = userId;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = (userName == null) ? null : userName.toLowerCase();
    }

    public java.lang.Integer getConsecutiveFailures() {
        return consecutiveFailures;
    }

    public void setConsecutiveFailures(java.lang.Integer consecutiveFailures) {
        this.consecutiveFailures = consecutiveFailures;
    }

    public void incrementConsecutiveFailures() {
        this.consecutiveFailures++;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(java.lang.String localUsername) {
        this.localUsername = localUsername;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public void setMemberEntities(java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities) {
        this.memberEntities = memberEntities;
    }

    public java.lang.Boolean getActive() {
        return active == 0 ? java.lang.Boolean.FALSE : java.lang.Boolean.TRUE;
    }

    public void setActive(java.lang.Boolean active) {
        if (active == null) {
            this.active = null;
        } else {
            this.active = (active) ? 1 : 0;
        }
    }

    public org.apache.ambari.server.orm.entities.PrincipalEntity getPrincipal() {
        return principal;
    }

    public void setPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        this.principal = principal;
    }

    public java.lang.String getActiveWidgetLayouts() {
        return activeWidgetLayouts;
    }

    public void setActiveWidgetLayouts(java.lang.String activeWidgetLayouts) {
        this.activeWidgetLayouts = activeWidgetLayouts;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getAuthenticationEntities() {
        return authenticationEntities;
    }

    public void setAuthenticationEntities(java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities) {
        if (this.authenticationEntities != authenticationEntities) {
            this.authenticationEntities.clear();
            if (authenticationEntities != null) {
                this.authenticationEntities.addAll(authenticationEntities);
            }
        }
    }

    @javax.persistence.PrePersist
    protected void onCreate() {
        createTime = java.lang.System.currentTimeMillis();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        } else if ((o == null) || (getClass() != o.getClass())) {
            return false;
        } else {
            org.apache.ambari.server.orm.entities.UserEntity that = ((org.apache.ambari.server.orm.entities.UserEntity) (o));
            org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
            equalsBuilder.append(userId, that.userId);
            equalsBuilder.append(userName, that.userName);
            equalsBuilder.append(displayName, that.displayName);
            equalsBuilder.append(localUsername, that.localUsername);
            equalsBuilder.append(consecutiveFailures, that.consecutiveFailures);
            equalsBuilder.append(active, that.active);
            equalsBuilder.append(createTime, that.createTime);
            equalsBuilder.append(version, that.version);
            return equalsBuilder.isEquals();
        }
    }

    @java.lang.Override
    public int hashCode() {
        org.apache.commons.lang.builder.HashCodeBuilder hashCodeBuilder = new org.apache.commons.lang.builder.HashCodeBuilder();
        hashCodeBuilder.append(userId);
        hashCodeBuilder.append(userName);
        hashCodeBuilder.append(displayName);
        hashCodeBuilder.append(localUsername);
        hashCodeBuilder.append(consecutiveFailures);
        hashCodeBuilder.append(active);
        hashCodeBuilder.append(createTime);
        hashCodeBuilder.append(version);
        return hashCodeBuilder.toHashCode();
    }
}