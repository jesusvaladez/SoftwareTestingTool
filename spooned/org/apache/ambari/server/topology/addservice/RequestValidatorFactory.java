package org.apache.ambari.server.topology.addservice;
public interface RequestValidatorFactory {
    org.apache.ambari.server.topology.addservice.RequestValidator create(org.apache.ambari.server.topology.addservice.AddServiceRequest request, org.apache.ambari.server.state.Cluster cluster);
}