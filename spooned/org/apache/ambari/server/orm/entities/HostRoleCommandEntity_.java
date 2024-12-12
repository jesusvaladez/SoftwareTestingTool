package org.apache.ambari.server.orm.entities;
import javax.persistence.metamodel.SingularAttribute;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class)
public class HostRoleCommandEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> taskId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> requestId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> stageId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> hostId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> role;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> event;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Integer> exitcode;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> status;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, byte[]> stdError;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, byte[]> stdOut;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> outputLog;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> errorLog;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, byte[]> structuredOut;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> startTime;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> endTime;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Long> lastAttemptTime;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.Short> attemptCount;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> roleCommand;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> commandDetail;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, java.lang.String> customCommandName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, org.apache.ambari.server.orm.entities.HostEntity> host;

    public static java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> getPredicateMapping() {
        java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> mapping = new java.util.HashMap<>();
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.taskId));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.requestId));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.stageId));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_HOST_NAME_PROPERTY_ID, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.host, org.apache.ambari.server.orm.entities.HostEntity_.hostId));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ROLE_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.role));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_EXIT_CODE_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.exitcode));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STATUS_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.status));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_START_TIME_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.startTime));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_END_TIME_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.endTime));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ATTEMPT_CNT_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.attemptCount));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.roleCommand));
        mapping.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CUST_CMD_NAME_PROPERTY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.customCommandName));
        return mapping;
    }
}