package org.apache.ambari.server.configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@com.google.inject.Singleton
public class Configuration {
    private static final java.lang.String AMBARI_CONFIGURATION_MD_TEMPLATE_PROPERTY = "ambari.configuration.md.template";

    private static final java.lang.String MARKDOWN_TEMPLATE_FILE = "index_template.md";

    private static final java.lang.String MARKDOWN_CONFIGURATION_TABLE_KEY = "$configuration-properties";

    private static final java.lang.String MARKDOWN_BASELINE_VALUES_KEY = "$baseline-values";

    private static final java.lang.String MARKDOWN_RELATED_TO_TEMPLATE = " This property is related to `%s`.";

    private static final java.lang.String HTML_BREAK_TAG = "<br/>";

    private static final java.lang.String AGENT_CONFIGS_DEFAULT_SECTION = "agentConfig";

    @com.google.inject.Inject
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    private static final java.lang.String CONFIG_FILE = "ambari.properties";

    public static final java.lang.String PREFIX_DIR = "/var/lib/ambari-agent/data";

    public static final float JDK_MIN_VERSION = 1.7F;

    public static final java.lang.String SERVER_JDBC_PROPERTIES_PREFIX = "server.jdbc.properties.";

    public static final java.lang.String SERVER_PERSISTENCE_PROPERTIES_PREFIX = "server.persistence.properties.";

    public static final java.lang.String HOSTNAME_MACRO = "{hostname}";

    public static final java.lang.String JDBC_UNIT_NAME = "ambari-server";

    public static final java.lang.String JDBC_LOCAL_URL = "jdbc:postgresql://localhost/";

    public static final java.lang.String DEFAULT_DERBY_SCHEMA = "ambari";

    public static final java.lang.String DEFAULT_H2_SCHEMA = "ambari";

    public static final java.lang.String JDBC_IN_MEMORY_URL = java.lang.String.format("jdbc:h2:mem:%1$s;ALIAS_COLUMN_NAME=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS %1$s\\;SET SCHEMA %1$s;", org.apache.ambari.server.configuration.Configuration.DEFAULT_DERBY_SCHEMA);

    public static final java.lang.String JDBC_IN_MEMORY_DRIVER = "org.h2.Driver";

    public static final java.lang.String JDBC_IN_MEMORY_USER = "sa";

    public static final java.lang.String JDBC_IN_MEMORY_PASSWORD = "";

    public static final java.lang.String JAVAX_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";

    public static final java.lang.String JAVAX_SSL_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    public static final java.lang.String JAVAX_SSL_TRUSTSTORE_TYPE = "javax.net.ssl.trustStoreType";

    public static final java.lang.String MAPREDUCE2_LOG4J_CONFIG_TAG = "mapreduce2-log4j";

    public static final int PROCESSOR_BASED_THREADPOOL_CORE_SIZE_DEFAULT = 2 * java.lang.Runtime.getRuntime().availableProcessors();

    public static final int PROCESSOR_BASED_THREADPOOL_MAX_SIZE_DEFAULT = 4 * java.lang.Runtime.getRuntime().availableProcessors();

    private static final java.util.Set<java.lang.String> dbConnectorPropertyNames = com.google.common.collect.Sets.newHashSet("custom.mysql.jdbc.name", "custom.oracle.jdbc.name", "custom.postgres.jdbc.name", "custom.mssql.jdbc.name", "custom.hsqldb.jdbc.name", "custom.sqlanywhere.jdbc.name");

    public static final java.lang.String MASTER_KEY_ENV_PROP = "AMBARI_SECURITY_MASTER_KEY";

    public static final java.lang.String MASTER_KEY_FILENAME_DEFAULT = "master";

    public static final java.lang.String MASTER_KEYSTORE_FILENAME_DEFAULT = "credentials.jceks";

    public static final java.lang.String SERVER_VERSION_KEY = "version";

    public static final java.lang.String AMBARI_LOG_FILE = "log4j.properties";

    public static final int MAXIMUM_PASSWORD_HISTORY_LIMIT = 10;

    public static final int MINIMUM_PASSWORD_HISTORY_LIMIT = 1;

