package org.apache.ambari.server.api.services.serializers;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
public class JsonSerializer implements org.apache.ambari.server.api.services.serializers.ResultSerializer {
    org.codehaus.jackson.JsonFactory m_factory = new org.codehaus.jackson.JsonFactory();

    org.codehaus.jackson.map.ObjectMapper m_mapper = new org.codehaus.jackson.map.ObjectMapper(m_factory);

    org.codehaus.jackson.JsonGenerator m_generator;

    @java.lang.Override
    public java.lang.Object serialize(org.apache.ambari.server.api.services.Result result) {
        try {
            java.io.ByteArrayOutputStream bytesOut = init();
            if (result.getStatus().isErrorState()) {
                return serializeError(result.getStatus());
            }
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> treeNode = result.getResultTree();
            processNode(treeNode);
            processResultMetadata(result.getResultMetadata());
            m_generator.close();
            return bytesOut.toString("UTF-8");
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Unable to serialize to json: " + e, e);
        }
    }

    @java.lang.Override
    public java.lang.Object serializeError(org.apache.ambari.server.api.services.ResultStatus error) {
        try {
            java.io.ByteArrayOutputStream bytesOut = init();
            m_generator.writeStartObject();
            m_generator.writeNumberField("status", error.getStatus().getStatus());
            m_generator.writeStringField("message", error.getMessage());
            m_generator.writeEndObject();
            m_generator.close();
            return bytesOut.toString("UTF-8");
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Unable to serialize to json: " + e, e);
        }
    }

    private java.io.ByteArrayOutputStream init() throws java.io.IOException {
        java.io.ByteArrayOutputStream bytesOut = new java.io.ByteArrayOutputStream();
        m_generator = createJsonGenerator(bytesOut);
        org.codehaus.jackson.util.DefaultPrettyPrinter p = new org.codehaus.jackson.util.DefaultPrettyPrinter();
        p.indentArraysWith(new org.codehaus.jackson.util.DefaultPrettyPrinter.Lf2SpacesIndenter());
        m_generator.setPrettyPrinter(p);
        return bytesOut;
    }

    private void processResultMetadata(org.apache.ambari.server.api.services.ResultMetadata resultMetadata) throws java.io.IOException {
        if (resultMetadata == null) {
            return;
        }
        if (resultMetadata.getClass() == org.apache.ambari.server.api.services.DeleteResultMetadata.class) {
            processResultMetadata(((org.apache.ambari.server.api.services.DeleteResultMetadata) (resultMetadata)));
        } else if (resultMetadata.getClass() == org.apache.ambari.server.controller.internal.OperationStatusMetaData.class) {
            processResultMetadata(((org.apache.ambari.server.controller.internal.OperationStatusMetaData) (resultMetadata)));
        } else {
            throw new java.lang.IllegalArgumentException("ResultDetails is not of type DeleteResultDetails, cannot parse");
        }
    }

    private void processResultMetadata(org.apache.ambari.server.api.services.DeleteResultMetadata deleteResultMetadata) throws java.io.IOException {
        m_generator.writeStartObject();
        m_generator.writeArrayFieldStart("deleteResult");
        for (java.lang.String key : deleteResultMetadata.getDeletedKeys()) {
            m_generator.writeStartObject();
            m_generator.writeObjectFieldStart("deleted");
            m_generator.writeStringField("key", key);
            m_generator.writeEndObject();
            m_generator.writeEndObject();
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.services.ResultStatus> entry : deleteResultMetadata.getExcptions().entrySet()) {
            org.apache.ambari.server.api.services.ResultStatus resultStatus = entry.getValue();
            m_generator.writeStartObject();
            m_generator.writeObjectFieldStart("error");
            m_generator.writeStringField("key", entry.getKey());
            m_generator.writeNumberField("code", resultStatus.getStatusCode());
            m_generator.writeStringField("message", resultStatus.getMessage());
            m_generator.writeEndObject();
            m_generator.writeEndObject();
        }
        m_generator.writeEndArray();
        m_generator.writeEndObject();
    }

