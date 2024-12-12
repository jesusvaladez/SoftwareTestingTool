package org.apache.ambari.server.state.services;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
@org.apache.ambari.server.AmbariService
public class AlertNoticeDispatchService extends com.google.common.util.concurrent.AbstractScheduledService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.services.AlertNoticeDispatchService.class);

    private static final java.lang.String VELOCITY_LOG_TAG = "ambari-alerts";

    private static final java.lang.String AMBARI_ALERT_TEMPLATES = "alert-templates.xml";

    public static final java.lang.String AMBARI_DISPATCH_CREDENTIAL_USERNAME = "ambari.dispatch.credential.username";

    public static final java.lang.String AMBARI_DISPATCH_CREDENTIAL_PASSWORD = "ambari.dispatch.credential.password";

    public static final java.lang.String AMBARI_DISPATCH_RECIPIENTS = "ambari.dispatch.recipients";

    private static final java.lang.String VELOCITY_AMBARI_KEY = "ambari";

    private static final java.lang.String VELOCITY_SUMMARY_KEY = "summary";

    private static final java.lang.String VELOCITY_ALERT_KEY = "alert";

    private static final java.lang.String VELOCITY_DISPATCH_KEY = "dispatch";

    private final com.google.gson.Gson m_gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao;

    @com.google.inject.Inject
    private org.apache.ambari.server.notifications.DispatchFactory m_dispatchFactory;

    private org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplates m_alertTemplates;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_metaInfo;

    private java.util.concurrent.Executor m_executor;

    public AlertNoticeDispatchService() {
        m_executor = new java.util.concurrent.ThreadPoolExecutor(0, 2, 5L, java.util.concurrent.TimeUnit.MINUTES, new java.util.concurrent.LinkedBlockingQueue<>(), new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertDispatchThreadFactory(), new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        gsonBuilder.registerTypeAdapter(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties.class, new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetPropertyDeserializer());
        m_gson = gsonBuilder.create();
    }

    @java.lang.Override
    protected void startUp() throws java.lang.Exception {
        super.startUp();
        java.io.InputStream inputStream = null;
        java.lang.String alertTemplatesFile = null;
        try {
            alertTemplatesFile = m_configuration.getAlertTemplateFile();
            if (null != alertTemplatesFile) {
                java.io.File file = new java.io.File(alertTemplatesFile);
                inputStream = new java.io.FileInputStream(file);
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.warn("Unable to load alert template file {}", alertTemplatesFile, exception);
        }
        try {
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplates.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            if (null == inputStream) {
                inputStream = java.lang.ClassLoader.getSystemResourceAsStream(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_ALERT_TEMPLATES);
            }
            m_alertTemplates = ((org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplates) (unmarshaller.unmarshal(inputStream)));
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Unable to load alert template file {}, outbound notifications will not be formatted", org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_ALERT_TEMPLATES, exception);
        } finally {
            if (null != inputStream) {
                org.apache.commons.io.IOUtils.closeQuietly(inputStream);
            }
        }
    }

    protected void setExecutor(java.util.concurrent.Executor executor) {
        m_executor = executor;
    }

    @java.lang.Override
    protected void runOneIteration() throws java.lang.Exception {
        java.util.Map<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity>> aggregateMap;
        try {
            aggregateMap = getGroupedNotices();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Caught exception during alert notices preparing.", e);
            return;
        }
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = aggregateMap.keySet();
        for (org.apache.ambari.server.orm.entities.AlertTargetEntity target : targets) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = aggregateMap.get(target);
            if ((null == notices) || (notices.size() == 0)) {
                continue;
            }
            try {
                java.lang.String targetType = target.getNotificationType();
                org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = m_dispatchFactory.getDispatcher(targetType);
                if (dispatcher.isDigestSupported()) {
                    createSingleNotice(dispatcher, target, notices);
                } else {
                    createSeparateNotices(dispatcher, target, notices);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Caught exception during Alert Notice dispatching.", e);
            }
        }
    }

    private java.util.Map<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity>> getGroupedNotices() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> pending = m_dao.findPendingNotices();
        if (pending.size() == 0) {
            return java.util.Collections.EMPTY_MAP;
        }
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.info("There are {} pending alert notices about to be dispatched...", pending.size());
        java.util.Map<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity>> aggregateMap = new java.util.HashMap<>(pending.size());
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity notice : pending) {
            org.apache.ambari.server.orm.entities.AlertTargetEntity target = notice.getAlertTarget();
            java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = aggregateMap.get(target);
            if (null == notices) {
                notices = new java.util.ArrayList<>();
                aggregateMap.put(target, notices);
            }
            notice.setNotifyState(org.apache.ambari.server.state.NotificationState.DISPATCHED);
            notice = m_dao.merge(notice);
            notices.add(notice);
        }
        return aggregateMap;
    }

    private void createSingleNotice(org.apache.ambari.server.notifications.NotificationDispatcher dispatcher, org.apache.ambari.server.orm.entities.AlertTargetEntity target, java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices) {
        org.apache.ambari.server.state.alert.AlertNotification notification = buildNotificationFromTarget(target);
        notification.CallbackIds = new java.util.ArrayList<>(notices.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = new java.util.ArrayList<>(notices.size());
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity notice : notices) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = notice.getAlertHistory();
            histories.add(history);
            notification.CallbackIds.add(notice.getUuid());
        }
        try {
            renderDigestNotificationContent(dispatcher, notification, histories, target);
            org.apache.ambari.server.notifications.DispatchRunnable runnable = new org.apache.ambari.server.notifications.DispatchRunnable(dispatcher, notification);
            m_executor.execute(runnable);
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Unable to create notification for alerts", exception);
            notification.Callback.onFailure(notification.CallbackIds);
        }
    }

    private void createSeparateNotices(org.apache.ambari.server.notifications.NotificationDispatcher dispatcher, org.apache.ambari.server.orm.entities.AlertTargetEntity target, java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices) {
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity notice : notices) {
            org.apache.ambari.server.state.alert.AlertNotification notification = buildNotificationFromTarget(target);
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = notice.getAlertHistory();
            notification.CallbackIds = java.util.Collections.singletonList(notice.getUuid());
            try {
                renderNotificationContent(dispatcher, notification, history, target);
                org.apache.ambari.server.notifications.DispatchRunnable runnable = new org.apache.ambari.server.notifications.DispatchRunnable(dispatcher, notification);
                m_executor.execute(runnable);
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Unable to create notification for alert", exception);
                notification.Callback.onFailure(notification.CallbackIds);
            }
        }
    }

    @java.lang.Override
    protected com.google.common.util.concurrent.AbstractScheduledService.Scheduler scheduler() {
        return com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule(2, 2, java.util.concurrent.TimeUnit.MINUTES);
    }

    private org.apache.ambari.server.state.alert.AlertNotification buildNotificationFromTarget(org.apache.ambari.server.orm.entities.AlertTargetEntity target) {
        java.lang.String propertiesJson = target.getProperties();
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties targetProperties = m_gson.fromJson(propertiesJson, org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties.class);
        java.util.Map<java.lang.String, java.lang.String> properties = targetProperties.Properties;
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        notification.Callback = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertNoticeDispatchCallback();
        notification.DispatchProperties = properties;
        if (properties.containsKey(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_USERNAME) && properties.containsKey(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_PASSWORD)) {
            org.apache.ambari.server.notifications.DispatchCredentials credentials = new org.apache.ambari.server.notifications.DispatchCredentials();
            credentials.UserName = properties.get(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_USERNAME);
            credentials.Password = properties.get(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_PASSWORD);
            notification.Credentials = credentials;
        }
        if (null != targetProperties.Recipients) {
            java.util.List<org.apache.ambari.server.notifications.Recipient> recipients = new java.util.ArrayList<>(targetProperties.Recipients.size());
            for (java.lang.String stringRecipient : targetProperties.Recipients) {
                org.apache.ambari.server.notifications.Recipient recipient = new org.apache.ambari.server.notifications.Recipient();
                recipient.Identifier = stringRecipient;
                recipients.add(recipient);
            }
            notification.Recipients = recipients;
        }
        return notification;
    }

    private void renderDigestNotificationContent(org.apache.ambari.server.notifications.NotificationDispatcher dispatcher, org.apache.ambari.server.state.alert.AlertNotification notification, java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories, org.apache.ambari.server.orm.entities.AlertTargetEntity target) throws java.io.IOException {
        java.lang.String targetType = target.getNotificationType();
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AmbariInfo ambari = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AmbariInfo(m_metaInfo.get(), m_configuration);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertSummaryInfo summary = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertSummaryInfo(histories);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.DispatchInfo dispatch = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.DispatchInfo(target);
        final java.io.Writer subjectWriter = new java.io.StringWriter();
        final java.io.Writer bodyWriter = new java.io.StringWriter();
        final org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplate template = m_alertTemplates.getTemplate(targetType);
        if (dispatcher.isNotificationContentGenerationRequired()) {
            if (null != template) {
                org.apache.velocity.VelocityContext velocityContext = new org.apache.velocity.VelocityContext();
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_AMBARI_KEY, ambari);
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_SUMMARY_KEY, summary);
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_DISPATCH_KEY, dispatch);
                java.lang.String subjectTemplate = template.getSubject();
                java.lang.String bodyTemplate = template.getBody();
                org.apache.velocity.app.Velocity.evaluate(velocityContext, subjectWriter, org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_LOG_TAG, subjectTemplate);
                org.apache.velocity.app.Velocity.evaluate(velocityContext, bodyWriter, org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_LOG_TAG, bodyTemplate);
            } else {
                for (org.apache.ambari.server.orm.entities.AlertHistoryEntity alert : histories) {
                    subjectWriter.write("Apache Ambari Alert Summary");
                    bodyWriter.write(alert.getAlertState().name());
                    bodyWriter.write(" ");
                    bodyWriter.write(alert.getAlertDefinition().getLabel());
                    bodyWriter.write(" ");
                    bodyWriter.write(alert.getAlertText());
                    bodyWriter.write("\n");
                }
            }
        }
        notification.Subject = subjectWriter.toString();
        notification.Body = bodyWriter.toString();
    }

    private void renderNotificationContent(org.apache.ambari.server.notifications.NotificationDispatcher dispatcher, org.apache.ambari.server.state.alert.AlertNotification notification, org.apache.ambari.server.orm.entities.AlertHistoryEntity history, org.apache.ambari.server.orm.entities.AlertTargetEntity target) throws java.io.IOException {
        java.lang.String targetType = target.getNotificationType();
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AmbariInfo ambari = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AmbariInfo(m_metaInfo.get(), m_configuration);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alert = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(history);
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.DispatchInfo dispatch = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.DispatchInfo(target);
        notification.setAlertInfo(alert);
        final java.io.Writer subjectWriter = new java.io.StringWriter();
        final java.io.Writer bodyWriter = new java.io.StringWriter();
        final org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplate template = m_alertTemplates.getTemplate(targetType);
        if (dispatcher.isNotificationContentGenerationRequired()) {
            if (null != template) {
                org.apache.velocity.VelocityContext velocityContext = new org.apache.velocity.VelocityContext();
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_AMBARI_KEY, ambari);
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_ALERT_KEY, alert);
                velocityContext.put(org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_DISPATCH_KEY, dispatch);
                java.lang.String subjectTemplate = template.getSubject();
                java.lang.String bodyTemplate = template.getBody();
                org.apache.velocity.app.Velocity.evaluate(velocityContext, subjectWriter, org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_LOG_TAG, subjectTemplate);
                org.apache.velocity.app.Velocity.evaluate(velocityContext, bodyWriter, org.apache.ambari.server.state.services.AlertNoticeDispatchService.VELOCITY_LOG_TAG, bodyTemplate);
            } else {
                subjectWriter.write(alert.getAlertState().name());
                subjectWriter.write(" ");
                subjectWriter.write(alert.getAlertName());
                bodyWriter.write(alert.getAlertState().name());
                bodyWriter.write(" ");
                bodyWriter.write(alert.getAlertName());
                bodyWriter.write(" ");
                bodyWriter.write(alert.getAlertText());
                if (alert.hasHostName()) {
                    bodyWriter.write(" ");
                    bodyWriter.append(alert.getHostName());
                }
                bodyWriter.write("\n");
            }
        }
        notification.Subject = subjectWriter.toString();
        notification.Body = bodyWriter.toString();
    }

    private static final class AlertTargetProperties {
        public java.util.Map<java.lang.String, java.lang.String> Properties;

        public java.util.List<java.lang.String> Recipients;
    }

    private static final class AlertTargetPropertyDeserializer implements com.google.gson.JsonDeserializer<org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties> {
        @java.lang.Override
        public org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties properties = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTargetProperties();
            properties.Properties = new java.util.HashMap<>();
            final com.google.gson.JsonObject jsonObject = json.getAsJsonObject();
            java.util.Set<java.util.Map.Entry<java.lang.String, com.google.gson.JsonElement>> entrySet = jsonObject.entrySet();
            for (java.util.Map.Entry<java.lang.String, com.google.gson.JsonElement> entry : entrySet) {
                java.lang.String entryKey = entry.getKey();
                com.google.gson.JsonElement entryValue = entry.getValue();
                if (entryKey.equals(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_RECIPIENTS)) {
                    java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.List<java.lang.String>>() {}.getType();
                    com.google.gson.JsonArray jsonArray = entryValue.getAsJsonArray();
                    properties.Recipients = context.deserialize(jsonArray, listType);
                } else {
                    properties.Properties.put(entryKey, entryValue.getAsString());
                }
            }
            return properties;
        }
    }

    private static final class AlertDispatchThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final java.util.concurrent.atomic.AtomicInteger s_threadIdPool = new java.util.concurrent.atomic.AtomicInteger(1);

        @java.lang.Override
        public java.lang.Thread newThread(java.lang.Runnable r) {
            java.lang.Thread thread = new java.lang.Thread(r, "alert-dispatch-" + org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertDispatchThreadFactory.s_threadIdPool.getAndIncrement());
            thread.setDaemon(false);
            thread.setPriority(java.lang.Thread.NORM_PRIORITY - 1);
            return thread;
        }
    }

    private final class AlertNoticeDispatchCallback implements org.apache.ambari.server.notifications.DispatchCallback {
        @java.lang.Override
        public void onSuccess(java.util.List<java.lang.String> callbackIds) {
            for (java.lang.String callbackId : callbackIds) {
                updateAlertNotice(callbackId, org.apache.ambari.server.state.NotificationState.DELIVERED);
            }
        }

        @java.lang.Override
        public void onFailure(java.util.List<java.lang.String> callbackIds) {
            for (java.lang.String callbackId : callbackIds) {
                updateAlertNotice(callbackId, org.apache.ambari.server.state.NotificationState.FAILED);
            }
        }

        private void updateAlertNotice(java.lang.String uuid, org.apache.ambari.server.state.NotificationState state) {
            try {
                org.apache.ambari.server.orm.entities.AlertNoticeEntity entity = m_dao.findNoticeByUuid(uuid);
                if (null == entity) {
                    org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.warn("Unable to find an alert notice with UUID {}", uuid);
                    return;
                }
                entity.setNotifyState(state);
                m_dao.merge(entity);
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.state.services.AlertNoticeDispatchService.LOG.error("Unable to update the alert notice with UUID {} to {}, notifications will continue to be sent", uuid, state, exception);
            }
        }
    }

    public static final class AlertInfo {
        private final org.apache.ambari.server.orm.entities.AlertHistoryEntity m_history;

        public AlertInfo(org.apache.ambari.server.orm.entities.AlertHistoryEntity history) {
            m_history = history;
        }

        public java.lang.String getHostName() {
            return m_history.getHostName();
        }

        public boolean hasHostName() {
            return m_history.getHostName() != null;
        }

        public java.lang.String getServiceName() {
            return m_history.getServiceName();
        }

        public java.lang.String getComponentName() {
            return m_history.getComponentName();
        }

        public boolean hasComponentName() {
            return m_history.getComponentName() != null;
        }

        public long getAlertTimestamp() {
            return m_history.getAlertTimestamp();
        }

        public org.apache.ambari.server.state.AlertState getAlertState() {
            return m_history.getAlertState();
        }

        public java.lang.Long getAlertDefinitionId() {
            return m_history.getAlertDefinitionId();
        }

        public int getAlertDefinitionHash() {
            return m_history.getAlertDefinitionHash();
        }

        public java.lang.String getAlertName() {
            return m_history.getAlertDefinition().getLabel();
        }

        public org.apache.ambari.server.orm.entities.AlertDefinitionEntity getAlertDefinition() {
            return m_history.getAlertDefinition();
        }

        public java.lang.String getAlertText() {
            return m_history.getAlertText();
        }
    }

    public static final class AlertSummaryInfo {
        private int m_okCount = 0;

        private int m_warningCount = 0;

        private int m_criticalCount = 0;

        private int m_unknownCount = 0;

        private final java.util.Set<java.lang.String> m_hosts = new java.util.HashSet<>();

        private final java.util.Set<java.lang.String> m_services = new java.util.HashSet<>();

        private final java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> m_alerts;

        private final java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.AlertState, java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity>>> m_alertsByServiceAndState = new java.util.HashMap<>();

        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> m_servicesByState = new java.util.HashMap<>();

        private final java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity>> m_alertsByService = new java.util.HashMap<>();

        protected AlertSummaryInfo(java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories) {
            m_alerts = histories;
            for (org.apache.ambari.server.orm.entities.AlertHistoryEntity history : m_alerts) {
                org.apache.ambari.server.state.AlertState alertState = history.getAlertState();
                java.lang.String serviceName = history.getServiceName();
                java.lang.String hostName = history.getHostName();
                if (null != hostName) {
                    m_hosts.add(hostName);
                }
                if (null != serviceName) {
                    m_services.add(serviceName);
                }
                java.util.Map<org.apache.ambari.server.state.AlertState, java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity>> service = m_alertsByServiceAndState.get(serviceName);
                if (null == service) {
                    service = new java.util.HashMap<>();
                    m_alertsByServiceAndState.put(serviceName, service);
                }
                java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> alertList = service.get(alertState);
                if (null == alertList) {
                    alertList = new java.util.ArrayList<>();
                    service.put(alertState, alertList);
                }
                alertList.add(history);
                java.util.Set<java.lang.String> services = m_servicesByState.get(alertState.name());
                if (null == services) {
                    services = new java.util.HashSet<>();
                    m_servicesByState.put(alertState.name(), services);
                }
                services.add(serviceName);
                java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> alertsByService = m_alertsByService.get(serviceName);
                if (null == alertsByService) {
                    alertsByService = new java.util.ArrayList<>();
                    m_alertsByService.put(serviceName, alertsByService);
                }
                alertsByService.add(history);
                switch (alertState) {
                    case CRITICAL :
                        m_criticalCount++;
                        break;
                    case OK :
                        m_okCount++;
                        break;
                    case UNKNOWN :
                        m_unknownCount++;
                        break;
                    case WARNING :
                        m_warningCount++;
                        break;
                    default :
                        m_unknownCount++;
                        break;
                }
            }
        }

        public int getOkCount() {
            return m_okCount;
        }

        public int getWarningCount() {
            return m_warningCount;
        }

        public int getCriticalCount() {
            return m_criticalCount;
        }

        public int getUnknownCount() {
            return m_unknownCount;
        }

        public int getTotalCount() {
            return ((m_okCount + m_warningCount) + m_criticalCount) + m_unknownCount;
        }

        public java.util.Set<java.lang.String> getServices() {
            return m_services;
        }

        public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> getAlerts() {
            return m_alerts;
        }

        public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> getAlerts(java.lang.String serviceName) {
            return m_alertsByService.get(serviceName);
        }

        public java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> getAlerts(java.lang.String serviceName, java.lang.String alertState) {
            java.util.Map<org.apache.ambari.server.state.AlertState, java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity>> serviceAlerts = m_alertsByServiceAndState.get(serviceName);
            if (null == serviceAlerts) {
                return null;
            }
            org.apache.ambari.server.state.AlertState state = org.apache.ambari.server.state.AlertState.valueOf(alertState);
            return serviceAlerts.get(state);
        }

        public java.util.Set<java.lang.String> getServicesByAlertState(java.lang.String alertState) {
            return m_servicesByState.get(alertState);
        }
    }

    public static final class AmbariInfo {
        private java.lang.String m_hostName = null;

        private java.lang.String m_url = null;

        private java.lang.String m_version = null;

        protected AmbariInfo(org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.configuration.Configuration m_configuration) {
            m_url = m_configuration.getAmbariDisplayUrl();
            m_version = metaInfo.getServerVersion();
        }

        public java.lang.String getHostName() {
            return m_hostName;
        }

        public boolean hasUrl() {
            return m_url != null;
        }

        public java.lang.String getUrl() {
            return m_url;
        }

        public java.lang.String getServerVersion() {
            return m_version;
        }
    }

    public static final class DispatchInfo {
        private java.lang.String m_targetName;

        private java.lang.String m_targetDescription;

        protected DispatchInfo(org.apache.ambari.server.orm.entities.AlertTargetEntity target) {
            m_targetName = target.getTargetName();
            m_targetDescription = target.getDescription();
        }

        public java.lang.String getTargetName() {
            return m_targetName;
        }

        public java.lang.String getTargetDescription() {
            return m_targetDescription;
        }
    }

    @javax.xml.bind.annotation.XmlRootElement(name = "alert-templates")
    private static final class AlertTemplates {
        @javax.xml.bind.annotation.XmlElement(name = "alert-template", required = true)
        private java.util.List<org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplate> m_templates;

        public org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplate getTemplate(java.lang.String type) {
            for (org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertTemplate template : m_templates) {
                if (type.equals(template.getType())) {
                    return template;
                }
            }
            return null;
        }
    }

    private static final class AlertTemplate {
        @javax.xml.bind.annotation.XmlAttribute(name = "type", required = true)
        private java.lang.String m_type;

        @javax.xml.bind.annotation.XmlElement(name = "subject", required = true)
        private java.lang.String m_subject;

        @javax.xml.bind.annotation.XmlElement(name = "body", required = true)
        private java.lang.String m_body;

        public java.lang.String getType() {
            return m_type;
        }

        public java.lang.String getSubject() {
            return m_subject;
        }

        public java.lang.String getBody() {
            return m_body;
        }
    }
}