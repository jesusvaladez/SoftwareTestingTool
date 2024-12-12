package org.apache.ambari.server.orm.entities;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class)
public class AlertCurrentEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, java.lang.Long> alertId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, java.lang.Long> latestTimestamp;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, java.lang.Long> originalTimestamp;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, org.apache.ambari.server.state.MaintenanceState> maintenanceState;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, java.lang.String> latestText;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinition;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertCurrentEntity, org.apache.ambari.server.orm.entities.AlertHistoryEntity> alertHistory;

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> getPredicateMapping() {
        java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> mapping = new java.util.HashMap<>();
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LATEST_TIMESTAMP, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.latestTimestamp));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.originalTimestamp));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.maintenanceState));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_TEXT, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.latestText));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertDefinition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity_.definitionId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertDefinition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity_.definitionName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertDefinition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity_.serviceName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_COMPONENT, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertDefinition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity_.componentName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertHistory, org.apache.ambari.server.orm.entities.AlertHistoryEntity_.hostName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertCurrentEntity_.alertHistory, org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertState));
        return mapping;
    }
}