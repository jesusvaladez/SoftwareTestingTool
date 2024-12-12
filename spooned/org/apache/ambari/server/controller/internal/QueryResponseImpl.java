package org.apache.ambari.server.controller.internal;
public class QueryResponseImpl implements org.apache.ambari.server.controller.spi.QueryResponse {
    private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources;

    private final boolean sortedResponse;

    private final boolean pagedResponse;

    private final int totalResourceCount;

    public QueryResponseImpl(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources) {
        this.resources = resources;
        this.sortedResponse = false;
        this.pagedResponse = false;
        this.totalResourceCount = 0;
    }

    public QueryResponseImpl(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, boolean sortedResponse, boolean pagedResponse, int totalResourceCount) {
        this.resources = resources;
        this.sortedResponse = sortedResponse;
        this.pagedResponse = pagedResponse;
        this.totalResourceCount = totalResourceCount;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources() {
        return resources;
    }

    @java.lang.Override
    public boolean isSortedResponse() {
        return sortedResponse;
    }

    @java.lang.Override
    public boolean isPagedResponse() {
        return pagedResponse;
    }

    @java.lang.Override
    public int getTotalResourceCount() {
        return totalResourceCount;
    }
}