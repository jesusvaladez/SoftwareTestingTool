package org.apache.ambari.server.controller.utilities;
public class PredicateBuilderTest {
    @org.junit.Test
    public void testSimple() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("bar").toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testSimpleNot() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.not().property(p1).equals("foo").toPredicate();
        junit.framework.Assert.assertFalse(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.not().property(p1).equals("bar").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testDone() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property(p1).equals("foo").toPredicate();
        try {
            pb.property(p1).equals("foo").toPredicate();
            junit.framework.Assert.fail("Expected IllegalStateException.");
        } catch (java.lang.IllegalStateException e) {
        }
        junit.framework.Assert.assertSame(predicate, pb.toPredicate());
    }

    @org.junit.Test
    public void testSimpleAnd() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").and().property(p2).equals("bar").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").and().property(p2).equals("car").toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testSimpleAndNot() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").and().not().property(p2).equals("bar").toPredicate();
        junit.framework.Assert.assertFalse(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").and().not().property(p2).equals("car").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLongAnd() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop4");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        resource.setProperty(p4, "dog");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").and().property(p2).equals("bar").and().property(p3).equals("cat").and().property(p4).equals("dog").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").and().property(p2).equals("bar").and().property(p3).equals("cat").and().property(p4).equals("dot").toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testSimpleOr() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").or().property(p2).equals("bar").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").or().property(p2).equals("car").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb3 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate3 = pb3.property(p1).equals("fun").or().property(p2).equals("car").toPredicate();
        junit.framework.Assert.assertFalse(predicate3.evaluate(resource));
    }

    @org.junit.Test
    public void testLongOr() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop4");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        resource.setProperty(p4, "dog");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").or().property(p2).equals("bar").or().property(p3).equals("cat").or().property(p4).equals("dog").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").or().property(p2).equals("car").or().property(p3).equals("cat").or().property(p4).equals("dog").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb3 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate3 = pb3.property(p1).equals("fun").or().property(p2).equals("car").or().property(p3).equals("bat").or().property(p4).equals("dot").toPredicate();
        junit.framework.Assert.assertFalse(predicate3.evaluate(resource));
    }

    @org.junit.Test
    public void testAndOr() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb1.property(p1).equals("foo").and().property(p2).equals("bar").or().property(p3).equals("cat").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("foo").and().property(p2).equals("car").or().property(p3).equals("cat").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb3 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate3 = pb3.property(p1).equals("foo").and().property(p2).equals("bar").or().property(p3).equals("can").toPredicate();
        junit.framework.Assert.assertTrue(predicate3.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb4 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate4 = pb4.property(p1).equals("foo").and().property(p2).equals("bat").or().property(p3).equals("can").toPredicate();
        junit.framework.Assert.assertFalse(predicate4.evaluate(resource));
    }

    @org.junit.Test
    public void testBlocks() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb1.begin().property(p1).equals("foo").and().property(p2).equals("bar").end().or().property(p3).equals("cat").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.begin().property(p1).equals("foo").and().property(p2).equals("bat").end().or().property(p3).equals("cat").toPredicate();
        junit.framework.Assert.assertTrue(predicate2.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb3 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate3 = pb3.begin().property(p1).equals("foo").and().property(p2).equals("bar").end().or().property(p3).equals("can").toPredicate();
        junit.framework.Assert.assertTrue(predicate3.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb4 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate4 = pb4.begin().property(p1).equals("foo").and().property(p2).equals("bat").end().or().property(p3).equals("can").toPredicate();
        junit.framework.Assert.assertFalse(predicate4.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb5 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate5 = pb5.property(p1).equals("foo").and().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().toPredicate();
        junit.framework.Assert.assertTrue(predicate5.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb6 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate6 = pb6.property(p1).equals("foo").and().begin().property(p2).equals("bat").or().property(p3).equals("cat").end().toPredicate();
        junit.framework.Assert.assertTrue(predicate6.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb7 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate7 = pb7.property(p1).equals("foo").and().begin().property(p2).equals("bat").or().property(p3).equals("can").end().toPredicate();
        junit.framework.Assert.assertFalse(predicate7.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb8 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate8 = pb8.property(p1).equals("fat").and().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().toPredicate();
        junit.framework.Assert.assertFalse(predicate8.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb9 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate9 = pb9.property(p1).equals("foo").and().not().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().toPredicate();
        junit.framework.Assert.assertFalse(predicate9.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb10 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate10 = pb10.property(p1).equals("foo").and().not().begin().property(p2).equals("bat").or().property(p3).equals("car").end().toPredicate();
        junit.framework.Assert.assertTrue(predicate10.evaluate(resource));
    }

    @org.junit.Test
    public void testNestedBlocks() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop4");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        resource.setProperty(p4, "dog");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb1.begin().property(p1).equals("foo").and().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().end().or().property(p4).equals("dog").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.begin().property(p1).equals("fat").and().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().end().or().property(p4).equals("dot").toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testUnbalancedBlocks() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop4");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        resource.setProperty(p4, "dog");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        try {
            pb1.begin().property(p1).equals("foo").and().begin().property(p2).equals("bar").or().property(p3).equals("cat").end().or().property(p4).equals("dog").toPredicate();
            junit.framework.Assert.fail("Expected IllegalStateException.");
        } catch (java.lang.IllegalStateException e) {
        }
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        try {
            pb2.begin().property(p1).equals("foo").and().property(p2).equals("bar").or().property(p3).equals("cat").end().end().or().property(p4).equals("dog").toPredicate();
            junit.framework.Assert.fail("Expected IllegalStateException.");
        } catch (java.lang.IllegalStateException e) {
        }
    }

    @org.junit.Test
    public void testAltProperty() {
        java.lang.String p1 = "cat1/prop1";
        java.lang.String p2 = "cat1/prop2";
        java.lang.String p3 = "prop3";
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, "bar");
        resource.setProperty(p3, "cat");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb1.begin().property("cat1/prop1").equals("foo").and().property("cat1/prop2").equals("bar").end().or().property("prop3").equals("cat").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
    }

    @org.junit.Test
    public void testEqualsString() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, "foo");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals("foo").toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals("bar").toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testEqualsInteger() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 1);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals(1).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals(99).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testEqualsFloat() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, ((float) (1)));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals(java.lang.Float.valueOf(1)).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals(java.lang.Float.valueOf(99)).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testEqualsDouble() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 1.999);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals(java.lang.Double.valueOf(1.999)).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals(java.lang.Double.valueOf(99.998)).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testEqualsLong() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 1L);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).equals(1L).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).equals(99L).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterInteger() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThan(1).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThan(99).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterFloat() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, ((float) (2)));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThan(((float) (1))).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThan(((float) (99))).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterDouble() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2.999);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThan(1.999).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThan(99.998).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterLong() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2L);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThan(1L).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThan(99L).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterThanEqualToInteger() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThanEqualTo(1).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThanEqualTo(99).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterThanEqualToFloat() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, ((float) (2)));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThanEqualTo(((float) (1))).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThanEqualTo(((float) (99))).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterThanEqualToDouble() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2.999);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThanEqualTo(1.999).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThanEqualTo(99.998).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testGreaterThanEqualToLong() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2L);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).greaterThanEqualTo(1L).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).greaterThanEqualTo(99L).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessInteger() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThan(99).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThan(1).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessFloat() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, ((float) (2)));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThan(((float) (99))).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThan(((float) (1))).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessDouble() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2.999);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThan(99.999).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThan(1.998).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessLong() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2L);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThan(99L).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThan(1L).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessThanEqualToInteger() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThanEqualTo(99).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThanEqualTo(1).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessThanEqualToFloat() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, ((float) (2)));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThanEqualTo(((float) (99))).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThanEqualTo(((float) (1))).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessThanEqualToDouble() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2.999);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThanEqualTo(99.999).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThanEqualTo(1.998).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }

    @org.junit.Test
    public void testLessThanEqualToLong() {
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat1", "prop1");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(p1, 2L);
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate1 = pb.property(p1).lessThanEqualTo(99L).toPredicate();
        junit.framework.Assert.assertTrue(predicate1.evaluate(resource));
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = pb2.property(p1).lessThanEqualTo(1L).toPredicate();
        junit.framework.Assert.assertFalse(predicate2.evaluate(resource));
    }
}