package org.apache.ambari.server;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.kerberos.client.KdcConfig;
import org.apache.directory.kerberos.client.KdcConnection;
import org.apache.directory.shared.kerberos.KerberosMessageType;
import org.apache.directory.shared.kerberos.exceptions.ErrorType;
import org.apache.directory.shared.kerberos.exceptions.KerberosException;
import org.apache.directory.shared.kerberos.messages.KrbError;
@com.google.inject.Singleton
public class KdcServerConnectionVerification {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.KdcServerConnectionVerification.class);

    private org.apache.ambari.server.configuration.Configuration config;

    private int connectionTimeout = 10;

    @com.google.inject.Inject
    public KdcServerConnectionVerification(org.apache.ambari.server.configuration.Configuration config) {
        this.config = config;
    }

    public boolean isKdcReachable(java.lang.String kdcHost) {
        try {
            if ((kdcHost == null) || kdcHost.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid hostname for KDC server");
            }
            java.lang.String[] kdcDetails = kdcHost.split(":");
            if (kdcDetails.length == 1) {
                return isKdcReachable(kdcDetails[0], parsePort(config.getDefaultKdcPort()));
            } else {
                return isKdcReachable(kdcDetails[0], parsePort(kdcDetails[1]));
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.KdcServerConnectionVerification.LOG.error("Exception while checking KDC reachability: " + e);
            return false;
        }
    }

    public boolean isKdcReachable(java.lang.String server, int port) {
        boolean success = isKdcReachable(server, port, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.TCP) || isKdcReachable(server, port, org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP);
        if (!success) {
            org.apache.ambari.server.KdcServerConnectionVerification.LOG.error("Failed to connect to the KDC at {}:{} using either TCP or UDP", server, port);
        }
        return success;
    }

    public boolean isKdcReachable(final java.lang.String server, final int port, final org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol connectionProtocol) {
        int timeoutMillis = connectionTimeout * 1000;
        final org.apache.directory.kerberos.client.KdcConfig config = org.apache.directory.kerberos.client.KdcConfig.getDefaultConfig();
        config.setHostName(server);
        config.setKdcPort(port);
        config.setUseUdp(org.apache.ambari.server.KdcServerConnectionVerification.ConnectionProtocol.UDP == connectionProtocol);
        config.setTimeout(timeoutMillis);
        java.util.concurrent.FutureTask<java.lang.Boolean> future = new java.util.concurrent.FutureTask<>(new java.util.concurrent.Callable<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean call() {
                java.lang.Boolean success;
                try {
                    org.apache.directory.kerberos.client.KdcConnection connection = getKdcConnection(config);
                    connection.getTgt("noUser@noRealm", "noPassword");
                    org.apache.ambari.server.KdcServerConnectionVerification.LOG.info(java.lang.String.format("Encountered no Exceptions while testing connectivity to the KDC:\n" + "**** Host: %s:%d (%s)", server, port, connectionProtocol.name()));
                    success = true;
                } catch (org.apache.directory.shared.kerberos.exceptions.KerberosException e) {
                    org.apache.directory.shared.kerberos.messages.KrbError error = e.getError();
                    org.apache.directory.shared.kerberos.exceptions.ErrorType errorCode = error.getErrorCode();
                    java.lang.String errorCodeMessage;
                    int errorCodeCode;
                    if (errorCode != null) {
                        errorCodeMessage = errorCode.getMessage();
                        errorCodeCode = errorCode.getValue();
                    } else {
                        errorCodeMessage = "<Not Specified>";
                        errorCodeCode = -1;
                    }
                    success = !((errorCodeCode == ErrorType.KRB_ERR_GENERIC.getValue()) && errorCodeMessage.contains("TimeOut"));
                    if ((!success) || org.apache.ambari.server.KdcServerConnectionVerification.LOG.isDebugEnabled()) {
                        org.apache.directory.shared.kerberos.KerberosMessageType messageType = error.getMessageType();
                        java.lang.String messageTypeMessage;
                        int messageTypeCode;
                        if (messageType != null) {
                            messageTypeMessage = messageType.getMessage();
                            messageTypeCode = messageType.getValue();
                        } else {
                            messageTypeMessage = "<Not Specified>";
                            messageTypeCode = -1;
                        }
                        java.lang.String message = java.lang.String.format("Received KerberosException while testing connectivity to the KDC: %s\n" + ((("**** Host:    %s:%d (%s)\n" + "**** Error:   %s\n") + "**** Code:    %d (%s)\n") + "**** Message: %d (%s)"), e.getLocalizedMessage(), server, port, connectionProtocol.name(), error.getEText(), errorCodeCode, errorCodeMessage, messageTypeCode, messageTypeMessage);
                        if (org.apache.ambari.server.KdcServerConnectionVerification.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.KdcServerConnectionVerification.LOG.info(message, e);
                        } else {
                            org.apache.ambari.server.KdcServerConnectionVerification.LOG.info(message);
                        }
                    }
                } catch (java.lang.Throwable e) {
                    org.apache.ambari.server.KdcServerConnectionVerification.LOG.info(java.lang.String.format("Received Exception while testing connectivity to the KDC: %s\n**** Host: %s:%d (%s)", e.getLocalizedMessage(), server, port, connectionProtocol.name()), e);
                    throw new java.lang.RuntimeException(e);
                }
                return success;
            }
        });
        new java.lang.Thread(future, "ambari-kdc-verify").start();
        java.lang.Boolean result;
        try {
            result = future.get(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (result) {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.info(java.lang.String.format("Successfully connected to the KDC server at %s:%d over %s", server, port, connectionProtocol.name()));
            } else {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(java.lang.String.format("Failed to connect to the KDC server at %s:%d over %s", server, port, connectionProtocol.name()));
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.String message = java.lang.String.format("Interrupted while trying to communicate with KDC server at %s:%d over %s", server, port, connectionProtocol.name());
            if (org.apache.ambari.server.KdcServerConnectionVerification.LOG.isDebugEnabled()) {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message, e);
            } else {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message);
            }
            result = false;
            future.cancel(true);
        } catch (java.util.concurrent.ExecutionException e) {
            java.lang.String message = java.lang.String.format("An unexpected exception occurred while attempting to communicate with the KDC server at %s:%d over %s", server, port, connectionProtocol.name());
            if (org.apache.ambari.server.KdcServerConnectionVerification.LOG.isDebugEnabled()) {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message, e);
            } else {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message);
            }
            result = false;
        } catch (java.util.concurrent.TimeoutException e) {
            java.lang.String message = java.lang.String.format("Timeout occurred while attempting to to communicate with KDC server at %s:%d over %s", server, port, connectionProtocol.name());
            if (org.apache.ambari.server.KdcServerConnectionVerification.LOG.isDebugEnabled()) {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message, e);
            } else {
                org.apache.ambari.server.KdcServerConnectionVerification.LOG.warn(message);
            }
            result = false;
            future.cancel(true);
        }
        return result;
    }

    protected org.apache.directory.kerberos.client.KdcConnection getKdcConnection(org.apache.directory.kerberos.client.KdcConfig config) {
        return new org.apache.directory.kerberos.client.KdcConnection(config);
    }

    public void setConnectionTimeout(int timeoutSeconds) {
        connectionTimeout = (timeoutSeconds < 1) ? 1 : timeoutSeconds;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    protected int parsePort(java.lang.String port) {
        if (org.apache.commons.lang.StringUtils.isEmpty(port)) {
            throw new java.lang.IllegalArgumentException("Port number must be non-empty, non-null positive integer");
        }
        return java.lang.Integer.parseInt(port);
    }

    public enum ConnectionProtocol {

        TCP,
        UDP;}
}