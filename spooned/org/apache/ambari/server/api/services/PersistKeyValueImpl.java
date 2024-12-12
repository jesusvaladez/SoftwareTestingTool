package org.apache.ambari.server.api.services;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
@com.google.inject.Singleton
public class PersistKeyValueImpl {
    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.KeyValueDAO keyValueDAO;

    public java.lang.String generateKey() {
        return java.util.UUID.randomUUID().toString();
    }

    public java.util.Collection<java.lang.String> generateKeys(int number) {
        java.util.List<java.lang.String> keys = new java.util.ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            keys.add(generateKey());
        }
        return keys;
    }

    public synchronized java.lang.String getValue(java.lang.String key) {
        org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity = keyValueDAO.findByKey(key);
        if (keyValueEntity != null) {
            return keyValueEntity.getValue();
        }
        throw new javax.ws.rs.WebApplicationException(Response.Status.NOT_FOUND);
    }

    public synchronized java.lang.String put(java.lang.String value) {
        java.lang.String key = generateKey();
        put(key, value);
        return key;
    }

    public synchronized void put(java.lang.String key, java.lang.String value) {
        org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity = keyValueDAO.findByKey(key);
        if (keyValueEntity != null) {
            keyValueEntity.setValue(value);
            keyValueDAO.merge(keyValueEntity);
        } else {
            keyValueEntity = new org.apache.ambari.server.orm.entities.KeyValueEntity();
            keyValueEntity.setKey(key);
            keyValueEntity.setValue(value);
            keyValueDAO.create(keyValueEntity);
        }
    }

    public synchronized java.util.Map<java.lang.String, java.lang.String> getAllKeyValues() {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity : keyValueDAO.findAll()) {
            map.put(keyValueEntity.getKey(), keyValueEntity.getValue());
        }
        return map;
    }
}