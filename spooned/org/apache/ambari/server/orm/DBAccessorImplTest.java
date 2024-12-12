package org.apache.ambari.server.orm;
import org.eclipse.persistence.platform.database.DatabasePlatform;
import org.eclipse.persistence.sessions.DatabaseSession;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reset;
public class DBAccessorImplTest {
    private com.google.inject.Injector injector;

    private static final java.util.concurrent.atomic.AtomicInteger tables_counter = new java.util.concurrent.atomic.AtomicInteger(1);

    private static final java.util.concurrent.atomic.AtomicInteger schemas_counter = new java.util.concurrent.atomic.AtomicInteger(1);

    @org.junit.Rule
    public org.junit.rules.ExpectedException exception = org.junit.rules.ExpectedException.none();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private static java.lang.String getFreeTableName() {
        return "test_table_" + org.apache.ambari.server.orm.DBAccessorImplTest.tables_counter.getAndIncrement();
    }

    private static java.lang.String getFreeSchamaName() {
        return "test_schema_" + org.apache.ambari.server.orm.DBAccessorImplTest.schemas_counter.getAndIncrement();
    }

    private void createMyTable(java.lang.String tableName) throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("id", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name", java.lang.String.class, 20000, null, true));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("time", java.lang.Long.class, null, null, true));
        dbAccessor.createTable(tableName, columns, "id");
    }

