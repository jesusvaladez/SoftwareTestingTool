package org.apache.ambari.log4j.common.store;
import org.apache.hadoop.util.StringUtils;
import org.apache.log4j.spi.LoggingEvent;
public class DatabaseStore implements org.apache.ambari.log4j.common.LogStore {
    private final java.lang.String database;

    private final java.lang.String user;

    private final java.lang.String password;

    private final org.apache.ambari.log4j.common.LogStoreUpdateProvider updateProvider;

    private final java.lang.String driver;

    private java.sql.Connection connection;

    private boolean initialized;

    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.log4j.common.store.DatabaseStore.class);

    public DatabaseStore(java.lang.String driver, java.lang.String database, java.lang.String user, java.lang.String password, org.apache.ambari.log4j.common.LogStoreUpdateProvider updateProvider) throws java.io.IOException {
        this.initialized = false;
        this.driver = driver;
        try {
            java.lang.Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            java.lang.System.err.println("Can't load driver - " + driver);
            throw new java.lang.RuntimeException("Can't load driver - " + driver);
        }
        this.database = database;
        this.user = (user == null) ? "" : user;
        this.password = (password == null) ? "" : password;
        this.updateProvider = updateProvider;
    }

    @java.lang.Override
    public void persist(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException {
        if (!this.initialized) {
            synchronized(org.apache.ambari.log4j.common.store.DatabaseStore.class) {
                if (!this.initialized) {
                    try {
                        this.connection = java.sql.DriverManager.getConnection(this.database, this.user, this.password);
                    } catch (java.sql.SQLException sqle) {
                        org.apache.ambari.log4j.common.store.DatabaseStore.LOG.debug("Failed to connect to db " + this.database, sqle);
                        java.lang.System.err.println((((((((("Failed to connect to db " + this.database) + " as user ") + this.user) + " password ") + this.password) + " and driver ") + this.driver) + " with ") + org.apache.hadoop.util.StringUtils.stringifyException(sqle));
                        throw new java.io.IOException("Can't connect to database " + this.database, sqle);
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.log4j.common.store.DatabaseStore.LOG.debug("Failed to connect to db " + this.database, e);
                        java.lang.System.err.println((((((((("Failed to connect to db " + this.database) + " as user ") + this.user) + " password ") + this.password) + " and driver ") + this.driver) + " with ") + org.apache.hadoop.util.StringUtils.stringifyException(e));
                        throw new java.lang.RuntimeException("Failed to create database store for " + this.database, e);
                    }
                    this.updateProvider.init(this.connection);
                    this.initialized = true;
                }
            }
        }
        updateProvider.update(originalEvent, parsedEvent);
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        try {
            if (this.initialized && (this.connection != null)) {
                connection.close();
            }
        } catch (java.sql.SQLException sqle) {
            throw new java.io.IOException("Failed to close connection to database " + this.database, sqle);
        }
    }
}