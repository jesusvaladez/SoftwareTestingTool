package org.apache.ambari.server.security.authorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
@com.google.inject.Singleton
public class PermissionHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.PermissionHelper.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO;

    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getPermissionLabels(org.springframework.security.core.Authentication authentication) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> permissionLabels = new java.util.HashMap<>();
        if (authentication.getAuthorities() != null) {
            for (org.springframework.security.core.GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (!(grantedAuthority instanceof org.apache.ambari.server.security.authorization.AmbariGrantedAuthority)) {
                    continue;
                }
                org.apache.ambari.server.security.authorization.AmbariGrantedAuthority ambariGrantedAuthority = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (grantedAuthority));
                org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = ambariGrantedAuthority.getPrivilegeEntity();
                java.lang.String key = null;
                try {
                    switch (privilegeEntity.getResource().getResourceType().getName()) {
                        case "CLUSTER" :
                            key = clusterDAO.findByResourceId(privilegeEntity.getResource().getId()).getClusterName();
                            break;
                        case "AMBARI" :
                            key = "Ambari";
                            break;
                        default :
                            key = viewInstanceDAO.findByResourceId(privilegeEntity.getResource().getId()).getLabel();
                            break;
                    }
                } catch (java.lang.Throwable ignored) {
                    org.apache.ambari.server.security.authorization.PermissionHelper.LOG.warn("Error occurred when cluster or view is searched based on resource id", ignored);
                }
                if (key != null) {
                    if (!permissionLabels.containsKey(key)) {
                        permissionLabels.put(key, new java.util.LinkedList<>());
                    }
                    permissionLabels.get(key).add(privilegeEntity.getPermission().getPermissionLabel());
                }
            }
        }
        return permissionLabels;
    }
}