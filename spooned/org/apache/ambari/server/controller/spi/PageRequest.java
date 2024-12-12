package org.apache.ambari.server.controller.spi;
public interface PageRequest {
    org.apache.ambari.server.controller.spi.PageRequest.StartingPoint getStartingPoint();

    int getPageSize();

    int getOffset();

    org.apache.ambari.server.controller.spi.Predicate getPredicate();

    enum StartingPoint {

        Beginning,
        End,
        OffsetStart,
        OffsetEnd,
        PredicateStart,
        PredicateEnd;}
}