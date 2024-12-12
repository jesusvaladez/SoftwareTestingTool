package org.apache.ambari.server.serveraction.users;
public interface CollectionPersisterServiceFactory {
    org.apache.ambari.server.serveraction.users.CsvFilePersisterService createCsvFilePersisterService(java.lang.String csvFile);
}