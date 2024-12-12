package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminpermission")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "permission_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "permission_id_seq", initialValue = 100)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "PermissionEntity.findByName", query = "SELECT p FROM PermissionEntity p WHERE p.permissionName = :permissionName"), @javax.persistence.NamedQuery(name = "PermissionEntity.findByPrincipals", query = "SELECT p FROM PermissionEntity p WHERE p.principal IN :principalList") })
public class PermissionEntity {
    public static final int AMBARI_ADMINISTRATOR_PERMISSION = 1;

    public static final int CLUSTER_USER_PERMISSION = 2;

    public static final int CLUSTER_ADMINISTRATOR_PERMISSION = 3;

    public static final int VIEW_USER_PERMISSION = 4;

    public static final java.lang.String AMBARI_ADMINISTRATOR_PERMISSION_NAME = "AMBARI.ADMINISTRATOR";

    public static final java.lang.String CLUSTER_ADMINISTRATOR_PERMISSION_NAME = "CLUSTER.ADMINISTRATOR";

    public static final java.lang.String CLUSTER_OPERATOR_PERMISSION_NAME = "CLUSTER.OPERATOR";

    public static final java.lang.String SERVICE_ADMINISTRATOR_PERMISSION_NAME = "SERVICE.ADMINISTRATOR";

    public static final java.lang.String SERVICE_OPERATOR_PERMISSION_NAME = "SERVICE.OPERATOR";

    public static final java.lang.String CLUSTER_USER_PERMISSION_NAME = "CLUSTER.USER";

    public static final java.lang.String VIEW_USER_PERMISSION_NAME = "VIEW.USER";

    @javax.persistence.Id
    @javax.persistence.Column(name = "permission_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "permission_id_generator")
    private java.lang.Integer id;

    @javax.persistence.Column(name = "permission_name")
    private java.lang.String permissionName;

    @javax.persistence.Column(name = "permission_label")
    private java.lang.String permissionLabel;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "principal_id", referencedColumnName = "principal_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PrincipalEntity principal;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_type_id", referencedColumnName = "resource_type_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType;

    @javax.persistence.ManyToMany
    @javax.persistence.JoinTable(name = "permission_roleauthorization", joinColumns = { @javax.persistence.JoinColumn(name = "permission_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "authorization_id") })
    private java.util.Set<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizations = new java.util.LinkedHashSet<>();

    @javax.persistence.Column(name = "sort_order", nullable = false)
    private java.lang.Integer sortOrder = 1;

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(java.lang.String permissionName) {
        this.permissionName = permissionName;
    }

    public java.lang.String getPermissionLabel() {
        return permissionLabel;
    }

    public void setPermissionLabel(java.lang.String permissionLabel) {
        this.permissionLabel = permissionLabel;
    }

    public org.apache.ambari.server.orm.entities.PrincipalEntity getPrincipal() {
        return principal;
    }

    public void setPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        this.principal = principal;
    }

    public org.apache.ambari.server.orm.entities.ResourceTypeEntity getResourceType() {
        return resourceType;
    }

    public void setResourceType(org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType) {
        this.resourceType = resourceType;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> getAuthorizations() {
        return authorizations;
    }

    public void addAuthorization(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorization) {
        authorizations.add(roleAuthorization);
    }

    public void addAuthorizations(java.util.Collection<org.apache.ambari.server.security.authorization.RoleAuthorization> roleAuthorizations) {
        for (org.apache.ambari.server.security.authorization.RoleAuthorization roleAuthorization : roleAuthorizations) {
            addAuthorization(org.apache.ambari.server.orm.entities.PermissionEntity.createRoleAuthorizationEntity(roleAuthorization));
        }
    }

    private static org.apache.ambari.server.orm.entities.RoleAuthorizationEntity createRoleAuthorizationEntity(org.apache.ambari.server.security.authorization.RoleAuthorization authorization) {
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        roleAuthorizationEntity.setAuthorizationId(authorization.getId());
        roleAuthorizationEntity.setAuthorizationName(authorization.name());
        return roleAuthorizationEntity;
    }

    public java.lang.Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(java.lang.Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.PermissionEntity that = ((org.apache.ambari.server.orm.entities.PermissionEntity) (o));
        return (((((!(id != null ? !id.equals(that.id) : that.id != null)) && (!(permissionName != null ? !permissionName.equals(that.permissionName) : that.permissionName != null))) && (!(permissionLabel != null ? !permissionLabel.equals(that.permissionLabel) : that.permissionLabel != null))) && (!(resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null))) && (!(sortOrder != null ? !sortOrder.equals(that.sortOrder) : that.sortOrder != null))) && (!(authorizations != null ? !authorizations.equals(that.authorizations) : that.authorizations != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (permissionName != null ? permissionName.hashCode() : 0);
        result = (31 * result) + (permissionLabel != null ? permissionLabel.hashCode() : 0);
        result = (31 * result) + (resourceType != null ? resourceType.hashCode() : 0);
        result = (31 * result) + (sortOrder != null ? sortOrder.hashCode() : 0);
        result = (31 * result) + (authorizations != null ? authorizations.hashCode() : 0);
        return result;
    }
}