package org.apache.ambari.server.api.query;
public class SubResourcePredicateVisitorTest {
    @org.junit.Test
    public void testGetSubResourcePredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("ServiceInfo/service_name").equals("HBASE").and().property("components/ServiceComponentInfo/category").equals("SLAVE").and().property("components/host_components/metrics/cpu/cpu_num").greaterThanEqualTo(1).toPredicate();
        org.apache.ambari.server.api.query.SubResourcePredicateVisitor visitor = new org.apache.ambari.server.api.query.SubResourcePredicateVisitor("components");
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        org.apache.ambari.server.controller.spi.Predicate subResourcePredicate = visitor.getSubResourcePredicate();
        org.apache.ambari.server.controller.spi.Predicate expectedPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("ServiceComponentInfo/category").equals("SLAVE").and().property("host_components/metrics/cpu/cpu_num").greaterThanEqualTo(1).toPredicate();
        org.junit.Assert.assertEquals(expectedPredicate, subResourcePredicate);
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property("ServiceInfo/service_name").equals("HBASE").and().property("ServiceInfo/component_name").equals("HBASE_MASTER").toPredicate();
        visitor = new org.apache.ambari.server.api.query.SubResourcePredicateVisitor("components");
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        subResourcePredicate = visitor.getSubResourcePredicate();
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.AndPredicate(), subResourcePredicate);
        org.apache.ambari.server.controller.spi.Predicate notPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().not().property("host_components/HostRoles/component_name").equals("ZOOKEEPER_SERVER").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate expectedNotPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().not().property("HostRoles/component_name").equals("ZOOKEEPER_SERVER").toPredicate();
        visitor = new org.apache.ambari.server.api.query.SubResourcePredicateVisitor("host_components");
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(notPredicate, visitor);
        subResourcePredicate = visitor.getSubResourcePredicate();
        org.junit.Assert.assertEquals(expectedNotPredicate, subResourcePredicate);
    }
}