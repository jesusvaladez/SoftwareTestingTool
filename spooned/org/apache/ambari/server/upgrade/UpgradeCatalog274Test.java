package org.apache.ambari.server.upgrade;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class UpgradeCatalog274Test {
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
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo dbColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, java.lang.String.class, 2000);
        final org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> alterPropertyValueColumnCapture = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.getColumnInfo(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN));
        EasyMock.expectLastCall().andReturn(dbColumnInfo).once();
        dbAccessor.alterColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE), EasyMock.capture(alterPropertyValueColumnCapture));
        EasyMock.expectLastCall().once();
        EasyMock.replay(dbAccessor, injector);
        org.apache.ambari.server.upgrade.UpgradeCatalog274 upgradeCatalog274 = new org.apache.ambari.server.upgrade.UpgradeCatalog274(injector);
        upgradeCatalog274.dbAccessor = dbAccessor;
        upgradeCatalog274.executeDDLUpdates();
        final org.apache.ambari.server.orm.DBAccessor.DBColumnInfo alterPropertyValueColumn = alterPropertyValueColumnCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, alterPropertyValueColumn.getName());
        org.junit.Assert.assertEquals(java.lang.String.class, alterPropertyValueColumn.getType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN, alterPropertyValueColumn.getLength());
        EasyMock.verify(dbAccessor);
    }
}