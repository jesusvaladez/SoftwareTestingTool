package org.apache.ambari.server.serveraction.users;
public interface CollectionPersisterService<K, V> {
    boolean persist(java.util.Collection<V> collectionData);

    boolean persistMap(java.util.Map<K, V> mapData);
}