package com.microsoft.sqlserver.jdbc;
public class SQLServerDriver implements java.sql.Driver {
    private static final com.microsoft.sqlserver.jdbc.SQLServerDriver singleton = new com.microsoft.sqlserver.jdbc.SQLServerDriver();

    private static java.sql.Connection connection;

    static {
        try {
            java.sql.DriverManager.registerDriver(singleton);
        } catch (java.sql.SQLException e) {
            java.lang.System.out.println("SQLServerDriver.static intializer : can't register driver with manager : " + e);
        }
    }

    private SQLServerDriver() {
    }

    @java.lang.Override
    public java.sql.Connection connect(java.lang.String s, java.util.Properties properties) throws java.sql.SQLException {
        return com.microsoft.sqlserver.jdbc.SQLServerDriver.connection;
    }

    @java.lang.Override
    public boolean acceptsURL(java.lang.String s) throws java.sql.SQLException {
        return true;
    }

    @java.lang.Override
    public java.sql.DriverPropertyInfo[] getPropertyInfo(java.lang.String s, java.util.Properties properties) throws java.sql.SQLException {
        return new java.sql.DriverPropertyInfo[0];
    }

    @java.lang.Override
    public int getMajorVersion() {
        return 1;
    }

    @java.lang.Override
    public int getMinorVersion() {
        return 0;
    }

    @java.lang.Override
    public boolean jdbcCompliant() {
        return true;
    }

    public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
        throw new java.sql.SQLFeatureNotSupportedException();
    }

    public static void setConnection(java.sql.Connection conn) {
        com.microsoft.sqlserver.jdbc.SQLServerDriver.connection = conn;
    }
}