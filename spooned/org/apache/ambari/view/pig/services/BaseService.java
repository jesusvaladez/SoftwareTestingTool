package org.apache.ambari.view.pig.services;
public class BaseService {
    @com.google.inject.Inject
    protected org.apache.ambari.view.ViewContext context;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.services.BaseService.class);

    private org.apache.ambari.view.pig.persistence.Storage storage;

    protected org.apache.ambari.view.pig.persistence.Storage getStorage() {
        if (this.storage == null) {
            storage = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context).getStorage();
        }
        return storage;
    }

    protected void setStorage(org.apache.ambari.view.pig.persistence.Storage storage) {
        this.storage = storage;
    }

    protected org.apache.ambari.view.utils.hdfs.HdfsApi getHdfsApi() {
        return org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context);
    }

    public void setHdfsApi(org.apache.ambari.view.utils.hdfs.HdfsApi api) {
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(api, context);
    }
}