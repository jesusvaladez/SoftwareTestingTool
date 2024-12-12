package org.apache.ambari.server.upgrade;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog272.BLUEPRINT_PROVISIONING_STATE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog272.CLUSTERS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog272.HOST_COMPONENT_DESIRED_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog272.RENAME_COLLISION_BEHAVIOR_PROPERTY_SQL;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class UpgradeCatalog272Test {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @org.junit.Before
    public void init() {
        final org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        dbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
    }

    @org.junit.Test
    public void testExecuteDMLUpdates() throws java.lang.Exception {
        final java.lang.reflect.Method renameLdapSynchCollisionBehaviorValue = org.apache.ambari.server.upgrade.UpgradeCatalog272.class.getDeclaredMethod("renameLdapSynchCollisionBehaviorValue");
        final java.lang.reflect.Method createRoleAuthorizations = org.apache.ambari.server.upgrade.UpgradeCatalog272.class.getDeclaredMethod("createRoleAuthorizations");
        final org.apache.ambari.server.upgrade.UpgradeCatalog272 upgradeCatalog272 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog272.class).addMockedMethod(renameLdapSynchCollisionBehaviorValue).addMockedMethod(createRoleAuthorizations).createMock();
        EasyMock.expect(upgradeCatalog272.renameLdapSynchCollisionBehaviorValue()).andReturn(0).once();
        upgradeCatalog272.createRoleAuthorizations();
        EasyMock.expectLastCall().once();
        EasyMock.replay(upgradeCatalog272);
        upgradeCatalog272.executeDMLUpdates();
        EasyMock.verify(upgradeCatalog272);
    }

    @org.junit.Test
    public void testExecuteDDLUpdates() throws java.lang.Exception {
        dbAccessor.dropColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog272.CLUSTERS_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog272.BLUEPRINT_PROVISIONING_STATE_COLUMN));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> blueprintProvisioningStateColumnCapture = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog272.HOST_COMPONENT_DESIRED_STATE_TABLE), EasyMock.capture(blueprintProvisioningStateColumnCapture));
        EasyMock.expectLastCall().once();
        EasyMock.replay(dbAccessor, injector);
        org.apache.ambari.server.upgrade.UpgradeCatalog272 upgradeCatalog272 = new org.apache.ambari.server.upgrade.UpgradeCatalog272(injector);
        upgradeCatalog272.dbAccessor = dbAccessor;
        upgradeCatalog272.executeDDLUpdates();
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo capturedBlueprintProvisioningStateColumn = blueprintProvisioningStateColumnCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog272.BLUEPRINT_PROVISIONING_STATE_COLUMN, capturedBlueprintProvisioningStateColumn.getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.BlueprintProvisioningState.NONE, capturedBlueprintProvisioningStateColumn.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.String.class, capturedBlueprintProvisioningStateColumn.getType());
        EasyMock.verify(dbAccessor);
    }

    @org.junit.Test
    public void shouldRenameCollisionBehaviorLdapCategoryPropertyNameIfTableWithDataExists() throws java.lang.Exception {
        final int expectedResult = 3;
        EasyMock.expect(dbAccessor.tableExists(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE)).andReturn(true).once();
        EasyMock.expect(dbAccessor.executeUpdate(org.apache.ambari.server.upgrade.UpgradeCatalog272.RENAME_COLLISION_BEHAVIOR_PROPERTY_SQL)).andReturn(expectedResult).once();
        EasyMock.replay(dbAccessor);
        final org.apache.ambari.server.upgrade.UpgradeCatalog272 upgradeCatalog272 = new org.apache.ambari.server.upgrade.UpgradeCatalog272(injector);
        upgradeCatalog272.dbAccessor = dbAccessor;
        org.junit.Assert.assertEquals(expectedResult, upgradeCatalog272.renameLdapSynchCollisionBehaviorValue());
        EasyMock.verify(dbAccessor);
    }

    @org.junit.Test
    public void shouldNotRenameCollisionBehaviorLdapCategoryPropertyNameIfTableDoesNotExist() throws java.lang.Exception {
        final int expectedResult = 0;
        EasyMock.expect(dbAccessor.tableExists(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE)).andReturn(false).once();
        EasyMock.replay(dbAccessor);
        final org.apache.ambari.server.upgrade.UpgradeCatalog272 upgradeCatalog272 = new org.apache.ambari.server.upgrade.UpgradeCatalog272(injector);
        upgradeCatalog272.dbAccessor = dbAccessor;
        org.junit.Assert.assertEquals(expectedResult, upgradeCatalog272.renameLdapSynchCollisionBehaviorValue());
        EasyMock.verify(dbAccessor);
    }
}