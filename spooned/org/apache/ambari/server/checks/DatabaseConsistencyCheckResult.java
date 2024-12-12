package org.apache.ambari.server.checks;
public enum DatabaseConsistencyCheckResult {

    DB_CHECK_SUCCESS,
    DB_CHECK_WARNING,
    DB_CHECK_ERROR;
    public boolean isErrorOrWarning() {
        return (this == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING) || (this == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_ERROR);
    }

    public boolean isError() {
        return this == org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_ERROR;
    }
}