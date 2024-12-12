package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractJDBCResourceProvider<E extends java.lang.Enum<E>> extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private final java.util.Map<java.lang.String, E> dbFields;

    protected AbstractJDBCResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
        super(propertyIds, keyPropertyIds);
        this.dbFields = getDBFieldMap();
    }

    protected abstract java.util.Map<java.lang.String, E> getDBFieldMap();

    protected void setString(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String propertyId, java.sql.ResultSet rs, java.util.Set<java.lang.String> requestedIds) throws java.sql.SQLException {
        if (requestedIds.contains(propertyId))
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, propertyId, rs.getString(dbFields.get(propertyId).toString()), requestedIds);

    }

    protected void setInt(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String propertyId, java.sql.ResultSet rs, java.util.Set<java.lang.String> requestedIds) throws java.sql.SQLException {
        if (requestedIds.contains(propertyId))
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, propertyId, rs.getInt(dbFields.get(propertyId).toString()), requestedIds);

    }

    protected void setLong(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String propertyId, java.sql.ResultSet rs, java.util.Set<java.lang.String> requestedIds) throws java.sql.SQLException {
        if (requestedIds.contains(propertyId))
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, propertyId, rs.getLong(dbFields.get(propertyId).toString()), requestedIds);

    }

    protected java.lang.String getDBFieldString(java.util.Set<java.lang.String> requestedIds) {
        java.lang.String[] tmp = new java.lang.String[requestedIds.size()];
        int i = 0;
        for (java.lang.String s : requestedIds)
            if (dbFields.containsKey(s))
                tmp[i++] = dbFields.get(s).toString();


        return org.apache.commons.lang.StringUtils.join(tmp, ",", 0, i);
    }

    protected E getDBField(java.lang.String propertyId) {
        return dbFields.get(propertyId);
    }
}