package org.apache.ambari.view.pig.persistence;
import javax.ws.rs.WebApplicationException;
import org.apache.commons.beanutils.BeanUtils;
public class DataStoreStorage implements org.apache.ambari.view.pig.persistence.Storage {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.persistence.DataStoreStorage.class);

    protected org.apache.ambari.view.ViewContext context;

    public DataStoreStorage(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    @java.lang.Override
    public synchronized void store(org.apache.ambari.view.pig.persistence.utils.Indexed obj) {
        try {
            org.apache.ambari.view.pig.persistence.utils.Indexed newBean = ((org.apache.ambari.view.pig.persistence.utils.Indexed) (org.apache.commons.beanutils.BeanUtils.cloneBean(obj)));
            context.getDataStore().store(newBean);
            obj.setId(newBean.getId());
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error while saving object to DataStorage", e);
        }
    }

    @java.lang.Override
    public synchronized <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> T load(java.lang.Class<T> model, int id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        org.apache.ambari.view.pig.persistence.DataStoreStorage.LOG.debug(java.lang.String.format("Loading %s #%d", model.getName(), id));
        try {
            T obj = context.getDataStore().find(model, java.lang.String.valueOf(id));
            if (obj != null) {
                return obj;
            } else {
                throw new org.apache.ambari.view.pig.persistence.utils.ItemNotFound();
            }
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error while finding object in DataStorage", e);
        }
    }

    @java.lang.Override
    public synchronized <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model, org.apache.ambari.view.pig.persistence.utils.FilteringStrategy filter) {
        java.util.LinkedList<T> list = new java.util.LinkedList<T>();
        org.apache.ambari.view.pig.persistence.DataStoreStorage.LOG.debug(java.lang.String.format("Loading all %s-s", model.getName()));
        try {
            for (T item : context.getDataStore().findAll(model, null)) {
                if ((filter == null) || filter.isConform(item)) {
                    list.add(item);
                }
            }
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error while finding all objects in DataStorage", e);
        }
        return list;
    }

    @java.lang.Override
    public synchronized <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model) {
        return loadAll(model, new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername()));
    }

    @java.lang.Override
    public synchronized void delete(java.lang.Class model, int id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        org.apache.ambari.view.pig.persistence.DataStoreStorage.LOG.debug(java.lang.String.format("Deleting %s:%d", model.getName(), id));
        java.lang.Object obj = load(model, id);
        try {
            context.getDataStore().remove(obj);
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error while removing object from DataStorage", e);
        }
    }

    @java.lang.Override
    public boolean exists(java.lang.Class model, int id) {
        try {
            return context.getDataStore().find(model, java.lang.String.valueOf(id)) != null;
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error while finding object in DataStorage", e);
        }
    }

    public static void storageSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            org.apache.ambari.view.pig.persistence.SmokeTestEntity entity = new org.apache.ambari.view.pig.persistence.SmokeTestEntity();
            entity.setData("42");
            org.apache.ambari.view.pig.persistence.DataStoreStorage storage = new org.apache.ambari.view.pig.persistence.DataStoreStorage(context);
            storage.store(entity);
            if (entity.getId() == null)
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Ambari Views instance data DB doesn't work properly (auto increment id doesn't work)", null);

            int id = java.lang.Integer.parseInt(entity.getId());
            org.apache.ambari.view.pig.persistence.SmokeTestEntity entity2 = storage.load(org.apache.ambari.view.pig.persistence.SmokeTestEntity.class, id);
            boolean status = entity2.getData().compareTo("42") == 0;
            storage.delete(org.apache.ambari.view.pig.persistence.SmokeTestEntity.class, id);
            if (!status)
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Ambari Views instance data DB doesn't work properly", null);

        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }
}