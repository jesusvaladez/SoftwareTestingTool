package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
public abstract class AbstractKerberosDataFileWriter {
    private java.io.File file;

    private org.apache.commons.csv.CSVPrinter csvPrinter;

    public AbstractKerberosDataFileWriter(java.io.File file) throws java.io.IOException {
        this.file = file;
        open();
    }

    public void open() throws java.io.IOException {
        if (isClosed()) {
            if (file == null) {
                throw new java.io.IOException("Missing file path");
            } else {
                csvPrinter = new org.apache.commons.csv.CSVPrinter(new java.io.FileWriter(file, true), org.apache.commons.csv.CSVFormat.DEFAULT);
                if (file.length() == 0) {
                    java.lang.Iterable<?> headerRecord = getHeaderRecord();
                    csvPrinter.printRecord(headerRecord);
                }
            }
        }
    }

    public boolean isClosed() {
        return csvPrinter == null;
    }

    public void close() throws java.io.IOException {
        if (csvPrinter != null) {
            csvPrinter.close();
            csvPrinter = null;
        }
    }

    protected void appendRecord(java.lang.String... record) throws java.io.IOException {
        if (csvPrinter == null) {
            throw new java.io.IOException("Data file is not open");
        }
        csvPrinter.printRecord(record);
    }

    protected abstract java.lang.Iterable<java.lang.String> getHeaderRecord();
}