package org.apache.ambari.server.checks;
import com.google.inject.persist.PersistService;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class MpackInstallCheckerTest {
    @org.junit.Test
    public void testCheckValidClusters() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final java.sql.ResultSet stackResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final com.google.inject.persist.PersistService mockPersistService = easyMockSupport.createNiceMock(com.google.inject.persist.PersistService.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(com.google.inject.persist.PersistService.class).toInstance(mockPersistService);
            }
        });
        org.apache.ambari.server.checks.MpackInstallChecker mpackInstallChecker = mockInjector.getInstance(org.apache.ambari.server.checks.MpackInstallChecker.class);
        java.util.HashSet<java.lang.String> stacksInMpack = new java.util.HashSet<>();
        stacksInMpack.add("HDF");
        EasyMock.expect(mpackInstallChecker.getConnection()).andReturn(mockConnection);
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id")).andReturn(stackResultSet);
        EasyMock.expect(stackResultSet.next()).andReturn(true);
        EasyMock.expect(stackResultSet.getString("stack_name")).andReturn("HDF");
        EasyMock.expect(stackResultSet.getString("stack_version")).andReturn("2.0");
        EasyMock.expect(stackResultSet.getString("cluster_name")).andReturn("cl1");
        easyMockSupport.replayAll();
        mpackInstallChecker.checkClusters(stacksInMpack);
        easyMockSupport.verifyAll();
        junit.framework.Assert.assertFalse("No errors should have been triggered.", mpackInstallChecker.isErrorsFound());
    }

    @org.junit.Test
    public void testCheckInvalidClusters() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final java.sql.ResultSet stackResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final com.google.inject.persist.PersistService mockPersistService = easyMockSupport.createNiceMock(com.google.inject.persist.PersistService.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(com.google.inject.persist.PersistService.class).toInstance(mockPersistService);
            }
        });
        org.apache.ambari.server.checks.MpackInstallChecker mpackInstallChecker = mockInjector.getInstance(org.apache.ambari.server.checks.MpackInstallChecker.class);
        java.util.HashSet<java.lang.String> stacksInMpack = new java.util.HashSet<>();
        stacksInMpack.add("HDF");
        EasyMock.expect(mpackInstallChecker.getConnection()).andReturn(mockConnection);
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id")).andReturn(stackResultSet);
        EasyMock.expect(stackResultSet.next()).andReturn(true);
        EasyMock.expect(stackResultSet.getString("stack_name")).andReturn("HDP");
        EasyMock.expect(stackResultSet.getString("stack_version")).andReturn("2.5");
        EasyMock.expect(stackResultSet.getString("cluster_name")).andReturn("cl1");
        easyMockSupport.replayAll();
        mpackInstallChecker.checkClusters(stacksInMpack);
        easyMockSupport.verifyAll();
        junit.framework.Assert.assertTrue("Installing HDF mpack on HDP cluster with purge option should have triggered an error.", mpackInstallChecker.isErrorsFound());
    }
}