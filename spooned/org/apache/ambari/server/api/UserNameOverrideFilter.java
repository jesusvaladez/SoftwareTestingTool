package org.apache.ambari.server.api;
public class UserNameOverrideFilter implements javax.servlet.Filter {
    private static final java.util.regex.Pattern USER_NAME_IN_URI_REGEXP = java.util.regex.Pattern.compile("(?<pre>.*/users/)(?<username>[^/]+)(?<post>(/.*)?)");

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        if (request instanceof javax.servlet.http.HttpServletRequest) {
            final javax.servlet.http.HttpServletRequest httpServletRequest = ((javax.servlet.http.HttpServletRequest) (request));
            java.util.regex.Matcher userNameMatcher = getUserNameMatcher(httpServletRequest.getRequestURI());
            if (userNameMatcher.find()) {
                java.lang.String userNameFromUri = java.net.URLDecoder.decode(userNameMatcher.group("username"), "UTF-8");
                final java.lang.String userName = org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(userNameFromUri);
                if (!userNameFromUri.equals(userName)) {
                    final java.lang.String requestUriOverride = java.lang.String.format("%s%s%s", userNameMatcher.group("pre"), userName, userNameMatcher.group("post"));
                    request = new javax.servlet.http.HttpServletRequestWrapper(httpServletRequest) {
                        @java.lang.Override
                        public java.lang.String getRequestURI() {
                            return requestUriOverride;
                        }
                    };
                }
            }
        }
        chain.doFilter(request, response);
    }

    protected java.util.regex.Matcher getUserNameMatcher(java.lang.String requestUri) {
        return org.apache.ambari.server.api.UserNameOverrideFilter.USER_NAME_IN_URI_REGEXP.matcher(requestUri);
    }

    @java.lang.Override
    public void destroy() {
    }
}