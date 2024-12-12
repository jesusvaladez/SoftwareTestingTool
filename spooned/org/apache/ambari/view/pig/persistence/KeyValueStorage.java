package org.apache.ambari.view.pig.persistence;
import org.apache.commons.configuration.Configuration;
public abstract class KeyValueStorage implements org.apache.ambari.view.pig.persistence.Storage {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.persistence.KeyValueStorage.class);

    protected final com.google.gson.Gson gson = new com.google.gson.Gson();

    protected org.apache.ambari.view.ViewContext context;

    public KeyValueStorage(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    protected abstract org.apache.commons.configuration.Configuration getConfig();

    @java.lang.Override
    public synchronized void store(org.apache.ambari.view.pig.persistence.utils.Indexed obj) {
        java.lang.String modelIndexingPropName = getIndexPropertyName(obj.getClass());
        if (obj.getId() == null) {
            int lastIndex = getConfig().getInt(modelIndexingPropName, 0);
            lastIndex++;
            getConfig().setProperty(modelIndexingPropName, lastIndex);
            obj.setId(java.lang.Integer.toString(lastIndex));
        }
        java.lang.String modelPropName = getItemPropertyName(obj.getClass(), java.lang.Integer.parseInt(obj.getId()));
        java.lang.String json = serialize(obj);
        write(modelPropName, json);
    }

    @java.lang.Override
    public <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> T load(java.lang.Class<T> model, int id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound {
        java.lang.String modelPropName = getItemPropertyName(model, id);
        org.apache.ambari.view.pig.persistence.KeyValueStorage.LOG.debug(java.lang.String.format("Loading %s", modelPropName));
        if (getConfig().containsKey(modelPropName)) {
            java.lang.String json = read(modelPropName);
            org.apache.ambari.view.pig.persistence.KeyValueStorage.LOG.debug(java.lang.String.format("json: %s", json));
            return deserialize(model, json);
        } else {
            throw new org.apache.ambari.view.pig.persistence.utils.ItemNotFound();
        }
    }

    protected void write(java.lang.String modelPropName, java.lang.String json) {
        getConfig().setProperty(modelPropName, json);
    }

    protected java.lang.String read(java.lang.String modelPropName) {
        return getConfig().getString(modelPropName);
    }

    protected void clear(java.lang.String modelPropName) {
        getConfig().clearProperty(modelPropName);
    }

    protected java.lang.String serialize(org.apache.ambari.view.pig.persistence.utils.Indexed obj) {
        return gson.toJson(obj);
    }

    protected <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> T deserialize(java.lang.Class<T> model, java.lang.String json) {
        return gson.fromJson(json, model);
    }

    @java.lang.Override
    public synchronized <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model, org.apache.ambari.view.pig.persistence.utils.FilteringStrategy filter) {
        java.util.ArrayList<T> list = new java.util.ArrayList<T>();
        java.lang.String modelIndexingPropName = getIndexPropertyName(model);
        org.apache.ambari.view.pig.persistence.KeyValueStorage.LOG.debug(java.lang.String.format("Loading all %s-s", model.getName()));
        int lastIndex = getConfig().getInt(modelIndexingPropName, 0);
        for (int i = 1; i <= lastIndex; i++) {
            try {
                T item = load(model, i);
                if ((filter == null) || filter.isConform(item)) {
                    list.add(item);
                }
            } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound ignored) {
            }
        }
        return list;
    }

    @java.lang.Override
    public synchronized <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model) {
        return loadAll(model, new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername()));
    }

    @java.lang.Override
    public synchronized void delete(java.lang.Class model, int id) {
        org.apache.ambari.view.pig.persistence.KeyValueStorage.LOG.debug(java.lang.String.format("Deleting %s:%d", model.getName(), id));
        java.lang.String modelPropName = getItemPropertyName(model, id);
        clear(modelPropName);
    }

    @java.lang.Override
    public boolean exists(java.lang.Class model, int id) {
        return getConfig().containsKey(getItemPropertyName(model, id));
    }

    private java.lang.String getIndexPropertyName(java.lang.Class model) {
        return java.lang.String.format("%s:index", model.getName());
    }

    private java.lang.String getItemPropertyName(java.lang.Class model, int id) {
        return java.lang.String.format("%s.%d", model.getName(), id);
    }
}