package org.apache.ambari.server.api;
import org.apache.commons.lang.StringUtils;
public class AmbariErrorHandler extends org.eclipse.jetty.server.handler.ErrorHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.AmbariErrorHandler.class);

    private final com.google.gson.Gson gson;

    private org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider;

    @com.google.inject.Inject
    public AmbariErrorHandler(@com.google.inject.name.Named("prettyGson")
    com.google.gson.Gson prettyGson, org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider jwtAuthenticationPropertiesProvider) {
        this.gson = prettyGson;
        this.jwtAuthenticationPropertiesProvider = jwtAuthenticationPropertiesProvider;
    }

    @java.lang.Override
    public void handle(java.lang.String target, org.eclipse.jetty.server.Request baseRequest, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
        org.eclipse.jetty.server.HttpChannel connection = org.eclipse.jetty.server.HttpConnection.getCurrentConnection().getHttpChannel();
        connection.getRequest().setHandled(true);
        response.setContentType(org.eclipse.jetty.http.MimeTypes.Type.TEXT_PLAIN.asString());
        java.util.Map<java.lang.String, java.lang.Object> errorMap = new java.util.LinkedHashMap<>();
        int code = connection.getResponse().getStatus();
        errorMap.put("status", code);
        java.lang.String message = connection.getResponse().getReason();
        if (message == null) {
            message = org.eclipse.jetty.http.HttpStatus.getMessage(code);
        }
        errorMap.put("message", message);
        java.lang.Throwable th = ((java.lang.Throwable) (request.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)));
        if (th != null) {
            if (code == org.eclipse.jetty.http.HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                java.util.UUID requestId = java.util.UUID.randomUUID();
                message = ("Internal server error, please refer the exception by " + requestId) + " in the server log file";
                errorMap.put("message", message);
                org.apache.ambari.server.api.AmbariErrorHandler.LOG.error((message + ", requestURI: ") + request.getRequestURI(), th);
            }
            if (this.isShowStacks()) {
                java.io.StringWriter writer = new java.io.StringWriter();
                writeErrorPageStacks(request, writer);
                errorMap.put("reason:", writer.toString());
            }
        }
        if ((code == javax.servlet.http.HttpServletResponse.SC_FORBIDDEN) || (code == javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED)) {
            org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties jwtProperties = jwtAuthenticationPropertiesProvider.get();
            if ((jwtProperties != null) && jwtProperties.isEnabledForAmbari()) {
                java.lang.String providerUrl = jwtProperties.getAuthenticationProviderUrl();
                java.lang.String originalUrl = jwtProperties.getOriginalUrlQueryParam();
                if (org.apache.commons.lang.StringUtils.isEmpty(providerUrl)) {
                    org.apache.ambari.server.api.AmbariErrorHandler.LOG.warn("The SSO provider URL is not available, forwarding to the SSO provider is not possible");
                } else if (org.apache.commons.lang.StringUtils.isEmpty(originalUrl)) {
                    org.apache.ambari.server.api.AmbariErrorHandler.LOG.warn("The original URL parameter name is not available, forwarding to the SSO provider is not possible");
                } else {
                    errorMap.put("jwtProviderUrl", java.lang.String.format("%s?%s=", providerUrl, originalUrl));
                }
            }
        }
        gson.toJson(errorMap, response.getWriter());
    }

    @java.lang.Override
    protected void writeErrorPageStacks(javax.servlet.http.HttpServletRequest request, java.io.Writer writer) throws java.io.IOException {
        java.lang.Throwable th = ((java.lang.Throwable) (request.getAttribute(javax.servlet.RequestDispatcher.ERROR_EXCEPTION)));
        while (th != null) {
            writer.write("Caused by:\n");
            write(writer, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(th));
            writer.write("\n");
            th = th.getCause();
        } 
    }
}