package org.apache.ambari.server.orm.entities;
import javax.persistence.metamodel.SingularAttribute;
@javax.persistence.metamodel.StaticMetamodel(org.apache.ambari.server.orm.entities.HostEntity.class)
public class HostEntity_ {
    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.Long> hostId;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> hostName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> ipv4;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> ipv6;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> publicHostName;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.Long> totalMem;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.Integer> cpuCount;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.Integer> phCpuCount;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> cpuInfo;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> osArch;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> osInfo;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> discoveryStatus;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.Long> lastRegistrationTime;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> rackInfo;

    public static volatile javax.persistence.metamodel.SingularAttribute<org.apache.ambari.server.orm.entities.HostEntity, java.lang.String> hostAttributes;
}