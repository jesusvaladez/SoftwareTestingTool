package org.apache.ambari.server.api;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import javax.ws.rs.WebApplicationException;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AmbariCsrfProtectionFilterTest {
    @org.junit.Test
    public void testGetMethod() {
        org.apache.ambari.server.api.AmbariCsrfProtectionFilter filter = new org.apache.ambari.server.api.AmbariCsrfProtectionFilter();
        com.sun.jersey.spi.container.ContainerRequest containerRequest = EasyMock.createMock(com.sun.jersey.spi.container.ContainerRequest.class);
        EasyMock.expect(containerRequest.getMethod()).andReturn("GET");
        EasyMock.replay(containerRequest);
        org.junit.Assert.assertEquals(containerRequest, filter.filter(containerRequest));
    }

    @org.junit.Test(expected = javax.ws.rs.WebApplicationException.class)
    public void testPostNoXRequestedBy() {
        org.apache.ambari.server.api.AmbariCsrfProtectionFilter filter = new org.apache.ambari.server.api.AmbariCsrfProtectionFilter();
        com.sun.jersey.spi.container.ContainerRequest containerRequest = EasyMock.createMock(com.sun.jersey.spi.container.ContainerRequest.class);
        com.sun.jersey.core.header.InBoundHeaders headers = new com.sun.jersey.core.header.InBoundHeaders();
        EasyMock.expect(containerRequest.getMethod()).andReturn("POST");
        EasyMock.expect(containerRequest.getRequestHeaders()).andReturn(headers);
        EasyMock.replay(containerRequest);
        filter.filter(containerRequest);
    }

    @org.junit.Test
    public void testPostXRequestedBy() {
        org.apache.ambari.server.api.AmbariCsrfProtectionFilter filter = new org.apache.ambari.server.api.AmbariCsrfProtectionFilter();
        com.sun.jersey.spi.container.ContainerRequest containerRequest = EasyMock.createMock(com.sun.jersey.spi.container.ContainerRequest.class);
        com.sun.jersey.core.header.InBoundHeaders headers = new com.sun.jersey.core.header.InBoundHeaders();
        headers.add("X-Requested-By", "anything");
        EasyMock.expect(containerRequest.getMethod()).andReturn("GET");
        EasyMock.expect(containerRequest.getRequestHeaders()).andReturn(headers);
        EasyMock.replay(containerRequest);
        org.junit.Assert.assertEquals(containerRequest, filter.filter(containerRequest));
    }
}