package org.apache.ambari.view.commons.exceptions;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;
public class ServiceFormattedException extends javax.ws.rs.WebApplicationException {
    public ServiceFormattedException(java.lang.String message, java.lang.Throwable exception) {
        super(org.apache.ambari.view.commons.exceptions.ServiceFormattedException.errorEntity(message, exception, org.apache.ambari.view.commons.exceptions.ServiceFormattedException.suggestStatus(exception)));
    }

    public ServiceFormattedException(java.lang.String message, java.lang.Throwable exception, int status) {
        super(org.apache.ambari.view.commons.exceptions.ServiceFormattedException.errorEntity(message, exception, status));
    }

    private static int suggestStatus(java.lang.Throwable exception) {
        int status = 500;
        if (exception instanceof java.security.AccessControlException) {
            status = 403;
        }
        return status;
    }

    protected static javax.ws.rs.core.Response errorEntity(java.lang.String message, java.lang.Throwable e, int status) {
        java.util.HashMap<java.lang.String, java.lang.Object> response = new java.util.HashMap<java.lang.String, java.lang.Object>();
        response.put("message", message);
        java.lang.String trace = null;
        if (e != null)
            trace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e);

        response.put("trace", trace);
        response.put("status", status);
        return javax.ws.rs.core.Response.status(status).entity(new org.json.simple.JSONObject(response)).type(MediaType.APPLICATION_JSON).build();
    }
}