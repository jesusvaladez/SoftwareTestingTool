package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.MapUtils;
import static org.easymock.EasyMock.createNiceMock;
public class AgentDataHolderTest {
    @org.junit.Test
    public void testGetHashWithTimestamp() {
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder = new org.apache.ambari.server.agent.stomp.AgentConfigsHolder(ambariEventPublisher, org.apache.ambari.server.security.encryption.Encryptor.NONE);
        org.apache.ambari.server.events.AgentConfigsUpdateEvent event1 = new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, null);
        event1.setHash("01");
        event1.setTimestamp(1L);
        java.lang.String eventHash1 = agentConfigsHolder.getHash(event1);
        org.apache.ambari.server.events.AgentConfigsUpdateEvent event2 = new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, null);
        event2.setHash("02");
        event2.setTimestamp(1L);
        java.lang.String eventHash2 = agentConfigsHolder.getHash(event2);
        org.apache.ambari.server.events.AgentConfigsUpdateEvent event3 = new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, null);
        event3.setHash("01");
        event3.setTimestamp(2L);
        java.lang.String eventHash3 = agentConfigsHolder.getHash(event3);
        org.apache.ambari.server.events.AgentConfigsUpdateEvent event4 = new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, null);
        event4.setHash("02");
        event4.setTimestamp(2L);
        java.lang.String eventHash4 = agentConfigsHolder.getHash(event4);
        org.apache.ambari.server.events.AgentConfigsUpdateEvent event5 = new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, org.apache.commons.collections.MapUtils.EMPTY_SORTED_MAP);
        event5.setHash("01");
        event5.setTimestamp(1L);
        java.lang.String eventHash5 = agentConfigsHolder.getHash(event5);
        org.junit.Assert.assertEquals(eventHash1, eventHash2);
        org.junit.Assert.assertEquals(eventHash1, eventHash3);
        org.junit.Assert.assertEquals(eventHash1, eventHash4);
        org.junit.Assert.assertFalse(eventHash1.equals(eventHash5));
    }

    @org.junit.Test
    public void testGetHash() {
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.agent.stomp.MetadataHolder metadataHolder = new org.apache.ambari.server.agent.stomp.MetadataHolder(ambariEventPublisher);
        org.apache.ambari.server.events.MetadataUpdateEvent event1 = new org.apache.ambari.server.events.MetadataUpdateEvent(null, null, null, org.apache.ambari.server.events.UpdateEventType.CREATE);
        event1.setHash("01");
        java.lang.String eventHash1 = metadataHolder.getHash(event1);
        org.apache.ambari.server.events.MetadataUpdateEvent event2 = new org.apache.ambari.server.events.MetadataUpdateEvent(null, null, null, org.apache.ambari.server.events.UpdateEventType.CREATE);
        event2.setHash("02");
        java.lang.String eventHash2 = metadataHolder.getHash(event2);
        org.apache.ambari.server.events.MetadataUpdateEvent event3 = new org.apache.ambari.server.events.MetadataUpdateEvent(null, null, null, org.apache.ambari.server.events.UpdateEventType.UPDATE);
        event3.setHash("01");
        java.lang.String eventHash3 = metadataHolder.getHash(event3);
        org.junit.Assert.assertEquals(eventHash1, eventHash2);
        org.junit.Assert.assertFalse(eventHash1.equals(eventHash3));
    }
}