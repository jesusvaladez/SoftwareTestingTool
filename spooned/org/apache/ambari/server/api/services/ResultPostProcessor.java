package org.apache.ambari.server.api.services;
public interface ResultPostProcessor {
    void process(org.apache.ambari.server.api.services.Result result);
}