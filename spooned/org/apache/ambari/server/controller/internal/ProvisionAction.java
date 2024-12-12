package org.apache.ambari.server.controller.internal;
public enum ProvisionAction {

    INSTALL_ONLY() {
        @java.lang.Override
        public java.util.List<org.apache.ambari.server.topology.ProvisionStep> getSteps() {
            return com.google.common.collect.ImmutableList.of(org.apache.ambari.server.topology.ProvisionStep.INSTALL);
        }
    },
    START_ONLY() {
        @java.lang.Override
        public java.util.List<org.apache.ambari.server.topology.ProvisionStep> getSteps() {
            return com.google.common.collect.ImmutableList.of(org.apache.ambari.server.topology.ProvisionStep.SKIP_INSTALL, org.apache.ambari.server.topology.ProvisionStep.START);
        }
    },
    INSTALL_AND_START() {
        @java.lang.Override
        public java.util.List<org.apache.ambari.server.topology.ProvisionStep> getSteps() {
            return com.google.common.collect.ImmutableList.of(org.apache.ambari.server.topology.ProvisionStep.INSTALL, org.apache.ambari.server.topology.ProvisionStep.START);
        }
    };
    public abstract java.util.List<org.apache.ambari.server.topology.ProvisionStep> getSteps();
}