package org.apache.ambari.server.topology;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DO_NOT_SKIP_INSTALL_FOR_COMPONENTS;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.FOR_ALL_COMPONENTS;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.FOR_NO_COMPONENTS;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SKIP_INSTALL_FOR_COMPONENTS;
public enum ProvisionStep {

    INSTALL() {
        @java.lang.Override
        public org.apache.ambari.server.state.State getDesiredStateToSet() {
            return org.apache.ambari.server.state.State.INSTALLED;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.String> getProvisionProperties() {
            return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL);
        }
    },
    SKIP_INSTALL() {
        @java.lang.Override
        public org.apache.ambari.server.state.State getDesiredStateToSet() {
            return org.apache.ambari.server.state.State.INSTALLED;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.String> getProvisionProperties() {
            return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SKIP_INSTALL_FOR_COMPONENTS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.FOR_ALL_COMPONENTS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DO_NOT_SKIP_INSTALL_FOR_COMPONENTS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.FOR_NO_COMPONENTS, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL);
        }
    },
    START() {
        @java.lang.Override
        public org.apache.ambari.server.state.State getDesiredStateToSet() {
            return org.apache.ambari.server.state.State.STARTED;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.String> getProvisionProperties() {
            return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START);
        }
    };
    public abstract org.apache.ambari.server.state.State getDesiredStateToSet();

    public abstract java.util.Map<java.lang.String, java.lang.String> getProvisionProperties();
}