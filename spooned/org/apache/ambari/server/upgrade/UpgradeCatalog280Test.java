package org.apache.ambari.server.upgrade;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog280.HOST_COMPONENT_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog280.LAST_LIVE_STATE_COLUMN;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class UpgradeCatalog280Test {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @org.junit.Before
    public void init() {
        final org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        dbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
    }

    @org.junit.Test
    public void testExecuteDDLUpdates() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> perBatchLimitColumn = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq("requestschedule"), EasyMock.capture(perBatchLimitColumn));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> autoPauseColumn = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq("requestschedule"), EasyMock.capture(autoPauseColumn));
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog280.HOST_COMPONENT_STATE_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog280.LAST_LIVE_STATE_COLUMN));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> upgradePackStackColumn = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq("upgrade"), EasyMock.capture(upgradePackStackColumn));
        EasyMock.expectLastCall().once();
        final org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> alterPropertyValueColumnCapture = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.alterColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_TABLE), EasyMock.capture(alterPropertyValueColumnCapture));
        EasyMock.expectLastCall().once();
        EasyMock.replay(dbAccessor, injector);
        org.apache.ambari.server.upgrade.UpgradeCatalog280 upgradeCatalog280 = new org.apache.ambari.server.upgrade.UpgradeCatalog280(injector);
        upgradeCatalog280.dbAccessor = dbAccessor;
        upgradeCatalog280.executeDDLUpdates();
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo perBatchLimitColumnInfo = perBatchLimitColumn.getValue();
        org.junit.Assert.assertEquals("batch_toleration_limit_per_batch", perBatchLimitColumnInfo.getName());
        org.junit.Assert.assertEquals(null, perBatchLimitColumnInfo.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.Short.class, perBatchLimitColumnInfo.getType());
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo autoPauseColumnInfo = autoPauseColumn.getValue();
        org.junit.Assert.assertEquals("pause_after_first_batch", autoPauseColumnInfo.getName());
        org.junit.Assert.assertEquals(null, autoPauseColumnInfo.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.Boolean.class, autoPauseColumnInfo.getType());
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo capturedUpgradeColumn = upgradePackStackColumn.getValue();
        org.junit.Assert.assertEquals("upgrade_pack_stack_id", capturedUpgradeColumn.getName());
        org.junit.Assert.assertEquals(java.lang.String.class, capturedUpgradeColumn.getType());
        org.junit.Assert.assertEquals(((java.lang.Integer) (255)), capturedUpgradeColumn.getLength());
        final org.apache.ambari.server.orm.DBAccessor.DBColumnInfo alterPropertyValueColumn = alterPropertyValueColumnCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, alterPropertyValueColumn.getName());
        org.junit.Assert.assertEquals(java.lang.String.class, alterPropertyValueColumn.getType());
        org.junit.Assert.assertEquals(((java.lang.Integer) (4000)), alterPropertyValueColumn.getLength());
        org.junit.Assert.assertFalse(alterPropertyValueColumn.isNullable());
        EasyMock.verify(dbAccessor);
    }
}