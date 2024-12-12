package org.apache.ambari.server.api.services;
public class ResultPostProcessorImpl implements org.apache.ambari.server.api.services.ResultPostProcessor {
    private org.apache.ambari.server.api.services.Request m_request;

    java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor>> m_mapPostProcessors = new java.util.HashMap<>();

    public ResultPostProcessorImpl(org.apache.ambari.server.api.services.Request request) {
        m_request = request;
        registerResourceProcessors(m_request.getResource());
    }

    @java.lang.Override
    public void process(org.apache.ambari.server.api.services.Result result) {
        java.lang.String href = m_request.getURI();
        int pos = href.indexOf('?');
        if (pos != (-1)) {
            try {
                href = href.substring(0, pos + 1) + java.net.URLDecoder.decode(href.substring(pos + 1), "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                throw new java.lang.RuntimeException("Unable to decode URI: " + e, e);
            }
        }
        processNode(result.getResultTree(), href);
    }

    private void processNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node, java.lang.String href) {
        org.apache.ambari.server.controller.spi.Resource r = node.getObject();
        if (r != null) {
            java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = m_mapPostProcessors.get(r.getType());
            for (org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor processor : listProcessors) {
                processor.process(m_request, node, href);
            }
            href = node.getStringProperty("href");
            int i = href.indexOf('?');
            if (i != (-1)) {
                try {
                    href = java.net.URLDecoder.decode(href.substring(0, i), "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    throw new java.lang.RuntimeException("Unable to decode URI: " + e, e);
                }
            }
        } else {
            java.lang.String isItemsCollection = node.getStringProperty("isCollection");
            if ((node.getName() == null) && "true".equals(isItemsCollection)) {
                node.setName("items");
                node.setProperty("href", href);
            }
        }
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child : node.getChildren()) {
            processNode(child, href);
        }
        finalizeNode(node);
    }

    protected void finalizeNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
    }

    private void registerResourceProcessors(org.apache.ambari.server.api.resources.ResourceInstance resource) {
        org.apache.ambari.server.controller.spi.Resource.Type type = resource.getResourceDefinition().getType();
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = m_mapPostProcessors.get(type);
        if (listProcessors == null) {
            listProcessors = new java.util.ArrayList<>();
            m_mapPostProcessors.put(type, listProcessors);
        }
        listProcessors.addAll(resource.getResourceDefinition().getPostProcessors());
        for (org.apache.ambari.server.api.resources.ResourceInstance child : resource.getSubResources().values()) {
            if (!m_mapPostProcessors.containsKey(child.getResourceDefinition().getType())) {
                registerResourceProcessors(child);
            }
        }
        m_mapPostProcessors.put(org.apache.ambari.server.controller.spi.Resource.Type.Request, new org.apache.ambari.server.api.resources.RequestResourceDefinition().getPostProcessors());
    }
}