    private void createMyTable(java.lang.String tableName, java.lang.String... columnNames) throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("id", java.lang.Long.class, null, null, false));
        for (java.lang.String column : columnNames) {
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(column, java.lang.String.class, 20000, null, true));
        }
        dbAccessor.createTable(tableName, columns, "id");
    }

    @org.junit.Test
    public void testDbType() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.DBAccessor.DbType.H2, dbAccessor.getDbType());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testAlterColumn() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.sql.ResultSet rs;
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo fromColumn;
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo toColumn;
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        final java.lang.String dataString = "Data for inserting column.";
        toColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name", java.lang.String.class, 500, null, true);
        statement.execute(java.lang.String.format("INSERT INTO %s(id, name) VALUES (1, '%s')", tableName, dataString));
        dbAccessor.alterColumn(tableName, toColumn);
        rs = statement.executeQuery(java.lang.String.format("SELECT name FROM %s", tableName));
        while (rs.next()) {
            java.sql.ResultSetMetaData rsm = rs.getMetaData();
            org.junit.Assert.assertEquals(rs.getString(toColumn.getName()), dataString);
            org.junit.Assert.assertEquals(rsm.getColumnTypeName(1), "VARCHAR");
            org.junit.Assert.assertEquals(rsm.getColumnDisplaySize(1), 500);
        } 
        rs.close();
        toColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name", java.sql.Clob.class, 999, null, true);
        dbAccessor.alterColumn(tableName, toColumn);
        rs = statement.executeQuery(java.lang.String.format("SELECT name FROM %s", tableName));
        while (rs.next()) {
            java.sql.ResultSetMetaData rsm = rs.getMetaData();
            java.sql.Clob clob = rs.getClob(toColumn.getName());
            org.junit.Assert.assertEquals(dataString, clob.getSubString(1, ((int) (clob.length()))));
            org.junit.Assert.assertEquals("CLOB", rsm.getColumnTypeName(1));
        } 
        rs.close();
        toColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name_blob_to_clob", java.sql.Clob.class, 567, null, true);
        fromColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name_blob_to_clob", byte[].class, 20000, null, true);
        dbAccessor.addColumn(tableName, fromColumn);
        java.lang.String sql = java.lang.String.format("insert into %s(id, name_blob_to_clob) values (2, ?)", tableName);
        java.sql.PreparedStatement preparedStatement = dbAccessor.getConnection().prepareStatement(sql);
        preparedStatement.setBinaryStream(1, new java.io.ByteArrayInputStream(dataString.getBytes()), dataString.getBytes().length);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        dbAccessor.alterColumn(tableName, toColumn);
        rs = statement.executeQuery(java.lang.String.format("SELECT name_blob_to_clob FROM %s WHERE id=2", tableName));
        while (rs.next()) {
            java.sql.ResultSetMetaData rsm = rs.getMetaData();
            java.sql.Clob clob = rs.getClob(toColumn.getName());
            org.junit.Assert.assertEquals(clob.getSubString(1, ((int) (clob.length()))), dataString);
            org.junit.Assert.assertEquals(rsm.getColumnTypeName(1), "CLOB");
            org.junit.Assert.assertEquals(rsm.getColumnDisplaySize(1), 567);
        } 
        rs.close();
        toColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name_blob_to_clob", char[].class, 1500, null, true);
        dbAccessor.alterColumn(tableName, toColumn);
        rs = statement.executeQuery(java.lang.String.format("SELECT name_blob_to_clob FROM %s WHERE id=2", tableName));
        while (rs.next()) {
            java.sql.ResultSetMetaData rsm = rs.getMetaData();
            java.sql.Clob clob = rs.getClob(toColumn.getName());
            org.junit.Assert.assertEquals(clob.getSubString(1, ((int) (clob.length()))), dataString);
            org.junit.Assert.assertEquals(rsm.getColumnTypeName(1), "CLOB");
            org.junit.Assert.assertEquals(rsm.getColumnDisplaySize(1), 1500);
        } 
        rs.close();
        dbAccessor.dropTable(tableName);
    }

    @org.junit.Test
    public void testCreateTable() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(java.lang.String.format("insert into %s(id, name) values(1,'hello')", tableName));
        java.sql.ResultSet resultSet = statement.executeQuery(java.lang.String.format("select * from %s", tableName));
        int count = 0;
        while (resultSet.next()) {
            org.junit.Assert.assertEquals(resultSet.getString("name"), "hello");
            count++;
        } 
        org.junit.Assert.assertEquals(count, 1);
    }

    @org.junit.Test
    public void testAddFKConstraint() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fid", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fname", java.lang.String.class, null, null, false));
        java.lang.String foreignTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        dbAccessor.createTable(foreignTableName, columns, "fid");
        dbAccessor.addFKConstraint(foreignTableName, "MYFKCONSTRAINT", "fid", tableName, "id", false);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(("insert into " + tableName) + "(id, name) values(1,'hello')");
        statement.execute(("insert into " + foreignTableName) + "(fid, fname) values(1,'howdy')");
        java.sql.ResultSet resultSet = statement.executeQuery("select * from " + foreignTableName);
        int count = 0;
        while (resultSet.next()) {
            org.junit.Assert.assertEquals(resultSet.getString("fname"), "howdy");
            count++;
        } 
        resultSet.close();
        org.junit.Assert.assertEquals(count, 1);
        exception.expect(java.sql.SQLException.class);
        exception.expectMessage(org.junit.matchers.JUnitMatchers.containsString("MYFKCONSTRAINT"));
        dbAccessor.executeQuery("DELETE FROM " + tableName);
    }

    @org.junit.Test
    public void testAddPKConstraint() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("id", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("sid", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("data", char[].class, null, null, true));
        dbAccessor.createTable(tableName, columns);
        dbAccessor.addPKConstraint(tableName, "PK_sid", "sid");
        try {
        } finally {
            dbAccessor.dropTable(tableName);
        }
    }

    @org.junit.Test
    public void testAddColumn() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo dbColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("description", java.lang.String.class, null, null, true);
        dbAccessor.addColumn(tableName, dbColumnInfo);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(("update " + tableName) + " set description = 'blah' where id = 1");
        java.sql.ResultSet resultSet = statement.executeQuery("select description from " + tableName);
        while (resultSet.next()) {
            org.junit.Assert.assertEquals(resultSet.getString("description"), "blah");
        } 
        resultSet.close();
    }

    @org.junit.Test
    public void testUpdateTable() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        dbAccessor.updateTable(tableName, "name", "blah", "where id = 1");
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery("select name from " + tableName);
        while (resultSet.next()) {
            org.junit.Assert.assertEquals(resultSet.getString("name"), "blah");
        } 
        resultSet.close();
    }

    @org.junit.Test
    public void testTableHasFKConstraint() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fid", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fname", java.lang.String.class, null, null, false));
        java.lang.String foreignTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        dbAccessor.createTable(foreignTableName, columns, "fid");
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(((("ALTER TABLE " + foreignTableName) + " ADD CONSTRAINT FK_test FOREIGN KEY (fid) REFERENCES ") + tableName) + " (id)");
        junit.framework.Assert.assertTrue(dbAccessor.tableHasForeignKey(foreignTableName, tableName, "fid", "id"));
    }

    @org.junit.Test
    public void testGetCheckedForeignKey() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fid", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fname", java.lang.String.class, null, null, false));
        java.lang.String foreignTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        dbAccessor.createTable(foreignTableName, columns, "fid");
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(((("ALTER TABLE " + foreignTableName) + " ADD CONSTRAINT FK_test1 FOREIGN KEY (fid) REFERENCES ") + tableName) + " (id)");
        junit.framework.Assert.assertEquals("FK_TEST1", dbAccessor.getCheckedForeignKey(foreignTableName, "fk_test1"));
    }

    @org.junit.Test
    public void getCheckedForeignKeyReferencingUniqueKey() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        statement.execute(java.lang.String.format("ALTER TABLE %s ADD CONSTRAINT UC_name UNIQUE (%s)", tableName, "name"));
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fid", java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("fname", java.lang.String.class, null, null, false));
        java.lang.String foreignTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        dbAccessor.createTable(foreignTableName, columns);
        statement = dbAccessor.getConnection().createStatement();
        statement.execute(java.lang.String.format("ALTER TABLE %s ADD CONSTRAINT FK_name FOREIGN KEY (%s) REFERENCES %s (%s)", foreignTableName, "fname", tableName, "name"));
        junit.framework.Assert.assertEquals("FK_NAME", dbAccessor.getCheckedForeignKey(foreignTableName, "fk_name"));
    }

    @org.junit.Test
    public void testTableExists() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        statement.execute(("Create table " + tableName) + " (id VARCHAR(255))");
        junit.framework.Assert.assertTrue(dbAccessor.tableExists(tableName));
    }

    @org.junit.Test
    public void testTableExistsMultipleSchemas() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        createTableUnderNewSchema(dbAccessor, tableName);
        junit.framework.Assert.assertTrue(dbAccessor.tableExists(tableName));
    }

    @org.junit.Test
    public void testColumnExists() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        junit.framework.Assert.assertTrue(dbAccessor.tableHasColumn(tableName, "time"));
    }

    @org.junit.Test
    public void testColumnExistsMultipleSchemas() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        createTableUnderNewSchema(dbAccessor, tableName);
        junit.framework.Assert.assertTrue(dbAccessor.tableHasColumn(tableName, "id"));
    }

    @org.junit.Test
    public void testColumnsExistsMultipleSchemas() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        createTableUnderNewSchema(dbAccessor, tableName);
        junit.framework.Assert.assertTrue(dbAccessor.tableHasColumn(tableName, "id", "time"));
    }

    @org.junit.Test
    public void testRenameColumn() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        dbAccessor.executeQuery(("insert into " + tableName) + "(id, name, time) values(1, 'Bob', 1234567)");
        dbAccessor.renameColumn(tableName, "time", new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("new_time", java.lang.Long.class, 0, null, true));
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery(("select new_time from " + tableName) + " where id=1");
        int count = 0;
        while (resultSet.next()) {
            count++;
            long newTime = resultSet.getLong("new_time");
            org.junit.Assert.assertEquals(newTime, 1234567L);
        } 
        org.junit.Assert.assertEquals(count, 1);
    }

    @org.junit.Test
    public void testModifyColumn() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        dbAccessor.executeQuery(("insert into " + tableName) + "(id, name, time) values(1, 'Bob', 1234567)");
        dbAccessor.alterColumn(tableName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("name", java.lang.String.class, 25000));
    }

    @org.junit.Test
    public void testAddColumnWithDefault() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        dbAccessor.executeQuery(("insert into " + tableName) + "(id, name, time) values(1, 'Bob', 1234567)");
        dbAccessor.addColumn(tableName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("test", java.lang.String.class, 1000, "test", false));
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery("select * from " + tableName);
        int count = 0;
        while (resultSet.next()) {
            org.junit.Assert.assertEquals(resultSet.getString("test"), "test");
            count++;
        } 
        org.junit.Assert.assertEquals(count, 1);
    }

    @org.junit.Test
    public void testDBSession() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        dbAccessor.executeQuery(("insert into " + tableName) + "(id, name, time) values(1, 'Bob', 1234567)");
        org.eclipse.persistence.sessions.DatabaseSession databaseSession = dbAccessor.getNewDatabaseSession();
        databaseSession.login();
        java.util.Vector vector = databaseSession.executeSQL(("select * from " + tableName) + " where id=1");
        org.junit.Assert.assertEquals(vector.size(), 1);
        java.util.Map map = ((java.util.Map) (vector.get(0)));
        org.junit.Assert.assertEquals("Bob", map.get("name".toUpperCase()));
        databaseSession.logout();
    }

    @org.junit.Test
    public void testGetColumnType() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.junit.Assert.assertEquals(java.sql.Types.BIGINT, dbAccessor.getColumnType(tableName, "id"));
        org.junit.Assert.assertEquals(java.sql.Types.VARCHAR, dbAccessor.getColumnType(tableName, "name"));
    }

    @org.junit.Test
    public void testSetNullable() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo dbColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("isNullable", java.lang.String.class, 1000, "test", false);
        dbAccessor.addColumn(tableName, dbColumnInfo);
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery("SELECT isNullable FROM " + tableName);
        java.sql.ResultSetMetaData rsmd = resultSet.getMetaData();
        org.junit.Assert.assertEquals(java.sql.ResultSetMetaData.columnNoNulls, rsmd.isNullable(1));
        statement.close();
        dbAccessor.setColumnNullable(tableName, dbColumnInfo, true);
        statement = dbAccessor.getConnection().createStatement();
        resultSet = statement.executeQuery("SELECT isNullable FROM " + tableName);
        rsmd = resultSet.getMetaData();
        org.junit.Assert.assertEquals(java.sql.ResultSetMetaData.columnNullable, rsmd.isNullable(1));
        statement.close();
        dbAccessor.setColumnNullable(tableName, dbColumnInfo, false);
        statement = dbAccessor.getConnection().createStatement();
        resultSet = statement.executeQuery("SELECT isNullable FROM " + tableName);
        rsmd = resultSet.getMetaData();
        org.junit.Assert.assertEquals(java.sql.ResultSetMetaData.columnNoNulls, rsmd.isNullable(1));
        statement.close();
    }

    private void createTableUnderNewSchema(org.apache.ambari.server.orm.DBAccessorImpl dbAccessor, java.lang.String tableName) throws java.sql.SQLException {
        java.sql.Statement schemaCreation = dbAccessor.getConnection().createStatement();
        java.lang.String schemaName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeSchamaName();
        schemaCreation.execute("create schema " + schemaName);
        java.sql.Statement customSchemaTableCreation = dbAccessor.getConnection().createStatement();
        customSchemaTableCreation.execute(toString().format("Create table %s.%s (id int, time int)", schemaName, tableName));
    }

    @org.junit.Test
    public void testDefaultColumnConstraintOnAddColumn() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName().toUpperCase();
        java.lang.String columnName = "COLUMN_WITH_DEFAULT_VALUE";
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo dbColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(columnName, java.lang.String.class, 32, "foo", false);
        dbAccessor.addColumn(tableName, dbColumnInfo);
        java.lang.String schema = null;
        java.sql.Connection connection = dbAccessor.getConnection();
        java.sql.DatabaseMetaData databaseMetadata = connection.getMetaData();
        java.sql.ResultSet schemaResultSet = databaseMetadata.getSchemas();
        if (schemaResultSet.next()) {
            schema = schemaResultSet.getString(1);
        }
        schemaResultSet.close();
        java.lang.String columnDefaultVal = null;
        java.sql.ResultSet rs = databaseMetadata.getColumns(null, schema, tableName, columnName);
        if (rs.next()) {
            columnDefaultVal = rs.getString("COLUMN_DEF");
        }
        rs.close();
        org.junit.Assert.assertEquals("'foo'", columnDefaultVal);
    }

    @org.junit.Test
    public void testMoveColumnToAnotherTable() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String sourceTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        java.lang.String targetTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        int testRowAmount = 10;
        createMyTable(sourceTableName, "col1", "col2");
        createMyTable(targetTableName, "col1");
        for (java.lang.Integer i = 0; i < testRowAmount; i++) {
            dbAccessor.insertRow(sourceTableName, new java.lang.String[]{ "id", "col1", "col2" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'source,1,%s'", i), java.lang.String.format("'source,2,%s'", i) }, false);
            dbAccessor.insertRow(targetTableName, new java.lang.String[]{ "id", "col1" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'target,1,%s'", i) }, false);
        }
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col2", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col2", java.lang.String.class, null, null, false);
        dbAccessor.moveColumnToAnotherTable(sourceTableName, sourceColumn, "id", targetTableName, targetColumn, "id", "initial");
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery(("SELECT col2 FROM " + targetTableName) + " ORDER BY col2");
        org.junit.Assert.assertNotNull(resultSet);
        java.util.List<java.lang.String> response = new java.util.LinkedList<>();
        while (resultSet.next()) {
            response.add(resultSet.getString(1));
        } 
        org.junit.Assert.assertEquals(testRowAmount, response.toArray().length);
        int i = 0;
        for (java.lang.String row : response) {
            org.junit.Assert.assertEquals(java.lang.String.format("source,2,%s", i), row);
            i++;
        }
    }

    @org.junit.Test
    public void testCopyColumnToAnotherTable() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String sourceTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        java.lang.String targetTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        int testRowAmount = 10;
        createMyTable(sourceTableName, "col1", "col2", "col3", "col4", "col5");
        createMyTable(targetTableName, "col1", "col2", "col3");
        for (java.lang.Integer i = 0; i < testRowAmount; i++) {
            dbAccessor.insertRow(sourceTableName, new java.lang.String[]{ "id", "col1", "col2", "col3", "col4", "col5" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'1,%s'", i), java.lang.String.format("'2,%s'", i * 2), java.lang.String.format("'3,%s'", i * 3), java.lang.String.format("'4,%s'", i * 4), java.lang.String.format("'%s'", (i * 5) % 2) }, false);
            dbAccessor.insertRow(targetTableName, new java.lang.String[]{ "id", "col1", "col2", "col3" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'1,%s'", i), java.lang.String.format("'2,%s'", i * 2), java.lang.String.format("'3,%s'", i * 3) }, false);
        }
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col4", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col4", java.lang.String.class, null, null, false);
        dbAccessor.copyColumnToAnotherTable(sourceTableName, sourceColumn, "id", "col1", "col2", targetTableName, targetColumn, "id", "col1", "col2", "col5", "0", "initial");
        java.sql.Statement statement = dbAccessor.getConnection().createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery(("SELECT col4 FROM " + targetTableName) + " ORDER BY id");
        org.junit.Assert.assertNotNull(resultSet);
        java.util.List<java.lang.String> response = new java.util.LinkedList<>();
        while (resultSet.next()) {
            response.add(resultSet.getString(1));
        } 
        org.junit.Assert.assertEquals(testRowAmount, response.toArray().length);
        for (java.lang.String row : response) {
            java.lang.System.out.println(row);
        }
        int i = 0;
        for (java.lang.String row : response) {
            if ((i % 2) == 0) {
                org.junit.Assert.assertEquals(java.lang.String.format("4,%s", i * 4), row);
            } else {
                org.junit.Assert.assertEquals("initial", row);
            }
            i++;
        }
    }

    @org.junit.Test
    public void testGetIntColumnValues() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String sourceTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        int testRowAmount = 10;
        createMyTable(sourceTableName, "col1", "col2", "col3", "col4", "col5");
        for (java.lang.Integer i = 0; i < testRowAmount; i++) {
            dbAccessor.insertRow(sourceTableName, new java.lang.String[]{ "id", "col1", "col2", "col3", "col4", "col5" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'1,%s'", i), java.lang.String.format("'2,%s'", i * 2), java.lang.String.format("'3,%s'", i * 3), java.lang.String.format("'4,%s'", i * 4), java.lang.String.format("'%s'", (i * 5) % 2) }, false);
        }
        java.util.List<java.lang.Integer> idList = dbAccessor.getIntColumnValues(sourceTableName, "id", new java.lang.String[]{ "col1", "col5" }, new java.lang.String[]{ "1,0", "0" }, false);
        org.junit.Assert.assertEquals(idList.size(), 1);
        org.junit.Assert.assertEquals(idList.get(0), java.lang.Integer.valueOf(0));
        idList = dbAccessor.getIntColumnValues(sourceTableName, "id", new java.lang.String[]{ "col5" }, new java.lang.String[]{ "0" }, false);
        org.junit.Assert.assertEquals(idList.size(), 5);
        int i = 0;
        for (java.lang.Integer id : idList) {
            org.junit.Assert.assertEquals(id, java.lang.Integer.valueOf(i * 2));
            i++;
        }
    }

    @org.junit.Test
    public void testMoveNonexistentColumnIsNoop() throws java.lang.Exception {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        java.lang.String sourceTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        java.lang.String targetTableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        int testRowAmount = 10;
        createMyTable(sourceTableName, "col1");
        createMyTable(targetTableName, "col1", "col2");
        for (java.lang.Integer i = 0; i < testRowAmount; i++) {
            dbAccessor.insertRow(sourceTableName, new java.lang.String[]{ "id", "col1" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'source,1,%s'", i) }, false);
            dbAccessor.insertRow(targetTableName, new java.lang.String[]{ "id", "col1", "col2" }, new java.lang.String[]{ i.toString(), java.lang.String.format("'target,1,%s'", i), java.lang.String.format("'target,2,%s'", i) }, false);
        }
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col2", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col2", java.lang.String.class, null, null, false);
        dbAccessor.moveColumnToAnotherTable(sourceTableName, sourceColumn, "id", targetTableName, targetColumn, "id", "initial");
    }

    @org.junit.Test
    public void testDbColumnInfoEqualsAndHash() {
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo column1 = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo equalsColumn1 = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1Name = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col1", java.lang.String.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1Type = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.Integer.class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1Length = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, 10, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1DefaultValue = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, null, "default", false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1DefaultValueEmptyString = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, null, "", false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo notEqualsColumn1Nullable = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo("col", java.lang.String.class, null, null, true);
        org.junit.Assert.assertTrue(column1.hashCode() == equalsColumn1.hashCode());
        org.junit.Assert.assertFalse(column1.hashCode() == notEqualsColumn1Name.hashCode());
        org.junit.Assert.assertFalse(column1.hashCode() == notEqualsColumn1Type.hashCode());
        org.junit.Assert.assertFalse(column1.hashCode() == notEqualsColumn1Length.hashCode());
        org.junit.Assert.assertFalse(column1.hashCode() == notEqualsColumn1DefaultValue.hashCode());
        org.junit.Assert.assertTrue(column1.hashCode() == notEqualsColumn1DefaultValueEmptyString.hashCode());
        org.junit.Assert.assertFalse(column1.hashCode() == notEqualsColumn1Nullable.hashCode());
        org.junit.Assert.assertTrue(column1.equals(equalsColumn1));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1Name));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1Type));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1Length));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1DefaultValue));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1DefaultValueEmptyString));
        org.junit.Assert.assertFalse(column1.equals(notEqualsColumn1Nullable));
    }

    @org.junit.Test
    public void testFromSqlTypeToClass() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        java.lang.String columnName = "col1";
        createMyTable(tableName, columnName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo = dbAccessor.getColumnInfo(tableName, columnName);
        org.junit.Assert.assertEquals(columnName.toUpperCase(), columnInfo.getName());
        org.junit.Assert.assertEquals(java.lang.String.class, columnInfo.getType());
    }

    @org.junit.Test
    public void testBuildQuery() throws java.lang.Exception {
        java.lang.String tableName = org.apache.ambari.server.orm.DBAccessorImplTest.getFreeTableName();
        createMyTable(tableName);
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        org.junit.Assert.assertEquals(java.lang.String.format("SELECT id FROM %s WHERE name='value1'", tableName), dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id" }, new java.lang.String[]{ "name" }, new java.lang.String[]{ "value1" }));
        org.junit.Assert.assertEquals(java.lang.String.format("SELECT id FROM %s WHERE name='value1' AND time='100'", tableName), dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id" }, new java.lang.String[]{ "name", "time" }, new java.lang.String[]{ "value1", "100" }));
        org.junit.Assert.assertEquals(java.lang.String.format("SELECT id, name FROM %s WHERE time='100'", tableName), dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id", "name" }, new java.lang.String[]{ "time" }, new java.lang.String[]{ "100" }));
        org.junit.Assert.assertEquals(java.lang.String.format("SELECT id, name, time FROM %s", tableName), dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id", "name", "time" }, null, null));
        try {
            dbAccessor.buildQuery("invalid_table_name", new java.lang.String[]{ "id", "name" }, new java.lang.String[]{ "time" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due to bad table name");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            dbAccessor.buildQuery(tableName, new java.lang.String[]{ "invalid_column_name" }, new java.lang.String[]{ "time" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due to bad column name");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id" }, new java.lang.String[]{ "invalid_column_name" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due to bad column name");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            dbAccessor.buildQuery(tableName, new java.lang.String[]{  }, new java.lang.String[]{ "name" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due missing select columns");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            dbAccessor.buildQuery(tableName, null, new java.lang.String[]{ "name" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due missing select columns");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            dbAccessor.buildQuery(tableName, new java.lang.String[]{ "id" }, new java.lang.String[]{ "name", "time" }, new java.lang.String[]{ "100" });
            org.junit.Assert.fail("Expected IllegalArgumentException due mismatch condition column and value arrays");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    @org.junit.Test
    public void escapesEnumValue() {
        org.eclipse.persistence.platform.database.DatabasePlatform platform = EasyMock.createNiceMock(org.eclipse.persistence.platform.database.DatabasePlatform.class);
        java.lang.Object value = org.apache.ambari.server.state.State.UNKNOWN;
        EasyMock.expect(platform.convertToDatabaseType(value)).andReturn(value).anyTimes();
        EasyMock.reset(platform);
        org.junit.Assert.assertEquals(("'" + value) + "'", org.apache.ambari.server.orm.DBAccessorImpl.escapeParameter(value, platform));
    }

    @org.junit.Test
    public void escapesString() {
        org.eclipse.persistence.platform.database.DatabasePlatform platform = EasyMock.createNiceMock(org.eclipse.persistence.platform.database.DatabasePlatform.class);
        java.lang.Object value = "hello, world";
        EasyMock.expect(platform.convertToDatabaseType(value)).andReturn(value).anyTimes();
        EasyMock.reset(platform);
        org.junit.Assert.assertEquals(("'" + value) + "'", org.apache.ambari.server.orm.DBAccessorImpl.escapeParameter(value, platform));
    }

    @org.junit.Test
    public void doesNotEscapeNumbers() {
        org.eclipse.persistence.platform.database.DatabasePlatform platform = EasyMock.createNiceMock(org.eclipse.persistence.platform.database.DatabasePlatform.class);
        java.lang.Object value = 123;
        EasyMock.expect(platform.convertToDatabaseType(value)).andReturn(value).anyTimes();
        EasyMock.reset(platform);
        org.junit.Assert.assertEquals("123", org.apache.ambari.server.orm.DBAccessorImpl.escapeParameter(value, platform));
    }
}