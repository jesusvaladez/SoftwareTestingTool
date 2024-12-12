package org.apache.ambari.server.controller.internal;
public class RequestImplTest {
    private static final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();

    static {
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c2", "p3"));
        propertyIds.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c3", "p4"));
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        org.apache.ambari.server.controller.internal.DefaultProviderModule defaultProviderModule = injector.getInstance(org.apache.ambari.server.controller.internal.DefaultProviderModule.class);
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            try {
                defaultProviderModule.getResourceProvider(type);
            } catch (java.lang.Exception exception) {
            }
        }
    }

    @org.junit.Test
    public void testGetPropertyIds() {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RequestImplTest.propertyIds);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.RequestImplTest.propertyIds, request.getPropertyIds());
    }

    @org.junit.Test
    public void testValidPropertyIds() {
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent));
        java.util.Set<java.lang.String> validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("HostRoles/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("params/run_smoke_test"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/actual_configs"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/desired_stack_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/desired_repository_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/desired_state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/component_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/host_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("HostRoles/role_id"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Cluster));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Clusters/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Clusters/cluster_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Clusters/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Clusters/version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Clusters/state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Clusters/desired_configs"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Service));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("ServiceInfo/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceInfo/service_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceInfo/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceInfo/state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("params/run_smoke_test"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("params/reconfigure_client"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Hosts/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/host_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/ip"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/attributes"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/total_mem"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/cpu_count"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/ph_cpu_count"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/os_arch"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/os_type"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/rack_info"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/last_heartbeat_time"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/last_agent_env"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/last_registration_time"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/disk_info"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/host_status"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/host_health_report"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/public_host_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/host_state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Hosts/desired_configs"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("ServiceComponentInfo/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceComponentInfo/service_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceComponentInfo/component_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceComponentInfo/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceComponentInfo/state"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("ServiceComponentInfo/display_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("params/run_smoke_test"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Action));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Action/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/action_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/action_type"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/inputs"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/target_service"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/target_component"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/description"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/target_type"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Actions/default_timeout"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Request));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Requests/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Requests/id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Requests/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Requests/request_status"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Requests/request_context"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Task));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Task/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/request_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/cluster_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/stage_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/host_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/command"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/role"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/status"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/exit_code"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/stderr"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/stdout"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/start_time"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Tasks/attempt_cnt"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.User));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Users/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Users/user_name"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Stack));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Stacks/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Stacks/stack_name"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Versions/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Versions/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Versions/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Versions/min_upgrade_version"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.propertyIds);
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("OperatingSystems/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("OperatingSystems/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("OperatingSystems/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("OperatingSystems/os_type"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.propertyIds);
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("Repositories/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/os_type"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/base_url"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/repo_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/repo_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("Repositories/mirrors_list"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.StackService));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("StackServices/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/service_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/user_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/comments"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServices/service_version"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("StackConfigurations/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/service_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/property_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/property_description"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackConfigurations/property_value"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent));
        validPropertyIds = request.getPropertyIds();
        junit.framework.Assert.assertFalse(validPropertyIds.contains("StackServiceComponents/unsupported_property_id"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/stack_version"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/stack_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/service_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/component_name"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/component_category"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/is_master"));
        junit.framework.Assert.assertTrue(validPropertyIds.contains("StackServiceComponents/is_client"));
    }

    @org.junit.Test
    public void testDryRunRequest() {
        org.apache.ambari.server.controller.spi.Request dryRunRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.emptySet(), java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "true"));
        org.apache.ambari.server.controller.spi.Request nonDryRunReqest1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.emptySet(), java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, "false"));
        org.apache.ambari.server.controller.spi.Request nonDryRunReqest2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.emptySet(), java.util.Collections.emptyMap());
        junit.framework.Assert.assertTrue(dryRunRequest.isDryRunRequest());
        junit.framework.Assert.assertFalse(nonDryRunReqest1.isDryRunRequest());
        junit.framework.Assert.assertFalse(nonDryRunReqest2.isDryRunRequest());
    }
}