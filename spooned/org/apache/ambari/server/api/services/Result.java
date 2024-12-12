package org.apache.ambari.server.api.services;
public interface Result {
    org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> getResultTree();

    boolean isSynchronous();

    org.apache.ambari.server.api.services.ResultStatus getStatus();

    void setResultStatus(org.apache.ambari.server.api.services.ResultStatus status);

    void setResultMetadata(org.apache.ambari.server.api.services.ResultMetadata resultMetadata);

    org.apache.ambari.server.api.services.ResultMetadata getResultMetadata();
}