package org.apache.ambari.server.orm.helpers;
public class ScriptRunner {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.helpers.ScriptRunner.class);

    private static final java.lang.String DEFAULT_DELIMITER = ";";

    private java.sql.Connection connection;

    private boolean stopOnError;

    private boolean autoCommit;

    private java.io.PrintWriter logWriter = new java.io.PrintWriter(java.lang.System.out);

    private java.io.PrintWriter errorLogWriter = new java.io.PrintWriter(java.lang.System.err);

    private java.lang.String delimiter = org.apache.ambari.server.orm.helpers.ScriptRunner.DEFAULT_DELIMITER;

    private boolean fullLineDelimiter = false;

    public ScriptRunner(java.sql.Connection connection, boolean autoCommit, boolean stopOnError) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    public void setDelimiter(java.lang.String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    public void setLogWriter(java.io.PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void setErrorLogWriter(java.io.PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    public void runScript(java.io.Reader reader) throws java.io.IOException, java.sql.SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (java.io.IOException | java.sql.SQLException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    private void runScript(java.sql.Connection conn, java.io.Reader reader) throws java.io.IOException, java.sql.SQLException {
        java.lang.StringBuffer command = null;
        java.sql.Statement statement = null;
        try {
            java.io.LineNumberReader lineReader = new java.io.LineNumberReader(reader);
            java.lang.String line = null;
            statement = conn.createStatement();
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new java.lang.StringBuffer();
                }
                java.lang.String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(trimmedLine);
                } else if (((trimmedLine.length() < 1) || trimmedLine.startsWith("//")) || trimmedLine.startsWith("--")) {
                } else if (((!fullLineDelimiter) && trimmedLine.endsWith(getDelimiter())) || (fullLineDelimiter && trimmedLine.equals(getDelimiter()))) {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    println(command);
                    boolean hasResults = false;
                    if (stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (java.sql.SQLException e) {
                            printlnError("Error executing: " + command);
                            printlnError(e);
                        }
                    }
                    if (autoCommit && (!conn.getAutoCommit())) {
                        conn.commit();
                    }
                    java.sql.ResultSet rs = statement.getResultSet();
                    if (hasResults && (rs != null)) {
                        java.sql.ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            java.lang.String name = md.getColumnLabel(i);
                            print(name + "\t");
                        }
                        println("");
                        while (rs.next()) {
                            for (int i = 0; i < cols; i++) {
                                java.lang.String value = rs.getString(i);
                                print(value + "\t");
                            }
                            println("");
                        } 
                    }
                    command = null;
                    java.lang.Thread.yield();
                } else {
                    command.append(line);
                    command.append(" ");
                }
            } 
            if (!autoCommit) {
                conn.commit();
            }
        } catch (java.sql.SQLException | java.io.IOException e) {
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            if (!autoCommit) {
                conn.rollback();
            }
            if (statement != null) {
                statement.close();
            }
            flush();
        }
    }

    private java.lang.String getDelimiter() {
        return delimiter;
    }

    private void print(java.lang.Object o) {
        if (logWriter != null) {
            java.lang.System.out.print(o);
        }
    }

    private void println(java.lang.Object o) {
        if (logWriter != null) {
            logWriter.println(o);
        }
    }

    private void printlnError(java.lang.Object o) {
        if (errorLogWriter != null) {
            errorLogWriter.println(o);
        }
    }

    private void flush() {
        if (logWriter != null) {
            logWriter.flush();
        }
        if (errorLogWriter != null) {
            errorLogWriter.flush();
        }
    }
}