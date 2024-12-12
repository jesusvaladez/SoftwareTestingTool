package org.apache.ambari.view.pig.templeton.client;
public class TempletonApiFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.templeton.client.TempletonApiFactory.class);

    private org.apache.ambari.view.ViewContext context;

    private org.apache.ambari.view.utils.ambari.AmbariApi ambariApi;

    public TempletonApiFactory(org.apache.ambari.view.ViewContext context) {
        this.context = context;
        this.ambariApi = new org.apache.ambari.view.utils.ambari.AmbariApi(context);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi connectToTempletonApi() {
        java.lang.String webhcatUrl = ambariApi.getServices().getWebHCatURL();
        return new org.apache.ambari.view.pig.templeton.client.TempletonApi(webhcatUrl, getTempletonUser(context), context);
    }

    private java.lang.String getTempletonUser(org.apache.ambari.view.ViewContext context) {
        java.lang.String username = context.getProperties().get("webhcat.username");
        if (((username == null) || (username.compareTo("null") == 0)) || (username.compareTo("") == 0)) {
            username = context.getUsername();
        }
        return username;
    }
}