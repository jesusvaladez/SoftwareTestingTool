package org.apache.ambari.view.pig.utils;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
public class MisconfigurationFormattedException extends javax.ws.rs.WebApplicationException {
    private static final int STATUS = 500;

    private static final java.lang.String message = "Parameter \"%s\" is set to null";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.class);

    public MisconfigurationFormattedException(java.lang.String name) {
        super(org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.errorEntity(name));
    }

    protected static javax.ws.rs.core.Response errorEntity(java.lang.String name) {
        java.util.HashMap<java.lang.String, java.lang.Object> response = new java.util.HashMap<java.lang.String, java.lang.Object>();
        response.put("message", java.lang.String.format(org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.message, name));
        response.put("trace", null);
        response.put("status", org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.STATUS);
        return javax.ws.rs.core.Response.status(org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.STATUS).entity(new org.json.simple.JSONObject(response)).type(MediaType.APPLICATION_JSON).build();
    }
}