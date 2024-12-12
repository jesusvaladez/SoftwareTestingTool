package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class VariableReplacementHelperTest {
    private org.apache.ambari.server.state.kerberos.VariableReplacementHelper helper = new org.apache.ambari.server.state.kerberos.VariableReplacementHelper();

    @org.junit.Test
    public void testReplaceVariables() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("global_variable", "Hello World");
                        put("variable-name", "dash");
                        put("variable_name", "underscore");
                        put("variable.name", "dot");
                    }
                });
                put("config_type", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("variable-name", "config_type_dash");
                        put("variable_name", "config_type_underscore");
                        put("variable.name", "config_type_dot");
                    }
                });
                put("config.type", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("variable-name", "config.type_dash");
                        put("variable_name", "config.type_underscore");
                        put("variable.name", "config.type_dot");
                    }
                });
                put("config-type", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("variable.name", "Replacement1");
                        put("variable.name1", "${config-type2/variable.name}");
                        put("variable.name2", "");
                    }
                });
                put("config-type2", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("variable.name", "Replacement2");
                        put("self_reference", "${config-type2/self_reference}");
                        put("${config-type/variable.name}_reference", "Replacement in the key");
                    }
                });
            }
        };
        junit.framework.Assert.assertEquals("concrete", helper.replaceVariables("concrete", configurations));
        junit.framework.Assert.assertEquals("Hello World", helper.replaceVariables("${global_variable}", configurations));
        junit.framework.Assert.assertEquals("Replacement1", helper.replaceVariables("${config-type/variable.name}", configurations));
        junit.framework.Assert.assertEquals("Replacement1|Replacement2", helper.replaceVariables("${config-type/variable.name}|${config-type2/variable.name}", configurations));
        junit.framework.Assert.assertEquals("Replacement1|Replacement2|${config-type3/variable.name}", helper.replaceVariables("${config-type/variable.name}|${config-type2/variable.name}|${config-type3/variable.name}", configurations));
        junit.framework.Assert.assertEquals("Replacement2|Replacement2", helper.replaceVariables("${config-type/variable.name1}|${config-type2/variable.name}", configurations));
        junit.framework.Assert.assertEquals("Replacement1_reference", helper.replaceVariables("${config-type/variable.name}_reference", configurations));
        junit.framework.Assert.assertEquals("dash", helper.replaceVariables("${variable-name}", configurations));
        junit.framework.Assert.assertEquals("underscore", helper.replaceVariables("${variable_name}", configurations));
        junit.framework.Assert.assertEquals("config_type_dot", helper.replaceVariables("${config_type/variable.name}", configurations));
        junit.framework.Assert.assertEquals("config_type_dash", helper.replaceVariables("${config_type/variable-name}", configurations));
        junit.framework.Assert.assertEquals("config_type_underscore", helper.replaceVariables("${config_type/variable_name}", configurations));
        junit.framework.Assert.assertEquals("config.type_dot", helper.replaceVariables("${config.type/variable.name}", configurations));
        junit.framework.Assert.assertEquals("config.type_dash", helper.replaceVariables("${config.type/variable-name}", configurations));
        junit.framework.Assert.assertEquals("config.type_underscore", helper.replaceVariables("${config.type/variable_name}", configurations));
        junit.framework.Assert.assertEquals("dot", helper.replaceVariables("${variable.name}", configurations));
        junit.framework.Assert.assertEquals("", helper.replaceVariables("${config-type/variable.name2}", configurations));
        try {
            junit.framework.Assert.assertEquals("${config-type2/self_reference}", helper.replaceVariables("${config-type2/self_reference}", configurations));
            junit.framework.Assert.fail(java.lang.String.format("%s expected to be thrown", org.apache.ambari.server.AmbariRuntimeException.class.getName()));
        } catch (org.apache.ambari.server.AmbariRuntimeException e) {
        }
    }

    @org.junit.Test
    public void testReplaceVariablesRecursive() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("", new java.util.HashMap<java.lang.String, java.lang.String>());
                put("data", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("data_host1.example.com", "host 1 data");
                        put("data_host2.example.com", "host 2 data");
                        put("data_host3.example.com", "host 3 data");
                    }
                });
            }
        };
        configurations.get("").put("h", "host");
        junit.framework.Assert.assertEquals("${data/data_${host}}", helper.replaceVariables("${data/data_${${h}}}", configurations));
        configurations.get("").put("host", "host.example.com");
        junit.framework.Assert.assertEquals("${data/data_host.example.com}", helper.replaceVariables("${data/data_${${h}}}", configurations));
        for (int i = 1; i <= 3; i++) {
            configurations.get("").put("host", java.lang.String.format("host%d.example.com", i));
            junit.framework.Assert.assertEquals(java.lang.String.format("host %d data", i), helper.replaceVariables("${data/data_${${h}}}", configurations));
        }
    }

    @org.junit.Test
    public void testReplaceComplicatedVariables() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("host", "c6401.ambari.apache.org");
                        put("realm", "EXAMPLE.COM");
                    }
                });
            }
        };
        junit.framework.Assert.assertEquals("hive.metastore.local=false,hive.metastore.uris=thrift://c6401.ambari.apache.org:9083,hive.metastore.sasl.enabled=true,hive.metastore.execute.setugi=true,hive.metastore.warehouse.dir=/apps/hive/warehouse,hive.exec.mode.local.auto=false,hive.metastore.kerberos.principal=hive/_HOST@EXAMPLE.COM", helper.replaceVariables("hive.metastore.local=false,hive.metastore.uris=thrift://${host}:9083,hive.metastore.sasl.enabled=true,hive.metastore.execute.setugi=true,hive.metastore.warehouse.dir=/apps/hive/warehouse,hive.exec.mode.local.auto=false,hive.metastore.kerberos.principal=hive/_HOST@${realm}", configurations));
        junit.framework.Assert.assertEquals("Hello my realm is {EXAMPLE.COM}", helper.replaceVariables("Hello my realm is {${realm}}", configurations));
        junit.framework.Assert.assertEquals("$c6401.ambari.apache.org", helper.replaceVariables("$${host}", configurations));
    }

    @org.junit.Test
    public void testReplaceVariablesWithFunctions() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("delimited.data", "one,two,three,four");
                        put("realm", "UNIT.TEST");
                        put("admin_server_host", "c7401.ambari.apache.org");
                        put("admin_server_host_port", "c7401.ambari.apache.org:8080");
                    }
                });
                put("kafka-broker", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("listeners", "PLAINTEXT://localhost:6667");
                    }
                });
                put("clusterHostInfo", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("hive_metastore_host", "host1.unit.test, host2.unit.test , host3.unit.test");
                    }
                });
                put("foobar-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("data", "one, two, three,    four");
                        put("hello", "hello");
                        put("hello_there", "hello, there");
                        put("hello_there_one", "hello, there, one");
                    }
                });
            }
        };
        junit.framework.Assert.assertEquals("test=thrift://one:9083\\,thrift://two:9083\\,thrift://three:9083\\,thrift://four:9083", helper.replaceVariables("test=${delimited.data|each(thrift://%s:9083, \\\\,, \\s*\\,\\s*)}", configurations));
        junit.framework.Assert.assertEquals("hive.metastore.local=false,hive.metastore.uris=thrift://host1.unit.test:9083\\,thrift://host2.unit.test:9083\\,thrift://host3.unit.test:9083,hive.metastore.sasl.enabled=true,hive.metastore.execute.setugi=true,hive.metastore.warehouse.dir=/apps/hive/warehouse,hive.exec.mode.local.auto=false,hive.metastore.kerberos.principal=hive/_HOST@UNIT.TEST", helper.replaceVariables("hive.metastore.local=false,hive.metastore.uris=${clusterHostInfo/hive_metastore_host | each(thrift://%s:9083, \\\\,, \\s*\\,\\s*)},hive.metastore.sasl.enabled=true,hive.metastore.execute.setugi=true,hive.metastore.warehouse.dir=/apps/hive/warehouse,hive.exec.mode.local.auto=false,hive.metastore.kerberos.principal=hive/_HOST@${realm}", configurations));
        java.util.List<java.lang.String> expected;
        java.util.List<java.lang.String> actual;
        expected = new java.util.LinkedList<>(java.util.Arrays.asList("four", "hello", "one", "three", "two"));
        actual = new java.util.LinkedList<>(java.util.Arrays.asList(helper.replaceVariables("${foobar-site/hello | append(foobar-site/data, \\,, true)}", configurations).split(",")));
        java.util.Collections.sort(expected);
        java.util.Collections.sort(actual);
        junit.framework.Assert.assertEquals(expected, actual);
        expected = new java.util.LinkedList<>(java.util.Arrays.asList("four", "hello", "one", "there", "three", "two"));
        actual = new java.util.LinkedList<>(java.util.Arrays.asList(helper.replaceVariables("${foobar-site/hello_there | append(foobar-site/data, \\,, true)}", configurations).split(",")));
        java.util.Collections.sort(expected);
        java.util.Collections.sort(actual);
        junit.framework.Assert.assertEquals(expected, actual);
        expected = new java.util.LinkedList<>(java.util.Arrays.asList("four", "hello", "one", "there", "three", "two"));
        actual = new java.util.LinkedList<>(java.util.Arrays.asList(helper.replaceVariables("${foobar-site/hello_there_one | append(foobar-site/data, \\,, true)}", configurations).split(",")));
        java.util.Collections.sort(expected);
        java.util.Collections.sort(actual);
        junit.framework.Assert.assertEquals(expected, actual);
        expected = new java.util.LinkedList<>(java.util.Arrays.asList("four", "hello", "one", "one", "there", "three", "two"));
        actual = new java.util.LinkedList<>(java.util.Arrays.asList(helper.replaceVariables("${foobar-site/hello_there_one | append(foobar-site/data, \\,, false)}", configurations).split(",")));
        java.util.Collections.sort(expected);
        java.util.Collections.sort(actual);
        junit.framework.Assert.assertEquals(expected, actual);
        try {
            helper.replaceVariables("${foobar-site/hello_there_one | append(foobar-site/data, \\,)}", configurations);
            junit.framework.Assert.fail("Expected IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException e) {
        }
        junit.framework.Assert.assertEquals("test=unit.test", helper.replaceVariables("test=${realm|toLower()}", configurations));
        junit.framework.Assert.assertEquals("PLAINTEXTSASL://localhost:6667", helper.replaceVariables("${kafka-broker/listeners|replace(\\bPLAINTEXT\\b,PLAINTEXTSASL)}", configurations));
        junit.framework.Assert.assertEquals("kadmin/c7401.ambari.apache.org", helper.replaceVariables("kadmin/${admin_server_host|stripPort()}", configurations));
        junit.framework.Assert.assertEquals("kadmin/c7401.ambari.apache.org", helper.replaceVariables("kadmin/${admin_server_host_port|stripPort()}", configurations));
    }

    @org.junit.Test
    public void testReplacePrincipalWithPrimary() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("principals", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("resource_manager_rm", "rm/HOST@EXAMPLE.COM");
                        put("hive_server_hive", "hive@EXAMPLE.COM");
                        put("hdfs", "hdfs");
                    }
                });
            }
        };
        junit.framework.Assert.assertEquals("hdfs", helper.replaceVariables("${principals/hdfs|principalPrimary()}", config));
        junit.framework.Assert.assertEquals("rm", helper.replaceVariables("${principals/resource_manager_rm|principalPrimary()}", config));
        junit.framework.Assert.assertEquals("hive", helper.replaceVariables("${principals/hive_server_hive|principalPrimary()}", config));
    }
}