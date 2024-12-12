package org.apache.ambari.view.pig.resources;
public abstract class CRUDResourceManager<T extends org.apache.ambari.view.pig.persistence.utils.Indexed> {
    private org.apache.ambari.view.pig.persistence.Storage storage = null;

    protected final java.lang.Class<T> resourceClass;

    public CRUDResourceManager(java.lang.Class<T> responseClass) {
        this.resourceClass = responseClass;
    }

    public T create(T object) {
        object.setId(null);
        return this.save(object);
    }

    public T read(java.lang.String id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        T object = null;
        object = getPigStorage().load(this.resourceClass, java.lang.Integer.parseInt(id));
        if (!checkPermissions(object))
            throw new org.apache.ambari.view.pig.persistence.utils.ItemNotFound();

        return object;
    }

    public java.util.List<T> readAll(org.apache.ambari.view.pig.persistence.utils.FilteringStrategy filteringStrategy) {
        return getPigStorage().loadAll(this.resourceClass, filteringStrategy);
    }

    public T update(T newObject, java.lang.String id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        newObject.setId(id);
        this.save(newObject);
        return newObject;
    }

    public void delete(java.lang.String resourceId) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        int id = java.lang.Integer.parseInt(resourceId);
        if (!getPigStorage().exists(this.resourceClass, id)) {
            throw new org.apache.ambari.view.pig.persistence.utils.ItemNotFound();
        }
        getPigStorage().delete(this.resourceClass, id);
    }

    protected T save(T object) {
        getPigStorage().store(object);
        return object;
    }

    protected org.apache.ambari.view.pig.persistence.Storage getPigStorage() {
        if (storage == null) {
            storage = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(getContext()).getStorage();
        }
        return storage;
    }

    protected abstract boolean checkPermissions(T object);

    protected abstract org.apache.ambari.view.ViewContext getContext();
}