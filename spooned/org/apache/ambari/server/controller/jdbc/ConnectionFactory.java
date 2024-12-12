package org.apache.ambari.server.controller.jdbc;
public interface ConnectionFactory {
    java.sql.Connection getConnection() throws java.sql.SQLException;
}