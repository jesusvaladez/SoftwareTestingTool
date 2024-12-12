package org.apache.ambari.log4j.hadoop.mapreduce.jobhistory;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.tools.rumen.Hadoop20JHParser;
import org.apache.hadoop.tools.rumen.JobHistoryParser;
import org.apache.hadoop.util.LineReader;
import org.apache.log4j.spi.LoggingEvent;
public class MapReduceJobHistoryParser implements org.apache.ambari.log4j.common.LogParser {
    private org.apache.hadoop.tools.rumen.JobHistoryParser parser;

    private org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryParser.LogLineReader reader = new org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryParser.LogLineReader("Meta VERSION=\"1\" .");

    public MapReduceJobHistoryParser() {
        try {
            parser = new org.apache.hadoop.tools.rumen.Hadoop20JHParser(reader);
        } catch (java.io.IOException ioe) {
            throw new java.lang.RuntimeException(ioe);
        }
    }

    @java.lang.Override
    public void addEventToParse(org.apache.log4j.spi.LoggingEvent event) {
        reader.addLine(event.getMessage().toString());
    }

    @java.lang.Override
    public java.lang.Object getParseResult() throws java.io.IOException {
        return parser.nextEvent();
    }

    static class LogLineReader extends org.apache.hadoop.util.LineReader {
        private java.util.Queue<java.lang.String> lines = new java.util.concurrent.LinkedBlockingQueue<java.lang.String>();

        public LogLineReader(java.lang.String line) {
            super(null);
            addLine(line);
        }

        private void addLine(java.lang.String line) {
            lines.add(line);
        }

        public int readLine(org.apache.hadoop.io.Text str) throws java.io.IOException {
            java.lang.String line = lines.poll();
            if (line != null) {
                str.set(line);
                return line.length();
            }
            return 0;
        }

        public void close() throws java.io.IOException {
        }
    }
}