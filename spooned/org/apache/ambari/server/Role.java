package org.apache.ambari.server;
public class Role {
    private static final java.util.Map<java.lang.String, org.apache.ambari.server.Role> roles = new java.util.concurrent.ConcurrentHashMap<>();

    public static org.apache.ambari.server.Role valueOf(java.lang.String name) {
        if (org.apache.ambari.server.Role.roles.containsKey(name)) {
            return org.apache.ambari.server.Role.roles.get(name);
        }
        org.apache.ambari.server.Role role = new org.apache.ambari.server.Role(name);
        org.apache.ambari.server.Role.roles.put(name, role);
        return role;
    }

    public static java.util.Collection<org.apache.ambari.server.Role> values() {
        return java.util.Collections.unmodifiableCollection(org.apache.ambari.server.Role.roles.values());
    }

    public static final org.apache.ambari.server.Role AMBARI_SERVER_ACTION = org.apache.ambari.server.Role.valueOf("AMBARI_SERVER_ACTION");

    public static final org.apache.ambari.server.Role DATANODE = org.apache.ambari.server.Role.valueOf("DATANODE");

    public static final org.apache.ambari.server.Role FLUME_HANDLER = org.apache.ambari.server.Role.valueOf("FLUME_HANDLER");

    public static final org.apache.ambari.server.Role FLUME_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("FLUME_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role GANGLIA_MONITOR = org.apache.ambari.server.Role.valueOf("GANGLIA_MONITOR");

    public static final org.apache.ambari.server.Role GANGLIA_SERVER = org.apache.ambari.server.Role.valueOf("GANGLIA_SERVER");

    public static final org.apache.ambari.server.Role HBASE_CLIENT = org.apache.ambari.server.Role.valueOf("HBASE_CLIENT");

    public static final org.apache.ambari.server.Role HBASE_MASTER = org.apache.ambari.server.Role.valueOf("HBASE_MASTER");

    public static final org.apache.ambari.server.Role HBASE_REGIONSERVER = org.apache.ambari.server.Role.valueOf("HBASE_REGIONSERVER");

    public static final org.apache.ambari.server.Role HBASE_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("HBASE_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role HCAT = org.apache.ambari.server.Role.valueOf("HCAT");

    public static final org.apache.ambari.server.Role HCAT_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("HCAT_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role GLUSTERFS_CLIENT = org.apache.ambari.server.Role.valueOf("GLUSTERFS_CLIENT");

    public static final org.apache.ambari.server.Role GLUSTERFS_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("GLUSTERFS_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role HDFS_CLIENT = org.apache.ambari.server.Role.valueOf("HDFS_CLIENT");

    public static final org.apache.ambari.server.Role HDFS_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("HDFS_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role HISTORYSERVER = org.apache.ambari.server.Role.valueOf("HISTORYSERVER");

    public static final org.apache.ambari.server.Role HIVE_CLIENT = org.apache.ambari.server.Role.valueOf("HIVE_CLIENT");

    public static final org.apache.ambari.server.Role HIVE_METASTORE = org.apache.ambari.server.Role.valueOf("HIVE_METASTORE");

    public static final org.apache.ambari.server.Role HIVE_SERVER = org.apache.ambari.server.Role.valueOf("HIVE_SERVER");

    public static final org.apache.ambari.server.Role HIVE_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("HIVE_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role JOBTRACKER = org.apache.ambari.server.Role.valueOf("JOBTRACKER");

    public static final org.apache.ambari.server.Role NODEMANAGER = org.apache.ambari.server.Role.valueOf("NODEMANAGER");

    public static final org.apache.ambari.server.Role OOZIE_CLIENT = org.apache.ambari.server.Role.valueOf("OOZIE_CLIENT");

    public static final org.apache.ambari.server.Role OOZIE_SERVER = org.apache.ambari.server.Role.valueOf("OOZIE_SERVER");

    public static final org.apache.ambari.server.Role PEERSTATUS = org.apache.ambari.server.Role.valueOf("PEERSTATUS");

    public static final org.apache.ambari.server.Role PIG = org.apache.ambari.server.Role.valueOf("PIG");

    public static final org.apache.ambari.server.Role PIG_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("PIG_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role MAHOUT = org.apache.ambari.server.Role.valueOf("MAHOUT");

    public static final org.apache.ambari.server.Role MAHOUT_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("MAHOUT_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role RESOURCEMANAGER = org.apache.ambari.server.Role.valueOf("RESOURCEMANAGER");

    public static final org.apache.ambari.server.Role SECONDARY_NAMENODE = org.apache.ambari.server.Role.valueOf("SECONDARY_NAMENODE");

    public static final org.apache.ambari.server.Role SQOOP = org.apache.ambari.server.Role.valueOf("SQOOP");

    public static final org.apache.ambari.server.Role SQOOP_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("SQOOP_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role HUE_SERVER = org.apache.ambari.server.Role.valueOf("HUE_SERVER");

    public static final org.apache.ambari.server.Role JOURNALNODE = org.apache.ambari.server.Role.valueOf("JOURNALNODE");

    public static final org.apache.ambari.server.Role MAPREDUCE_CLIENT = org.apache.ambari.server.Role.valueOf("MAPREDUCE_CLIENT");

    public static final org.apache.ambari.server.Role MAPREDUCE_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("MAPREDUCE_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role MAPREDUCE2_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("MAPREDUCE2_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role MYSQL_SERVER = org.apache.ambari.server.Role.valueOf("MYSQL_SERVER");

    public static final org.apache.ambari.server.Role NAMENODE = org.apache.ambari.server.Role.valueOf("NAMENODE");

    public static final org.apache.ambari.server.Role NAMENODE_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("NAMENODE_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role OOZIE_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("OOZIE_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role TASKTRACKER = org.apache.ambari.server.Role.valueOf("TASKTRACKER");

    public static final org.apache.ambari.server.Role WEBHCAT_SERVER = org.apache.ambari.server.Role.valueOf("WEBHCAT_SERVER");

    public static final org.apache.ambari.server.Role WEBHCAT_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("WEBHCAT_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role YARN_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("YARN_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role ZKFC = org.apache.ambari.server.Role.valueOf("ZKFC");

    public static final org.apache.ambari.server.Role ZOOKEEPER_CLIENT = org.apache.ambari.server.Role.valueOf("ZOOKEEPER_CLIENT");

    public static final org.apache.ambari.server.Role ZOOKEEPER_QUORUM_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("ZOOKEEPER_QUORUM_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role ZOOKEEPER_SERVER = org.apache.ambari.server.Role.valueOf("ZOOKEEPER_SERVER");

    public static final org.apache.ambari.server.Role FALCON_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("FALCON_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role STORM_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("STORM_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role YARN_CLIENT = org.apache.ambari.server.Role.valueOf("YARN_CLIENT");

    public static final org.apache.ambari.server.Role KDC_SERVER = org.apache.ambari.server.Role.valueOf("KDC_SERVER");

    public static final org.apache.ambari.server.Role KERBEROS_CLIENT = org.apache.ambari.server.Role.valueOf("KERBEROS_CLIENT");

    public static final org.apache.ambari.server.Role KERBEROS_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("KERBEROS_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role METRICS_COLLECTOR = org.apache.ambari.server.Role.valueOf("METRICS_COLLECTOR");

    public static final org.apache.ambari.server.Role METRICS_MONITOR = org.apache.ambari.server.Role.valueOf("METRICS_MONITOR");

    public static final org.apache.ambari.server.Role AMS_SERVICE_CHECK = org.apache.ambari.server.Role.valueOf("AMBARI_METRICS_SERVICE_CHECK");

    public static final org.apache.ambari.server.Role ACCUMULO_CLIENT = org.apache.ambari.server.Role.valueOf("ACCUMULO_CLIENT");

    public static final org.apache.ambari.server.Role RANGER_ADMIN = org.apache.ambari.server.Role.valueOf("RANGER_ADMIN");

    public static final org.apache.ambari.server.Role RANGER_USERSYNC = org.apache.ambari.server.Role.valueOf("RANGER_USERSYNC");

    public static final org.apache.ambari.server.Role KNOX_GATEWAY = org.apache.ambari.server.Role.valueOf("KNOX_GATEWAY");

    public static final org.apache.ambari.server.Role KAFKA_BROKER = org.apache.ambari.server.Role.valueOf("KAFKA_BROKER");

    public static final org.apache.ambari.server.Role NIMBUS = org.apache.ambari.server.Role.valueOf("NIMBUS");

    public static final org.apache.ambari.server.Role RANGER_KMS_SERVER = org.apache.ambari.server.Role.valueOf("RANGER_KMS_SERVER");

    public static final org.apache.ambari.server.Role LOGSEARCH_SERVER = org.apache.ambari.server.Role.valueOf("LOGSEARCH_SERVER");

    public static final org.apache.ambari.server.Role INFRA_SOLR = org.apache.ambari.server.Role.valueOf("INFRA_SOLR");

    public static final org.apache.ambari.server.Role LOGSEARCH_LOGFEEDER = org.apache.ambari.server.Role.valueOf("LOGSEARCH_LOGFEEDER");

    public static final org.apache.ambari.server.Role INSTALL_PACKAGES = org.apache.ambari.server.Role.valueOf("install_packages");

    public static final org.apache.ambari.server.Role UPDATE_REPO = org.apache.ambari.server.Role.valueOf("update_repo");

    private java.lang.String name = null;

    private Role(java.lang.String roleName) {
        name = roleName;
    }

    public java.lang.String name() {
        return name;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }

    @java.lang.Override
    public int hashCode() {
        return name.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if ((null == o) || (!org.apache.ambari.server.Role.class.equals(o.getClass()))) {
            return false;
        }
        return (this == o) || name.equals(((org.apache.ambari.server.Role) (o)).name);
    }
}