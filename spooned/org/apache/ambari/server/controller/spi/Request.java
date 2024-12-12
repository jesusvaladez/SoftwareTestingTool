package org.apache.ambari.server.controller.spi;
public interface Request {
    java.lang.String REQUEST_INFO_BODY_PROPERTY = "RAW_REQUEST_BODY";

    java.lang.String DIRECTIVE_DRY_RUN = "dry_run";

    java.util.Set<java.lang.String> getPropertyIds();

    java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getProperties();

    java.util.Map<java.lang.String, java.lang.String> getRequestInfoProperties();

    org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo(java.lang.String id);

    org.apache.ambari.server.controller.spi.PageRequest getPageRequest();

    org.apache.ambari.server.controller.spi.SortRequest getSortRequest();

    boolean isDryRunRequest();
}