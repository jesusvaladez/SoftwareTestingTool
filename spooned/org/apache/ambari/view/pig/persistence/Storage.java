package org.apache.ambari.view.pig.persistence;
public interface Storage {
    void store(org.apache.ambari.view.pig.persistence.utils.Indexed obj);

    <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> T load(java.lang.Class<T> model, int id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound;

    <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model, org.apache.ambari.view.pig.persistence.utils.FilteringStrategy filter);

    <T extends org.apache.ambari.view.pig.persistence.utils.Indexed> java.util.List<T> loadAll(java.lang.Class<T> model);

    void delete(java.lang.Class model, int id) throws org.apache.ambari.view.pig.persistence.utils.ItemNotFound;

    boolean exists(java.lang.Class model, int id);
}