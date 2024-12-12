package org.apache.ambari.server.audit.request.creator;
public class ConfigurationChangeEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void clusterNameChangeTest() {
        org.apache.ambari.server.audit.request.eventcreator.ConfigurationChangeEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ConfigurationChangeEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "newname");
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, "oldname");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Cluster, properties, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Cluster name change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Old name(oldname), New name(newname)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void configurationChangeTest() {
        org.apache.ambari.server.audit.request.eventcreator.ConfigurationChangeEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ConfigurationChangeEventCreator();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resourceNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(resultTree, resource, "resources");
        org.apache.ambari.server.controller.spi.Resource version = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        version.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID, "1");
        version.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID, "note");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> versionNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(resourceNode, version, "");
        resourceNode.addChild(versionNode);
        resultTree.addChild(resourceNode);
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Cluster, new java.util.HashMap<>(), null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Configuration change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), VersionNumber(V1), VersionNote(note)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}