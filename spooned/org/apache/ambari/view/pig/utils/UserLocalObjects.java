package org.apache.ambari.view.pig.utils;
public class UserLocalObjects {
    public static final java.lang.String VIEW_CONF_KEYVALUES = "view.conf.keyvalues";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.utils.UserLocalObjects.class);

    private static org.apache.ambari.view.utils.UserLocal<org.apache.ambari.view.utils.hdfs.HdfsApi> hdfsApi;

    private static org.apache.ambari.view.utils.UserLocal<org.apache.ambari.view.pig.templeton.client.TempletonApi> templetonApi;

    static {
        templetonApi = new org.apache.ambari.view.utils.UserLocal<org.apache.ambari.view.pig.templeton.client.TempletonApi>(org.apache.ambari.view.pig.templeton.client.TempletonApi.class) {
            @java.lang.Override
            protected synchronized org.apache.ambari.view.pig.templeton.client.TempletonApi initialValue(org.apache.ambari.view.ViewContext context) {
                org.apache.ambari.view.pig.templeton.client.TempletonApiFactory templetonApiFactory = new org.apache.ambari.view.pig.templeton.client.TempletonApiFactory(context);
                return templetonApiFactory.connectToTempletonApi();
            }
        };
        hdfsApi = new org.apache.ambari.view.utils.UserLocal<org.apache.ambari.view.utils.hdfs.HdfsApi>(org.apache.ambari.view.utils.hdfs.HdfsApi.class) {
            @java.lang.Override
            protected synchronized org.apache.ambari.view.utils.hdfs.HdfsApi initialValue(org.apache.ambari.view.ViewContext context) {
                try {
                    com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.ambari.view.pig.utils.UserLocalObjects.VIEW_CONF_KEYVALUES);
                    org.apache.ambari.view.utils.hdfs.HdfsApi api;
                    if (props.isPresent()) {
                        api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context, props.get());
                    } else {
                        api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context);
                    }
                    return api;
                } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
                    throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(e);
                }
            }
        };
    }

    public static org.apache.ambari.view.utils.hdfs.HdfsApi getHdfsApi(org.apache.ambari.view.ViewContext context) {
        return org.apache.ambari.view.pig.utils.UserLocalObjects.hdfsApi.get(context);
    }

    public static void setHdfsApi(org.apache.ambari.view.utils.hdfs.HdfsApi api, org.apache.ambari.view.ViewContext context) {
        org.apache.ambari.view.pig.utils.UserLocalObjects.hdfsApi.set(api, context);
    }

    public static org.apache.ambari.view.pig.templeton.client.TempletonApi getTempletonApi(org.apache.ambari.view.ViewContext context) {
        return org.apache.ambari.view.pig.utils.UserLocalObjects.templetonApi.get(context);
    }

    public static void setTempletonApi(org.apache.ambari.view.pig.templeton.client.TempletonApi api, org.apache.ambari.view.ViewContext context) {
        org.apache.ambari.view.pig.utils.UserLocalObjects.templetonApi.set(api, context);
    }
}