package org.apache.ambari.server.stack.upgrade;
public interface UpgradeFunction {
    org.apache.ambari.server.stack.upgrade.Task.Type getFunction();
}