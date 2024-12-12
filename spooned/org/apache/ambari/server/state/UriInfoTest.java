package org.apache.ambari.server.state;
public class UriInfoTest {
    @org.junit.Test
    public void testChoosesHttpByDefault() throws java.lang.Exception {
        org.apache.ambari.server.state.UriInfo uri = new org.apache.ambari.server.state.UriInfo();
        uri.setHttpUri("${config1/http-host}/path");
        org.junit.Assert.assertThat(resolved(uri), org.hamcrest.core.Is.is("http://http-host/path"));
    }

    @org.junit.Test
    public void testChoosesHttpsBasedOnProperties() throws java.lang.Exception {
        org.apache.ambari.server.state.UriInfo uri = new org.apache.ambari.server.state.UriInfo();
        uri.setHttpUri("${config1/http-host}/path");
        uri.setHttpsUri("${config1/https-host}/path");
        uri.setHttpsProperty("${config1/use-http}");
        uri.setHttpsPropertyValue("YES");
        org.junit.Assert.assertThat(resolved(uri), org.hamcrest.core.Is.is("https://https-host/path"));
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config() {
        return new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("config1", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("http-host", "http-host");
                        put("https-host", "https-host");
                        put("use-http", "YES");
                    }
                });
            }
        };
    }

    private java.lang.String resolved(org.apache.ambari.server.state.UriInfo uri) throws org.apache.ambari.server.AmbariException {
        return uri.resolve(config()).toString();
    }
}