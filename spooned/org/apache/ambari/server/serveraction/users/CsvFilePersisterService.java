package org.apache.ambari.server.serveraction.users;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
public class CsvFilePersisterService implements org.apache.ambari.server.serveraction.users.CollectionPersisterService<java.lang.String, java.util.List<java.lang.String>> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.users.CsvFilePersisterService.class);

    private java.lang.String NEW_LINE_SEPARATOR = "\n";

    private java.lang.String csvFile;

    private org.apache.commons.csv.CSVPrinter csvPrinter;

    private java.io.FileWriter fileWriter;

    @com.google.inject.assistedinject.AssistedInject
    public CsvFilePersisterService(@com.google.inject.assistedinject.Assisted
    java.lang.String csvFile) {
        this.csvFile = csvFile;
    }

    public java.util.Set<java.nio.file.attribute.PosixFilePermission> getCsvPermissions() {
        java.util.Set<java.nio.file.attribute.PosixFilePermission> permissionsSet = new java.util.HashSet<>();
        permissionsSet.add(java.nio.file.attribute.PosixFilePermission.OWNER_READ);
        permissionsSet.add(java.nio.file.attribute.PosixFilePermission.OWNER_WRITE);
        permissionsSet.add(java.nio.file.attribute.PosixFilePermission.GROUP_READ);
        permissionsSet.add(java.nio.file.attribute.PosixFilePermission.OTHERS_READ);
        return permissionsSet;
    }

    @javax.inject.Inject
    public void init() throws java.io.IOException {
        java.nio.file.Path csv = java.nio.file.Files.createFile(java.nio.file.Paths.get(csvFile));
        java.nio.file.Files.setPosixFilePermissions(java.nio.file.Paths.get(csvFile), getCsvPermissions());
        fileWriter = new java.io.FileWriter(csv.toFile());
        csvPrinter = new org.apache.commons.csv.CSVPrinter(fileWriter, CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR));
    }

    @java.lang.Override
    public boolean persist(java.util.Collection<java.util.List<java.lang.String>> collectionData) {
        try {
            org.apache.ambari.server.serveraction.users.CsvFilePersisterService.LOGGER.info("Persisting collection to csv file");
            csvPrinter.printRecords(collectionData);
            org.apache.ambari.server.serveraction.users.CsvFilePersisterService.LOGGER.info("Collection successfully persisted to csv file.");
            return true;
        } catch (java.io.IOException e) {
            org.apache.ambari.server.serveraction.users.CsvFilePersisterService.LOGGER.error("Failed to persist the collection to csv file", e);
            return false;
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvPrinter.close();
            } catch (java.io.IOException e) {
                org.apache.ambari.server.serveraction.users.CsvFilePersisterService.LOGGER.error("Error while flushing/closing fileWriter/csvPrinter", e);
            }
        }
    }

    @java.lang.Override
    public boolean persistMap(java.util.Map<java.lang.String, java.util.List<java.lang.String>> mapData) {
        org.apache.ambari.server.serveraction.users.CsvFilePersisterService.LOGGER.info("Persisting map data to csv file");
        java.util.Collection<java.util.List<java.lang.String>> collectionData = new java.util.ArrayList<>();
        for (java.lang.String key : mapData.keySet()) {
            java.util.List<java.lang.String> record = new java.util.ArrayList<>();
            record.add(key);
            record.addAll(mapData.get(key));
            collectionData.add(record);
        }
        return persist(collectionData);
    }
}