package org.apache.ambari.server.api;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
public class ContentTypeOverrideFilter implements javax.servlet.Filter {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.ContentTypeOverrideFilter.class);

    private final java.util.Set<java.util.regex.Pattern> excludedUrls = new java.util.HashSet<>();

    class ContentTypeOverrideRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
        public ContentTypeOverrideRequestWrapper(javax.servlet.http.HttpServletRequest request) {
            super(request);
        }

        @java.lang.Override
        public java.util.Enumeration<java.lang.String> getHeaders(java.lang.String name) {
            java.util.Enumeration<java.lang.String> headerValues = super.getHeaders(name);
            if (HttpHeaders.CONTENT_TYPE.equals(name)) {
                java.util.Set<java.lang.String> newContentTypeValues = new java.util.HashSet<>();
                while (headerValues.hasMoreElements()) {
                    java.lang.String value = headerValues.nextElement();
                    if ((value != null) && value.startsWith(MediaType.APPLICATION_JSON)) {
                        newContentTypeValues.add(value.replace(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
                    } else {
                        newContentTypeValues.add(value);
                    }
                } 
                return java.util.Collections.enumeration(newContentTypeValues);
            }
            return headerValues;
        }

        @java.lang.Override
        public java.lang.String getHeader(java.lang.String name) {
            if (HttpHeaders.CONTENT_TYPE.equals(name)) {
                java.lang.String header = super.getHeader(name);
                if ((header != null) && header.startsWith(MediaType.APPLICATION_JSON)) {
                    return header.replace(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
                }
            }
            return super.getHeader(name);
        }
    }

    class ContentTypeOverrideResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {
        public ContentTypeOverrideResponseWrapper(javax.servlet.http.HttpServletResponse response) {
            super(response);
            super.setContentType(MediaType.APPLICATION_JSON);
        }

        @java.lang.Override
        public void setHeader(java.lang.String name, java.lang.String value) {
            if (!HttpHeaders.CONTENT_TYPE.equals(name)) {
                super.setHeader(name, value);
            }
        }

        @java.lang.Override
        public void addHeader(java.lang.String name, java.lang.String value) {
            if (!HttpHeaders.CONTENT_TYPE.equals(name)) {
                super.addHeader(name, value);
            }
        }

        @java.lang.Override
        public void setContentType(java.lang.String type) {
        }
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        if (request instanceof javax.servlet.http.HttpServletRequest) {
            javax.servlet.http.HttpServletRequest httpServletRequest = ((javax.servlet.http.HttpServletRequest) (request));
            java.lang.String contentType = httpServletRequest.getContentType();
            if (((contentType != null) && contentType.startsWith(MediaType.APPLICATION_JSON)) && (!isUrlExcluded(httpServletRequest.getPathInfo()))) {
                org.apache.ambari.server.api.ContentTypeOverrideFilter.ContentTypeOverrideRequestWrapper requestWrapper = new org.apache.ambari.server.api.ContentTypeOverrideFilter.ContentTypeOverrideRequestWrapper(httpServletRequest);
                org.apache.ambari.server.api.ContentTypeOverrideFilter.ContentTypeOverrideResponseWrapper responseWrapper = new org.apache.ambari.server.api.ContentTypeOverrideFilter.ContentTypeOverrideResponseWrapper(((javax.servlet.http.HttpServletResponse) (response)));
                chain.doFilter(requestWrapper, responseWrapper);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
        try {
            com.google.common.reflect.ClassPath classPath = com.google.common.reflect.ClassPath.from(java.lang.ClassLoader.getSystemClassLoader());
            com.google.common.collect.ImmutableSet<com.google.common.reflect.ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive("org.apache.ambari.server.api");
            restart : for (com.google.common.reflect.ClassPath.ClassInfo classInfo : classes) {
                java.lang.Class<?> clazz = classInfo.load();
                if (clazz.isAnnotationPresent(javax.ws.rs.Path.class)) {
                    javax.ws.rs.Path path = clazz.getAnnotation(javax.ws.rs.Path.class);
                    for (java.lang.reflect.Method method : clazz.getMethods()) {
                        if (method.isAnnotationPresent(javax.ws.rs.Consumes.class)) {
                            javax.ws.rs.Consumes consumesAnnotation = method.getAnnotation(javax.ws.rs.Consumes.class);
                            for (java.lang.String consume : consumesAnnotation.value()) {
                                if (MediaType.APPLICATION_JSON.equals(consume)) {
                                    excludedUrls.add(java.util.regex.Pattern.compile(path.value()));
                                    continue restart;
                                }
                            }
                        }
                    }
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.api.ContentTypeOverrideFilter.logger.error("Failed to discover URLs that are excluded from Content-Type override. Falling back to pre-defined list of exluded URLs.", e);
            excludedUrls.add(java.util.regex.Pattern.compile("/bootstrap"));
        } finally {
            excludedUrls.add(java.util.regex.Pattern.compile("/views/.*"));
        }
    }

    private boolean isUrlExcluded(java.lang.String pathInfo) {
        return excludedUrls.stream().anyMatch(p -> p.matcher(pathInfo).matches());
    }

    @java.lang.Override
    public void destroy() {
    }
}