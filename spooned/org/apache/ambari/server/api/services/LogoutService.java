package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.springframework.security.core.context.SecurityContextHolder;
@org.apache.ambari.server.StaticallyInject
@javax.ws.rs.Path("/logout")
public class LogoutService {
    @com.google.inject.Inject
    private static org.apache.ambari.server.audit.AuditLogger auditLogger;

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response performLogout(@javax.ws.rs.core.Context
    javax.servlet.http.HttpServletRequest servletRequest) {
        auditLog(servletRequest);
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        servletRequest.getSession().invalidate();
        return javax.ws.rs.core.Response.status(Response.Status.OK).build();
    }

    private void auditLog(javax.servlet.http.HttpServletRequest servletRequest) {
        if (!org.apache.ambari.server.api.services.LogoutService.auditLogger.isEnabled()) {
            return;
        }
        org.apache.ambari.server.audit.event.LogoutAuditEvent logoutEvent = org.apache.ambari.server.audit.event.LogoutAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(servletRequest)).withUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName()).withProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName()).build();
        org.apache.ambari.server.api.services.LogoutService.auditLogger.log(logoutEvent);
    }
}