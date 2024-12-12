package org.apache.ambari.server.api.services;
public class ResultImpl implements org.apache.ambari.server.api.services.Result {
    private boolean m_synchronous;

    private org.apache.ambari.server.api.services.ResultStatus m_status;

    private org.apache.ambari.server.api.services.ResultMetadata m_resultMetadata;

    private org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> m_tree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);

    public ResultImpl(boolean synchronous) {
        m_synchronous = synchronous;
    }

    public ResultImpl(org.apache.ambari.server.api.services.ResultStatus status) {
        m_status = status;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> getResultTree() {
        return m_tree;
    }

    @java.lang.Override
    public boolean isSynchronous() {
        return m_synchronous;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultStatus getStatus() {
        return m_status;
    }

    @java.lang.Override
    public void setResultStatus(org.apache.ambari.server.api.services.ResultStatus status) {
        m_status = status;
    }

    @java.lang.Override
    public void setResultMetadata(org.apache.ambari.server.api.services.ResultMetadata resultMetadata) {
        m_resultMetadata = resultMetadata;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultMetadata getResultMetadata() {
        return m_resultMetadata;
    }
}