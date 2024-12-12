package org.apache.ambari.view.pig.resources;
public class SharedCRUDResourceManager<T extends org.apache.ambari.view.pig.persistence.utils.Indexed> extends org.apache.ambari.view.pig.resources.CRUDResourceManager<T> {
    protected org.apache.ambari.view.ViewContext context;

    public SharedCRUDResourceManager(java.lang.Class<T> responseClass, org.apache.ambari.view.ViewContext context) {
        super(responseClass);
        this.context = context;
    }

    @java.lang.Override
    protected boolean checkPermissions(T object) {
        return true;
    }

    @java.lang.Override
    protected org.apache.ambari.view.ViewContext getContext() {
        return context;
    }
}