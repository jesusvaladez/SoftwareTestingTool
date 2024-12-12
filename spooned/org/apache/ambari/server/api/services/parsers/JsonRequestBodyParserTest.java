package org.apache.ambari.server.api.services.parsers;
public class JsonRequestBodyParserTest {
    java.lang.String serviceJson = "{\"Services\" : {" + ((((((((("    \"display_name\" : \"HDFS\"," + "    \"description\" : \"Apache Hadoop Distributed File System\",") + "    \"service_name\" : \"HDFS\"") + "  },") + "  \"ServiceInfo\" : {") + "    \"cluster_name\" : \"tbmetrictest\",") + "    \"state\" : \"STARTED\"") + "  },") + "\"OuterCategory\" : { \"propName\" : 100, \"nested1\" : { \"nested2\" : { \"innerPropName\" : \"innerPropValue\" } } },") + "\"topLevelProp\" : \"value\" }");

    java.lang.String serviceJsonWithQuery = ("{ \"RequestInfo\" : { \"query\" : \"foo=bar\" }, \"Body\":" + serviceJson) + "}";

    java.lang.String arrayJson = "[ {" + (((((((((((("\"Clusters\" : {\n" + "    \"cluster_name\" : \"unitTestCluster1\"") + "} },") + "{") + "\"Clusters\" : {\n") + "    \"cluster_name\" : \"unitTestCluster2\",") + "    \"property1\" : \"prop1Value\"") + "} },") + "{") + "\"Clusters\" : {\n") + "    \"cluster_name\" : \"unitTestCluster3\",") + "    \"Category\" : { \"property2\" : null}") + "} } ]");

    java.lang.String arrayJson2 = "{" + (((((((((((("\"Clusters\" : {\n" + "    \"cluster_name\" : \"unitTestCluster1\"") + "} },") + "{") + "\"Clusters\" : {\n") + "    \"cluster_name\" : \"unitTestCluster2\",") + "    \"property1\" : \"prop1Value\"") + "} },") + "{") + "\"Clusters\" : {\n") + "    \"cluster_name\" : \"unitTestCluster3\",") + "    \"Category\" : { \"property2\" : \"prop2Value\"}") + "} }");

    java.lang.String multiBody = "[\n" + (((((((((((((((((((((((((((((("  {\n" + "    \"RequestInfo\":{\n") + "      \"query\":\"Hosts/host_name=h1\"\n") + "    },\n") + "    \"Body\":\n") + "    {\n") + "      \"Hosts\": {\n") + "        \"desired_config\": {\n") + "          \"type\": \"global\",\n") + "          \"tag\": \"version20\",\n") + "          \"properties\": { \"a\": \"b\", \"x\": \"y\" }\n") + "        }\n") + "      }\n") + "    }\n") + "  },\n") + "  {\n") + "    \"RequestInfo\":{\n") + "      \"query\":\"Hosts/host_name=h2\"\n") + "    },\n") + "    \"Body\":\n") + "    {\n") + "      \"Hosts\": {\n") + "        \"desired_config\": {\n") + "          \"type\": \"global\",\n") + "          \"tag\": \"version21\",\n") + "          \"properties\": { \"a\": \"c\", \"x\": \"z\" }\n") + "        }\n") + "      }\n") + "    }\n") + "  }\n") + "]\n");

    java.lang.String queryPostJson = "{ \"services\" : [ {" + (((((((((((("\"ServiceInfo\" : {\n" + "    \"service_name\" : \"unitTestService1\"") + "} },") + "{") + "\"ServiceInfo\" : {\n") + "    \"service_name\" : \"unitTestService2\",") + "    \"property1\" : \"prop1Value\"") + "} },") + "{") + "\"ServiceInfo\" : {\n") + "    \"service_name\" : \"unitTestService3\",") + "    \"Category\" : { \"property2\" : \"prop2Value\"}") + "} } ] }");

    java.lang.String queryPostJsonWithQuery = ("{ \"RequestInfo\" : { \"query\" : \"foo=bar\" }, \"Body\":" + queryPostJson) + "}";

