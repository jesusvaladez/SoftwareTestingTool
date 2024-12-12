package org.apache.ambari.server.api.services.serializers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easymock.EasyMockSupport;
public class CsvSerializerTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testSerializeResources_NoColumnInfo() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        java.util.List<java.util.TreeMap<java.lang.String, java.lang.Object>> data = new java.util.ArrayList<java.util.TreeMap<java.lang.String, java.lang.Object>>() {
            {
                add(new java.util.TreeMap<java.lang.String, java.lang.Object>() {
                    {
                        put("property1", "value1a");
                        put("property2", "value2a");
                        put("property3", "value3a");
                        put("property4", "value4a");
                    }
                });
                add(new java.util.TreeMap<java.lang.String, java.lang.Object>() {
                    {
                        put("property1", "value1'b");
                        put("property2", "value2'b");
                        put("property3", "value3'b");
                        put("property4", "value4'b");
                    }
                });
                add(new java.util.TreeMap<java.lang.String, java.lang.Object>() {
                    {
                        put("property1", "value1,c");
                        put("property2", "value2,c");
                        put("property3", "value3,c");
                        put("property4", "value4,c");
                    }
                });
            }
        };
        tree.setName("items");
        tree.setProperty("isCollection", "true");
        addChildResource(tree, "resource", 0, data.get(0));
        addChildResource(tree, "resource", 1, data.get(1));
        addChildResource(tree, "resource", 2, data.get(2));
        replayAll();
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.CsvSerializer().serialize(result).toString().replace("\r", "");
        verifyAll();
        org.junit.Assert.assertNotNull(o);
        java.io.StringReader reader = new java.io.StringReader(o.toString());
        org.apache.commons.csv.CSVParser csvParser = new org.apache.commons.csv.CSVParser(reader, org.apache.commons.csv.CSVFormat.DEFAULT);
        java.util.List<org.apache.commons.csv.CSVRecord> records = csvParser.getRecords();
        org.junit.Assert.assertNotNull(records);
        org.junit.Assert.assertEquals(3, records.size());
        int i = 0;
        for (org.apache.commons.csv.CSVRecord record : records) {
            java.util.TreeMap<java.lang.String, java.lang.Object> actualData = data.get(i++);
            org.junit.Assert.assertEquals(actualData.size(), record.size());
            for (java.lang.String item : record) {
                org.junit.Assert.assertTrue(actualData.containsValue(item));
            }
        }
        csvParser.close();
    }

    @org.junit.Test
    public void testSerializeResources_HeaderInfo() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        tree.setName("items");
        tree.setProperty("isCollection", "true");
        tree.setProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_MAP, new java.util.TreeMap<java.lang.String, java.lang.String>() {
            {
                put("propertyD", "Property D");
                put("propertyC", "Property C");
                put("propertyB", "Property B");
                put("propertyA", "Property A");
            }
        });
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> data = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                add(new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("propertyD", "value1a");
                        put("propertyC", "value2a");
                        put("propertyB", "value3a");
                        put("propertyA", "value4a");
                    }
                });
                add(new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("propertyD", "value1'b");
                        put("propertyC", "value2'b");
                        put("propertyB", "value3'b");
                        put("propertyA", "value4'b");
                    }
                });
                add(new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("propertyD", "value1,c");
                        put("propertyC", "value2,c");
                        put("propertyB", "value3,c");
                        put("propertyA", "value4,c");
                    }
                });
            }
        };
        addChildResource(tree, "resource", 0, data.get(0));
        addChildResource(tree, "resource", 1, data.get(1));
        addChildResource(tree, "resource", 2, data.get(2));
        replayAll();
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.CsvSerializer().serialize(result).toString().replace("\r", "");
        verifyAll();
        java.lang.String expected = "Property A,Property B,Property C,Property D\n" + (("value4a,value3a,value2a,value1a\n" + "value4\'b,value3\'b,value2\'b,value1\'b\n") + "\"value4,c\",\"value3,c\",\"value2,c\",\"value1,c\"\n");
        org.junit.Assert.assertEquals(expected, o);
    }

    @org.junit.Test
    public void testSerializeResources_HeaderOrderInfo() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        result.setResultStatus(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> tree = result.getResultTree();
        tree.setName("items");
        tree.setProperty("isCollection", "true");
        tree.setProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_MAP, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("property1", "Property 1");
                put("property2", "Property 2");
                put("property3", "Property 3");
                put("property4", "Property 4");
            }
        });
        tree.setProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_ORDER, java.util.Arrays.asList("property1", "property2", "property3", "property4"));
        addChildResource(tree, "resource", 0, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("property1", "value1a");
                put("property2", "value2a");
                put("property3", "value3a");
                put("property4", "value4a");
            }
        });
        addChildResource(tree, "resource", 1, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("property1", "value1'b");
                put("property2", "value2'b");
                put("property3", "value3'b");
                put("property4", "value4'b");
            }
        });
        addChildResource(tree, "resource", 2, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("property1", "value1,c");
                put("property2", "value2,c");
                put("property3", "value3,c");
                put("property4", "value4,c");
            }
        });
        replayAll();
        java.lang.Object o = new org.apache.ambari.server.api.services.serializers.CsvSerializer().serialize(result).toString().replace("\r", "");
        java.lang.String expected = "Property 1,Property 2,Property 3,Property 4\n" + (("value1a,value2a,value3a,value4a\n" + "value1\'b,value2\'b,value3\'b,value4\'b\n") + "\"value1,c\",\"value2,c\",\"value3,c\",\"value4,c\"\n");
        org.junit.Assert.assertEquals(expected, o);
        verifyAll();
    }

    private void addChildResource(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parent, java.lang.String name, int index, final java.util.Map<java.lang.String, java.lang.Object> data) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        if (data != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : data.entrySet()) {
                resource.setProperty(entry.getKey(), entry.getValue());
            }
        }
        parent.addChild(resource, java.lang.String.format("%s:%d", name, index));
    }
}