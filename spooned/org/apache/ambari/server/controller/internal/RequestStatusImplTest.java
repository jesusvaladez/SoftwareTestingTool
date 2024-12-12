package org.apache.ambari.server.controller.internal;
public class RequestStatusImplTest {
    @org.junit.Test
    public void testGetAssociatedResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.RequestStatusImpl status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), status.getAssociatedResources());
        org.apache.ambari.server.controller.spi.Resource associatedResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = java.util.Collections.singleton(associatedResource);
        status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, associatedResources);
        org.junit.Assert.assertEquals(associatedResources, status.getAssociatedResources());
    }

    @org.junit.Test
    public void testGetRequestResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.RequestStatusImpl status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        org.junit.Assert.assertNull(status.getRequestResource());
        org.apache.ambari.server.controller.spi.Resource requestResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
        status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(requestResource);
        org.junit.Assert.assertEquals(requestResource, status.getRequestResource());
    }

    @org.junit.Test
    public void testGetStatus() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.RequestStatusImpl status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete, status.getStatus());
        org.apache.ambari.server.controller.spi.Resource requestResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
        requestResource.setProperty("Requests/status", "InProgress");
        status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(requestResource);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.InProgress, status.getStatus());
    }

    @org.junit.Test
    public void testGetRequestStatusMetadata() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.RequestStatusImpl status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        org.junit.Assert.assertNull(status.getStatusMetadata());
        org.apache.ambari.server.controller.spi.RequestStatusMetaData metaData = new org.apache.ambari.server.controller.spi.RequestStatusMetaData() {};
        status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, null, metaData);
        org.junit.Assert.assertEquals(metaData, status.getStatusMetadata());
    }
}