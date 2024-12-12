package org.apache.ambari.server.state.alert;
@com.google.inject.Singleton
public class AlertDefinitionFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.alert.AlertDefinitionFactory.class);

    private final com.google.gson.GsonBuilder m_builder = new com.google.gson.GsonBuilder();

    private final com.google.gson.Gson m_gson;

    public AlertDefinitionFactory() {
        m_builder.registerTypeAdapter(org.apache.ambari.server.state.alert.Source.class, new org.apache.ambari.server.state.alert.AlertDefinitionFactory.AlertDefinitionSourceAdapter());
        m_gson = m_builder.create();
    }

    public java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions(java.io.File alertDefinitionFile, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        try {
            java.io.FileReader fileReader = new java.io.FileReader(alertDefinitionFile);
            return getAlertDefinitions(fileReader, serviceName);
        } catch (java.io.IOException ioe) {
            java.lang.String message = "Could not read the alert definition file";
            org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.error(message, ioe);
            throw new org.apache.ambari.server.AmbariException(message, ioe);
        }
    }

    public java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions(java.io.Reader reader, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.alert.AlertDefinition>>> serviceDefinitionMap = null;
        try {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.alert.AlertDefinition>>>>() {}.getType();
            serviceDefinitionMap = m_gson.fromJson(reader, type);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.error("Could not read the alert definitions", e);
            throw new org.apache.ambari.server.AmbariException("Could not read alert definitions", e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(reader);
        }
        java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> definitions = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.alert.AlertDefinition>> definitionMap = serviceDefinitionMap.get(serviceName);
        if (null == definitionMap) {
            return definitions;
        }
        for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.state.alert.AlertDefinition>> entry : definitionMap.entrySet()) {
            for (org.apache.ambari.server.state.alert.AlertDefinition ad : entry.getValue()) {
                ad.setServiceName(serviceName);
                if (!entry.getKey().equals("service")) {
                    ad.setComponentName(entry.getKey());
                }
            }
            definitions.addAll(entry.getValue());
        }
        return definitions;
    }

    public org.apache.ambari.server.state.alert.AlertDefinition coerce(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) {
        if (null == entity) {
            return null;
        }
        org.apache.ambari.server.state.alert.AlertDefinition definition = new org.apache.ambari.server.state.alert.AlertDefinition();
        definition.setClusterId(entity.getClusterId());
        definition.setDefinitionId(entity.getDefinitionId());
        definition.setComponentName(entity.getComponentName());
        definition.setEnabled(entity.getEnabled());
        definition.setHostIgnored(entity.isHostIgnored());
        definition.setInterval(entity.getScheduleInterval());
        definition.setName(entity.getDefinitionName());
        definition.setScope(entity.getScope());
        definition.setServiceName(entity.getServiceName());
        definition.setLabel(entity.getLabel());
        definition.setHelpURL(entity.getHelpURL());
        definition.setDescription(entity.getDescription());
        definition.setUuid(entity.getHash());
        definition.setRepeatTolerance(entity.getRepeatTolerance());
        definition.setRepeatToleranceEnabled(entity.isRepeatToleranceEnabled());
        try {
            java.lang.String sourceJson = entity.getSource();
            org.apache.ambari.server.state.alert.Source source = m_gson.fromJson(sourceJson, org.apache.ambari.server.state.alert.Source.class);
            definition.setSource(source);
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.error((("Alert defintion is invalid for  Id : " + entity.getDefinitionId()) + " Name: ") + entity.getDefinitionName());
            org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.error("Unable to deserialize the alert definition source during coercion", exception);
            return null;
        }
        return definition;
    }

    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity coerce(long clusterId, org.apache.ambari.server.state.alert.AlertDefinition definition) {
        if (null == definition) {
            return null;
        }
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        entity.setClusterId(clusterId);
        return merge(definition, entity);
    }

    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity merge(org.apache.ambari.server.state.alert.AlertDefinition definition, org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) {
        entity.setComponentName(definition.getComponentName());
        entity.setDefinitionName(definition.getName());
        entity.setEnabled(definition.isEnabled());
        entity.setHostIgnored(definition.isHostIgnored());
        entity.setLabel(definition.getLabel());
        entity.setDescription(definition.getDescription());
        entity.setScheduleInterval(definition.getInterval());
        entity.setHelpURL(definition.getHelpURL());
        entity.setServiceName(definition.getServiceName());
        org.apache.ambari.server.state.alert.Scope scope = definition.getScope();
        if (null == scope) {
            scope = org.apache.ambari.server.state.alert.Scope.ANY;
        }
        entity.setScope(scope);
        return mergeSource(definition.getSource(), entity);
    }

    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity mergeSource(org.apache.ambari.server.state.alert.Source source, org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) {
        entity.setSourceType(source.getType());
        try {
            java.lang.String sourceJson = m_gson.toJson(source);
            entity.setSource(sourceJson);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.error("Unable to serialize the alert definition source during merge", e);
            return null;
        }
        org.apache.ambari.server.state.alert.AlertDefinitionFactory.assignNewUUID(entity);
        return entity;
    }

    private static void assignNewUUID(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) {
        if (entity != null) {
            entity.setHash(java.util.UUID.randomUUID().toString());
        }
    }

    public com.google.gson.Gson getGson() {
        return m_gson;
    }

    private static final class AlertDefinitionSourceAdapter implements com.google.gson.JsonDeserializer<org.apache.ambari.server.state.alert.Source> {
        @java.lang.Override
        public org.apache.ambari.server.state.alert.Source deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            com.google.gson.JsonObject jsonObj = ((com.google.gson.JsonObject) (json));
            org.apache.ambari.server.state.alert.SourceType type = org.apache.ambari.server.state.alert.SourceType.valueOf(jsonObj.get("type").getAsString());
            java.lang.Class<? extends org.apache.ambari.server.state.alert.Source> clazz = null;
            switch (type) {
                case METRIC :
                    {
                        clazz = org.apache.ambari.server.state.alert.MetricSource.class;
                        break;
                    }
                case AMS :
                    {
                        clazz = org.apache.ambari.server.state.alert.AmsSource.class;
                        break;
                    }
                case PORT :
                    {
                        clazz = org.apache.ambari.server.state.alert.PortSource.class;
                        break;
                    }
                case SCRIPT :
                    {
                        clazz = org.apache.ambari.server.state.alert.ScriptSource.class;
                        break;
                    }
                case AGGREGATE :
                    {
                        clazz = org.apache.ambari.server.state.alert.AggregateSource.class;
                        break;
                    }
                case PERCENT :
                    {
                        clazz = org.apache.ambari.server.state.alert.PercentSource.class;
                        break;
                    }
                case WEB :
                    {
                        clazz = org.apache.ambari.server.state.alert.WebSource.class;
                        break;
                    }
                case RECOVERY :
                    {
                        clazz = org.apache.ambari.server.state.alert.RecoverySource.class;
                        break;
                    }
                case SERVER :
                    {
                        clazz = org.apache.ambari.server.state.alert.ServerSource.class;
                        break;
                    }
                default :
                    break;
            }
            if (null == clazz) {
                org.apache.ambari.server.state.alert.AlertDefinitionFactory.LOG.warn("Unable to deserialize an alert definition with source type {}", type);
                return null;
            }
            return context.deserialize(json, clazz);
        }
    }
}