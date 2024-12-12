package org.apache.ambari.server.controller;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
public class ServiceConfigVersionResponseTest {
    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.controller.ServiceConfigVersionResponse.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}