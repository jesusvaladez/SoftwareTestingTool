package org.apache.ambari.server.api.query;
public class ProcessingPredicateVisitorTest {
    @org.junit.Test
    public void testGetProcessedPredicate() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.StackResourceDefinition();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, "HDP");
        org.apache.ambari.server.api.query.QueryImpl instance = new org.apache.ambari.server.api.query.QueryImplTest.TestQuery(mapIds, resourceDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("Stacks/stack_name").equals("HDP").and().property("versions/stackServices/StackServices/service_name").equals("HBASE").and().property("versions/operatingSystems/OperatingSystems/os_type").equals("centos5").toPredicate();
        org.apache.ambari.server.api.query.ProcessingPredicateVisitor visitor = new org.apache.ambari.server.api.query.ProcessingPredicateVisitor(instance);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        org.apache.ambari.server.controller.spi.Predicate processedPredicate = visitor.getProcessedPredicate();
        org.apache.ambari.server.controller.spi.Predicate expectedPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("Stacks/stack_name").equals("HDP").toPredicate();
        org.junit.Assert.assertEquals(expectedPredicate, processedPredicate);
    }

    @org.junit.Test
    public void testGetSubResourceForNotPredicate() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.HostResourceDefinition();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, null);
        org.apache.ambari.server.api.query.QueryImpl instance = new org.apache.ambari.server.api.query.QueryImplTest.TestQuery(mapIds, resourceDefinition);
        org.apache.ambari.server.controller.spi.Predicate notPredicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().not().property("host_components/HostRoles/component_name").equals("ZOOKEEPER_SERVER").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate notPredicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().not().property("host_components/HostRoles/component_name").equals("HBASE_MASTER").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(notPredicate1, notPredicate2);
        org.apache.ambari.server.api.query.ProcessingPredicateVisitor visitor = new org.apache.ambari.server.api.query.ProcessingPredicateVisitor(instance);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(andPredicate, visitor);
        java.util.Set<java.lang.String> categories = visitor.getSubResourceCategories();
        org.junit.Assert.assertEquals(categories.iterator().next(), "host_components");
    }

    @org.junit.Test
    public void testGetSubResourceCategories() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.StackResourceDefinition();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, "HDP");
        org.apache.ambari.server.api.query.QueryImpl instance = new org.apache.ambari.server.api.query.QueryImplTest.TestQuery(mapIds, resourceDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("Stacks/stack_name").equals("HDP").and().property("versions/stackServices/StackServices/service_name").equals("HBASE").and().property("versions/operatingSystems/OperatingSystems/os_type").equals("centos5").toPredicate();
        org.apache.ambari.server.api.query.ProcessingPredicateVisitor visitor = new org.apache.ambari.server.api.query.ProcessingPredicateVisitor(instance);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        java.util.Set<java.lang.String> categories = visitor.getSubResourceCategories();
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>();
        expected.add("versions");
        org.junit.Assert.assertEquals(expected, categories);
    }

    @org.junit.Test
    public void testGetSubResourceProperties() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.StackResourceDefinition();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, "HDP");
        org.apache.ambari.server.api.query.QueryImpl instance = new org.apache.ambari.server.api.query.QueryImplTest.TestQuery(mapIds, resourceDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("Stacks/stack_name").equals("HDP").and().property("versions/stackServices/StackServices/service_name").equals("HBASE").and().property("versions/operatingSystems/OperatingSystems/os_type").equals("centos5").toPredicate();
        org.apache.ambari.server.api.query.ProcessingPredicateVisitor visitor = new org.apache.ambari.server.api.query.ProcessingPredicateVisitor(instance);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        java.util.Set<java.lang.String> properties = visitor.getSubResourceProperties();
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>();
        expected.add("versions/stackServices/StackServices/service_name");
        expected.add("versions/operatingSystems/OperatingSystems/os_type");
        org.junit.Assert.assertEquals(expected, properties);
    }
}