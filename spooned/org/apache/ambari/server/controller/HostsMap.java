package org.apache.ambari.server.controller;
@com.google.inject.Singleton
public class HostsMap {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.HostsMap.class);

    private java.lang.String hostsMapFile;

    private java.util.Properties hostsMap;

    @com.google.inject.Inject
    public HostsMap(org.apache.ambari.server.configuration.Configuration conf) {
        hostsMapFile = conf.getHostsMapFile();
        setupMap();
    }

    public HostsMap(java.lang.String file) {
        hostsMapFile = file;
    }

    public void setupMap() {
        java.io.InputStream inputStream = null;
        org.apache.ambari.server.controller.HostsMap.LOG.info("Using hostsmap file " + this.hostsMapFile);
        try {
            if (hostsMapFile != null) {
                hostsMap = new java.util.Properties();
                inputStream = new java.io.FileInputStream(new java.io.File(hostsMapFile));
                hostsMap.load(inputStream);
            }
        } catch (java.io.FileNotFoundException fnf) {
            org.apache.ambari.server.controller.HostsMap.LOG.info(("No configuration file " + hostsMapFile) + " found in classpath.", fnf);
        } catch (java.io.IOException ie) {
            throw new java.lang.IllegalArgumentException("Can't read configuration file " + hostsMapFile, ie);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException io) {
                }
            }
        }
    }

    public java.lang.String getHostMap(java.lang.String hostName) {
        if (hostsMapFile == null)
            return hostName;

        return hostsMap.getProperty(hostName, hostName);
    }
}