    private void processResultMetadata(org.apache.ambari.server.controller.internal.OperationStatusMetaData metaData) throws java.io.IOException {
        m_generator.writeStartObject();
        m_generator.writeObjectFieldStart("operationResults");
        for (org.apache.ambari.server.controller.internal.OperationStatusMetaData.Result result : metaData.getResults()) {
            m_generator.writeObjectFieldStart(result.getId());
            m_generator.writeStringField("status", result.isSuccess() ? "success" : "error");
            if (result.getMessage() != null) {
                m_generator.writeStringField("message", result.getMessage());
            }
            if (result.getResponse() != null) {
                m_generator.writeFieldName("response");
                m_mapper.writeValue(m_generator, result.getResponse());
            }
            m_generator.writeEndObject();
        }
        m_generator.writeEndObject();
        m_generator.writeEndObject();
    }

    private void processNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) throws java.io.IOException {
        if (isObject(node)) {
            m_generator.writeStartObject();
            writeHref(node);
            writeItemCount(node);
            org.apache.ambari.server.controller.spi.Resource r = node.getObject();
            if (r != null) {
                handleResourceProperties(getTreeProperties(r.getPropertiesMap()));
            }
        }
        if (isArray(node)) {
            if (node.getName() != null) {
                m_generator.writeArrayFieldStart(node.getName());
            } else {
                m_generator.writeStartArray();
            }
        }
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child : node.getChildren()) {
            processNode(child);
        }
        if (isArray(node)) {
            m_generator.writeEndArray();
        }
        if (isObject(node)) {
            m_generator.writeEndObject();
        }
    }

    private boolean isObject(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
        return (node.getObject() != null) || ((node.getName() != null) && ((node.getParent() == null) || (!isObject(node.getParent()))));
    }

    private boolean isArray(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
        return ((node.getObject() == null) && (node.getName() != null)) || (((node.getObject() == null) && (node.getName() == null)) && (node.getChildren().size() > 1));
    }

    private org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> getTreeProperties(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertiesMap) {
        org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> treeProperties = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, new java.util.LinkedHashMap<>(), null);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> entry : propertiesMap.entrySet()) {
            java.lang.String category = entry.getKey();
            org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> node;
            if ((category == null) || category.isEmpty()) {
                node = treeProperties;
            } else {
                node = treeProperties.getChild(category);
                if (node == null) {
                    java.lang.String[] tokens = category.split("/");
                    node = treeProperties;
                    for (java.lang.String t : tokens) {
                        org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> child = node.getChild(t);
                        if (child == null) {
                            child = node.addChild(new java.util.LinkedHashMap<>(), t);
                        }
                        node = child;
                    }
                }
            }
            java.util.Map<java.lang.String, java.lang.Object> properties = entry.getValue();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : properties.entrySet()) {
                node.getObject().put(propertyEntry.getKey(), propertyEntry.getValue());
            }
        }
        return treeProperties;
    }

    private void handleResourceProperties(org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> node) throws java.io.IOException {
        java.lang.String category = node.getName();
        if (category != null) {
            m_generator.writeFieldName(category);
            m_generator.writeStartObject();
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : node.getObject().entrySet()) {
            m_generator.writeFieldName(entry.getKey());
            m_mapper.writeValue(m_generator, entry.getValue());
        }
        for (org.apache.ambari.server.api.util.TreeNode<java.util.Map<java.lang.String, java.lang.Object>> n : node.getChildren()) {
            handleResourceProperties(n);
        }
        if (category != null) {
            m_generator.writeEndObject();
        }
    }

    private org.codehaus.jackson.JsonGenerator createJsonGenerator(java.io.ByteArrayOutputStream baos) throws java.io.IOException {
        org.codehaus.jackson.JsonGenerator generator = m_factory.createJsonGenerator(new java.io.OutputStreamWriter(baos, java.nio.charset.Charset.forName("UTF-8").newEncoder()));
        org.codehaus.jackson.util.DefaultPrettyPrinter p = new org.codehaus.jackson.util.DefaultPrettyPrinter();
        p.indentArraysWith(new org.codehaus.jackson.util.DefaultPrettyPrinter.Lf2SpacesIndenter());
        generator.setPrettyPrinter(p);
        return generator;
    }

    private void writeHref(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) throws java.io.IOException {
        java.lang.String hrefProp = node.getStringProperty("href");
        if (hrefProp != null) {
            m_generator.writeStringField("href", hrefProp);
        }
    }

    private void writeItemCount(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) throws java.io.IOException {
        java.lang.String countProp = node.getStringProperty("count");
        if (countProp != null) {
            m_generator.writeStringField("itemTotal", countProp);
            node.setProperty("count", null);
        }
    }
}