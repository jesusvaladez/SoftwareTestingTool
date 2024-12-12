package org.apache.ambari.spi.upgrade;
public interface OrchestrationOptions {
    int getConcurrencyCount(org.apache.ambari.spi.ClusterInformation cluster, java.lang.String service, java.lang.String component);
}