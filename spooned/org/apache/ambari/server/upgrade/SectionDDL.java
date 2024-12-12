package org.apache.ambari.server.upgrade;
public interface SectionDDL {
    void execute(org.apache.ambari.server.orm.DBAccessor dbAccessor) throws java.sql.SQLException;

    void verify(org.apache.ambari.server.orm.DBAccessor dbAccessor) throws java.sql.SQLException;
}