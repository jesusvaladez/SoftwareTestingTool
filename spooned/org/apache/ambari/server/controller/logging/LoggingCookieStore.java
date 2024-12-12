package org.apache.ambari.server.controller.logging;
class LoggingCookieStore {
    public static final org.apache.ambari.server.controller.logging.LoggingCookieStore INSTANCE = new org.apache.ambari.server.controller.logging.LoggingCookieStore();

    private final java.util.Map<java.lang.String, java.lang.String> cookiesMap = new java.util.HashMap<>();

    private LoggingCookieStore() {
    }

    public java.util.Map<java.lang.String, java.lang.String> getCookiesMap() {
        return cookiesMap;
    }

    public void addCookie(java.lang.String cookieName, java.lang.String cookieValue) {
        cookiesMap.put(cookieName, cookieValue);
    }
}