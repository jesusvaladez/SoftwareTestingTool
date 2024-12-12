package org.apache.ambari.view.pig.persistence;
import javax.ws.rs.WebApplicationException;
import org.apache.commons.configuration.Configuration;
@java.lang.Deprecated
public class InstanceKeyValueStorage extends org.apache.ambari.view.pig.persistence.KeyValueStorage {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.class);

    private org.apache.ambari.view.pig.persistence.utils.ContextConfigurationAdapter config = null;

    private int VALUE_LENGTH_LIMIT = 254;

    public InstanceKeyValueStorage(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    @java.lang.Override
    protected synchronized org.apache.commons.configuration.Configuration getConfig() {
        if (config == null) {
            config = new org.apache.ambari.view.pig.persistence.utils.ContextConfigurationAdapter(context);
        }
        return config;
    }

    protected void write(java.lang.String modelPropName, java.lang.String json) {
        int saved = 0;
        int page = 1;
        while (saved < json.length()) {
            int end = java.lang.Math.min(saved + VALUE_LENGTH_LIMIT, json.length());
            java.lang.String substring = json.substring(saved, end);
            getConfig().setProperty((modelPropName + "#") + page, substring);
            saved += VALUE_LENGTH_LIMIT;
            page += 1;
            org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((((("Chunk saved: " + modelPropName) + "#") + page) + "=") + substring);
        } 
        getConfig().setProperty(modelPropName, page - 1);
        org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((("Write finished: " + modelPropName) + " pages:") + (page - 1));
    }

    protected java.lang.String read(java.lang.String modelPropName) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        int pages = getConfig().getInt(modelPropName);
        org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((("Read started: " + modelPropName) + " pages:") + pages);
        for (int page = 1; page <= pages; page++) {
            java.lang.String substring = getConfig().getString((modelPropName + "#") + page);
            org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((((("Chunk read: " + modelPropName) + "#") + page) + "=") + substring);
            if (substring != null) {
                result.append(substring);
            }
        }
        return result.toString();
    }

    protected void clear(java.lang.String modelPropName) {
        int pages = getConfig().getInt(modelPropName);
        org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((("Clean started: " + modelPropName) + " pages:") + pages);
        for (int page = 1; page <= pages; page++) {
            getConfig().clearProperty((modelPropName + "#") + page);
            org.apache.ambari.view.pig.persistence.InstanceKeyValueStorage.LOG.debug((("Chunk clean: " + modelPropName) + "#") + page);
        }
        getConfig().clearProperty(modelPropName);
    }

    public static void storageSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            final java.lang.String property = "test.smoke.property";
            context.putInstanceData(property, "42");
            boolean status = context.getInstanceData(property).equals("42");
            context.removeInstanceData(property);
            if (!status)
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Ambari Views instance data DB doesn't work properly", null);

        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }
}