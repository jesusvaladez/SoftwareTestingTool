package org.apache.ambari.server.api.services.views;
import javax.ws.rs.WebApplicationException;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewDataMigrationServiceTest {
    private static java.lang.String viewName = "MY_VIEW";

    private static java.lang.String instanceName = "INSTANCE1";

    private static java.lang.String version1 = "1.0.0";

    private static java.lang.String version2 = "2.0.0";

    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    @org.junit.Test
    public void testServiceMigrateCallAdmin() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        EasyMock.expect(viewRegistry.checkAdmin()).andReturn(true).anyTimes();
        EasyMock.replay(viewRegistry);
        org.apache.ambari.server.view.ViewRegistry.initInstance(viewRegistry);
        org.apache.ambari.server.api.services.views.ViewDataMigrationService service = new org.apache.ambari.server.api.services.views.ViewDataMigrationService();
        org.apache.ambari.server.view.ViewDataMigrationUtility migrationUtility = EasyMock.createStrictMock(org.apache.ambari.server.view.ViewDataMigrationUtility.class);
        migrationUtility.migrateData(EasyMock.anyObject(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class), EasyMock.anyObject(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class), EasyMock.eq(false));
        EasyMock.replay(migrationUtility);
        service.setViewDataMigrationUtility(migrationUtility);
        service.migrateData(org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.viewName, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.version1, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.instanceName, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.version2, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.instanceName);
        EasyMock.verify(migrationUtility);
    }

    @org.junit.Test
    public void testServiceMigrateCallNotAdmin() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        EasyMock.expect(viewRegistry.checkAdmin()).andReturn(false).anyTimes();
        EasyMock.replay(viewRegistry);
        org.apache.ambari.server.view.ViewRegistry.initInstance(viewRegistry);
        org.apache.ambari.server.api.services.views.ViewDataMigrationService service = new org.apache.ambari.server.api.services.views.ViewDataMigrationService();
        thrown.expect(javax.ws.rs.WebApplicationException.class);
        service.migrateData(org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.viewName, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.version1, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.instanceName, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.version2, org.apache.ambari.server.api.services.views.ViewDataMigrationServiceTest.instanceName);
    }
}