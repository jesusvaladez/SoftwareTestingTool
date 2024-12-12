package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
public abstract class AbstractKerberosDataFileReader implements java.lang.Iterable<java.util.Map<java.lang.String, java.lang.String>> {
    private java.io.File file;

    private org.apache.commons.csv.CSVParser csvParser = null;

    protected AbstractKerberosDataFileReader(java.io.File file) throws java.io.IOException {
        this.file = file;
        open();
    }

    public void open() throws java.io.IOException {
        if (isClosed()) {
            csvParser = org.apache.commons.csv.CSVParser.parse(file, java.nio.charset.Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
        }
    }

    public boolean isClosed() {
        return csvParser == null;
    }

    public void close() throws java.io.IOException {
        if (csvParser != null) {
            csvParser.close();
            csvParser = null;
        }
    }

    @java.lang.Override
    public java.util.Iterator<java.util.Map<java.lang.String, java.lang.String>> iterator() {
        return new java.util.Iterator<java.util.Map<java.lang.String, java.lang.String>>() {
            java.util.Iterator<org.apache.commons.csv.CSVRecord> iterator = (csvParser == null) ? null : csvParser.iterator();

            @java.lang.Override
            public boolean hasNext() {
                return (iterator != null) && iterator.hasNext();
            }

            @java.lang.Override
            public java.util.Map<java.lang.String, java.lang.String> next() {
                return iterator == null ? null : iterator.next().toMap();
            }

            @java.lang.Override
            public void remove() {
                if (iterator != null) {
                    iterator.remove();
                }
            }
        };
    }
}