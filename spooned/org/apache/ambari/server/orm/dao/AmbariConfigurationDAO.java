package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.StringUtils;
@javax.inject.Singleton
public class AmbariConfigurationDAO extends org.apache.ambari.server.orm.dao.CrudDAO<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity, org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);

    @javax.inject.Inject
    public AmbariConfigurationDAO() {
        super(org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> findByCategory(java.lang.String categoryName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> query = entityManagerProvider.get().createNamedQuery("AmbariConfigurationEntity.findByCategory", org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class);
        query.setParameter("categoryName", categoryName);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public int removeByCategory(java.lang.String categoryName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> query = entityManagerProvider.get().createNamedQuery("AmbariConfigurationEntity.deleteByCategory", org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class);
        query.setParameter("categoryName", categoryName);
        return query.executeUpdate();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity) {
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntity foundEntity = findByPK(new org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK(entity.getCategoryName(), entity.getPropertyName()));
        if (foundEntity != null) {
            java.lang.String message = java.lang.String.format("Only one configuration entry can exist for the category %s and name %s", entity.getCategoryName(), entity.getPropertyName());
            org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.LOGGER.error(message);
            throw new javax.persistence.EntityExistsException(message);
        }
        super.create(entity);
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.AmbariConfigurationEntity merge(org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity) {
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntity foundEntity = findByPK(new org.apache.ambari.server.orm.entities.AmbariConfigurationEntityPK(entity.getCategoryName(), entity.getPropertyName()));
        if (foundEntity == null) {
            java.lang.String message = java.lang.String.format("The configuration entry for the category %s and name %s does not exist", entity.getCategoryName(), entity.getPropertyName());
            org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.LOGGER.debug(message);
            throw new javax.persistence.EntityNotFoundException(message);
        }
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntity updatedEntity = entity;
        if (!org.apache.commons.lang.StringUtils.equals(foundEntity.getPropertyValue(), entity.getPropertyValue())) {
            updatedEntity = super.merge(entity);
            entityManagerProvider.get().flush();
        }
        return updatedEntity;
    }

    @com.google.inject.persist.Transactional
    public boolean reconcileCategory(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean removeIfNotProvided) {
        boolean changesDetected = false;
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> existingEntities = findByCategory(categoryName);
        java.util.Map<java.lang.String, java.lang.String> propertiesToProcess = new java.util.HashMap<>();
        if (properties != null) {
            propertiesToProcess.putAll(properties);
        }
        if (existingEntities != null) {
            for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : existingEntities) {
                java.lang.String propertyName = entity.getPropertyName();
                if (propertiesToProcess.containsKey(propertyName)) {
                    java.lang.String newPropertyValue = propertiesToProcess.get(propertyName);
                    if (!org.apache.commons.lang.StringUtils.equals(newPropertyValue, entity.getPropertyValue())) {
                        entity.setPropertyValue(newPropertyValue);
                        merge(entity);
                        changesDetected = true;
                    }
                } else if (removeIfNotProvided) {
                    remove(entity);
                    changesDetected = true;
                }
                propertiesToProcess.remove(propertyName);
            }
        }
        if (!propertiesToProcess.isEmpty()) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : propertiesToProcess.entrySet()) {
                org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
                entity.setCategoryName(categoryName);
                entity.setPropertyName(property.getKey());
                entity.setPropertyValue(property.getValue());
                create(entity);
            }
            changesDetected = true;
        }
        if (changesDetected) {
            entityManagerProvider.get().flush();
        }
        return changesDetected;
    }
}