package org.apache.ambari.server.security.authentication;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
@com.google.inject.Singleton
public class AmbariAuthenticationEventHandlerImpl implements org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.audit.AuditLogger auditLogger;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.PermissionHelper permissionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.Users users;

    @java.lang.Override
    public void onSuccessfulAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.springframework.security.core.Authentication result) {
        java.lang.String username = (result == null) ? null : result.getName();
        if (auditLogger.isEnabled()) {
            org.apache.ambari.server.audit.event.AuditEvent loginSucceededAuditEvent = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(servletRequest)).withUserName(username).withProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName(result)).withTimestamp(java.lang.System.currentTimeMillis()).withRoles(permissionHelper.getPermissionLabels(result)).build();
            auditLogger.log(loginSucceededAuditEvent);
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
            org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.LOG.debug("Successfully authenticated {}", username);
            users.clearConsecutiveAuthenticationFailures(username);
        } else {
            org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.LOG.warn("Successfully authenticated an unknown user");
        }
    }

    @java.lang.Override
    public void onUnsuccessfulAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.apache.ambari.server.security.authentication.AmbariAuthenticationException cause) {
        java.lang.String username;
        java.lang.String message;
        java.lang.String logMessage;
        java.lang.Integer consecutiveFailures = null;
        boolean incrementFailureCount;
        if (cause == null) {
            username = null;
            message = "Unknown cause";
            incrementFailureCount = false;
        } else {
            username = cause.getUsername();
            message = cause.getLocalizedMessage();
            incrementFailureCount = cause.isCredentialFailure();
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
            if (incrementFailureCount && filter.shouldIncrementFailureCount()) {
                consecutiveFailures = users.incrementConsecutiveAuthenticationFailures(username);
                if (consecutiveFailures == null) {
                    logMessage = java.lang.String.format("Failed to authenticate %s: The user does not exist in the Ambari database", username);
                } else {
                    logMessage = java.lang.String.format("Failed to authenticate %s (attempt #%d): %s", username, consecutiveFailures, message);
                }
            } else {
                logMessage = java.lang.String.format("Failed to authenticate %s: %s", username, message);
            }
        } else {
            logMessage = java.lang.String.format("Failed to authenticate an unknown user: %s", message);
        }
        if (org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.LOG.debug(logMessage, cause);
        } else {
            org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandlerImpl.LOG.info(logMessage);
        }
        if (auditLogger.isEnabled()) {
            org.apache.ambari.server.audit.event.AuditEvent loginFailedAuditEvent = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(servletRequest)).withTimestamp(java.lang.System.currentTimeMillis()).withReasonOfFailure(message).withConsecutiveFailures(consecutiveFailures).withUserName(username).withProxyUserName(null).build();
            auditLogger.log(loginFailedAuditEvent);
        }
    }

    @java.lang.Override
    public void beforeAttemptAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) {
    }
}