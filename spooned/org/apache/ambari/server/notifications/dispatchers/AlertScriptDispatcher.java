package org.apache.ambari.server.notifications.dispatchers;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
public class AlertScriptDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
    public static final java.lang.String SCRIPT_CONFIG_DEFAULT_KEY = "notification.dispatch.alert.script";

    public static final java.lang.String SCRIPT_CONFIG_TIMEOUT_KEY = "notification.dispatch.alert.script.timeout";

    public static final java.lang.String DISPATCH_PROPERTY_SCRIPT_CONFIG_KEY = "ambari.dispatch-property.script";

    public static final java.lang.String DISPATCH_PROPERTY_SCRIPT_FILENAME_KEY = "ambari.dispatch-property.script.filename";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class);

    private static final long DEFAULT_SCRIPT_TIMEOUT = 5000L;

    public static final com.google.common.escape.Escaper SHELL_ESCAPE;

    static {
        final com.google.common.escape.Escapers.Builder builder = com.google.common.escape.Escapers.builder();
        builder.addEscape('\"', "\\\"");
        builder.addEscape('!', "\\!");
        SHELL_ESCAPE = builder.build();
    }

    @com.google.inject.Inject
    protected org.apache.ambari.server.configuration.Configuration m_configuration;

    private final java.util.concurrent.Executor m_executor = new java.util.concurrent.ThreadPoolExecutor(0, 1, 5L, java.util.concurrent.TimeUnit.MINUTES, new java.util.concurrent.LinkedBlockingQueue<>(), new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.ScriptDispatchThreadFactory(), new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());

    public java.lang.String getScriptConfigurationKey(org.apache.ambari.server.notifications.Notification notification) {
        if ((null == notification) || (null == notification.DispatchProperties)) {
            return org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_DEFAULT_KEY;
        }
        if (null == notification.DispatchProperties.get(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_CONFIG_KEY)) {
            return org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_DEFAULT_KEY;
        }
        return notification.DispatchProperties.get(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_CONFIG_KEY);
    }

    public long getScriptConfigurationTimeout() {
        java.lang.String scriptTimeout = m_configuration.getProperty(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SCRIPT_CONFIG_TIMEOUT_KEY);
        if (null == scriptTimeout) {
            return org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DEFAULT_SCRIPT_TIMEOUT;
        }
        return java.lang.Long.parseLong(scriptTimeout);
    }

    @java.lang.Override
    public final java.lang.String getType() {
        return org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT.name();
    }

    @java.lang.Override
    public final boolean isNotificationContentGenerationRequired() {
        return false;
    }

    @java.lang.Override
    public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
        java.lang.String scriptKey = null;
        java.lang.String script = getScriptLocation(notification);
        if (null == script) {
            scriptKey = getScriptConfigurationKey(notification);
            script = m_configuration.getProperty(scriptKey);
        }
        if (null == script) {
            org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.LOG.warn("Unable to dispatch notification because the {} configuration property was not found", scriptKey);
            if (null != notification.Callback) {
                notification.Callback.onFailure(notification.CallbackIds);
            }
            return;
        }
        if (notification.getType() != org.apache.ambari.server.notifications.Notification.Type.ALERT) {
            org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.LOG.warn("The {} dispatcher is not able to dispatch notifications of type {}", getType(), notification.getType());
            if (null != notification.Callback) {
                notification.Callback.onFailure(notification.CallbackIds);
            }
            return;
        }
        long timeout = getScriptConfigurationTimeout();
        java.util.concurrent.TimeUnit timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS;
        org.apache.ambari.server.state.alert.AlertNotification alertNotification = ((org.apache.ambari.server.state.alert.AlertNotification) (notification));
        java.lang.ProcessBuilder processBuilder = getProcessBuilder(script, alertNotification);
        org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable runnable = new org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable(alertNotification, script, processBuilder, timeout, timeUnit);
        m_executor.execute(runnable);
    }

    java.lang.String getScriptLocation(org.apache.ambari.server.notifications.Notification notification) {
        java.lang.String scriptName = null;
        java.lang.String scriptDir = null;
        if ((null == notification) || (null == notification.DispatchProperties))
            return null;

        scriptName = notification.DispatchProperties.get(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_FILENAME_KEY);
        if (null == scriptName) {
            org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.LOG.warn("the {} configuration property was not found for dispatching notification", org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.DISPATCH_PROPERTY_SCRIPT_FILENAME_KEY);
            return null;
        }
        scriptDir = m_configuration.getDispatchScriptDirectory();
        return (scriptDir + java.io.File.separator) + scriptName;
    }

    @java.lang.Override
    public final boolean isDigestSupported() {
        return false;
    }

    @java.lang.Override
    public final org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return org.apache.ambari.server.notifications.TargetConfigurationResult.valid();
    }

    java.lang.ProcessBuilder getProcessBuilder(java.lang.String script, org.apache.ambari.server.state.alert.AlertNotification notification) {
        if ((script == null) || script.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Script path cannot be null or empty");
        }
        java.lang.String unixPathRegex = "^(/[a-zA-Z0-9._-]+)+$";
        java.lang.String windowsPathRegex = "^[a-zA-Z]:\\\\([a-zA-Z0-9._-]+\\\\?)+$";
        boolean isValidPath = (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) ? script.matches(windowsPathRegex) : script.matches(unixPathRegex);
        if (!isValidPath) {
            throw new java.lang.IllegalArgumentException("Invalid script path format: " + script);
        }
        java.nio.file.Path scriptPath = java.nio.file.Paths.get(script);
        if (!scriptPath.isAbsolute()) {
            throw new java.lang.IllegalArgumentException("Script path must be an absolute path: " + script);
        }
        if (!java.nio.file.Files.exists(scriptPath)) {
            throw new java.lang.IllegalArgumentException("Script does not exist: " + script);
        }
        if (!java.nio.file.Files.isExecutable(scriptPath)) {
            throw new java.lang.IllegalArgumentException("Script is not executable: " + script);
        }
        final java.lang.String shellCommand;
        final java.lang.String shellCommandOption;
        if (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) {
            shellCommand = "cmd";
            shellCommandOption = "/c";
        } else {
            shellCommand = "sh";
            shellCommandOption = "-c";
        }
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = notification.getAlertInfo();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = alertInfo.getAlertDefinition();
        java.lang.String definitionName = definition.getDefinitionName();
        org.apache.ambari.server.state.AlertState alertState = alertInfo.getAlertState();
        java.lang.String serviceName = alertInfo.getServiceName();
        java.lang.String alertLabel = ("\"" + org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SHELL_ESCAPE.escape(definition.getLabel())) + "\"";
        java.lang.String alertText = ("\"" + org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.SHELL_ESCAPE.escape(alertInfo.getAlertText())) + "\"";
        long alertTimestamp = alertInfo.getAlertTimestamp();
        java.lang.String hostName = alertInfo.getHostName();
        java.lang.Object[] params = new java.lang.Object[]{ script, definitionName, alertLabel, serviceName, alertState.name(), alertText, alertTimestamp, hostName };
        java.lang.String foo = org.apache.commons.lang.StringUtils.join(params, " ");
        return new java.lang.ProcessBuilder(shellCommand, shellCommandOption, foo);
    }

    private static final class AlertScriptRunnable implements java.lang.Runnable {
        private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable.class);

        private final java.lang.ProcessBuilder m_processBuilder;

        private final long m_timeout;

        private final java.util.concurrent.TimeUnit m_timeoutUnits;

        private final org.apache.ambari.server.notifications.Notification m_notification;

        private final java.lang.String m_script;

        private AlertScriptRunnable(org.apache.ambari.server.notifications.Notification notification, java.lang.String script, java.lang.ProcessBuilder processBuilder, long timeout, java.util.concurrent.TimeUnit timeoutUnits) {
            m_notification = notification;
            m_script = script;
            m_processBuilder = processBuilder;
            m_timeout = timeout;
            m_timeoutUnits = timeoutUnits;
        }

        @java.lang.Override
        public void run() {
            boolean isDispatchSuccessful = true;
            try {
                java.lang.Process process = m_processBuilder.start();
                int exitCode = execute(process, m_timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (exitCode != 0) {
                    org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable.LOG.warn("Unable to dispatch {} notification because {} terminated with exit code {}", org.apache.ambari.server.state.alert.TargetType.ALERT_SCRIPT, m_script, exitCode);
                    isDispatchSuccessful = false;
                }
            } catch (java.util.concurrent.TimeoutException timeoutException) {
                isDispatchSuccessful = false;
                org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable.LOG.warn("Unable to dispatch notification with {} in under {}ms", m_script, m_timeoutUnits.toMillis(m_timeout), timeoutException);
            } catch (java.lang.Exception exception) {
                isDispatchSuccessful = false;
                org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.AlertScriptRunnable.LOG.warn("Unable to dispatch notification with {}", m_script, exception);
            }
            if (null != m_notification.Callback) {
                if (isDispatchSuccessful) {
                    m_notification.Callback.onSuccess(m_notification.CallbackIds);
                } else {
                    m_notification.Callback.onFailure(m_notification.CallbackIds);
                }
            }
        }

        public int execute(java.lang.Process process, long timeout, java.util.concurrent.TimeUnit unit) throws java.util.concurrent.TimeoutException, java.lang.InterruptedException {
            long timeRemaining = unit.toMillis(timeout);
            long startTime = java.lang.System.currentTimeMillis();
            while (timeRemaining > 0) {
                try {
                    return process.exitValue();
                } catch (java.lang.IllegalThreadStateException ex) {
                    java.lang.Thread.sleep(java.lang.Math.min(timeRemaining, 500));
                }
                long timeElapsed = java.lang.System.currentTimeMillis() - startTime;
                timeRemaining = unit.toMillis(timeout) - timeElapsed;
            } 
            process.destroy();
            throw new java.util.concurrent.TimeoutException();
        }
    }

    private static final class ScriptDispatchThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final java.util.concurrent.atomic.AtomicInteger s_threadIdPool = new java.util.concurrent.atomic.AtomicInteger(1);

        @java.lang.Override
        public java.lang.Thread newThread(java.lang.Runnable r) {
            java.lang.Thread thread = new java.lang.Thread(r, "script-dispatcher-" + org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.ScriptDispatchThreadFactory.s_threadIdPool.getAndIncrement());
            thread.setDaemon(false);
            thread.setPriority(java.lang.Thread.NORM_PRIORITY - 1);
            return thread;
        }
    }
}