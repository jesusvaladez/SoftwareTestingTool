package org.apache.ambari.server;
import com.google.inject.persist.PersistService;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
public class H2DatabaseCleaner {
    private static final java.lang.String SEQ_STATEMENT = "INSERT INTO ambari_sequences(sequence_name, sequence_value) values (?, 0);";

    private static java.util.List<java.lang.String> sequenceList = new java.util.ArrayList<>();

    static {
        sequenceList.add("extension_id_seq");
        sequenceList.add("resource_id_seq");
        sequenceList.add("alert_target_id_seq");
        sequenceList.add("topology_request_id_seq");
        sequenceList.add("setting_id_seq");
        sequenceList.add("principal_type_id_seq");
        sequenceList.add("group_id_seq");
        sequenceList.add("remote_cluster_id_seq");
        sequenceList.add("privilege_id_seq");
        sequenceList.add("servicecomponent_history_id_seq");
        sequenceList.add("permission_id_seq");
        sequenceList.add("principal_id_seq");
        sequenceList.add("repo_version_id_seq");
        sequenceList.add("cluster_version_id_seq");
        sequenceList.add("topology_host_task_id_seq");
        sequenceList.add("topology_logical_task_id_seq");
        sequenceList.add("host_id_seq");
        sequenceList.add("servicecomponentdesiredstate_id_seq");
        sequenceList.add("configgroup_id_seq");
        sequenceList.add("topology_host_group_id_seq");
        sequenceList.add("upgrade_item_id_seq");
        sequenceList.add("requestschedule_id_seq");
        sequenceList.add("blueprint_setting_id_seq");
        sequenceList.add("host_version_id_seq");
        sequenceList.add("hostcomponentstate_id_seq");
        sequenceList.add("cluster_id_seq");
        sequenceList.add("view_instance_id_seq");
        sequenceList.add("resourcefilter_id_seq");
        sequenceList.add("alert_group_id_seq");
        sequenceList.add("link_id_seq");
        sequenceList.add("topology_host_info_id_seq");
        sequenceList.add("viewentity_id_seq");
        sequenceList.add("alert_notice_id_seq");
        sequenceList.add("user_id_seq");
        sequenceList.add("upgrade_id_seq");
        sequenceList.add("stack_id_seq");
        sequenceList.add("alert_current_id_seq");
        sequenceList.add("widget_id_seq");
        sequenceList.add("remote_cluster_service_id_seq");
        sequenceList.add("alert_history_id_seq");
        sequenceList.add("config_id_seq");
        sequenceList.add("upgrade_group_id_seq");
        sequenceList.add("member_id_seq");
        sequenceList.add("service_config_id_seq");
        sequenceList.add("widget_layout_id_seq");
        sequenceList.add("hostcomponentdesiredstate_id_seq");
        sequenceList.add("operation_level_id_seq");
        sequenceList.add("servicecomponent_version_id_seq");
        sequenceList.add("host_role_command_id_seq");
        sequenceList.add("alert_definition_id_seq");
        sequenceList.add("resource_type_id_seq");
    }

    public static void clearDatabaseAndStopPersistenceService(com.google.inject.Injector injector) throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(injector.getProvider(javax.persistence.EntityManager.class).get());
        injector.getInstance(com.google.inject.persist.PersistService.class).stop();
    }

    public static void clearDatabase(javax.persistence.EntityManager entityManager) throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(entityManager, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_USER, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_PASSWORD);
    }

    public static void resetSequences(com.google.inject.Injector injector) {
        org.apache.ambari.server.orm.DBAccessorImpl dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessorImpl.class);
        try {
            if (dbAccessor.tableExists("ambari_sequences")) {
                dbAccessor.truncateTable("ambari_sequences");
                java.sql.PreparedStatement preparedStatement = dbAccessor.getConnection().prepareStatement(org.apache.ambari.server.H2DatabaseCleaner.SEQ_STATEMENT);
                try {
                    for (java.lang.String sequenceName : org.apache.ambari.server.H2DatabaseCleaner.sequenceList) {
                        preparedStatement.setString(1, sequenceName);
                        preparedStatement.executeUpdate();
                    }
                } finally {
                    preparedStatement.close();
                }
            }
        } catch (java.sql.SQLException ignored) {
        }
    }

    public static void clearDatabase(javax.persistence.EntityManager entityManager, java.lang.String dbURL, java.lang.String dbUser, java.lang.String dbPass) throws java.sql.SQLException {
        java.sql.Connection connection = java.sql.DriverManager.getConnection(dbURL, dbUser, dbPass);
        java.sql.Statement s = connection.createStatement();
        try {
            s.execute("SET REFERENTIAL_INTEGRITY FALSE");
            entityManager.getTransaction().begin();
            for (javax.persistence.metamodel.EntityType<?> entity : entityManager.getMetamodel().getEntities()) {
                javax.persistence.Query query = entityManager.createQuery(("DELETE FROM " + entity.getName()) + " em");
                query.executeUpdate();
            }
            entityManager.getTransaction().commit();
            s.execute("SET REFERENTIAL_INTEGRITY TRUE");
            entityManager.getEntityManagerFactory().getCache().evictAll();
        } finally {
            s.close();
            connection.close();
        }
    }
}