package org.apache.ambari.server.controller.jdbc;
public class JDBCResourceProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.ResourceProvider {
    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory;

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> importedKeys = new java.util.HashMap<>();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.class);

    public JDBCResourceProvider(org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory, org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
        super(propertyIds);
        this.connectionFactory = connectionFactory;
        this.type = type;
        this.keyPropertyIds = keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_id"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "disk_info"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "public_host_name"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "last_registration_time"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_state"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "last_heartbeat_time"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_health_report"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_status"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "desired_configs"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "desired_configs"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "configs"));
        propertyIds.remove(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "desired_configs"));
        java.sql.Connection connection = null;
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            connection = connectionFactory.getConnection();
            for (java.lang.String table : org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.getTables(propertyIds)) {
                getImportedKeys(connection, table);
            }
            java.lang.String sql = getSelectSQL(propertyIds, predicate);
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                java.sql.ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                final org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
                for (int i = 1; i <= columnCount; ++i) {
                    java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(metaData.getTableName(i), metaData.getColumnName(i));
                    if (propertyIds.contains(propertyId)) {
                        resource.setProperty(propertyId, rs.getString(i));
                    }
                }
                resources.add(resource);
            } 
            statement.close();
        } catch (java.sql.SQLException e) {
            if (org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.LOG.debug("Caught exception getting resource.", e);
            }
            return java.util.Collections.emptySet();
        } finally {
            try {
                if (rs != null)
                    rs.close();

            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.LOG.error("Exception while closing ResultSet", e);
            }
            try {
                if (statement != null)
                    statement.close();

            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.LOG.error("Exception while closing statment", e);
            }
            try {
                if (connection != null)
                    connection.close();

            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.controller.jdbc.JDBCResourceProvider.LOG.error("Exception while closing statment", e);
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.sql.Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            java.sql.Statement statement = null;
            try {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = request.getProperties();
                statement = connection.createStatement();
                for (java.util.Map<java.lang.String, java.lang.Object> properties : propertySet) {
                    java.lang.String sql = getInsertSQL(properties);
                    statement.execute(sql);
                }
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (java.sql.SQLException e) {
            throw new java.lang.IllegalStateException("DB error : ", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException ex) {
                    throw new java.lang.IllegalStateException("DB error : ", ex);
                }
            }
        }
        return getRequestStatus();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.sql.Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            java.sql.Statement statement = null;
            try {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = request.getProperties();
                java.util.Map<java.lang.String, java.lang.Object> properties = propertySet.iterator().next();
                java.lang.String sql = getUpdateSQL(properties, predicate);
                statement = connection.createStatement();
                statement.execute(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (java.sql.SQLException e) {
            throw new java.lang.IllegalStateException("DB error : ", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException ex) {
                    throw new java.lang.IllegalStateException("DB error : ", ex);
                }
            }
        }
        return getRequestStatus();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.sql.Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            java.sql.Statement statement = null;
            try {
                java.lang.String sql = getDeleteSQL(predicate);
                statement = connection.createStatement();
                statement.execute(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (java.sql.SQLException e) {
            throw new java.lang.IllegalStateException("DB error : ", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (java.sql.SQLException ex) {
                    throw new java.lang.IllegalStateException("DB error : ", ex);
                }
            }
        }
        return getRequestStatus();
    }

    private java.lang.String getInsertSQL(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.StringBuilder columns = new java.lang.StringBuilder();
        java.lang.StringBuilder values = new java.lang.StringBuilder();
        java.lang.String table = null;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String propertyId = entry.getKey();
            java.lang.Object propertyValue = entry.getValue();
            table = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
            if (columns.length() > 0) {
                columns.append(", ");
            }
            columns.append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyId));
            if (values.length() > 0) {
                values.append(", ");
            }
            values.append("'");
            values.append(propertyValue);
            values.append("'");
        }
        return ((((("insert into " + table) + " (") + columns) + ") values (") + values) + ")";
    }

    private java.lang.String getSelectSQL(java.util.Set<java.lang.String> propertyIds, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.lang.StringBuilder columns = new java.lang.StringBuilder();
        java.util.Set<java.lang.String> tableSet = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if (columns.length() > 0) {
                columns.append(", ");
            }
            java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
            columns.append(propertyCategory).append(".").append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyId));
            tableSet.add(propertyCategory);
        }
        boolean haveWhereClause = false;
        java.lang.StringBuilder whereClause = new java.lang.StringBuilder();
        if (((predicate != null) && propertyIds.containsAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate))) && (predicate instanceof org.apache.ambari.server.controller.predicate.PredicateVisitorAcceptor)) {
            org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor visitor = new org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor();
            ((org.apache.ambari.server.controller.predicate.PredicateVisitorAcceptor) (predicate)).accept(visitor);
            whereClause.append(visitor.getSQL());
            haveWhereClause = true;
        }
        java.lang.StringBuilder joinClause = new java.lang.StringBuilder();
        if (tableSet.size() > 1) {
            for (java.lang.String table : tableSet) {
                java.util.Map<java.lang.String, java.lang.String> joinKeys = importedKeys.get(table);
                if (joinKeys != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : joinKeys.entrySet()) {
                        java.lang.String category1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
                        java.lang.String category2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getValue());
                        if (tableSet.contains(category1) && tableSet.contains(category2)) {
                            if (haveWhereClause) {
                                joinClause.append(" AND ");
                            }
                            joinClause.append(category1).append(".").append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()));
                            joinClause.append(" = ");
                            joinClause.append(category2).append(".").append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getValue()));
                            tableSet.add(category1);
                            tableSet.add(category2);
                            haveWhereClause = true;
                        }
                    }
                }
            }
        }
        java.lang.StringBuilder tables = new java.lang.StringBuilder();
        for (java.lang.String table : tableSet) {
            if (tables.length() > 0) {
                tables.append(", ");
            }
            tables.append(table);
        }
        java.lang.String sql = (("select " + columns) + " from ") + tables;
        if (haveWhereClause) {
            sql = ((sql + " where ") + whereClause) + joinClause;
        }
        return sql;
    }

    private java.lang.String getDeleteSQL(org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.lang.StringBuilder whereClause = new java.lang.StringBuilder();
        if (predicate instanceof org.apache.ambari.server.controller.predicate.BasePredicate) {
            org.apache.ambari.server.controller.predicate.BasePredicate basePredicate = ((org.apache.ambari.server.controller.predicate.BasePredicate) (predicate));
            org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor visitor = new org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor();
            basePredicate.accept(visitor);
            whereClause.append(visitor.getSQL());
            java.lang.String table = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(basePredicate.getPropertyIds().iterator().next());
            return (("delete from " + table) + " where ") + whereClause;
        }
        throw new java.lang.IllegalStateException("Can't generate SQL.");
    }

    private java.lang.String getUpdateSQL(java.util.Map<java.lang.String, java.lang.Object> properties, org.apache.ambari.server.controller.spi.Predicate predicate) {
        if (predicate instanceof org.apache.ambari.server.controller.predicate.BasePredicate) {
            java.lang.StringBuilder whereClause = new java.lang.StringBuilder();
            org.apache.ambari.server.controller.predicate.BasePredicate basePredicate = ((org.apache.ambari.server.controller.predicate.BasePredicate) (predicate));
            org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor visitor = new org.apache.ambari.server.controller.jdbc.SQLPredicateVisitor();
            basePredicate.accept(visitor);
            whereClause.append(visitor.getSQL());
            java.lang.String table = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(basePredicate.getPropertyIds().iterator().next());
            java.lang.StringBuilder setClause = new java.lang.StringBuilder();
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()));
                setClause.append(" = ");
                setClause.append("'");
                setClause.append(entry.getValue());
                setClause.append("'");
            }
            return (((("update " + table) + " set ") + setClause) + " where ") + whereClause;
        }
        throw new java.lang.IllegalStateException("Can't generate SQL.");
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return keyPropertyIds;
    }

    private void getImportedKeys(java.sql.Connection connection, java.lang.String table) throws java.sql.SQLException {
        if (!this.importedKeys.containsKey(table)) {
            java.util.Map<java.lang.String, java.lang.String> importedKeys = new java.util.HashMap<>();
            this.importedKeys.put(table, importedKeys);
            java.sql.DatabaseMetaData metaData = connection.getMetaData();
            java.sql.ResultSet rs = null;
            try {
                rs = metaData.getImportedKeys(connection.getCatalog(), null, table);
                while (rs.next()) {
                    java.lang.String pkPropertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME"));
                    java.lang.String fkPropertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(rs.getString("FKTABLE_NAME"), rs.getString("FKCOLUMN_NAME"));
                    importedKeys.put(pkPropertyId, fkPropertyId);
                } 
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        }
    }

    private org.apache.ambari.server.controller.spi.RequestStatus getRequestStatus() {
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    private static java.util.Set<java.lang.String> getTables(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> tables = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            tables.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId));
        }
        return tables;
    }
}