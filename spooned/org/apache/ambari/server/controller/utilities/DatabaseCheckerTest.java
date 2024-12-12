package org.apache.ambari.server.controller.utilities;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class DatabaseCheckerTest {
    private static com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @org.junit.BeforeClass
    public static void setupClass() throws java.lang.Exception {
        org.apache.ambari.server.controller.utilities.DatabaseCheckerTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.controller.utilities.DatabaseCheckerTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.controller.utilities.DatabaseCheckerTest.injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.utilities.DatabaseCheckerTest.injector);
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testCheckDBVersion_Valid() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity = new org.apache.ambari.server.orm.entities.MetainfoEntity();
        java.lang.String serverVersion = ambariMetaInfo.getServerVersion();
        metainfoEntity.setMetainfoName(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY);
        metainfoEntity.setMetainfoValue(serverVersion);
        EasyMock.expect(metainfoDAO.findByKey(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY)).andReturn(metainfoEntity);
        EasyMock.replay(metainfoDAO);
        org.apache.ambari.server.controller.utilities.DatabaseChecker.metainfoDAO = metainfoDAO;
        org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo = ambariMetaInfo;
        try {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.checkDBVersion();
        } catch (org.apache.ambari.server.AmbariException ae) {
            org.junit.Assert.fail("DB versions check failed.");
        }
    }

    @org.junit.Ignore
    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testCheckDBVersionInvalid() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity = new org.apache.ambari.server.orm.entities.MetainfoEntity();
        metainfoEntity.setMetainfoName(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY);
        metainfoEntity.setMetainfoValue("0.0.0");
        EasyMock.expect(metainfoDAO.findByKey(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY)).andReturn(metainfoEntity);
        EasyMock.replay(metainfoDAO);
        org.apache.ambari.server.controller.utilities.DatabaseChecker.metainfoDAO = metainfoDAO;
        org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo = ambariMetaInfo;
        org.apache.ambari.server.controller.utilities.DatabaseChecker.checkDBVersion();
    }
}