package org.apache.ambari.server.view;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewInstanceOperationHandlerTest {
    @org.junit.Test
    public void uninstallViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewInstanceOperationHandler viewInstanceOperationHandler = getViewInstanceOperationHandler();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(3L);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        instanceEntity.setName("VIEW_INSTANCE_NAME");
        instanceEntity.setViewName("VIEW_NAME");
        instanceEntity.setResource(resourceEntity);
        org.apache.ambari.server.orm.dao.ViewDAO viewDAO = viewInstanceOperationHandler.viewDAO;
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewDAO.findByName(instanceEntity.getViewName())).andReturn(viewEntity);
        EasyMock.expect(viewEntity.getCommonName()).andReturn("view-common-name");
        EasyMock.expect(viewEntity.getVersion()).andReturn("0.0.1");
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = viewInstanceOperationHandler.instanceDAO;
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(viewInstanceDAO.findByName(instanceEntity.getViewName(), instanceEntity.getName())).andReturn(viewInstanceEntity);
        EasyMock.expect(viewInstanceEntity.isXmlDriven()).andReturn(false);
        EasyMock.expect(viewInstanceEntity.isXmlDriven()).andReturn(false);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilege1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilege2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = java.util.Arrays.asList(privilege1, privilege2);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = viewInstanceOperationHandler.privilegeDAO;
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(privilege1.getPrincipal()).andReturn(principalEntity);
        EasyMock.expect(privilege2.getPrincipal()).andReturn(principalEntity);
        principalEntity.removePrivilege(privilege1);
        principalEntity.removePrivilege(privilege2);
        EasyMock.expect(privilegeDAO.findByResourceId(3L)).andReturn(privileges);
        privilegeDAO.remove(privilege1);
        privilegeDAO.remove(privilege2);
        viewInstanceDAO.remove(viewInstanceEntity);
        EasyMock.replay(privilegeDAO, viewDAO, viewInstanceDAO, principalEntity, privilege1, privilege2, viewInstanceEntity, viewEntity);
        viewInstanceOperationHandler.uninstallViewInstance(instanceEntity);
        EasyMock.verify(privilegeDAO, viewDAO, viewInstanceDAO);
    }

    private org.apache.ambari.server.view.ViewInstanceOperationHandler getViewInstanceOperationHandler() {
        org.apache.ambari.server.orm.dao.ViewDAO viewDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO instanceDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        org.apache.ambari.server.view.ViewInstanceOperationHandler instance = new org.apache.ambari.server.view.ViewInstanceOperationHandler();
        instance.viewDAO = viewDAO;
        instance.instanceDAO = instanceDAO;
        instance.privilegeDAO = privilegeDAO;
        return instance;
    }
}