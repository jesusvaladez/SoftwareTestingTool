package org.apache.ambari.view.pig.persistence.utils;
public class StorageUtil {
    private org.apache.ambari.view.pig.persistence.Storage storageInstance = null;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.persistence.utils.StorageUtil.class);

    private static java.util.Map<java.lang.String, org.apache.ambari.view.pig.persistence.utils.StorageUtil> viewSingletonObjects = new java.util.HashMap<java.lang.String, org.apache.ambari.view.pig.persistence.utils.StorageUtil>();

    public static org.apache.ambari.view.pig.persistence.utils.StorageUtil getInstance(org.apache.ambari.view.ViewContext context) {
        if (!org.apache.ambari.view.pig.persistence.utils.StorageUtil.viewSingletonObjects.containsKey(context.getInstanceName()))
            org.apache.ambari.view.pig.persistence.utils.StorageUtil.viewSingletonObjects.put(context.getInstanceName(), new org.apache.ambari.view.pig.persistence.utils.StorageUtil(context));

        return org.apache.ambari.view.pig.persistence.utils.StorageUtil.viewSingletonObjects.get(context.getInstanceName());
    }

    public static void dropAllConnections() {
        org.apache.ambari.view.pig.persistence.utils.StorageUtil.viewSingletonObjects.clear();
    }

    private org.apache.ambari.view.ViewContext context;

    public StorageUtil(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    public synchronized org.apache.ambari.view.pig.persistence.Storage getStorage() {
        if (storageInstance == null) {
            java.lang.String fileName = context.getProperties().get("dataworker.storagePath");
            if (fileName != null) {
                org.apache.ambari.view.pig.persistence.utils.StorageUtil.LOG.debug(("Using local storage in " + fileName) + " to store data");
                storageInstance = new org.apache.ambari.view.pig.persistence.LocalKeyValueStorage(context);
            } else {
                org.apache.ambari.view.pig.persistence.utils.StorageUtil.LOG.debug("Using Persistence API to store data");
                storageInstance = new org.apache.ambari.view.pig.persistence.DataStoreStorage(context);
            }
        }
        return storageInstance;
    }

    public void setStorage(org.apache.ambari.view.pig.persistence.Storage storage) {
        storageInstance = storage;
    }
}