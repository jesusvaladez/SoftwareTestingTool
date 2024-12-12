package org.apache.ambari.server.view.events;
public class EventImplTest {
    private static java.lang.String view_xml = "<view>\n" + ((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "</view>");

    @org.junit.Test
    public void testGetId() throws java.lang.Exception {
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.events.EventImplTest.view_xml);
        org.junit.Assert.assertEquals("MyEvent", event.getId());
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("p1", "v1");
        properties.put("p2", "v2");
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", properties, org.apache.ambari.server.view.events.EventImplTest.view_xml);
        org.junit.Assert.assertEquals(properties, event.getProperties());
    }

    @org.junit.Test
    public void testGetViewSubject() throws java.lang.Exception {
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.events.EventImplTest.view_xml);
        org.junit.Assert.assertEquals("MY_VIEW", event.getViewSubject().getViewName());
        org.junit.Assert.assertEquals("My View!", event.getViewSubject().getLabel());
        org.junit.Assert.assertEquals("1.0.0", event.getViewSubject().getVersion());
    }

    @org.junit.Test
    public void testGetViewInstanceSubject() throws java.lang.Exception {
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.events.EventImplTest.view_xml);
        org.junit.Assert.assertNull(event.getViewInstanceSubject());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), viewInstanceEntity);
        org.junit.Assert.assertEquals(viewInstanceEntity, event.getViewInstanceSubject());
    }

    public static org.apache.ambari.server.view.events.EventImpl getEvent(java.lang.String id, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String xml) throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(xml);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(viewConfig);
        return new org.apache.ambari.server.view.events.EventImpl(id, properties, viewEntity);
    }

    public static org.apache.ambari.server.view.events.EventImpl getEvent(java.lang.String id, java.util.Map<java.lang.String, java.lang.String> properties, org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) throws java.lang.Exception {
        return new org.apache.ambari.server.view.events.EventImpl(id, properties, instanceEntity);
    }
}