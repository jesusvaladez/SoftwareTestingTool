package org.apache.ambari.funtest.server.tests.db;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
public class DDLCreateIT {
    @org.junit.Test
    public void mysql() {
        org.apache.ambari.funtest.server.tests.db.DDLCreateIT.testSchemaCreate(() -> new org.testcontainers.containers.MariaDBContainer("mariadb:10.2").withConfigurationOverride(null).withInitScript("Ambari-DDL-MySQL-CREATE.sql"));
        org.apache.ambari.funtest.server.tests.db.DDLCreateIT.testSchemaCreate(() -> new org.testcontainers.containers.MySQLContainer("mysql:5.7").withConfigurationOverride(null).withInitScript("Ambari-DDL-MySQL-CREATE.sql"));
    }

    @org.junit.Test
    public void postgres() {
        org.apache.ambari.funtest.server.tests.db.DDLCreateIT.testSchemaCreate(() -> new org.testcontainers.containers.PostgreSQLContainer("postgres:9.6").withInitScript("Ambari-DDL-Postgres-CREATE.sql"));
        org.apache.ambari.funtest.server.tests.db.DDLCreateIT.testSchemaCreate(() -> new org.testcontainers.containers.PostgreSQLContainer("postgres:10").withInitScript("Ambari-DDL-Postgres-CREATE.sql"));
    }

    private static void testSchemaCreate(java.util.function.Supplier<? extends org.testcontainers.containers.JdbcDatabaseContainer> containerSupplier) {
        try (org.testcontainers.containers.JdbcDatabaseContainer container = containerSupplier.get().withPassword(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_PASSWD.getDefaultValue())) {
            container.start();
            java.util.Properties props = new java.util.Properties();
            props.put(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), org.apache.ambari.server.orm.PersistenceType.REMOTE.getValue());
            props.put(org.apache.ambari.server.configuration.Configuration.SERVER_DB_NAME.getKey(), container.getDatabaseName());
            props.put(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getKey(), container.getDriverClassName());
            props.put(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), container.getJdbcUrl().replace("mariadb", "mysql"));
            props.put(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_NAME.getKey(), container.getUsername());
            org.apache.ambari.server.configuration.Configuration config = new org.apache.ambari.server.configuration.Configuration(props);
            org.apache.ambari.server.orm.DBAccessor db = new org.apache.ambari.server.orm.DBAccessorImpl(config);
            org.junit.Assert.assertTrue(db.tableExists("metainfo"));
            org.junit.Assert.assertEquals(new java.lang.Integer(1), db.getIntColumnValues("users", "user_id", new java.lang.String[]{ "user_name" }, new java.lang.String[]{ "admin" }, false).get(0));
        } catch (java.sql.SQLException e) {
            org.junit.Assert.fail(e.getMessage());
        }
    }
}