package org.apache.ambari.server.api.query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
public class JpaSortBuilderTest {
    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void before() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testSortDoesNotAddExtraRootPaths() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortRequestProperties = new java.util.ArrayList<>();
        sortRequestProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_TIMESTAMP, org.apache.ambari.server.controller.spi.SortRequest.Order.ASC));
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortRequestProperties);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_NAME).equals("foo").toPredicate();
        org.apache.ambari.server.api.query.JpaSortBuilderTest.MockAlertHistoryredicateVisitor visitor = new org.apache.ambari.server.api.query.JpaSortBuilderTest.MockAlertHistoryredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.AlertHistoryEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
        java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(sortRequest, visitor);
        junit.framework.Assert.assertEquals(sortOrders.size(), 1);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = visitor.getCriteriaQuery();
        java.util.Set<javax.persistence.criteria.Root<?>> roots = query.getRoots();
        junit.framework.Assert.assertEquals(1, roots.size());
    }

    private final class MockAlertHistoryredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.AlertHistoryEntity> {
        public MockAlertHistoryredicateVisitor() {
            super(m_injector.getInstance(javax.persistence.EntityManager.class), org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        }

        @java.lang.Override
        public java.lang.Class<org.apache.ambari.server.orm.entities.AlertHistoryEntity> getEntityClass() {
            return org.apache.ambari.server.orm.entities.AlertHistoryEntity.class;
        }

        @java.lang.Override
        public java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId) {
            return org.apache.ambari.server.orm.entities.AlertHistoryEntity_.getPredicateMapping().get(propertyId);
        }
    }
}