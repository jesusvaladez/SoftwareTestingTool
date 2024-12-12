package org.apache.ambari.server.notifications.dispatchers;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
@com.google.inject.Singleton
public class EmailDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.class);

    public static final java.lang.String JAVAMAIL_FROM_PROPERTY = "mail.smtp.from";

    @java.lang.Override
    public java.lang.String getType() {
        return org.apache.ambari.server.state.alert.TargetType.EMAIL.name();
    }

    @java.lang.Override
    public boolean isNotificationContentGenerationRequired() {
        return true;
    }

    @java.lang.Override
    public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.info("Sending email: {}", notification);
        if (null == notification.DispatchProperties) {
            org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.error("Unable to dispatch an email notification that does not contain SMTP properties");
            if (null != notification.Callback) {
                notification.Callback.onFailure(notification.CallbackIds);
            }
            return;
        }
        java.lang.String fromAddress = null;
        java.util.Properties properties = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : notification.DispatchProperties.entrySet()) {
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            properties.put(key, value);
            if (key.equals(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.JAVAMAIL_FROM_PROPERTY)) {
                fromAddress = value;
            }
        }
        if (null == notification.Recipients) {
            org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.error("Unable to dispatch an email notification that does not have recipients");
            if (null != notification.Callback) {
                notification.Callback.onFailure(notification.CallbackIds);
            }
            return;
        }
        final javax.mail.Session session;
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.EmailAuthenticator authenticator = null;
        if (null != notification.Credentials) {
            authenticator = new org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.EmailAuthenticator(notification.Credentials);
        }
        session = javax.mail.Session.getInstance(properties, authenticator);
        try {
            javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
            for (org.apache.ambari.server.notifications.Recipient recipient : notification.Recipients) {
                javax.mail.internet.InternetAddress address = new javax.mail.internet.InternetAddress(recipient.Identifier);
                message.addRecipient(RecipientType.TO, address);
            }
            message.setSentDate(new java.util.Date());
            message.setSubject(notification.Subject);
            message.setText(notification.Body, "UTF-8", "html");
            if (null != fromAddress) {
                message.setFrom(fromAddress);
            }
            javax.mail.Transport.send(message);
            if (org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.isDebugEnabled()) {
                org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.debug("Successfully dispatched email to {}", notification.Recipients);
            }
            if (null != notification.Callback) {
                notification.Callback.onSuccess(notification.CallbackIds);
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.error("Unable to dispatch notification via Email", exception);
            if (null != notification.Callback) {
                notification.Callback.onFailure(notification.CallbackIds);
            }
        } finally {
            try {
                session.getTransport().close();
            } catch (javax.mail.MessagingException me) {
                org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.warn("Dispatcher unable to close SMTP transport", me);
            }
        }
    }

    @java.lang.Override
    public boolean isDigestSupported() {
        return true;
    }

    @java.lang.Override
    public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
        try {
            javax.mail.Transport transport = getMailTransport(properties);
            transport.connect();
            transport.close();
        } catch (javax.mail.AuthenticationFailedException e) {
            org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.debug("Invalid credentials. Authentication failure.", e);
            return org.apache.ambari.server.notifications.TargetConfigurationResult.invalid("Invalid credentials. Authentication failure: " + e.getMessage());
        } catch (javax.mail.MessagingException e) {
            org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.LOG.debug("Invalid config.", e);
            return org.apache.ambari.server.notifications.TargetConfigurationResult.invalid("Invalid config: " + e.getMessage());
        }
        return org.apache.ambari.server.notifications.TargetConfigurationResult.valid();
    }

    protected javax.mail.Transport getMailTransport(java.util.Map<java.lang.String, java.lang.Object> properties) throws javax.mail.NoSuchProviderException {
        org.apache.ambari.server.notifications.DispatchCredentials credentials = null;
        if (properties.containsKey(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_USERNAME)) {
            credentials = new org.apache.ambari.server.notifications.DispatchCredentials();
            credentials.UserName = ((java.lang.String) (properties.get(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_USERNAME)));
            credentials.Password = ((java.lang.String) (properties.get(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_CREDENTIAL_PASSWORD)));
        }
        java.util.Properties props = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        javax.mail.Session session = javax.mail.Session.getInstance(props, new org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.EmailAuthenticator(credentials));
        return session.getTransport();
    }

    private static final class EmailAuthenticator extends javax.mail.Authenticator {
        private final org.apache.ambari.server.notifications.DispatchCredentials m_credentials;

        private EmailAuthenticator(org.apache.ambari.server.notifications.DispatchCredentials credentials) {
            m_credentials = credentials;
        }

        @java.lang.Override
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            if (m_credentials != null) {
                return new javax.mail.PasswordAuthentication(m_credentials.UserName, m_credentials.Password);
            }
            return null;
        }
    }
}