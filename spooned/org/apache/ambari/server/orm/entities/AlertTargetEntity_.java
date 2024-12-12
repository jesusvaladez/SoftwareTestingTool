package org.apache.ambari.server.orm.entities;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.AlertTargetEntity.class)
public class AlertTargetEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.Long> targetId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.String> description;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.String> notificationType;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.String> properties;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.String> targetName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.Short> isGlobal;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, java.lang.Short> isEnabled;

    public static volatile javax.persistence.metamodel.SetAttribute<org.apache.ambari.server.orm.entities.AlertTargetEntity, org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups;
}