package org.apache.ambari.server.view;
@javax.inject.Singleton
public class ViewInstanceOperationHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewInstanceOperationHandler.class);

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ViewDAO viewDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ViewInstanceDAO instanceDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO;

    private void removePrivilegeEntity(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = privilegeEntity.getPrincipal();
        if (principalEntity != null) {
            principalEntity.removePrivilege(privilegeEntity);
        }
        privilegeDAO.remove(privilegeEntity);
    }

    public void uninstallViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        org.apache.ambari.server.view.ViewInstanceOperationHandler.LOG.info("uninstalling ViewInstance : {} ", instanceEntity);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewDAO.findByName(instanceEntity.getViewName());
        org.apache.ambari.server.view.ViewInstanceOperationHandler.LOG.info("viewEntity received corresponding to the view entity : {} ", viewEntity);
        if (viewEntity != null) {
            java.lang.String instanceName = instanceEntity.getName();
            java.lang.String viewName = viewEntity.getCommonName();
            java.lang.String version = viewEntity.getVersion();
            org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition = instanceDAO.findByName(instanceEntity.getViewName(), instanceEntity.getName());
            org.apache.ambari.server.view.ViewInstanceOperationHandler.LOG.debug("view instance entity received from database : {}", instanceDefinition);
            if (instanceDefinition != null) {
                if (instanceDefinition.isXmlDriven()) {
                    throw new java.lang.IllegalStateException("View instances defined via xml can't be deleted through api requests");
                }
                java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> instancePrivileges = privilegeDAO.findByResourceId(instanceEntity.getResource().getId());
                org.apache.ambari.server.view.ViewInstanceOperationHandler.LOG.info("Removing privilege entities : {}", instancePrivileges);
                for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : instancePrivileges) {
                    removePrivilegeEntity(privilegeEntity);
                }
                org.apache.ambari.server.view.ViewInstanceOperationHandler.LOG.info("Deleting view instance : view name : {}, version : {}, instanceName : {}", viewName, version, instanceName);
                instanceDAO.remove(instanceDefinition);
            } else {
                throw new java.lang.IllegalStateException(("View instance '" + instanceEntity.getName()) + "' not found.");
            }
        } else {
            throw new java.lang.IllegalStateException(((("View '" + instanceEntity.getViewName()) + "' not found corresponding to view instance '") + instanceEntity.getName()) + "'");
        }
    }
}