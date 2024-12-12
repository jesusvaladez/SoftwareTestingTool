package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.easymock.EasyMock;
public class AlertsDAOCachedTest {
    private static final java.lang.String HOST = "c6401.ambari.apache.org";

    private com.google.inject.Injector m_injector;

    private enum CachedAlertTestArea {

        FIND_ALL() {
            @java.lang.Override
            java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> execute(org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO) throws java.lang.Exception {
                return alertsDAO.findCurrent();
            }

            @java.lang.Override
            java.lang.String getNamedQuery() {
                return "AlertCurrentEntity.findAll";
            }
        },
        FIND_BY_DEFINITION_ID() {
            @java.lang.Override
            java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> execute(org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO) throws java.lang.Exception {
                return alertsDAO.findCurrentByDefinitionId(0L);
            }

            @java.lang.Override
            java.lang.String getNamedQuery() {
                return "AlertCurrentEntity.findByDefinitionId";
            }
        },
        FIND_BY_CLUSTER_ID() {
            @java.lang.Override
            java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> execute(org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO) throws java.lang.Exception {
                return alertsDAO.findCurrentByCluster(0L);
            }

            @java.lang.Override
            java.lang.String getNamedQuery() {
                return "AlertCurrentEntity.findByCluster";
            }
        },
        FIND_BY_SERVICE() {
            @java.lang.Override
            java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> execute(org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO) throws java.lang.Exception {
                return alertsDAO.findCurrentByService(0L, org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.HOST);
            }

            @java.lang.Override
            java.lang.String getNamedQuery() {
                return "AlertCurrentEntity.findByService";
            }
        };
        abstract java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> execute(org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO) throws java.lang.Exception;

        abstract java.lang.String getNamedQuery();
    }

    @org.junit.Before
    public void before() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.MockModule());
    }

    @org.junit.Test
    public void testFindAll() throws java.lang.Exception {
        testFindUsesCache(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea.FIND_ALL);
    }

    @org.junit.Test
    public void testFindByClusterId() throws java.lang.Exception {
        testFindUsesCache(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea.FIND_BY_CLUSTER_ID);
    }

    @org.junit.Test
    public void testFindByDefinitionId() throws java.lang.Exception {
        testFindUsesCache(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea.FIND_BY_DEFINITION_ID);
    }

    @org.junit.Test
    public void testFindByService() throws java.lang.Exception {
        testFindUsesCache(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea.FIND_BY_SERVICE);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testMergeIntoCacheOnly() throws java.lang.Exception {
        javax.persistence.EntityManager entityManager = m_injector.getInstance(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.dao.DaoUtils daoUtils = m_injector.getInstance(org.apache.ambari.server.orm.dao.DaoUtils.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        mock(definition, history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity jpaCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        jpaCurrent.setAlertHistory(history);
        jpaCurrent.setOriginalTimestamp(1L);
        jpaCurrent.setLatestTimestamp(2L);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity memoryCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        memoryCurrent.setAlertHistory(history);
        memoryCurrent.setOriginalTimestamp(1L);
        memoryCurrent.setLatestTimestamp(3L);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> typedQuery = org.easymock.EasyMock.createNiceMock(javax.persistence.TypedQuery.class);
        org.easymock.EasyMock.expect(entityManager.createNamedQuery(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea.FIND_ALL.getNamedQuery(), org.apache.ambari.server.orm.entities.AlertCurrentEntity.class)).andReturn(typedQuery).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> jpaCurrentAlerts = com.google.common.collect.Lists.newArrayList(jpaCurrent);
        org.easymock.EasyMock.expect(daoUtils.selectList(typedQuery)).andReturn(jpaCurrentAlerts).atLeastOnce();
        org.easymock.EasyMock.replay(entityManager, daoUtils, typedQuery);
        org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        alertsDAO.merge(memoryCurrent, true);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> testCurrentAlerts = alertsDAO.findCurrent();
        org.junit.Assert.assertEquals(1, testCurrentAlerts.size());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(3), testCurrentAlerts.get(0).getLatestTimestamp());
        org.easymock.EasyMock.verify(definition, history, entityManager, daoUtils);
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testFindUsesCache(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.CachedAlertTestArea testArea) throws java.lang.Exception {
        javax.persistence.EntityManager entityManager = m_injector.getInstance(javax.persistence.EntityManager.class);
        org.apache.ambari.server.orm.dao.DaoUtils daoUtils = m_injector.getInstance(org.apache.ambari.server.orm.dao.DaoUtils.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        mock(definition, history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity jpaCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        jpaCurrent.setAlertHistory(history);
        jpaCurrent.setOriginalTimestamp(1L);
        jpaCurrent.setLatestTimestamp(2L);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity memoryCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        memoryCurrent.setAlertHistory(history);
        memoryCurrent.setOriginalTimestamp(1L);
        memoryCurrent.setLatestTimestamp(3L);
        org.easymock.EasyMock.expect(entityManager.merge(memoryCurrent)).andReturn(memoryCurrent).atLeastOnce();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> typedQuery = org.easymock.EasyMock.createNiceMock(javax.persistence.TypedQuery.class);
        org.easymock.EasyMock.expect(entityManager.createNamedQuery(testArea.getNamedQuery(), org.apache.ambari.server.orm.entities.AlertCurrentEntity.class)).andReturn(typedQuery).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> jpaCurrentAlerts = com.google.common.collect.Lists.newArrayList(jpaCurrent);
        org.easymock.EasyMock.expect(daoUtils.selectList(typedQuery)).andReturn(jpaCurrentAlerts).atLeastOnce();
        org.easymock.EasyMock.replay(entityManager, daoUtils, typedQuery);
        org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        alertsDAO.merge(memoryCurrent);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> testCurrentAlerts = testArea.execute(alertsDAO);
        org.junit.Assert.assertEquals(1, testCurrentAlerts.size());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(3), testCurrentAlerts.get(0).getLatestTimestamp());
        org.easymock.EasyMock.verify(definition, history, entityManager, daoUtils);
    }

    private void mock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition, org.apache.ambari.server.orm.entities.AlertHistoryEntity history) {
        org.easymock.EasyMock.expect(definition.getDefinitionName()).andReturn("definitionName").atLeastOnce();
        org.easymock.EasyMock.expect(history.getClusterId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getHostName()).andReturn(org.apache.ambari.server.orm.dao.AlertsDAOCachedTest.HOST).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinitionId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertText()).andReturn("alertText").atLeastOnce();
        org.easymock.EasyMock.replay(definition, history);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
            org.easymock.EasyMock.expect(configuration.getAlertEventPublisherCorePoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_CORE_SIZE.getDefaultValue())).anyTimes();
            org.easymock.EasyMock.expect(configuration.getAlertEventPublisherMaxPoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_MAX_SIZE.getDefaultValue())).anyTimes();
            org.easymock.EasyMock.expect(configuration.getAlertEventPublisherWorkerQueueSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_WORKER_QUEUE_SIZE.getDefaultValue())).anyTimes();
            org.easymock.EasyMock.expect(configuration.isAlertCacheEnabled()).andReturn(java.lang.Boolean.TRUE).anyTimes();
            org.easymock.EasyMock.expect(configuration.getAlertCacheSize()).andReturn(100).anyTimes();
            org.easymock.EasyMock.replay(configuration);
            binder.bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addAlertDefinitionBinding().addLdapBindings().build().configure(binder);
        }
    }
}