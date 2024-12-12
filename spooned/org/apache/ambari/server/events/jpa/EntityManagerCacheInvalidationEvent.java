package org.apache.ambari.server.events.jpa;
public final class EntityManagerCacheInvalidationEvent extends org.apache.ambari.server.events.jpa.JPAEvent {
    public EntityManagerCacheInvalidationEvent() {
        super(org.apache.ambari.server.events.jpa.JPAEvent.JPAEventType.CACHE_INVALIDATION);
    }
}