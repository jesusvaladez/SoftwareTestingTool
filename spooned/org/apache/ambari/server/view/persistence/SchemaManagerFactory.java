package org.apache.ambari.server.view.persistence;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
public interface SchemaManagerFactory {
    org.eclipse.persistence.tools.schemaframework.SchemaManager getSchemaManager(org.eclipse.persistence.sessions.DatabaseSession session);
}