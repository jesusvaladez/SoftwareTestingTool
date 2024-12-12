package org.apache.ambari.server;
public class DBConnectionVerification {
    public static void main(java.lang.String[] args) throws java.lang.Exception {
        java.lang.String url = args[0];
        java.lang.String username = args[1];
        java.lang.String password = args[2];
        java.lang.String driver = args[3];
        java.sql.Connection conn = null;
        try {
            java.lang.Class.forName(driver);
            if (url.contains("integratedSecurity=true")) {
                conn = java.sql.DriverManager.getConnection(url);
            } else {
                conn = java.sql.DriverManager.getConnection(url, username, password);
            }
            java.lang.System.out.println("Connected to DB Successfully!");
        } catch (java.lang.Throwable e) {
            java.lang.System.out.println("ERROR: Unable to connect to the DB. Please check DB connection properties.");
            java.lang.System.out.println(e);
            java.lang.System.exit(1);
        } finally {
            conn.close();
        }
    }
}