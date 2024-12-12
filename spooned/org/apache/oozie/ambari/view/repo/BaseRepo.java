package org.apache.oozie.ambari.view.repo;
public class BaseRepo<T> {
    protected final org.apache.ambari.view.DataStore dataStore;

    private final java.lang.Class type;

    public BaseRepo(java.lang.Class type, org.apache.ambari.view.DataStore dataStore) {
        this.type = type;
        this.dataStore = dataStore;
    }

    public java.lang.String generateId() {
        return java.util.UUID.randomUUID().toString();
    }

    public java.util.Collection<T> findAll() {
        try {
            return dataStore.findAll(type, null);
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public T findById(java.lang.String id) {
        try {
            return ((T) (dataStore.find(type, id)));
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public T create(T obj) {
        try {
            if (obj instanceof org.apache.oozie.ambari.view.model.Indexed) {
                org.apache.oozie.ambari.view.model.Indexed idxObj = ((org.apache.oozie.ambari.view.model.Indexed) (obj));
                if (idxObj.getId() == null) {
                    idxObj.setId(this.generateId());
                } else {
                    T findById = findById(idxObj.getId());
                    if (findById != null) {
                        throw new java.lang.RuntimeException("Object already exist in db");
                    }
                }
            }
            if (obj instanceof org.apache.oozie.ambari.view.model.When) {
                java.util.Date now = new java.util.Date();
                org.apache.oozie.ambari.view.model.When when = ((org.apache.oozie.ambari.view.model.When) (obj));
                when.setCreatedAt(java.lang.String.valueOf(now.getTime()));
                when.setUpdatedAt(java.lang.String.valueOf(now.getTime()));
            }
            this.dataStore.store(obj);
            return obj;
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void update(T obj) {
        try {
            if (obj instanceof org.apache.oozie.ambari.view.model.When) {
                java.util.Date now = new java.util.Date();
                org.apache.oozie.ambari.view.model.When when = ((org.apache.oozie.ambari.view.model.When) (obj));
                when.setUpdatedAt(java.lang.String.valueOf(now.getTime()));
            }
            this.dataStore.store(obj);
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void delete(T obj) {
        try {
            this.dataStore.remove(obj);
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void deleteById(java.lang.String id) {
        try {
            T findById = this.findById(id);
            this.dataStore.remove(findById);
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}