package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class AuthResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider implements org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator {
    @com.google.inject.assistedinject.AssistedInject
    AuthResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Auth, java.util.Collections.emptySet(), java.util.Collections.emptyMap(), managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return getRequestStatus(null);
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource) {
        return true;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>();
    }
}