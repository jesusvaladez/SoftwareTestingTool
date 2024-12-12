package org.apache.ambari.server.controller.logging;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
public class LoggingRequestHelperImpl implements org.apache.ambari.server.controller.logging.LoggingRequestHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.class);

    private static final java.lang.String LOGSEARCH_ADMIN_JSON_CONFIG_TYPE_NAME = "logsearch-admin-json";

    private static final java.lang.String LOGSEARCH_ADMIN_USERNAME_PROPERTY_NAME = "logsearch_admin_username";

    private static final java.lang.String LOGSEARCH_ADMIN_PASSWORD_PROPERTY_NAME = "logsearch_admin_password";

    private static final java.lang.String LOGSEARCH_QUERY_PATH = "/api/v1/service/logs";

    private static final java.lang.String LOGSEARCH_GET_LOG_LEVELS_PATH = "/api/v1/service/logs/levels/counts";

    private static final java.lang.String LOGSEARCH_GET_LOG_FILES_PATH = "/api/v1/service/logs/files";

    private static final java.lang.String LOGSEARCH_ADMIN_CREDENTIAL_NAME = "logsearch.admin.credential";

    private static final java.lang.String COMPONENT_QUERY_PARAMETER_NAME = "component_name";

    private static final java.lang.String HOST_QUERY_PARAMETER_NAME = "host_name";

    private static final java.lang.String DEFAULT_PAGE_SIZE = "50";

    private static final java.lang.String PAGE_SIZE_QUERY_PARAMETER_NAME = "pageSize";

    private static final java.lang.String COOKIE_HEADER = "Cookie";

    private static final java.lang.String SET_COOKIES_HEADER = "Set-Cookie";

    private static final int DEFAULT_LOGSEARCH_CONNECT_TIMEOUT_IN_MILLISECONDS = 5000;

    private static final int DEFAULT_LOGSEARCH_READ_TIMEOUT_IN_MILLISECONDS = 5000;

    private static final java.lang.String LOGSEARCH_CLUSTERS_QUERY_PARAMETER_NAME = "clusters";

    private static java.util.concurrent.atomic.AtomicInteger errorLogCounterForLogSearchConnectionExceptions = new java.util.concurrent.atomic.AtomicInteger(0);

    private final java.lang.String hostName;

    private final java.lang.String portNumber;

    private final java.lang.String protocol;

    private final org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    private final org.apache.ambari.server.state.Cluster cluster;

    private final java.lang.String externalAddress;

    private final org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnection;

    private javax.net.ssl.SSLSocketFactory sslSocketFactory;

    private int logSearchConnectTimeoutInMilliseconds = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.DEFAULT_LOGSEARCH_CONNECT_TIMEOUT_IN_MILLISECONDS;

    private int logSearchReadTimeoutInMilliseconds = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.DEFAULT_LOGSEARCH_READ_TIMEOUT_IN_MILLISECONDS;

    public LoggingRequestHelperImpl(java.lang.String hostName, java.lang.String portNumber, java.lang.String protocol, org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService, org.apache.ambari.server.state.Cluster cluster, java.lang.String externalAddress) {
        this(hostName, portNumber, protocol, credentialStoreService, cluster, externalAddress, new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.DefaultNetworkConnection());
    }

    protected LoggingRequestHelperImpl(java.lang.String hostName, java.lang.String portNumber, java.lang.String protocol, org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService, org.apache.ambari.server.state.Cluster cluster, java.lang.String externalAddress, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnection) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.protocol = protocol;
        this.credentialStoreService = credentialStoreService;
        this.cluster = cluster;
        this.externalAddress = externalAddress;
        this.networkConnection = networkConnection;
    }

    public int getLogSearchConnectTimeoutInMilliseconds() {
        return this.logSearchConnectTimeoutInMilliseconds;
    }

    public void setLogSearchConnectTimeoutInMilliseconds(int logSearchConnectTimeoutInMilliseconds) {
        this.logSearchConnectTimeoutInMilliseconds = logSearchConnectTimeoutInMilliseconds;
    }

    public int getLogSearchReadTimeoutInMilliseconds() {
        return this.logSearchReadTimeoutInMilliseconds;
    }

    public void setLogSearchReadTimeoutInMilliseconds(int logSearchReadTimeoutInMilliseconds) {
        this.logSearchReadTimeoutInMilliseconds = logSearchReadTimeoutInMilliseconds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.logging.LogQueryResponse sendQueryRequest(java.util.Map<java.lang.String, java.lang.String> queryParameters) {
        try {
            java.net.URI logSearchURI = createLogSearchQueryURI(protocol, queryParameters);
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Attempting to connect to LogSearch server at {}", logSearchURI);
            java.net.HttpURLConnection httpURLConnection = ((java.net.HttpURLConnection) (logSearchURI.toURL().openConnection()));
            secure(httpURLConnection, protocol);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(logSearchConnectTimeoutInMilliseconds);
            httpURLConnection.setReadTimeout(logSearchReadTimeoutInMilliseconds);
            addCookiesFromCookieStore(httpURLConnection);
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Attempting request to LogSearch Portal Server, with connect timeout = {} milliseconds and read timeout = {} milliseconds", logSearchConnectTimeoutInMilliseconds, logSearchReadTimeoutInMilliseconds);
            setupCredentials(httpURLConnection);
            java.lang.StringBuffer buffer = networkConnection.readQueryResponseFromServer(httpURLConnection);
            addCookiesToCookieStoreFromResponse(httpURLConnection);
            java.io.StringReader stringReader = new java.io.StringReader(buffer.toString());
            org.codehaus.jackson.map.ObjectReader logQueryResponseReader = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createObjectReader(org.apache.ambari.server.controller.logging.LogQueryResponse.class);
            return logQueryResponseReader.readValue(stringReader);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.errorLogCounterForLogSearchConnectionExceptions, "Error occurred while trying to connect to the LogSearch service...", e);
        }
        return null;
    }

    private void secure(java.net.HttpURLConnection connection, java.lang.String protocol) {
        if ("https".equals(protocol)) {
            javax.net.ssl.HttpsURLConnection secureConnection = ((javax.net.ssl.HttpsURLConnection) (connection));
            loadTrustStore();
            secureConnection.setSSLSocketFactory(this.sslSocketFactory);
        }
    }

    private void loadTrustStore() {
        if (this.sslSocketFactory == null) {
            org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfig = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
            java.lang.String trustStorePath = sslConfig.getTruststorePath();
            java.lang.String trustStoreType = sslConfig.getTruststoreType();
            java.lang.String trustStorePassword = sslConfig.getTruststorePassword();
            if ((trustStorePath == null) || (trustStorePassword == null)) {
                java.lang.String trustStoreErrorMsg = "Can\'t load TrustStore. Truststore path or password is not set.";
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.error(trustStoreErrorMsg);
                throw new java.lang.IllegalStateException(trustStoreErrorMsg);
            }
            try (java.io.FileInputStream in = new java.io.FileInputStream(new java.io.File(trustStorePath))) {
                java.security.KeyStore e = java.security.KeyStore.getInstance(trustStoreType == null ? java.security.KeyStore.getDefaultType() : trustStoreType);
                e.load(in, trustStorePassword.toCharArray());
                javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(e);
                javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("TLS");
                context.init(((javax.net.ssl.KeyManager[]) (null)), tmf.getTrustManagers(), ((java.security.SecureRandom) (null)));
                this.sslSocketFactory = context.getSocketFactory();
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.error("Unable to load TrustStore", ex);
            }
        }
    }

    private void addCookiesFromCookieStore(java.net.HttpURLConnection httpURLConnection) {
        if (org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().size() > 0) {
            java.util.List<java.lang.String> cookiesStrList = new java.util.ArrayList<>();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().entrySet()) {
                cookiesStrList.add(java.lang.String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
            httpURLConnection.setRequestProperty(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.COOKIE_HEADER, org.apache.commons.lang.StringUtils.join(cookiesStrList, "; "));
        }
    }

    private void addCookiesToCookieStoreFromResponse(java.net.HttpURLConnection httpURLConnection) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerFields = httpURLConnection.getHeaderFields();
        java.util.List<java.lang.String> cookiesHeader = headerFields.get(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.SET_COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (java.lang.String cookie : cookiesHeader) {
                java.net.HttpCookie cookie1 = java.net.HttpCookie.parse(cookie).get(0);
                org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.addCookie(cookie1.getName(), cookie1.getValue());
            }
        }
    }

    private void setupCredentials(java.net.HttpURLConnection httpURLConnection) {
        final java.lang.String logSearchAdminUser = getLogSearchAdminUser();
        final java.lang.String logSearchAdminPassword = getLogSearchAdminPassword();
        if (((logSearchAdminUser != null) && (logSearchAdminPassword != null)) && org.apache.commons.lang.StringUtils.isEmpty(externalAddress)) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Credential found in config, will be used to connect to LogSearch");
            networkConnection.setupBasicAuthentication(httpURLConnection, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createEncodedCredentials(logSearchAdminUser, logSearchAdminPassword));
        } else {
            org.apache.ambari.server.security.credential.PrincipalKeyCredential principalKeyCredential = getLogSearchCredentials();
            if (principalKeyCredential != null) {
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Credential found in CredentialStore, will be used to connect to LogSearch");
                networkConnection.setupBasicAuthentication(httpURLConnection, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createEncodedCredentials(principalKeyCredential));
            } else {
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("No LogSearch credential could be found, this is probably an error in configuration");
                if (org.apache.commons.lang.StringUtils.isEmpty(externalAddress)) {
                    org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.error("No LogSearch credential could be found, this is required for external LogSearch (credential: {})", org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_CREDENTIAL_NAME);
                }
            }
        }
    }

    private java.lang.String getLogSearchAdminUser() {
        org.apache.ambari.server.state.Config logSearchAdminConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_JSON_CONFIG_TYPE_NAME);
        if (logSearchAdminConfig != null) {
            return logSearchAdminConfig.getProperties().get(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_USERNAME_PROPERTY_NAME);
        }
        return null;
    }

    private java.lang.String getLogSearchAdminPassword() {
        org.apache.ambari.server.state.Config logSearchAdminConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_JSON_CONFIG_TYPE_NAME);
        if (logSearchAdminConfig != null) {
            return logSearchAdminConfig.getProperties().get(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_PASSWORD_PROPERTY_NAME);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.logging.HostLogFilesResponse sendGetLogFileNamesRequest(java.lang.String hostName) {
        try {
            org.apache.http.client.utils.URIBuilder uriBuilder = createBasicURI(protocol);
            appendUriPath(uriBuilder, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_GET_LOG_FILES_PATH);
            uriBuilder.addParameter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.HOST_QUERY_PARAMETER_NAME, hostName);
            uriBuilder.addParameter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_CLUSTERS_QUERY_PARAMETER_NAME, cluster.getClusterName());
            java.net.URI logFileNamesURI = uriBuilder.build();
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Attempting to connect to LogSearch server at {}", logFileNamesURI);
            java.net.HttpURLConnection httpURLConnection = ((java.net.HttpURLConnection) (logFileNamesURI.toURL().openConnection()));
            secure(httpURLConnection, protocol);
            httpURLConnection.setRequestMethod("GET");
            addCookiesFromCookieStore(httpURLConnection);
            setupCredentials(httpURLConnection);
            java.lang.StringBuffer buffer = networkConnection.readQueryResponseFromServer(httpURLConnection);
            addCookiesToCookieStoreFromResponse(httpURLConnection);
            java.io.StringReader stringReader = new java.io.StringReader(buffer.toString());
            org.codehaus.jackson.map.ObjectReader hostFilesQueryResponseReader = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createObjectReader(org.apache.ambari.server.controller.logging.HostLogFilesResponse.class);
            org.apache.ambari.server.controller.logging.HostLogFilesResponse response = hostFilesQueryResponseReader.readValue(stringReader);
            if ((org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.isDebugEnabled() && (response != null)) && org.apache.commons.collections.MapUtils.isNotEmpty(response.getHostLogFiles())) {
                for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> componentEntry : response.getHostLogFiles().entrySet()) {
                    org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Log files for component '{}' : {}", componentEntry.getKey(), org.apache.commons.lang.StringUtils.join(componentEntry.getValue(), ","));
                }
            }
            return response;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.errorLogCounterForLogSearchConnectionExceptions, "Error occurred while trying to connect to the LogSearch service...", e);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.logging.LogLevelQueryResponse sendLogLevelQueryRequest(java.lang.String componentName, java.lang.String hostName) {
        try {
            java.net.URI logLevelQueryURI = createLogLevelQueryURI(protocol, componentName, hostName);
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Attempting to connect to LogSearch server at {}", logLevelQueryURI);
            java.net.HttpURLConnection httpURLConnection = ((java.net.HttpURLConnection) (logLevelQueryURI.toURL().openConnection()));
            secure(httpURLConnection, protocol);
            httpURLConnection.setRequestMethod("GET");
            addCookiesFromCookieStore(httpURLConnection);
            setupCredentials(httpURLConnection);
            java.lang.StringBuffer buffer = networkConnection.readQueryResponseFromServer(httpURLConnection);
            addCookiesToCookieStoreFromResponse(httpURLConnection);
            java.io.StringReader stringReader = new java.io.StringReader(buffer.toString());
            org.codehaus.jackson.map.ObjectReader logQueryResponseReader = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createObjectReader(org.apache.ambari.server.controller.logging.LogLevelQueryResponse.class);
            return logQueryResponseReader.readValue(stringReader);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.errorLogCounterForLogSearchConnectionExceptions, "Error occurred while trying to connect to the LogSearch service...", e);
        }
        return null;
    }

    @java.lang.Override
    public java.lang.String createLogFileTailURI(java.lang.String baseURI, java.lang.String componentName, java.lang.String hostName) {
        return (((((((((((baseURI + "?") + org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.COMPONENT_QUERY_PARAMETER_NAME) + "=") + componentName) + "&") + org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.HOST_QUERY_PARAMETER_NAME) + "=") + hostName) + "&") + org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.PAGE_SIZE_QUERY_PARAMETER_NAME) + "=") + org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.DEFAULT_PAGE_SIZE;
    }

    private static org.codehaus.jackson.map.ObjectReader createObjectReader(java.lang.Class type) {
        org.codehaus.jackson.map.ObjectMapper mapper = org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createJSONObjectMapper();
        return mapper.reader(type);
    }

    private java.net.URI createLogSearchQueryURI(java.lang.String scheme, java.util.Map<java.lang.String, java.lang.String> queryParameters) throws java.net.URISyntaxException {
        org.apache.http.client.utils.URIBuilder uriBuilder = createBasicURI(scheme);
        appendUriPath(uriBuilder, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_QUERY_PATH);
        uriBuilder.addParameter(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_CLUSTERS_QUERY_PARAMETER_NAME, cluster.getClusterName());
        for (java.lang.String key : queryParameters.keySet()) {
            uriBuilder.addParameter(key, queryParameters.get(key));
        }
        return uriBuilder.build();
    }

    private org.apache.http.client.utils.URIBuilder createBasicURI(java.lang.String scheme) throws java.net.URISyntaxException {
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder();
        if (org.apache.commons.lang.StringUtils.isNotBlank(externalAddress)) {
            final java.net.URI uri = new java.net.URI(externalAddress);
            uriBuilder.setScheme(uri.getScheme());
            uriBuilder.setHost(uri.getHost());
            if (uri.getPort() != (-1)) {
                uriBuilder.setPort(uri.getPort());
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(uri.getPath())) {
                uriBuilder.setPath(uri.getPath());
            }
        } else {
            uriBuilder.setScheme(scheme);
            uriBuilder.setHost(hostName);
            uriBuilder.setPort(java.lang.Integer.parseInt(portNumber));
        }
        return uriBuilder;
    }

    private void appendUriPath(org.apache.http.client.utils.URIBuilder uriBuilder, java.lang.String path) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(uriBuilder.getPath())) {
            uriBuilder.setPath(uriBuilder.getPath() + path);
        } else {
            uriBuilder.setPath(path);
        }
    }

    private java.net.URI createLogLevelQueryURI(java.lang.String scheme, java.lang.String componentName, java.lang.String hostName) throws java.net.URISyntaxException {
        org.apache.http.client.utils.URIBuilder uriBuilder = createBasicURI(scheme);
        appendUriPath(uriBuilder, org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_GET_LOG_LEVELS_PATH);
        java.util.Map<java.lang.String, java.lang.String> queryParameters = new java.util.HashMap<>();
        queryParameters.put(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.HOST_QUERY_PARAMETER_NAME, hostName);
        queryParameters.put(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.COMPONENT_QUERY_PARAMETER_NAME, componentName);
        for (java.lang.String key : queryParameters.keySet()) {
            uriBuilder.addParameter(key, queryParameters.get(key));
        }
        return uriBuilder.build();
    }

    protected static org.codehaus.jackson.map.ObjectMapper createJSONObjectMapper() {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        org.codehaus.jackson.map.AnnotationIntrospector introspector = new org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper;
    }

    private org.apache.ambari.server.security.credential.PrincipalKeyCredential getLogSearchCredentials() {
        try {
            org.apache.ambari.server.security.credential.Credential credential = credentialStoreService.getCredential(cluster.getClusterName(), org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOGSEARCH_ADMIN_CREDENTIAL_NAME);
            if ((credential != null) && (credential instanceof org.apache.ambari.server.security.credential.PrincipalKeyCredential)) {
                return ((org.apache.ambari.server.security.credential.PrincipalKeyCredential) (credential));
            }
            if (credential == null) {
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("LogSearch credentials could not be obtained from store.");
            } else {
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("LogSearch credentials were not of the correct type, this is likely an error in configuration, credential type is = {}", credential.getClass().getName());
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Error encountered while trying to obtain LogSearch admin credentials.", ambariException);
        }
        return null;
    }

    private static java.lang.String createEncodedCredentials(org.apache.ambari.server.security.credential.PrincipalKeyCredential principalKeyCredential) {
        return org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.createEncodedCredentials(principalKeyCredential.getPrincipal(), new java.lang.String(principalKeyCredential.getKey()));
    }

    private static java.lang.String createEncodedCredentials(java.lang.String userName, java.lang.String password) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(((userName + ":") + password).getBytes());
    }

    interface NetworkConnection {
        java.lang.StringBuffer readQueryResponseFromServer(java.net.HttpURLConnection httpURLConnection) throws java.io.IOException;

        void setupBasicAuthentication(java.net.HttpURLConnection httpURLConnection, java.lang.String encodedCredentials);
    }

    private static class DefaultNetworkConnection implements org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection {
        @java.lang.Override
        public java.lang.StringBuffer readQueryResponseFromServer(java.net.HttpURLConnection httpURLConnection) throws java.io.IOException {
            java.io.InputStream resultStream = null;
            try {
                resultStream = httpURLConnection.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(resultStream));
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Response code from LogSearch Service is = {}", httpURLConnection.getResponseCode());
                java.lang.String line = reader.readLine();
                java.lang.StringBuffer buffer = new java.lang.StringBuffer();
                while (line != null) {
                    buffer.append(line);
                    line = reader.readLine();
                } 
                org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.LOG.debug("Sucessfully retrieved response from server, response = {}", buffer);
                return buffer;
            } finally {
                if (resultStream != null) {
                    resultStream.close();
                }
            }
        }

        @java.lang.Override
        public void setupBasicAuthentication(java.net.HttpURLConnection httpURLConnection, java.lang.String encodedCredentials) {
            httpURLConnection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        }
    }
}