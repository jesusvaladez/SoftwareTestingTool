package org.apache.ambari.server.controller.internal;
public class PageRequestImpl implements org.apache.ambari.server.controller.spi.PageRequest {
    private final org.apache.ambari.server.controller.spi.PageRequest.StartingPoint startingPoint;

    private final int pageSize;

    private final int offset;

    private final org.apache.ambari.server.controller.spi.Predicate predicate;

    private final java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> comparator;

    public PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint startingPoint, int pageSize, int offset, org.apache.ambari.server.controller.spi.Predicate predicate, java.util.Comparator<org.apache.ambari.server.controller.spi.Resource> comparator) {
        this.startingPoint = startingPoint;
        this.pageSize = pageSize;
        this.offset = offset;
        this.predicate = predicate;
        this.comparator = comparator;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.PageRequest.StartingPoint getStartingPoint() {
        return startingPoint;
    }

    @java.lang.Override
    public int getPageSize() {
        return pageSize;
    }

    @java.lang.Override
    public int getOffset() {
        return offset;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return predicate;
    }
}