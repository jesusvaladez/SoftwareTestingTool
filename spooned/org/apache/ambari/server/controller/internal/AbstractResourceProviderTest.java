package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
public class AbstractResourceProviderTest {
    @org.junit.Test
    public void testCheckPropertyIds() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("foo");
        propertyIds.add("cat1/foo");
        propertyIds.add("cat2/bar");
        propertyIds.add("cat2/baz");
        propertyIds.add("cat3/sub1/bam");
        propertyIds.add("cat4/sub2/sub3/bat");
        propertyIds.add("cat5/subcat5/map");
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider(managementController);
        java.util.Set<java.lang.String> unsupported = provider.checkPropertyIds(java.util.Collections.singleton("HostComponentProcess/host_name"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("HostComponentProcess/host_name/foo")).isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("bar"));
        org.junit.Assert.assertEquals(1, unsupported.size());
        org.junit.Assert.assertTrue(unsupported.contains("bar"));
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("HostComponentProcess/status"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("HostComponentProcess"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
    }

    @org.junit.Test
    public void testGetPropertyIds() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("HostComponentProcess/name");
        propertyIds.add("HostComponentProcess/status");
        propertyIds.add("HostComponentProcess/cluster_name");
        propertyIds.add("HostComponentProcess/host_name");
        propertyIds.add("HostComponentProcess/component_name");
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.replay(maintenanceStateHelper, repositoryVersionDAO);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider(managementController);
        java.util.Set<java.lang.String> supportedPropertyIds = provider.getPropertyIds();
        org.junit.Assert.assertTrue(supportedPropertyIds.containsAll(propertyIds));
    }

