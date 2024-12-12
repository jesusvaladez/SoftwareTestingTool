package org.apache.ambari.server.orm;
import javax.activation.DataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.queries.ContainerPolicy;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
public class EclipseLinkSessionCustomizer implements org.eclipse.persistence.config.SessionCustomizer {
    @java.lang.Override
    public void customize(org.eclipse.persistence.sessions.Session session) throws java.lang.Exception {
        org.eclipse.persistence.sessions.DatabaseLogin databaseLogin = ((org.eclipse.persistence.sessions.DatabaseLogin) (session.getDatasourceLogin()));
        databaseLogin.setTransactionIsolation(DatabaseLogin.TRANSACTION_READ_COMMITTED);
        java.lang.Object ddlGeneration = session.getProperty(PersistenceUnitProperties.DDL_GENERATION);
        if ((null == ddlGeneration) || PersistenceUnitProperties.NONE.equals(ddlGeneration)) {
            org.eclipse.persistence.internal.queries.ContainerPolicy.setDefaultContainerClass(java.util.ArrayList.class);
        }
    }
}