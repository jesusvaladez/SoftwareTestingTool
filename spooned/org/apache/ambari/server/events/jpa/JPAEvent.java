package org.apache.ambari.server.events.jpa;
import javax.persistence.EntityManagerFactory;
public abstract class JPAEvent {
    public enum JPAEventType {

        CACHE_INVALIDATION;}

    protected final org.apache.ambari.server.events.jpa.JPAEvent.JPAEventType m_eventType;

    public JPAEvent(org.apache.ambari.server.events.jpa.JPAEvent.JPAEventType eventType) {
        m_eventType = eventType;
    }

    public org.apache.ambari.server.events.jpa.JPAEvent.JPAEventType getType() {
        return m_eventType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(getClass().getSimpleName());
        buffer.append("{eventType=").append(m_eventType);
        buffer.append("}");
        return buffer.toString();
    }
}