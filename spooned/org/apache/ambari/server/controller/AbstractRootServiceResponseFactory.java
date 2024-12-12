package org.apache.ambari.server.controller;
public abstract class AbstractRootServiceResponseFactory {
    public abstract java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> getRootServices(org.apache.ambari.server.controller.RootServiceRequest request) throws org.apache.ambari.server.AmbariException;

    public abstract java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> getRootServiceComponents(org.apache.ambari.server.controller.RootServiceComponentRequest request) throws org.apache.ambari.server.AmbariException;

    public abstract java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> getRootServiceHostComponent(org.apache.ambari.server.controller.RootServiceHostComponentRequest request, java.util.Set<org.apache.ambari.server.controller.HostResponse> hosts) throws org.apache.ambari.server.AmbariException;
}