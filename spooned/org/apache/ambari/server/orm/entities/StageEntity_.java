package org.apache.ambari.server.orm.entities;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.StageEntity.class)
public class StageEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, java.lang.Long> clusterId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, java.lang.Long> requestId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, java.lang.Long> stageId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, java.lang.String> logInfo;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, java.lang.String> requestContext;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, byte[]> commandParamsStage;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, byte[]> hostParamsStage;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, org.apache.ambari.server.orm.entities.RequestEntity> request;

    public static java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, ?>>> getPredicateMapping() {
        java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.StageEntity, ?>>> mapping = new java.util.HashMap<>();
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.requestId));
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STAGE_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.stageId));
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_LOG_INFO, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.logInfo));
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CONTEXT, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.requestContext));
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_COMMAND_PARAMS, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.commandParamsStage));
        mapping.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_HOST_PARAMS, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.StageEntity_.hostParamsStage));
        return mapping;
    }
}