    java.lang.String queryPostMultipleSubResourcesJson = "{ \"foo\" : [ {" + ((((((((("\"ServiceInfo\" : {\n" + "    \"service_name\" : \"unitTestService1\"") + "} }") + "],") + " \"bar\" : [") + "{") + "\"ServiceInfo\" : {\n") + "    \"service_name\" : \"unitTestService2\",") + "    \"Category\" : { \"property2\" : \"prop2Value\"}") + "} } ] }");

    java.lang.String bodyQueryOnly = "{ \"RequestInfo\" : { \"query\" : \"foo=bar\" }}";

    java.lang.String malformedJson = "{ \"Category\" : { \"foo\" : \"bar\"}";

    java.lang.String bodyWithRequestInfoProperties = ("{ \"RequestInfo\" : { \"query\" : \"foo=bar\", \"prop1\" : \"val1\", \"prop2\" : \"val2\" }, \"Body\":" + serviceJson) + "}";

    java.lang.String bodyWithRequestBlobProperties = (("{ \"RequestBodyInfo\" : { " + "\"RequestInfo\" : { \"query\" : \"foo=bar\", \"prop1\" : \"val1\", \"prop2\" : \"val2\" }, \"Body\":") + serviceJson) + "} }";

    @org.junit.Test
    public void testParse() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(serviceJson).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps.size());
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>();
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "service_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "display_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name"), "tbmetrictest");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "description"), "Apache Hadoop Distributed File System");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state"), "STARTED");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory", "propName"), "100");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory/nested1/nested2", "innerPropName"), "innerPropValue");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "topLevelProp"), "value");
        org.junit.Assert.assertEquals(mapExpected, setProps.iterator().next().getProperties());
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(mapExpected, setProps2.iterator().next().getProperties());
    }

    @org.junit.Test
    public void testParse_NullBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(null).iterator().next();
        org.junit.Assert.assertNotNull(body.getNamedPropertySets());
        org.junit.Assert.assertEquals(0, body.getNamedPropertySets().size());
        org.junit.Assert.assertNull(body.getQueryString());
        org.junit.Assert.assertNull(body.getPartialResponseFields());
        org.junit.Assert.assertNull(body.getBody());
    }

    @org.junit.Test
    public void testParse_EmptyBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse("").iterator().next();
        org.junit.Assert.assertNotNull(body.getNamedPropertySets());
        org.junit.Assert.assertEquals(0, body.getNamedPropertySets().size());
        org.junit.Assert.assertNull(body.getQueryString());
        org.junit.Assert.assertNull(body.getPartialResponseFields());
        org.junit.Assert.assertNull(body.getBody());
    }

    @org.junit.Test
    public void testParse_MultiBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        java.util.Set<org.apache.ambari.server.api.services.RequestBody> bodySet = parser.parse(multiBody);
        org.junit.Assert.assertEquals(2, bodySet.size());
        for (org.apache.ambari.server.api.services.RequestBody body : bodySet) {
            java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
            org.junit.Assert.assertEquals(1, setProps.size());
            java.util.Map<java.lang.String, java.lang.Object> mapProps = setProps.iterator().next().getProperties();
            org.junit.Assert.assertEquals(4, mapProps.size());
            org.junit.Assert.assertEquals("global", mapProps.get("Hosts/desired_config/type"));
        }
    }

    @org.junit.Test
    public void testParse_Array() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(arrayJson).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(3, setProps.size());
        boolean cluster1Matches = false;
        boolean cluster2Matches = false;
        boolean cluster3Matches = false;
        java.util.Map<java.lang.String, java.lang.String> mapCluster1 = new java.util.HashMap<>();
        mapCluster1.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster1");
        java.util.Map<java.lang.String, java.lang.String> mapCluster2 = new java.util.HashMap<>();
        mapCluster2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster2");
        mapCluster2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "property1"), "prop1Value");
        java.util.Map<java.lang.String, java.lang.String> mapCluster3 = new java.util.HashMap<>();
        mapCluster3.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster3");
        mapCluster3.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters/Category", "property2"), null);
        for (org.apache.ambari.server.api.services.NamedPropertySet propertySet : setProps) {
            org.junit.Assert.assertEquals("", propertySet.getName());
            java.util.Map<java.lang.String, java.lang.Object> mapProps = propertySet.getProperties();
            if (mapProps.equals(mapCluster1)) {
                cluster1Matches = true;
            } else if (mapProps.equals(mapCluster2)) {
                cluster2Matches = true;
            } else if (mapProps.equals(mapCluster3)) {
                cluster3Matches = true;
            }
        }
        org.junit.Assert.assertTrue(cluster1Matches);
        org.junit.Assert.assertTrue(cluster2Matches);
        org.junit.Assert.assertTrue(cluster3Matches);
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(3, setProps2.size());
        org.junit.Assert.assertEquals(setProps, setProps2);
    }

    @org.junit.Test
    public void testParse___Array_NoArrayBrackets() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(arrayJson2).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(3, setProps.size());
        boolean cluster1Matches = false;
        boolean cluster2Matches = false;
        boolean cluster3Matches = false;
        java.util.Map<java.lang.String, java.lang.String> mapCluster1 = new java.util.HashMap<>();
        mapCluster1.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster1");
        java.util.Map<java.lang.String, java.lang.String> mapCluster2 = new java.util.HashMap<>();
        mapCluster2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster2");
        mapCluster2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "property1"), "prop1Value");
        java.util.Map<java.lang.String, java.lang.String> mapCluster3 = new java.util.HashMap<>();
        mapCluster3.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name"), "unitTestCluster3");
        mapCluster3.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters/Category", "property2"), "prop2Value");
        for (org.apache.ambari.server.api.services.NamedPropertySet propertySet : setProps) {
            java.util.Map<java.lang.String, java.lang.Object> mapProps = propertySet.getProperties();
            if (mapProps.equals(mapCluster1)) {
                cluster1Matches = true;
            } else if (mapProps.equals(mapCluster2)) {
                cluster2Matches = true;
            } else if (mapProps.equals(mapCluster3)) {
                cluster3Matches = true;
            }
        }
        org.junit.Assert.assertTrue(cluster1Matches);
        org.junit.Assert.assertTrue(cluster2Matches);
        org.junit.Assert.assertTrue(cluster3Matches);
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(3, setProps2.size());
        org.junit.Assert.assertEquals(setProps, setProps2);
    }

    @org.junit.Test
    public void testParse_QueryInBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(serviceJsonWithQuery).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps.size());
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>();
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "service_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "display_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name"), "tbmetrictest");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "description"), "Apache Hadoop Distributed File System");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state"), "STARTED");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory", "propName"), "100");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory/nested1/nested2", "innerPropName"), "innerPropValue");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "topLevelProp"), "value");
        org.junit.Assert.assertEquals(mapExpected, setProps.iterator().next().getProperties());
        org.junit.Assert.assertEquals("foo=bar", body.getQueryString());
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(mapExpected, setProps2.iterator().next().getProperties());
    }

    @org.junit.Test
    public void testParse_QueryPost() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(queryPostJson).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProperties = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProperties.size());
        boolean contains1 = false;
        boolean contains2 = false;
        boolean contains3 = false;
        for (org.apache.ambari.server.api.services.NamedPropertySet ps : setProperties) {
            java.util.Map<java.lang.String, java.lang.Object> mapProps = ps.getProperties();
            org.junit.Assert.assertEquals(1, mapProps.size());
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> set = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (mapProps.get("services")));
            for (java.util.Map<java.lang.String, java.lang.Object> map : set) {
                java.lang.String serviceName = ((java.lang.String) (map.get("ServiceInfo/service_name")));
                if (serviceName.equals("unitTestService1")) {
                    org.junit.Assert.assertEquals(1, map.size());
                    contains1 = true;
                } else if (serviceName.equals("unitTestService2")) {
                    org.junit.Assert.assertEquals("prop1Value", map.get("ServiceInfo/property1"));
                    org.junit.Assert.assertEquals(2, map.size());
                    contains2 = true;
                } else if (serviceName.equals("unitTestService3")) {
                    org.junit.Assert.assertEquals("prop2Value", map.get("ServiceInfo/Category/property2"));
                    org.junit.Assert.assertEquals(2, map.size());
                    contains3 = true;
                } else {
                    org.junit.Assert.fail("Unexpected service name");
                }
            }
        }
        org.junit.Assert.assertTrue(contains1);
        org.junit.Assert.assertTrue(contains2);
        org.junit.Assert.assertTrue(contains3);
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps2.size());
        org.junit.Assert.assertEquals(setProperties, setProps2);
        org.junit.Assert.assertEquals("java.util.LinkedHashSet", body.getNamedPropertySets().iterator().next().getProperties().get("services").getClass().getName());
    }

    @org.junit.Test
    public void testParse___QueryPost_multipleSubResTypes() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(queryPostMultipleSubResourcesJson).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProperties = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProperties.size());
        boolean contains1 = false;
        boolean contains2 = false;
        for (org.apache.ambari.server.api.services.NamedPropertySet ps : setProperties) {
            java.util.Map<java.lang.String, java.lang.Object> mapProps = ps.getProperties();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : mapProps.entrySet()) {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> set = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (entry.getValue()));
                for (java.util.Map<java.lang.String, java.lang.Object> map : set) {
                    java.lang.String serviceName = ((java.lang.String) (map.get("ServiceInfo/service_name")));
                    if (serviceName.equals("unitTestService1")) {
                        org.junit.Assert.assertEquals("foo", entry.getKey());
                        org.junit.Assert.assertEquals(1, map.size());
                        contains1 = true;
                    } else if (serviceName.equals("unitTestService2")) {
                        org.junit.Assert.assertEquals("bar", entry.getKey());
                        org.junit.Assert.assertEquals("prop2Value", map.get("ServiceInfo/Category/property2"));
                        org.junit.Assert.assertEquals(2, map.size());
                        contains2 = true;
                    } else {
                        org.junit.Assert.fail("Unexpected service name");
                    }
                }
            }
        }
        org.junit.Assert.assertTrue(contains1);
        org.junit.Assert.assertTrue(contains2);
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps2.size());
        org.junit.Assert.assertEquals(setProperties, setProps2);
    }

    @org.junit.Test
    public void testParse___QueryPost_QueryInBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(queryPostJsonWithQuery).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProperties = body.getNamedPropertySets();
        org.junit.Assert.assertEquals("foo=bar", body.getQueryString());
        org.junit.Assert.assertEquals(1, setProperties.size());
        boolean contains1 = false;
        boolean contains2 = false;
        boolean contains3 = false;
        for (org.apache.ambari.server.api.services.NamedPropertySet ps : setProperties) {
            org.junit.Assert.assertEquals("", ps.getName());
            java.util.Map<java.lang.String, java.lang.Object> mapProps = ps.getProperties();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : mapProps.entrySet()) {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> set = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (entry.getValue()));
                for (java.util.Map<java.lang.String, java.lang.Object> map : set) {
                    java.lang.String serviceName = ((java.lang.String) (map.get("ServiceInfo/service_name")));
                    if (serviceName.equals("unitTestService1")) {
                        org.junit.Assert.assertEquals(1, map.size());
                        contains1 = true;
                    } else if (serviceName.equals("unitTestService2")) {
                        org.junit.Assert.assertEquals("prop1Value", map.get("ServiceInfo/property1"));
                        org.junit.Assert.assertEquals(2, map.size());
                        contains2 = true;
                    } else if (serviceName.equals("unitTestService3")) {
                        org.junit.Assert.assertEquals("prop2Value", map.get("ServiceInfo/Category/property2"));
                        org.junit.Assert.assertEquals(2, map.size());
                        contains3 = true;
                    } else {
                        org.junit.Assert.fail("Unexpected service name");
                    }
                }
            }
        }
        org.junit.Assert.assertTrue(contains1);
        org.junit.Assert.assertTrue(contains2);
        org.junit.Assert.assertTrue(contains3);
        java.lang.String b = body.getBody();
        org.junit.Assert.assertEquals(queryPostJsonWithQuery, b);
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps2.size());
        org.junit.Assert.assertEquals(setProperties, setProps2);
    }

    @org.junit.Test
    public void testParse_QueryOnlyInBody() throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(bodyQueryOnly).iterator().next();
        org.junit.Assert.assertEquals("foo=bar", body.getQueryString());
        org.junit.Assert.assertEquals(bodyQueryOnly, body.getBody());
    }

    @org.junit.Test
    public void testParse_malformedBody() {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        try {
            parser.parse(malformedJson);
            org.junit.Assert.fail("Expected exception due to malformed body");
        } catch (org.apache.ambari.server.api.services.parsers.BodyParseException e) {
        }
    }

    @org.junit.Test
    public void testRequestInfoProps() throws java.lang.Exception {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(bodyWithRequestInfoProperties).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps.size());
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>();
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "service_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "display_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name"), "tbmetrictest");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "description"), "Apache Hadoop Distributed File System");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state"), "STARTED");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory", "propName"), "100");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory/nested1/nested2", "innerPropName"), "innerPropValue");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "topLevelProp"), "value");
        org.junit.Assert.assertEquals(mapExpected, setProps.iterator().next().getProperties());
        org.junit.Assert.assertEquals("foo=bar", body.getQueryString());
        java.util.Map<java.lang.String, java.lang.String> mapRequestInfoProps = body.getRequestInfoProperties();
        org.junit.Assert.assertEquals(4, mapRequestInfoProps.size());
        org.junit.Assert.assertEquals("val1", mapRequestInfoProps.get("prop1"));
        org.junit.Assert.assertEquals("val2", mapRequestInfoProps.get("prop2"));
        org.junit.Assert.assertEquals("foo=bar", mapRequestInfoProps.get("query"));
        org.junit.Assert.assertEquals(bodyWithRequestInfoProperties, mapRequestInfoProps.get("RAW_REQUEST_BODY"));
        java.lang.String b = body.getBody();
        body = parser.parse(b).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(mapExpected, setProps2.iterator().next().getProperties());
    }

    @org.junit.Test
    public void testRequestBlobProperties() throws java.lang.Exception {
        org.apache.ambari.server.api.services.parsers.RequestBodyParser parser = new org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser();
        org.apache.ambari.server.api.services.RequestBody body = parser.parse(bodyWithRequestBlobProperties).iterator().next();
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(1, setProps.size());
        java.lang.String requestBlob = null;
        for (org.apache.ambari.server.api.services.NamedPropertySet ps : setProps) {
            org.junit.Assert.assertEquals("", ps.getName());
            java.util.Map<java.lang.String, java.lang.Object> mapProps = ps.getProperties();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : mapProps.entrySet()) {
                if (entry.getKey().equals(org.apache.ambari.server.api.services.parsers.JsonRequestBodyParser.REQUEST_BLOB_TITLE)) {
                    requestBlob = ((java.lang.String) (entry.getValue()));
                }
            }
        }
        junit.framework.Assert.assertNotNull(requestBlob);
        body = parser.parse(requestBlob).iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> mapExpected = new java.util.HashMap<>();
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "service_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "display_name"), "HDFS");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name"), "tbmetrictest");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "description"), "Apache Hadoop Distributed File System");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state"), "STARTED");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory", "propName"), "100");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OuterCategory/nested1/nested2", "innerPropName"), "innerPropValue");
        mapExpected.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "topLevelProp"), "value");
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProps2 = body.getNamedPropertySets();
        org.junit.Assert.assertEquals(mapExpected, setProps2.iterator().next().getProperties());
    }
}