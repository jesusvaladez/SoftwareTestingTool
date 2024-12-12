package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
@org.springframework.stereotype.Component
public class AmbariAuthorizationFilter implements javax.servlet.Filter {
    private static final java.lang.String REALM_PARAM = "realm";

    private static final java.lang.String DEFAULT_REALM = "AuthFilter";

    private static final java.lang.String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    private static final java.util.regex.Pattern STACK_ADVISOR_REGEX = java.util.regex.Pattern.compile("/api/v[0-9]+/stacks/[^/]+/versions/[^/]+/(validations|recommendations).*");

    public static final java.lang.String API_VERSION_PREFIX = "/api/v[0-9]+";

    public static final java.lang.String VIEWS_CONTEXT_PATH_PREFIX = "/views/";

    private static final java.lang.String VIEWS_CONTEXT_PATH_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_PATH_PREFIX + "([^/]+)/([^/]+)/([^/]+)(.*)";

    private static final java.lang.String VIEWS_CONTEXT_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_PATH_PREFIX + ".*";

    private static final java.lang.String API_USERS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/users.*";

    private static final java.lang.String API_PRIVILEGES_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/privileges.*";

    private static final java.lang.String API_GROUPS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/groups.*";

    private static final java.lang.String API_CLUSTERS_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/(\\w+/?)?";

    private static final java.lang.String API_WIDGET_LAYOUTS_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/widget_layouts.*?";

    private static final java.lang.String API_WIDGET_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/widgets.*";

    private static final java.lang.String API_CLUSTERS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters.*";

    private static final java.lang.String API_VIEWS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/views.*";

    private static final java.lang.String API_AUTH_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/auth";

    private static final java.lang.String API_PERSIST_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/persist.*";

    private static final java.lang.String API_LDAP_SYNC_EVENTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/ldap_sync_events.*";

    private static final java.lang.String API_CREDENTIALS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/credentials.*";

    private static final java.lang.String API_CREDENTIALS_AMBARI_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/credentials/ambari\\..*";

    private static final java.lang.String API_CLUSTER_REQUESTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/requests.*";

    private static final java.lang.String API_CLUSTER_SERVICES_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/services.*";

    private static final java.lang.String API_CLUSTER_ALERT_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/alert.*";

    private static final java.lang.String API_CLUSTER_HOSTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/hosts.*";

    private static final java.lang.String API_CLUSTER_CONFIGURATIONS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/configurations.*";

    private static final java.lang.String API_CLUSTER_COMPONENTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/components.*";

    private static final java.lang.String API_CLUSTER_HOST_COMPONENTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/host_components.*";

    private static final java.lang.String API_CLUSTER_CONFIG_GROUPS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/config_groups.*";

    private static final java.lang.String API_STACK_VERSIONS_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/stacks/.*?/versions/.*";

    private static final java.lang.String API_HOSTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/hosts.*";

    private static final java.lang.String API_ALERT_TARGETS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/alert_targets.*";

    private static final java.lang.String API_BOOTSTRAP_PATTERN_ALL = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/bootstrap.*";

    private static final java.lang.String API_REQUESTS_ALL_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/requests.*";

    private static final java.lang.String API_CLUSTERS_UPGRADES_PATTERN = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VERSION_PREFIX + "/clusters/.*?/upgrades.*";

    protected static final java.lang.String LOGIN_REDIRECT_BASE = "/#/login?targetURI=";

    private final org.apache.ambari.server.security.AmbariEntryPoint entryPoint;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private final org.apache.ambari.server.security.authorization.Users users;

    private final org.apache.ambari.server.audit.AuditLogger auditLogger;

    private final org.apache.ambari.server.security.authorization.PermissionHelper permissionHelper;

    private java.lang.String realm;

