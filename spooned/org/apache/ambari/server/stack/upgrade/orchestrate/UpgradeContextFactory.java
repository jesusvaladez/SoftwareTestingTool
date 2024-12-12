package org.apache.ambari.server.stack.upgrade.orchestrate;
public interface UpgradeContextFactory {
    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext create(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.Object> upgradeRequestMap) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext create(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity);
}