package org.apache.ambari.server.controller.internal;
public abstract class JsonHttpPropertyRequest extends org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest.class);

    private static final java.lang.reflect.Type MAP_TYPE = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType();

    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    public JsonHttpPropertyRequest(java.util.Map<java.lang.String, java.lang.String> propertyMappings) {
        super(propertyMappings);
    }

    @java.lang.Override
    public void populateResource(org.apache.ambari.server.controller.spi.Resource resource, java.io.InputStream inputStream) throws org.apache.ambari.server.controller.spi.SystemException {
        try {
            java.util.Map<java.lang.String, java.lang.Object> responseMap = org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest.GSON.fromJson(org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8"), org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest.MAP_TYPE);
            if (responseMap == null) {
                org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest.LOG.error("Properties map from HTTP response is null");
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : getPropertyMappings().entrySet()) {
                java.lang.Object propertyValueToSet = getPropertyValue(responseMap, entry.getKey());
                resource.setProperty(entry.getValue(), propertyValueToSet);
            }
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Error setting properties.", e);
        }
    }

    private java.lang.Object getPropertyValue(java.util.Map<java.lang.String, java.lang.Object> responseMap, java.lang.String property) throws org.apache.ambari.server.controller.spi.SystemException {
        if ((property == null) || (responseMap == null)) {
            return null;
        }
        java.lang.Object result = responseMap;
        try {
            for (java.lang.String key : property.split("/")) {
                result = ((java.util.Map) (result)).get(key);
            }
        } catch (java.lang.ClassCastException e) {
            java.lang.String msg = java.lang.String.format("Error getting property value for %s.", property);
            org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest.LOG.error(msg, e);
            throw new org.apache.ambari.server.controller.spi.SystemException(msg, e);
        }
        return result;
    }
}