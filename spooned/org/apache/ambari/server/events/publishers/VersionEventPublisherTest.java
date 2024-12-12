package org.apache.ambari.server.events.publishers;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class VersionEventPublisherTest {
    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector();
    }

    @org.junit.Test
    public void testPublish() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(99L);
        EasyMock.replay(cluster, sch);
        org.apache.ambari.server.events.publishers.VersionEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.VersionEventPublisher.class);
        org.apache.ambari.server.events.publishers.VersionEventPublisherTest.Listener listener = injector.getInstance(org.apache.ambari.server.events.publishers.VersionEventPublisherTest.Listener.class);
        org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cluster, sch, "1.2.3.4-5678");
        publisher.publish(event);
        org.junit.Assert.assertEquals(event, listener.getLastEvent());
        EasyMock.verify(cluster, sch);
    }

    private static class Listener {
        private org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent lastEvent = null;

        @com.google.inject.Inject
        private Listener(org.apache.ambari.server.events.publishers.VersionEventPublisher eventPublisher) {
            eventPublisher.register(this);
        }

        @com.google.common.eventbus.Subscribe
        public void onEvent(org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event) {
            lastEvent = event;
        }

        public org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent getLastEvent() {
            return lastEvent;
        }
    }
}