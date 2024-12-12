package org.apache.ambari.server.agent;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
public class AgentHostInfoTest {
    @org.junit.Test
    public void testDeserializeHostInfo() throws org.codehaus.jackson.JsonParseException, org.codehaus.jackson.map.JsonMappingException, java.io.IOException {
        java.lang.String hostinfo = "{\"architecture\": \"x86_64\", " + (((((((((((((((((((((((((((("\"augeasversion\": \"0.10.0\"," + "\"domain\": \"test.com\", ") + "\"facterversion\": \"1.6.10\",") + "\"fqdn\": \"dev.test.com\", ") + "\"hardwareisa\": \"x86_64\", ") + "\"hardwaremodel\": \"x86_64\",") + "\"hostname\": \"dev\", ") + "\"id\": \"root\", ") + "\"interfaces\": \"eth0,lo\", ") + "\"ipaddress\": \"10.0.2.15\",") + "\"ipaddress_eth0\": \"10.0.2.15\",") + "\"ipaddress_lo\": \"127.0.0.1\",") + "\"is_virtual\": true,") + "\"kernel\": \"Linux\", ") + "\"kernelmajversion\": \"2.6\",") + "\"kernelrelease\": \"2.6.18-238.12.1.el5\",") + "\"kernelversion\": \"2.6.18\", ") + "\"lsbdistcodename\": \"Final\",") + "\"lsbdistdescription\": \"CentOS release 5.8 (Final)\",") + "\"lsbdistid\": \"CentOS\", ") + "\"lsbdistrelease\": \"5.8\", ") + "\"lsbmajdistrelease\": \"5\",") + "\"macaddress\": \"08:00:27:D2:59:B2\", ") + "\"macaddress_eth0\": \"08:00:27:D2:59:B2\",") + "\"manufacturer\": \"innotek GmbH\",") + "\"memoryfree\": 2453667,") + "\"memorysize\": 3051356, ") + "\"memorytotal\": 3051356,") + "\"netmask\": \"255.255.255.0\"}");
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        org.apache.ambari.server.agent.HostInfo info = mapper.readValue(hostinfo, org.apache.ambari.server.agent.HostInfo.class);
        junit.framework.Assert.assertEquals(info.getMemoryTotal(), 3051356L);
        junit.framework.Assert.assertEquals(info.getKernel(), "Linux");
        junit.framework.Assert.assertEquals(info.getFQDN(), "dev.test.com");
        junit.framework.Assert.assertEquals(info.getAgentUserId(), "root");
        junit.framework.Assert.assertEquals(info.getMemorySize(), 3051356L);
        junit.framework.Assert.assertEquals(info.getArchitecture(), "x86_64");
    }
}