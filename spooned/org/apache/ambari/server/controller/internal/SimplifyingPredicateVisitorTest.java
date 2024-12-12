package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class SimplifyingPredicateVisitorTest {
    private static final java.lang.String PROPERTY_A = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "A");

    private static final java.lang.String PROPERTY_B = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "B");

    private static final java.lang.String PROPERTY_C = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "C");

    private static final java.lang.String PROPERTY_D = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "D");

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_A).equals("Monkey").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_B).equals("Runner").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_3 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_2);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_4 = new org.apache.ambari.server.controller.predicate.OrPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_2);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_5 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_C).equals("Racer").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_6 = new org.apache.ambari.server.controller.predicate.OrPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_5, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_4);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_7 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_C).equals("Power").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_8 = new org.apache.ambari.server.controller.predicate.OrPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_6, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_7);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_9 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_8);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_10 = new org.apache.ambari.server.controller.predicate.OrPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_3, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_5);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_11 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_4, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_10);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_12 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_D).equals("Installer").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_13 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_12);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_14 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_D).greaterThan(12).toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_15 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_14);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_16 = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate("cat1");

    @org.junit.Test
    public void testVisit() {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        org.easymock.Capture<java.util.Set<java.lang.String>> propertiesCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor visitor = new org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor(provider);
        EasyMock.expect(provider.checkPropertyIds(EasyMock.capture(propertiesCapture))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(provider);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, visitor);
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(1, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(0));
        java.util.Set<java.lang.String> setProps = propertiesCapture.getValue();
        junit.framework.Assert.assertEquals(1, setProps.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_A, setProps.iterator().next());
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_3, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(1, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_3, simplifiedPredicates.get(0));
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_4, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(2, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(0));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_2, simplifiedPredicates.get(1));
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_6, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(3, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_5, simplifiedPredicates.get(0));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(1));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_2, simplifiedPredicates.get(2));
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_8, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(4, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_5, simplifiedPredicates.get(0));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(1));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_2, simplifiedPredicates.get(2));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_7, simplifiedPredicates.get(3));
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_9, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(4, simplifiedPredicates.size());
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_11, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(4, simplifiedPredicates.size());
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_16, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(1, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_16, simplifiedPredicates.get(0));
        EasyMock.verify(provider);
        EasyMock.reset(provider);
        EasyMock.expect(provider.checkPropertyIds(EasyMock.capture(propertiesCapture))).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(provider.checkPropertyIds(EasyMock.capture(propertiesCapture))).andReturn(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PROPERTY_D));
        EasyMock.replay(provider);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_13, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(1, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(0));
        EasyMock.verify(provider);
        EasyMock.reset(provider);
        EasyMock.expect(provider.checkPropertyIds(EasyMock.capture(propertiesCapture))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(provider);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_15, visitor);
        simplifiedPredicates = visitor.getSimplifiedPredicates();
        junit.framework.Assert.assertEquals(1, simplifiedPredicates.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitorTest.PREDICATE_1, simplifiedPredicates.get(0));
        EasyMock.verify(provider);
    }
}