    public AmbariAuthorizationFilter(org.apache.ambari.server.security.AmbariEntryPoint entryPoint, org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.security.authorization.Users users, org.apache.ambari.server.audit.AuditLogger auditLogger, org.apache.ambari.server.security.authorization.PermissionHelper permissionHelper) {
        this.entryPoint = entryPoint;
        this.configuration = configuration;
        this.users = users;
        this.auditLogger = auditLogger;
        this.permissionHelper = permissionHelper;
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
        realm = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.getParameterValue(filterConfig, org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.REALM_PARAM, org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.DEFAULT_REALM);
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        javax.servlet.http.HttpServletRequest httpRequest = ((javax.servlet.http.HttpServletRequest) (request));
        javax.servlet.http.HttpServletResponse httpResponse = ((javax.servlet.http.HttpServletResponse) (response));
        java.lang.String requestURI = httpRequest.getRequestURI();
        org.springframework.security.core.context.SecurityContext context = getSecurityContext();
        org.springframework.security.core.Authentication authentication = context.getAuthentication();
        org.apache.ambari.server.audit.event.AuditEvent auditEvent = null;
        if ((authentication == null) || (authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            org.springframework.security.core.Authentication defaultAuthentication = getDefaultAuthentication();
            if (defaultAuthentication != null) {
                context.setAuthentication(defaultAuthentication);
                authentication = defaultAuthentication;
            }
        }
        if (((authentication == null) || (authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) || (!authentication.isAuthenticated())) {
            java.lang.String token = httpRequest.getHeader(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.INTERNAL_TOKEN_HEADER);
            if (token != null) {
                org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken internalAuthenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken(token);
                context.setAuthentication(internalAuthenticationToken);
                if (auditLogger.isEnabled()) {
                    org.apache.ambari.server.audit.event.LoginAuditEvent loginAuditEvent = org.apache.ambari.server.audit.event.LoginAuditEvent.builder().withUserName(internalAuthenticationToken.getName()).withProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName(internalAuthenticationToken)).withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(httpRequest)).withRoles(permissionHelper.getPermissionLabels(authentication)).withTimestamp(java.lang.System.currentTimeMillis()).build();
                    auditLogger.log(loginAuditEvent);
                }
            } else {
                if (requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_ALL_PATTERN)) {
                    java.lang.String queryString = httpRequest.getQueryString();
                    java.lang.String requestedURL = (queryString == null) ? requestURI : (requestURI + '?') + queryString;
                    java.lang.String redirectURL = httpResponse.encodeRedirectURL(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.LOGIN_REDIRECT_BASE + requestedURL);
                    httpResponse.sendRedirect(redirectURL);
                } else {
                    entryPoint.commence(httpRequest, httpResponse, new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("Missing authentication token"));
                }
                return;
            }
        } else if (!authorizationPerformedInternally(requestURI)) {
            boolean authorized = false;
            if (requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_BOOTSTRAP_PATTERN_ALL)) {
                authorized = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, null, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
            } else {
                for (org.springframework.security.core.GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    if (grantedAuthority instanceof org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) {
                        org.apache.ambari.server.security.authorization.AmbariGrantedAuthority ambariGrantedAuthority = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (grantedAuthority));
                        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = ambariGrantedAuthority.getPrivilegeEntity();
                        java.lang.Integer permissionId = privilegeEntity.getPermission().getId();
                        if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION)) {
                            authorized = true;
                            break;
                        }
                        if ((!"GET".equalsIgnoreCase(httpRequest.getMethod())) && requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CREDENTIALS_AMBARI_PATTERN)) {
                            if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION)) {
                                authorized = true;
                                break;
                            }
                        } else if (requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTERS_ALL_PATTERN)) {
                            if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_USER_PERMISSION) || permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_ADMINISTRATOR_PERMISSION)) {
                                authorized = true;
                                break;
                            }
                        } else if (org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.STACK_ADVISOR_REGEX.matcher(requestURI).matches()) {
                            if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_USER_PERMISSION) || permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_ADMINISTRATOR_PERMISSION)) {
                                authorized = true;
                                break;
                            }
                        } else if (requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VIEWS_ALL_PATTERN)) {
                            if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION)) {
                                authorized = true;
                                break;
                            }
                        } else if (requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_AUTH_PATTERN) && "POST".equalsIgnoreCase(httpRequest.getMethod())) {
                            authorized = true;
                            break;
                        }
                    }
                }
                authorized = authorized || (httpRequest.getMethod().equals("GET") && (!requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_LDAP_SYNC_EVENTS_ALL_PATTERN)));
            }
            if (!authorized) {
                if (auditLogger.isEnabled()) {
                    auditEvent = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withHttpMethodName(httpRequest.getMethod()).withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(httpRequest)).withResourcePath(httpRequest.getRequestURI()).withUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName()).withProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName()).withTimestamp(java.lang.System.currentTimeMillis()).build();
                    auditLogger.log(auditEvent);
                }
                httpResponse.setHeader("WWW-Authenticate", ("Basic realm=\"" + realm) + "\"");
                httpResponse.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "You do not have permissions to access this resource.");
                httpResponse.flushBuffer();
                return;
            }
        }
        if (org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName() != null) {
            httpResponse.setHeader("User", org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
            if (auditLogger.isEnabled() && (httpResponse.getStatus() == javax.servlet.http.HttpServletResponse.SC_FORBIDDEN)) {
                auditEvent = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withHttpMethodName(httpRequest.getMethod()).withRemoteIp(org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(httpRequest)).withResourcePath(httpRequest.getRequestURI()).withUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName()).withProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName()).withTimestamp(java.lang.System.currentTimeMillis()).build();
                auditLogger.log(auditEvent);
            }
        }
        chain.doFilter(request, response);
    }

    private org.springframework.security.core.Authentication getDefaultAuthentication() {
        org.springframework.security.core.Authentication defaultUser = null;
        if ((configuration != null) && (users != null)) {
            java.lang.String username = configuration.getDefaultApiAuthenticatedUser();
            if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
                final org.apache.ambari.server.security.authorization.User user = users.getUser(username);
                if (user != null) {
                    java.security.Principal principal = new java.security.Principal() {
                        @java.lang.Override
                        public java.lang.String getName() {
                            return user.getUserName();
                        }
                    };
                    defaultUser = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, null, users.getUserAuthorities(user.getUserName()));
                }
            }
        }
        return defaultUser;
    }

    private boolean authorizationPerformedInternally(java.lang.String requestURI) {
        return (((((((((((((((((((((requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_USERS_ALL_PATTERN) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_REQUESTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_GROUPS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CREDENTIALS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_PRIVILEGES_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_REQUESTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_SERVICES_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_ALERT_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTERS_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_STACK_VERSIONS_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_VIEWS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_PATH_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_WIDGET_LAYOUTS_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_WIDGET_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_HOSTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_CONFIGURATIONS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_COMPONENTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_HOST_COMPONENTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTER_CONFIG_GROUPS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_HOSTS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_ALERT_TARGETS_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_PERSIST_ALL_PATTERN)) || requestURI.matches(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.API_CLUSTERS_UPGRADES_PATTERN);
    }

    @java.lang.Override
    public void destroy() {
    }

    private static java.lang.String getParameterValue(javax.servlet.FilterConfig filterConfig, java.lang.String parameterName, java.lang.String defaultValue) {
        java.lang.String value = filterConfig.getInitParameter(parameterName);
        if ((value == null) || (value.length() == 0)) {
            value = filterConfig.getServletContext().getInitParameter(parameterName);
        }
        return (value == null) || (value.length() == 0) ? defaultValue : value;
    }

    org.springframework.security.core.context.SecurityContext getSecurityContext() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext();
    }

    org.apache.ambari.server.view.ViewRegistry getViewRegistry() {
        return org.apache.ambari.server.view.ViewRegistry.getInstance();
    }
}