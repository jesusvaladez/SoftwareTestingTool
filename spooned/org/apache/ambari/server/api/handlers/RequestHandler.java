package org.apache.ambari.server.api.handlers;
public interface RequestHandler {
    org.apache.ambari.server.api.services.Result handleRequest(org.apache.ambari.server.api.services.Request request);
}