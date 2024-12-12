package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class HostKerberosIdentityResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = java.util.Collections.singleton(java.util.Collections.<java.lang.String, java.lang.Object>emptyMap());
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID, "Host100");
        properties.put(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID, "principal@REALM");
        properties.put(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID, "userA");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID).equals("Host100").and().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID).equals("principal@REALM").toPredicate();
        provider.updateResources(request, predicate);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID).equals("Host100").and().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID).equals("principal@REALM").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(org.easymock.EasyMock.anyString())).andReturn(createNiceMock(org.apache.ambari.server.state.Cluster.class)).once();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1.getValue()).andReturn("principal1@EXAMPLE.COM");
        EasyMock.expect(principalDescriptor1.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER).times(1);
        EasyMock.expect(principalDescriptor1.getLocalUsername()).andReturn("principal1");
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(keytabDescriptor1.getFile()).andReturn("/etc/security/keytabs/principal1.headless.keytab").times(1);
        EasyMock.expect(keytabDescriptor1.getOwnerAccess()).andReturn("rw").once();
        EasyMock.expect(keytabDescriptor1.getGroupAccess()).andReturn("r").once();
        EasyMock.expect(keytabDescriptor1.getFile()).andReturn("/etc/security/keytabs/principal1.headless.keytab").times(1);
        EasyMock.expect(keytabDescriptor1.getOwnerName()).andReturn("principal1").once();
        EasyMock.expect(keytabDescriptor1.getGroupName()).andReturn("principal1").once();
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity1 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identity1.getPrincipalDescriptor()).andReturn(principalDescriptor1).times(1);
        EasyMock.expect(identity1.getKeytabDescriptor()).andReturn(keytabDescriptor1).times(1);
        EasyMock.expect(identity1.getName()).andReturn("identity1").times(1);
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor2.getValue()).andReturn("principal2/Host100@EXAMPLE.COM");
        EasyMock.expect(principalDescriptor2.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).times(1);
        EasyMock.expect(principalDescriptor2.getLocalUsername()).andReturn("principal2");
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity2 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identity2.getPrincipalDescriptor()).andReturn(principalDescriptor2).times(1);
        EasyMock.expect(identity2.getKeytabDescriptor()).andReturn(null).times(1);
        EasyMock.expect(identity2.getName()).andReturn("identity2").times(1);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity3 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identity3.getPrincipalDescriptor()).andReturn(null).times(1);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity4 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identity4.getPrincipalDescriptor()).andReturn(null).times(1);
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor5 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor5.getValue()).andReturn("principal5@EXAMPLE.COM");
        EasyMock.expect(principalDescriptor5.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER).times(1);
        EasyMock.expect(principalDescriptor5.getLocalUsername()).andReturn("principal5");
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor5 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(keytabDescriptor5.getOwnerAccess()).andReturn("r").times(1);
        EasyMock.expect(keytabDescriptor5.getGroupAccess()).andReturn("r").times(1);
        EasyMock.expect(keytabDescriptor5.getFile()).andReturn("/etc/security/keytabs/principal5.headless.keytab").times(1);
        EasyMock.expect(keytabDescriptor5.getOwnerName()).andReturn("principal5").times(1);
        EasyMock.expect(keytabDescriptor5.getGroupName()).andReturn("hadoop").times(1);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity5 = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identity5.getPrincipalDescriptor()).andReturn(principalDescriptor5).times(1);
        EasyMock.expect(identity5.getKeytabDescriptor()).andReturn(keytabDescriptor5).times(1);
        EasyMock.expect(identity5.getName()).andReturn("identity5").times(1);
        org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO = createStrictMock(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class);
        EasyMock.expect(kerberosPrincipalDAO.exists("principal1@EXAMPLE.COM")).andReturn(true).times(1);
        EasyMock.expect(kerberosPrincipalDAO.exists("principal2/Host100@EXAMPLE.COM")).andReturn(true).times(1);
        EasyMock.expect(kerberosPrincipalDAO.exists("principal5@EXAMPLE.COM")).andReturn(false).times(1);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO = createStrictMock(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class);
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity distributedEntity = new org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity();
        distributedEntity.setDistributed(true);
        EasyMock.expect(kerberosKeytabPrincipalDAO.findByNaturalKey(100L, "/etc/security/keytabs/principal1.headless.keytab", "principal1@EXAMPLE.COM")).andReturn(distributedEntity).times(1);
        org.apache.ambari.server.orm.entities.HostEntity host100 = createStrictMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        EasyMock.expect(host100.getHostId()).andReturn(100L).times(1);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = createStrictMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        EasyMock.expect(hostDAO.findByName("Host100")).andReturn(host100).times(1);
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = new java.util.ArrayList<>();
        identities.add(identity1);
        identities.add(identity2);
        identities.add(identity3);
        identities.add(identity4);
        identities.add(identity5);
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> activeIdentities = new java.util.HashMap<>();
        activeIdentities.put("Host100", identities);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createNiceMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.eq(false))).andReturn(kerberosDescriptor).atLeastOnce();
        EasyMock.expect(kerberosHelper.getActiveIdentities("Cluster100", "Host100", null, null, true, null, kerberosDescriptor, null)).andReturn(activeIdentities).once();
        replayAll();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider(managementController);
        java.lang.reflect.Field field;
        field = org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.class.getDeclaredField("kerberosHelper");
        field.setAccessible(true);
        field.set(provider, kerberosHelper);
        field = org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.class.getDeclaredField("kerberosPrincipalDAO");
        field.setAccessible(true);
        field.set(provider, kerberosPrincipalDAO);
        field = org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.class.getDeclaredField("kerberosKeytabPrincipalDAO");
        field.setAccessible(true);
        field.set(provider, kerberosKeytabPrincipalDAO);
        field = org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.class.getDeclaredField("hostDAO");
        field.setAccessible(true);
        field.set(provider, hostDAO);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID).equals("Host100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals("Cluster100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("Host100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID));
            java.lang.String principal = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID)));
            if ("principal1@EXAMPLE.COM".equals(principal)) {
                org.junit.Assert.assertEquals("identity1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID));
                org.junit.Assert.assertEquals("/etc/security/keytabs/principal1.headless.keytab", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID));
                org.junit.Assert.assertEquals("rw", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID));
                org.junit.Assert.assertEquals("r", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertEquals("640", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID));
                org.junit.Assert.assertEquals("true", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID));
            } else if ("principal2/Host100@EXAMPLE.COM".equals(principal)) {
                org.junit.Assert.assertEquals("identity2", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal2", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID));
                org.junit.Assert.assertEquals("false", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID));
            } else if ("principal5@EXAMPLE.COM".equals(principal)) {
                org.junit.Assert.assertEquals("identity5", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal5", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID));
                org.junit.Assert.assertEquals("/etc/security/keytabs/principal5.headless.keytab", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID));
                org.junit.Assert.assertEquals("principal5", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID));
                org.junit.Assert.assertEquals("r", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertEquals("hadoop", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID));
                org.junit.Assert.assertEquals("r", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID));
                org.junit.Assert.assertEquals("440", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID));
                org.junit.Assert.assertEquals("unknown", resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID));
            } else {
                org.junit.Assert.fail("Unexpected principal: " + principal);
            }
        }
        verifyAll();
    }
}