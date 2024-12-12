package org.apache.ambari.server.controller.jdbc;
public class JobHistoryPostgresConnectionFactory implements org.apache.ambari.server.controller.jdbc.ConnectionFactory {
    private static final java.lang.String DEFAULT_HOSTNAME = "localhost";

    private static final java.lang.String DEFAULT_DBNAME = "ambarirca";

    private static final java.lang.String DEFAULT_USERNAME = "mapred";

    private static final java.lang.String DEFAULT_PASSWORD = "mapred";

    private java.lang.String url;

    private java.lang.String username;

    private java.lang.String password;

    public JobHistoryPostgresConnectionFactory() {
        this(org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory.DEFAULT_HOSTNAME, org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory.DEFAULT_DBNAME, org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory.DEFAULT_USERNAME, org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory.DEFAULT_PASSWORD);
    }

    public JobHistoryPostgresConnectionFactory(java.lang.String hostname, java.lang.String dbname, java.lang.String username, java.lang.String password) {
        url = (("jdbc:postgresql://" + hostname) + "/") + dbname;
        this.username = username;
        this.password = password;
        try {
            java.lang.Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.IllegalStateException("Can't load postgresql", e);
        }
    }

    @java.lang.Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        return java.sql.DriverManager.getConnection(url, username, password);
    }
}