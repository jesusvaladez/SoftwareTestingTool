package org.apache.ambari.view;
public interface DataStore {
    public void store(java.lang.Object entity) throws org.apache.ambari.view.PersistenceException;

    public void remove(java.lang.Object entity) throws org.apache.ambari.view.PersistenceException;

    public <T> T find(java.lang.Class<T> clazz, java.lang.Object primaryKey) throws org.apache.ambari.view.PersistenceException;

    public <T> java.util.Collection<T> findAll(java.lang.Class<T> clazz, java.lang.String whereClause) throws org.apache.ambari.view.PersistenceException;
}