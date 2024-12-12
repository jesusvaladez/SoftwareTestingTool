package org.apache.ambari.server.security.authorization;
import org.springframework.security.core.GrantedAuthority;
public class AmbariGrantedAuthority implements org.springframework.security.core.GrantedAuthority {
    private final org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity;

    public AmbariGrantedAuthority(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity) {
        this.privilegeEntity = privilegeEntity;
    }

    @java.lang.Override
    public java.lang.String getAuthority() {
        org.apache.ambari.server.orm.entities.ResourceEntity resource = privilegeEntity.getResource();
        java.lang.Long resourceId = resource.getId();
        java.lang.String resourceTypeQualifier = resource.getResourceType().getName().toUpperCase() + ".";
        java.lang.String privilegeName = (privilegeEntity.getPermission().getPermissionName() + "@") + resourceId;
        return privilegeName.startsWith(resourceTypeQualifier) ? privilegeName : resourceTypeQualifier + privilegeName;
    }

    public org.apache.ambari.server.orm.entities.PrivilegeEntity getPrivilegeEntity() {
        return privilegeEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.security.authorization.AmbariGrantedAuthority that = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (o));
        return !(privilegeEntity != null ? !privilegeEntity.equals(that.privilegeEntity) : that.privilegeEntity != null);
    }

    @java.lang.Override
    public int hashCode() {
        return privilegeEntity != null ? privilegeEntity.hashCode() : 0;
    }
}