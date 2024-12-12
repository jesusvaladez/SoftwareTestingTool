package org.apache.ambari.view.pig.persistence;
import org.apache.commons.configuration.ConfigurationException;
@java.lang.Deprecated
public class LocalKeyValueStorage extends org.apache.ambari.view.pig.persistence.KeyValueStorage {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.persistence.LocalKeyValueStorage.class);

    private org.apache.ambari.view.pig.persistence.PersistentConfiguration config = null;

    public LocalKeyValueStorage(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    @java.lang.Override
    protected synchronized org.apache.ambari.view.pig.persistence.PersistentConfiguration getConfig() {
        if (config == null) {
            java.lang.String fileName = context.getProperties().get("dataworker.storagePath");
            if (fileName == null) {
                java.lang.String msg = "dataworker.storagePath is not configured!";
                org.apache.ambari.view.pig.persistence.LocalKeyValueStorage.LOG.error(msg);
                throw new org.apache.ambari.view.pig.utils.MisconfigurationFormattedException("dataworker.storagePath");
            }
            try {
                config = new org.apache.ambari.view.pig.persistence.PersistentConfiguration(fileName);
            } catch (org.apache.commons.configuration.ConfigurationException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}