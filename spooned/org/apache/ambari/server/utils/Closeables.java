package org.apache.ambari.server.utils;
public final class Closeables {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.Closeables.class);

    public static void closeSilently(java.io.Closeable c) {
        org.apache.ambari.server.utils.Closeables.close(c, null);
    }

    public static void closeLoggingExceptions(java.io.Closeable c) {
        org.apache.ambari.server.utils.Closeables.closeLoggingExceptions(c, org.apache.ambari.server.utils.Closeables.LOGGER);
    }

    public static void closeLoggingExceptions(java.io.Closeable c, org.slf4j.Logger logger) {
        org.apache.ambari.server.utils.Closeables.close(c, logger);
    }

    private static void close(java.io.Closeable c, org.slf4j.Logger logger) {
        if (c != null) {
            try {
                c.close();
            } catch (java.io.IOException e) {
                if (logger != null) {
                    logger.warn("IOException while closing Closeable", e);
                }
            }
        }
    }

    private Closeables() {
        throw new java.lang.UnsupportedOperationException("No instances");
    }
}