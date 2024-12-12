package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "\"groups\"", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "group_name", "ldap_group" }) })
@javax.persistence.TableGenerator(name = "group_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "group_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "groupByName", query = "SELECT group_entity FROM GroupEntity group_entity where lower(group_entity.groupName)=:groupname") })
public class GroupEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "group_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "group_id_generator")
    private java.lang.Integer groupId;

    @javax.persistence.Column(name = "group_name")
    private java.lang.String groupName;

    @javax.persistence.Column(name = "ldap_group")
    private java.lang.Integer ldapGroup = 0;

    @javax.persistence.Column(name = "group_type")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Basic
    private org.apache.ambari.server.security.authorization.GroupType groupType = org.apache.ambari.server.security.authorization.GroupType.LOCAL;

    @javax.persistence.OneToMany(mappedBy = "group", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "principal_id", referencedColumnName = "principal_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PrincipalEntity principal;

    public java.lang.Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(java.lang.Integer groupId) {
        this.groupId = groupId;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.lang.Boolean getLdapGroup() {
        return ldapGroup == 0 ? java.lang.Boolean.FALSE : java.lang.Boolean.TRUE;
    }

    private void setLdapGroup(java.lang.Boolean ldapGroup) {
        if (ldapGroup == null) {
            this.ldapGroup = null;
        } else {
            this.ldapGroup = (ldapGroup) ? 1 : 0;
        }
    }

    public org.apache.ambari.server.security.authorization.GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(org.apache.ambari.server.security.authorization.GroupType groupType) {
        this.groupType = groupType;
        setLdapGroup(groupType == org.apache.ambari.server.security.authorization.GroupType.LDAP);
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public void setMemberEntities(java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities) {
        this.memberEntities = memberEntities;
    }

    public org.apache.ambari.server.orm.entities.PrincipalEntity getPrincipal() {
        return principal;
    }

    public void setPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        this.principal = principal;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.GroupEntity that = ((org.apache.ambari.server.orm.entities.GroupEntity) (o));
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null)
            return false;

        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null)
            return false;

        if (ldapGroup != null ? !ldapGroup.equals(that.ldapGroup) : that.ldapGroup != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (groupId != null) ? groupId.hashCode() : 0;
        result = (31 * result) + (groupName != null ? groupName.hashCode() : 0);
        result = (31 * result) + (ldapGroup != null ? ldapGroup.hashCode() : 0);
        return result;
    }
}