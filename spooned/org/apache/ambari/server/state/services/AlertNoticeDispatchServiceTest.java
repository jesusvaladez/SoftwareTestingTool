package org.apache.ambari.server.state.services;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.class, java.lang.management.ManagementFactory.class })
public class AlertNoticeDispatchServiceTest extends org.apache.ambari.server.state.services.AlertNoticeDispatchService {
    static final java.lang.String ALERT_NOTICE_UUID_1 = java.util.UUID.randomUUID().toString();

    static final java.lang.String ALERT_NOTICE_UUID_2 = java.util.UUID.randomUUID().toString();

    static final java.lang.String ALERT_UNIQUE_TEXT = "0eeda438-2b13-4869-a416-137e35ff76e9";

    static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    static final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));

    static final long UPTIME_MILLIS = 8589934592L;

    static final long UPTIME_HUNDREDTHS_OF_SECOND = org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.UPTIME_MILLIS / 10;

    private org.apache.ambari.server.api.services.AmbariMetaInfo m_metaInfo = null;

    private org.apache.ambari.server.notifications.DispatchFactory m_dispatchFactory = null;

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao = null;

    private com.google.inject.Injector m_injector;

    private java.lang.management.RuntimeMXBean m_runtimeMXBean;

    java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> m_definitions = new java.util.ArrayList<>();

    java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> m_histories = new java.util.ArrayList<>();

    @org.junit.Before
    public void before() {
        m_dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_dispatchFactory = EasyMock.createStrictMock(org.apache.ambari.server.notifications.DispatchFactory.class);
        m_metaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockModule());
        org.junit.Assert.assertNotNull(m_injector);
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("Service " + i);
            definition.setComponentName(null);
            definition.setClusterId(1L);
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(java.lang.Integer.valueOf(60));
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            m_definitions.add(definition);
        }
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.calendar.clear();
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.calendar.set(2014, java.util.Calendar.JANUARY, 1);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : m_definitions) {
            for (int i = 0; i < 10; i++) {
                org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
                history.setServiceName(definition.getServiceName());
                history.setClusterId(1L);
                history.setAlertDefinition(definition);
                history.setAlertLabel((definition.getDefinitionName() + " ") + i);
                history.setAlertText((definition.getDefinitionName() + " ") + i);
                history.setAlertTimestamp(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.calendar.getTimeInMillis());
                history.setHostName(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.HOSTNAME);
                history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
                if ((i == 0) || (i == 5)) {
                    history.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
                }
                org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.calendar.add(java.util.Calendar.DATE, 1);
                m_histories.add(history);
            }
        }
        m_runtimeMXBean = org.easymock.EasyMock.createNiceMock(java.lang.management.RuntimeMXBean.class);
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.management.ManagementFactory.class);
        EasyMock.expect(java.lang.management.ManagementFactory.getRuntimeMXBean()).andReturn(m_runtimeMXBean).atLeastOnce();
        org.powermock.api.easymock.PowerMock.replay(java.lang.management.ManagementFactory.class);
        EasyMock.expect(m_runtimeMXBean.getUptime()).andReturn(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.UPTIME_MILLIS).atLeastOnce();
        EasyMock.replay(m_runtimeMXBean);
    }

    @org.junit.Test
    public void testAlertInfo() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = m_histories.get(0);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(history);
        org.junit.Assert.assertEquals(history.getAlertDefinition().getLabel(), alertInfo.getAlertName());
        org.junit.Assert.assertEquals(history.getAlertState(), alertInfo.getAlertState());
        org.junit.Assert.assertEquals(history.getAlertText(), alertInfo.getAlertText());
        org.junit.Assert.assertEquals(history.getComponentName(), alertInfo.getComponentName());
        org.junit.Assert.assertEquals(history.getHostName(), alertInfo.getHostName());
        org.junit.Assert.assertEquals(history.getServiceName(), alertInfo.getServiceName());
        org.junit.Assert.assertEquals(false, alertInfo.hasComponentName());
        org.junit.Assert.assertEquals(true, alertInfo.hasHostName());
    }

    @org.junit.Test
    public void testAlertSummaryInfo() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertSummaryInfo alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertSummaryInfo(m_histories);
        org.junit.Assert.assertEquals(50, alertInfo.getAlerts().size());
        org.junit.Assert.assertEquals(10, alertInfo.getAlerts("Service 1").size());
        org.junit.Assert.assertEquals(10, alertInfo.getAlerts("Service 2").size());
        org.junit.Assert.assertEquals(8, alertInfo.getAlerts("Service 1", "OK").size());
        org.junit.Assert.assertEquals(2, alertInfo.getAlerts("Service 1", "CRITICAL").size());
        org.junit.Assert.assertNull(alertInfo.getAlerts("Service 1", "WARNING"));
        org.junit.Assert.assertNull(alertInfo.getAlerts("Service 1", "UNKNOWN"));
        org.junit.Assert.assertEquals(5, alertInfo.getServices().size());
    }

    @org.junit.Test
    public void testNoDispatch() throws java.lang.Exception {
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(new java.util.ArrayList<>()).once();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
    }

    @org.junit.Test
    public void testDigestDispatch() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockEmailDispatcher dispatcher = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockEmailDispatcher();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSingleMockNotice(dispatcher.getType());
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = notices.get(0);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher("EMAIL")).andReturn(dispatcher).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice)).andReturn(notice).atLeastOnce();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        org.apache.ambari.server.notifications.Notification notification = dispatcher.getNotification();
        org.junit.Assert.assertNotNull(notification);
        org.junit.Assert.assertTrue(notification.Subject.contains("OK[1]"));
        org.junit.Assert.assertTrue(notification.Subject.contains("Critical[0]"));
        org.junit.Assert.assertTrue(notification.Body.contains(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT));
    }

    @org.junit.Test
    public void testExceptionHandling() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSingleMockNotice("EMAIL");
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = notices.get(0);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher("EMAIL")).andReturn(null).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice)).andReturn(notice).atLeastOnce();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
    }

    @org.junit.Test
    public void testSingleSnmpDispatch() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockSnmpDispatcher dispatcher = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockSnmpDispatcher();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSnmpMockNotices("SNMP");
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice1 = notices.get(0);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice2 = notices.get(1);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice1)).andReturn(notice1).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice2)).andReturn(notice2).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher("SNMP")).andReturn(dispatcher).atLeastOnce();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        java.util.List<org.apache.ambari.server.notifications.Notification> notifications = dispatcher.getNotifications();
        org.junit.Assert.assertEquals(2, notifications.size());
    }

    @org.junit.Test
    public void testAmbariSnmpSingleDispatch() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockAmbariSnmpDispatcher dispatcher = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockAmbariSnmpDispatcher();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSnmpMockNotices("AMBARI_SNMP");
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice1 = notices.get(0);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice2 = notices.get(1);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice1)).andReturn(notice1).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice2)).andReturn(notice2).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher("AMBARI_SNMP")).andReturn(dispatcher).atLeastOnce();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        java.util.List<org.apache.ambari.server.notifications.Notification> notifications = dispatcher.getNotifications();
        org.junit.Assert.assertEquals(2, notifications.size());
    }

    @org.junit.Test
    public void testAmbariSnmpRealDispatch() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(8081);
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSnmpMockNotices("AMBARI_SNMP");
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice1 = notices.get(0);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice2 = notices.get(1);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice1)).andReturn(notice1).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice2)).andReturn(notice2).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher("AMBARI_SNMP")).andReturn(dispatcher).once();
        org.easymock.EasyMock.expect(m_dao.findNoticeByUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_1)).andReturn(notice1).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice1)).andReturn(notice1).once();
        org.easymock.EasyMock.expect(m_dao.findNoticeByUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_2)).andReturn(notice2).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice2)).andReturn(notice2).once();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.SnmpReceiver snmpReceiver = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.SnmpReceiver();
        service.runOneIteration();
        java.lang.Thread.sleep(1000);
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        java.util.List<java.util.Vector> expectedTrapVectors = new java.util.LinkedList<>();
        java.util.Vector firstVector = new java.util.Vector();
        firstVector.add(new org.snmp4j.smi.VariableBinding(org.snmp4j.mp.SnmpConstants.sysUpTime, new org.snmp4j.smi.TimeTicks(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.UPTIME_HUNDREDTHS_OF_SECOND)));
        firstVector.add(new org.snmp4j.smi.VariableBinding(org.snmp4j.mp.SnmpConstants.snmpTrapOID, new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TRAP_OID)));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_ID_OID), new org.snmp4j.smi.Integer32(new java.math.BigDecimal(1L).intValueExact())));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_NAME_OID), new org.snmp4j.smi.OctetString("alert-definition-1")));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_HASH_OID), new org.snmp4j.smi.OctetString("1")));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_NAME_OID), new org.snmp4j.smi.OctetString("Alert Definition 1")));
        java.util.Vector secondVector = new java.util.Vector(firstVector);
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID), new org.snmp4j.smi.OctetString(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT)));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID), new org.snmp4j.smi.Integer32(0)));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID), new org.snmp4j.smi.OctetString("null")));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID), new org.snmp4j.smi.OctetString("HDFS")));
        firstVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID), new org.snmp4j.smi.OctetString("null")));
        secondVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID), new org.snmp4j.smi.OctetString(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT + " CRITICAL")));
        secondVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID), new org.snmp4j.smi.Integer32(3)));
        secondVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID), new org.snmp4j.smi.OctetString("null")));
        secondVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID), new org.snmp4j.smi.OctetString("HDFS")));
        secondVector.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID), new org.snmp4j.smi.OctetString("null")));
        expectedTrapVectors.add(firstVector);
        expectedTrapVectors.add(secondVector);
        org.junit.Assert.assertNotNull(snmpReceiver.receivedTrapsVectors);
        org.junit.Assert.assertTrue(snmpReceiver.receivedTrapsVectors.size() == 2);
        org.junit.Assert.assertEquals(expectedTrapVectors, snmpReceiver.receivedTrapsVectors);
    }

    @org.junit.Test
    public void testFailedDispatch() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockEmailDispatcher dispatcher = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockEmailDispatcher();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSingleMockNotice(dispatcher.getType());
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = notices.get(0);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice)).andReturn(notice).once();
        org.easymock.EasyMock.expect(m_dao.findNoticeByUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_1)).andReturn(notice).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice)).andReturn(notice).once();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher(dispatcher.getType())).andReturn(dispatcher).once();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        org.apache.ambari.server.notifications.Notification notification = dispatcher.getNotification();
        org.junit.Assert.assertNull(notification);
    }

    @org.junit.Test
    public void testDispatcherWithoutCallbacks() throws java.lang.Exception {
        org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockNoCallbackDispatcher dispatcher = new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockNoCallbackDispatcher();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = getSingleMockNotice(dispatcher.getType());
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = notices.get(0);
        org.easymock.EasyMock.expect(m_dao.findPendingNotices()).andReturn(notices).once();
        org.easymock.EasyMock.expect(m_dao.merge(notice)).andReturn(notice).atLeastOnce();
        org.easymock.EasyMock.expect(m_dispatchFactory.getDispatcher(dispatcher.getType())).andReturn(dispatcher).once();
        org.easymock.EasyMock.replay(m_dao, m_dispatchFactory);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService service = m_injector.getInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);
        service.startUp();
        service.setExecutor(new org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockExecutor());
        service.runOneIteration();
        org.easymock.EasyMock.verify(m_dao, m_dispatchFactory);
        org.apache.ambari.server.notifications.Notification notification = dispatcher.getNotification();
        org.junit.Assert.assertNotNull(notification);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.NotificationState.DISPATCHED, notice.getNotifyState());
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> getSingleMockNotice(java.lang.String notificationType) {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionId(1L);
        definition.setDefinitionName("alert-definition-1");
        definition.setLabel("Alert Definition 1");
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setServiceName("HDFS");
        history.setClusterId(1L);
        history.setAlertLabel("Label");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT);
        history.setAlertTimestamp(java.lang.System.currentTimeMillis());
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        target.setTargetId(1L);
        target.setAlertStates(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class));
        target.setTargetName("Alert Target");
        target.setDescription("Mock Target");
        target.setNotificationType(notificationType);
        java.lang.String properties = "{ \"foo\" : \"bar\" }";
        target.setProperties(properties);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice.setUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_1);
        notice.setAlertTarget(target);
        notice.setAlertHistory(history);
        notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        java.util.ArrayList<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = new java.util.ArrayList<>();
        notices.add(notice);
        return notices;
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> getSnmpMockNotices(java.lang.String notificationType) {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionId(1L);
        definition.setDefinitionName("alert-definition-1");
        definition.setLabel("Alert Definition 1");
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history1.setAlertDefinition(definition);
        history1.setServiceName("HDFS");
        history1.setClusterId(1L);
        history1.setAlertLabel("Label");
        history1.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history1.setAlertText(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT);
        history1.setAlertTimestamp(java.lang.System.currentTimeMillis());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history2.setAlertDefinition(definition);
        history2.setServiceName("HDFS");
        history2.setClusterId(1L);
        history2.setAlertLabel("Label");
        history2.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        history2.setAlertText(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_UNIQUE_TEXT + " CRITICAL");
        history2.setAlertTimestamp(java.lang.System.currentTimeMillis());
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
        target.setTargetId(1L);
        target.setAlertStates(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class));
        target.setTargetName("Alert Target");
        target.setDescription("Mock Target");
        target.setNotificationType(notificationType);
        java.lang.String properties = "{ \"ambari.dispatch.snmp.version\": \"SNMPv1\", \"ambari.dispatch.snmp.port\": \"8000\"," + " \"ambari.dispatch.recipients\": [\"127.0.0.1\"],\"ambari.dispatch.snmp.community\":\"\" }";
        target.setProperties(properties);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice1 = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice1.setUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_1);
        notice1.setAlertTarget(target);
        notice1.setAlertHistory(history1);
        notice1.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice2 = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice2.setUuid(org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.ALERT_NOTICE_UUID_2);
        notice2.setAlertTarget(target);
        notice2.setAlertHistory(history2);
        notice2.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        java.util.ArrayList<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = new java.util.ArrayList<>();
        notices.add(notice1);
        notices.add(notice2);
        return notices;
    }

    private static final class MockEmailDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
        private org.apache.ambari.server.notifications.Notification m_notificaiton;

        @java.lang.Override
        public java.lang.String getType() {
            return "EMAIL";
        }

        @java.lang.Override
        public boolean isDigestSupported() {
            return true;
        }

        @java.lang.Override
        public boolean isNotificationContentGenerationRequired() {
            return true;
        }

        @java.lang.Override
        public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
            m_notificaiton = notification;
        }

        @java.lang.Override
        public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
            return null;
        }

        public org.apache.ambari.server.notifications.Notification getNotification() {
            return m_notificaiton;
        }
    }

    private static class MockSnmpDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
        private java.util.List<org.apache.ambari.server.notifications.Notification> m_notifications = new java.util.ArrayList<>();

        @java.lang.Override
        public java.lang.String getType() {
            return "SNMP";
        }

        @java.lang.Override
        public boolean isNotificationContentGenerationRequired() {
            return true;
        }

        @java.lang.Override
        public boolean isDigestSupported() {
            return false;
        }

        @java.lang.Override
        public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
            m_notifications.add(notification);
        }

        public java.util.List<org.apache.ambari.server.notifications.Notification> getNotifications() {
            return m_notifications;
        }

        @java.lang.Override
        public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
            return null;
        }
    }

    private static final class MockAmbariSnmpDispatcher extends org.apache.ambari.server.state.services.AlertNoticeDispatchServiceTest.MockSnmpDispatcher {
        @java.lang.Override
        public java.lang.String getType() {
            return org.apache.ambari.server.state.alert.TargetType.AMBARI_SNMP.name();
        }
    }

    private static final class MockNoCallbackDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
        private org.apache.ambari.server.notifications.Notification m_notificaiton;

        @java.lang.Override
        public java.lang.String getType() {
            return "NO_CALLBACK";
        }

        @java.lang.Override
        public boolean isNotificationContentGenerationRequired() {
            return true;
        }

        @java.lang.Override
        public boolean isDigestSupported() {
            return false;
        }

        @java.lang.Override
        public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
            m_notificaiton = notification;
        }

        @java.lang.Override
        public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
            return null;
        }

        public org.apache.ambari.server.notifications.Notification getNotification() {
            return m_notificaiton;
        }
    }

    private static final class MockExecutor implements java.util.concurrent.Executor {
        @java.lang.Override
        public void execute(java.lang.Runnable runnable) {
            runnable.run();
        }
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addDBAccessorBinding().addAmbariMetaInfoBinding().addLdapBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.notifications.DispatchFactory.class).toInstance(m_dispatchFactory);
            binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(m_metaInfo);
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
            binder.bind(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class).toInstance(new org.apache.ambari.server.state.services.AlertNoticeDispatchService());
            org.easymock.EasyMock.expect(m_metaInfo.getServerVersion()).andReturn("2.0.0").anyTimes();
            org.easymock.EasyMock.replay(m_metaInfo);
        }
    }

    private class SnmpReceiver {
        private org.snmp4j.Snmp snmp = null;

        private org.snmp4j.smi.Address targetAddress = org.snmp4j.smi.GenericAddress.parse("udp:127.0.0.1/8000");

        private org.snmp4j.TransportMapping transport = null;

        public java.util.List<java.util.Vector> receivedTrapsVectors = null;

        public SnmpReceiver() throws java.lang.Exception {
            transport = new org.snmp4j.transport.DefaultUdpTransportMapping();
            snmp = new org.snmp4j.Snmp(transport);
            receivedTrapsVectors = new java.util.LinkedList<>();
            org.snmp4j.CommandResponder trapPrinter = new org.snmp4j.CommandResponder() {
                @java.lang.Override
                public synchronized void processPdu(org.snmp4j.CommandResponderEvent e) {
                    org.snmp4j.PDU command = e.getPDU();
                    if (command != null) {
                        receivedTrapsVectors.add(command.getVariableBindings());
                    }
                }
            };
            snmp.addNotificationListener(targetAddress, trapPrinter);
        }
    }
}