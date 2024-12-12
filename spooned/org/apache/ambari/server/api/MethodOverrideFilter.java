package org.apache.ambari.server.api;
public class MethodOverrideFilter implements javax.servlet.Filter {
    private static final java.lang.String HEADER_NAME = "X-Http-Method-Override";

    private static final java.util.List<java.lang.String> ALLOWED_METHODS = new java.util.ArrayList<java.lang.String>() {
        {
            add("GET");
        }
    };

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        if (request instanceof javax.servlet.http.HttpServletRequest) {
            javax.servlet.http.HttpServletRequest httpServletRequest = ((javax.servlet.http.HttpServletRequest) (request));
            java.lang.String method = httpServletRequest.getHeader(org.apache.ambari.server.api.MethodOverrideFilter.HEADER_NAME);
            if (method != null) {
                if (org.apache.ambari.server.api.MethodOverrideFilter.ALLOWED_METHODS.contains(method.toUpperCase())) {
                    final org.springframework.http.HttpMethod httpMethod = org.springframework.http.HttpMethod.valueOf(method.toUpperCase());
                    javax.servlet.http.HttpServletRequestWrapper requestWrapper = new javax.servlet.http.HttpServletRequestWrapper(httpServletRequest) {
                        @java.lang.Override
                        public java.lang.String getMethod() {
                            return httpMethod.toString();
                        }
                    };
                    chain.doFilter(requestWrapper, response);
                    return;
                } else {
                    javax.servlet.http.HttpServletResponse httpResponse = ((javax.servlet.http.HttpServletResponse) (response));
                    httpResponse.sendError(400, (("Incorrect HTTP method for override: " + method) + ". Allowed values: ") + org.apache.ambari.server.api.MethodOverrideFilter.ALLOWED_METHODS);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @java.lang.Override
    public void destroy() {
    }
}