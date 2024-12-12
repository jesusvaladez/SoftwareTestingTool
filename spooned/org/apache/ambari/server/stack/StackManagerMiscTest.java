package org.apache.ambari.server.stack;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackManagerMiscTest {
    @org.junit.Test
    public void testCycleDetection() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.metadata.ActionMetadata actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.state.stack.OsFamily osFamily = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.StackEntity.class);
        EasyMock.expect(stackDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(stackEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(actionMetadata, stackDao, extensionDao, linkDao, metaInfoDao, osFamily);
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(stackDao, extensionDao, linkDao);
        try {
            java.lang.String stacksCycle1 = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks_with_cycle").getPath();
            org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(stacksCycle1), null, null, osFamily, false, metaInfoDao, actionMetadata, stackDao, extensionDao, linkDao, helper);
            org.junit.Assert.fail("Expected exception due to cyclic stack");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.assertEquals("Cycle detected while parsing stack definition", e.getMessage());
        }
        try {
            java.lang.String stacksCycle2 = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks_with_cycle2").getPath();
            org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(stacksCycle2), null, null, osFamily, true, metaInfoDao, actionMetadata, stackDao, extensionDao, linkDao, helper);
            org.junit.Assert.fail("Expected exception due to cyclic stack");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.assertEquals("Cycle detected while parsing stack definition", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetServiceInfoFromSingleStack() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.metadata.ActionMetadata actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.state.stack.OsFamily osFamily = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.StackEntity.class);
        actionMetadata.addServiceCheckAction("HDFS");
        EasyMock.expect(stackDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(stackEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(metaInfoDao, stackDao, extensionDao, linkDao, actionMetadata, osFamily);
        java.lang.String singleStack = java.lang.ClassLoader.getSystemClassLoader().getResource("single_stack").getPath();
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(stackDao, extensionDao, linkDao);
        org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(singleStack.replace(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER, java.io.File.separator)), null, null, osFamily, false, metaInfoDao, actionMetadata, stackDao, extensionDao, linkDao, helper);
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = stackManager.getStacks();
        org.junit.Assert.assertEquals(1, stacks.size());
        org.junit.Assert.assertNotNull(stacks.iterator().next().getService("HDFS"));
        EasyMock.verify(metaInfoDao, stackDao, actionMetadata, osFamily);
    }

    @org.junit.Test
    public void testCircularDependencyForServiceUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionDAO.class);
        org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ExtensionLinkDAO.class);
        org.apache.ambari.server.metadata.ActionMetadata actionMetadata = EasyMock.createNiceMock(org.apache.ambari.server.metadata.ActionMetadata.class);
        org.apache.ambari.server.state.stack.OsFamily osFamily = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.StackEntity.class);
        EasyMock.expect(stackDao.find(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(stackEntity).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = java.util.Collections.emptyList();
        EasyMock.expect(linkDao.findByStack(org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(list).atLeastOnce();
        EasyMock.replay(metaInfoDao, stackDao, extensionDao, linkDao, actionMetadata, osFamily);
        org.apache.ambari.server.controller.AmbariManagementHelper helper = new org.apache.ambari.server.controller.AmbariManagementHelper(stackDao, extensionDao, linkDao);
        try {
            java.lang.String upgradeCycle = java.lang.ClassLoader.getSystemClassLoader().getResource("stacks_with_upgrade_cycle").getPath();
            org.apache.ambari.server.stack.StackManager stackManager = new org.apache.ambari.server.stack.StackManager(new java.io.File(upgradeCycle), null, null, osFamily, false, metaInfoDao, actionMetadata, stackDao, extensionDao, linkDao, helper);
            org.junit.Assert.fail("Expected exception due to cyclic service upgrade xml");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.assertEquals("Missing groups: [BAR, FOO]", e.getMessage());
        }
    }
}