package org.apache.ambari.server.checks;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.MapUtils;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
public class DatabaseConsistencyCheckHelperTest {
    private com.google.inject.Injector injector;

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        if (injector != null) {
            org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        }
    }

    @org.junit.Test
    public void testCheckForConfigsSelectedMoreThanOnce() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cc.type_name from clusterconfig cc " + (("join clusters c on cc.cluster_id=c.cluster_id " + "group by c.cluster_name, cc.type_name ") + "having sum(selected) > 1"))).andReturn(mockResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForConfigsSelectedMoreThanOnce();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckForHostsWithoutState() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select host_name from hosts where host_id not in (select host_id from hoststate)")).andReturn(mockResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForHostsWithoutState();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckHostComponentStatesCountEqualsHostComponentsDesiredStates() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select count(*) from hostcomponentstate")).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select count(*) from hostcomponentdesiredstate")).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select count(*) FROM hostcomponentstate hcs " + ("JOIN hostcomponentdesiredstate hcds ON hcs.service_name=hcds.service_name AND " + "hcs.component_name=hcds.component_name AND hcs.host_id=hcds.host_id"))).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select component_name, host_id from hostcomponentstate group by component_name, host_id having count(component_name) > 1")).andReturn(mockResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkHostComponentStates();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckServiceConfigs() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo = easyMockSupport.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet stackResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet serviceConfigResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.state.ServiceInfo mockHDFSServiceInfo = easyMockSupport.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = createInjectorWithAmbariMetaInfo(mockAmbariMetainfo, mockDBDbAccessor);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = new java.util.HashMap<>();
        services.put("HDFS", mockHDFSServiceInfo);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configAttributes = new java.util.HashMap<>();
        configAttributes.put("core-site", new java.util.HashMap<>());
        EasyMock.expect(mockHDFSServiceInfo.getConfigTypeAttributes()).andReturn(configAttributes);
        EasyMock.expect(mockAmbariMetainfo.getServices("HDP", "2.2")).andReturn(services);
        EasyMock.expect(serviceConfigResultSet.next()).andReturn(true).times(2);
        EasyMock.expect(serviceConfigResultSet.getString("service_name")).andReturn("HDFS").andReturn("HBASE");
        EasyMock.expect(serviceConfigResultSet.getString("type_name")).andReturn("core-site").andReturn("hbase-env");
        EasyMock.expect(stackResultSet.next()).andReturn(true);
        EasyMock.expect(stackResultSet.getString("stack_name")).andReturn("HDP");
        EasyMock.expect(stackResultSet.getString("stack_version")).andReturn("2.2");
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, service_name from clusterservices cs " + ("join clusters c on cs.cluster_id=c.cluster_id " + "where service_name not in (select service_name from serviceconfig sc where sc.cluster_id=cs.cluster_id and sc.service_name=cs.service_name and sc.group_id is null)"))).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, sc.service_name, sc.version from serviceconfig sc " + ("join clusters c on sc.cluster_id=c.cluster_id " + "where service_config_id not in (select service_config_id from serviceconfigmapping) and group_id is null"))).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id")).andReturn(stackResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cs.service_name, cc.type_name, sc.version from clusterservices cs " + ((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and sc.cluster_id=cc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id and sc.stack_id=c.desired_stack_id ") + "where sc.group_id is null and sc.service_config_id=(select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name, sc.version"))).andReturn(serviceConfigResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cs.service_name, cc.type_name from clusterservices cs " + (((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and cc.cluster_id=sc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id ") + "where sc.group_id is null and sc.service_config_id = (select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name ") + "having sum(cc.selected) < 1"))).andReturn(mockResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        mockAmbariMetainfo.init();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkServiceConfigs();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testSchemaName_NoIssues() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", "ambari, public", com.google.common.collect.Lists.newArrayList("ambari", "public"), com.google.common.collect.Lists.newArrayList("ambari"));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertFalse("No warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_WrongSearchPathOrder() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", "public, ambari", com.google.common.collect.Lists.newArrayList("ambari", "public"), com.google.common.collect.Lists.newArrayList("ambari"));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_NoSearchPath() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", null, com.google.common.collect.Lists.newArrayList("ambari", "public"), com.google.common.collect.Lists.newArrayList("ambari"));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_NoAmbariSchema() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", null, com.google.common.collect.Lists.newArrayList("public"), com.google.common.collect.Lists.newArrayList());
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_NoTablesInAmbariSchema() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", "ambari", com.google.common.collect.Lists.newArrayList("ambari", "public"), com.google.common.collect.Lists.newArrayList("public"));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_AmbariTablesInMultipleSchemas() throws java.lang.Exception {
        setupMocksForTestSchemaName("ambari", "ambari", com.google.common.collect.Lists.newArrayList("ambari", "public"), com.google.common.collect.Lists.newArrayList("ambari", "public"));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testSchemaName_NullsAreTolerated() throws java.lang.Exception {
        setupMocksForTestSchemaName(null, null, null, null);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
        org.junit.Assert.assertTrue("Warnings were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors were expected.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    private void setupMocksForTestSchemaName(java.lang.String configuredSchema, java.lang.String searchPath, java.util.List<java.lang.String> schemas, java.util.List<java.lang.String> schemasWithAmbariTables) throws java.lang.Exception {
        final org.apache.ambari.server.configuration.Configuration config = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        final org.apache.ambari.server.state.stack.OsFamily osFamily = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        final org.apache.ambari.server.orm.DBAccessor dbAccessor = EasyMock.createStrictMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Statement searchPathStatement = EasyMock.createStrictMock(java.sql.Statement.class);
        final java.sql.Statement getTablesStatement = EasyMock.createStrictMock(java.sql.Statement.class);
        final java.sql.DatabaseMetaData dbMetaData = EasyMock.createStrictMock(java.sql.DatabaseMetaData.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(config);
            }
        });
        EasyMock.expect(config.getDatabaseSchema()).andReturn(configuredSchema).anyTimes();
        EasyMock.expect(config.getDatabaseType()).andReturn(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES);
        EasyMock.expect(dbAccessor.getConnection()).andReturn(connection);
        EasyMock.expect(connection.getMetaData()).andReturn(dbMetaData);
        EasyMock.expect(connection.createStatement()).andReturn(searchPathStatement);
        EasyMock.expect(connection.createStatement()).andReturn(getTablesStatement);
        EasyMock.expect(dbMetaData.getSchemas()).andReturn(resultSet("TABLE_SCHEM", schemas));
        EasyMock.expect(searchPathStatement.executeQuery(EasyMock.anyString())).andReturn(resultSet("search_path", com.google.common.collect.Lists.newArrayList(searchPath)));
        EasyMock.expect(getTablesStatement.executeQuery(EasyMock.anyString())).andReturn(resultSet("table_schema", schemasWithAmbariTables));
        EasyMock.replay(config, connection, dbAccessor, dbMetaData, getTablesStatement, osFamily, searchPathStatement);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(null);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.resetCheckResult();
    }

    private java.sql.ResultSet resultSet(final java.lang.String columnName, final java.util.List<? extends java.lang.Object> columnData) throws java.sql.SQLException {
        if (null == columnData) {
            return null;
        } else {
            java.sql.ResultSet rs = EasyMock.createNiceMock(java.sql.ResultSet.class);
            if (!columnData.isEmpty()) {
                EasyMock.expect(rs.next()).andReturn(true).times(columnData.size());
            }
            EasyMock.expect(rs.next()).andReturn(false);
            for (java.lang.Object item : columnData) {
                EasyMock.expect(rs.getObject(columnName)).andReturn(item);
            }
            EasyMock.replay(rs);
            return rs;
        }
    }

    @org.junit.Test
    public void testCheckServiceConfigs_missingServiceConfigGeneratesWarning() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo = easyMockSupport.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet clusterServicesResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet stackResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet serviceConfigResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.state.ServiceInfo mockHDFSServiceInfo = easyMockSupport.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = createInjectorWithAmbariMetaInfo(mockAmbariMetainfo, mockDBDbAccessor);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = new java.util.HashMap<>();
        services.put("HDFS", mockHDFSServiceInfo);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configAttributes = new java.util.HashMap<>();
        configAttributes.put("core-site", new java.util.HashMap<>());
        EasyMock.expect(mockHDFSServiceInfo.getConfigTypeAttributes()).andReturn(configAttributes);
        EasyMock.expect(mockAmbariMetainfo.getServices("HDP", "2.2")).andReturn(services);
        EasyMock.expect(clusterServicesResultSet.next()).andReturn(true);
        EasyMock.expect(clusterServicesResultSet.getString("service_name")).andReturn("OPENSOFT R");
        EasyMock.expect(clusterServicesResultSet.getString("cluster_name")).andReturn("My Cluster");
        EasyMock.expect(serviceConfigResultSet.next()).andReturn(true);
        EasyMock.expect(serviceConfigResultSet.getString("service_name")).andReturn("HDFS");
        EasyMock.expect(serviceConfigResultSet.getString("type_name")).andReturn("core-site");
        EasyMock.expect(stackResultSet.next()).andReturn(true);
        EasyMock.expect(stackResultSet.getString("stack_name")).andReturn("HDP");
        EasyMock.expect(stackResultSet.getString("stack_version")).andReturn("2.2");
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, service_name from clusterservices cs " + ("join clusters c on cs.cluster_id=c.cluster_id " + "where service_name not in (select service_name from serviceconfig sc where sc.cluster_id=cs.cluster_id and sc.service_name=cs.service_name and sc.group_id is null)"))).andReturn(clusterServicesResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, sc.service_name, sc.version from serviceconfig sc " + ("join clusters c on sc.cluster_id=c.cluster_id " + "where service_config_id not in (select service_config_id from serviceconfigmapping) and group_id is null"))).andReturn(mockResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id")).andReturn(stackResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cs.service_name, cc.type_name, sc.version from clusterservices cs " + ((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and sc.cluster_id=cc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id and sc.stack_id=c.desired_stack_id ") + "where sc.group_id is null and sc.service_config_id=(select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name, sc.version"))).andReturn(serviceConfigResultSet);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cs.service_name, cc.type_name from clusterservices cs " + (((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and cc.cluster_id=sc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id ") + "where sc.group_id is null and sc.service_config_id = (select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name ") + "having sum(cc.selected) < 1"))).andReturn(mockResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        mockAmbariMetainfo.init();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.resetCheckResult();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkServiceConfigs();
        easyMockSupport.verifyAll();
        org.junit.Assert.assertTrue("Missing service config for OPENSOFT R should have triggered a warning.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult() == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
        org.junit.Assert.assertFalse("No errors should have been triggered.", org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError());
    }

    @org.junit.Test
    public void testCheckForLargeTables() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo = easyMockSupport.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final java.sql.ResultSet hostRoleCommandResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet executionCommandResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet stageResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet requestResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.ResultSet alertHistoryResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final com.google.inject.Injector mockInjector = createInjectorWithAmbariMetaInfo(mockAmbariMetainfo, mockDBDbAccessor);
        EasyMock.expect(hostRoleCommandResultSet.next()).andReturn(true).once();
        EasyMock.expect(executionCommandResultSet.next()).andReturn(true).once();
        EasyMock.expect(stageResultSet.next()).andReturn(true).once();
        EasyMock.expect(requestResultSet.next()).andReturn(true).once();
        EasyMock.expect(alertHistoryResultSet.next()).andReturn(true).once();
        EasyMock.expect(hostRoleCommandResultSet.getLong(1)).andReturn(2345L).atLeastOnce();
        EasyMock.expect(executionCommandResultSet.getLong(1)).andReturn(12345L).atLeastOnce();
        EasyMock.expect(stageResultSet.getLong(1)).andReturn(2321L).atLeastOnce();
        EasyMock.expect(requestResultSet.getLong(1)).andReturn(1111L).atLeastOnce();
        EasyMock.expect(alertHistoryResultSet.getLong(1)).andReturn(2223L).atLeastOnce();
        EasyMock.expect(mockDBDbAccessor.getConnection()).andReturn(mockConnection);
        EasyMock.expect(mockDBDbAccessor.getDbType()).andReturn(org.apache.ambari.server.orm.DBAccessor.DbType.MYSQL);
        EasyMock.expect(mockDBDbAccessor.getDbSchema()).andReturn("test_schema");
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement).anyTimes();
        EasyMock.expect(mockStatement.executeQuery("SELECT (data_length + index_length) \"Table Size\" " + "FROM information_schema.TABLES WHERE table_schema = \"test_schema\" AND table_name =\"host_role_command\"")).andReturn(hostRoleCommandResultSet);
        EasyMock.expect(mockStatement.executeQuery("SELECT (data_length + index_length) \"Table Size\" " + "FROM information_schema.TABLES WHERE table_schema = \"test_schema\" AND table_name =\"execution_command\"")).andReturn(executionCommandResultSet);
        EasyMock.expect(mockStatement.executeQuery("SELECT (data_length + index_length) \"Table Size\" " + "FROM information_schema.TABLES WHERE table_schema = \"test_schema\" AND table_name =\"stage\"")).andReturn(stageResultSet);
        EasyMock.expect(mockStatement.executeQuery("SELECT (data_length + index_length) \"Table Size\" " + "FROM information_schema.TABLES WHERE table_schema = \"test_schema\" AND table_name =\"request\"")).andReturn(requestResultSet);
        EasyMock.expect(mockStatement.executeQuery("SELECT (data_length + index_length) \"Table Size\" " + "FROM information_schema.TABLES WHERE table_schema = \"test_schema\" AND table_name =\"alert_history\"")).andReturn(alertHistoryResultSet);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        mockAmbariMetainfo.init();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.resetCheckResult();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForLargeTables();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testConfigGroupHostMappings() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c1", cluster);
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).anyTimes();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.configgroup.ConfigGroup cg1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup cg2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        configGroupMap.put(1L, cg1);
        configGroupMap.put(2L, cg2);
        EasyMock.expect(cluster.getConfigGroups()).andReturn(configGroupMap).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        org.apache.ambari.server.state.Host h1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host h2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Host.class);
        hosts.put("h1", h1);
        EasyMock.expect(mockClusters.getHostsForCluster("c1")).andReturn(hosts);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> cgHosts = new java.util.HashMap<>();
        cgHosts.put(1L, h1);
        cgHosts.put(2L, h2);
        EasyMock.expect(cg1.getHosts()).andReturn(cgHosts);
        EasyMock.expect(h1.getHostName()).andReturn("h1").anyTimes();
        EasyMock.expect(h2.getHostName()).andReturn("h2").anyTimes();
        EasyMock.expect(h1.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(h2.getHostId()).andReturn(2L).anyTimes();
        EasyMock.expect(cg1.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(cg2.getId()).andReturn(2L).anyTimes();
        EasyMock.expect(cg1.getName()).andReturn("cg1").anyTimes();
        EasyMock.expect(cg2.getName()).andReturn("cg2").anyTimes();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        java.util.Map<java.lang.Long, java.util.Set<java.lang.Long>> hostIds = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupHostMapping(true);
        easyMockSupport.verifyAll();
        org.junit.Assert.assertNotNull(hostIds);
        org.junit.Assert.assertEquals(1, hostIds.size());
        org.junit.Assert.assertEquals(1L, hostIds.keySet().iterator().next().longValue());
        org.junit.Assert.assertEquals(2L, hostIds.get(1L).iterator().next().longValue());
    }

    @org.junit.Test
    public void testConfigGroupForDeletedServices() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c1", cluster);
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).anyTimes();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.configgroup.ConfigGroup cg1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup cg2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup cgWithoutServiceName = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        configGroupMap.put(1L, cg1);
        configGroupMap.put(2L, cg2);
        configGroupMap.put(3L, cgWithoutServiceName);
        EasyMock.expect(cluster.getConfigGroups()).andStubReturn(configGroupMap);
        EasyMock.expect(cg1.getName()).andReturn("cg1").anyTimes();
        EasyMock.expect(cg1.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(cg1.getServiceName()).andReturn("YARN").anyTimes();
        EasyMock.expect(cg2.getServiceName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(cgWithoutServiceName.getName()).andReturn("cg3").anyTimes();
        EasyMock.expect(cgWithoutServiceName.getId()).andReturn(3L).anyTimes();
        EasyMock.expect(cgWithoutServiceName.getServiceName()).andReturn(null).anyTimes();
        org.apache.ambari.server.state.Service service = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("HDFS", service);
        EasyMock.expect(cluster.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(cg1.getClusterName()).andReturn("c1");
        EasyMock.expect(mockClusters.getCluster("c1")).andReturn(cluster).anyTimes();
        cluster.deleteConfigGroup(1L);
        EasyMock.expectLastCall();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupsForDeletedServices(true);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigGroupsForDeletedServices();
        easyMockSupport.verifyAll();
        org.junit.Assert.assertFalse(org.apache.commons.collections.MapUtils.isEmpty(configGroups));
        org.junit.Assert.assertEquals(2, configGroups.size());
        org.junit.Assert.assertTrue(configGroups.containsKey(1L));
        org.junit.Assert.assertFalse(configGroups.containsKey(2L));
        org.junit.Assert.assertTrue(configGroups.containsKey(3L));
    }

    @org.junit.Test
    public void testCheckForStalealertdefs() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.state.alert.AlertDefinition alertDefinition = easyMockSupport.createNiceMock(org.apache.ambari.server.state.alert.AlertDefinition.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.alert.AlertDefinition.class).toInstance(alertDefinition);
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        final java.sql.ResultSet staleAlertResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(staleAlertResultSet.next()).andReturn(true).once();
        EasyMock.expect(staleAlertResultSet.getString("definition_name")).andReturn("ALERT-NAME").atLeastOnce();
        EasyMock.expect(staleAlertResultSet.getString("service_name")).andReturn("SERVICE-DELETED").atLeastOnce();
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        EasyMock.expect(mockDBDbAccessor.getConnection()).andReturn(mockConnection);
        EasyMock.expect(mockDBDbAccessor.getDbType()).andReturn(org.apache.ambari.server.orm.DBAccessor.DbType.MYSQL);
        EasyMock.expect(mockDBDbAccessor.getDbSchema()).andReturn("test_schema");
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement).anyTimes();
        EasyMock.expect(mockStatement.executeQuery("select definition_name, service_name from alert_definition where service_name not in " + "(select service_name from clusterservices) and service_name not in ('AMBARI')")).andReturn(staleAlertResultSet);
        EasyMock.expect(alertDefinition.getDefinitionId()).andReturn(1L);
        EasyMock.expect(alertDefinition.getName()).andReturn("AlertTest");
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        java.util.Map<java.lang.String, java.lang.String> stalealertdefs1 = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForStalealertdefs();
        org.junit.Assert.assertEquals(1, stalealertdefs1.size());
    }

    @org.junit.Test
    public void testCollectConfigGroupsWithoutServiceName() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster1 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c1", cluster1);
        org.apache.ambari.server.state.Cluster cluster2 = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c2", cluster2);
        EasyMock.expect(cluster2.getConfigGroups()).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup>(0)).anyTimes();
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(mockClusters.getCluster("c1")).andReturn(cluster1).anyTimes();
        EasyMock.expect(mockClusters.getCluster("c2")).andReturn(cluster2).anyTimes();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.configgroup.ConfigGroup cgWithoutServiceName = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup cgWithServiceName = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup cgForNonExistentService = easyMockSupport.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        configGroupMap.put(1L, cgWithoutServiceName);
        configGroupMap.put(2L, cgWithServiceName);
        configGroupMap.put(3L, cgForNonExistentService);
        EasyMock.expect(cluster1.getConfigGroups()).andReturn(configGroupMap).anyTimes();
        EasyMock.expect(cgWithoutServiceName.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(cgWithoutServiceName.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(cgWithoutServiceName.getServiceName()).andReturn(null).anyTimes();
        EasyMock.expect(cgWithoutServiceName.getTag()).andReturn("YARN").anyTimes();
        cgWithoutServiceName.setServiceName("YARN");
        EasyMock.expectLastCall();
        EasyMock.expect(cgWithServiceName.getId()).andReturn(2L).anyTimes();
        EasyMock.expect(cgWithServiceName.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(cgWithServiceName.getServiceName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(cgForNonExistentService.getId()).andReturn(3L).anyTimes();
        EasyMock.expect(cgForNonExistentService.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(cgForNonExistentService.getServiceName()).andReturn(null).anyTimes();
        EasyMock.expect(cgForNonExistentService.getTag()).andReturn("NOT_EXISTS").anyTimes();
        org.apache.ambari.server.state.Service hdfsService = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service yarnService = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("HDFS", hdfsService);
        services.put("YARN", yarnService);
        EasyMock.expect(cluster1.getServices()).andReturn(services).anyTimes();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.collectConfigGroupsWithoutServiceName();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigGroupServiceNames();
        easyMockSupport.verifyAll();
        org.junit.Assert.assertFalse(org.apache.commons.collections.MapUtils.isEmpty(configGroups));
        org.junit.Assert.assertEquals(2, configGroups.size());
        org.junit.Assert.assertTrue(configGroups.containsKey(1L));
        org.junit.Assert.assertFalse(configGroups.containsKey(2L));
        org.junit.Assert.assertTrue(configGroups.containsKey(3L));
    }

    @org.junit.Test
    public void testCollectConfigGroupsWithoutServiceNameReturnsEmptyMapWhenNoClusters() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).anyTimes();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.collectConfigGroupsWithoutServiceName();
        easyMockSupport.verifyAll();
        org.junit.Assert.assertTrue(org.apache.commons.collections.MapUtils.isEmpty(configGroups));
    }

    @org.junit.Test
    public void testFixConfigsSelectedMoreThanOnce() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final java.sql.ResultSet mockResultSet = easyMockSupport.createNiceMock(java.sql.ResultSet.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(clusterDAO);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
            }
        });
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement);
        EasyMock.expect(mockStatement.executeQuery("select c.cluster_name, cc.type_name from clusterconfig cc " + (("join clusters c on cc.cluster_id=c.cluster_id " + "group by c.cluster_name, cc.type_name ") + "having sum(cc.selected) > 1"))).andReturn(mockResultSet);
        EasyMock.expect(mockResultSet.next()).andReturn(true).once();
        EasyMock.expect(mockResultSet.getString("cluster_name")).andReturn("123").once();
        EasyMock.expect(mockResultSet.getString("type_name")).andReturn("type1").once();
        EasyMock.expect(mockResultSet.next()).andReturn(false).once();
        org.apache.ambari.server.state.Cluster clusterMock = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(mockClusters.getCluster("123")).andReturn(clusterMock);
        EasyMock.expect(clusterMock.getClusterId()).andReturn(123L).once();
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity1 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity2 = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        EasyMock.expect(clusterConfigEntity1.getType()).andReturn("type1").anyTimes();
        EasyMock.expect(clusterConfigEntity1.getSelectedTimestamp()).andReturn(123L);
        clusterConfigEntity1.setSelected(false);
        EasyMock.expectLastCall().once();
        EasyMock.expect(clusterConfigEntity2.getType()).andReturn("type1").anyTimes();
        EasyMock.expect(clusterConfigEntity2.getSelectedTimestamp()).andReturn(321L);
        clusterConfigEntity2.setSelected(false);
        EasyMock.expectLastCall().once();
        clusterConfigEntity2.setSelected(true);
        EasyMock.expectLastCall().once();
        javax.persistence.TypedQuery queryMock = easyMockSupport.createNiceMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(mockEntityManager.createNamedQuery(EasyMock.anyString(), EasyMock.anyObject(java.lang.Class.class))).andReturn(queryMock).anyTimes();
        EasyMock.expect(queryMock.setParameter(EasyMock.anyString(), EasyMock.anyString())).andReturn(queryMock).once();
        EasyMock.expect(queryMock.setParameter(EasyMock.anyString(), EasyMock.anyLong())).andReturn(queryMock).once();
        EasyMock.expect(queryMock.getResultList()).andReturn(java.util.Arrays.asList(clusterConfigEntity1, clusterConfigEntity2)).once();
        EasyMock.expect(clusterDAO.merge(EasyMock.anyObject(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class), EasyMock.anyBoolean())).andReturn(null).times(3);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setConnection(mockConnection);
        easyMockSupport.replayAll();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigsSelectedMoreThanOnce();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckForConfigsNotMappedToService() throws java.sql.SQLException, org.apache.ambari.server.AmbariException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.state.ConfigFactory configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(injector);
        java.lang.String STACK_VERSION = "0.1";
        java.lang.String REPO_VERSION = "0.1-1234";
        org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);
        org.apache.ambari.server.orm.OrmTestHelper ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(STACK_ID, REPO_VERSION);
        java.lang.String clusterName = "foo";
        clusters.addCluster(clusterName, STACK_ID);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        configFactory.createNew(cluster, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        configFactory.createNew(cluster, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForConfigsNotMappedToService();
        org.junit.Assert.assertEquals(org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING, org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult());
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixClusterConfigsNotMappedToAnyService();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.resetCheckResult();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForConfigsNotMappedToService();
        org.junit.Assert.assertEquals(org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_SUCCESS, org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult());
    }

    private com.google.inject.Injector createInjectorWithAmbariMetaInfo(org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo, org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor) {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addAmbariMetaInfoBinding().addDBAccessorBinding(mockDBDbAccessor).addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(mockAmbariMetainfo);
            }
        });
    }
}