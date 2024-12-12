package org.apache.ambari.server.api.services.serializers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
public class CsvSerializer implements org.apache.ambari.server.api.services.serializers.ResultSerializer {
    public static final java.lang.String PROPERTY_COLUMN_MAP = "csv_column_map";

    public static final java.lang.String PROPERTY_COLUMN_ORDER = "csv_column_order";

    @java.lang.Override
    public java.lang.Object serialize(org.apache.ambari.server.api.services.Result result) {
        if (result.getStatus().isErrorState()) {
            return serializeError(result.getStatus());
        } else {
            org.apache.commons.csv.CSVPrinter csvPrinter = null;
            try {
                java.lang.StringBuffer buffer = new java.lang.StringBuffer();
                org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> root = result.getResultTree();
                if (root != null) {
                    csvPrinter = new org.apache.commons.csv.CSVPrinter(buffer, org.apache.commons.csv.CSVFormat.DEFAULT);
                    if ("true".equalsIgnoreCase(root.getStringProperty("isCollection"))) {
                        java.util.List<java.lang.String> fieldNameOrder = processHeader(csvPrinter, root);
                        java.util.Collection<org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource>> children = root.getChildren();
                        if (children != null) {
                            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child : children) {
                                processRecord(csvPrinter, child, fieldNameOrder);
                            }
                        }
                    }
                }
                return buffer.toString();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException("Unable to serialize to csv: " + e, e);
            } finally {
                org.apache.ambari.server.utils.Closeables.closeSilently(csvPrinter);
            }
        }
    }

    @java.lang.Override
    public java.lang.Object serializeError(org.apache.ambari.server.api.services.ResultStatus error) {
        org.apache.commons.csv.CSVPrinter csvPrinter = null;
        try {
            java.lang.StringBuffer buffer = new java.lang.StringBuffer();
            csvPrinter = new org.apache.commons.csv.CSVPrinter(buffer, org.apache.commons.csv.CSVFormat.DEFAULT);
            csvPrinter.printRecord(java.util.Arrays.asList("status", "message"));
            csvPrinter.printRecord(java.util.Arrays.asList(error.getStatus().getStatus(), error.getMessage()));
            return buffer.toString();
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Unable to serialize to csv: " + e, e);
        } finally {
            org.apache.ambari.server.utils.Closeables.closeSilently(csvPrinter);
        }
    }

    private void processRecord(org.apache.commons.csv.CSVPrinter csvPrinter, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node, java.util.List<java.lang.String> fieldNameOrder) throws java.io.IOException {
        if (node != null) {
            org.apache.ambari.server.controller.spi.Resource recordResource = node.getObject();
            if (recordResource != null) {
                java.util.List<java.lang.Object> values = new java.util.ArrayList<>();
                if (fieldNameOrder != null) {
                    for (java.lang.String fieldName : fieldNameOrder) {
                        values.add(recordResource.getPropertyValue(fieldName));
                    }
                } else {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = recordResource.getPropertiesMap();
                    if (properties != null) {
                        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> outer : properties.entrySet()) {
                            java.util.Map<java.lang.String, java.lang.Object> innerProperties = outer.getValue();
                            if (innerProperties != null) {
                                for (java.util.Map.Entry<java.lang.String, java.lang.Object> inner : innerProperties.entrySet()) {
                                    values.add(inner.getValue());
                                }
                            }
                        }
                    }
                }
                if (!values.isEmpty()) {
                    csvPrinter.printRecord(values);
                }
            }
        }
    }

    private java.util.List<java.lang.String> processHeader(org.apache.commons.csv.CSVPrinter csvPrinter, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) throws java.io.IOException {
        java.util.Map<java.lang.String, java.lang.String> header;
        java.util.List<java.lang.String> fieldNameOrder;
        java.lang.Object object;
        object = node.getProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_MAP);
        if (object instanceof java.util.Map) {
            header = ((java.util.Map<java.lang.String, java.lang.String>) (object));
        } else {
            header = null;
        }
        object = node.getProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_ORDER);
        if (object instanceof java.util.List) {
            fieldNameOrder = ((java.util.List<java.lang.String>) (object));
        } else if (header != null) {
            fieldNameOrder = new java.util.ArrayList<>(header.keySet());
        } else {
            fieldNameOrder = null;
        }
        if (header != null) {
            java.util.List<java.lang.String> headerNames = new java.util.ArrayList<>();
            for (java.lang.String fieldName : fieldNameOrder) {
                headerNames.add(header.get(fieldName));
            }
            csvPrinter.printRecord(headerNames);
        }
        return fieldNameOrder;
    }
}