package org.apache.ambari.server.controller.jmx;
public class JMXMetricHolderTest {
    private org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics = new org.apache.ambari.server.controller.jmx.JMXMetricHolder();

    @org.junit.Before
    public void setUp() {
        metrics.setBeans(java.util.Arrays.asList(new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("name", "bean1");
                put("value", "val1");
            }
        }, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("name", "bean2");
                put("value", "val2");
            }
        }, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("name", "nested");
                put("value", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("key1", "nested-val1");
                        put("key2", "nested-val2");
                    }
                });
            }
        }));
    }

    @org.junit.Test
    public void testFindSingleBeanByName() throws java.lang.Exception {
        org.junit.Assert.assertThat(metrics.find("bean1/value"), org.hamcrest.core.Is.is(java.util.Optional.of("val1")));
        org.junit.Assert.assertThat(metrics.find("bean2/value"), org.hamcrest.core.Is.is(java.util.Optional.of("val2")));
        org.junit.Assert.assertThat(metrics.find("bean3/notfound"), org.hamcrest.core.Is.is(java.util.Optional.empty()));
    }

    @org.junit.Test
    public void testFindMultipleBeansByName() throws java.lang.Exception {
        java.util.List<java.lang.Object> result = metrics.findAll(java.util.Arrays.asList("bean1/value", "bean2/value", "bean3/notfound"));
        org.junit.Assert.assertThat(result, org.junit.internal.matchers.IsCollectionContaining.hasItems("val1", "val2"));
    }

    @org.junit.Test
    public void testFindNestedBean() throws java.lang.Exception {
        java.util.List<java.lang.Object> result = metrics.findAll(java.util.Arrays.asList("nested/value[key1]", "nested/value[key2]"));
        org.junit.Assert.assertThat(result, org.junit.internal.matchers.IsCollectionContaining.hasItems("nested-val1", "nested-val2"));
    }
}