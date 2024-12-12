package org.apache.ambari.server.orm.entities;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class)
public class AlertHistoryEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.Long> alertId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> alertInstance;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> alertLabel;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, org.apache.ambari.server.state.AlertState> alertState;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> alertText;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.Long> alertTimestamp;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.Long> clusterId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> componentName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> hostName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, java.lang.String> serviceName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertHistoryEntity, org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinition;

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> getPredicateMapping() {
        java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> mapping = new java.util.HashMap<>();
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.serviceName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_COMPONENT_NAME, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.componentName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_STATE, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertState));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_TIMESTAMP, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertTimestamp));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_HOSTNAME, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.hostName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_LABEL, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertLabel));
        mapping.put(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_NAME, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertDefinition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity_.definitionName));
        return mapping;
    }
}