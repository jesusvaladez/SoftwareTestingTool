package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class RequestUtils {
    private static java.util.Set<java.lang.String> headersToCheck = com.google.common.collect.ImmutableSet.copyOf(java.util.Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"));

    public static java.lang.String getRemoteAddress(javax.servlet.http.HttpServletRequest request) {
        java.lang.String ip = null;
        for (java.lang.String header : org.apache.ambari.server.utils.RequestUtils.headersToCheck) {
            ip = request.getHeader(header);
            if (!org.apache.ambari.server.utils.RequestUtils.isRemoteAddressUnknown(ip)) {
                break;
            }
        }
        if (org.apache.ambari.server.utils.RequestUtils.isRemoteAddressUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        if (org.apache.ambari.server.utils.RequestUtils.containsMultipleRemoteAddresses(ip)) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static java.lang.String getRemoteAddress() {
        if (org.apache.ambari.server.utils.RequestUtils.hasValidRequest()) {
            return org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(((org.springframework.web.context.request.ServletRequestAttributes) (org.springframework.web.context.request.RequestContextHolder.getRequestAttributes())).getRequest());
        }
        return null;
    }

    private static boolean isRemoteAddressUnknown(java.lang.String ip) {
        return ((ip == null) || (ip.length() == 0)) || "unknown".equalsIgnoreCase(ip);
    }

    private static boolean containsMultipleRemoteAddresses(java.lang.String ip) {
        return (ip != null) && (ip.indexOf(",") > 0);
    }

    private static boolean hasValidRequest() {
        return ((org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() != null) && (org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() instanceof org.springframework.web.context.request.ServletRequestAttributes)) && (((org.springframework.web.context.request.ServletRequestAttributes) (org.springframework.web.context.request.RequestContextHolder.getRequestAttributes())).getRequest() != null);
    }

    public static void logRequestHeadersAndQueryParams(javax.servlet.http.HttpServletRequest request, org.slf4j.Logger logger) {
        if (logger != null) {
            java.lang.StringBuilder builder;
            builder = new java.lang.StringBuilder();
            builder.append("\n##### HEADERS #######");
            java.util.Enumeration<java.lang.String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                java.lang.String name = headerNames.nextElement();
                java.util.Enumeration<java.lang.String> values = request.getHeaders(name);
                while (values.hasMoreElements()) {
                    java.lang.String value = values.nextElement();
                    builder.append("\n\t");
                    builder.append(name);
                    builder.append(" = ");
                    builder.append(value);
                } 
            } 
            builder.append("\n#####################");
            builder.append("\n##### PARAMETERS ####");
            org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> queryParams = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
            if (queryParams != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : queryParams.entrySet()) {
                    java.lang.String name = entry.getKey();
                    java.util.List<java.lang.String> values = entry.getValue();
                    for (java.lang.String value : values) {
                        builder.append("\n\t");
                        builder.append(name);
                        builder.append(" = ");
                        builder.append(value);
                    }
                }
            }
            builder.append("\n#####################");
            logger.info(builder.toString());
        }
    }

    public static org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> getQueryStringParameters(javax.servlet.http.HttpServletRequest request) {
        java.lang.String queryString = request.getQueryString();
        return org.apache.commons.lang.StringUtils.isEmpty(queryString) ? null : org.springframework.web.util.UriComponentsBuilder.newInstance().query(queryString).build().getQueryParams();
    }

    public static java.util.List<java.lang.String> getQueryStringParameterValues(javax.servlet.http.HttpServletRequest request, java.lang.String parameterName) {
        org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> valueMap = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
        return (valueMap == null) || (!valueMap.containsKey(parameterName)) ? null : valueMap.get(parameterName);
    }

    public static java.lang.String getQueryStringParameterValue(javax.servlet.http.HttpServletRequest request, java.lang.String parameterName) {
        org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> valueMap = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
        return (valueMap == null) || (!valueMap.containsKey(parameterName)) ? null : valueMap.getFirst(parameterName);
    }
}