package org.apache.ambari.server.api.services;
import javax.ws.rs.Path;
@javax.ws.rs.Path("/privileges/")
public class AmbariPrivilegeService extends org.apache.ambari.server.api.services.PrivilegeService {
    @java.lang.Override
    protected org.apache.ambari.server.api.resources.ResourceInstance createPrivilegeResource(java.lang.String privilegeId) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege, privilegeId));
    }
}