    @org.apache.ambari.annotations.Markdown(description = "Interval for heartbeat presence checks.", examples = { "60000", "600000" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> HEARTBEAT_MONITORING_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("heartbeat.monitoring.interval", 60000);

    @org.apache.ambari.annotations.Markdown(description = "The directory on the Ambari Server file system used for storing Ambari Agent bootstrap information such as request responses.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> BOOTSTRAP_DIRECTORY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("bootstrap.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/run/ambari-server/bootstrap"));

    @org.apache.ambari.annotations.Markdown(description = "The directory on the Ambari Server file system used for expanding Views and storing webapp work.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_DIRECTORY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/resources/views"));

    @org.apache.ambari.annotations.Markdown(description = "Determines whether to validate a View's configuration XML file against an XSD.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_VALIDATE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.validate", "false");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether the view directory watcher service should be disabled.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> DISABLE_VIEW_DIRECTORY_WATCHER = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.directory.watcher.disable", "false");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether remove undeployed views from the Ambari database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_REMOVE_UNDEPLOYED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.remove.undeployed", "false");

    @org.apache.ambari.annotations.Markdown(description = "The Ambari Server webapp root directory.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> WEBAPP_DIRECTORY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("webapp.dir", "web");

    @org.apache.ambari.annotations.Markdown(description = "The location and name of the Python script used to bootstrap new Ambari Agent hosts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> BOOTSTRAP_SCRIPT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("bootstrap.script", org.apache.ambari.server.utils.AmbariPath.getPath("/usr/lib/ambari-server/lib/ambari_server/bootstrap.py"));

    @org.apache.ambari.annotations.Markdown(description = "The location and name of the Python script executed on the Ambari Agent host during the bootstrap process.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> BOOTSTRAP_SETUP_AGENT_SCRIPT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("bootstrap.setup_agent.script", org.apache.ambari.server.utils.AmbariPath.getPath("/usr/lib/ambari-server/lib/ambari_server/setupAgent.py"));

    @org.apache.ambari.annotations.Markdown(description = "The password to set on the `AMBARI_PASSPHRASE` environment variable before invoking the bootstrap script.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> BOOTSTRAP_SETUP_AGENT_PASSWORD = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("bootstrap.setup_agent.password", "password");

    @org.apache.ambari.annotations.Markdown(description = "The host name of the Ambari Server which will be used by the Ambari Agents for communication.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> BOOTSTRAP_MASTER_HOSTNAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("bootstrap.master_host_name", null);

    @org.apache.ambari.annotations.Markdown(description = "The amount of time that Recommendation API data is kept on the Ambari Server file system. This is specified using a `hdwmy` syntax for pairing the value with a time unit (hours, days, weeks, months, years)", examples = { "8h", "2w", "1m" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOMMENDATIONS_ARTIFACTS_LIFETIME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recommendations.artifacts.lifetime", "1w");

    @org.apache.ambari.annotations.Markdown(description = "Maximum number of recommendations artifacts at a given time", examples = { "50", "10", "100" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> RECOMMENDATIONS_ARTIFACTS_ROLLOVER_MAX = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recommendations.artifacts.rollover.max", 100);

    @org.apache.ambari.annotations.Markdown(description = "The directory on the Ambari Server file system used for storing Recommendation API artifacts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOMMENDATIONS_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recommendations.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/run/ambari-server/stack-recommendations"));

    @org.apache.ambari.annotations.Markdown(description = "The location and name of the Python stack advisor script executed when configuring services.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_ADVISOR_SCRIPT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stackadvisor.script", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/resources/scripts/stack_advisor.py"));

    @org.apache.ambari.annotations.Markdown(description = "The name of the shell script used to wrap all invocations of Python by Ambari. ")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AMBARI_PYTHON_WRAP = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ambari.python.wrap", "ambari-python-wrap");

    @org.apache.ambari.annotations.Markdown(description = "The username of the default user assumed to be executing API calls. When set, authentication is not required in order to login to Ambari or use the REST APIs.  ")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> API_AUTHENTICATED_USER = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.authenticated.user", null);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether SSL is used in for secure connections to Ambari. When enabled, ambari-server setup-https must be run in order to properly configure keystores.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> API_USE_SSL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.ssl", "false");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether Cross-Site Request Forgery attacks are prevented by looking for the `X-Requested-By` header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> API_CSRF_PREVENTION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.csrfPrevention.enabled", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether jetty Gzip compression is enabled or not.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> GZIP_HANDLER_JETTY_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("gzip.handler.jetty.enabled", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether data sent to and from the Ambari service should be compressed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> API_GZIP_COMPRESSION_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.gzip.compression.enabled", "true");

    @org.apache.ambari.annotations.Markdown(description = "Used in conjunction with `api.gzip.compression.enabled`, determines the mininum size that an HTTP request must be before it should be compressed. This is measured in bytes.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> API_GZIP_MIN_COMPRESSION_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.gzip.compression.min.size", "10240");

    @org.apache.ambari.annotations.Markdown(description = "Determiens whether communication with the Ambari Agents should have the JSON payloads compressed with GZIP.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AGENT_API_GZIP_COMPRESSION_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.api.gzip.compression.enabled", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether SSL is used to communicate between Ambari Server and Ambari Agents.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AGENT_USE_SSL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.ssl", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines Ambari user password policy. Passwords should match the regex")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PASSWORD_POLICY_REGEXP = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.password.policy.regexp", ".*");

    @org.apache.ambari.annotations.Markdown(description = "Password policy description that is shown to users")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PASSWORD_POLICY_DESCRIPTION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.password.policy.description", "");

    @org.apache.ambari.annotations.Markdown(description = "Password policy to mandate that new password should be different from previous passwords, this would be based on the history count configured by the user. Valid values are 1 to 10.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PASSWORD_POLICY_HISTORY_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.password.policy.history.count", "1");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether the Ambari Agent host names should be validated against a regular expression to ensure that they are well-formed.<br><br>WARNING: By setting this value to false, host names will not be validated, allowing a possible security vulnerability as described in CVE-2014-3582. See https://cwiki.apache.org/confluence/display/AMBARI/Ambari+Vulnerabilities for more information.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_AGENT_HOSTNAME_VALIDATE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.agent.hostname.validate", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether two-way SSL should be used between Ambari Server and Ambari Agents so that the agents must also use SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_TWO_WAY_SSL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.two_way_ssl", "false");

    @org.apache.ambari.annotations.Markdown(description = "The port that the Ambari Server will use to communicate with the agents over SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_TWO_WAY_SSL_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.two_way_ssl.port", "8441");

    @org.apache.ambari.annotations.Markdown(description = "The port that the Ambari Agents will use to communicate with the Ambari Server over SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_ONE_WAY_SSL_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.one_way_ssl.port", "8440");

    @org.apache.ambari.annotations.Markdown(description = "The directory on the Ambari Server where keystores are kept.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_KSTR_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.keys_dir", ".");

    @org.apache.ambari.annotations.Markdown(description = "The name of the file located in the `security.server.keys_dir` directory where certificates will be generated when Ambari uses the `openssl ca` command.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CRT_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.cert_name", "ca.crt");

    @org.apache.ambari.annotations.Markdown(description = "The name of the file located in the `security.server.keys_dir` directory containing the CA certificate chain used to verify certificates during 2-way SSL communications.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CRT_CHAIN_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.cert_chain_name", "ca_chain.pem");

    @org.apache.ambari.annotations.Markdown(description = "The name of the certificate request file used when generating certificates.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CSR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.csr_name", "ca.csr");

    @org.apache.ambari.annotations.Markdown(description = "The name of the private key used to sign requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_KEY_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.key_name", "ca.key");

    @org.apache.ambari.annotations.Markdown(description = "The name of the keystore file, located in `security.server.keys_dir`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KSTR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.keystore_name", "keystore.p12");

    @org.apache.ambari.annotations.Markdown(description = "The type of the keystore file specified in `security.server.key_name`. Self-signed certificates can be `PKCS12` while CA signed certificates are `JKS`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KSTR_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.keystore_type", "PKCS12");

    @org.apache.ambari.annotations.Markdown(description = "The name of the truststore file ambari uses to store trusted certificates. Located in `security.server.keys_dir`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> TSTR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.truststore_name", "keystore.p12");

    @org.apache.ambari.annotations.Markdown(description = "The type of the truststore file specified in `security.server.truststore_name`. Self-signed certificates can be `PKCS12` while CA signed certificates are `JKS`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> TSTR_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.truststore_type", "PKCS12");

    @org.apache.ambari.annotations.Markdown(description = "The filename which contains the password for the keystores, truststores, and certificates.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CRT_PASS_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.crt_pass_file", "pass.txt");

    @org.apache.ambari.annotations.Markdown(description = "The password for the keystores, truststores, and certificates. If not specified, then `security.server.crt_pass_file` should be used")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CRT_PASS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.crt_pass", null);

    @org.apache.ambari.annotations.Markdown(description = "The length of the randomly generated password for keystores and truststores. ")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_CRT_PASS_LEN = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.crt_pass.len", "50");

    @org.apache.ambari.annotations.Markdown(description = "An environment variable which can be used to supply the Ambari Server password when bootstrapping new Ambari Agents.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PASSPHRASE_ENV = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.passphrase_env_var", "AMBARI_PASSPHRASE");

    @org.apache.ambari.annotations.Markdown(description = "The password to the Ambari Server to supply to new Ambari Agent hosts being bootstrapped.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PASSPHRASE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.passphrase", "AMBARI_PASSPHRASE");

    @org.apache.ambari.annotations.Markdown(description = "A list of cipher suites which are not strong enough to use and will be excluded when creating SSL connections.", examples = { "SSL_RSA_WITH_RC4_128_MD5\\|SSL_RSA_WITH_RC4_12‌​8_MD5" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_DISABLED_CIPHERS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.disabled.ciphers", "");

    @org.apache.ambari.annotations.Markdown(description = "The list of protocols which should not be used when creating SSL connections.", examples = { "TLSv1.1\\|TLSv1.2" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_DISABLED_PROTOCOLS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.disabled.protocols", "");

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where all resources exist, including common services, stacks, and scripts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RESOURCES_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("resources.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/resources/"));

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where the stack resources exist.", examples = { "/var/lib/ambari-server/resources/stacks" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> METADATA_DIR_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("metadata.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where common service resources exist. Stack services share the common service files.", examples = { "/var/lib/ambari-server/resources/common-services" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> COMMON_SERVICES_DIR_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("common.services.path", null);

    @org.apache.ambari.annotations.Markdown(description = "Determines how to handle username collision while updating from LDAP.", examples = { "skip", "convert", "add" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> LDAP_SYNC_USERNAME_COLLISIONS_BEHAVIOR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ldap.sync.username.collision.behavior", "add");

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where stack extensions exist.", examples = { "/var/lib/ambari-server/resources/extensions" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> EXTENSIONS_DIR_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("extensions.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The Ambari Management Pack staging directory on the Ambari Server.", examples = { "/var/lib/ambari-server/resources/mpacks" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> MPACKS_STAGING_DIR_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("mpacks.staging.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The Ambari Management Pack version-2 staging directory on the Ambari Server.", examples = { "/var/lib/ambari-server/resources/mpacks-v2" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> MPACKS_V2_STAGING_DIR_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("mpacks-v2.staging.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The full path to the file which contains the Ambari Server version. This is used to ensure that there is not a version mismatch between Ambari Agents and Ambari Server.", examples = { "/var/lib/ambari-server/resources/version" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_VERSION_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.version.file", null);

    @org.apache.ambari.annotations.Markdown(description = "Whether user accepted GPL license.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> GPL_LICENSE_ACCEPTED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("gpl.license.accepted", false);

    @org.apache.ambari.annotations.Markdown(description = "The location of the JDK on the Ambari Agent hosts. If stack.java.home exists, that is only used by Ambari Server (or you can find that as ambari_java_home in the commandParams on the agent side)", examples = { "/usr/jdk64/jdk1.8.0_112" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> JAVA_HOME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("java.home", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the JDK installation binary. If stack.jdk.name exists, that is only used by Ambari Server (or you can find that as ambari_jdk_name in the commandParams on the agent side)", examples = { "jdk-8u112-linux-x64.tar.gz" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> JDK_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("jdk.name", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the JCE policy ZIP file. If stack.jce.name exists, that is only used by Ambari Server (or you can find that as ambari_jce_name in the commandParams on the agent side)", examples = { "UnlimitedJCEPolicyJDK8.zip" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> JCE_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("jce.name", null);

    @org.apache.ambari.annotations.Markdown(description = "The location of the JDK on the Ambari Agent hosts for stack services.", examples = { "/usr/jdk64/jdk1.7.0_45" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_JAVA_HOME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.java.home", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the JDK installation binary for stack services.", examples = { "jdk-7u45-linux-x64.tar.gz" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_JDK_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.jdk.name", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the JCE policy ZIP file for stack services.", examples = { "UnlimitedJCEPolicyJDK7.zip" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_JCE_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.jce.name", null);

    @org.apache.ambari.annotations.Markdown(description = "JDK version of the stack, use in case of it differs from Ambari JDK version.", examples = { "1.7" })
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_JAVA_VERSION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.java.version", null);

    @org.apache.ambari.annotations.Markdown(description = "The auto group creation by Ambari")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> AUTO_GROUP_CREATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("auto.group.creation", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The PAM configuration file.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PAM_CONFIGURATION_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("pam.configuration", null);

    @org.apache.ambari.annotations.Markdown(examples = { "local", "ldap", "pam" }, description = "The type of authentication mechanism used by Ambari.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_SECURITY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.security", null);

    @org.apache.ambari.annotations.Markdown(description = "The port that client connections will use with the REST API. The Ambari Web client runs on this port.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.port", "8080");

    @org.apache.ambari.annotations.Markdown(description = "The port that client connections will use with the REST API when using SSL. The Ambari Web client runs on this port if SSL is enabled.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.port", "8443");

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari server where the REST API keystore and password files are stored if using SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_KSTR_DIR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.keys_dir", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the keystore used when the Ambari Server REST API is protected by SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_KSTR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.keystore_name", "https.keystore.p12");

    @org.apache.ambari.annotations.Markdown(description = "The type of the keystore file specified in `client.api.ssl.keystore_name`. Self-signed certificates can be `PKCS12` while CA signed certificates are `JKS`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_KSTR_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.keystore_type", "PKCS12");

    @org.apache.ambari.annotations.Markdown(description = "The name of the truststore used when the Ambari Server REST API is protected by SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_TSTR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.truststore_name", "https.keystore.p12");

    @org.apache.ambari.annotations.Markdown(description = "The type of the keystore file specified in `client.api.ssl.truststore_name`. Self-signed certificates can be `PKCS12` while CA signed certificates are `JKS`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_TSTR_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.truststore_type", "PKCS12");

    @org.apache.ambari.annotations.Markdown(description = "The filename which contains the password for the keystores, truststores, and certificates for the REST API when it's protected by SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_CRT_PASS_FILE_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.cert_pass_file", "https.pass.txt");

    @org.apache.ambari.annotations.Markdown(description = "The password for the keystores, truststores, and certificates for the REST API when it's protected by SSL. If not specified, then `client.api.ssl.cert_pass_file` should be used.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CLIENT_API_SSL_CRT_PASS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.ssl.crt_pass", null);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether the agents will automatically attempt to download updates to stack resources from the Ambari Server.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ENABLE_AUTO_AGENT_CACHE_UPDATE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.auto.cache.update", "true");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether the Ambari Agents will use the `df` or `df -l` command when checking disk mounts for capacity issues. Auto-mounted remote directories can cause long delays.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CHECK_REMOTE_MOUNTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.check.remote.mounts", "false");

    @org.apache.ambari.annotations.Markdown(description = "The timeout, used by the `timeout` command in linux, when checking mounts for free capacity.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CHECK_MOUNTS_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.check.mounts.timeout", "0");

    @org.apache.ambari.annotations.Markdown(description = "The path of the file which lists the properties that should be masked from the api that returns ambari.properties")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PROPERTY_MASK_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("property.mask.file", null);

    @org.apache.ambari.annotations.Markdown(description = "The name of the database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_DB_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.database_name", "ambari");

    @org.apache.ambari.annotations.Markdown(description = "The amount of time, in milliseconds, that a view will wait before terminating an HTTP(S) read request.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REQUEST_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.request.read.timeout.millis", "10000");

    @org.apache.ambari.annotations.Markdown(description = "The amount of time, in milliseconds, that a view will wait when trying to connect on HTTP(S) operations to a remote resource.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REQUEST_CONNECT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.request.connect.timeout.millis", "5000");

    @org.apache.ambari.annotations.Markdown(description = "The amount of time, in milliseconds, that a view will wait before terminating an HTTP(S) read request to the Ambari REST API.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AMBARI_REQUEST_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.ambari.request.read.timeout.millis", "45000");

    @org.apache.ambari.annotations.Markdown(description = "The amount of time, in milliseconds, that a view will wait when trying to connect on HTTP(S) operations to the Ambari REST API.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AMBARI_REQUEST_CONNECT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.ambari.request.connect.timeout.millis", "30000");

    @org.apache.ambari.annotations.Markdown(description = "The schema within a named PostgreSQL database where Ambari's tables, users, and constraints are stored. ")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_POSTGRES_SCHEMA_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.postgres.schema", "");

    @org.apache.ambari.annotations.Markdown(description = "The name of the Oracle JDBC JAR connector.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> OJDBC_JAR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("db.oracle.jdbc.name", "ojdbc6.jar");

    @org.apache.ambari.annotations.Markdown(description = "The name of the MySQL JDBC JAR connector.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> MYSQL_JAR_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("db.mysql.jdbc.name", "mysql-connector-java.jar");

    @org.apache.ambari.annotations.Markdown(description = "Enable the profiling of internal locks.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> SERVER_LOCKS_PROFILING = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.locks.profiling", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The size of the cache which is used to hold current operations in memory until they complete.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> SERVER_EC_CACHE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.ecCacheSize", 10000L);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether an existing request's status is cached. This is enabled by default to prevent increases in database access when there are long running operations in progress.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.hrcStatusSummary.cache.enabled", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.hrcStatusSummary.cache.enabled", description = "The size of the cache which is used to hold a status of every operation in a request.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> SERVER_HRC_STATUS_SUMMARY_CACHE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.hrcStatusSummary.cache.size", 10000L);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.hrcStatusSummary.cache.enabled", description = "The expiration time, in minutes, of the request status cache.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> SERVER_HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.hrcStatusSummary.cache.expiryDuration", 30L);

    @org.apache.ambari.annotations.Markdown(description = "Determines when the stale configuration cache is enabled. If disabled, then queries to determine if components need to be restarted will query the database directly.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> SERVER_STALE_CONFIG_CACHE_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.cache.isStale.enabled", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.cache.isStale.enabled", description = "The expiration time, in {@link TimeUnit#MINUTES}, that stale configuration information is cached.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_STALE_CONFIG_CACHE_EXPIRATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.cache.isStale.expiration", 600);

    @org.apache.ambari.annotations.Markdown(examples = { "local", "remote" }, description = "The type of database connection being used. Unless using an embedded PostgresSQL server, then this should be `remote`.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_PERSISTENCE_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.persistence.type", "local");

    @org.apache.ambari.annotations.Markdown(description = "The user name used to login to the database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_USER_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.user.name", "ambari");

    @org.apache.ambari.annotations.Markdown(description = "The password for the user when logging into the database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_USER_PASSWD = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.user.passwd", "bigdata");

    @org.apache.ambari.annotations.Markdown(description = "The name of the PostgresSQL JDBC JAR connector.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_DRIVER = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.driver", "org.postgresql.Driver");

    @org.apache.ambari.annotations.Markdown(internal = true, description = "The full JDBC url used for in-memory database creation.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_URL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.url", null);

    @org.apache.ambari.annotations.Markdown(description = "The size of the buffer to use, in bytes, for REST API HTTP header requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_HTTP_REQUEST_HEADER_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.http.request.header.size", 64 * 1024);

    @org.apache.ambari.annotations.Markdown(description = "The size of the buffer to use, in bytes, for REST API HTTP header responses.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_HTTP_RESPONSE_HEADER_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.http.response.header.size", 64 * 1024);

    @org.apache.ambari.annotations.Markdown(description = "A comma-separated list of packages which will be skipped during a stack upgrade.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ROLLING_UPGRADE_SKIP_PACKAGES_PREFIXES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("rolling.upgrade.skip.packages.prefixes", "");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether pre-upgrade checks will be skipped when performing a rolling or express stack upgrade.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> STACK_UPGRADE_BYPASS_PRECHECKS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.bypass.prechecks", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The amount of time to wait in order to retry a command during a stack upgrade when an agent loses communication. This value must be greater than the `agent.task.timeout` value.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> STACK_UPGRADE_AUTO_RETRY_TIMEOUT_MINS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.auto.retry.timeout.mins", 0);

    @org.apache.ambari.annotations.Markdown(relatedTo = "stack.upgrade.auto.retry.timeout.mins", description = "The amount of time to wait, in seconds, between checking for upgrade tasks to be retried. This value is only applicable if `stack.upgrade.auto.retry.timeout.mins` is positive.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> STACK_UPGRADE_AUTO_RETRY_CHECK_INTERVAL_SECS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.auto.retry.check.interval.secs", 20);

    @org.apache.ambari.annotations.Markdown(description = "A comma-separate list of upgrade tasks names to skip when retrying failed commands automatically.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_UPGRADE_AUTO_RETRY_CUSTOM_COMMAND_NAMES_TO_IGNORE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.auto.retry.command.names.to.ignore", "\"ComponentVersionCheckAction\",\"FinalizeUpgradeAction\"");

    @org.apache.ambari.annotations.Markdown(description = "A comma-separate list of upgrade tasks details to skip when retrying failed commands automatically.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> STACK_UPGRADE_AUTO_RETRY_COMMAND_DETAILS_TO_IGNORE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.auto.retry.command.details.to.ignore", "\"Execute HDFS Finalize\"");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether to use Kerberos (SPNEGO) authentication when connecting Ambari.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> KERBEROS_AUTH_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.kerberos.enabled", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The Kerberos principal name to use when verifying user-supplied Kerberos tokens for authentication via SPNEGO")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KERBEROS_AUTH_SPNEGO_PRINCIPAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.kerberos.spnego.principal", "HTTP/_HOST");

    @org.apache.ambari.annotations.Markdown(description = "The Kerberos keytab file to use when verifying user-supplied Kerberos tokens for authentication via SPNEGO")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KERBEROS_AUTH_SPNEGO_KEYTAB_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.kerberos.spnego.keytab.file", "/etc/security/keytabs/spnego.service.keytab");

    @org.apache.ambari.annotations.Markdown(description = "The auth-to-local rules set to use when translating a user's principal name to a local user name during authentication via SPNEGO.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KERBEROS_AUTH_AUTH_TO_LOCAL_RULES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.kerberos.auth_to_local.rules", "DEFAULT");

    @org.apache.ambari.annotations.Markdown(description = "The number of times failed Kerberos operations should be retried to execute.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> KERBEROS_OPERATION_RETRIES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kerberos.operation.retries", 3);

    @org.apache.ambari.annotations.Markdown(description = "The time to wait (in seconds) between failed Kerberos operations retries.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> KERBEROS_OPERATION_RETRY_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kerberos.operation.retry.timeout", 10);

    @org.apache.ambari.annotations.Markdown(description = "Validate the trust of the SSL certificate provided by the KDC when performing Kerberos operations over SSL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> KERBEROS_OPERATION_VERIFY_KDC_TRUST = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kerberos.operation.verify.kdc.trust", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(examples = { "internal", "c3p0" }, description = "The connection pool manager to use for database connections. If using MySQL, then `c3p0` is automatically chosen.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_CONNECTION_POOL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool", org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.INTERNAL.getName());

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The minimum number of connections that should always exist in the database connection pool. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_MIN_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.min-size", 5);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The maximum number of connections that should exist in the database connection pool. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.max-size", 32);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The number of connections that should be retrieved when the pool size must increase. " + "This should be set higher than 1 since the assumption is that a pool that needs to grow should probably grow by more than 1. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_AQUISITION_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.acquisition-size", 5);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = " The maximum amount of time, in seconds, any connection, whether its been idle or active, should remain in the pool. " + "This will terminate the connection after the expiration age and force new connections to be opened. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_MAX_AGE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.max-age", 0);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The maximum amount of time, in seconds, that an idle connection can remain in the pool. " + "This should always be greater than the value returned from `server.jdbc.connection-pool.max-idle-time-excess`. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.max-idle-time", 14400);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The maximum amount of time, in seconds, that connections beyond the minimum pool size should remain in the pool. " + "This should always be less than than the value returned from `server.jdbc.connection-pool.max-idle-time`. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME_EXCESS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.max-idle-time-excess", 0);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The number of seconds in between testing each idle connection in the connection pool for validity. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_IDLE_TEST_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.idle-test-interval", 7200);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The number of times connections should be retried to be acquired from the database before giving up. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_ATTEMPTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.acquisition-retry-attempts", 30);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.jdbc.connection-pool", description = "The delay, in milliseconds, between connection acquisition attempts. Only used with c3p0.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_DELAY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.connection-pool.acquisition-retry-delay", 1000);

    @org.apache.ambari.annotations.Markdown(description = "The number of retry attempts for failed API and blueprint operations.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> OPERATIONS_RETRY_ATTEMPTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.operations.retry-attempts", 0);

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Markdown(description = "The user name for connecting to the database which stores RCA information.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_RCA_USER_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.rca.user.name", "mapred");

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Markdown(description = "The password for the user when connecting to the database which stores RCA information.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_RCA_USER_PASSWD = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.rca.user.passwd", "mapred");

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Markdown(description = "The PostgresSQL driver name for the RCA database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_RCA_DRIVER = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.rca.driver", "org.postgresql.Driver");

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Markdown(description = "The full JDBC URL for connecting to the RCA database.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_JDBC_RCA_URL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.rca.url", ("jdbc:postgresql://" + org.apache.ambari.server.configuration.Configuration.HOSTNAME_MACRO) + "/ambarirca");

    @org.apache.ambari.annotations.Markdown(description = "The table generation strategy to use when initializing JPA.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<org.apache.ambari.server.orm.JPATableGenerationStrategy> SERVER_JDBC_GENERATE_TABLES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.jdbc.generateTables", org.apache.ambari.server.orm.JPATableGenerationStrategy.NONE);

    @org.apache.ambari.annotations.Markdown(examples = { "redhat", "ubuntu" }, description = "The operating system family for all hosts in the cluster. This is used when bootstrapping agents and when enabling Kerberos.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> OS_FAMILY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.os_family", "");

    @org.apache.ambari.annotations.Markdown(examples = { "6", "7" }, description = "The operating system version for all hosts in the cluster. This is used when bootstrapping agents and when enabling Kerberos.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> OS_VERSION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.os_type", "");

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server of the file which is used for mapping host names.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SRVR_HOSTS_MAPPING = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.hosts.mapping", null);

    @org.apache.ambari.annotations.Markdown(description = "The location of the truststore to use when setting the `javax.net.ssl.trustStore` property.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SSL_TRUSTSTORE_PATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ssl.trustStore.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The password to use when setting the `javax.net.ssl.trustStorePassword` property")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SSL_TRUSTSTORE_PASSWORD = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ssl.trustStore.password", null);

    @org.apache.ambari.annotations.Markdown(description = "The type of truststore used by the `javax.net.ssl.trustStoreType` property.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SSL_TRUSTSTORE_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ssl.trustStore.type", null);

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server of the master key file. This is the key to the master keystore.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> MASTER_KEY_LOCATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.master.key.location", null);

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server of the master keystore file.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> MASTER_KEYSTORE_LOCATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.master.keystore.location", null);

    @org.apache.ambari.annotations.Markdown(description = "The time, in minutes, that the temporary, in-memory credential store retains values.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> TEMPORARYSTORE_RETENTION_MINUTES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.temporary.keystore.retention.minutes", 90L);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether the temporary keystore should have keys actively purged on a fixed internal. or only when requested after expiration.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> TEMPORARYSTORE_ACTIVELY_PURGE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.temporary.keystore.actibely.purge", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(examples = { "http://ambari.apache.org:8080" }, description = "The URL to use when creating messages which should include the Ambari Server URL.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> AMBARI_DISPLAY_URL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ambari.display.url", null);

    @org.apache.ambari.annotations.Markdown(description = "The suffixes to use when validating Ubuntu repositories.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REPO_SUFFIX_KEY_UBUNTU = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("repo.validation.suffixes.ubuntu", "/dists/%s/Release");

    @org.apache.ambari.annotations.Markdown(description = "The suffixes to use when validating most types of repositories.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REPO_SUFFIX_KEY_DEFAULT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("repo.validation.suffixes.default", "/repodata/repomd.xml");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether Quartz will use a clustered job scheduled when performing scheduled actions like rolling restarts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> EXECUTION_SCHEDULER_CLUSTERED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.isClustered", "false");

    @org.apache.ambari.annotations.Markdown(description = "The number of threads that the Quartz job scheduler will use when executing scheduled jobs.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> EXECUTION_SCHEDULER_THREADS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.maxThreads", "5");

    @org.apache.ambari.annotations.Markdown(description = "The number of concurrent database connections that the Quartz job scheduler can use.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> EXECUTION_SCHEDULER_CONNECTIONS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.maxDbConnections", "5");

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of prepared statements cached per database connection.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> EXECUTION_SCHEDULER_MAX_STATEMENTS_PER_CONNECTION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.maxStatementsPerConnection", "120");

    @org.apache.ambari.annotations.Markdown(description = "The time, in minutes, that a scheduled job can be run after its missed scheduled execution time.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> EXECUTION_SCHEDULER_MISFIRE_TOLERATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.misfire.toleration.minutes", 480L);

    @org.apache.ambari.annotations.Markdown(description = "The delay, in seconds, that a Quartz job must wait before it starts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> EXECUTION_SCHEDULER_START_DELAY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.start.delay.seconds", 120);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, that the Quartz execution scheduler will wait before checking for new commands to schedule, such as rolling restarts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> EXECUTION_SCHEDULER_WAIT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.execution.scheduler.wait", 1L);

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where temporary artifacts can be created.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_TMP_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.tmp.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/tmp"));

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where request logs can be created.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REQUEST_LOGPATH = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.requestlogs.path", null);

    @org.apache.ambari.annotations.Markdown(description = "The pattern of request log file name")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> REQUEST_LOGNAMEPATTERN = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.requestlogs.namepattern", "ambari-access-yyyy_mm_dd.log");

    @org.apache.ambari.annotations.Markdown(description = "The number of days that request log would be retained.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> REQUEST_LOG_RETAINDAYS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.requestlogs.retaindays", 15);

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, until an external script is killed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> EXTERNAL_SCRIPT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.script.timeout", 10000);

    @org.apache.ambari.annotations.Markdown(description = "The number of threads that should be allocated to run external script.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.script.threads", 20);

    public static final java.lang.String DEF_ARCHIVE_EXTENSION;

    public static final java.lang.String DEF_ARCHIVE_CONTENT_TYPE;

    @org.apache.ambari.annotations.Markdown(description = "The port used to communicate with the Kerberos Key Distribution Center.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KDC_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("default.kdcserver.port", "88");

    @org.apache.ambari.annotations.Markdown(description = "The timeout, in milliseconds, to wait when communicating with a Kerberos Key Distribution Center.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> KDC_CONNECTION_CHECK_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kdcserver.connection.check.timeout", 10000);

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where Kerberos keytabs are cached.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> KERBEROSTAB_CACHE_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kerberos.keytab.cache.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/data/cache"));

    @org.apache.ambari.annotations.Markdown(description = "Determines whether Kerberos-enabled Ambari deployments should use JAAS to validate login credentials.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> KERBEROS_CHECK_JAAS_CONFIGURATION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("kerberos.check.jaas.configuration", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(examples = { "DEFAULT", "AUTO_START", "FULL" }, description = "The type of automatic recovery of failed services and components to use.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.type", null);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of recovery attempts of a failed component during the lifetime of an Ambari Agent instance. " + "This is reset when the Ambari Agent is restarted.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_LIFETIME_MAX_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.lifetime_max_count", null);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of recovery attempts of a failed component during a specified recovery window.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_MAX_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.max_count", null);

    @org.apache.ambari.annotations.Markdown(relatedTo = "recovery.max_count", description = "The length of a recovery window, in minutes, in which recovery attempts can be retried.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_WINDOW_IN_MIN = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.window_in_minutes", null);

    @org.apache.ambari.annotations.Markdown(description = "The delay, in minutes, between automatic retry windows.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_RETRY_GAP = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.retry_interval", null);

    @org.apache.ambari.annotations.Markdown(examples = { "NAMENODE,ZOOKEEPER_SERVER" }, description = "A comma-separated list of component names which are not included in automatic recovery attempts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_DISABLED_COMPONENTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.disabled_components", null);

    @org.apache.ambari.annotations.Markdown(examples = { "NAMENODE,ZOOKEEPER_SERVER" }, description = "A comma-separated list of component names which are included in automatic recovery attempts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> RECOVERY_ENABLED_COMPONENTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("recovery.enabled_components", null);

    @org.apache.ambari.annotations.Markdown(description = "A comma-separated whitelist of host and port values which Ambari Server can use to determine if a proxy value is valid.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> PROXY_ALLOWED_HOST_PORTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("proxy.allowed.hostports", "*:*");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether operations in different execution requests can be run concurrently.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> PARALLEL_STAGE_EXECUTION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.stages.parallel", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(description = "Drives view extraction in case of blueprint deployments; non-system views are deployed when cluster configuration is successful")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> VIEW_EXTRACT_AFTER_CLUSTER_CONFIG = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.extract-after-cluster-config", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "How to execute commands in one stage")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> COMMAND_EXECUTION_TYPE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.stage.command.execution_type", org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE.toString());

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, before agent commands are killed. This does not include package installation commands.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> AGENT_TASK_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.task.timeout", 900L);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, before agent service check commands are killed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> AGENT_SERVICE_CHECK_TASK_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.service.check.task.timeout", 0L);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, before package installation commands are killed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> AGENT_PACKAGE_INSTALL_TASK_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.package.install.task.timeout", 1800L);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of tasks which can run within a single operational request. If there are more tasks, then they will be broken up between multiple operations.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.package.parallel.commands.limit", 100);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, before a server-side operation is terminated.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_TASK_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.task.timeout", 1200);

    @org.apache.ambari.annotations.Markdown(description = "A location of hooks folder relative to resources folder.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HOOKS_FOLDER = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.hooks.folder", "stack-hooks");

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where custom actions are defined.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> CUSTOM_ACTION_DEFINITION = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("custom.action.definitions", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/resources/custom_action_definitions"));

    @org.apache.ambari.annotations.Markdown(description = "The location on the Ambari Server where resources are stored. This is exposed via HTTP in order for Ambari Agents to access them.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SHARED_RESOURCES_DIR = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("shared.resources.dir", org.apache.ambari.server.utils.AmbariPath.getPath("/usr/lib/ambari-server/lib/ambari_commons/resources"));

    @org.apache.ambari.annotations.Markdown(description = "The name of the user given to requests which are executed without any credentials.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ANONYMOUS_AUDIT_NAME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("anonymous.audit.name", "_anonymous");

    @org.apache.ambari.annotations.Markdown(description = "Determines whether Ambari Agent instances have already have the necessary stack software installed")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SYS_PREPPED_HOSTS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("packages.pre.installed", "false");

    @org.apache.ambari.annotations.Markdown(description = "This property is used in specific testing circumstances only. Its use otherwise will lead to very unpredictable results with repository management and package installation")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> LEGACY_OVERRIDE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("repositories.legacy-override.enabled", "false");

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that Ambari Agent connections can remain open and idle.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_CONNECTION_MAX_IDLE_TIME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.connection.max.idle.millis", 900000);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.JETTY_THREAD_POOL, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "25"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "35"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "50"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "65") }, markdown = @org.apache.ambari.annotations.Markdown(description = "The size of the Jetty connection pool used for handling incoming REST API requests. This should be large enough to handle requests from both web browsers and embedded Views."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> CLIENT_THREADPOOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.threadpool.size.max", 25);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.JETTY_THREAD_POOL, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "25"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "35"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "75"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "100") }, markdown = @org.apache.ambari.annotations.Markdown(description = "The size of the Jetty connection pool used for handling incoming Ambari Agent requests."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENT_THREADPOOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.threadpool.size.max", 25);

    @org.apache.ambari.annotations.Markdown(description = "Thread pool size for spring messaging")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> MESSAGING_THREAD_POOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("messaging.threadpool.size", 10);

    @org.apache.ambari.annotations.Markdown(description = "Thread pool size for agents registration")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> REGISTRATION_THREAD_POOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("registration.threadpool.size", 10);

    @org.apache.ambari.annotations.Markdown(description = "Maximal cache size for spring subscription registry.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SUBSCRIPTION_REGISTRY_CACHE_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("subscription.registry.cache.size", 1500);

    @org.apache.ambari.annotations.Markdown(description = "Queue size for agents in registration.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENTS_REGISTRATION_QUEUE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agents.registration.queue.size", 200);

    @org.apache.ambari.annotations.Markdown(description = "Period in seconds with agents reports will be processed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENTS_REPORT_PROCESSING_PERIOD = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agents.reports.processing.period", 1);

    @org.apache.ambari.annotations.Markdown(description = "Timeout in seconds before start processing of agents' reports.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENTS_REPORT_PROCESSING_START_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agents.reports.processing.start.timeout", 5);

    @org.apache.ambari.annotations.Markdown(description = "Thread pool size for agents reports processing.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENTS_REPORT_THREAD_POOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agents.reports.thread.pool.size", 10);

    @org.apache.ambari.annotations.Markdown(description = "Server to API STOMP endpoint heartbeat interval in milliseconds.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> API_HEARTBEAT_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("api.heartbeat.interval", 10000);

    @org.apache.ambari.annotations.Markdown(description = "The maximum size of an incoming stomp text message. Default is 2 MB.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> STOMP_MAX_INCOMING_MESSAGE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stomp.max_incoming.message.size", (2 * 1024) * 1024);

    @org.apache.ambari.annotations.Markdown(description = "The maximum size of a buffer for stomp message sending. Default is 5 MB.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> STOMP_MAX_BUFFER_MESSAGE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stomp.max_buffer.message.size", (5 * 1024) * 1024);

    @org.apache.ambari.annotations.Markdown(description = "The number of attempts to emit execution command message to agent. Default is 4")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> EXECUTION_COMMANDS_RETRY_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("execution.command.retry.count", 4);

    @org.apache.ambari.annotations.Markdown(description = "The interval in seconds between attempts to emit execution command message to agent. Default is 15")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> EXECUTION_COMMANDS_RETRY_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("execution.command.retry.interval", 15);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of threads used to extract Ambari Views when Ambari Server is starting up.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VIEW_EXTRACTION_THREADPOOL_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.extraction.threadpool.size.max", 20);

    @org.apache.ambari.annotations.Markdown(description = "The number of threads used to extract Ambari Views when Ambari Server is starting up.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VIEW_EXTRACTION_THREADPOOL_CORE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.extraction.threadpool.size.core", 10);

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that non-core threads will live when extraction views on Ambari Server startup.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> VIEW_EXTRACTION_THREADPOOL_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.extraction.threadpool.timeout", 100000L);

    @org.apache.ambari.annotations.Markdown(relatedTo = "agent.threadpool.size.max", description = "The maximum number of threads which will be allocated to handling REST API requests from embedded views. This value should be smaller than `agent.threadpool.size.max`")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VIEW_REQUEST_THREADPOOL_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.request.threadpool.size.max", 0);

    @org.apache.ambari.annotations.Markdown(description = "The time, milliseconds, that REST API requests from embedded views can wait if there are no threads available to service the view's request. " + "Setting this too low can cause views to timeout.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VIEW_REQUEST_THREADPOOL_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("view.request.threadpool.timeout", 2000);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of threads that will be used to retrieve data from federated datasources, such as remote JMX endpoints.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> PROPERTY_PROVIDER_THREADPOOL_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.property-provider.threadpool.size.max", org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_MAX_SIZE_DEFAULT);

    @org.apache.ambari.annotations.Markdown(description = "The core number of threads that will be used to retrieve data from federated datasources, such as remote JMX endpoints.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> PROPERTY_PROVIDER_THREADPOOL_CORE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.property-provider.threadpool.size.core", org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_CORE_SIZE_DEFAULT);

    @org.apache.ambari.annotations.Markdown(description = "The maximum size of pending federated datasource requests, such as those to JMX endpoints, which can be queued before rejecting new requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> PROPERTY_PROVIDER_THREADPOOL_WORKER_QUEUE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.property-provider.threadpool.worker.size", java.lang.Integer.MAX_VALUE);

    @org.apache.ambari.annotations.Markdown(description = "The maximum time, in milliseconds, that federated requests for data can execute before being terminated. " + "Increasing this value could result in degraded performanc from the REST APIs.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> PROPERTY_PROVIDER_THREADPOOL_COMPLETION_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.property-provider.threadpool.completion.timeout", 5000L);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, that open HTTP sessions will remain valid while they are inactive.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_HTTP_SESSION_INACTIVE_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.http.session.inactive_timeout", 1800);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether Ambari Metric data is cached.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> TIMELINE_METRICS_CACHE_DISABLE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.disabled", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in seconds, that Ambari Metric timeline data is cached by Ambari Server.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_CACHE_TTL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.entry.ttl.seconds", 3600);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in seconds, that Ambari Metric data can remain in the cache without being accessed.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_CACHE_IDLE_TIME = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.entry.idle.seconds", 1800);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "cache size, in entries, that ambari metrics cache will hold.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_CACHE_ENTRY_UNIT_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.entry.entry.unit.size", 100);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in milliseconds, that initial requests to populate metric data will wait while reading from Ambari Metrics.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_REQUEST_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.read.timeout.millis", 10000);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in milliseconds, that requests to update stale metric data will wait while reading from Ambari Metrics. " + "This allows for greater control by allowing stale values to be returned instead of waiting for Ambari Metrics to always populate responses with the latest data.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_REQUEST_INTERVAL_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.interval.read.timeout.millis", 10000);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in milliseconds, to wait while attempting to connect to Ambari Metrics.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TIMELINE_METRICS_REQUEST_CONNECT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.connect.timeout.millis", 5000);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The time, in milliseconds, that Ambari Metrics intervals should use when extending the boundaries of the original request.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> TIMELINE_METRICS_REQUEST_CATCHUP_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.catchup.interval", 300000L);

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "The amount of heap on the Ambari Server dedicated to the caching values from Ambari Metrics. Measured as part of the total heap of Ambari Server.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> TIMELINE_METRICS_CACHE_HEAP_PERCENT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.heap.percent", "15%");

    @org.apache.ambari.annotations.Markdown(relatedTo = "server.timeline.metrics.cache.disabled", description = "Determines if a custom engine should be used to increase performance of calculating the current size of the cache for Ambari Metric data.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> TIMELINE_METRICS_CACHE_USE_CUSTOM_SIZING_ENGINE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.cache.use.custom.sizing.engine", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether to use to SSL to connect to Ambari Metrics when retrieving metric data.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> AMBARI_METRICS_HTTPS_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.timeline.metrics.https.enabled", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The full path to the XML file that describes the different alert templates.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ALERT_TEMPLATE_FILE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.template.file", null);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "2"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "2"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "4"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "4") }, markdown = @org.apache.ambari.annotations.Markdown(description = "The core number of threads used to process incoming alert events. The value should be increased as the size of the cluster increases."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> ALERTS_EXECUTION_SCHEDULER_THREADS_CORE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.execution.scheduler.threadpool.size.core", 2);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "2"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "2"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "8"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "8") }, markdown = @org.apache.ambari.annotations.Markdown(description = "The number of threads used to handle alerts received from the Ambari Agents. The value should be increased as the size of the cluster increases."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> ALERTS_EXECUTION_SCHEDULER_THREADS_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.execution.scheduler.threadpool.size.max", 2);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "400"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "2000"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "4000"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "20000") }, markdown = @org.apache.ambari.annotations.Markdown(description = "The number of queued alerts allowed before discarding old alerts which have not been handled. The value should be increased as the size of the cluster increases."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> ALERTS_EXECUTION_SCHEDULER_WORKER_QUEUE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.execution.scheduler.threadpool.worker.size", 2000);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "false"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "false"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "false"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "true") }, markdown = @org.apache.ambari.annotations.Markdown(description = "Determines whether current alerts should be cached. " + "Enabling this can increase performance on large cluster, but can also result in lost alert data if the cache is not flushed frequently."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> ALERTS_CACHE_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.cache.enabled", java.lang.Boolean.FALSE);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "10"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "10"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "10"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "10") }, markdown = @org.apache.ambari.annotations.Markdown(relatedTo = "alerts.cache.enabled", description = "The time, in minutes, after which cached alert information is flushed to the database"))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> ALERTS_CACHE_FLUSH_INTERVAL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.cache.flush.interval", 10);

    @org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown(group = org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.ALERTS, scaleValues = { @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_10, value = "50000"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_50, value = "50000"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_100, value = "100000"), @org.apache.ambari.server.configuration.Configuration.ClusterScale(clusterSize = org.apache.ambari.server.configuration.Configuration.ClusterSizeType.HOSTS_500, value = "100000") }, markdown = @org.apache.ambari.annotations.Markdown(relatedTo = "alerts.cache.enabled", description = "The size of the alert cache."))
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> ALERTS_CACHE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.cache.size", 50000);

    @org.apache.ambari.annotations.Markdown(description = "When using SSL, this will be used to set the `Strict-Transport-Security` response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_STRICT_TRANSPORT_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.strict-transport-security", "max-age=31536000");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-Frame-Options` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_X_FRAME_OPTIONS_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.x-frame-options", "DENY");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-XSS-Protection` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_X_XSS_PROTECTION_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.x-xss-protection", "1; mode=block");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `Content-Security-Policy` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.content-security-policy", "");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-CONTENT-TYPE` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_X_CONTENT_TYPE_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.x-content-type-options", "nosniff");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `Cache-Control` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_CACHE_CONTROL_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.cache-control", "no-store");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `PRAGMA` HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_PRAGMA_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.pragma", "no-cache");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the Character encoding to HTTP response header.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> HTTP_CHARSET = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("http.charset", "utf-8");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `Strict-Transport-Security` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_STRICT_TRANSPORT_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.strict-transport-security", "max-age=31536000");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-Frame-Options` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_X_FRAME_OPTIONS_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.x-frame-options", "SAMEORIGIN");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-XSS-Protection` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_X_XSS_PROTECTION_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.x-xss-protection", "1; mode=block");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `Content-Security-Policy` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.content-security-policy", "");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `X-CONTENT-TYPE` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_X_CONTENT_TYPE_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.x-content-type-options", "nosniff");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `Cache-Control` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_CACHE_CONTROL_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.cache-control", "no-store");

    @org.apache.ambari.annotations.Markdown(description = "Additional class path added to each Ambari View. Comma separated jars or directories")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_ADDITIONAL_CLASSPATH_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.additional.classpath", "");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the `PRAGMA` HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_PRAGMA_HEADER_VALUE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.pragma", "no-cache");

    @org.apache.ambari.annotations.Markdown(description = "The value that will be used to set the Character encoding to HTTP response header for Ambari View requests.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> VIEWS_HTTP_CHARSET = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("views.http.charset", "utf-8");

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that requests to connect to a URL to retrieve Version Definition Files (VDF) will wait before being terminated.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VERSION_DEFINITION_CONNECT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.version_definition.connect.timeout.millis", 5000);

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that requests to read from a connected URL to retrieve Version Definition Files (VDF) will wait before being terminated.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> VERSION_DEFINITION_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.version_definition.read.timeout.millis", 5000);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether agents should retrying installation commands when the repository is not available. " + "This can prevent false installation errors with repositories that are sporadically inaccessible.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> AGENT_STACK_RETRY_ON_REPO_UNAVAILABILITY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.stack.retry.on_repo_unavailability", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "agent.stack.retry.on_repo_unavailability", description = "The number of times an Ambari Agent should retry package installation when it fails due to a repository error. ")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENT_STACK_RETRY_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.stack.retry.tries", 5);

    @org.apache.ambari.annotations.Markdown(description = "Determines whether audit logging is enabled.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> AUDIT_LOG_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("auditlog.enabled", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "auditlog.enabled", description = "The size of the worker queue for audit logger events.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AUDIT_LOGGER_CAPACITY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("auditlog.logger.capacity", 10000);

    @org.apache.ambari.annotations.Markdown(description = "The UDP port to use when binding the SNMP dispatcher on Ambari Server startup. If no port is specified, then a random port will be used.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ALERTS_SNMP_DISPATCH_UDP_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.snmp.dispatcher.udp.port", null);

    @org.apache.ambari.annotations.Markdown(description = "The UDP port to use when binding the Ambari SNMP dispatcher on Ambari Server startup. If no port is specified, then a random port will be used.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ALERTS_AMBARI_SNMP_DISPATCH_UDP_PORT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.ambari.snmp.dispatcher.udp.port", null);

    @org.apache.ambari.annotations.Markdown(description = "The amount of time, in minutes, that JMX and REST metrics retrieved directly can remain in the cache.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_CACHE_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("metrics.retrieval-service.cache.timeout", 30);

    @org.apache.ambari.annotations.Markdown(description = "The priority of threads used by the service which retrieves JMX and REST metrics directly from their respective endpoints.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_THREAD_PRIORITY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.metrics.retrieval-service.thread.priority", java.lang.Thread.NORM_PRIORITY);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of threads used to retrieve JMX and REST metrics directly from their respective endpoints.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_THREADPOOL_MAX_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.metrics.retrieval-service.threadpool.size.max", org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_MAX_SIZE_DEFAULT);

    @org.apache.ambari.annotations.Markdown(description = "The core number of threads used to retrieve JMX and REST metrics directly from their respective endpoints.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_THREADPOOL_CORE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.metrics.retrieval-service.threadpool.size.core", org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_CORE_SIZE_DEFAULT);

    @org.apache.ambari.annotations.Markdown(description = "The number of queued requests allowed for JMX and REST metrics before discarding old requests which have not been fullfilled.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_THREADPOOL_WORKER_QUEUE_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.metrics.retrieval-service.threadpool.worker.size", 10 * org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_THREADPOOL_MAX_SIZE.getDefaultValue());

    @org.apache.ambari.annotations.Markdown(relatedTo = "metrics.retrieval-service.request.ttl", description = "Enables throttling requests to the same endpoint within a fixed amount of time. " + "This property will prevent Ambari from making new metric requests to update the cache for URLs which have been recently retrieved.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> METRIC_RETRIEVAL_SERVICE_REQUEST_TTL_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("metrics.retrieval-service.request.ttl.enabled", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(relatedTo = "metrics.retrieval-service.request.ttl.enabled", description = "The number of seconds to wait between issuing JMX or REST metric requests to the same endpoint. " + "This property is used to throttle requests to the same URL being made too close together")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> METRIC_RETRIEVAL_SERVICE_REQUEST_TTL = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("metrics.retrieval-service.request.ttl", 5);

    @org.apache.ambari.annotations.Markdown(description = "Indicates whether the current ambari server instance is active or not.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> ACTIVE_INSTANCE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("active.instance", java.lang.Boolean.TRUE);

    @org.apache.ambari.annotations.Markdown(description = "Indicates whether the post user creation is enabled or not. By default is false.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> POST_USER_CREATION_HOOK_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ambari.post.user.creation.hook.enabled", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The location of the post user creation hook on the ambari server hosting machine.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> POST_USER_CREATION_HOOK = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ambari.post.user.creation.hook", "/var/lib/ambari-server/resources/scripts/post-user-creation-hook.sh");

    @org.apache.ambari.annotations.Markdown(description = "Indicates the delay, in milliseconds, for the log4j monitor to check for changes")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> LOG4JMONITOR_DELAY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("log4j.monitor.delay", java.util.concurrent.TimeUnit.MINUTES.toMillis(5));

    @org.apache.ambari.annotations.Markdown(description = "Indicates whether parallel topology task creation is enabled")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> TOPOLOGY_TASK_PARALLEL_CREATION_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("topology.task.creation.parallel", java.lang.Boolean.FALSE);

    @org.apache.ambari.annotations.Markdown(description = "The number of threads to use for parallel topology task creation if enabled")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TOPOLOGY_TASK_PARALLEL_CREATION_THREAD_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("topology.task.creation.parallel.threads", 10);

    @org.apache.ambari.annotations.Markdown(description = "Count of acceptors to configure for the jetty connector used for Ambari agent.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SRVR_AGENT_ACCEPTOR_THREAD_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("agent.api.acceptor.count", null);

    @org.apache.ambari.annotations.Markdown(description = "Count of acceptors to configure for the jetty connector used for Ambari API.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SRVR_API_ACCEPTOR_THREAD_COUNT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("client.api.acceptor.count", null);

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that the Ambari Server will wait while attempting to connect to the LogSearch Portal service.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> LOGSEARCH_PORTAL_CONNECT_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("logsearch.portal.connect.timeout", 5000);

    @org.apache.ambari.annotations.Markdown(description = "The time, in milliseconds, that the Ambari Server will wait while attempting to read a response from the LogSearch Portal service.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> LOGSEARCH_PORTAL_READ_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("logsearch.portal.read.timeout", 5000);

    @org.apache.ambari.annotations.Markdown(description = "Address of an external LogSearch Portal service. (managed outside of Ambari) Using Ambari Credential store is required for this feature (credential: 'logsearch.admin.credential')")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> LOGSEARCH_PORTAL_EXTERNAL_ADDRESS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("logsearch.portal.external.address", "");

    @org.apache.ambari.annotations.Markdown(description = "Global disable flag for AmbariServer Metrics.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> AMBARISERVER_METRICS_DISABLE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("ambariserver.metrics.disable", false);

    @org.apache.ambari.annotations.Markdown(description = "The time, in hours, that the Ambari Server will hold Log File metadata in its internal cache before making a request to the LogSearch Portal to get the latest metadata.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> LOGSEARCH_METADATA_CACHE_EXPIRE_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("logsearch.metadata.cache.expire.timeout", 24);

    @org.apache.ambari.annotations.Markdown(description = "The time, in seconds, that the ambari-server Python script will wait for Jetty to startup before returning an error code.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_STARTUP_WEB_TIMEOUT = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.startup.web.timeout", 50);

    @org.apache.ambari.annotations.Markdown(description = "The Ephemeral TLS Diffie-Hellman (DH) key size. Supported from Java 8.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> TLS_EPHEMERAL_DH_KEY_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.tls.ephemeral_dh_key_size", 2048);

    @org.apache.ambari.annotations.Markdown(description = "The directory for scripts which are used by the alert notification dispatcher.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> DISPATCH_PROPERTY_SCRIPT_DIRECTORY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("notification.dispatch.alert.script.directory", org.apache.ambari.server.utils.AmbariPath.getPath("/var/lib/ambari-server/resources/scripts"));

    @org.apache.ambari.annotations.Markdown(description = "Whether security password encryption is enabled or not. In case it is we store passwords in their own file(s); otherwise we store passwords in the Ambari credential store.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> SECURITY_PASSWORD_ENCRYPTON_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.passwords.encryption.enabled", false);

    @org.apache.ambari.annotations.Markdown(description = "Whether to encrypt sensitive data (at rest) on service level configuration.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> SECURITY_SENSITIVE_DATA_ENCRYPTON_ENABLED = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("security.server.encrypt_sensitive_data", false);

    @org.apache.ambari.annotations.Markdown(description = "The maximum number of authentication attempts permitted to a local user. Once the number of failures reaches this limit the user will be locked out. 0 indicates unlimited failures.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> MAX_LOCAL_AUTHENTICATION_FAILURES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.local.max.failures", 0);

    @org.apache.ambari.annotations.Markdown(description = "Show or hide whether the user account is disabled or locked out, if relevant, when an authentication attempt fails.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SHOW_LOCKED_OUT_USER_MESSAGE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("authentication.local.show.locked.account.messages", "false");

    @org.apache.ambari.annotations.Markdown(description = "The core pool size of the executor service that runs server side alerts.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> SERVER_SIDE_ALERTS_CORE_POOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("alerts.server.side.scheduler.threadpool.size.core", 4);

    @org.apache.ambari.annotations.Markdown(description = "Default value of max number of tasks to schedule in parallel for upgrades. Upgrade packs can override this value.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> DEFAULT_MAX_DEGREE_OF_PARALLELISM_FOR_UPGRADES = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("stack.upgrade.default.parallelism", 100);

    @org.apache.ambari.annotations.Markdown(description = "The timeout, in seconds, when finalizing Kerberos enable/disable/regenerate commands.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> KERBEROS_SERVER_ACTION_FINALIZE_SECONDS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.kerberos.finalize.timeout", 600);

    @org.apache.ambari.annotations.Markdown(description = "The number of threads to use when executing server-side Kerberos commands, such as generate keytabs.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> KERBEROS_SERVER_ACTION_THREADPOOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.kerberos.action.threadpool.size", 1);

    @org.apache.ambari.annotations.Markdown(description = "The Agent command publisher pool. Affects degree of parallelization for generating the commands.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> AGENT_COMMAND_PUBLISHER_THREADPOOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.pools.agent.command.publisher.size", 5);

    @org.apache.ambari.annotations.Markdown(description = "Configures size of the default JOIN Fork pool used for Streams.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Integer> DEFAULT_FORK_JOIN_THREADPOOL_SIZE = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.pools.default.size", 5);

    @org.apache.ambari.annotations.Markdown(description = "Show or hide the error stacks on the error page")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> SERVER_SHOW_ERROR_STACKS = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.show.error.stacks", "false");

    @org.apache.ambari.annotations.Markdown(description = "Fully qualified class name of the strategy used to form host groups for add service request layout recommendation.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> ADD_SERVICE_HOST_GROUP_STRATEGY = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("addservice.hostgroup.strategy", org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy.class.getName());

    @org.apache.ambari.annotations.Markdown(description = "Controls whether VDF can be read from the filesystem.")
    public static final org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Boolean> VDF_FROM_FILESYSTEM = new org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<>("server.version_definition.allow_from_filesystem", java.lang.Boolean.FALSE);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.Configuration.class);

    private java.util.Properties properties;

    private java.util.Properties log4jProperties = new java.util.Properties();

    private java.util.Set<java.lang.String> propertiesToMask = null;

    private java.lang.String ambariUpgradeConfigUpdatesFilePath;

    private com.google.gson.JsonObject hostChangesJson;

    private java.util.Map<java.lang.String, java.lang.String> configsMap;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> agentConfigsMap;

    private java.util.Properties customDbProperties = null;

    private java.util.Properties customPersistenceProperties = null;

    private java.lang.Long configLastModifiedDateForCustomJDBC = 0L;

    private java.lang.Long configLastModifiedDateForCustomJDBCToRemove = 0L;

    private java.util.Map<java.lang.String, java.lang.String> databaseConnectorNames = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> databasePreviousConnectorNames = new java.util.HashMap<>();

    private final org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties;

    static {
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            DEF_ARCHIVE_EXTENSION = ".zip";
            DEF_ARCHIVE_CONTENT_TYPE = "application/zip";
        } else {
            DEF_ARCHIVE_EXTENSION = ".tar.gz";
            DEF_ARCHIVE_CONTENT_TYPE = "application/x-ustar";
        }
    }

    public void validatePasswordPolicyRegexp() {
        java.lang.String regexp = getPasswordPolicyRegexp();
        if ((!org.apache.commons.lang.StringUtils.isEmpty(regexp)) && (!regexp.equalsIgnoreCase(".*"))) {
            java.util.regex.Pattern.compile(regexp);
        }
    }

    public enum LdapUsernameCollisionHandlingBehavior {

        ADD,
        CONVERT,
        SKIP;
        public static org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior translate(java.lang.String value, org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior defaultValue) {
            java.lang.String processedValue = org.apache.commons.lang.StringUtils.upperCase(org.apache.commons.lang.StringUtils.trim(value));
            if (org.apache.commons.lang.StringUtils.isEmpty(processedValue)) {
                return defaultValue;
            } else {
                try {
                    return org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior.valueOf(processedValue);
                } catch (java.lang.IllegalArgumentException e) {
                    org.apache.ambari.server.configuration.Configuration.LOG.warn("Invalid LDAP username collision value ({}), using the default value ({})", value, defaultValue.name().toLowerCase());
                    return defaultValue;
                }
            }
        }
    }

    public enum DatabaseType {

        POSTGRES("postgres"),
        ORACLE("oracle"),
        MYSQL("mysql"),
        DERBY("derby"),
        SQL_SERVER("sqlserver"),
        SQL_ANYWHERE("sqlanywhere"),
        H2("h2");
        private static final java.util.Map<java.lang.String, org.apache.ambari.server.configuration.Configuration.DatabaseType> m_mappedTypes = new java.util.HashMap<>(5);

        static {
            for (org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType : java.util.EnumSet.allOf(org.apache.ambari.server.configuration.Configuration.DatabaseType.class)) {
                m_mappedTypes.put(databaseType.getName(), databaseType);
            }
        }

        private java.lang.String m_databaseType;

        DatabaseType(java.lang.String databaseType) {
            m_databaseType = databaseType;
        }

        public java.lang.String getName() {
            return m_databaseType;
        }

        public org.apache.ambari.server.configuration.Configuration.DatabaseType get(java.lang.String databaseTypeName) {
            return org.apache.ambari.server.configuration.Configuration.DatabaseType.m_mappedTypes.get(databaseTypeName);
        }
    }

    public enum ConnectionPoolType {

        INTERNAL("internal"),
        C3P0("c3p0");
        private java.lang.String m_name;

        ConnectionPoolType(java.lang.String name) {
            m_name = name;
        }

        public java.lang.String getName() {
            return m_name;
        }
    }

    public Configuration() {
        this(org.apache.ambari.server.configuration.Configuration.readConfigFile());
    }

    public Configuration(java.util.Properties properties) {
        this.properties = properties;
        agentConfigsMap = new java.util.HashMap<>();
        agentConfigsMap.put(org.apache.ambari.server.configuration.Configuration.AGENT_CONFIGS_DEFAULT_SECTION, new java.util.HashMap<java.lang.String, java.lang.String>());
        java.util.Map<java.lang.String, java.lang.String> defaultAgentConfigsMap = agentConfigsMap.get(org.apache.ambari.server.configuration.Configuration.AGENT_CONFIGS_DEFAULT_SECTION);
        defaultAgentConfigsMap.put(org.apache.ambari.server.configuration.Configuration.CHECK_REMOTE_MOUNTS.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CHECK_REMOTE_MOUNTS));
        defaultAgentConfigsMap.put(org.apache.ambari.server.configuration.Configuration.CHECK_MOUNTS_TIMEOUT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CHECK_MOUNTS_TIMEOUT));
        defaultAgentConfigsMap.put(org.apache.ambari.server.configuration.Configuration.ENABLE_AUTO_AGENT_CACHE_UPDATE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.ENABLE_AUTO_AGENT_CACHE_UPDATE));
        defaultAgentConfigsMap.put(org.apache.ambari.server.configuration.Configuration.JAVA_HOME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.JAVA_HOME));
        configsMap = new java.util.HashMap<>();
        configsMap.putAll(defaultAgentConfigsMap);
        configsMap.put(org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL_PORT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL_PORT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_ONE_WAY_SSL_PORT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_ONE_WAY_SSL_PORT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_CHAIN_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_CHAIN_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CSR_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CSR_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.KSTR_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.KSTR_TYPE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.KSTR_TYPE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.TSTR_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.TSTR_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.TSTR_TYPE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.TSTR_TYPE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_FILE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_FILE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.PASSPHRASE_ENV.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.PASSPHRASE_ENV));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), java.lang.System.getenv(configsMap.get(org.apache.ambari.server.configuration.Configuration.PASSPHRASE_ENV.getKey())));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_LEN.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_LEN));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_CIPHERS.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_CIPHERS));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_PROTOCOLS.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_PROTOCOLS));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), properties.getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey())));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_TYPE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_TYPE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_TYPE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_TYPE));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.JAVA_HOME.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.JAVA_HOME));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.REQUEST_LOGPATH.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.REQUEST_LOGPATH));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.LOG4JMONITOR_DELAY.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.LOG4JMONITOR_DELAY));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.REQUEST_LOG_RETAINDAYS.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.REQUEST_LOG_RETAINDAYS));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.EXTERNAL_SCRIPT_TIMEOUT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.EXTERNAL_SCRIPT_TIMEOUT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.KDC_PORT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.KDC_PORT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.PROXY_ALLOWED_HOST_PORTS.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.PROXY_ALLOWED_HOST_PORTS));
        configsMap.put(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE.getKey(), getProperty(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE));
        java.io.File passFile = new java.io.File((configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_FILE.getKey()));
        java.lang.String password = null;
        if (!passFile.exists()) {
            org.apache.ambari.server.configuration.Configuration.LOG.info("Generation of file with password");
            try {
                password = org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(java.lang.Integer.parseInt(configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_LEN.getKey())));
                org.apache.commons.io.FileUtils.writeStringToFile(passFile, password, java.nio.charset.Charset.defaultCharset());
                org.apache.ambari.server.utils.ShellCommandUtil.setUnixFilePermissions(org.apache.ambari.server.utils.ShellCommandUtil.MASK_OWNER_ONLY_RW, passFile.getAbsolutePath());
            } catch (java.io.IOException e) {
                e.printStackTrace();
                throw new java.lang.RuntimeException("Error reading certificate password from file");
            }
        } else {
            org.apache.ambari.server.configuration.Configuration.LOG.info("Reading password from existing file");
            try {
                password = org.apache.commons.io.FileUtils.readFileToString(passFile, java.nio.charset.Charset.defaultCharset());
                password = password.replaceAll("\\p{Cntrl}", "");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        configsMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey(), password);
        if (getApiSSLAuthentication()) {
            org.apache.ambari.server.configuration.Configuration.LOG.info("API SSL Authentication is turned on.");
            java.io.File httpsPassFile = new java.io.File((configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey()));
            if (httpsPassFile.exists()) {
                org.apache.ambari.server.configuration.Configuration.LOG.info("Reading password from existing file");
                try {
                    password = org.apache.commons.io.FileUtils.readFileToString(httpsPassFile, java.nio.charset.Charset.defaultCharset());
                    password = password.replaceAll("\\p{Cntrl}", "");
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    throw new java.lang.RuntimeException(("Error reading certificate password from" + " file ") + httpsPassFile.getAbsolutePath());
                }
            } else {
                org.apache.ambari.server.configuration.Configuration.LOG.error("There is no keystore for https UI connection.");
                org.apache.ambari.server.configuration.Configuration.LOG.error(("Run \"ambari-server setup-https\" or set " + org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey()) + " = false.");
                throw new java.lang.RuntimeException(("Error reading certificate password from " + "file ") + httpsPassFile.getAbsolutePath());
            }
            configsMap.put(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS.getKey(), password);
        }
        kerberosAuthenticationProperties = createKerberosAuthenticationProperties();
        loadSSLParams();
    }

    public java.lang.String getProperty(java.lang.String key) {
        return properties.getProperty(key);
    }

    public java.util.Properties getProperties() {
        return new java.util.Properties(properties);
    }

    public <T> java.lang.String getProperty(org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<T> configurationProperty) {
        java.lang.String defaultStringValue = null;
        if (null != configurationProperty.getDefaultValue()) {
            defaultStringValue = java.lang.String.valueOf(configurationProperty.getDefaultValue());
        }
        return properties.getProperty(configurationProperty.getKey(), defaultStringValue);
    }

    public void setProperty(org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.String> configurationProperty, java.lang.String value) {
        properties.setProperty(configurationProperty.getKey(), value);
    }

    protected void loadSSLParams() {
        if (getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PATH) != null) {
            java.lang.System.setProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE, getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PATH));
        }
        if (getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD) != null) {
            java.lang.String ts_password = org.apache.ambari.server.utils.PasswordUtils.getInstance().readPasswordFromStore(getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD), this);
            if (ts_password != null) {
                java.lang.System.setProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE_PASSWORD, ts_password);
            } else {
                java.lang.System.setProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE_PASSWORD, getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD));
            }
        }
        if (getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_TYPE) != null) {
            java.lang.System.setProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE_TYPE, getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_TYPE));
        }
    }

    private static java.util.Properties readConfigFile() {
        java.util.Properties properties = new java.util.Properties();
        java.io.InputStream inputStream = org.apache.ambari.server.configuration.Configuration.class.getClassLoader().getResourceAsStream(org.apache.ambari.server.configuration.Configuration.CONFIG_FILE);
        if (inputStream == null) {
            throw new java.lang.RuntimeException(org.apache.ambari.server.configuration.Configuration.CONFIG_FILE + " not found in classpath");
        }
        try {
            properties.load(new java.io.InputStreamReader(inputStream, com.google.common.base.Charsets.UTF_8));
            inputStream.close();
        } catch (java.io.FileNotFoundException fnf) {
            org.apache.ambari.server.configuration.Configuration.LOG.info(("No configuration file " + org.apache.ambari.server.configuration.Configuration.CONFIG_FILE) + " found in classpath.", fnf);
        } catch (java.io.IOException ie) {
            throw new java.lang.IllegalArgumentException("Can't read configuration file " + org.apache.ambari.server.configuration.Configuration.CONFIG_FILE, ie);
        }
        return properties;
    }

    private void writeConfigFile(java.util.Properties propertiesToStore, boolean append) throws org.apache.ambari.server.AmbariException {
        java.io.File configFile = null;
        try {
            configFile = getConfigFile();
            propertiesToStore.store(new java.io.OutputStreamWriter(new java.io.FileOutputStream(configFile, append), com.google.common.base.Charsets.UTF_8), null);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.configuration.Configuration.LOG.error(((((("Cannot write properties [" + propertiesToStore) + "] into configuration file [") + configFile) + ", ") + append) + "] ");
            throw new org.apache.ambari.server.AmbariException("Error while clearing ambari.properties", e);
        }
    }

    public void removePropertiesFromAmbariProperties(java.util.Collection<java.lang.String> propertiesToBeRemoved) throws org.apache.ambari.server.AmbariException {
        final java.util.Properties existingProperties = org.apache.ambari.server.configuration.Configuration.readConfigFile();
        propertiesToBeRemoved.forEach(key -> {
            existingProperties.remove(key);
        });
        writeConfigFile(existingProperties, false);
        properties = org.apache.ambari.server.configuration.Configuration.readConfigFile();
    }

    public java.util.Properties getLog4jProperties() {
        if (!log4jProperties.isEmpty()) {
            return log4jProperties;
        }
        java.io.InputStream inputStream = org.apache.ambari.server.configuration.Configuration.class.getClassLoader().getResourceAsStream(org.apache.ambari.server.configuration.Configuration.AMBARI_LOG_FILE);
        if (inputStream == null) {
            throw new java.lang.RuntimeException(org.apache.ambari.server.configuration.Configuration.AMBARI_LOG_FILE + " not found in classpath");
        }
        try {
            log4jProperties.load(inputStream);
            inputStream.close();
        } catch (java.io.FileNotFoundException fnf) {
            org.apache.ambari.server.configuration.Configuration.LOG.info(("No configuration file " + org.apache.ambari.server.configuration.Configuration.AMBARI_LOG_FILE) + " found in classpath.", fnf);
        } catch (java.io.IOException ie) {
            throw new java.lang.IllegalArgumentException("Can't read configuration file " + org.apache.ambari.server.configuration.Configuration.AMBARI_LOG_FILE, ie);
        }
        return log4jProperties;
    }

    public void writeToAmbariUpgradeConfigUpdatesFile(com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog, java.lang.String configType, java.lang.String serviceName, java.lang.String writeToAmbariUpgradeConfigUpdatesFile) {
        try {
            if (ambariUpgradeConfigUpdatesFilePath == null) {
                java.util.Properties log4jProperties = getLog4jProperties();
                if (log4jProperties != null) {
                    java.lang.String logPath = log4jProperties.getProperty("ambari.log.dir");
                    java.lang.String rootPath = log4jProperties.getProperty("ambari.root.dir");
                    logPath = org.apache.commons.lang.StringUtils.replace(logPath, "${ambari.root.dir}", rootPath);
                    logPath = org.apache.commons.lang.StringUtils.replace(logPath, "//", "/");
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(logPath)) {
                        ambariUpgradeConfigUpdatesFilePath = (logPath + java.io.File.separator) + writeToAmbariUpgradeConfigUpdatesFile;
                    }
                } else {
                    org.apache.ambari.server.configuration.Configuration.LOG.warn("Log4j properties are not available");
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Failed to create log file name or get path for it:", e);
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ambariUpgradeConfigUpdatesFilePath)) {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            java.io.Writer fileWriter = null;
            try {
                com.google.gson.JsonObject rootJson = readFileToJSON(ambariUpgradeConfigUpdatesFilePath);
                buildServiceJson(propertiesToLog, configType, serviceName, rootJson);
                fileWriter = new java.io.FileWriter(ambariUpgradeConfigUpdatesFilePath);
                gson.toJson(rootJson, fileWriter);
            } catch (java.lang.IllegalArgumentException e) {
                com.google.gson.JsonObject rootJson = new com.google.gson.JsonObject();
                buildServiceJson(propertiesToLog, configType, serviceName, rootJson);
                try {
                    fileWriter = new java.io.FileWriter(ambariUpgradeConfigUpdatesFilePath);
                    gson.toJson(rootJson, fileWriter);
                } catch (java.io.IOException e1) {
                    org.apache.ambari.server.configuration.Configuration.LOG.error("Unable to write data into " + ambariUpgradeConfigUpdatesFilePath, e);
                }
            } catch (java.io.IOException e) {
                org.apache.ambari.server.configuration.Configuration.LOG.error("Unable to write data into " + ambariUpgradeConfigUpdatesFilePath, e);
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.configuration.Configuration.LOG.error("Unable to close file " + ambariUpgradeConfigUpdatesFilePath, e);
                }
            }
        }
    }

    private void buildServiceJson(com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog, java.lang.String configType, java.lang.String serviceName, com.google.gson.JsonObject rootJson) {
        com.google.gson.JsonElement serviceJson = null;
        serviceJson = rootJson.get(serviceName);
        com.google.gson.JsonObject serviceJsonObject = null;
        if (serviceJson != null) {
            serviceJsonObject = serviceJson.getAsJsonObject();
        } else {
            serviceJsonObject = new com.google.gson.JsonObject();
        }
        buildConfigJson(propertiesToLog, serviceJsonObject, configType);
        if (serviceName == null) {
            serviceName = "General";
        }
        rootJson.add(serviceName, serviceJsonObject);
    }

    private void buildConfigJson(com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog, com.google.gson.JsonObject serviceJson, java.lang.String configType) {
        com.google.gson.JsonElement configJson = null;
        configJson = serviceJson.get(configType);
        com.google.gson.JsonObject configJsonObject = null;
        if (configJson != null) {
            configJsonObject = configJson.getAsJsonObject();
        } else {
            configJsonObject = new com.google.gson.JsonObject();
        }
        buildConfigUpdateTypes(propertiesToLog, configJsonObject);
        serviceJson.add(configType, configJsonObject);
    }

    private void buildConfigUpdateTypes(com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog, com.google.gson.JsonObject configJson) {
        for (org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType configUpdateType : propertiesToLog.keySet()) {
            com.google.gson.JsonElement currentConfigUpdateType = configJson.get(configUpdateType.getDescription());
            com.google.gson.JsonObject currentConfigUpdateTypeJsonObject = null;
            if (currentConfigUpdateType != null) {
                currentConfigUpdateTypeJsonObject = currentConfigUpdateType.getAsJsonObject();
            } else {
                currentConfigUpdateTypeJsonObject = new com.google.gson.JsonObject();
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : propertiesToLog.get(configUpdateType)) {
                currentConfigUpdateTypeJsonObject.add(property.getKey(), new com.google.gson.JsonPrimitive(property.getValue()));
            }
            configJson.add(configUpdateType.getDescription(), currentConfigUpdateTypeJsonObject);
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getDatabaseConnectorNames() {
        java.io.File file = getConfigFile();
        java.lang.Long currentConfigLastModifiedDate = file.lastModified();
        if (currentConfigLastModifiedDate.longValue() != configLastModifiedDateForCustomJDBC.longValue()) {
            org.apache.ambari.server.configuration.Configuration.LOG.info("Ambari properties config file changed.");
            java.util.Properties properties = org.apache.ambari.server.configuration.Configuration.readConfigFile();
            for (java.lang.String propertyName : org.apache.ambari.server.configuration.Configuration.dbConnectorPropertyNames) {
                java.lang.String propertyValue = properties.getProperty(propertyName);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(propertyValue)) {
                    databaseConnectorNames.put(propertyName.replace(".", "_"), propertyValue);
                }
            }
            configLastModifiedDateForCustomJDBC = currentConfigLastModifiedDate;
        }
        return databaseConnectorNames;
    }

    public java.io.File getConfigFile() {
        return new java.io.File(org.apache.ambari.server.configuration.Configuration.class.getClassLoader().getResource(org.apache.ambari.server.configuration.Configuration.CONFIG_FILE).getPath());
    }

    public java.util.Map<java.lang.String, java.lang.String> getPreviousDatabaseConnectorNames() {
        java.io.File file = getConfigFile();
        java.lang.Long currentConfigLastModifiedDate = file.lastModified();
        if (currentConfigLastModifiedDate.longValue() != configLastModifiedDateForCustomJDBCToRemove.longValue()) {
            org.apache.ambari.server.configuration.Configuration.LOG.info("Ambari properties config file changed.");
            java.util.Properties properties = org.apache.ambari.server.configuration.Configuration.readConfigFile();
            for (java.lang.String propertyName : org.apache.ambari.server.configuration.Configuration.dbConnectorPropertyNames) {
                propertyName = "previous." + propertyName;
                java.lang.String propertyValue = properties.getProperty(propertyName);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(propertyValue)) {
                    databasePreviousConnectorNames.put(propertyName.replace(".", "_"), propertyValue);
                }
            }
            configLastModifiedDateForCustomJDBCToRemove = currentConfigLastModifiedDate;
        }
        return databasePreviousConnectorNames;
    }

    public com.google.gson.JsonObject getHostChangesJson(java.lang.String hostChangesFile) {
        if (hostChangesJson == null) {
            hostChangesJson = readFileToJSON(hostChangesFile);
        }
        return hostChangesJson;
    }

    private com.google.gson.JsonObject readFileToJSON(java.lang.String file) {
        com.google.gson.JsonObject jsonObject;
        try {
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            com.google.gson.JsonElement jsonElement = parser.parse(new java.io.FileReader(file));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (java.io.FileNotFoundException e) {
            throw new java.lang.IllegalArgumentException("No file " + file, e);
        }
        return jsonObject;
    }

    public java.io.File getViewsDir() {
        java.lang.String fileName = getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_DIRECTORY);
        return new java.io.File(fileName);
    }

    public boolean isViewValidationEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_VALIDATE));
    }

    public boolean isViewRemoveUndeployedEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_REMOVE_UNDEPLOYED));
    }

    public boolean isViewDirectoryWatcherServiceDisabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.DISABLE_VIEW_DIRECTORY_WATCHER));
    }

    public int getJavaVersion() {
        java.lang.String versionStr = java.lang.System.getProperty("java.version");
        if (versionStr.startsWith("1.6")) {
            return 6;
        } else if (versionStr.startsWith("1.7")) {
            return 7;
        } else if (versionStr.startsWith("1.8")) {
            return 8;
        } else if (versionStr.startsWith("17")) {
            return 17;
        } else {
            return -1;
        }
    }

    public java.io.File getBootStrapDir() {
        java.lang.String fileName = getProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_DIRECTORY);
        return new java.io.File(fileName);
    }

    public java.lang.String getBootStrapScript() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_SCRIPT);
    }

    public java.lang.String getBootSetupAgentScript() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_SETUP_AGENT_SCRIPT);
    }

    public java.lang.String getBootSetupAgentPassword() {
        java.lang.String pass = configsMap.get(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey());
        if (null != pass) {
            return pass;
        }
        return getProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_SETUP_AGENT_PASSWORD);
    }

    public java.io.File getRecommendationsDir() {
        java.lang.String fileName = getProperty(org.apache.ambari.server.configuration.Configuration.RECOMMENDATIONS_DIR);
        return new java.io.File(fileName);
    }

    public java.lang.String getRecommendationsArtifactsLifetime() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOMMENDATIONS_ARTIFACTS_LIFETIME);
    }

    public int getRecommendationsArtifactsRolloverMax() {
        int rollovermax = java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.RECOMMENDATIONS_ARTIFACTS_ROLLOVER_MAX));
        return rollovermax == 0 ? 100 : rollovermax;
    }

    public java.lang.String areHostsSysPrepped() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SYS_PREPPED_HOSTS);
    }

    public java.lang.Integer getHeartbeatMonitorInterval() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.HEARTBEAT_MONITORING_INTERVAL));
    }

    public boolean arePackagesLegacyOverridden() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.LEGACY_OVERRIDE).equalsIgnoreCase("true");
    }

    public org.apache.ambari.server.actionmanager.CommandExecutionType getStageExecutionType() {
        return org.apache.ambari.server.actionmanager.CommandExecutionType.valueOf(getProperty(org.apache.ambari.server.configuration.Configuration.COMMAND_EXECUTION_TYPE));
    }

    public java.lang.String getStackAdvisorScript() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.STACK_ADVISOR_SCRIPT);
    }

    public java.util.List<java.lang.String> getRollingUpgradeSkipPackagesPrefixes() {
        java.lang.String propertyValue = getProperty(org.apache.ambari.server.configuration.Configuration.ROLLING_UPGRADE_SKIP_PACKAGES_PREFIXES);
        java.util.ArrayList<java.lang.String> res = new java.util.ArrayList<>();
        for (java.lang.String prefix : propertyValue.split(",")) {
            if (!prefix.isEmpty()) {
                res.add(prefix.trim());
            }
        }
        return res;
    }

    public boolean isUpgradePrecheckBypass() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_BYPASS_PRECHECKS));
    }

    public int getStackUpgradeAutoRetryTimeoutMins() {
        java.lang.Integer result = org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_AUTO_RETRY_TIMEOUT_MINS));
        return result >= 0 ? result : 0;
    }

    public int getStackUpgradeAutoRetryCheckIntervalSecs() {
        java.lang.Integer result = org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_AUTO_RETRY_CHECK_INTERVAL_SECS));
        return result >= 0 ? result : 0;
    }

    public java.util.List<java.lang.String> getStackUpgradeAutoRetryCustomCommandNamesToIgnore() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_AUTO_RETRY_CUSTOM_COMMAND_NAMES_TO_IGNORE);
        java.util.List<java.lang.String> list = convertCSVwithQuotesToList(value);
        listToLowerCase(list);
        return list;
    }

    public java.util.List<java.lang.String> getStackUpgradeAutoRetryCommandDetailsToIgnore() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_AUTO_RETRY_COMMAND_DETAILS_TO_IGNORE);
        java.util.List<java.lang.String> list = convertCSVwithQuotesToList(value);
        listToLowerCase(list);
        return list;
    }

    private java.util.List<java.lang.String> convertCSVwithQuotesToList(java.lang.String value) {
        java.util.List<java.lang.String> list = new java.util.ArrayList<>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
            if (value.indexOf(",") >= 0) {
                for (java.lang.String e : value.split(",")) {
                    e = org.apache.commons.lang.StringUtils.stripStart(e, "\"");
                    e = org.apache.commons.lang.StringUtils.stripEnd(e, "\"");
                    list.add(e);
                }
            } else {
                list.add(value);
            }
        }
        return list;
    }

    private void listToLowerCase(java.util.List<java.lang.String> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toLowerCase());
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getConfigsMap() {
        return configsMap;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getAgentConfigsMap() {
        return agentConfigsMap;
    }

    public boolean csrfProtectionEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.API_CSRF_PREVENTION));
    }

    public org.apache.ambari.server.security.ClientSecurityType getClientSecurityType() {
        return org.apache.ambari.server.security.ClientSecurityType.fromString(getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_SECURITY));
    }

    public void setClientSecurityType(org.apache.ambari.server.security.ClientSecurityType type) {
        setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_SECURITY, type.toString());
    }

    public java.lang.String getWebAppDir() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.WEBAPP_DIRECTORY.getKey());
    }

    public java.lang.String getHostsMapFile() {
        org.apache.ambari.server.configuration.Configuration.LOG.info("Hosts Mapping File " + getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_HOSTS_MAPPING));
        return getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_HOSTS_MAPPING);
    }

    public java.lang.String getMetadataPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH);
    }

    public java.lang.String getCommonServicesPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.COMMON_SERVICES_DIR_PATH);
    }

    public java.lang.String getExtensionsPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.EXTENSIONS_DIR_PATH);
    }

    public java.lang.String getMpacksStagingPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_STAGING_DIR_PATH);
    }

    public java.lang.String getMpacksV2StagingPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH);
    }

    public java.lang.String getServerVersionFilePath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE);
    }

    public java.lang.String getServerVersion() {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(new java.io.File(getServerVersionFilePath()), java.nio.charset.Charset.defaultCharset()).trim();
        } catch (java.io.IOException e) {
            org.apache.ambari.server.configuration.Configuration.LOG.error("Unable to read server version file", e);
        }
        return null;
    }

    public java.lang.String getDefaultApiAuthenticatedUser() {
        return properties.getProperty(org.apache.ambari.server.configuration.Configuration.API_AUTHENTICATED_USER.getKey());
    }

    public int getClientSSLApiPort() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_PORT));
    }

    public boolean getApiSSLAuthentication() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL));
    }

    public boolean getAgentSSLAuthentication() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_USE_SSL));
    }

    public java.lang.String getStrictTransportSecurityHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_STRICT_TRANSPORT_HEADER_VALUE);
    }

    public java.lang.String getXFrameOptionsHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_X_FRAME_OPTIONS_HEADER_VALUE);
    }

    public java.lang.String getXXSSProtectionHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_X_XSS_PROTECTION_HEADER_VALUE);
    }

    public java.lang.String getContentSecurityPolicyHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE);
    }

    public java.lang.String getXContentTypeHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_X_CONTENT_TYPE_HEADER_VALUE);
    }

    public java.lang.String getCacheControlHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CACHE_CONTROL_HEADER_VALUE);
    }

    public java.lang.String getPragmaHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_PRAGMA_HEADER_VALUE);
    }

    public java.lang.String getCharsetHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET);
    }

    public java.lang.String getViewsStrictTransportSecurityHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_STRICT_TRANSPORT_HEADER_VALUE);
    }

    public java.lang.String getViewsXFrameOptionsHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_FRAME_OPTIONS_HEADER_VALUE);
    }

    public java.lang.String getViewsXXSSProtectionHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_XSS_PROTECTION_HEADER_VALUE);
    }

    public java.lang.String getViewsContentSecurityPolicyHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE);
    }

    public java.lang.String getViewsXContentTypeHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_CONTENT_TYPE_HEADER_VALUE);
    }

    public java.lang.String getViewsCacheControlHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CACHE_CONTROL_HEADER_VALUE);
    }

    public java.lang.String getViewsAdditionalClasspath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_ADDITIONAL_CLASSPATH_VALUE);
    }

    public java.lang.String getViewsPragmaHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_PRAGMA_HEADER_VALUE);
    }

    public java.lang.String getViewsCharsetHTTPResponseHeader() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CHARSET);
    }

    public boolean validateAgentHostnames() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE));
    }

    public boolean isTwoWaySsl() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL));
    }

    public boolean isApiGzipped() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.API_GZIP_COMPRESSION_ENABLED));
    }

    public boolean isGzipHandlerEnabledForJetty() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.GZIP_HANDLER_JETTY_ENABLED));
    }

    public boolean isAgentApiGzipped() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_API_GZIP_COMPRESSION_ENABLED));
    }

    public java.lang.String getApiGzipMinSize() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.API_GZIP_MIN_COMPRESSION_SIZE);
    }

    public org.apache.ambari.server.orm.PersistenceType getPersistenceType() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE);
        return org.apache.ambari.server.orm.PersistenceType.fromString(value);
    }

    public java.lang.String getDatabaseDriver() {
        if (getPersistenceType() != org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
            return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER);
        } else {
            return org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER;
        }
    }

    public java.lang.String getDatabaseUrl() {
        if (getPersistenceType() != org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
            java.lang.String URI = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL);
            if (URI != null) {
                return URI;
            } else {
                return getLocalDatabaseUrl();
            }
        } else {
            return org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL;
        }
    }

    public java.lang.String getLocalDatabaseUrl() {
        java.lang.String dbName = properties.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_DB_NAME.getKey());
        if ((dbName == null) || dbName.isEmpty()) {
            throw new java.lang.RuntimeException("Server DB Name is not configured!");
        }
        return org.apache.ambari.server.configuration.Configuration.JDBC_LOCAL_URL + dbName;
    }

    public java.lang.String getDatabaseUser() {
        if (getPersistenceType() == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
            return org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_USER;
        }
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_NAME);
    }

    public java.lang.String getDatabasePassword() {
        if (getPersistenceType() == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
            return org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_PASSWORD;
        }
        java.lang.String passwdProp = properties.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_PASSWD.getKey());
        java.lang.String dbpasswd = null;
        boolean isPasswordAlias = false;
        if (org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(passwdProp)) {
            dbpasswd = org.apache.ambari.server.utils.PasswordUtils.getInstance().readPasswordFromStore(passwdProp, this);
            isPasswordAlias = true;
        }
        if (dbpasswd != null) {
            return dbpasswd;
        } else if (isPasswordAlias) {
            org.apache.ambari.server.configuration.Configuration.LOG.error("Can't read db password from keystore. Please, check master key was set correctly.");
            throw new java.lang.RuntimeException("Can't read db password from keystore. Please, check master key was set correctly.");
        } else {
            return org.apache.ambari.server.utils.PasswordUtils.getInstance().readPasswordFromFile(passwdProp, org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_PASSWD.getDefaultValue());
        }
    }

    public java.lang.String getRcaDatabaseDriver() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_DRIVER);
    }

    public java.lang.String getRcaDatabaseUrl() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_URL);
    }

    public java.lang.String getRcaDatabaseUser() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_USER_NAME);
    }

    public java.lang.String getRcaDatabasePassword() {
        java.lang.String passwdProp = properties.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_USER_PASSWD.getKey());
        return org.apache.ambari.server.utils.PasswordUtils.getInstance().readPassword(passwdProp, org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_USER_PASSWD.getDefaultValue());
    }

    public java.lang.String getServerOsType() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION);
    }

    public java.lang.String getServerOsFamily() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.OS_FAMILY);
    }

    public java.lang.String getMasterHostname(java.lang.String defaultValue) {
        return properties.getProperty(org.apache.ambari.server.configuration.Configuration.BOOTSTRAP_MASTER_HOSTNAME.getKey(), defaultValue);
    }

    public int getClientApiPort() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_PORT));
    }

    public java.lang.String getOjdbcJarName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.OJDBC_JAR_NAME);
    }

    public java.lang.String getJavaHome() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.JAVA_HOME);
    }

    public java.lang.String getJDKName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.JDK_NAME);
    }

    public java.lang.String getJCEName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.JCE_NAME);
    }

    public java.lang.String getStackJavaHome() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.STACK_JAVA_HOME);
    }

    public java.lang.String getStackJDKName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.STACK_JDK_NAME);
    }

    public java.lang.String getStackJCEName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.STACK_JCE_NAME);
    }

    public java.lang.String getStackJavaVersion() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.STACK_JAVA_VERSION);
    }

    public java.lang.String getAmbariBlacklistFile() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_MASK_FILE);
    }

    public java.lang.String getServerDBName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_DB_NAME);
    }

    public java.lang.String getMySQLJarName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.MYSQL_JAR_NAME);
    }

    public java.lang.String getPasswordPolicyRegexp() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.PASSWORD_POLICY_REGEXP);
    }

    public java.lang.String getPasswordPolicyDescription() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.PASSWORD_POLICY_DESCRIPTION);
    }

    public int getPasswordPolicyHistoryCount() {
        int historyCount = java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.PASSWORD_POLICY_HISTORY_COUNT));
        if (historyCount < org.apache.ambari.server.configuration.Configuration.MINIMUM_PASSWORD_HISTORY_LIMIT) {
            historyCount = org.apache.ambari.server.configuration.Configuration.MINIMUM_PASSWORD_HISTORY_LIMIT;
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Invalid password history count detected. The count has been automatically set to the minimum permissible value of {}.", org.apache.ambari.server.configuration.Configuration.MINIMUM_PASSWORD_HISTORY_LIMIT);
        }
        if (historyCount > org.apache.ambari.server.configuration.Configuration.MAXIMUM_PASSWORD_HISTORY_LIMIT) {
            historyCount = org.apache.ambari.server.configuration.Configuration.MAXIMUM_PASSWORD_HISTORY_LIMIT;
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Invalid password history count detected. The count has been automatically set to the maximum permissible value of {}.", org.apache.ambari.server.configuration.Configuration.MAXIMUM_PASSWORD_HISTORY_LIMIT);
        }
        return historyCount;
    }

    public org.apache.ambari.server.orm.JPATableGenerationStrategy getJPATableGenerationStrategy() {
        return org.apache.ambari.server.orm.JPATableGenerationStrategy.fromString(java.lang.System.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_GENERATE_TABLES.getKey()));
    }

    public int getConnectionMaxIdleTime() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_CONNECTION_MAX_IDLE_TIME));
    }

    public java.lang.String getAnonymousAuditName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.ANONYMOUS_AUDIT_NAME);
    }

    public boolean isMasterKeyPersisted() {
        java.io.File masterKeyFile = getMasterKeyLocation();
        return (masterKeyFile != null) && masterKeyFile.exists();
    }

    public java.io.File getServerKeyStoreDirectory() {
        java.lang.String path = getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR);
        return (path == null) || path.isEmpty() ? new java.io.File(".") : new java.io.File(path);
    }

    public java.io.File getMasterKeyLocation() {
        java.io.File location;
        java.lang.String path = getProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION);
        if (org.apache.commons.lang.StringUtils.isEmpty(path)) {
            location = new java.io.File(getServerKeyStoreDirectory(), org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is not set, using {}", org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION, location.getAbsolutePath());
        } else {
            location = new java.io.File(path, org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is {}", org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION, location.getAbsolutePath());
        }
        return location;
    }

    public java.io.File getMasterKeyStoreLocation() {
        java.io.File location;
        java.lang.String path = getProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION);
        if (org.apache.commons.lang.StringUtils.isEmpty(path)) {
            location = new java.io.File(getServerKeyStoreDirectory(), org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is not set, using {}", org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION, location.getAbsolutePath());
        } else {
            location = new java.io.File(path, org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is {}", org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION, location.getAbsolutePath());
        }
        return location;
    }

    public long getTemporaryKeyStoreRetentionMinutes() {
        long minutes;
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES);
        if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is not set, using default value ({})", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getKey(), org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getDefaultValue());
            minutes = org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getDefaultValue();
        } else {
            try {
                minutes = java.lang.Long.parseLong(value);
                org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is {}", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES, value);
            } catch (java.lang.NumberFormatException e) {
                org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} ({}) should be a number, falling back to default value ({})", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getKey(), value, org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getDefaultValue());
                minutes = org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_RETENTION_MINUTES.getDefaultValue();
            }
        }
        return minutes;
    }

    public boolean isActivelyPurgeTemporaryKeyStore() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE);
        if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is not set, using default value ({})", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getKey(), org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getDefaultValue());
            return org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getDefaultValue();
        } else if ("true".equalsIgnoreCase(value)) {
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is {}", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getKey(), value);
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            org.apache.ambari.server.configuration.Configuration.LOG.debug("Value of {} is {}", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getKey(), value);
            return false;
        } else {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} should be either \"true\" or \"false\" but is \"{}\", falling back to default value ({})", org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getKey(), value, org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getDefaultValue());
            return org.apache.ambari.server.configuration.Configuration.TEMPORARYSTORE_ACTIVELY_PURGE.getDefaultValue();
        }
    }

    public java.lang.String getSrvrDisabledCiphers() {
        java.lang.String disabledCiphers = getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_CIPHERS);
        return disabledCiphers.trim();
    }

    public java.lang.String getSrvrDisabledProtocols() {
        java.lang.String disabledProtocols = getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_DISABLED_PROTOCOLS);
        return disabledProtocols.trim();
    }

    public int getOneWayAuthPort() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_ONE_WAY_SSL_PORT));
    }

    public int getTwoWayAuthPort() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL_PORT));
    }

    public java.util.Properties getDatabaseCustomProperties() {
        if (null != customDbProperties) {
            return customDbProperties;
        }
        customDbProperties = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.Object, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String key = entry.getKey().toString();
            java.lang.String val = entry.getValue().toString();
            if (key.startsWith(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_PROPERTIES_PREFIX)) {
                key = "eclipselink.jdbc.property." + key.substring(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_PROPERTIES_PREFIX.length());
                customDbProperties.put(key, val);
            }
        }
        return customDbProperties;
    }

    public java.util.Properties getPersistenceCustomProperties() {
        if (null != customPersistenceProperties) {
            return customPersistenceProperties;
        }
        customPersistenceProperties = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.Object, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String key = entry.getKey().toString();
            java.lang.String val = entry.getValue().toString();
            if (key.startsWith(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_PROPERTIES_PREFIX)) {
                key = key.substring(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_PROPERTIES_PREFIX.length());
                customPersistenceProperties.put(key, val);
            }
        }
        return customPersistenceProperties;
    }

    public int getHttpRequestHeaderSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HTTP_REQUEST_HEADER_SIZE));
    }

    public int getHttpResponseHeaderSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HTTP_RESPONSE_HEADER_SIZE));
    }

    public java.util.Set<java.lang.String> getPropertiesToBlackList() {
        if (propertiesToMask != null) {
            return propertiesToMask;
        }
        java.util.Properties blacklistProperties = new java.util.Properties();
        java.lang.String blacklistFile = getAmbariBlacklistFile();
        propertiesToMask = new java.util.HashSet<>();
        if (blacklistFile != null) {
            java.io.File propertiesMaskFile = new java.io.File(blacklistFile);
            java.io.InputStream inputStream = null;
            if (propertiesMaskFile.exists()) {
                try {
                    inputStream = new java.io.FileInputStream(propertiesMaskFile);
                    blacklistProperties.load(inputStream);
                    propertiesToMask = blacklistProperties.stringPropertyNames();
                } catch (java.lang.Exception e) {
                    java.lang.String message = java.lang.String.format("Blacklist properties file %s cannot be read", blacklistFile);
                    org.apache.ambari.server.configuration.Configuration.LOG.error(message);
                } finally {
                    org.apache.commons.io.IOUtils.closeQuietly(inputStream);
                }
            }
        }
        return propertiesToMask;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAmbariProperties() {
        java.util.Properties properties = org.apache.ambari.server.configuration.Configuration.readConfigFile();
        java.util.Map<java.lang.String, java.lang.String> ambariPropertiesMap = new java.util.HashMap<>();
        for (java.lang.String key : properties.stringPropertyNames()) {
            ambariPropertiesMap.put(key, properties.getProperty(key));
        }
        return ambariPropertiesMap;
    }

    public long getExecutionCommandsCacheSize() {
        java.lang.String stringValue = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_EC_CACHE_SIZE);
        long value = org.apache.ambari.server.configuration.Configuration.SERVER_EC_CACHE_SIZE.getDefaultValue();
        if (stringValue != null) {
            try {
                value = java.lang.Long.parseLong(stringValue);
            } catch (java.lang.NumberFormatException ignored) {
            }
        }
        return value;
    }

    public boolean getHostRoleCommandStatusSummaryCacheEnabled() {
        java.lang.String stringValue = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED);
        boolean value = org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getDefaultValue();
        if (stringValue != null) {
            try {
                value = java.lang.Boolean.valueOf(stringValue);
            } catch (java.lang.NumberFormatException ignored) {
            }
        }
        return value;
    }

    public long getHostRoleCommandStatusSummaryCacheSize() {
        java.lang.String stringValue = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_SIZE);
        long value = org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_SIZE.getDefaultValue();
        if (stringValue != null) {
            try {
                value = java.lang.Long.parseLong(stringValue);
            } catch (java.lang.NumberFormatException ignored) {
            }
        }
        return value;
    }

    public long getHostRoleCommandStatusSummaryCacheExpiryDuration() {
        java.lang.String stringValue = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION);
        long value = org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION.getDefaultValue();
        if (stringValue != null) {
            try {
                value = java.lang.Long.parseLong(stringValue);
            } catch (java.lang.NumberFormatException ignored) {
            }
        }
        return value;
    }

    public boolean isStaleConfigCacheEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_STALE_CONFIG_CACHE_ENABLED));
    }

    public java.lang.Integer staleConfigCacheExpiration() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_STALE_CONFIG_CACHE_EXPIRATION));
    }

    public java.lang.String[] getRepoValidationSuffixes(java.lang.String osType) {
        java.lang.String repoSuffixes;
        if (osFamily.isUbuntuFamily(osType)) {
            repoSuffixes = getProperty(org.apache.ambari.server.configuration.Configuration.REPO_SUFFIX_KEY_UBUNTU);
        } else {
            repoSuffixes = getProperty(org.apache.ambari.server.configuration.Configuration.REPO_SUFFIX_KEY_DEFAULT);
        }
        return repoSuffixes.split(",");
    }

    public java.lang.String isExecutionSchedulerClusterd() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_CLUSTERED);
    }

    public java.lang.String getExecutionSchedulerThreads() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_THREADS);
    }

    public java.lang.Integer getRequestReadTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.REQUEST_READ_TIMEOUT));
    }

    public java.lang.Integer getRequestConnectTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.REQUEST_CONNECT_TIMEOUT));
    }

    public java.lang.Integer getViewAmbariRequestReadTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_REQUEST_READ_TIMEOUT));
    }

    public java.lang.Integer getViewAmbariRequestConnectTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_REQUEST_CONNECT_TIMEOUT));
    }

    public java.lang.String getExecutionSchedulerConnections() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_CONNECTIONS);
    }

    public java.lang.String getExecutionSchedulerMaxStatementsPerConnection() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_MAX_STATEMENTS_PER_CONNECTION);
    }

    public java.lang.Long getExecutionSchedulerMisfireToleration() {
        return java.lang.Long.parseLong(getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_MISFIRE_TOLERATION));
    }

    public java.lang.Integer getExecutionSchedulerStartDelay() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_START_DELAY));
    }

    public java.lang.Long getExecutionSchedulerWait() {
        java.lang.String stringValue = getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT);
        long sleepTime = org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getDefaultValue();
        if (stringValue != null) {
            try {
                sleepTime = java.lang.Long.parseLong(stringValue);
            } catch (java.lang.NumberFormatException ignored) {
                org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} ({}) should be a number, " + "falling back to default value ({})", org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getKey(), stringValue, org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getDefaultValue());
            }
        }
        if (sleepTime > 60) {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} ({}) should be a number between 1 adn 60, " + "falling back to maximum value ({})", org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT, sleepTime, 60);
            sleepTime = 60L;
        }
        return sleepTime * 1000;
    }

    public java.lang.Integer getExternalScriptTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.EXTERNAL_SCRIPT_TIMEOUT));
    }

    public java.lang.Integer getExternalScriptThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.THREAD_POOL_SIZE_FOR_EXTERNAL_SCRIPT));
    }

    public boolean getParallelStageExecution() {
        return java.lang.Boolean.parseBoolean(configsMap.get(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION.getKey()));
    }

    public java.lang.String getCustomActionDefinitionPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.CUSTOM_ACTION_DEFINITION);
    }

    public int getAgentPackageParallelCommandsLimit() {
        int value = java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT));
        if (value < 1) {
            value = 1;
        }
        return value;
    }

    public java.lang.String getDefaultAgentTaskTimeout(boolean isPackageInstallationTask) {
        org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<java.lang.Long> configurationProperty = (isPackageInstallationTask) ? org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_INSTALL_TASK_TIMEOUT : org.apache.ambari.server.configuration.Configuration.AGENT_TASK_TIMEOUT;
        java.lang.String key = configurationProperty.getKey();
        java.lang.Long defaultValue = configurationProperty.getDefaultValue();
        java.lang.String value = getProperty(configurationProperty);
        if (org.apache.commons.lang.StringUtils.isNumeric(value)) {
            return value;
        } else {
            org.apache.ambari.server.configuration.Configuration.LOG.warn(java.lang.String.format("Value of %s (%s) should be a number, " + "falling back to default value (%s)", key, value, defaultValue));
            return java.lang.String.valueOf(defaultValue);
        }
    }

    public java.lang.Long getAgentServiceCheckTaskTimeout() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT);
        if (org.apache.commons.lang.StringUtils.isNumeric(value)) {
            return java.lang.Long.parseLong(value);
        } else {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} ({}) should be a number, falling back to default value ({})", org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT.getKey(), value, org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT.getDefaultValue());
            return org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT.getDefaultValue();
        }
    }

    public java.lang.Integer getDefaultServerTaskTimeout() {
        java.lang.String value = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_TASK_TIMEOUT);
        if (org.apache.commons.lang.StringUtils.isNumeric(value)) {
            return java.lang.Integer.parseInt(value);
        } else {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Value of {} ({}) should be a number, falling back to default value ({})", org.apache.ambari.server.configuration.Configuration.SERVER_TASK_TIMEOUT.getKey(), value, org.apache.ambari.server.configuration.Configuration.SERVER_TASK_TIMEOUT.getDefaultValue());
            return org.apache.ambari.server.configuration.Configuration.SERVER_TASK_TIMEOUT.getDefaultValue();
        }
    }

    public java.lang.String getResourceDirPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR);
    }

    public java.lang.String getSharedResourcesDirPath() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR);
    }

    public java.lang.String getServerJDBCPostgresSchemaName() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_POSTGRES_SCHEMA_NAME);
    }

    public int getClientThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_THREADPOOL_SIZE));
    }

    public int getSpringMessagingThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.MESSAGING_THREAD_POOL_SIZE));
    }

    public int getRegistrationThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.REGISTRATION_THREAD_POOL_SIZE));
    }

    public int getSubscriptionRegistryCacheSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SUBSCRIPTION_REGISTRY_CACHE_MAX_SIZE));
    }

    public int getAgentsRegistrationQueueSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENTS_REGISTRATION_QUEUE_SIZE));
    }

    public int getAgentsReportProcessingPeriod() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENTS_REPORT_PROCESSING_PERIOD));
    }

    public int getAgentsReportProcessingStartTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENTS_REPORT_PROCESSING_START_TIMEOUT));
    }

    public int getAgentsReportThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENTS_REPORT_THREAD_POOL_SIZE));
    }

    public int getAPIHeartbeatInterval() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.API_HEARTBEAT_INTERVAL));
    }

    public int getStompMaxIncomingMessageSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.STOMP_MAX_INCOMING_MESSAGE_SIZE));
    }

    public int getStompMaxBufferMessageSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.STOMP_MAX_BUFFER_MESSAGE_SIZE));
    }

    public int getExecutionCommandsRetryCount() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_COMMANDS_RETRY_COUNT));
    }

    public int getExecutionCommandsRetryInterval() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_COMMANDS_RETRY_INTERVAL));
    }

    public int getAgentThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_THREADPOOL_SIZE));
    }

    public int getViewExtractionThreadPoolMaxSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_EXTRACTION_THREADPOOL_MAX_SIZE));
    }

    public int getViewExtractionThreadPoolCoreSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_EXTRACTION_THREADPOOL_CORE_SIZE));
    }

    public int getViewRequestThreadPoolMaxSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_REQUEST_THREADPOOL_MAX_SIZE));
    }

    public java.lang.Boolean extractViewsAfterClusterConfig() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_EXTRACT_AFTER_CLUSTER_CONFIG));
    }

    public int getViewRequestThreadPoolTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_REQUEST_THREADPOOL_TIMEOUT));
    }

    public int getPropertyProvidersThreadPoolCoreSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_CORE_SIZE));
    }

    public int getPropertyProvidersThreadPoolMaxSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_MAX_SIZE));
    }

    public int getPropertyProvidersWorkerQueueSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_WORKER_QUEUE_SIZE));
    }

    public long getPropertyProvidersCompletionServiceTimeout() {
        return java.lang.Long.parseLong(getProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_COMPLETION_TIMEOUT));
    }

    public long getViewExtractionThreadPoolTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.VIEW_EXTRACTION_THREADPOOL_TIMEOUT));
    }

    public int getHttpSessionInactiveTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HTTP_SESSION_INACTIVE_TIMEOUT));
    }

    public java.lang.String getAlertTemplateFile() {
        return org.apache.commons.lang.StringUtils.strip(getProperty(org.apache.ambari.server.configuration.Configuration.ALERT_TEMPLATE_FILE));
    }

    public int getAlertEventPublisherCorePoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_CORE_SIZE));
    }

    public int getAlertEventPublisherMaxPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_MAX_SIZE));
    }

    public int getAlertEventPublisherWorkerQueueSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_WORKER_QUEUE_SIZE));
    }

    public java.lang.String getNodeRecoveryType() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_TYPE);
    }

    public java.lang.String getNodeRecoveryMaxCount() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_MAX_COUNT);
    }

    public java.lang.String getNodeRecoveryLifetimeMaxCount() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_LIFETIME_MAX_COUNT);
    }

    public java.lang.String getNodeRecoveryWindowInMin() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_WINDOW_IN_MIN);
    }

    public java.lang.String getRecoveryDisabledComponents() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_DISABLED_COMPONENTS);
    }

    public java.lang.String getRecoveryEnabledComponents() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_ENABLED_COMPONENTS);
    }

    public java.lang.String getNodeRecoveryRetryGap() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.RECOVERY_RETRY_GAP);
    }

    public java.lang.String getDefaultKdcPort() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.KDC_PORT);
    }

    public int getKdcConnectionCheckTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.KDC_CONNECTION_CHECK_TIMEOUT));
    }

    public java.io.File getKerberosKeytabCacheDir() {
        return new java.io.File(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROSTAB_CACHE_DIR));
    }

    public boolean isKerberosJaasConfigurationCheckEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_CHECK_JAAS_CONFIGURATION));
    }

    public org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior getLdapSyncCollisionHandlingBehavior() {
        return org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior.translate(getProperty(org.apache.ambari.server.configuration.Configuration.LDAP_SYNC_USERNAME_COLLISIONS_BEHAVIOR), org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior.ADD);
    }

    public org.apache.ambari.server.configuration.Configuration.DatabaseType getDatabaseType() {
        java.lang.String dbUrl = getDatabaseUrl();
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType;
        if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.DERBY.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.DERBY;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.SQL_SERVER.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.SQL_SERVER;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.SQL_ANYWHERE.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.SQL_ANYWHERE;
        } else if (dbUrl.contains(org.apache.ambari.server.configuration.Configuration.DatabaseType.H2.getName())) {
            databaseType = org.apache.ambari.server.configuration.Configuration.DatabaseType.H2;
        } else {
            throw new java.lang.RuntimeException("The database type could be not determined from the JDBC URL " + dbUrl);
        }
        return databaseType;
    }

    public java.lang.String getDatabaseSchema() {
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = getDatabaseType();
        java.lang.String databaseSchema;
        if (databaseType.equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES)) {
            databaseSchema = getServerJDBCPostgresSchemaName();
        } else if (databaseType.equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL)) {
            databaseSchema = getServerDBName();
        } else if (databaseType.equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE)) {
            databaseSchema = getDatabaseUser();
        } else if (databaseType.equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.DERBY)) {
            databaseSchema = org.apache.ambari.server.configuration.Configuration.DEFAULT_DERBY_SCHEMA;
        } else if (databaseType.equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.H2)) {
            databaseSchema = org.apache.ambari.server.configuration.Configuration.DEFAULT_H2_SCHEMA;
        } else {
            databaseSchema = null;
        }
        return databaseSchema;
    }

    public org.apache.ambari.server.configuration.Configuration.ConnectionPoolType getConnectionPoolType() {
        java.lang.String connectionPoolType = getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL);
        if (connectionPoolType.equals(org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.C3P0.getName())) {
            return org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.C3P0;
        }
        return org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.INTERNAL;
    }

    public int getConnectionPoolMinimumSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MIN_SIZE));
    }

    public int getConnectionPoolMaximumSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_SIZE));
    }

    public int getConnectionPoolMaximumAge() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_AGE));
    }

    public int getConnectionPoolMaximumIdle() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME));
    }

    public int getConnectionPoolMaximumExcessIdle() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME_EXCESS));
    }

    public int getConnectionPoolAcquisitionSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_AQUISITION_SIZE));
    }

    public int getConnectionPoolAcquisitionRetryAttempts() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_ATTEMPTS));
    }

    public int getConnectionPoolAcquisitionRetryDelay() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_DELAY));
    }

    public int getConnectionPoolIdleTestInternval() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_IDLE_TEST_INTERVAL));
    }

    public void setProperty(java.lang.String key, java.lang.String value) {
        if (null == value) {
            properties.remove(key);
        } else {
            properties.setProperty(key, value);
        }
    }

    public int getMetricCacheTTLSeconds() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_TTL));
    }

    public int getMetricCacheIdleSeconds() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_IDLE_TIME));
    }

    public int getMetricCacheEntryUnitSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_ENTRY_UNIT_SIZE));
    }

    public int getMetricsRequestReadTimeoutMillis() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_REQUEST_READ_TIMEOUT));
    }

    public int getMetricsRequestIntervalReadTimeoutMillis() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_REQUEST_INTERVAL_READ_TIMEOUT));
    }

    public int getMetricsRequestConnectTimeoutMillis() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_REQUEST_CONNECT_TIMEOUT));
    }

    public boolean isMetricsCacheDisabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_DISABLE));
    }

    public boolean isMetricsServiceDisabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.AMBARISERVER_METRICS_DISABLE));
    }

    public java.lang.Long getMetricRequestBufferTimeCatchupInterval() {
        return java.lang.Long.parseLong(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_REQUEST_CATCHUP_INTERVAL));
    }

    public java.lang.String getMetricsCacheManagerHeapPercent() {
        java.lang.String percent = getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_HEAP_PERCENT);
        return percent.trim().endsWith("%") ? percent.trim() : percent.trim() + "%";
    }

    public boolean useMetricsCacheCustomSizingEngine() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.TIMELINE_METRICS_CACHE_USE_CUSTOM_SIZING_ENGINE));
    }

    public org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties getKerberosAuthenticationProperties() {
        return kerberosAuthenticationProperties;
    }

    public java.lang.String getServerTempDir() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ALERT_CACHING)
    public boolean isAlertCacheEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_ENABLED));
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ALERT_CACHING)
    public int getAlertCacheFlushInterval() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_FLUSH_INTERVAL));
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ALERT_CACHING)
    public int getAlertCacheSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_SIZE));
    }

    public java.lang.String getAmbariDisplayUrl() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_DISPLAY_URL);
    }

    public int getOperationsRetryAttempts() {
        final int RETRY_ATTEMPTS_LIMIT = 10;
        java.lang.String property = getProperty(org.apache.ambari.server.configuration.Configuration.OPERATIONS_RETRY_ATTEMPTS);
        int attempts = java.lang.Integer.parseInt(property);
        if (attempts < 0) {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Invalid operations retry attempts number ({}), should be [0,{}]. Value reset to default {}", attempts, RETRY_ATTEMPTS_LIMIT, org.apache.ambari.server.configuration.Configuration.OPERATIONS_RETRY_ATTEMPTS.getDefaultValue());
            attempts = org.apache.ambari.server.configuration.Configuration.OPERATIONS_RETRY_ATTEMPTS.getDefaultValue();
        } else if (attempts > RETRY_ATTEMPTS_LIMIT) {
            org.apache.ambari.server.configuration.Configuration.LOG.warn("Invalid operations retry attempts number ({}), should be [0,{}]. Value set to {}", attempts, RETRY_ATTEMPTS_LIMIT, RETRY_ATTEMPTS_LIMIT);
            attempts = RETRY_ATTEMPTS_LIMIT;
        }
        if (attempts > 0) {
            org.apache.ambari.server.configuration.Configuration.LOG.info("Operations retry enabled. Number of retry attempts: {}", attempts);
        }
        return attempts;
    }

    public int getVersionDefinitionConnectTimeout() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.VERSION_DEFINITION_CONNECT_TIMEOUT));
    }

    public int getVersionDefinitionReadTimeout() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.VERSION_DEFINITION_READ_TIMEOUT));
    }

    public java.lang.Boolean getGplLicenseAccepted() {
        java.util.Properties actualProps = org.apache.ambari.server.configuration.Configuration.readConfigFile();
        java.lang.String defaultGPLAcceptedValue = null;
        if (null != org.apache.ambari.server.configuration.Configuration.GPL_LICENSE_ACCEPTED.getDefaultValue()) {
            defaultGPLAcceptedValue = java.lang.String.valueOf(org.apache.ambari.server.configuration.Configuration.GPL_LICENSE_ACCEPTED.getDefaultValue());
        }
        return java.lang.Boolean.valueOf(actualProps.getProperty(org.apache.ambari.server.configuration.Configuration.GPL_LICENSE_ACCEPTED.getKey(), defaultGPLAcceptedValue));
    }

    public java.lang.String getAgentStackRetryOnInstallCount() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_STACK_RETRY_COUNT);
    }

    public java.lang.String isAgentStackRetryOnInstallEnabled() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_STACK_RETRY_ON_REPO_UNAVAILABILITY);
    }

    public boolean isAuditLogEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.AUDIT_LOG_ENABLED));
    }

    public boolean isServerLocksProfilingEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_LOCKS_PROFILING));
    }

    public int getAuditLoggerCapacity() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.AUDIT_LOGGER_CAPACITY));
    }

    public java.lang.Integer getSNMPUdpBindPort() {
        java.lang.String udpPort = getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_SNMP_DISPATCH_UDP_PORT);
        return org.apache.commons.lang.StringUtils.isEmpty(udpPort) ? null : java.lang.Integer.parseInt(udpPort);
    }

    public java.lang.Integer getAmbariSNMPUdpBindPort() {
        java.lang.String udpPort = getProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_AMBARI_SNMP_DISPATCH_UDP_PORT);
        return org.apache.commons.lang.StringUtils.isEmpty(udpPort) ? null : java.lang.Integer.parseInt(udpPort);
    }

    public java.lang.String getProxyHostAndPorts() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.PROXY_ALLOWED_HOST_PORTS);
    }

    public int getMetricsServiceCacheTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_CACHE_TIMEOUT));
    }

    public int getMetricsServiceThreadPriority() {
        int priority = java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_THREAD_PRIORITY));
        if ((priority < java.lang.Thread.MIN_PRIORITY) || (priority > java.lang.Thread.MAX_PRIORITY)) {
            priority = java.lang.Thread.NORM_PRIORITY;
        }
        return priority;
    }

    public int getMetricsServiceThreadPoolCoreSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_THREADPOOL_CORE_SIZE));
    }

    public int getMetricsServiceThreadPoolMaxSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_THREADPOOL_MAX_SIZE));
    }

    public int getMetricsServiceWorkerQueueSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_THREADPOOL_WORKER_QUEUE_SIZE));
    }

    public int getMetricsServiceRequestTTL() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL));
    }

    public boolean isMetricsServiceRequestTTLCacheEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL_ENABLED));
    }

    public boolean isActiveInstance() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.ACTIVE_INSTANCE));
    }

    public boolean isUserHookEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.POST_USER_CREATION_HOOK_ENABLED));
    }

    public int getParallelTopologyTaskCreationThreadPoolSize() {
        try {
            return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.TOPOLOGY_TASK_PARALLEL_CREATION_THREAD_COUNT));
        } catch (java.lang.NumberFormatException e) {
            return org.apache.ambari.server.configuration.Configuration.TOPOLOGY_TASK_PARALLEL_CREATION_THREAD_COUNT.getDefaultValue();
        }
    }

    public boolean isParallelTopologyTaskCreationEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.TOPOLOGY_TASK_PARALLEL_CREATION_ENABLED));
    }

    public int getLogSearchPortalConnectTimeout() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.LOGSEARCH_PORTAL_CONNECT_TIMEOUT));
    }

    public int getLogSearchPortalReadTimeout() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.LOGSEARCH_PORTAL_READ_TIMEOUT));
    }

    public java.lang.String getLogSearchPortalExternalAddress() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.LOGSEARCH_PORTAL_EXTERNAL_ADDRESS);
    }

    public int getLogSearchMetadataCacheExpireTimeout() {
        return org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.LOGSEARCH_METADATA_CACHE_EXPIRE_TIMEOUT));
    }

    public int getTlsEphemeralDhKeySize() {
        int keySize = org.apache.commons.lang.math.NumberUtils.toInt(getProperty(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE));
        if (keySize == 0) {
            throw new java.lang.IllegalArgumentException((("Invalid " + org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE) + " ") + getProperty(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE));
        }
        return keySize;
    }

    public java.lang.String getDispatchScriptDirectory() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.DISPATCH_PROPERTY_SCRIPT_DIRECTORY);
    }

    public boolean isSecurityPasswordEncryptionEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SECURITY_PASSWORD_ENCRYPTON_ENABLED));
    }

    public boolean isSensitiveDataEncryptionEnabled() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SECURITY_SENSITIVE_DATA_ENCRYPTON_ENABLED));
    }

    public boolean shouldEncryptSensitiveData() {
        return isSecurityPasswordEncryptionEnabled() && isSensitiveDataEncryptionEnabled();
    }

    public int getDefaultMaxParallelismForUpgrades() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.DEFAULT_MAX_DEGREE_OF_PARALLELISM_FOR_UPGRADES));
    }

    public int getKerberosServerActionThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_SERVER_ACTION_THREADPOOL_SIZE));
    }

    public int getAgentCommandPublisherThreadPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.AGENT_COMMAND_PUBLISHER_THREADPOOL_SIZE));
    }

    public int getDefaultForkJoinPoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.DEFAULT_FORK_JOIN_THREADPOOL_SIZE));
    }

    public int getKerberosServerActionFinalizeTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_SERVER_ACTION_FINALIZE_SECONDS));
    }

    public java.lang.Class<? extends org.apache.ambari.server.topology.addservice.HostGroupStrategy> getAddServiceHostGroupStrategyClass() throws java.lang.ClassNotFoundException {
        return java.lang.Class.forName(getProperty(org.apache.ambari.server.configuration.Configuration.ADD_SERVICE_HOST_GROUP_STRATEGY)).asSubclass(org.apache.ambari.server.topology.addservice.HostGroupStrategy.class);
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        final java.lang.String OUTPUT_ARGUMENT = "output";
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption(org.apache.commons.cli.Option.builder().longOpt(OUTPUT_ARGUMENT).desc("The absolute location of the index.md file to generate").required().type(java.lang.String.class).hasArg().valueSeparator(' ').build());
        org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
        org.apache.commons.cli.CommandLine line = parser.parse(options, args);
        java.lang.String outputFile = ((java.lang.String) (line.getParsedOptionValue(OUTPUT_ARGUMENT)));
        java.util.SortedMap<java.lang.String, java.lang.reflect.Field> sortedFields = new java.util.TreeMap<>();
        java.util.List<java.lang.reflect.Field> fields = new java.util.ArrayList<>(500);
        for (java.lang.reflect.Field field : org.apache.ambari.server.configuration.Configuration.class.getFields()) {
            if (field.getType() != org.apache.ambari.server.configuration.Configuration.ConfigurationProperty.class) {
                continue;
            }
            fields.add(field);
            org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> configurationProperty = ((org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>) (field.get(null)));
            sortedFields.put(configurationProperty.getKey(), field);
        }
        java.lang.StringBuilder allPropertiesBuffer = new java.lang.StringBuilder("| Property Name | Description | Default |");
        allPropertiesBuffer.append(java.lang.System.lineSeparator());
        allPropertiesBuffer.append("| --- | --- | --- |");
        allPropertiesBuffer.append(java.lang.System.lineSeparator());
        for (java.lang.String fieldKey : sortedFields.keySet()) {
            java.lang.reflect.Field field = sortedFields.get(fieldKey);
            org.apache.ambari.annotations.Markdown markdown = field.getAnnotation(org.apache.ambari.annotations.Markdown.class);
            if (null == markdown) {
                org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown configMarkdown = field.getAnnotation(org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown.class);
                markdown = configMarkdown.markdown();
            }
            if (markdown.internal()) {
                continue;
            }
            org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> configurationProperty = ((org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>) (field.get(null)));
            java.lang.String key = configurationProperty.getKey();
            java.lang.Object defaultValue = configurationProperty.getDefaultValue();
            allPropertiesBuffer.append("| ").append(key).append(" | ");
            java.lang.StringBuilder description = new java.lang.StringBuilder(markdown.description());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(markdown.relatedTo())) {
                java.lang.String relatedTo = java.lang.String.format(org.apache.ambari.server.configuration.Configuration.MARKDOWN_RELATED_TO_TEMPLATE, markdown.relatedTo());
                description.append(org.apache.ambari.server.configuration.Configuration.HTML_BREAK_TAG).append(org.apache.ambari.server.configuration.Configuration.HTML_BREAK_TAG).append(relatedTo);
            }
            if (markdown.examples().length > 0) {
                description.append(org.apache.ambari.server.configuration.Configuration.HTML_BREAK_TAG).append(org.apache.ambari.server.configuration.Configuration.HTML_BREAK_TAG);
                description.append("The following are examples of valid values:").append("<ul>");
                for (java.lang.String example : markdown.examples()) {
                    description.append("<li>").append("`").append(example).append("`");
                }
                description.append("</ul>");
            }
            allPropertiesBuffer.append(description);
            allPropertiesBuffer.append(" |");
            if ((null != defaultValue) && org.apache.commons.lang.StringUtils.isNotEmpty(defaultValue.toString())) {
                allPropertiesBuffer.append("`").append(defaultValue).append("`");
            }
            allPropertiesBuffer.append(" | ").append(java.lang.System.lineSeparator());
        }
        java.lang.StringBuilder baselineBuffer = new java.lang.StringBuilder(1024);
        for (org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping grouping : org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping.values()) {
            baselineBuffer.append("#### ").append(grouping);
            baselineBuffer.append(java.lang.System.lineSeparator());
            baselineBuffer.append("| Property Name | ");
            for (org.apache.ambari.server.configuration.Configuration.ClusterSizeType clusterSizeType : org.apache.ambari.server.configuration.Configuration.ClusterSizeType.values()) {
                baselineBuffer.append(clusterSizeType).append(" | ");
            }
            baselineBuffer.append(java.lang.System.lineSeparator());
            baselineBuffer.append("| --- | --- | --- | --- | --- |");
            baselineBuffer.append(java.lang.System.lineSeparator());
            for (java.lang.reflect.Field field : fields) {
                org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown configMarkdown = field.getAnnotation(org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown.class);
                if ((null == configMarkdown) || (configMarkdown.group() != grouping)) {
                    continue;
                }
                org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> configurationProperty = ((org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>) (field.get(null)));
                org.apache.ambari.server.configuration.Configuration.ClusterScale[] scaleValues = configMarkdown.scaleValues();
                java.util.SortedMap<org.apache.ambari.server.configuration.Configuration.ClusterSizeType, java.lang.String> miniSort = new java.util.TreeMap<>();
                for (org.apache.ambari.server.configuration.Configuration.ClusterScale clusterScale : scaleValues) {
                    miniSort.put(clusterScale.clusterSize(), clusterScale.value());
                }
                baselineBuffer.append("| ").append(configurationProperty.getKey()).append(" | ");
                for (org.apache.ambari.server.configuration.Configuration.ClusterSizeType clusterSizeType : miniSort.keySet()) {
                    baselineBuffer.append(miniSort.get(clusterSizeType)).append(" | ");
                }
                baselineBuffer.append(java.lang.System.lineSeparator());
            }
            baselineBuffer.append(java.lang.System.lineSeparator());
        }
        java.io.InputStream inputStream = null;
        try {
            if (java.lang.System.getProperties().containsKey(org.apache.ambari.server.configuration.Configuration.AMBARI_CONFIGURATION_MD_TEMPLATE_PROPERTY)) {
                inputStream = new java.io.FileInputStream(java.lang.System.getProperties().getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_CONFIGURATION_MD_TEMPLATE_PROPERTY));
            } else {
                inputStream = org.apache.ambari.server.configuration.Configuration.class.getResourceAsStream(org.apache.ambari.server.configuration.Configuration.MARKDOWN_TEMPLATE_FILE);
            }
            java.lang.String template = org.apache.commons.io.IOUtils.toString(inputStream);
            java.lang.String markdown = template.replace(org.apache.ambari.server.configuration.Configuration.MARKDOWN_CONFIGURATION_TABLE_KEY, allPropertiesBuffer.toString());
            markdown = markdown.replace(org.apache.ambari.server.configuration.Configuration.MARKDOWN_BASELINE_VALUES_KEY, baselineBuffer.toString());
            java.io.File file = new java.io.File(outputFile);
            org.apache.commons.io.FileUtils.writeStringToFile(file, markdown, java.nio.charset.Charset.defaultCharset());
            java.lang.System.out.println("Successfully created " + outputFile);
            org.apache.ambari.server.configuration.Configuration.LOG.info("Successfully created {}", outputFile);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(inputStream);
        }
    }

    public static class ConfigurationProperty<T> implements java.lang.Comparable<org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>> {
        private final java.lang.String m_key;

        private final T m_defaultValue;

        private ConfigurationProperty(java.lang.String key, T defaultValue) {
            m_key = key;
            m_defaultValue = defaultValue;
        }

        public java.lang.String getKey() {
            return m_key;
        }

        public T getDefaultValue() {
            return m_defaultValue;
        }

        @java.lang.Override
        public int hashCode() {
            return m_key.hashCode();
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
            org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> other = ((org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>) (obj));
            return org.apache.commons.lang.StringUtils.equals(this.m_key, other.m_key);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return m_key;
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> o) {
            return this.m_key.compareTo(o.m_key);
        }
    }

    private enum ConfigurationGrouping {

        ALERTS("Alerts & Notifications"),
        JETTY_THREAD_POOL("Jetty API & Agent Thread Pools");
        private java.lang.String m_description;

        ConfigurationGrouping(java.lang.String description) {
            m_description = description;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return m_description;
        }
    }

    private enum ClusterSizeType {

        HOSTS_10("10 Hosts"),
        HOSTS_50("~50 Hosts"),
        HOSTS_100("~100 Hosts"),
        HOSTS_500("500+ Hosts");
        private java.lang.String m_description;

        ClusterSizeType(java.lang.String description) {
            m_description = description;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return m_description;
        }
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD })
    @interface ConfigurationMarkdown {
        org.apache.ambari.annotations.Markdown markdown();

        org.apache.ambari.server.configuration.Configuration.ConfigurationGrouping group();

        org.apache.ambari.server.configuration.Configuration.ClusterScale[] scaleValues() default {  };
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD })
    private @interface ClusterScale {
        org.apache.ambari.server.configuration.Configuration.ClusterSizeType clusterSize();

        java.lang.String value();
    }

    private org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties createKerberosAuthenticationProperties() {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthProperties = new org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties();
        kerberosAuthProperties.setKerberosAuthenticationEnabled(java.lang.Boolean.valueOf(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED)));
        if (!kerberosAuthProperties.isKerberosAuthenticationEnabled()) {
            return kerberosAuthProperties;
        }
        java.lang.String spnegoPrincipalName = getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL);
        if ((spnegoPrincipalName != null) && spnegoPrincipalName.contains("_HOST")) {
            java.lang.String hostName = org.apache.ambari.server.utils.StageUtils.getHostName();
            if (org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
                org.apache.ambari.server.configuration.Configuration.LOG.warn("Cannot replace _HOST in the configured SPNEGO principal name with the host name this host since it is not available");
            } else {
                org.apache.ambari.server.configuration.Configuration.LOG.info("Replacing _HOST in the configured SPNEGO principal name with the host name this host: {}", hostName);
                spnegoPrincipalName = spnegoPrincipalName.replaceAll("_HOST", hostName);
            }
        }
        kerberosAuthProperties.setSpnegoPrincipalName(spnegoPrincipalName);
        if (org.apache.commons.lang.StringUtils.isEmpty(kerberosAuthProperties.getSpnegoPrincipalName())) {
            java.lang.String message = java.lang.String.format("The SPNEGO principal name specified in %s is empty. " + "This will cause issues authenticating users using Kerberos.", org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey());
            org.apache.ambari.server.configuration.Configuration.LOG.error(message);
            throw new java.lang.IllegalArgumentException(message);
        }
        kerberosAuthProperties.setSpnegoKeytabFilePath(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE));
        if (org.apache.commons.lang.StringUtils.isEmpty(kerberosAuthProperties.getSpnegoKeytabFilePath())) {
            java.lang.String message = java.lang.String.format("The SPNEGO keytab file path specified in %s is empty. " + "This will cause issues authenticating users using Kerberos.", org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey());
            org.apache.ambari.server.configuration.Configuration.LOG.error(message);
            throw new java.lang.IllegalArgumentException(message);
        } else {
            java.io.File keytabFile = new java.io.File(kerberosAuthProperties.getSpnegoKeytabFilePath());
            if (!keytabFile.exists()) {
                java.lang.String message = java.lang.String.format("The SPNEGO keytab file path (%s) specified in %s does not exist. " + "This will cause issues authenticating users using Kerberos. . Make sure proper keytab file provided later.", keytabFile.getAbsolutePath(), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey());
                org.apache.ambari.server.configuration.Configuration.LOG.error(message);
            } else if (!keytabFile.canRead()) {
                java.lang.String message = java.lang.String.format("The SPNEGO keytab file path (%s) specified in %s cannot be read. " + "This will cause issues authenticating users using Kerberos. . Make sure proper keytab file provided later.", keytabFile.getAbsolutePath(), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey());
                org.apache.ambari.server.configuration.Configuration.LOG.error(message);
            }
        }
        kerberosAuthProperties.setAuthToLocalRules(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES));
        org.apache.ambari.server.configuration.Configuration.LOG.info("Kerberos authentication is enabled:\n " + ((("\t{}: {}\n" + "\t{}: {}\n") + "\t{}: {}\n") + "\t{}: {}\n"), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), kerberosAuthProperties.isKerberosAuthenticationEnabled(), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey(), kerberosAuthProperties.getSpnegoPrincipalName(), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), kerberosAuthProperties.getSpnegoKeytabFilePath(), org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES.getKey(), kerberosAuthProperties.getAuthToLocalRules());
        return kerberosAuthProperties;
    }

    public int getKerberosOperationRetries() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_OPERATION_RETRIES));
    }

    public int getKerberosOperationRetryTimeout() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_OPERATION_RETRY_TIMEOUT));
    }

    public boolean validateKerberosOperationSSLCertTrust() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.KERBEROS_OPERATION_VERIFY_KDC_TRUST));
    }

    public java.lang.Integer getAgentApiAcceptors() {
        java.lang.String acceptors = getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_ACCEPTOR_THREAD_COUNT);
        return org.apache.commons.lang.StringUtils.isEmpty(acceptors) ? null : java.lang.Integer.parseInt(acceptors);
    }

    public java.lang.Integer getClientApiAcceptors() {
        java.lang.String acceptors = getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_API_ACCEPTOR_THREAD_COUNT);
        return org.apache.commons.lang.StringUtils.isEmpty(acceptors) ? null : java.lang.Integer.parseInt(acceptors);
    }

    public java.lang.String getPamConfigurationFile() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.PAM_CONFIGURATION_FILE);
    }

    public java.lang.String getAutoGroupCreation() {
        return getProperty(org.apache.ambari.server.configuration.Configuration.AUTO_GROUP_CREATION);
    }

    public int getMaxAuthenticationFailures() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.MAX_LOCAL_AUTHENTICATION_FAILURES));
    }

    public boolean showLockedOutUserMessage() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SHOW_LOCKED_OUT_USER_MESSAGE));
    }

    public int getAlertServiceCorePoolSize() {
        return java.lang.Integer.parseInt(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_SIDE_ALERTS_CORE_POOL_SIZE));
    }

    public boolean isServerShowErrorStacks() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_SHOW_ERROR_STACKS));
    }

    public boolean areFileVDFAllowed() {
        return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.configuration.Configuration.VDF_FROM_FILESYSTEM));
    }
}