package org.apache.ambari.server.orm.entities;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.AlertNoticeEntity.class)
public class AlertNoticeEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertNoticeEntity, java.lang.Long> notificationId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertNoticeEntity, org.apache.ambari.server.state.NotificationState> notifyState;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertNoticeEntity, java.lang.String> uuid;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertNoticeEntity, org.apache.ambari.server.orm.entities.AlertNoticeEntity> alertHistory;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertNoticeEntity, org.apache.ambari.server.orm.entities.AlertTargetEntity> alertTarget;

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> getPredicateMapping() {
        java.util.Map<java.lang.String, java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>>> mapping = new java.util.HashMap<>();
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.notificationId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.notifyState));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_UUID, java.util.Collections.singletonList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.uuid));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_ID, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.alertTarget, org.apache.ambari.server.orm.entities.AlertTargetEntity_.targetId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.alertTarget, org.apache.ambari.server.orm.entities.AlertTargetEntity_.targetName));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_HISTORY_ID, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.alertHistory, org.apache.ambari.server.orm.entities.AlertHistoryEntity_.alertId));
        mapping.put(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME, java.util.Arrays.asList(org.apache.ambari.server.orm.entities.AlertNoticeEntity_.alertHistory, org.apache.ambari.server.orm.entities.AlertHistoryEntity_.serviceName));
        return mapping;
    }
}