package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.security.core.context.SecurityContextHolder;
public class AmbariUserAuthorizationFilter implements javax.servlet.Filter {
    private final org.apache.ambari.server.security.authorization.internal.InternalTokenStorage internalTokenStorage;

    private final org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.Inject
    public AmbariUserAuthorizationFilter(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage internalTokenStorage, org.apache.ambari.server.security.authorization.Users users) {
        this.internalTokenStorage = internalTokenStorage;
        this.users = users;
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        javax.servlet.http.HttpServletRequest httpRequest = ((javax.servlet.http.HttpServletRequest) (request));
        javax.servlet.http.HttpServletResponse httpResponse = ((javax.servlet.http.HttpServletResponse) (response));
        java.lang.String token = httpRequest.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER);
        if (token != null) {
            if (internalTokenStorage.isValidInternalToken(token)) {
                java.lang.String userToken = httpRequest.getHeader(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER);
                if (userToken != null) {
                    if (!org.apache.commons.lang.math.NumberUtils.isDigits(userToken)) {
                        httpResponse.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "Invalid user ID");
                        httpResponse.flushBuffer();
                        return;
                    }
                    java.lang.Integer userId = java.lang.Integer.parseInt(userToken);
                    org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(userId);
                    if (userEntity == null) {
                        httpResponse.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "Authentication required");
                        httpResponse.flushBuffer();
                        return;
                    }
                    if (!userEntity.getActive()) {
                        httpResponse.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "User is not active");
                        httpResponse.flushBuffer();
                        return;
                    } else {
                        org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(users.getUser(userEntity), null, users.getUserAuthorities(userEntity));
                        org.apache.ambari.server.security.authentication.AmbariUserAuthentication authentication = new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(token, userDetails, true);
                        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                        httpResponse.setHeader("User", org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    @java.lang.Override
    public void destroy() {
    }
}