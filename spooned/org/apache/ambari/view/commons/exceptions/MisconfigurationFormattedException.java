package org.apache.ambari.view.commons.exceptions;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
public class MisconfigurationFormattedException extends javax.ws.rs.WebApplicationException {
    private static final int STATUS = 500;

    private static final java.lang.String message = "Parameter \"%s\" is set to null";

    public MisconfigurationFormattedException(java.lang.String name) {
        super(org.apache.ambari.view.commons.exceptions.MisconfigurationFormattedException.errorEntity(name));
    }

    protected static javax.ws.rs.core.Response errorEntity(java.lang.String name) {
        java.util.HashMap<java.lang.String, java.lang.Object> response = new java.util.HashMap<java.lang.String, java.lang.Object>();
        response.put("message", java.lang.String.format(org.apache.ambari.view.commons.exceptions.MisconfigurationFormattedException.message, name));
        response.put("trace", null);
        response.put("status", org.apache.ambari.view.commons.exceptions.MisconfigurationFormattedException.STATUS);
        return javax.ws.rs.core.Response.status(org.apache.ambari.view.commons.exceptions.MisconfigurationFormattedException.STATUS).entity(new org.json.simple.JSONObject(response)).type(MediaType.APPLICATION_JSON).build();
    }
}