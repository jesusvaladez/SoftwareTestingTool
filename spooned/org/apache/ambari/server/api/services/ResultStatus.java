package org.apache.ambari.server.api.services;
public class ResultStatus {
    public enum STATUS {

        OK(200, "OK", false),
        CREATED(201, "Created", false),
        ACCEPTED(202, "Accepted", false),
        CONFLICT(409, "Resource Conflict", true),
        NOT_FOUND(404, "Not Found", true),
        BAD_REQUEST(400, "Bad Request", true),
        UNAUTHORIZED(401, "Unauthorized", true),
        FORBIDDEN(403, "Forbidden", true),
        SERVER_ERROR(500, "Internal Server Error", true);
        private int m_code;

        private java.lang.String m_desc;

        private boolean m_isErrorState;

        STATUS(int code, java.lang.String description, boolean isErrorState) {
            m_code = code;
            m_desc = description;
            m_isErrorState = isErrorState;
        }

        public int getStatus() {
            return m_code;
        }

        public java.lang.String getDescription() {
            return m_desc;
        }

        public boolean isErrorState() {
            return m_isErrorState;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return getDescription();
        }
    }

    private org.apache.ambari.server.api.services.ResultStatus.STATUS m_status;

    private java.lang.String m_msg;

    public ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS status, java.lang.String msg) {
        m_status = status;
        m_msg = msg;
    }

    public ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS status) {
        m_status = status;
    }

    public ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS status, java.lang.Exception e) {
        m_status = status;
        m_msg = e.getMessage();
    }

    public org.apache.ambari.server.api.services.ResultStatus.STATUS getStatus() {
        return m_status;
    }

    public int getStatusCode() {
        return m_status.getStatus();
    }

    public boolean isErrorState() {
        return m_status.isErrorState();
    }

    public java.lang.String getMessage() {
        return m_msg;
    }
}