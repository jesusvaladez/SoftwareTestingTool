package org.apache.ambari.view.utils.ambari;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class AmbariApiException extends java.lang.RuntimeException {
    public AmbariApiException(java.lang.String message) {
        super(message);
    }

    public AmbariApiException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public javax.ws.rs.core.Response toEntity() {
        java.util.HashMap<java.lang.String, java.lang.Object> respJson = new java.util.HashMap<>();
        respJson.put("trace", getCause());
        respJson.put("message", getMessage());
        respJson.put("status", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        return javax.ws.rs.core.Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respJson).type(MediaType.APPLICATION_JSON).build();
    }
}