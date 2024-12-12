package org.apache.ambari.server.api;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class AmbariCsrfProtectionFilter implements com.sun.jersey.spi.container.ContainerRequestFilter {
    private static final java.util.Set<java.lang.String> IGNORED_METHODS;

    private static final java.lang.String CSRF_HEADER = "X-Requested-By";

    private static final java.lang.String ERROR_MESSAGE = ("CSRF protection is turned on. " + org.apache.ambari.server.api.AmbariCsrfProtectionFilter.CSRF_HEADER) + " HTTP header is required.";

    private static final org.apache.ambari.server.api.services.serializers.JsonSerializer JSON_SERIALIZER = new org.apache.ambari.server.api.services.serializers.JsonSerializer();

    static {
        java.util.HashSet<java.lang.String> methods = new java.util.HashSet<>();
        methods.add("GET");
        methods.add("OPTIONS");
        methods.add("HEAD");
        IGNORED_METHODS = java.util.Collections.unmodifiableSet(methods);
    }

    @java.lang.Override
    public com.sun.jersey.spi.container.ContainerRequest filter(com.sun.jersey.spi.container.ContainerRequest containerRequest) {
        if ((!org.apache.ambari.server.api.AmbariCsrfProtectionFilter.IGNORED_METHODS.contains(containerRequest.getMethod())) && (!containerRequest.getRequestHeaders().containsKey(org.apache.ambari.server.api.AmbariCsrfProtectionFilter.CSRF_HEADER))) {
            throw new javax.ws.rs.WebApplicationException(javax.ws.rs.core.Response.status(Response.Status.BAD_REQUEST).entity(org.apache.ambari.server.api.AmbariCsrfProtectionFilter.JSON_SERIALIZER.serializeError(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, org.apache.ambari.server.api.AmbariCsrfProtectionFilter.ERROR_MESSAGE))).type(MediaType.TEXT_PLAIN_TYPE).build());
        }
        return containerRequest;
    }
}