    @org.junit.Test
    public void testGetRequestStatus() {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.replay(maintenanceStateHelper, repositoryVersionDAO);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(managementController, maintenanceStateHelper, repositoryVersionDAO);
        org.apache.ambari.server.controller.spi.RequestStatus status = provider.getRequestStatus(null);
        org.junit.Assert.assertNull(status.getRequestResource());
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), status.getAssociatedResources());
        org.apache.ambari.server.controller.RequestStatusResponse response = new org.apache.ambari.server.controller.RequestStatusResponse(99L);
        status = provider.getRequestStatus(response);
        org.apache.ambari.server.controller.spi.Resource resource = status.getRequestResource();
        org.junit.Assert.assertEquals(99L, resource.getPropertyValue("Requests/id"));
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), status.getAssociatedResources());
        status = provider.getRequestStatus(response, null);
        resource = status.getRequestResource();
        org.junit.Assert.assertEquals(99L, resource.getPropertyValue("Requests/id"));
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), status.getAssociatedResources());
        org.apache.ambari.server.controller.spi.Resource associatedResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = java.util.Collections.singleton(associatedResource);
        status = provider.getRequestStatus(response, associatedResources);
        resource = status.getRequestResource();
        org.junit.Assert.assertEquals(99L, resource.getPropertyValue("Requests/id"));
        org.junit.Assert.assertEquals(associatedResources, status.getAssociatedResources());
    }

    @org.junit.Test
    public void testGetPropertyMaps() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestResourceProvider();
        java.util.Map<java.lang.String, java.lang.Object> updatePropertyMap = new java.util.HashMap<>();
        updatePropertyMap.put("SomeProperty", "SomeUpdateValue");
        updatePropertyMap.put("SomeOtherProperty", 99);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property("ClusterName").equals("c1").and().property("ResourceName").equals("r1").toPredicate();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = provider.getPropertyMaps(updatePropertyMap, predicate);
        org.junit.Assert.assertEquals(1, propertyMaps.size());
        java.util.Map<java.lang.String, java.lang.Object> map = propertyMaps.iterator().next();
        org.junit.Assert.assertEquals(4, map.size());
        org.junit.Assert.assertEquals("c1", map.get("ClusterName"));
        org.junit.Assert.assertEquals("r1", map.get("ResourceName"));
        org.junit.Assert.assertEquals("SomeUpdateValue", map.get("SomeProperty"));
        org.junit.Assert.assertEquals(99, map.get("SomeOtherProperty"));
        pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        predicate = pb.property("ClusterName").equals("c1").and().begin().property("ResourceName").equals("r1").or().property("ResourceName").equals("r2").end().toPredicate();
        propertyMaps = provider.getPropertyMaps(updatePropertyMap, predicate);
        org.junit.Assert.assertEquals(2, propertyMaps.size());
        for (java.util.Map<java.lang.String, java.lang.Object> map2 : propertyMaps) {
            org.junit.Assert.assertEquals(4, map2.size());
            org.junit.Assert.assertEquals("c1", map2.get("ClusterName"));
            java.lang.Object resourceName = map2.get("ResourceName");
            org.junit.Assert.assertTrue(resourceName.equals("r1") || resourceName.equals("r2"));
            org.junit.Assert.assertEquals("SomeUpdateValue", map2.get("SomeProperty"));
            org.junit.Assert.assertEquals(99, map2.get("SomeOtherProperty"));
        }
        predicate = new org.apache.ambari.server.controller.predicate.AlwaysPredicate();
        propertyMaps = provider.getPropertyMaps(updatePropertyMap, predicate);
        org.junit.Assert.assertEquals(4, propertyMaps.size());
        for (java.util.Map<java.lang.String, java.lang.Object> map2 : propertyMaps) {
            org.junit.Assert.assertEquals(4, map2.size());
            org.junit.Assert.assertEquals("c1", map2.get("ClusterName"));
            java.lang.Object resourceName = map2.get("ResourceName");
            org.junit.Assert.assertTrue(((resourceName.equals("r1") || resourceName.equals("r2")) || resourceName.equals("r3")) || resourceName.equals("r4"));
            org.junit.Assert.assertEquals("SomeUpdateValue", map2.get("SomeProperty"));
            org.junit.Assert.assertEquals(99, map2.get("SomeOtherProperty"));
        }
    }

    @org.junit.Test
    public void testGetQueryParameterValue() {
        java.lang.String queryParameterId1 = "qp/variable1";
        java.lang.String queryParameterValue1 = "value1";
        java.lang.String queryParameterId2 = "qp/variable2";
        java.lang.String queryParameterValue2 = "value2";
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(queryParameterId1).equals(queryParameterValue1).and().property(queryParameterId2).equals(queryParameterValue2).toPredicate();
        org.junit.Assert.assertEquals(queryParameterValue1, org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId1, predicate));
        org.junit.Assert.assertFalse(queryParameterValue2.equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId1, predicate)));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue("queryParameterIdNotFound", predicate));
        java.lang.String queryParameterId3 = "qp/variable3";
        java.lang.String queryParameterValue3 = "value3";
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(queryParameterId3).equals(queryParameterValue3).and().begin().property(queryParameterId1).equals(queryParameterValue1).and().property(queryParameterId2).equals(queryParameterValue2).end().toPredicate();
        org.junit.Assert.assertEquals(queryParameterValue1, org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId1, predicate));
        org.junit.Assert.assertFalse(queryParameterValue2.equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId1, predicate)));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue("queryParameterIdNotFound", predicate));
        org.junit.Assert.assertEquals(queryParameterValue3, org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId3, predicate));
    }

    private static boolean eq(java.lang.Object left, java.lang.Object right) {
        return left == null ? right == null : (right != null) && left.equals(right);
    }

    public static class Matcher {
        public static org.apache.ambari.server.controller.ClusterRequest getClusterRequest(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.ClusterRequestMatcher(clusterId, clusterName, stackVersion, hostNames));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.ClusterRequest> getClusterRequestSet(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String provisioningState, org.apache.ambari.server.state.SecurityType securityType, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.ClusterRequestSetMatcher(clusterId, clusterName, provisioningState, securityType, stackVersion, hostNames));
            return null;
        }

        public static org.apache.ambari.server.controller.ConfigurationRequest getConfigurationRequest(java.lang.String clusterName, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> configs, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.ConfigurationRequestMatcher(clusterName, type, tag, configs, configAttributes));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.HostRequest> getHostRequestSet(java.lang.String hostname, java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.HostRequestSetMatcher(hostname, clusterName, hostAttributes));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> getHostComponentRequestSet(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName, java.util.Map<java.lang.String, java.lang.String> configVersions, java.lang.String desiredState) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.HostComponentRequestSetMatcher(clusterName, serviceName, componentName, hostName, configVersions, desiredState));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.UserRequest> getUserRequestSet(java.lang.String name) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.UserRequestSetMatcher(name));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.GroupRequest> getGroupRequestSet(java.lang.String name) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.GroupRequestSetMatcher(name));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.MemberRequest> getMemberRequestSet(java.lang.String groupname, java.lang.String username) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.MemberRequestSetMatcher(groupname, username));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.StackConfigurationRequest> getStackConfigurationRequestSet(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.StackConfigurationRequestSetMatcher(stackName, stackVersion, serviceName, propertyName));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyRequest> getStackConfigurationDependencyRequestSet(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName, java.lang.String dependencyName) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.StackConfigurationDependencyRequestSetMatcher(stackName, stackVersion, serviceName, propertyName, dependencyName));
            return null;
        }

        public static java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest> getStackLevelConfigurationRequestSet(java.lang.String stackName, java.lang.String stackVersion, java.lang.String propertyName) {
            org.easymock.EasyMock.reportMatcher(new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.StackLevelConfigurationRequestSetMatcher(stackName, stackVersion, propertyName));
            return null;
        }
    }

    public static class ClusterRequestMatcher extends org.apache.ambari.server.controller.ClusterRequest implements org.easymock.IArgumentMatcher {
        public ClusterRequestMatcher(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
            super(clusterId, clusterName, stackVersion, hostNames);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            return ((((o instanceof org.apache.ambari.server.controller.ClusterRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (o)).getClusterId(), getClusterId())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (o)).getClusterName(), getClusterName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (o)).getStackVersion(), getStackVersion())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (o)).getHostNames(), getHostNames());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("ClusterRequestMatcher(").append(super.toString()).append(")");
        }
    }

    public static class ClusterRequestSetMatcher extends org.apache.ambari.server.controller.ClusterRequest implements org.easymock.IArgumentMatcher {
        public ClusterRequestSetMatcher(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String provisioningState, org.apache.ambari.server.state.SecurityType securityType, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
            super(clusterId, clusterName, provisioningState, securityType, stackVersion, hostNames);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return ((org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (request)).getClusterId(), getClusterId()) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (request)).getClusterName(), getClusterName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (request)).getStackVersion(), getStackVersion())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ClusterRequest) (request)).getHostNames(), getHostNames());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("ClusterRequestSetMatcher(").append(super.toString()).append(")");
        }
    }

    public static class ConfigurationRequestMatcher extends org.apache.ambari.server.controller.ConfigurationRequest implements org.easymock.IArgumentMatcher {
        public ConfigurationRequestMatcher(java.lang.String clusterName, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> configs, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configsAttributes) {
            super(clusterName, type, tag, configs, configsAttributes);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            return (((((o instanceof org.apache.ambari.server.controller.ConfigurationRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ConfigurationRequest) (o)).getClusterName(), getClusterName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ConfigurationRequest) (o)).getType(), getType())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ConfigurationRequest) (o)).getVersionTag(), getVersionTag())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ConfigurationRequest) (o)).getProperties(), getProperties())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ConfigurationRequest) (o)).getPropertiesAttributes(), getPropertiesAttributes());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("ConfigurationRequestMatcher(").append(super.toString()).append(")");
        }
    }

    public static class HostRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.HostRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.HostRequest hostRequest;

        public HostRequestSetMatcher(java.lang.String hostname, java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
            hostRequest = new org.apache.ambari.server.controller.HostRequest(hostname, clusterName);
            add(hostRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return ((request instanceof org.apache.ambari.server.controller.HostRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.HostRequest) (request)).getClusterName(), hostRequest.getClusterName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.HostRequest) (request)).getHostname(), hostRequest.getHostname());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("HostRequestSetMatcher(").append(hostRequest).append(")");
        }
    }

    public static class HostComponentRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.ServiceComponentHostRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.ServiceComponentHostRequest hostComponentRequest;

        public HostComponentRequestSetMatcher(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName, java.util.Map<java.lang.String, java.lang.String> configVersions, java.lang.String desiredState) {
            hostComponentRequest = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostName, desiredState);
            add(hostComponentRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return (((((request instanceof org.apache.ambari.server.controller.ServiceComponentHostRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ServiceComponentHostRequest) (request)).getClusterName(), hostComponentRequest.getClusterName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ServiceComponentHostRequest) (request)).getServiceName(), hostComponentRequest.getServiceName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ServiceComponentHostRequest) (request)).getComponentName(), hostComponentRequest.getComponentName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ServiceComponentHostRequest) (request)).getHostname(), hostComponentRequest.getHostname())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.ServiceComponentHostRequest) (request)).getDesiredState(), hostComponentRequest.getDesiredState());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("HostComponentRequestSetMatcher(").append(hostComponentRequest).append(")");
        }
    }

    public static class UserRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.UserRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.UserRequest userRequest;

        public UserRequestSetMatcher(java.lang.String name) {
            userRequest = new org.apache.ambari.server.controller.UserRequest(name);
            add(userRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return (request instanceof org.apache.ambari.server.controller.UserRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.UserRequest) (request)).getUsername(), userRequest.getUsername());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("UserRequestSetMatcher(").append(userRequest).append(")");
        }
    }

    public static class GroupRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.GroupRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.GroupRequest groupRequest;

        public GroupRequestSetMatcher(java.lang.String name) {
            groupRequest = new org.apache.ambari.server.controller.GroupRequest(name);
            add(groupRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return (request instanceof org.apache.ambari.server.controller.GroupRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.GroupRequest) (request)).getGroupName(), groupRequest.getGroupName());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("GroupRequestSetMatcher(").append(groupRequest).append(")");
        }
    }

    public static class MemberRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.MemberRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.MemberRequest memberRequest;

        public MemberRequestSetMatcher(java.lang.String groupname, java.lang.String username) {
            memberRequest = new org.apache.ambari.server.controller.MemberRequest(groupname, username);
            add(memberRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return ((request instanceof org.apache.ambari.server.controller.MemberRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.MemberRequest) (request)).getGroupName(), memberRequest.getGroupName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.MemberRequest) (request)).getUserName(), memberRequest.getUserName());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("MemberRequestSetMatcher(").append(memberRequest).append(")");
        }
    }

    public static class StackConfigurationRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.StackConfigurationRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.StackConfigurationRequest stackConfigurationRequest;

        public StackConfigurationRequestSetMatcher(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
            stackConfigurationRequest = new org.apache.ambari.server.controller.StackConfigurationRequest(stackName, stackVersion, serviceName, propertyName);
            add(stackConfigurationRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return ((((request instanceof org.apache.ambari.server.controller.StackConfigurationRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getPropertyName(), stackConfigurationRequest.getPropertyName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getServiceName(), stackConfigurationRequest.getServiceName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getStackName(), stackConfigurationRequest.getStackName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getStackVersion(), stackConfigurationRequest.getStackVersion());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("StackConfigurationRequestSetMatcher(").append(stackConfigurationRequest).append(")");
        }
    }

    public static class StackConfigurationDependencyRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.StackConfigurationRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.StackConfigurationDependencyRequest stackConfigurationDependencyRequest;

        public StackConfigurationDependencyRequestSetMatcher(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName, java.lang.String dependencyName) {
            stackConfigurationDependencyRequest = new org.apache.ambari.server.controller.StackConfigurationDependencyRequest(stackName, stackVersion, serviceName, propertyName, dependencyName);
            add(stackConfigurationDependencyRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return ((((request instanceof org.apache.ambari.server.controller.StackConfigurationRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getPropertyName(), stackConfigurationDependencyRequest.getPropertyName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getServiceName(), stackConfigurationDependencyRequest.getServiceName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getStackName(), stackConfigurationDependencyRequest.getStackName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackConfigurationRequest) (request)).getStackVersion(), stackConfigurationDependencyRequest.getStackVersion());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("StackConfigurationRequestSetMatcher(").append(stackConfigurationDependencyRequest).append(")");
        }
    }

    public static class StackLevelConfigurationRequestSetMatcher extends java.util.HashSet<org.apache.ambari.server.controller.StackLevelConfigurationRequest> implements org.easymock.IArgumentMatcher {
        private final org.apache.ambari.server.controller.StackLevelConfigurationRequest stackLevelConfigurationRequest;

        public StackLevelConfigurationRequestSetMatcher(java.lang.String stackName, java.lang.String stackVersion, java.lang.String propertyName) {
            stackLevelConfigurationRequest = new org.apache.ambari.server.controller.StackLevelConfigurationRequest(stackName, stackVersion, propertyName);
            add(stackLevelConfigurationRequest);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            if (!(o instanceof java.util.Set)) {
                return false;
            }
            java.util.Set set = ((java.util.Set) (o));
            if (set.size() != 1) {
                return false;
            }
            java.lang.Object request = set.iterator().next();
            return (((request instanceof org.apache.ambari.server.controller.StackLevelConfigurationRequest) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackLevelConfigurationRequest) (request)).getPropertyName(), stackLevelConfigurationRequest.getPropertyName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackLevelConfigurationRequest) (request)).getStackName(), stackLevelConfigurationRequest.getStackName())) && org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.eq(((org.apache.ambari.server.controller.StackLevelConfigurationRequest) (request)).getStackVersion(), stackLevelConfigurationRequest.getStackVersion());
        }

        @java.lang.Override
        public void appendTo(java.lang.StringBuffer stringBuffer) {
            stringBuffer.append("StackLevelConfigurationRequestSetMatcher(").append(stackLevelConfigurationRequest).append(")");
        }
    }

    public static class TestObserver implements org.apache.ambari.server.controller.internal.ResourceProviderObserver {
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = null;

        @java.lang.Override
        public void update(org.apache.ambari.server.controller.internal.ResourceProviderEvent event) {
            lastEvent = event;
        }

        public org.apache.ambari.server.controller.internal.ResourceProviderEvent getLastEvent() {
            return lastEvent;
        }
    }

    private static org.apache.ambari.server.controller.spi.Resource.Type testResourceType = new org.apache.ambari.server.controller.spi.Resource.Type("testResource");

    private static java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ "ClusterName", "ResourceName" }));

    private static java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ "ClusterName", "ResourceName", "SomeProperty", "SomeOtherProperty" }));

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();

    static {
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, "ClusterName");
        keyPropertyIds.put(testResourceType, "ResourceName");
    }

    private static java.util.Set<org.apache.ambari.server.controller.spi.Resource> allResources = new java.util.HashSet<>();

    static {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(testResourceType);
        resource.setProperty("ClusterName", "c1");
        resource.setProperty("ResourceName", "r1");
        resource.setProperty("SomeProperty", "SomeValue1");
        resource.setProperty("SomeOtherProperty", 10);
        allResources.add(resource);
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(testResourceType);
        resource.setProperty("ClusterName", "c1");
        resource.setProperty("ResourceName", "r2");
        resource.setProperty("SomeProperty", "SomeValue2");
        resource.setProperty("SomeOtherProperty", 100);
        allResources.add(resource);
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(testResourceType);
        resource.setProperty("ClusterName", "c1");
        resource.setProperty("ResourceName", "r3");
        resource.setProperty("SomeProperty", "SomeValue3");
        resource.setProperty("SomeOtherProperty", 1000);
        allResources.add(resource);
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(testResourceType);
        resource.setProperty("ClusterName", "c1");
        resource.setProperty("ResourceName", "r4");
        resource.setProperty("SomeProperty", "SomeValue4");
        resource.setProperty("SomeOtherProperty", 9999);
        allResources.add(resource);
    }

    public static class TestResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
        protected TestResourceProvider() {
            super(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.propertyIds, org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.keyPropertyIds);
        }

        @java.lang.Override
        protected java.util.Set<java.lang.String> getPKPropertyIds() {
            return org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.pkPropertyIds;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
            for (org.apache.ambari.server.controller.spi.Resource resource : org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.allResources) {
                if (predicate.evaluate(resource)) {
                    resources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(resource, request.getPropertyIds()));
                }
            }
            return resources;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
        }
    }
}