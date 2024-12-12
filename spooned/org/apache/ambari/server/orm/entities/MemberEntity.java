package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "members", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "group_id", "user_id" }) })
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "memberByUserAndGroup", query = "SELECT memberEnt FROM MemberEntity memberEnt where lower(memberEnt.user.userName)=:username AND lower(memberEnt.group.groupName)=:groupname") })
@javax.persistence.TableGenerator(name = "member_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "member_id_seq", initialValue = 1, allocationSize = 500)
public class MemberEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "member_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "member_id_generator")
    private java.lang.Integer memberId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "group_id")
    private org.apache.ambari.server.orm.entities.GroupEntity group;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "user_id")
    private org.apache.ambari.server.orm.entities.UserEntity user;

    public java.lang.Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(java.lang.Integer memberId) {
        this.memberId = memberId;
    }

    public org.apache.ambari.server.orm.entities.GroupEntity getGroup() {
        return group;
    }

    public void setGroup(org.apache.ambari.server.orm.entities.GroupEntity group) {
        this.group = group;
    }

    public org.apache.ambari.server.orm.entities.UserEntity getUser() {
        return user;
    }

    public void setUser(org.apache.ambari.server.orm.entities.UserEntity user) {
        this.user = user;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.MemberEntity that = ((org.apache.ambari.server.orm.entities.MemberEntity) (o));
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null)
            return false;

        if (group != null ? !group.equals(that.group) : that.group != null)
            return false;

        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (memberId != null) ? memberId.hashCode() : 0;
        result = (31 * result) + (group != null ? group.hashCode() : 0);
        result = (31 * result) + (user != null ? user.hashCode() : 0);
        return result;
    }
}