package org.apache.ambari.view.pig.utils;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
public class ServiceFormattedException extends javax.ws.rs.WebApplicationException {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);

    public ServiceFormattedException(java.lang.Throwable e) {
        super(org.apache.ambari.view.pig.utils.ServiceFormattedException.errorEntity(null, e, org.apache.ambari.view.pig.utils.ServiceFormattedException.suggestStatus(e)));
    }

    public ServiceFormattedException(java.lang.String message) {
        super(org.apache.ambari.view.pig.utils.ServiceFormattedException.errorEntity(message, null, org.apache.ambari.view.pig.utils.ServiceFormattedException.suggestStatus(null)));
    }

    public ServiceFormattedException(java.lang.String message, java.lang.Throwable exception) {
        super(org.apache.ambari.view.pig.utils.ServiceFormattedException.errorEntity(message, exception, org.apache.ambari.view.pig.utils.ServiceFormattedException.suggestStatus(exception)));
    }

    public ServiceFormattedException(java.lang.String message, java.lang.Throwable exception, int status) {
        super(org.apache.ambari.view.pig.utils.ServiceFormattedException.errorEntity(message, exception, status));
    }

    private static int suggestStatus(java.lang.Throwable exception) {
        int status = 500;
        if (exception == null) {
            return status;
        }
        if (exception instanceof java.security.AccessControlException) {
            status = 403;
        }
        return status;
    }

    protected static javax.ws.rs.core.Response errorEntity(java.lang.String message, java.lang.Throwable e, int status) {
        java.util.HashMap<java.lang.String, java.lang.Object> response = new java.util.HashMap<java.lang.String, java.lang.Object>();
        java.lang.String trace = null;
        response.put("message", message);
        if (e != null) {
            trace = e.toString() + "\n\n";
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            trace += sw.toString();
            if (message == null) {
                java.lang.String innerMessage = e.getMessage();
                java.lang.String autoMessage;
                if (innerMessage != null) {
                    autoMessage = java.lang.String.format("%s [%s]", innerMessage, e.getClass().getSimpleName());
                } else {
                    autoMessage = e.getClass().getSimpleName();
                }
                response.put("message", autoMessage);
            }
        }
        response.put("trace", trace);
        response.put("status", status);
        if ((message != null) && (status != 404)) {
            org.apache.ambari.view.pig.utils.ServiceFormattedException.LOG.error(message);
        }
        if ((trace != null) && (status != 404)) {
            org.apache.ambari.view.pig.utils.ServiceFormattedException.LOG.error(trace);
        }
        javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.status(status).entity(new org.json.simple.JSONObject(response)).type(MediaType.APPLICATION_JSON);
        return responseBuilder.build();
    }
}