package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ALERT_CACHING)
public class AlertsDAO implements org.apache.ambari.server.orm.dao.Cleanable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.AlertsDAO.class);

    private static final java.lang.String ALERT_COUNT_SQL_TEMPLATE = "SELECT NEW %s(" + ((((("SUM(CASE WHEN history.alertState = :okState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), " + "SUM(CASE WHEN history.alertState = :warningState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN history.alertState = :criticalState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN history.alertState = :unknownState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN alert.maintenanceState != :maintenanceStateOff THEN 1 ELSE 0 END)) ") + "FROM AlertCurrentEntity alert JOIN alert.alertHistory history WHERE history.clusterId = :clusterId");

    private static final java.lang.String ALERT_COUNT_PER_HOST_SQL_TEMPLATE = "SELECT NEW %s(" + (((((("history.hostName, " + "SUM(CASE WHEN history.alertState = :okState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN history.alertState = :warningState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN history.alertState = :criticalState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN history.alertState = :unknownState AND alert.maintenanceState = :maintenanceStateOff THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN alert.maintenanceState != :maintenanceStateOff THEN 1 ELSE 0 END)) ") + "FROM AlertCurrentEntity alert JOIN alert.alertHistory history WHERE history.clusterId = :clusterId GROUP BY history.hostName");

    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> m_entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils m_daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_alertEventPublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    private final org.apache.ambari.server.configuration.Configuration m_configuration;

    private com.google.common.cache.LoadingCache<org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey, org.apache.ambari.server.orm.entities.AlertCurrentEntity> m_currentAlertCache = null;

    private static final int BATCH_SIZE = 999;

    @com.google.inject.Inject
    public AlertsDAO(org.apache.ambari.server.configuration.Configuration configuration) {
        m_configuration = configuration;
        if (m_configuration.isAlertCacheEnabled()) {
            int maximumSize = m_configuration.getAlertCacheSize();
            org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Alert caching is enabled (size={}, flushInterval={}m)", maximumSize, m_configuration.getAlertCacheFlushInterval());
            m_currentAlertCache = com.google.common.cache.CacheBuilder.newBuilder().maximumSize(maximumSize).build(new com.google.common.cache.CacheLoader<org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey, org.apache.ambari.server.orm.entities.AlertCurrentEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.AlertCurrentEntity load(org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key) throws java.lang.Exception {
                    org.apache.ambari.server.orm.dao.AlertsDAO.LOG.debug("Cache miss for alert key {}, fetching from JPA", key);
                    final org.apache.ambari.server.orm.entities.AlertCurrentEntity alertCurrentEntity;
                    long clusterId = key.getClusterId();
                    java.lang.String alertDefinitionName = key.getAlertDefinitionName();
                    java.lang.String hostName = key.getHostName();
                    if (org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
                        alertCurrentEntity = findCurrentByNameNoHostInternalInJPA(clusterId, alertDefinitionName);
                    } else {
                        alertCurrentEntity = findCurrentByHostAndNameInJPA(clusterId, hostName, alertDefinitionName);
                    }
                    if (null == alertCurrentEntity) {
                        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.trace("Cache lookup failed for {} because the alert does not yet exist", key);
                        throw new org.apache.ambari.server.orm.dao.AlertsDAO.AlertNotYetCreatedException();
                    }
                    return alertCurrentEntity;
                }
            });
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertHistoryEntity findById(long alertId) {
        return m_entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class, alertId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAll", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        return m_daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> findAll(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAllInCluster", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        query.setParameter("clusterId", clusterId);
        return m_daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> findAll(long clusterId, java.util.List<org.apache.ambari.server.state.AlertState> alertStates) {
        if ((null == alertStates) || (alertStates.size() == 0)) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAllInClusterWithState", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("alertStates", alertStates);
        return m_daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> findAll(long clusterId, java.util.Date startDate, java.util.Date endDate) {
        if ((null == startDate) && (null == endDate)) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = null;
        if ((null != startDate) && (null != endDate)) {
            if (startDate.after(endDate)) {
                return java.util.Collections.emptyList();
            }
            query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAllInClusterBetweenDates", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
            query.setParameter("clusterId", clusterId);
            query.setParameter("startDate", startDate.getTime());
            query.setParameter("endDate", endDate.getTime());
        } else if (null != startDate) {
            query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAllInClusterAfterDate", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
            query.setParameter("clusterId", clusterId);
            query.setParameter("afterDate", startDate.getTime());
        } else if (null != endDate) {
            query = m_entityManagerProvider.get().createNamedQuery("AlertHistoryEntity.findAllInClusterBeforeDate", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
            query.setParameter("clusterId", clusterId);
            query.setParameter("beforeDate", endDate.getTime());
        }
        if (null == query) {
            return java.util.Collections.emptyList();
        }
        return m_daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> findAll(org.apache.ambari.server.controller.AlertHistoryRequest request) {
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        org.apache.ambari.server.orm.dao.AlertsDAO.HistoryPredicateVisitor visitor = new org.apache.ambari.server.orm.dao.AlertsDAO.HistoryPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(request.Predicate, visitor);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> query = visitor.getCriteriaQuery();
        javax.persistence.criteria.Predicate jpaPredicate = visitor.getJpaPredicate();
        if (null != jpaPredicate) {
            query.where(jpaPredicate);
        }
        org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.AlertHistoryEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
        java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(request.Sort, visitor);
        query.orderBy(sortOrders);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> typedQuery = entityManager.createQuery(query);
        if (null != request.Pagination) {
            typedQuery.setFirstResult(request.Pagination.getOffset());
            typedQuery.setMaxResults(request.Pagination.getPageSize());
        }
        return m_daoUtils.selectList(typedQuery);
    }

    @com.google.inject.persist.Transactional
    public java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> findAll(org.apache.ambari.server.controller.AlertCurrentRequest request) {
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        org.apache.ambari.server.orm.dao.AlertsDAO.CurrentPredicateVisitor visitor = new org.apache.ambari.server.orm.dao.AlertsDAO.CurrentPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(request.Predicate, visitor);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = visitor.getCriteriaQuery();
        javax.persistence.criteria.Predicate jpaPredicate = visitor.getJpaPredicate();
        if (null != jpaPredicate) {
            query.where(jpaPredicate);
        }
        org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.AlertCurrentEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
        java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(request.Sort, visitor);
        query.orderBy(sortOrders);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> typedQuery = entityManager.createQuery(query);
        if (null != request.Pagination) {
            int offset = request.Pagination.getOffset();
            if (offset < 0) {
                offset = 0;
            }
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(request.Pagination.getPageSize());
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = m_daoUtils.selectList(typedQuery);
        if (m_configuration.isAlertCacheEnabled()) {
            alerts = supplementWithCachedAlerts(alerts);
        }
        return alerts;
    }

    public int getCount(org.apache.ambari.server.controller.spi.Predicate predicate) {
        return 0;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> findCurrent() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findAll", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = m_daoUtils.selectList(query);
        if (m_configuration.isAlertCacheEnabled()) {
            alerts = supplementWithCachedAlerts(alerts);
        }
        return alerts;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertCurrentEntity findCurrentById(long alertId) {
        return m_entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class, alertId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> findCurrentByDefinitionId(long definitionId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByDefinitionId", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("definitionId", java.lang.Long.valueOf(definitionId));
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = m_daoUtils.selectList(query);
        if (m_configuration.isAlertCacheEnabled()) {
            alerts = supplementWithCachedAlerts(alerts);
        }
        return alerts;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> findCurrentByCluster(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByCluster", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = m_daoUtils.selectList(query);
        if (m_configuration.isAlertCacheEnabled()) {
            alerts = supplementWithCachedAlerts(alerts);
        }
        return alerts;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.dao.AlertSummaryDTO findCurrentCounts(long clusterId, java.lang.String serviceName, java.lang.String hostName) {
        java.lang.String sql = java.lang.String.format(org.apache.ambari.server.orm.dao.AlertsDAO.ALERT_COUNT_SQL_TEMPLATE, org.apache.ambari.server.orm.dao.AlertSummaryDTO.class.getName());
        java.lang.StringBuilder sb = new java.lang.StringBuilder(sql);
        if (null != serviceName) {
            sb.append(" AND history.serviceName = :serviceName");
        }
        if (null != hostName) {
            sb.append(" AND history.hostName = :hostName");
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.AlertSummaryDTO> query = m_entityManagerProvider.get().createQuery(sb.toString(), org.apache.ambari.server.orm.dao.AlertSummaryDTO.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("okState", org.apache.ambari.server.state.AlertState.OK);
        query.setParameter("warningState", org.apache.ambari.server.state.AlertState.WARNING);
        query.setParameter("criticalState", org.apache.ambari.server.state.AlertState.CRITICAL);
        query.setParameter("unknownState", org.apache.ambari.server.state.AlertState.UNKNOWN);
        query.setParameter("maintenanceStateOff", org.apache.ambari.server.state.MaintenanceState.OFF);
        if (null != serviceName) {
            query.setParameter("serviceName", serviceName);
        }
        if (null != hostName) {
            query.setParameter("hostName", hostName);
        }
        return m_daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO> findCurrentPerHostCounts(long clusterId) {
        java.lang.String sql = java.lang.String.format(org.apache.ambari.server.orm.dao.AlertsDAO.ALERT_COUNT_PER_HOST_SQL_TEMPLATE, org.apache.ambari.server.orm.dao.HostAlertSummaryDTO.class.getName());
        java.lang.StringBuilder sb = new java.lang.StringBuilder(sql);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.HostAlertSummaryDTO> query = m_entityManagerProvider.get().createQuery(sb.toString(), org.apache.ambari.server.orm.dao.HostAlertSummaryDTO.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("okState", org.apache.ambari.server.state.AlertState.OK);
        query.setParameter("warningState", org.apache.ambari.server.state.AlertState.WARNING);
        query.setParameter("criticalState", org.apache.ambari.server.state.AlertState.CRITICAL);
        query.setParameter("unknownState", org.apache.ambari.server.state.AlertState.UNKNOWN);
        query.setParameter("maintenanceStateOff", org.apache.ambari.server.state.MaintenanceState.OFF);
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO> map = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.dao.HostAlertSummaryDTO> resultList = m_daoUtils.selectList(query);
        for (org.apache.ambari.server.orm.dao.HostAlertSummaryDTO result : resultList) {
            map.put(result.getHostName(), result);
        }
        return map;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.dao.AlertHostSummaryDTO findCurrentHostCounts(long clusterId) {
        java.lang.String sql = java.lang.String.format(org.apache.ambari.server.orm.dao.AlertsDAO.ALERT_COUNT_PER_HOST_SQL_TEMPLATE, org.apache.ambari.server.orm.dao.HostAlertSummaryDTO.class.getName());
        java.lang.StringBuilder sb = new java.lang.StringBuilder(sql);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.HostAlertSummaryDTO> query = m_entityManagerProvider.get().createQuery(sb.toString(), org.apache.ambari.server.orm.dao.HostAlertSummaryDTO.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("okState", org.apache.ambari.server.state.AlertState.OK);
        query.setParameter("criticalState", org.apache.ambari.server.state.AlertState.CRITICAL);
        query.setParameter("warningState", org.apache.ambari.server.state.AlertState.WARNING);
        query.setParameter("unknownState", org.apache.ambari.server.state.AlertState.UNKNOWN);
        query.setParameter("maintenanceStateOff", org.apache.ambari.server.state.MaintenanceState.OFF);
        int okCount = 0;
        int warningCount = 0;
        int criticalCount = 0;
        int unknownCount = 0;
        java.util.List<org.apache.ambari.server.orm.dao.HostAlertSummaryDTO> resultList = m_daoUtils.selectList(query);
        for (org.apache.ambari.server.orm.dao.HostAlertSummaryDTO result : resultList) {
            if (result.getHostName() == null) {
                continue;
            }
            if (result.getCriticalCount() > 0) {
                criticalCount++;
            } else if (result.getWarningCount() > 0) {
                warningCount++;
            } else if (result.getUnknownCount() > 0) {
                unknownCount++;
            } else {
                okCount++;
            }
        }
        org.apache.ambari.server.orm.dao.AlertHostSummaryDTO hostSummary = new org.apache.ambari.server.orm.dao.AlertHostSummaryDTO(okCount, unknownCount, warningCount, criticalCount);
        return hostSummary;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> findCurrentByService(long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByService", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("inlist", java.util.EnumSet.of(org.apache.ambari.server.state.alert.Scope.ANY, org.apache.ambari.server.state.alert.Scope.SERVICE));
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = m_daoUtils.selectList(query);
        if (m_configuration.isAlertCacheEnabled()) {
            alerts = supplementWithCachedAlerts(alerts);
        }
        return alerts;
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity findCurrentByHostAndName(long clusterId, java.lang.String hostName, java.lang.String alertName) {
        if (m_configuration.isAlertCacheEnabled()) {
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = new org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey(clusterId, alertName, hostName);
            try {
                return m_currentAlertCache.get(key);
            } catch (java.util.concurrent.ExecutionException executionException) {
                java.lang.Throwable cause = executionException.getCause();
                if (!(cause instanceof org.apache.ambari.server.orm.dao.AlertsDAO.AlertNotYetCreatedException)) {
                    org.apache.ambari.server.orm.dao.AlertsDAO.LOG.warn("Unable to retrieve alert for key {} from the cache", key);
                }
            }
        }
        return findCurrentByHostAndNameInJPA(clusterId, hostName, alertName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    private org.apache.ambari.server.orm.entities.AlertCurrentEntity findCurrentByHostAndNameInJPA(long clusterId, java.lang.String hostName, java.lang.String alertName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByHostAndName", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("hostName", hostName);
        query.setParameter("definitionName", alertName);
        return m_daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void removeByDefinitionId(long definitionId) {
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentQuery = entityManager.createNamedQuery("AlertCurrentEntity.removeByDefinitionId", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        currentQuery.setParameter("definitionId", definitionId);
        currentQuery.executeUpdate();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> historyQuery = entityManager.createNamedQuery("AlertHistoryEntity.removeByDefinitionId", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        historyQuery.setParameter("definitionId", definitionId);
        historyQuery.executeUpdate();
        entityManager.clear();
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
    }

    @com.google.inject.persist.Transactional
    public int removeCurrentByHistoryId(long historyId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.removeByHistoryId", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("historyId", historyId);
        int rowsRemoved = query.executeUpdate();
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
        return rowsRemoved;
    }

    @com.google.inject.persist.Transactional
    public int removeCurrentDisabledAlerts() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findDisabled", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        int rowsRemoved = 0;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = m_daoUtils.selectList(query);
        if (currentEntities != null) {
            for (org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntity : currentEntities) {
                remove(currentEntity);
                rowsRemoved++;
            }
        }
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
        return rowsRemoved;
    }

    @com.google.inject.persist.Transactional
    public int removeCurrentByService(long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByServiceName", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("serviceName", serviceName);
        int removedItems = 0;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = m_daoUtils.selectList(query);
        if (currentEntities != null) {
            for (org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntity : currentEntities) {
                remove(currentEntity);
                removedItems++;
            }
        }
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
        m_alertEventPublisher.publish(new org.apache.ambari.server.events.AggregateAlertRecalculateEvent(clusterId));
        return removedItems;
    }

    @com.google.inject.persist.Transactional
    public int removeCurrentByHost(java.lang.String hostName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByHost", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("hostName", hostName);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = m_daoUtils.selectList(query);
        int removedItems = 0;
        if (currentEntities != null) {
            for (org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntity : currentEntities) {
                remove(currentEntity);
                removedItems++;
            }
        }
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
        try {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = m_clusters.get().getClusters();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Cluster> entry : clusters.entrySet()) {
                m_alertEventPublisher.publish(new org.apache.ambari.server.events.AggregateAlertRecalculateEvent(entry.getValue().getClusterId()));
            }
        } catch (java.lang.Exception ambariException) {
            org.apache.ambari.server.orm.dao.AlertsDAO.LOG.warn("Unable to recalcuate aggregate alerts after removing host {}", hostName);
        }
        return removedItems;
    }

    @com.google.inject.persist.Transactional
    public int removeCurrentByServiceComponentHost(long clusterId, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByHostComponent", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("hostName", hostName);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = m_daoUtils.selectList(query);
        int removedItems = 0;
        if (currentEntities != null) {
            for (org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntity : currentEntities) {
                remove(currentEntity);
                removedItems++;
            }
        }
        if (m_configuration.isAlertCacheEnabled()) {
            m_currentAlertCache.invalidateAll();
        }
        m_alertEventPublisher.publish(new org.apache.ambari.server.events.AggregateAlertRecalculateEvent(clusterId));
        return removedItems;
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertHistoryEntity alert) {
        m_entityManagerProvider.get().persist(alert);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertHistoryEntity alert) {
        m_entityManagerProvider.get().refresh(alert);
    }

    public org.apache.ambari.server.orm.entities.AlertHistoryEntity merge(org.apache.ambari.server.orm.entities.AlertHistoryEntity alert) {
        if (m_configuration.isAlertCacheEnabled()) {
            synchronized(this) {
                return mergeTransactional(alert);
            }
        } else {
            return mergeTransactional(alert);
        }
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.orm.entities.AlertHistoryEntity mergeTransactional(org.apache.ambari.server.orm.entities.AlertHistoryEntity alert) {
        return m_entityManagerProvider.get().merge(alert);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertHistoryEntity alert) {
        alert = merge(alert);
        removeCurrentByHistoryId(alert.getAlertId());
        m_entityManagerProvider.get().remove(alert);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert) {
        m_entityManagerProvider.get().persist(alert);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert) {
        m_entityManagerProvider.get().refresh(alert);
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity merge(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert) {
        if (m_configuration.isAlertCacheEnabled()) {
            synchronized(this) {
                return mergeTransactional(alert);
            }
        } else {
            return mergeTransactional(alert);
        }
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.orm.entities.AlertCurrentEntity mergeTransactional(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert) {
        alert = m_entityManagerProvider.get().merge(alert);
        if (m_configuration.isAlertCacheEnabled()) {
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey.build(alert);
            m_currentAlertCache.put(key, alert);
        }
        return alert;
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity merge(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert, boolean updateCacheOnly) {
        if (updateCacheOnly) {
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey.build(alert);
            if (!m_configuration.isAlertCacheEnabled()) {
                org.apache.ambari.server.orm.dao.AlertsDAO.LOG.error("Unable to update a cached alert instance for {} because cached alerts are not enabled", key);
            } else {
                m_currentAlertCache.put(key, alert);
                return alert;
            }
        }
        return merge(alert);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertCurrentEntity alert) {
        m_entityManagerProvider.get().remove(merge(alert));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.dao.AlertSummaryDTO findAggregateCounts(long clusterId, java.lang.String alertName) {
        java.lang.String sql = java.lang.String.format(org.apache.ambari.server.orm.dao.AlertsDAO.ALERT_COUNT_SQL_TEMPLATE, org.apache.ambari.server.orm.dao.AlertSummaryDTO.class.getName());
        java.lang.StringBuilder buffer = new java.lang.StringBuilder(sql);
        buffer.append(" AND history.alertDefinition.definitionName = :definitionName");
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.AlertSummaryDTO> query = m_entityManagerProvider.get().createQuery(buffer.toString(), org.apache.ambari.server.orm.dao.AlertSummaryDTO.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("okState", org.apache.ambari.server.state.AlertState.OK);
        query.setParameter("warningState", org.apache.ambari.server.state.AlertState.WARNING);
        query.setParameter("criticalState", org.apache.ambari.server.state.AlertState.CRITICAL);
        query.setParameter("unknownState", org.apache.ambari.server.state.AlertState.UNKNOWN);
        query.setParameter("maintenanceStateOff", org.apache.ambari.server.state.MaintenanceState.OFF);
        query.setParameter("definitionName", alertName);
        return m_daoUtils.selectSingle(query);
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity findCurrentByNameNoHost(long clusterId, java.lang.String alertName) {
        if (m_configuration.isAlertCacheEnabled()) {
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = new org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey(clusterId, alertName);
            try {
                return m_currentAlertCache.get(key);
            } catch (java.util.concurrent.ExecutionException executionException) {
                java.lang.Throwable cause = executionException.getCause();
                if (!(cause instanceof org.apache.ambari.server.orm.dao.AlertsDAO.AlertNotYetCreatedException)) {
                    org.apache.ambari.server.orm.dao.AlertsDAO.LOG.warn("Unable to retrieve alert for key {} from, the cache", key);
                }
            }
        }
        return findCurrentByNameNoHostInternalInJPA(clusterId, alertName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    private org.apache.ambari.server.orm.entities.AlertCurrentEntity findCurrentByNameNoHostInternalInJPA(long clusterId, java.lang.String alertName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> query = m_entityManagerProvider.get().createNamedQuery("AlertCurrentEntity.findByNameAndNoHost", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        query.setParameter("definitionName", alertName);
        return m_daoUtils.selectOne(query);
    }

    public void flushCachedEntitiesToJPA() {
        if (m_configuration.isAlertCacheEnabled()) {
            synchronized(this) {
                flushCachedEntitiesToJPATransactional();
            }
        } else {
            org.apache.ambari.server.orm.dao.AlertsDAO.LOG.warn("Unable to flush cached alerts to JPA because caching is not enabled");
        }
    }

    @com.google.inject.persist.Transactional
    protected void flushCachedEntitiesToJPATransactional() {
        long cachedEntityCount = m_currentAlertCache.size();
        java.util.concurrent.ConcurrentMap<org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey, org.apache.ambari.server.orm.entities.AlertCurrentEntity> map = m_currentAlertCache.asMap();
        java.util.Set<java.util.Map.Entry<org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey, org.apache.ambari.server.orm.entities.AlertCurrentEntity>> entries = map.entrySet();
        for (java.util.Map.Entry<org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey, org.apache.ambari.server.orm.entities.AlertCurrentEntity> entry : entries) {
            merge(entry.getValue());
        }
        m_currentAlertCache.invalidateAll();
        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Flushed {} cached alerts to the database", cachedEntityCount);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> supplementWithCachedAlerts(java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts) {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> cachedAlerts = new java.util.ArrayList<>(alerts.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity alert : alerts) {
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey.build(alert);
            org.apache.ambari.server.orm.entities.AlertCurrentEntity cachedEntity = m_currentAlertCache.getIfPresent(key);
            if (null != cachedEntity) {
                if (cachedEntity.getAlertHistory() == null) {
                    org.apache.ambari.server.orm.dao.AlertsDAO.LOG.warn("There is current entity with null history in the cache, currentId: {}, persisted historyId: {}", cachedEntity.getAlertId(), alert.getHistoryId());
                }
                alert = cachedEntity;
            }
            cachedAlerts.add(alert);
        }
        return cachedAlerts;
    }

    @com.google.inject.persist.Transactional
    @java.lang.Override
    public long cleanup(org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy policy) {
        long affectedRows = 0;
        java.lang.Long clusterId = null;
        try {
            clusterId = m_clusters.get().getCluster(policy.getClusterName()).getClusterId();
            affectedRows += cleanAlertNoticesForClusterBeforeDate(clusterId, policy.getToDateInMillis());
            affectedRows += cleanAlertCurrentsForClusterBeforeDate(clusterId, policy.getToDateInMillis());
            affectedRows += cleanAlertHistoriesForClusterBeforeDate(clusterId, policy.getToDateInMillis());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.orm.dao.AlertsDAO.LOG.error("Error while looking up cluster with name: {}", policy.getClusterName(), e);
            throw new java.lang.IllegalStateException(e);
        }
        return affectedRows;
    }

    private final class HistoryPredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.AlertHistoryEntity> {
        public HistoryPredicateVisitor() {
            super(m_entityManagerProvider.get(), org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
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

    private final class CurrentPredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.AlertCurrentEntity> {
        public CurrentPredicateVisitor() {
            super(m_entityManagerProvider.get(), org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        }

        @java.lang.Override
        public java.lang.Class<org.apache.ambari.server.orm.entities.AlertCurrentEntity> getEntityClass() {
            return org.apache.ambari.server.orm.entities.AlertCurrentEntity.class;
        }

        @java.lang.Override
        public java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId) {
            return org.apache.ambari.server.orm.entities.AlertCurrentEntity_.getPredicateMapping().get(propertyId);
        }
    }

    private static final class AlertCacheKey {
        private final long m_clusterId;

        private final java.lang.String m_hostName;

        private final java.lang.String m_alertDefinitionName;

        private AlertCacheKey(long clusterId, java.lang.String alertDefinitionName) {
            this(clusterId, alertDefinitionName, null);
        }

        private AlertCacheKey(long clusterId, java.lang.String alertDefinitionName, java.lang.String hostName) {
            m_clusterId = clusterId;
            m_alertDefinitionName = alertDefinitionName;
            m_hostName = hostName;
        }

        public static org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey build(org.apache.ambari.server.orm.entities.AlertCurrentEntity current) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = current.getAlertHistory();
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey key = new org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey(history.getClusterId(), history.getAlertDefinition().getDefinitionName(), history.getHostName());
            return key;
        }

        public long getClusterId() {
            return m_clusterId;
        }

        public java.lang.String getHostName() {
            return m_hostName;
        }

        public java.lang.String getAlertDefinitionName() {
            return m_alertDefinitionName;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + (m_alertDefinitionName == null ? 0 : m_alertDefinitionName.hashCode());
            result = (prime * result) + ((int) (m_clusterId ^ (m_clusterId >>> 32)));
            result = (prime * result) + (m_hostName == null ? 0 : m_hostName.hashCode());
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey other = ((org.apache.ambari.server.orm.dao.AlertsDAO.AlertCacheKey) (obj));
            if (m_clusterId != other.m_clusterId) {
                return false;
            }
            if (m_alertDefinitionName == null) {
                if (other.m_alertDefinitionName != null) {
                    return false;
                }
            } else if (!m_alertDefinitionName.equals(other.m_alertDefinitionName)) {
                return false;
            }
            if (m_hostName == null) {
                if (other.m_hostName != null) {
                    return false;
                }
            } else if (!m_hostName.equals(other.m_hostName)) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder buffer = new java.lang.StringBuilder("AlertCacheKey{");
            buffer.append("cluserId=").append(m_clusterId);
            buffer.append(", alertName=").append(m_alertDefinitionName);
            if (null != m_hostName) {
                buffer.append(", hostName=").append(m_hostName);
            }
            buffer.append("}");
            return buffer.toString();
        }
    }

    @java.lang.SuppressWarnings("serial")
    private static final class AlertNotYetCreatedException extends java.lang.Exception {}

    private java.util.List<java.lang.Integer> findAllAlertHistoryIdsBeforeDate(java.lang.Long clusterId, long beforeDateMillis) {
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        javax.persistence.TypedQuery<java.lang.Integer> alertHistoryQuery = entityManager.createNamedQuery("AlertHistoryEntity.findAllIdsInClusterBeforeDate", java.lang.Integer.class);
        alertHistoryQuery.setParameter("clusterId", clusterId);
        alertHistoryQuery.setParameter("beforeDate", beforeDateMillis);
        return m_daoUtils.selectList(alertHistoryQuery);
    }

    @com.google.inject.persist.Transactional
    int cleanAlertNoticesForClusterBeforeDate(java.lang.Long clusterId, long beforeDateMillis) {
        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Deleting AlertNotice entities before date " + new java.util.Date(beforeDateMillis));
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        java.util.List<java.lang.Integer> ids = findAllAlertHistoryIdsBeforeDate(clusterId, beforeDateMillis);
        int affectedRows = 0;
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> noticeQuery = entityManager.createNamedQuery("AlertNoticeEntity.removeByHistoryIds", org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        if ((ids != null) && (!ids.isEmpty())) {
            for (int i = 0; i < ids.size(); i += org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE) {
                int endIndex = ((i + org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE) > ids.size()) ? ids.size() : i + org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE;
                java.util.List<java.lang.Integer> idsSubList = ids.subList(i, endIndex);
                org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info((("Deleting AlertNotice entity batch with history ids: " + idsSubList.get(0)) + " - ") + idsSubList.get(idsSubList.size() - 1));
                noticeQuery.setParameter("historyIds", idsSubList);
                affectedRows += noticeQuery.executeUpdate();
            }
        }
        return affectedRows;
    }

    @com.google.inject.persist.Transactional
    int cleanAlertCurrentsForClusterBeforeDate(long clusterId, long beforeDateMillis) {
        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Deleting AlertCurrent entities before date " + new java.util.Date(beforeDateMillis));
        javax.persistence.EntityManager entityManager = m_entityManagerProvider.get();
        java.util.List<java.lang.Integer> ids = findAllAlertHistoryIdsBeforeDate(clusterId, beforeDateMillis);
        int affectedRows = 0;
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentQuery = entityManager.createNamedQuery("AlertCurrentEntity.removeByHistoryIds", org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        if ((ids != null) && (!ids.isEmpty())) {
            for (int i = 0; i < ids.size(); i += org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE) {
                int endIndex = ((i + org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE) > ids.size()) ? ids.size() : i + org.apache.ambari.server.orm.dao.AlertsDAO.BATCH_SIZE;
                java.util.List<java.lang.Integer> idsSubList = ids.subList(i, endIndex);
                org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info((("Deleting AlertCurrent entity batch with history ids: " + idsSubList.get(0)) + " - ") + idsSubList.get(idsSubList.size() - 1));
                currentQuery.setParameter("historyIds", ids.subList(i, endIndex));
                affectedRows += currentQuery.executeUpdate();
            }
        }
        return affectedRows;
    }

    @com.google.inject.persist.Transactional
    int cleanAlertHistoriesForClusterBeforeDate(java.lang.Long clusterId, long beforeDateMillis) {
        return executeQuery("AlertHistoryEntity.removeInClusterBeforeDate", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class, clusterId, beforeDateMillis);
    }

    private int executeQuery(java.lang.String namedQuery, java.lang.Class entityType, long clusterId, long timestamp) {
        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Starting: Delete/update entries older than [ {} ] for entity [{}]", timestamp, entityType);
        javax.persistence.TypedQuery query = m_entityManagerProvider.get().createNamedQuery(namedQuery, entityType);
        query.setParameter("clusterId", clusterId);
        query.setParameter("beforeDate", timestamp);
        int affectedRows = query.executeUpdate();
        m_entityManagerProvider.get().flush();
        m_entityManagerProvider.get().clear();
        org.apache.ambari.server.orm.dao.AlertsDAO.LOG.info("Completed: Delete/update entries older than [ {} ] for entity: [{}]. Number of entities deleted: [{}]", timestamp, entityType, affectedRows);
        return affectedRows;
    }

    public void saveEntities(java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toMerge, java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toCreateHistoryAndMerge) {
        if (m_configuration.isAlertCacheEnabled()) {
            synchronized(this) {
                saveEntitiesTransactional(toMerge, toCreateHistoryAndMerge);
            }
        } else {
            saveEntitiesTransactional(toMerge, toCreateHistoryAndMerge);
        }
    }

    @com.google.inject.persist.Transactional
    protected void saveEntitiesTransactional(java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toMerge, java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toCreateHistoryAndMerge) {
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity entity : toMerge) {
            merge(entity, m_configuration.isAlertCacheEnabled());
        }
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity entity : toCreateHistoryAndMerge) {
            create(entity.getAlertHistory());
            merge(entity);
            if (org.apache.ambari.server.orm.dao.AlertsDAO.LOG.isDebugEnabled()) {
                org.apache.ambari.server.orm.dao.AlertsDAO.LOG.debug("Alert State Merged: CurrentId {}, CurrentTimestamp {}, HistoryId {}, HistoryState {}", entity.getAlertId(), entity.getLatestTimestamp(), entity.getAlertHistory().getAlertId(), entity.getAlertHistory().getAlertState());
            }
        }
    }
}