package org.apache.ambari.server.orm;
import com.google.inject.persist.Transactional;
@com.google.inject.Singleton
public class AlertDaoHelper {
    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private static final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dispatchDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDAO;

    @com.google.inject.persist.Transactional
    public void populateData(org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = m_definitionDao.findAll();
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            m_definitionDao.remove(definition);
        }
        org.apache.ambari.server.orm.entities.AlertTargetEntity administrators = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        administrators.setDescription("The Administrators");
        administrators.setNotificationType("EMAIL");
        administrators.setTargetName("Administrators");
        m_dispatchDAO.create(administrators);
        org.apache.ambari.server.orm.entities.AlertTargetEntity operators = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        operators.setDescription("The Operators");
        operators.setNotificationType("EMAIL");
        operators.setTargetName("Operators");
        m_dispatchDAO.create(operators);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity namenode = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        namenode.setDefinitionName("NAMENODE");
        namenode.setServiceName("HDFS");
        namenode.setComponentName("NAMENODE");
        namenode.setClusterId(cluster.getClusterId());
        namenode.setHash(java.util.UUID.randomUUID().toString());
        namenode.setScheduleInterval(java.lang.Integer.valueOf(60));
        namenode.setScope(org.apache.ambari.server.state.alert.Scope.ANY);
        namenode.setSource("{\"type\" : \"SCRIPT\"}");
        namenode.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(namenode);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity datanode = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        datanode.setDefinitionName("DATANODE");
        datanode.setServiceName("HDFS");
        datanode.setComponentName("DATANODE");
        datanode.setClusterId(cluster.getClusterId());
        datanode.setHash(java.util.UUID.randomUUID().toString());
        datanode.setScheduleInterval(java.lang.Integer.valueOf(60));
        datanode.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        datanode.setSource("{\"type\" : \"SCRIPT\"}");
        datanode.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(datanode);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity aggregate = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        aggregate.setDefinitionName("YARN_AGGREGATE");
        aggregate.setServiceName("YARN");
        aggregate.setComponentName(null);
        aggregate.setClusterId(cluster.getClusterId());
        aggregate.setHash(java.util.UUID.randomUUID().toString());
        aggregate.setScheduleInterval(java.lang.Integer.valueOf(60));
        aggregate.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        aggregate.setSource("{\"type\" : \"SCRIPT\"}");
        aggregate.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(aggregate);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity nnHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        nnHistory.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        nnHistory.setServiceName(namenode.getServiceName());
        nnHistory.setComponentName(namenode.getComponentName());
        nnHistory.setClusterId(cluster.getClusterId());
        nnHistory.setAlertDefinition(namenode);
        nnHistory.setAlertLabel(namenode.getDefinitionName());
        nnHistory.setAlertText(namenode.getDefinitionName());
        nnHistory.setAlertTimestamp(org.apache.ambari.server.orm.AlertDaoHelper.calendar.getTimeInMillis());
        nnHistory.setHostName(org.apache.ambari.server.orm.AlertDaoHelper.HOSTNAME);
        m_alertsDAO.create(nnHistory);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity dnHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        dnHistory.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        dnHistory.setServiceName(datanode.getServiceName());
        dnHistory.setComponentName(datanode.getComponentName());
        dnHistory.setClusterId(cluster.getClusterId());
        dnHistory.setAlertDefinition(datanode);
        dnHistory.setAlertLabel(datanode.getDefinitionName());
        dnHistory.setAlertText(datanode.getDefinitionName());
        dnHistory.setAlertTimestamp(org.apache.ambari.server.orm.AlertDaoHelper.calendar.getTimeInMillis());
        dnHistory.setHostName(org.apache.ambari.server.orm.AlertDaoHelper.HOSTNAME);
        m_alertsDAO.create(dnHistory);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity aggregateHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        aggregateHistory.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        aggregateHistory.setServiceName(aggregate.getServiceName());
        aggregateHistory.setComponentName(aggregate.getComponentName());
        aggregateHistory.setClusterId(cluster.getClusterId());
        aggregateHistory.setAlertDefinition(aggregate);
        aggregateHistory.setAlertLabel(aggregate.getDefinitionName());
        aggregateHistory.setAlertText(aggregate.getDefinitionName());
        aggregateHistory.setAlertTimestamp(org.apache.ambari.server.orm.AlertDaoHelper.calendar.getTimeInMillis());
        m_alertsDAO.create(aggregateHistory);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity nnPendingNotice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        nnPendingNotice.setAlertHistory(nnHistory);
        nnPendingNotice.setAlertTarget(administrators);
        nnPendingNotice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        nnPendingNotice.setUuid(java.util.UUID.randomUUID().toString());
        m_dispatchDAO.create(nnPendingNotice);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity dnDeliveredNotice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        dnDeliveredNotice.setAlertHistory(dnHistory);
        dnDeliveredNotice.setAlertTarget(administrators);
        dnDeliveredNotice.setNotifyState(org.apache.ambari.server.state.NotificationState.FAILED);
        dnDeliveredNotice.setUuid(java.util.UUID.randomUUID().toString());
        m_dispatchDAO.create(dnDeliveredNotice);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity aggregateFailedNotice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        aggregateFailedNotice.setAlertHistory(aggregateHistory);
        aggregateFailedNotice.setAlertTarget(operators);
        aggregateFailedNotice.setNotifyState(org.apache.ambari.server.state.NotificationState.FAILED);
        aggregateFailedNotice.setUuid(java.util.UUID.randomUUID().toString());
        m_dispatchDAO.create(aggregateFailedNotice);
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = m_alertsDAO.findAll();
        org.junit.Assert.assertEquals(3, histories.size());
    }
}