package org.apache.ambari.server.orm.entities;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class)
public class AlertDefinitionEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.Long> definitionId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> source;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.Long> clusterId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntity;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> componentName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> definitionName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> label;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> helpURL;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, org.apache.ambari.server.state.alert.Scope> scope;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.Integer> enabled;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> hash;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.Integer> scheduleInterval;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, java.lang.String> serviceName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, org.apache.ambari.server.state.alert.SourceType> sourceType;

    public static volatile javax.persistence.metamodel.SetAttribute<org.apache.ambari.server.orm.entities.AlertDefinitionEntity, org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups;
}