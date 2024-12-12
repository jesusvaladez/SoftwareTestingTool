package org.apache.ambari.server.controller.internal;
public class PageResponseImpl implements org.apache.ambari.server.controller.spi.PageResponse {
    private final java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable;

    private final int offset;

    private final org.apache.ambari.server.controller.spi.Resource previousResource;

    private final org.apache.ambari.server.controller.spi.Resource nextResource;

    private final java.lang.Integer totalResourceCount;

    public PageResponseImpl(java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable, int offset, org.apache.ambari.server.controller.spi.Resource previousResource, org.apache.ambari.server.controller.spi.Resource nextResource, java.lang.Integer totalResourceCount) {
        this.iterable = iterable;
        this.offset = offset;
        this.previousResource = previousResource;
        this.nextResource = nextResource;
        this.totalResourceCount = totalResourceCount;
    }

    @java.lang.Override
    public java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> getIterable() {
        return iterable;
    }

    @java.lang.Override
    public int getOffset() {
        return offset;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Resource getPreviousResource() {
        return previousResource;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Resource getNextResource() {
        return nextResource;
    }

    @java.lang.Override
    public java.lang.Integer getTotalResourceCount() {
        return totalResourceCount;
    }
}