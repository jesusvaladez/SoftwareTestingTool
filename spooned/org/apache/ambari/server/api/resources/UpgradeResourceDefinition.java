package org.apache.ambari.server.api.resources;
public class UpgradeResourceDefinition extends org.apache.ambari.server.api.resources.SimpleResourceDefinition {
    public static final java.lang.String SKIP_SERVICE_CHECKS_DIRECTIVE = "skip_service_checks";

    public UpgradeResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, "upgrade", "upgrades", java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup), java.util.Collections.singletonMap(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE, java.util.Arrays.asList(org.apache.ambari.server.api.resources.UpgradeResourceDefinition.SKIP_SERVICE_CHECKS_DIRECTIVE)));
    }
}