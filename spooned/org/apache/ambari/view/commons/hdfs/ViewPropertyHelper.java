package org.apache.ambari.view.commons.hdfs;
public class ViewPropertyHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder.class);

    public static com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> getViewConfigs(org.apache.ambari.view.ViewContext context, java.lang.String viewConfigPropertyName) {
        java.util.Map<java.lang.String, java.lang.String> viewConfigs = new java.util.HashMap<>();
        java.lang.String keyValues = context.getProperties().get(viewConfigPropertyName);
        org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.LOG.debug("{} : {}", viewConfigPropertyName, keyValues);
        if (com.google.common.base.Strings.isNullOrEmpty(keyValues)) {
            org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.LOG.info("No values found in {} property.", viewConfigPropertyName);
            return com.google.common.base.Optional.absent();
        }
        for (java.lang.String entry : keyValues.split(";")) {
            java.lang.String[] kv = entry.split("=");
            if (kv.length != 2) {
                org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.LOG.error("Ignoring entry {}, because it is not formatted like key=value");
                continue;
            }
            viewConfigs.put(kv[0], kv[1]);
        }
        return com.google.common.base.Optional.of(viewConfigs);
    }
}