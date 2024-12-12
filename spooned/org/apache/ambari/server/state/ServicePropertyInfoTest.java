package org.apache.ambari.server.state;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
public class ServicePropertyInfoTest {
    private static final java.lang.String XML = "<property>\n" + (("  <name>prop_name</name>\n" + "  <value>prop_value</value>\n") + "</property>");

    @org.junit.Test
    public void testName() throws java.lang.Exception {
        org.apache.ambari.server.state.ServicePropertyInfo p = org.apache.ambari.server.state.ServicePropertyInfoTest.getServiceProperty(org.apache.ambari.server.state.ServicePropertyInfoTest.XML);
        java.lang.String name = p.getName();
        junit.framework.Assert.assertEquals("prop_name", name);
    }

    @org.junit.Test
    public void testValue() throws java.lang.Exception {
        org.apache.ambari.server.state.ServicePropertyInfo p = org.apache.ambari.server.state.ServicePropertyInfoTest.getServiceProperty(org.apache.ambari.server.state.ServicePropertyInfoTest.XML);
        java.lang.String value = p.getValue();
        junit.framework.Assert.assertEquals("prop_value", value);
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.state.ServicePropertyInfo.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    public static org.apache.ambari.server.state.ServicePropertyInfo getServiceProperty(java.lang.String xml) throws javax.xml.bind.JAXBException {
        javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.ServicePropertyInfo.class);
        javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller.unmarshal(new javax.xml.transform.stream.StreamSource(new java.io.ByteArrayInputStream(xml.getBytes())), org.apache.ambari.server.state.ServicePropertyInfo.class).getValue();
    }
}